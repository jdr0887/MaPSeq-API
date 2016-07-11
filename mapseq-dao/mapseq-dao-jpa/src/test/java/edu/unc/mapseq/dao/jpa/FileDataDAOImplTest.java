package edu.unc.mapseq.dao.jpa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
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
    public void regexTest() throws MaPSeqDAOException {
        String asdf = "/proj/seq/mapseq/RENCI/121008_UNC14-SN744_0273_AD1526ACXX/NCGenes/OPH_00130-LT_2";
        Matcher m = Pattern.compile("/proj/seq/mapseq/RENCI/(?<flowcell>.+)/(?<workflow>.+)/(?<sampleName>.+)")
                .matcher(asdf);
        if (m.matches()) {
            String flowcell = m.group("flowcell");
            System.out.println(flowcell);
            String workflow = m.group("workflow");
            System.out.println(workflow);
            String sampleName = m.group("sampleName");
            System.out.println(sampleName);
        }
        asdf = "121008_UNC14-SN744_0273_AD1526ACXX_GCCAAT_L007_R1.sai";
        m = Pattern.compile("121008_UNC14-SN744_0273_AD1526ACXX_(?<barcode>[ATCG]+)_L(?<lane>\\d+).+").matcher(asdf);
        if (m.matches()) {
            String barcode = m.group("barcode");
            System.out.println(barcode);
            String lane = m.group("lane");
            System.out.println(Integer.valueOf(lane));
        }
    }

    @Test
    public void fixNCGenesPaths() throws MaPSeqDAOException {

        StudyDAOImpl studyDAO = new StudyDAOImpl();
        studyDAO.setEntityManager(em);

        FlowcellDAOImpl flowcellDAO = new FlowcellDAOImpl();
        flowcellDAO.setEntityManager(em);

        WorkflowRunDAOImpl workflowRunDAO = new WorkflowRunDAOImpl();
        workflowRunDAO.setEntityManager(em);

        SampleDAOImpl sampleDAO = new SampleDAOImpl();
        sampleDAO.setEntityManager(em);

        FileDataDAOImpl fileDataDAO = new FileDataDAOImpl();
        fileDataDAO.setEntityManager(em);

        AttributeDAOImpl attributeDAO = new AttributeDAOImpl();
        attributeDAO.setEntityManager(em);

        Map<Long, String> fileDataPathMap = new HashMap<>();

        fileDataPathMap.put(121137L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L006_ACAGTG/NCGenesBaseline");
        fileDataPathMap.put(121139L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L006_TGACCA/NCGenesBaseline");
        fileDataPathMap.put(121155L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L006_CGATGT/NCGenesBaseline");
        fileDataPathMap.put(122623L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L007_GCCAAT/NCGenesBaseline");
        fileDataPathMap.put(122493L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L006_GCCAAT/NCGenesBaseline");
        fileDataPathMap.put(122494L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L007_TGACCA/NCGenesBaseline");
        fileDataPathMap.put(122513L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L007_CGATGT/NCGenesBaseline");
        fileDataPathMap.put(122616L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L007_ACAGTG/NCGenesBaseline");
        fileDataPathMap.put(129736L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_GCCAAT/NCGenesBaseline");
        fileDataPathMap.put(129746L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_CAGATC/NCGenesBaseline");
        fileDataPathMap.put(129769L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_GCCAAT/NCGenesBaseline");
        fileDataPathMap.put(129733L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_TGACCA/NCGenesBaseline");
        fileDataPathMap.put(129735L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_TGACCA/NCGenesBaseline");
        fileDataPathMap.put(129738L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_ACTTGA/NCGenesBaseline");
        fileDataPathMap.put(129739L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_ACAGTG/NCGenesBaseline");
        fileDataPathMap.put(129756L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_ACAGTG/NCGenesBaseline");
        fileDataPathMap.put(443044L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_GCCAAT/NCGenesBaseline");
        fileDataPathMap.put(443105L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_GCCAAT/NCGenesBaseline");
        fileDataPathMap.put(443106L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_TTAGGC/NCGenesBaseline");
        fileDataPathMap.put(443130L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_CGATGT/NCGenesBaseline");
        fileDataPathMap.put(443135L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_ATCACG/NCGenesBaseline");
        fileDataPathMap.put(443137L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_TTAGGC/NCGenesBaseline");
        fileDataPathMap.put(443136L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120517_UNC15-SN850_0212_AD0VD1ACXX/L004_ACAGTG/NCGenesBaseline");
        fileDataPathMap.put(443132L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120517_UNC15-SN850_0212_AD0VD1ACXX/L004_TGACCA/NCGenesBaseline");
        fileDataPathMap.put(443133L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_CGATGT/NCGenesBaseline");
        fileDataPathMap.put(443011L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/121008_UNC14-SN744_0273_AD1526ACXX/L006_ACAGTG/NCGenesBaseline");
        fileDataPathMap.put(443129L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120817_UNC16-SN851_0178_AD13KNACXX/L002_ATCACG/NCGenesBaseline");
        fileDataPathMap.put(443103L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_ATCACG/NCGenesBaseline");
        fileDataPathMap.put(443019L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_TGACCA/NCGenesBaseline");
        fileDataPathMap.put(443107L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_TTAGGC/NCGenesBaseline");
        fileDataPathMap.put(443020L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_TGACCA/NCGenesBaseline");
        fileDataPathMap.put(443104L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_ACAGTG/NCGenesBaseline");
        fileDataPathMap.put(443108L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_ATCACG/NCGenesBaseline");
        fileDataPathMap.put(443109L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_CGATGT/NCGenesBaseline");
        fileDataPathMap.put(443112L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120712_UNC13-SN749_0183_AD13LKACXX/L001_CGATGT/NCGenesBaseline");
        fileDataPathMap.put(443123L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120517_UNC15-SN850_0212_AD0VD1ACXX/L004_CGATGT/NCGenesBaseline");
        fileDataPathMap.put(443134L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120517_UNC15-SN850_0212_AD0VD1ACXX/L003_GCCAAT/NCGenesBaseline");
        fileDataPathMap.put(107757L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120517_UNC15-SN850_0212_AD0VD1ACXX/L003_CAGATC/NCGenesBaseline");
        fileDataPathMap.put(443131L,
                "/projects/sequence_analysis/medgenwork/NC_GENES/analysis/120517_UNC15-SN850_0212_AD0VD1ACXX/L004_GCCAAT/NCGenesBaseline");

        List<FileData> fileDataList = fileDataDAO.findAll();
        if (CollectionUtils.isNotEmpty(fileDataList)) {

            for (FileData fileData : fileDataList) {

                if (fileData.getId().equals(222146L)) {
                    fileData.setName(
                            "130522_UNC11-SN627_0299_AD25TUACXX_ATCACG_L001.fixed-rg.deduped.realign.fixmate.recal.coverage.v16.gene.sample_summary");
                    em.getTransaction().begin();
                    fileDataDAO.save(fileData);
                    em.getTransaction().commit();
                    continue;
                }

                if (fileDataPathMap.containsKey(fileData.getId())) {
                    fileData.setPath(fileDataPathMap.get(fileData.getId()));
                    em.getTransaction().begin();
                    fileDataDAO.save(fileData);
                    em.getTransaction().commit();
                    continue;
                }

                if (fileData.getPath().startsWith("/proj/seq/mapseq/RENCI") && fileData.getPath().endsWith("NCGenes")) {
                    String path = fileData.getPath().replace("NCGenes", "NCGenesBaseline").replace(
                            "/proj/seq/mapseq/RENCI", "/projects/sequence_analysis/medgenwork/prod/NC_GENES/analysis");
                    fileData.setPath(path);
                    em.getTransaction().begin();
                    fileDataDAO.save(fileData);
                    em.getTransaction().commit();
                    continue;
                }

                Matcher m = Pattern.compile("/proj/seq/mapseq/RENCI/(?<flowcell>.+)/(?<workflow>.+)/(?<sampleName>.+)")
                        .matcher(fileData.getPath());
                if (m.matches()) {
                    String flowcell = m.group("flowcell");
                    String workflow = m.group("workflow");
                    String sampleName = m.group("sampleName");

                    if (workflow.equals("CASAVA")) {

                        Matcher newMatcher = Pattern
                                .compile(String.format("^%s_(?<barcode>[ATCG]+)_L(?<lane>\\d+).+", flowcell))
                                .matcher(fileData.getName());

                        if (newMatcher.matches()) {
                            String barcode = newMatcher.group("barcode");
                            String lane = newMatcher.group("lane");

                            List<Sample> sampleList = sampleDAO.findByName(sampleName);
                            Sample sample = null;
                            for (Sample s : sampleList) {
                                if (s.getLaneIndex().equals(Integer.valueOf(lane)) && s.getBarcode().equals(barcode)) {
                                    sample = s;
                                    break;
                                }
                            }

                            if (sample == null) {
                                String path = String.format(
                                        "/projects/sequence_analysis/medgenwork/prod/NC_GENES/analysis/%s/L%03d_%s/NCGenesCASAVA",
                                        flowcell, Integer.valueOf(lane), barcode);
                                fileData.setPath(path);
                            } else {
                                String path = String.format(
                                        "/projects/sequence_analysis/medgenwork/prod/NC_GENES/analysis/%s/L%03d_%s/NCGenesCASAVA",
                                        sample.getFlowcell().getName(), sample.getLaneIndex(), sample.getBarcode());
                                fileData.setPath(path);
                            }
                            em.getTransaction().begin();
                            fileDataDAO.save(fileData);
                            em.getTransaction().commit();
                            continue;

                        }

                    }

                    if (workflow.equals("NCGenes")) {

                        Matcher newMatcher = Pattern
                                .compile(String.format("^%s_(?<barcode>[ATCG]+)_L(?<lane>\\d+).+", flowcell))
                                .matcher(fileData.getName());

                        if (newMatcher.matches()) {
                            String barcode = newMatcher.group("barcode");
                            String lane = newMatcher.group("lane");

                            List<Sample> sampleList = sampleDAO.findByName(sampleName);
                            Sample sample = null;
                            for (Sample s : sampleList) {
                                if (s.getLaneIndex().equals(Integer.valueOf(lane)) && s.getBarcode().equals(barcode)) {
                                    sample = s;
                                    break;
                                }
                            }

                            if (sample == null) {
                                String path = String.format(
                                        "/projects/sequence_analysis/medgenwork/prod/NC_GENES/analysis/%s/L%03d_%s/NCGenesBaseline",
                                        flowcell, Integer.valueOf(lane), barcode);
                                fileData.setPath(path);
                            } else {
                                String path = String.format(
                                        "/projects/sequence_analysis/medgenwork/prod/NC_GENES/analysis/%s/L%03d_%s/NCGenesBaseline",
                                        sample.getFlowcell().getName(), sample.getLaneIndex(), sample.getBarcode());
                                fileData.setPath(path);
                            }
                            em.getTransaction().begin();
                            fileDataDAO.save(fileData);
                            em.getTransaction().commit();
                            continue;

                        }

                    }

                }

                m = Pattern
                        .compile("/proj/seq/mapseq/RENCI/(?<flowcell>.+)/L(?<lane>\\d+)_(?<barcode>.+)/(?<workflow>.+)")
                        .matcher(fileData.getPath());
                if (m.matches()) {
                    String flowcell = m.group("flowcell");
                    String workflow = m.group("workflow");
                    String lane = m.group("lane");
                    String barcode = m.group("barcode");

                    if (workflow.equals("NCGenesDX")) {

                        List<Sample> sampleList = sampleDAO.findByLaneIndexAndBarcode(Integer.valueOf(lane), barcode);
                        if (CollectionUtils.isNotEmpty(sampleList)) {
                            Sample sample = sampleList.get(0);
                            String path = String.format(
                                    "/projects/sequence_analysis/medgenwork/prod/NC_GENES/analysis/%s/L%03d_%s/NCGenesDX",
                                    sample.getFlowcell().getName(), sample.getLaneIndex(), sample.getBarcode());
                            fileData.setPath(path);
                            em.getTransaction().begin();
                            fileDataDAO.save(fileData);
                            em.getTransaction().commit();
                            continue;

                        }

                    }

                    if (workflow.equals("CASAVA")) {

                        List<Sample> sampleList = sampleDAO.findByLaneIndexAndBarcode(Integer.valueOf(lane), barcode);
                        if (CollectionUtils.isNotEmpty(sampleList)) {
                            Sample sample = sampleList.get(0);
                            String path = String.format(
                                    "/projects/sequence_analysis/medgenwork/prod/NC_GENES/analysis/%s/L%03d_%s/NCGenesCASAVA",
                                    sample.getFlowcell().getName(), sample.getLaneIndex(), sample.getBarcode());
                            fileData.setPath(path);
                            em.getTransaction().begin();
                            fileDataDAO.save(fileData);
                            em.getTransaction().commit();
                            continue;
                        }

                    }

                }

            }

        }

    }

    @Test
    public void writeAllFileDatas() throws MaPSeqDAOException {

        FileDataDAOImpl fileDataDAO = new FileDataDAOImpl();
        fileDataDAO.setEntityManager(em);

        List<FileData> fileDataList = fileDataDAO.findAll();
        try (FileWriter fw = new FileWriter(new File("/tmp", "fileData.out"));
                BufferedWriter bw = new BufferedWriter(fw)) {
            if (CollectionUtils.isNotEmpty(fileDataList)) {
                for (FileData fileData : fileDataList) {
                    bw.write(fileData.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
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

}
