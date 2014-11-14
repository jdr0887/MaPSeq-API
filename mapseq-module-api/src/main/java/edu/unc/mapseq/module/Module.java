package edu.unc.mapseq.module;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.renci.common.exec.BashExecutor;
import org.renci.common.exec.CommandInput;
import org.renci.common.exec.CommandOutput;
import org.renci.common.exec.Executor;
import org.renci.common.exec.ExecutorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.model.Attribute;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.module.annotations.Executable;
import edu.unc.mapseq.module.annotations.InputArgument;
import edu.unc.mapseq.module.annotations.OutputArgument;

public abstract class Module implements Callable<ModuleOutput> {

    private final Logger logger = LoggerFactory.getLogger(Module.class);

    private File serializeFile;

    private Long workflowRunAttemptId;

    private Long sampleId;

    private String workflowName = "TEST";

    private Boolean dryRun = Boolean.FALSE;

    private Boolean persistFileData = Boolean.FALSE;

    private Set<FileData> fileDatas;

    private Set<Attribute> attributes;

    public Module() {
        super();
        this.fileDatas = new HashSet<FileData>();
        this.attributes = new HashSet<Attribute>();
    }

    public abstract Class<?> getModuleClass();

    public String getExecutable() {
        if (getModuleClass().isAnnotationPresent(Executable.class)) {
            return getModuleClass().getAnnotation(Executable.class).value();
        }
        return "";
    }

    @Override
    public ModuleOutput call() throws Exception {
        logger.info("ENTERING call()");

        Class<?> moduleClass = getModuleClass();
        if (moduleClass.isAnnotationPresent(Executable.class)) {

            Executable executable = moduleClass.getAnnotation(Executable.class);

            CommandInput commandInput = new CommandInput();
            if (executable.exitImmediately()) {
                commandInput.setExitImmediately(true);
            }

            String mapseqHome = System.getenv("MAPSEQ_HOME");
            if (StringUtils.isNotEmpty(mapseqHome)) {
                File tmpDir = new File(mapseqHome, "tmp");
                File tmpWorkflowDir = new File(tmpDir, getWorkflowName());
                File tmpWorkflowAttemptDir = new File(tmpWorkflowDir, getWorkflowRunAttemptId().toString());
                tmpWorkflowAttemptDir.mkdirs();
                commandInput.setWorkDir(tmpWorkflowAttemptDir);
            }

            StringBuilder command = new StringBuilder();
            command.append(getExecutable());

            Field[] fieldArray = moduleClass.getDeclaredFields();
            List<Field> inputArgumentFieldList = new ArrayList<Field>();

            for (Field field : fieldArray) {
                if (field.isAnnotationPresent(InputArgument.class)) {
                    inputArgumentFieldList.add(field);
                }
            }

            List<Field> outputArgumentFieldList = new ArrayList<Field>();

            for (Field field : fieldArray) {
                if (field.isAnnotationPresent(OutputArgument.class)) {
                    outputArgumentFieldList.add(field);
                }
            }

            Collections.sort(inputArgumentFieldList, new Comparator<Field>() {

                @Override
                public int compare(Field o1, Field o2) {
                    if (o1.isAnnotationPresent(InputArgument.class) && o2.isAnnotationPresent(InputArgument.class)) {
                        Integer order1 = o1.getAnnotation(InputArgument.class).order();
                        Integer order2 = o2.getAnnotation(InputArgument.class).order();
                        return order1.compareTo(order2);
                    }
                    return 0;
                }

            });

            for (Field field : inputArgumentFieldList) {

                InputArgument arg = field.getAnnotation(InputArgument.class);
                String getterMethodName = "get" + StringUtils.capitalize(field.getName());
                Method getterMethod = this.getClass().getDeclaredMethod(getterMethodName, null);
                Object o = getterMethod.invoke(this, (Object[]) null);
                if (o != null) {

                    if (field.getType() == File.class) {
                        File f = (File) o;
                        command.append(" ").append(arg.flag()).append(arg.delimiter()).append(f.getAbsolutePath());
                    } else if (field.getType() == Boolean.class) {
                        Boolean b = (Boolean) o;
                        if (b) {
                            command.append(" ").append(arg.flag());
                        }
                    } else if (field.getType() == List.class) {

                        ParameterizedType listType = (ParameterizedType) field.getGenericType();
                        if (listType != null) {
                            Class<?> listTypeClass = (Class<?>) listType.getActualTypeArguments()[0];
                            if (listTypeClass == File.class) {
                                List<File> fileList = (List<File>) o;
                                for (File file : fileList) {
                                    command.append(" ").append(arg.flag()).append(arg.delimiter())
                                            .append(file.getAbsolutePath());
                                }
                            } else {
                                List<?> list = (List<?>) o;
                                for (int i = 0; i < list.size(); ++i) {
                                    String val = list.get(i).toString();
                                    command.append(" ").append(arg.flag()).append(arg.delimiter()).append(val);
                                }

                            }
                        }

                    } else if (field.getType() == String.class || field.getType().isEnum()) {
                        if (StringUtils.isNotEmpty(o.toString())) {
                            command.append(" ").append(arg.flag()).append(arg.delimiter()).append(o.toString());
                        }
                    } else if (field.getType() == Integer.class || field.getType() == Double.class
                            || field.getType() == Float.class) {
                        command.append(" ").append(arg.flag()).append(arg.delimiter()).append(o.toString());
                    }
                }
            }

            for (Field field : outputArgumentFieldList) {
                OutputArgument arg = field.getAnnotation(OutputArgument.class);
                String getterMethodName = "get" + StringUtils.capitalize(field.getName());
                Method getterMethod = this.getClass().getDeclaredMethod(getterMethodName, null);
                Object o = getterMethod.invoke(this, (Object[]) null);
                if (field.isAnnotationPresent(NotNull.class)) {
                    // we have a required field
                    if (field.getType() == File.class) {
                        File f = (File) o;
                        if (arg.redirect()) {
                            command.append(" > ").append(f.getAbsolutePath());
                        } else {
                            command.append(" ").append(arg.flag()).append(arg.delimiter()).append(f.getAbsolutePath());
                        }
                    } else {
                        command.append(" ").append(arg.flag()).append(arg.delimiter()).append(o.toString());
                    }
                } else {
                    if (o != null) {
                        if (field.getType() == File.class) {
                            File f = (File) o;
                            if (arg.redirect()) {
                                command.append(" > ").append(f.getAbsolutePath());
                            } else {
                                command.append(" ").append(arg.flag()).append(arg.delimiter())
                                        .append(f.getAbsolutePath());
                            }
                        }
                    }
                }
            }

            logger.info("command.toString(): {}", command.toString());
            System.out.println(command.toString());

            if (dryRun) {
                return new DefaultModuleOutput();
            }

            commandInput.setCommand(command.toString());
            CommandOutput commandOutput;
            Executor executor = BashExecutor.getInstance();
            try {
                commandOutput = executor.execute(commandInput, new File(System.getProperty("user.home"), ".mapseqrc"));
            } catch (ExecutorException e) {
                throw new ModuleException(e);
            }
            if (commandOutput != null && commandOutput.getExitCode() == 0) {
                for (Field field : outputArgumentFieldList) {
                    OutputArgument arg = field.getAnnotation(OutputArgument.class);
                    String getterMethodName = "get" + StringUtils.capitalize(field.getName());
                    Method getterMethod = this.getClass().getDeclaredMethod(getterMethodName, null);
                    Object o = getterMethod.invoke(this, (Object[]) null);
                    if (field.getType() == File.class && arg.persistFileData()) {
                        File f = (File) o;
                        FileData fileData = new FileData();
                        fileData.setMimeType(arg.mimeType());
                        fileData.setName(f.getName());
                        getFileDatas().add(fileData);
                    }
                }
            }
            return new ShellModuleOutput(commandOutput);
        }

        return null;
    }

    public Set<FileData> getFileDatas() {
        return this.fileDatas;
    }

    public Boolean getDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public Boolean getPersistFileData() {
        return persistFileData;
    }

    public void setPersistFileData(Boolean persistFileData) {
        this.persistFileData = persistFileData;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public File getSerializeFile() {
        return serializeFile;
    }

    public void setSerializeFile(File serializeFile) {
        this.serializeFile = serializeFile;
    }

    public Long getWorkflowRunAttemptId() {
        return workflowRunAttemptId;
    }

    public void setWorkflowRunAttemptId(Long workflowRunAttemptId) {
        this.workflowRunAttemptId = workflowRunAttemptId;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void setFileDatas(Set<FileData> fileDatas) {
        this.fileDatas = fileDatas;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

}
