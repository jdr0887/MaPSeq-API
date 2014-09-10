package edu.unc.mapseq.dao.jpa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
import edu.unc.mapseq.dao.WorkflowRunAttemptDAO;
import edu.unc.mapseq.dao.model.Study;
import edu.unc.mapseq.dao.model.WorkflowRun;
import edu.unc.mapseq.dao.model.WorkflowRunAttempt;

public class WorkflowRunDAOImplTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testFindByCreatedDateRange() throws MaPSeqDAOException {
        WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
        workflowRunDAO.setEntityManager(em);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // c.add(Calendar.WEEK_OF_YEAR, -1);
        c.add(Calendar.WEEK_OF_YEAR, -4);
        Date aWeekAgo = c.getTime();

        long startTime = System.currentTimeMillis();
        List<WorkflowRun> workflowRunList = workflowRunDAO.findByCreatedDateRange(aWeekAgo, date);
        long endTime = System.currentTimeMillis();
        System.out.println("Duration = " + (endTime - startTime) / 1000);
        System.out.println("workflowRunList.size() = " + workflowRunList.size());
    }

    @Test
    public void testFindStudyId() throws MaPSeqDAOException {
        WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
        workflowRunDAO.setEntityManager(em);
        StudyDAOImpl studyDAO = new StudyDAOImpl();
        studyDAO.setEntityManager(em);
        Study study = studyDAO.findByName("NC_GENES").get(0);
        long startTime = System.currentTimeMillis();
        List<WorkflowRun> workflowRunList = workflowRunDAO.findByStudyId(study.getId());
        long endTime = System.currentTimeMillis();
        System.out.println("Duration = " + (endTime - startTime) / 1000);
        System.out.println("workflowRunList.size() = " + workflowRunList.size());
    }

    @Test
    public void testFormatting() throws MaPSeqDAOException {

        WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
        workflowRunDAO.setEntityManager(em);

        WorkflowRunAttemptDAOImpl workflowRunAttemptDAO = new WorkflowRunAttemptDAOImpl();
        workflowRunAttemptDAO.setEntityManager(em);

        try {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("%1$-12s %2$-54s %3$-18s %4$-18s %5$-18s %6$-12s %7$-14s %8$s%n", "ID",
                    "Workflow Run Name", "Created Date", "Start Date", "End Date", "Status", "Condor JobId",
                    "Submit Directory");

            List<WorkflowRun> workflowRunList = workflowRunDAO.findByWorkflowId(8L);
            
            if (workflowRunList != null && !workflowRunList.isEmpty()) {
                for (WorkflowRun workflowRun : workflowRunList) {

                    Date createdDate = workflowRun.getCreated();
                    String formattedCreatedDate = "";
                    if (createdDate != null) {
                        formattedCreatedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                                .format(createdDate);
                    }

                    formatter.format("%1$-12s %2$-54s %3$s%n", workflowRun.getId(), workflowRun.getName(),
                            formattedCreatedDate);

                    List<WorkflowRunAttempt> attempts = workflowRunAttemptDAO.findByWorkflowRunId(workflowRun.getId());

                    if (attempts != null && !attempts.isEmpty()) {
                        for (WorkflowRunAttempt attempt : attempts) {

                            createdDate = attempt.getCreated();
                            if (createdDate != null) {
                                formattedCreatedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                        DateFormat.SHORT).format(createdDate);
                            }

                            Date startDate = attempt.getStarted();
                            String formattedStartDate = "";
                            if (startDate != null) {
                                formattedStartDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                                        .format(startDate);
                            }
                            Date endDate = attempt.getFinished();
                            String formattedEndDate = "";
                            if (endDate != null) {
                                formattedEndDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                                        .format(endDate);
                            }

                            formatter.format("%1$-12s %2$-54s %3$-18s %4$-18s %5$-18s %6$-12s %7$-14s %8$s%n", "--",
                                    "--", formattedCreatedDate, formattedStartDate, formattedEndDate, attempt
                                            .getStatus().toString(), attempt.getCondorDAGClusterId(), attempt
                                            .getSubmitDirectory());
                            formatter.flush();

                        }
                    }

                }

            }

            System.out.println(formatter.toString());
            formatter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFindByStudyNameAndSampleNameAndWorkflowName() {

        WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
        workflowRunDAO.setEntityManager(em);
        try {

            // List<WorkflowRun> workflowRunList =
            // workflowRunDAO.findByStudyNameAndSampleNameAndWorkflowName("NC_GENES",
            // "NCG%", "CASAVA");
            long start = System.currentTimeMillis();
            List<WorkflowRun> workflowRunList = workflowRunDAO.findByStudyNameAndSampleNameAndWorkflowName("NC_GENES",
                    "NCG_00073%", "CASAVA");

            // List<WorkflowRun> workflowRunList =
            // workflowRunDAO.findByStudyNameAndSampleNameAndWorkflowName("NC_GENES",
            // "NCG%", "NCGenes");
            long end = System.currentTimeMillis();
            System.out.println(String.format("Size: %d", workflowRunList.size()));
            System.out.println(String.format("Duration: %d seconds", TimeUnit.MILLISECONDS.toSeconds(end - start)));

            // assertTrue(workflowRunList != null && workflowRunList.size() > 0);
            for (WorkflowRun wr : workflowRunList) {

                try {
                    JAXBContext context = JAXBContext.newInstance(WorkflowRun.class);
                    Marshaller m = context.createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    File moduleClassXMLFile = new File("/tmp", String.format("%s-%d.xml", "WorkflowRun", wr.getId()));
                    FileWriter fw = new FileWriter(moduleClassXMLFile);
                    m.marshal(wr, fw);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (PropertyException e1) {
                    e1.printStackTrace();
                } catch (JAXBException e1) {
                    e1.printStackTrace();
                }

            }
        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDelete() {
        WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
        workflowRunDAO.setEntityManager(em);

        WorkflowRunAttemptDAOImpl workflowPlanDAO = new WorkflowRunAttemptDAOImpl();
        workflowPlanDAO.setEntityManager(em);

        try {
            List<WorkflowRun> wfRunList = workflowRunDAO.findByName("asdf");

            for (WorkflowRun wr : wfRunList) {

                List<WorkflowRunAttempt> workflowPlanList = workflowPlanDAO.findByWorkflowRunId(wr.getId());

                for (WorkflowRunAttempt entity : workflowPlanList) {
                    em.getTransaction().begin();
                    workflowPlanDAO.delete(entity);
                    em.getTransaction().commit();
                }

                em.getTransaction().begin();
                workflowRunDAO.delete(wr);
                em.getTransaction().commit();
            }

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
