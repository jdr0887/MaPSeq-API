package edu.unc.mapseq.dao.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.WorkflowRun;

public class WorkflowRunAttemptDAOImplTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testFindByStudyNameAndSampleNameAndWorkflowName() {

        WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
        workflowRunDAO.setEntityManager(em);
        try {

            // List<WorkflowPlan> workflowPlanList = workflowPlanDAO.findByStudyNameAndSampleNameAndWorkflowName(
            // "NC_GENES", "TCGA-BT-A20P%", "CASAVA");
            // List<WorkflowPlan> workflowPlanList = workflowPlanDAO.findByStudyNameAndSampleNameAndWorkflowName(
            // "NC_GENES", "TCGA-BT-A20P%", "NCGenes");
            // List<WorkflowPlan> workflowPlanList = workflowPlanDAO.findByStudyNameAndSampleNameAndWorkflowName(
            // "NC_GENES", "NCG%", "NCGenes");
            // List<WorkflowPlan> workflowPlanList = workflowPlanDAO.findByStudyNameAndSampleNameAndWorkflowName(
            // "NC_GENES", "NCG_00073%", "CASAVA");

            // List<WorkflowPlan> workflowPlanList = workflowPlanDAO.findByStudyNameAndSampleNameAndWorkflowName(
            // "NC_GENES", "NCG_00073%", "NCGenes");

            List<String> participantList = Arrays.asList("NCG_00483", "NCG_00494", "NCG_00524", "NCG_00384",
                    "NCG_00475", "NCG_00497", "NCG_00501", "NCG_00517", "NCG_00543", "NCG_00552", "NCG_00555",
                    "NCG_00558", "NCG_00564", "NCG_00571", "NCG_00584", "NCG_00709");

            List<WorkflowRun> workflowRunList = new ArrayList<WorkflowRun>();

            long start = System.currentTimeMillis();
            for (String participant : participantList) {
                workflowRunList.addAll(workflowRunDAO.findByStudyNameAndSampleNameAndWorkflowName("NC_GENES",
                        participant + "%", "CASAVA"));
            }
            long end = System.currentTimeMillis();
            System.out.println(String.format("Size: %d", workflowRunList.size()));
            System.out.println(String.format("Duration: %d", TimeUnit.MILLISECONDS.toSeconds(end - start)));

            // assertTrue(workflowPlanList != null && workflowPlanList.size() > 0);
            // for (WorkflowPlan wp : workflowPlanList) {
            //
            // try {
            // JAXBContext context = JAXBContext.newInstance(WorkflowPlan.class);
            // Marshaller m = context.createMarshaller();
            // m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // File moduleClassXMLFile = new File("/tmp", String.format("%s-%d.xml", "WorkflowPlan", wp.getId()));
            // FileWriter fw = new FileWriter(moduleClassXMLFile);
            // m.marshal(wp, fw);
            // } catch (IOException e1) {
            // e1.printStackTrace();
            // } catch (PropertyException e1) {
            // e1.printStackTrace();
            // } catch (JAXBException e1) {
            // e1.printStackTrace();
            // }
            //
            // }
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
