package edu.unc.mapseq.module;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.JobDAO;
import edu.unc.mapseq.dao.MaPSeqDAOBeanService;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Attribute;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.JobStatusType;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.Workflow;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;

/**
 * 
 * @author jdr0887
 */
public class ModuleExecutor extends Observable implements Callable<ModuleOutput> {

    private final Logger logger = LoggerFactory.getLogger(ModuleExecutor.class);

    private Job job;

    private Module module;

    private MaPSeqDAOBeanService daoBean;

    public ModuleExecutor() {
        super();
    }

    @Override
    public ModuleOutput call() throws ModuleException {
        logger.debug("ENTERING run(Module)");
        ModuleOutput output = null;

        WorkflowRunAttempt workflowRunAttempt = null;

        if (!module.getDryRun() && module.getWorkflowRunAttemptId() != null) {
            try {
                workflowRunAttempt = daoBean.getWorkflowRunAttemptDAO().findById(module.getWorkflowRunAttemptId());
                WorkflowRun workflowRun = workflowRunAttempt.getWorkflowRun();
                module.setWorkflowName(workflowRun.getWorkflow().getName());
            } catch (MaPSeqDAOException e) {
                e.printStackTrace();
            }
        }

        this.job = new Job();
        this.job.setName(module.getModuleClass().getName());
        this.job.setStarted(new Date());

        if (workflowRunAttempt != null) {
            this.job.setWorkflowRunAttempt(workflowRunAttempt);
        }

        Set<Attribute> jobAttributeSet = job.getAttributes();
        jobAttributeSet.addAll(createJobAttributes());
        job.setAttributes(jobAttributeSet);

        if (!module.getDryRun()) {
            try {
                JobDAO jobDAO = daoBean.getJobDAO();
                this.job.setId(jobDAO.save(this.job));
            } catch (MaPSeqDAOException e) {
                e.printStackTrace();
            }
        }

        try {
            if (module.getValidate()) {
                List<String> inputErrors = module.validateInputs();
                if (CollectionUtils.isNotEmpty(inputErrors)) {
                    String message = StringUtils.join(inputErrors, System.getProperty("line.separator"));
                    throw new ModuleException(message);
                }
            }

            updateJobState(JobStatusType.RUNNING);

            logger.info(module.toString());
            output = module.call();

            if (output == null) {
                throw new ModuleException("ModuleOutput is null");
            }

            logger.info(output.toString());

            if (output.getError() != null && output.getError().length() > 0) {
                job.setStderr(output.getError().toString());
            }

            if (output.getOutput() != null && output.getOutput().length() > 0) {
                job.setStdout(output.getOutput().toString());
            }

            if (!module.getDryRun() && module.getSampleId() != null && module.getPersistFileData()) {

                WorkflowRun workflowRun = workflowRunAttempt.getWorkflowRun();
                Sample sample = daoBean.getSampleDAO().findById(module.getSampleId());

                Workflow workflow = workflowRun.getWorkflow();

                if (CollectionUtils.isEmpty(module.getFileDatas())) {
                    logger.warn("No fileDatas found");
                } else {
                    logger.debug("module.getFileDatas().size() = {}", module.getFileDatas().size());
                    for (FileData fileData : module.getFileDatas()) {
                        if (StringUtils.isEmpty(fileData.getPath())) {
                            String outputDirectory = System.getenv("MAPSEQ_OUTPUT_DIRECTORY");
                            File systemDirectory = new File(outputDirectory, workflow.getSystem().getValue());
                            File studyDirectory = new File(systemDirectory, sample.getStudy().getName());
                            File analysisDirectory = new File(studyDirectory, "analysis");
                            File flowcellDirectory = new File(analysisDirectory, sample.getFlowcell().getName());
                            File sampleOutputDir = new File(flowcellDirectory,
                                    String.format("L%03d_%s", sample.getLaneIndex(), sample.getBarcode()));
                            File workflowDirectory = new File(sampleOutputDir, workflow.getName());
                            fileData.setPath(workflowDirectory.getAbsolutePath());
                        }
                        logger.info(fileData.toString());
                        List<FileData> foundFileDataList = daoBean.getFileDataDAO().findByExample(fileData);
                        if (CollectionUtils.isNotEmpty(foundFileDataList)) {
                            fileData = foundFileDataList.get(0);
                        } else {
                            fileData.setId(daoBean.getFileDataDAO().save(fileData));
                        }
                        logger.info(fileData.toString());
                        daoBean.getJobDAO().addFileData(fileData.getId(), job.getId());
                        if (module.getSampleId() != null) {
                            daoBean.getSampleDAO().addFileData(fileData.getId(), sample.getId());
                        }
                    }

                    // job has likely had fileData updates...go get fresh version
                    job = daoBean.getJobDAO().findById(job.getId());

                }

            }
            job.setExitCode(output.getExitCode());
            updateJobState(output.getExitCode() != 0 ? JobStatusType.FAILED : JobStatusType.DONE);

            if (module.getValidate()) {
                List<String> outputErrors = module.validateOutputs();
                if (CollectionUtils.isNotEmpty(outputErrors)) {
                    String message = StringUtils.join(outputErrors, System.getProperty("line.separator"));
                    throw new ModuleException(message);
                }
            }

        } catch (Exception e) {
            logger.error("Error", e);
            job.setStderr(e.getMessage());
            job.setExitCode(-1);
            updateJobState(JobStatusType.FAILED);
        } finally {
            if (module.getSerialize() != null) {
                try {
                    JAXBContext context = JAXBContext.newInstance(Job.class);
                    Marshaller m = context.createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    File moduleClassXMLFile = module.getSerialize();
                    FileWriter fw = new FileWriter(moduleClassXMLFile);
                    m.marshal(getJob(), fw);
                } catch (JAXBException | IOException e) {
                    logger.error("MaPSeq Error", e);
                }
            }
        }

        return output;
    }

    private void updateJobState(JobStatusType statusType) {
        setChanged();
        notifyObservers(statusType);
    }

    private Set<Attribute> createJobAttributes() {

        // AttributeDAO attributeDAO = daoBean.getAttributeDAO();
        Set<Attribute> attributeSet = new HashSet<Attribute>();

        String siteName = System.getenv("JLRM_SITE_NAME");
        if (StringUtils.isNotEmpty(siteName)) {
            attributeSet.add(new Attribute("siteName", siteName));
        }

        Map<String, String> envMap = System.getenv();
        for (String key : envMap.keySet()) {
            String value = envMap.get(key);
            String executable = module.getExecutable();
            if (StringUtils.isNotEmpty(executable) && executable.contains(key) && key.length() > 1) {
                attributeSet.add(new Attribute(key, value));
            }
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress currentAddr = addresses.nextElement();
                    if (currentAddr.isLoopbackAddress()) {
                        continue;
                    }
                    String name = current.getName();
                    if (currentAddr instanceof Inet4Address && StringUtils.isNotEmpty(name) && name.contains("eth")) {
                        attributeSet.add(new Attribute("inet4Address", currentAddr.getHostAddress()));
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return attributeSet;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public MaPSeqDAOBeanService getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(MaPSeqDAOBeanService daoBean) {
        this.daoBean = daoBean;
    }

}
