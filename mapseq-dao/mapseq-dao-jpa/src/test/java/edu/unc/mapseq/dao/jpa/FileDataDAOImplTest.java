package edu.unc.mapseq.dao.jpa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.Flowcell;
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.MimeType;

public class FileDataDAOImplTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testFindByExample() {
        FileData fileData = new FileData();
        fileData.setPath("/tmp");
        fileData.setName("qwer");
        fileData.setMimeType(MimeType.TEXT_PLAIN);
        FileDataDAOImpl fileDataDAO = new FileDataDAOImpl();
        fileDataDAO.setEntityManager(em);

        try {
            Long id = fileDataDAO.save(fileData);
            List<FileData> fileDataList = fileDataDAO.findByExample(fileData);
            for (FileData fd : fileDataList) {
                System.out.println(fd.toString());
                if (id.equals(fd.getId())) {
                    break;
                }
            }
        } catch (MaPSeqDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAssociateFileDataWithEntity() throws MaPSeqDAOException {

        FileData fileData = new FileData();
        fileData.setPath("/tmp");
        fileData.setName("qwer");
        fileData.setMimeType(MimeType.TEXT_PLAIN);

        FileDataDAOImpl fileDataDAO = new FileDataDAOImpl();
        fileDataDAO.setEntityManager(em);

        List<FileData> fileDataList = fileDataDAO.findByExample(fileData);

        if (fileDataList != null && fileDataList.size() > 0) {
            fileData = fileDataList.get(0);
        } else {
            em.getTransaction().begin();
            fileDataDAO.save(fileData);
            em.getTransaction().commit();
        }

        Job job = new Job();
        job.setName("edu.unc.mapseq.dao.model.Job");
        Date date = new Date();
        job.setStarted(date);
        job.getFileDatas().add(fileData);

        JobDAOImpl jobDAO = new JobDAOImpl();
        jobDAO.setEntityManager(em);

        em.getTransaction().begin();
        Long jobId = jobDAO.save(job);
        em.getTransaction().commit();

        FlowcellDAOImpl sequencerRunDAO = new FlowcellDAOImpl();
        sequencerRunDAO.setEntityManager(em);

        Flowcell sequencerRun = sequencerRunDAO.findById(584283L);
        sequencerRun.getFileDatas().add(fileData);

        em.getTransaction().begin();
        sequencerRunDAO.save(sequencerRun);
        em.getTransaction().commit();

        System.out.println(jobId);
    }

    @Test
    public void testSave() throws MaPSeqDAOException {

        File f = new File("/tmp", "qwer");
        if (f.exists()) {
            f.delete();
        }
        try {
            IOUtils.write("asdfasdfasdf", new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileData fileData = new FileData();
        fileData.setPath(f.getParentFile().getAbsolutePath());
        fileData.setName(f.getName());
        fileData.setMimeType(MimeType.TEXT_PLAIN);

        FileDataDAOImpl fileDataDAO = new FileDataDAOImpl();
        fileDataDAO.setEntityManager(em);

        em.getTransaction().begin();
        fileDataDAO.save(fileData);
        em.getTransaction().commit();

    }

    @Test
    public void testFileQuery() {

        CriteriaBuilder critBuilder = em.getCriteriaBuilder();
        CriteriaQuery<FileData> crit = critBuilder.createQuery(FileData.class);

        Root<FileData> fileDataRoot = crit.from(FileData.class);

        Query query = em.createQuery(crit);
        List<FileData> ret = query.getResultList();

    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }
}
