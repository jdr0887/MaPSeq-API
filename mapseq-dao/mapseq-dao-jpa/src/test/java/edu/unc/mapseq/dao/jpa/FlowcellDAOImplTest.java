package edu.unc.mapseq.dao.jpa;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Flowcell;
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.Study;
import edu.unc.mapseq.dao.model.WorkflowRun;

public class FlowcellDAOImplTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testSampleSheet() {

        FlowcellDAOImpl sequencerRunDAO = new FlowcellDAOImpl();
        sequencerRunDAO.setEntityManager(em);

        SampleDAOImpl HTSFSampleDAO = new SampleDAOImpl();
        HTSFSampleDAO.setEntityManager(em);

        StudyDAOImpl studyDAO = new StudyDAOImpl();
        studyDAO.setEntityManager(em);

        Vector<Vector<String>> data = new Vector<Vector<String>>();

        try {
            Flowcell sequencerRun = sequencerRunDAO.findById(113L);

            List<Sample> htsfSampleList = HTSFSampleDAO.findByFlowcellId(sequencerRun.getId());
            if (htsfSampleList != null && htsfSampleList.size() > 0) {
                for (Sample htsfSample : htsfSampleList) {
                    System.out.println("htsfSample.getId() = " + htsfSample.getId());
                    Study study = htsfSample.getStudy();
                    System.out.println("study.getId() = " + study.getId());
                    // Sample sample = htsfSample.getSample();
                    // System.out.println("sample.getId() = " + sample.getId());
                    Vector<String> childVector = new Vector<String>();
                    childVector.add(htsfSample.getLaneIndex().toString());
                    childVector.add(htsfSample.getName());
                    childVector.add(htsfSample.getBarcode());
                    childVector.add(study.getName());
                    data.add(childVector);
                }
            }

        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }

        Collections.sort(data, new Comparator<Vector<String>>() {

            @Override
            public int compare(Vector<String> arg0, Vector<String> arg1) {
                return Integer.valueOf(arg0.get(0)).compareTo(Integer.valueOf(arg1.get(0)));
            }

        });

        Iterator<Vector<String>> dataIter = data.iterator();
        while (dataIter.hasNext()) {
            Vector<String> values = dataIter.next();
            Iterator<String> valuesIter = values.iterator();
            StringBuilder sb = new StringBuilder();
            while (valuesIter.hasNext()) {
                sb.append(valuesIter.next()).append(",");
            }
            System.out.println(sb.toString());
        }

    }

    @Test
    public void testFindByStudyName() {

        FlowcellDAOImpl sequencerRunDAO = new FlowcellDAOImpl();
        sequencerRunDAO.setEntityManager(em);
        try {
            List<Flowcell> ret = sequencerRunDAO.findByStudyName("NC_GENES");
            assertTrue(ret != null && ret.size() > 0);
            for (Flowcell sr : ret) {
                System.out.println(sr.getId());
            }
        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testBatchDelete() {

        FlowcellDAOImpl sequencerRunDAO = new FlowcellDAOImpl();
        sequencerRunDAO.setEntityManager(em);
        List<Flowcell> sequencerRunIdList = new ArrayList<Flowcell>();
        try {
            em.getTransaction().begin();
            sequencerRunIdList.add(sequencerRunDAO.findById(95L));
            sequencerRunIdList.add(sequencerRunDAO.findById(113L));
            sequencerRunDAO.delete(sequencerRunIdList);
            em.getTransaction().commit();
        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        FlowcellDAOImpl sequencerRunDAO = new FlowcellDAOImpl();
        sequencerRunDAO.setEntityManager(em);

        SampleDAOImpl htsfSampleDAO = new SampleDAOImpl();
        htsfSampleDAO.setEntityManager(em);

        WorkflowRunAttemptDAOImpl workflowPlanDAO = new WorkflowRunAttemptDAOImpl();
        workflowPlanDAO.setEntityManager(em);

        try {
            Flowcell sequencerRun = sequencerRunDAO.findById(108078L);

            JobDAOImpl jobDAO = new JobDAOImpl();
            jobDAO.setEntityManager(em);

            WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
            workflowRunDAO.setEntityManager(em);

            List<Sample> htsfSamplesToDeleteList = htsfSampleDAO.findByFlowcellId(sequencerRun.getId());

            List<Job> jobsToDeleteList = new ArrayList<Job>();
            List<WorkflowRun> workflowRunsToDeleteList = new ArrayList<WorkflowRun>();

            for (Sample sample : htsfSamplesToDeleteList) {

                List<WorkflowRun> workflowRunList = workflowRunDAO.findBySampleId(sample.getId());

                if (workflowRunList == null) {
                    continue;
                }

                for (WorkflowRun workflowRun : workflowRunList) {
                    workflowRun.setSamples(null);
                    jobsToDeleteList.addAll(jobDAO.findByWorkflowRunAttemptId(workflowRun.getId()));
                    workflowRunDAO.save(workflowRun);
                    workflowRunsToDeleteList.add(workflowRun);
                }

            }

            em.getTransaction().begin();
            jobDAO.delete(jobsToDeleteList);
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
