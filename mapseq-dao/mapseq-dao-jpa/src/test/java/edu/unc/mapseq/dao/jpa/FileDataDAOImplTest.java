package edu.unc.mapseq.dao.jpa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Attribute;
import edu.unc.mapseq.dao.model.FileData;
import edu.unc.mapseq.dao.model.Flowcell;
import edu.unc.mapseq.dao.model.Job;
import edu.unc.mapseq.dao.model.MimeType;
import edu.unc.mapseq.dao.model.Sample;

public class FileDataDAOImplTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
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
    public void fixNCGenesPaths() throws MaPSeqDAOException {

        StudyDAOImpl studyDAO = new StudyDAOImpl();
        studyDAO.setEntityManager(em);

        FlowcellDAOImpl flowcellDAO = new FlowcellDAOImpl();
        flowcellDAO.setEntityManager(em);

        SampleDAOImpl sampleDAO = new SampleDAOImpl();
        sampleDAO.setEntityManager(em);

        FileDataDAOImpl fileDataDAO = new FileDataDAOImpl();
        fileDataDAO.setEntityManager(em);

        AttributeDAOImpl attributeDAO = new AttributeDAOImpl();
        attributeDAO.setEntityManager(em);

        List<Flowcell> flowcells = flowcellDAO.findByStudyId(2L);

        if (CollectionUtils.isNotEmpty(flowcells)) {
            for (Flowcell flowcell : flowcells) {

                flowcell.setBaseDirectory("/projects/sequence_analysis/medgenwork/NC_GENES/BCL");
                flowcell.setAttributes(new HashSet<Attribute>());
                flowcell.setFileDatas(new HashSet<FileData>());
                em.getTransaction().begin();
                flowcellDAO.save(flowcell);
                em.getTransaction().commit();

                Attribute attribute = new Attribute("readCount", "2");
                attribute.setId(attributeDAO.save(attribute));
                flowcell.getAttributes().add(attribute);

                FileData sampleSheetFileData = new FileData(String.format("%s.csv", flowcell.getName()),
                        "/projects/sequence_analysis/medgenwork/NC_GENES/SampleSheets", MimeType.TEXT_CSV);
                sampleSheetFileData.setId(fileDataDAO.save(sampleSheetFileData));
                flowcell.getFileDatas().add(sampleSheetFileData);
                em.getTransaction().begin();
                flowcellDAO.save(flowcell);
                em.getTransaction().commit();

                List<Sample> samples = sampleDAO.findByFlowcellId(flowcell.getId());
                if (CollectionUtils.isNotEmpty(samples)) {
                    for (Sample sample : samples) {

                        String outputDirectory = sample.getOutputDirectory();
                        if (StringUtils.isNotEmpty(outputDirectory)) {
                            sample.setOutputDirectory(outputDirectory.replace("/proj/seq/mapseq/RENCI",
                                    "/projects/sequence_analysis/medgenwork/NC_GENES/analysis"));
                        } else {
                            sample.setOutputDirectory(String.format("%s/%s/L%03d_%s",
                                    "/projects/sequence_analysis/medgenwork/NC_GENES/analysis",
                                    sample.getFlowcell().getName(), sample.getLaneIndex(), sample.getBarcode()));
                        }

                        Set<FileData> sampleFileDataSet = sample.getFileDatas();

                        if (CollectionUtils.isNotEmpty(sampleFileDataSet)) {
                            for (FileData fileData : sampleFileDataSet) {
                                String path = fileData.getPath();
                                fileData.setPath(path.replace("/proj/seq/mapseq/RENCI",
                                        "/projects/sequence_analysis/medgenwork/NC_GENES/analysis"));
                                em.getTransaction().begin();
                                fileDataDAO.save(fileData);
                                em.getTransaction().commit();

                            }
                        }

                        em.getTransaction().begin();
                        sampleDAO.save(sample);
                        em.getTransaction().commit();

                    }
                }

            }
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

}
