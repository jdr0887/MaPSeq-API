package edu.unc.mapseq.dao.jpa;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Attribute;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.Flowcell;
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.JobStatusType;
import edu.unc.mapseq.dao.model.Job_;
import edu.unc.mapseq.dao.model.MimeType;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.Workflow;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt_;

public class JobDAOImplTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testFindByCreatedDateRange() throws MaPSeqDAOException {
        JobDAOImpl jobDAO = new JobDAOImpl();
        jobDAO.setEntityManager(em);

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_YEAR, -1);
        // c.add(Calendar.WEEK_OF_YEAR, -4);
        Date aWeekAgo = c.getTime();

        long startTime = System.currentTimeMillis();
        List<Job> workflowRunList = jobDAO.findByCreatedDateRange(aWeekAgo, date);
        long endTime = System.currentTimeMillis();
        System.out.println("Duration = " + (endTime - startTime) / 1000);
        System.out.println("workflowRunList.size() = " + workflowRunList.size());
    }

    @Test
    public void testFindByWorkflowIdAndCreatedDateRange() throws MaPSeqDAOException {
        JobDAOImpl jobDAO = new JobDAOImpl();
        jobDAO.setEntityManager(em);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_YEAR, -1);
        // c.add(Calendar.WEEK_OF_YEAR, -4);
        Date aWeekAgo = c.getTime();

        Long workflowId = 189557L;
        // Long workflowId = 1343397L;

        long startTime = System.currentTimeMillis();

        List<Job> workflowRunList = jobDAO.findByWorkflowIdAndCreatedDateRange(workflowId, aWeekAgo, date);
        long endTime = System.currentTimeMillis();
        System.out.println("Duration = " + (endTime - startTime) / 1000);
        System.out.println("workflowRunList.size() = " + workflowRunList.size());
    }

    @Test
    public void testRetrieve() {

        CriteriaBuilder critBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Job> crit = critBuilder.createQuery(Job.class);

        Root<Job> jobRoot = crit.from(Job.class);

        Predicate condition1 = critBuilder.equal(jobRoot.get(Job_.id), 3L);

        crit.where(condition1);

        Query query = em.createQuery(crit);
        Job job = (Job) query.getSingleResult();
        String stdout = new String(job.getStdout());
        System.out.println(stdout);
        assertTrue(stdout.equals("qwerqwe"));

    }

    @Test
    public void testSave() {

        Job job = new Job();
        job.setFinished(new Date());
        job.setExitCode(0);
        job.setName("asdfasdasdf");
        job.setStarted(new Date());
        job.setStatus(JobStatusType.RUNNING);

        try {

            Set<Attribute> attributeSet = new HashSet<Attribute>();

            AttributeDAOImpl attributeDAO = new AttributeDAOImpl();
            attributeDAO.setEntityManager(em);

            Attribute attribute = new Attribute("foo", "bar");
            em.getTransaction().begin();
            attribute.setId(attributeDAO.save(attribute));
            em.getTransaction().commit();
            attributeSet.add(attribute);

            attribute = new Attribute("fuzz", "buzz");
            em.getTransaction().begin();
            attribute.setId(attributeDAO.save(attribute));
            em.getTransaction().commit();
            attributeSet.add(attribute);

            job.setAttributes(attributeSet);

            JobDAOImpl jobDAO = new JobDAOImpl();
            jobDAO.setEntityManager(em);

            em.getTransaction().begin();
            Long id = jobDAO.save(job);
            em.getTransaction().commit();
            System.out.println(id);
        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSaveWorkflow() {

        FlowcellDAOImpl sequencerRunDAO = new FlowcellDAOImpl();
        sequencerRunDAO.setEntityManager(em);

        JobDAOImpl jobDAO = new JobDAOImpl();
        jobDAO.setEntityManager(em);

        String[] moduleClasses = { "edu.unc.mapseq.module.bwa.BWAAlign", "edu.unc.mapseq.module.bwa.BWASAMPairedEnd",
                "edu.unc.mapseq.module.picard.PicardAddOrReplaceReadGroups",
                "edu.unc.mapseq.module.picard.PicardSortSAM", "edu.unc.mapseq.module.samtools.SAMToolsIndex",
                "edu.unc.mapseq.module.picard.PicardMarkDuplicates", "edu.unc.mapseq.module.samtools.SAMToolsIndex",
                "edu.unc.mapseq.module.picard.PicardReorderSAM", "edu.unc.mapseq.module.samtools.SAMToolsIndex" };
        for (String moduleClass : moduleClasses) {

            Job job = new Job();
            job.setFinished(new Date());
            job.setExitCode(0);
            job.setName("asdfasdasdf");
            job.setStarted(new Date());
            job.setStatus(JobStatusType.DONE);
            try {

                if (moduleClass.equals("edu.unc.mapseq.module.picard.PicardSortSAM")) {
                    Set<FileData> fileDataSet = new HashSet<FileData>();
                    FileData fileData = new FileData();
                    fileData.setMimeType(MimeType.APPLICATION_BAM);
                    fileData.setName("asdf.bam");
                    fileData.setPath("/tmp");
                    fileDataSet.add(fileData);
                    job.setFileDatas(fileDataSet);

                    Flowcell sequencerRun = sequencerRunDAO.findById(89L);
                    Set<FileData> sequencerRunFileDatas = sequencerRun.getFileDatas();
                    if (sequencerRunFileDatas == null) {
                        sequencerRunFileDatas = new HashSet<FileData>();
                    }
                    sequencerRunFileDatas.addAll(fileDataSet);
                    sequencerRun.setFileDatas(sequencerRunFileDatas);
                    em.getTransaction().begin();
                    sequencerRunDAO.save(sequencerRun);
                    em.getTransaction().commit();
                }

                if (moduleClass.equals("edu.unc.mapseq.module.picard.PicardReorderSAM")) {
                    Set<FileData> fileDataSet = new HashSet<FileData>();
                    FileData fileData = new FileData();
                    fileData.setMimeType(MimeType.APPLICATION_BAM);
                    fileData.setName("asdf.bam");
                    fileData.setPath("/tmp");
                    fileDataSet.add(fileData);
                    job.setFileDatas(fileDataSet);

                    Flowcell sequencerRun = sequencerRunDAO.findById(89L);
                    Set<FileData> sequencerRunFileDatas = sequencerRun.getFileDatas();
                    if (sequencerRunFileDatas == null) {
                        sequencerRunFileDatas = new HashSet<FileData>();
                    }
                    sequencerRunFileDatas.addAll(fileDataSet);
                    sequencerRun.setFileDatas(sequencerRunFileDatas);
                    em.getTransaction().begin();
                    sequencerRunDAO.save(sequencerRun);
                    em.getTransaction().commit();
                }

                em.getTransaction().begin();
                Long id = jobDAO.save(job);
                em.getTransaction().commit();
                System.out.println(id);
            } catch (MaPSeqDAOException e) {
                e.printStackTrace();
            }

        }

    }

    @Test
    public void testSaveAttributes() {

        try {
            JobDAOImpl jobDAO = new JobDAOImpl();
            jobDAO.setEntityManager(em);
            Job job = jobDAO.findById(259L);

            Set<Attribute> attributeSet = job.getAttributes();

            Set<String> attributeNameSet = new HashSet<String>();
            for (Attribute attribute : attributeSet) {
                attributeNameSet.add(attribute.getName());
            }

            if (!attributeNameSet.contains("asdf")) {
                Attribute attribute = new Attribute();
                attribute.setName("asdf");
                attribute.setValue("asdf");
                attributeSet.add(attribute);
            }

            for (Attribute attribute : attributeSet) {
                if ("foo".equals(attribute.getName())) {
                    attribute.setValue("buzz");
                }
                if ("fuzz".equals(attribute.getName())) {
                    attribute.setValue("bar");
                }
            }

            em.getTransaction().begin();
            Long id = jobDAO.save(job);
            em.getTransaction().commit();
            System.out.println(id);
        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFindJobBySequencerRunAndWorkflowRun() {

        CriteriaBuilder critBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Job> crit = critBuilder.createQuery(Job.class);

        Root<Job> jobRoot = crit.from(Job.class);

        Join<Job, WorkflowRunAttempt> jobWorkflowRunAttemptJoin = jobRoot.join(Job_.workflowRunAttempt);
        Predicate condition1 = critBuilder.equal(jobWorkflowRunAttemptJoin.get(WorkflowRunAttempt_.id), 2L);

        Query query = em.createQuery(crit);
        List<Job> ret = query.getResultList();

    }

    @Test
    public void testFindFileData() {

        JobDAOImpl jobDAO = new JobDAOImpl();
        jobDAO.setEntityManager(em);

        FlowcellDAOImpl sequencerRunDAO = new FlowcellDAOImpl();
        sequencerRunDAO.setEntityManager(em);

        try {
            Flowcell sequencerRun = sequencerRunDAO.findById(113034L);
            Set<FileData> sequencerRunFileDatas = sequencerRun.getFileDatas();
            System.out.println(sequencerRunFileDatas.size());
            long start = new Date().getTime();
            for (FileData fd : sequencerRunFileDatas) {
                List<Job> jobList = jobDAO.findByFileDataId(fd.getId(), "edu.unc.mapseq.module.bwa.BWAAlign");
                if (jobList != null && jobList.size() > 0) {
                    try {
                        JAXBContext context = JAXBContext.newInstance(Job.class);
                        Marshaller m = context.createMarshaller();
                        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                        System.out.println(jobList.get(0).getName());
                        File moduleClassXMLFile = new File("/tmp", String.format("%s.xml", jobList.get(0).getName()));
                        FileWriter fw = new FileWriter(moduleClassXMLFile);
                        m.marshal(jobList.get(0), fw);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (PropertyException e1) {
                        e1.printStackTrace();
                    } catch (JAXBException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            long end = new Date().getTime();
            System.out.println("duration = " + (end - start));
        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFindFileDataWithWorkflowId() throws MaPSeqDAOException {

        WorkflowDAOImpl workflowDAO = new WorkflowDAOImpl();
        workflowDAO.setEntityManager(em);

        SampleDAOImpl htsfSampleDAO = new SampleDAOImpl();
        htsfSampleDAO.setEntityManager(em);

        // HTSFSample sample = htsfSampleDAO.findById(182941L);
        Sample sample = htsfSampleDAO.findById(621070L);

        JobDAOImpl jobDAO = new JobDAOImpl();
        jobDAO.setEntityManager(em);

        Set<FileData> fileDataSet = sample.getFileDatas();

        MimeType mimeType = MimeType.APPLICATION_BAM;

        // uncomment the test scope in pom.xml
        List<Workflow> workflowList = workflowDAO.findByName("NIDAUCSFAlignment");

        List<File> ret = new ArrayList<File>();
        if (fileDataSet != null) {
            for (FileData fileData : fileDataSet) {
                if (fileData.getMimeType().equals(mimeType)) {
                    List<Job> jobList = jobDAO.findByFileDataIdAndWorkflowId(fileData.getId(),
                            "edu.unc.mapseq.module.picard.PicardAddOrReplaceReadGroups", workflowList.get(0).getId());
                    // List<Job> jobList = jobDAO.findFileDataById(fileData.getId(), clazz.getName());
                    if (jobList != null && jobList.size() > 0) {
                        for (Job job : jobList) {
                            if (job.getName().contains("PicardAddOrReplaceReadGroups")) {
                                // logger.debug("using FileData: {}", fileData.toString());
                                // logger.debug("from Job: {}", job.toString());
                                ret.add(new File(fileData.getPath(), fileData.getName()));
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (File f : ret) {
            System.out.println(f.getAbsolutePath());
        }

    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

}
