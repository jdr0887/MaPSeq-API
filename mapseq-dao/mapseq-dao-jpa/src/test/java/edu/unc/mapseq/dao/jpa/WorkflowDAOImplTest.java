package edu.unc.mapseq.dao.jpa;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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

    @Test
    public void testFindAll() throws MaPSeqDAOException {
        WorkflowDAOImpl workflowDAO = new WorkflowDAOImpl();
        workflowDAO.setEntityManager(em);
        List<Workflow> workflowList = workflowDAO.findAll();
        assertTrue(workflowList != null && !workflowList.isEmpty());
    }

    class ASDF {
        private List<Workflow> workflows;

        public ASDF(List<Workflow> workflows) {
            super();
            this.workflows = workflows;
        }

        @JsonValue
        public List<Workflow> getWorkflows() {
            return workflows;
        }

        public void setWorkflows(List<Workflow> workflows) {
            this.workflows = workflows;
        }

    }

    @Test
    public void testJSONSerialization() {
        try {
            WorkflowDAOImpl workflowDAO = new WorkflowDAOImpl();
            workflowDAO.setEntityManager(em);
            List<Workflow> workflowList = workflowDAO.findAll();
            em.close();
            emf.close();
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter ow = mapper.writer();
            ow.writeValue(new File("/tmp/workflow.json"), new ASDF(workflowList));
        } catch (MaPSeqDAOException | IOException e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        if (em.isOpen()) {
            em.close();
            emf.close();
        }
    }

}
