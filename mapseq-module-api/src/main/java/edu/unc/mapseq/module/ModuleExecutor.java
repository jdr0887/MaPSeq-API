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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.unc.mapseq.dao.AttributeDAO;
import edu.unc.mapseq.dao.FileDataDAO;
import edu.unc.mapseq.dao.JobDAO;
import edu.unc.mapseq.dao.MaPSeqDAOBean;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.SampleDAO;
import edu.unc.mapseq.dao.model.Attribute;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.JobStatusType;
import edu.unc.mapseq.dao.model.Sample;
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

    private MaPSeqDAOBean daoBean;

    public ModuleExecutor() {
        super();
    }

    @Override
    public ModuleOutput call() throws ModuleException {
        logger.debug("ENTERING run(Module)");
        ModuleOutput output = null;

        WorkflowRunAttempt workflowRunAttempt = null;
        if (!module.getDryRun()) {
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

        if (!module.getDryRun()) {
            try {
                JobDAO jobDAO = daoBean.getJobDAO();
                this.job.setId(jobDAO.save(this.job));
                for (Attribute attribute : createJobAttributes()) {
                    jobDAO.addAttribute(attribute.getId(), job.getId());
                }
            } catch (MaPSeqDAOException e) {
                e.printStackTrace();
            }
        }

        try {
            updateJobState(JobStatusType.RUNNING);

            output = module.call();

            if (output == null) {
                throw new ModuleException("ModuleOutput is null");
            }

            if (output.getError() != null && output.getError().length() > 0) {
                job.setStderr(output.getError().toString());
            }

            if (output.getOutput() != null && output.getOutput().length() > 0) {
                job.setStdout(output.getOutput().toString());
            }

            job.setExitCode(output.getExitCode());
            updateJobState(output.getExitCode() != 0 ? JobStatusType.FAILED : JobStatusType.DONE);

        } catch (Exception e) {
            job.setStderr(e.getMessage());
            job.setExitCode(-1);
            updateJobState(JobStatusType.FAILED);
            logger.error("Error", e);
        } finally {

            if (!module.getDryRun() && module.getPersistFileData()) {

                FileDataDAO fileDataDAO = daoBean.getFileDataDAO();
                SampleDAO sampleDAO = daoBean.getSampleDAO();
                JobDAO jobDAO = daoBean.getJobDAO();

                if (module.getFileDatas() != null && !module.getFileDatas().isEmpty() && module.getSampleId() != null) {
                    logger.debug("module.getFileDatas().size() = {}", module.getFileDatas().size());

                    WorkflowRun workflowRun = workflowRunAttempt.getWorkflowRun();
                    try {
                        Sample sample = sampleDAO.findById(module.getSampleId());

                        for (FileData fileData : module.getFileDatas()) {
                            fileData.setPath(String.format("%s/%s", sample.getOutputDirectory(), workflowRun
                                    .getWorkflow().getName()));
                            List<FileData> fileDataList = fileDataDAO.findByExample(fileData);
                            // if already exists, don't recreate duplicate FileData
                            FileData tmpFileData = null;
                            if (fileDataList != null && !fileDataList.isEmpty()) {
                                tmpFileData = fileDataList.get(0);
                            } else {
                                Long fileDataId = fileDataDAO.save(fileData);
                                fileData.setId(fileDataId);
                                tmpFileData = fileData;
                            }
                            logger.debug(tmpFileData.toString());
                            jobDAO.addFileData(tmpFileData.getId(), job.getId());
                            sampleDAO.addFileData(tmpFileData.getId(), sample.getId());
                        }
                    } catch (MaPSeqDAOException e) {
                        logger.error("MaPSeq Error", e);
                    }

                }

            }

        }

        if (module.getSerializeFile() != null) {
            try {
                JAXBContext context = JAXBContext.newInstance(Job.class);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                File moduleClassXMLFile = module.getSerializeFile();
                FileWriter fw = new FileWriter(moduleClassXMLFile);
                m.marshal(getJob(), fw);
            } catch (JAXBException | IOException e) {
                logger.error("MaPSeq Error", e);
            }
        }

        return output;
    }

    private void updateJobState(JobStatusType statusType) {
        setChanged();
        notifyObservers(statusType);
    }

    private Set<Attribute> createJobAttributes() throws MaPSeqDAOException {

        AttributeDAO attributeDAO = daoBean.getAttributeDAO();

        Set<Attribute> attributeSet = new HashSet<Attribute>();

        String siteName = System.getenv("JLRM_SITE_NAME");
        if (StringUtils.isNotEmpty(siteName)) {
            Attribute attribute = new Attribute("siteName", siteName);
            attribute.setId(attributeDAO.save(attribute));
            attributeSet.add(attribute);
        }

        Map<String, String> envMap = System.getenv();
        for (String key : envMap.keySet()) {
            String value = envMap.get(key);
            String executable = module.getExecutable();
            if (StringUtils.isNotEmpty(executable) && executable.contains(key) && key.length() > 1) {
                Attribute attribute = new Attribute(key, value);
                attribute.setId(attributeDAO.save(attribute));
                attributeSet.add(attribute);
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
                    InetAddress current_addr = addresses.nextElement();
                    if (current_addr.isLoopbackAddress()) {
                        continue;
                    }
                    if (current_addr instanceof Inet4Address && current.getName().contains("eth")) {
                        Attribute attribute = new Attribute("inet4Address", current_addr.getHostAddress());
                        attribute.setId(attributeDAO.save(attribute));
                        attributeSet.add(attribute);
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

    public MaPSeqDAOBean getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(MaPSeqDAOBean daoBean) {
        this.daoBean = daoBean;
    }

}
