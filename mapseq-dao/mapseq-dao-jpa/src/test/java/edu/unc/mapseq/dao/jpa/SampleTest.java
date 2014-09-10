package edu.unc.mapseq.dao.jpa;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.MimeType;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;

public class SampleTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testFindById() throws MaPSeqDAOException {

        SampleDAOImpl sampleDAO = new SampleDAOImpl();
        sampleDAO.setEntityManager(em);
        Sample sample = sampleDAO.findById(621070L);

        try {
            JAXBContext context = JAXBContext.newInstance(Sample.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File moduleClassXMLFile = new File("/tmp", String.format("%s-%d.xml", "Sample", sample.getId()));
            FileWriter fw = new FileWriter(moduleClassXMLFile);
            m.marshal(sample, fw);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (PropertyException e1) {
            e1.printStackTrace();
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }

    }

    @Test
    public void testSave() throws MaPSeqDAOException {

        SampleDAOImpl sampleDAO = new SampleDAOImpl();
        sampleDAO.setEntityManager(em);

        Sample sample = new Sample();
        sample.getAttributes().add(new Attribute("qwerasdzxcv", "qwerasdzxcv"));
        sample.setBarcode("asdf");
        sample.setLaneIndex(1);

        em.getTransaction().begin();
        Long id = sampleDAO.save(sample);
        sample.setId(id);
        em.getTransaction().commit();

        Sample newSample = sampleDAO.findById(id);

        assertTrue(newSample.getId().equals(id));
        assertTrue(newSample.getAttributes() != null);
        assertTrue(newSample.getAttributes().size() == 1);

        newSample.setAttributes(null);
        em.getTransaction().begin();
        sampleDAO.save(newSample);
        em.getTransaction().commit();

        Sample newNewSample = sampleDAO.findById(id);

        assertTrue(newNewSample.getId().equals(id));
        assertTrue(newNewSample.getAttributes() != null);
        assertTrue(newNewSample.getAttributes().size() == 0);

    }

    @Test
    public void testDuplicateFileData() throws MaPSeqDAOException {

        SampleDAOImpl htsfSampleDAO = new SampleDAOImpl();
        htsfSampleDAO.setEntityManager(em);

        FileDataDAOImpl fileDataDAO = new FileDataDAOImpl();
        fileDataDAO.setEntityManager(em);
        Set<FileData> fileDataSet = new HashSet<FileData>();

        Set<FileData> moduleFileDataSet = new HashSet<FileData>();
        FileData fileData = new FileData();
        fileData.setMimeType(MimeType.FASTQ);
        fileData.setName("asdf.fastq.gz");
        // fileData.setPath("/home/jdr0887");
        moduleFileDataSet.add(fileData);

        fileData = new FileData();
        fileData.setMimeType(MimeType.FASTQ);
        fileData.setName("130220_UNC13-SN749_0232_AD1VEJACXX_ATCACG_L003_R1.fastq.gz");
        // fileData.setPath("/proj/seq/mapseq/RENCI/130220_UNC13-SN749_0232_AD1VEJACXX/CASAVA/057220Sm");
        moduleFileDataSet.add(fileData);

        Sample htsfSample = htsfSampleDAO.findById(194511L);
        Set<FileData> htsfSampleFileDatas = htsfSample.getFileDatas();

        if (htsfSampleFileDatas == null) {
            htsfSampleFileDatas = new HashSet<FileData>();
        }

        for (FileData fd : moduleFileDataSet) {

            // fd.setPath(htsfSample.getOutputDirectory());

            List<FileData> fileDataList = fileDataDAO.findByExample(fd);

            // if already exists, don't recreate duplicate FileData
            FileData tmpFileData = null;
            if (fileDataList != null && fileDataList.size() > 0) {
                tmpFileData = fileDataList.get(0);
            } else {
                em.getTransaction().begin();
                Long fileDataId = fileDataDAO.save(fd);
                em.getTransaction().commit();
                fd.setId(fileDataId);
                tmpFileData = fd;
            }
            if (!htsfSampleFileDatas.contains(tmpFileData)) {
                htsfSampleFileDatas.add(tmpFileData);
                fileDataSet.add(tmpFileData);
            }
        }

        htsfSample.setFileDatas(htsfSampleFileDatas);

        em.getTransaction().begin();
        htsfSampleDAO.save(htsfSample);
        em.getTransaction().commit();

    }

    @Test
    public void testDelete() {

        SampleDAOImpl htsfSampleDAO = new SampleDAOImpl();
        htsfSampleDAO.setEntityManager(em);

        WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
        workflowRunDAO.setEntityManager(em);

        WorkflowRunAttemptDAOImpl workflowPlanDAO = new WorkflowRunAttemptDAOImpl();
        workflowPlanDAO.setEntityManager(em);

        JobDAOImpl jobDAO = new JobDAOImpl();
        jobDAO.setEntityManager(em);

        try {

            List<Sample> htsfSamplesToDeleteList = htsfSampleDAO.findByFlowcellId(56470L);

            List<Job> jobsToDeleteList = new ArrayList<Job>();
            List<WorkflowRunAttempt> workflowPlansToDeleteList = new ArrayList<WorkflowRunAttempt>();
            List<WorkflowRun> workflowRunsToDeleteList = new ArrayList<WorkflowRun>();

            for (Sample sample : htsfSamplesToDeleteList) {

                List<WorkflowRun> workflowRunList = workflowRunDAO.findBySampleId(sample.getId());

                if (workflowRunList != null && workflowRunList.size() > 0) {

                    for (WorkflowRun workflowRun : workflowRunList) {
                        workflowRun.setSamples(null);
                        jobsToDeleteList.addAll(jobDAO.findByWorkflowRunAttemptId(workflowRun.getId()));
                        workflowRunDAO.save(workflowRun);
                        workflowRunsToDeleteList.add(workflowRun);
                    }

                }

            }

            em.getTransaction().begin();
            jobDAO.delete(jobsToDeleteList);
            em.getTransaction().commit();

            em.getTransaction().begin();
            workflowPlanDAO.delete(workflowPlansToDeleteList);
            em.getTransaction().commit();

            em.getTransaction().begin();
            workflowRunDAO.delete(workflowRunsToDeleteList);
            em.getTransaction().commit();

            em.getTransaction().begin();
            htsfSampleDAO.delete(htsfSamplesToDeleteList);
            em.getTransaction().commit();

        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

}
