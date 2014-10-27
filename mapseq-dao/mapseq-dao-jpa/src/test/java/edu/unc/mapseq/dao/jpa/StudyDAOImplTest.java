package edu.unc.mapseq.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Study;

public class StudyDAOImplTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testPersist() {

        Study entity = new Study();
        entity.setName("test study");

        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();

        System.out.println(entity.getId());

    }

    @Test
    public void testFindByName() {

        try {
            StudyDAOImpl studyDAO = new StudyDAOImpl();
            studyDAO.setEntityManager(em);
            List<Study> entityList = studyDAO.findByName("NEC");
            System.out.println(entityList.get(0).getId());
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
