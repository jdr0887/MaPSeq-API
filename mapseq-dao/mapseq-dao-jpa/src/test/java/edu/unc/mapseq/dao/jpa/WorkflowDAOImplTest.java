package edu.unc.mapseq.dao.jpa;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Workflow;

public class WorkflowDAOImplTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testFindByName() throws MaPSeqDAOException {
        WorkflowDAOImpl workflowDAO = new WorkflowDAOImpl();
        workflowDAO.setEntityManager(em);
        List<Workflow> workflowList = workflowDAO.findByName("NCGenes");
        assertTrue(workflowList != null && workflowList.size() == 1);
        workflowList = workflowDAO.findByName("NC%");
        assertTrue(workflowList != null && workflowList.size() > 1);
    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

}
