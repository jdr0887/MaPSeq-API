package edu.unc.mapseq.dao.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.model.Flowcell;
import edu.unc.mapseq.dao.model.Sample;
import edu.unc.mapseq.dao.model.Study;

public class LoadFlowcellData {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void persistD0J7WACXX() {

        Map<Integer, Map<String, String>> laneSampleIdAndBarcodeMap = new HashMap<Integer, Map<String, String>>();
        Map<String, String> sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BT-A20J-01A-11R-A14Y-07", "CGATGT");
        sampleIdAndBarcodeMap.put("TCGA-BT-A20N-01A-11R-A14Y-07", "CTTGTA");
        laneSampleIdAndBarcodeMap.put(1, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BT-A20O-01A-21R-A14Y-07", "GCCAAT");
        sampleIdAndBarcodeMap.put("TCGA-BT-A20N-11A-11R-A14Y-07", "GGCTAC");
        laneSampleIdAndBarcodeMap.put(2, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BT-A20P-01A-11R-A14Y-07", "ACTTGA");
        sampleIdAndBarcodeMap.put("TCGA-BT-A20Q-01A-11R-A14Y-07", "TAGCTT");
        laneSampleIdAndBarcodeMap.put(3, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BT-A20Q-11A-11R-A14Y-07", "GATCAG");
        sampleIdAndBarcodeMap.put("TCGA-BT-A20T-01A-11R-A14Y-07", "GCCAAT");
        laneSampleIdAndBarcodeMap.put(4, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BT-A20U-01A-11R-A14Y-07", "ACAGTG");
        sampleIdAndBarcodeMap.put("TCGA-BT-A20U-11A-11R-A14Y-07", "ATCACG");
        laneSampleIdAndBarcodeMap.put(5, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BT-A20W-01A-21R-A14Y-07", "CAGATC");
        sampleIdAndBarcodeMap.put("TCGA-BT-A20V-01A-11R-A14Y-07", "TGACCA");
        laneSampleIdAndBarcodeMap.put(6, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BL-A0C8-01A-11R-A10U-07", "ATCACG");
        sampleIdAndBarcodeMap.put("TCGA-BT-A20W-11A-11R-A14Y-07", "TTAGGC");
        laneSampleIdAndBarcodeMap.put(7, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BL-A13J-01A-11R-A10U-07", "GATCAG");
        sampleIdAndBarcodeMap.put("TCGA-BL-A13J-11A-13R-A10U-07", "TTAGGC");
        laneSampleIdAndBarcodeMap.put(8, sampleIdAndBarcodeMap);

        persistTestCell(laneSampleIdAndBarcodeMap, "120110_UNC13-SN749_0141_AD0J7WACXX",
                "120110_UNC13-SN749_0141_AD0J7WACXX");

    }

    @Test
    public void persistD0HK1ACXX() {

        Map<Integer, Map<String, String>> laneSampleIdAndBarcodeMap = new HashMap<Integer, Map<String, String>>();
        Map<String, String> sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BT-A0YX-01A-11R-A10U-07", "GCCAAT");
        sampleIdAndBarcodeMap.put("TCGA-BT-A0S7-01A-11R-A10U-07", "GGCTAC");
        laneSampleIdAndBarcodeMap.put(1, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-C4-A0F0-01A-12R-A10U-07", "ACAGTG");
        sampleIdAndBarcodeMap.put("TCGA-C4-A0EZ-01A-21R-A10U-07", "TTAGGC");
        laneSampleIdAndBarcodeMap.put(2, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-C4-A0F6-01A-11R-A10U-07", "ACTTGA");
        sampleIdAndBarcodeMap.put("TCGA-C4-A0F1-01A-11R-A10U-07", "CAGATC");
        laneSampleIdAndBarcodeMap.put(3, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-CU-A0YN-01A-21R-A10U-07", "ATCACG");
        sampleIdAndBarcodeMap.put("TCGA-C4-A0F7-01A-11R-A10U-07", "TAGCTT");
        laneSampleIdAndBarcodeMap.put(4, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-CU-A0YN-11A-11R-A10U-07", "CGATGT");
        sampleIdAndBarcodeMap.put("TCGA-CU-A0YR-01A-12R-A10U-07", "TGACCA");
        laneSampleIdAndBarcodeMap.put(5, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-CU-A0YO-01A-11R-A10U-07", "CGATGT");
        sampleIdAndBarcodeMap.put("TCGA-CU-A0YR-11A-13R-A10U-07", "CTTGTA");
        laneSampleIdAndBarcodeMap.put(6, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-BL-A13I-01A-11R-A13Y-07", "ACTTGA");
        sampleIdAndBarcodeMap.put("TCGA-CF-A1HR-01A-11R-A13Y-07", "TGACCA");
        laneSampleIdAndBarcodeMap.put(7, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-DK-A1A3-01A-11R-A13Y-07", "ATCACG");
        sampleIdAndBarcodeMap.put("TCGA-CF-A1HS-01A-11R-A13Y-07", "GCCAAT");
        laneSampleIdAndBarcodeMap.put(8, sampleIdAndBarcodeMap);

        persistTestCell(laneSampleIdAndBarcodeMap, "120110_UNC13-SN749_0142_BD0HK1ACXX",
                "120110_UNC13-SN749_0142_BD0HK1ACXX");

    }

    @Test
    public void persistC0FDJACXX() {

        Map<Integer, Map<String, String>> laneSampleIdAndBarcodeMap = new HashMap<Integer, Map<String, String>>();
        Map<String, String> sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-D3-A2J8-06A-11R-A18T-07", "CTTGTA");
        sampleIdAndBarcodeMap.put("TCGA-D3-A1Q1-06A-21R-A18T-07", "GGCTAC");
        laneSampleIdAndBarcodeMap.put(1, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-D3-A1QA-06A-11R-A18T-07", "GGCTAC");
        sampleIdAndBarcodeMap.put("TCGA-DA-A1I8-06A-11R-A18T-07", "TAGCTT");
        laneSampleIdAndBarcodeMap.put(2, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-D3-A2JK-06A-11R-A18S-07", "ACTTGA");
        sampleIdAndBarcodeMap.put("TCGA-EB-A1NK-01A-11R-A18T-07", "GGCTAC");
        laneSampleIdAndBarcodeMap.put(3, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-EB-A24D-01A-11R-A18T-07", "GGCTAC");
        sampleIdAndBarcodeMap.put("TCGA-G2-A2EF-01A-12R-A18C-07", "TTAGGC");
        laneSampleIdAndBarcodeMap.put(4, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-EE-A2A5-06A-11R-A18T-07", "CTTGTA");
        sampleIdAndBarcodeMap.put("TCGA-FS-A1YX-06A-11R-A18T-07", "TAGCTT");
        laneSampleIdAndBarcodeMap.put(5, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-E5-A2PC-01A-11R-A206-07", "ACTTGA");
        sampleIdAndBarcodeMap.put("TCGA-EE-A2GE-06A-11R-A18T-07", "CTTGTA");
        laneSampleIdAndBarcodeMap.put(6, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-EE-A2MD-06A-11R-A18T-07", "CTTGTA");
        sampleIdAndBarcodeMap.put("TCGA-FT-A3EE-01A-11R-A206-07", "GCCAAT");
        laneSampleIdAndBarcodeMap.put(7, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-FS-A1YY-06A-11R-A18T-07", "CTTGTA");
        sampleIdAndBarcodeMap.put("TCGA-G2-A2EK-01A-22R-A18C-07", "GGCTAC");
        laneSampleIdAndBarcodeMap.put(8, sampleIdAndBarcodeMap);

        persistTestCell(laneSampleIdAndBarcodeMap, "120124_UNC16-SN851_0123_AC0FDJACXX",
                "120124_UNC16-SN851_0123_AC0FDJACXX");

    }

    @Test
    public void persistC0C16ACXX() {

        Map<Integer, Map<String, String>> laneSampleIdAndBarcodeMap = new HashMap<Integer, Map<String, String>>();
        Map<String, String> sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-G2-A2EL-01A-12R-A18C-07", "CTTGTA");
        sampleIdAndBarcodeMap.put("TCGA-EE-A29G-06A-12R-A18T-07", "GGCTAC");
        laneSampleIdAndBarcodeMap.put(1, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-EE-A29X-06A-11R-A18T-07", "GGCTAC");
        sampleIdAndBarcodeMap.put("TCGA-ER-A19T-01A-11R-A18T-07", "TAGCTT");
        laneSampleIdAndBarcodeMap.put(2, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-ER-A2NB-01A-12R-A18S-07", "ACTTGA");
        sampleIdAndBarcodeMap.put("TCGA-FS-A1YW-06A-11R-A18T-07", "GGCTAC");
        laneSampleIdAndBarcodeMap.put(3, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-FS-A1ZU-06A-12R-A18T-07", "GGCTAC");
        sampleIdAndBarcodeMap.put("TCGA-FD-A3B3-01A-12R-A206-07", "TGACCA");
        laneSampleIdAndBarcodeMap.put(4, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-FS-A1ZG-06A-11R-A18T-07", "CTTGTA");
        sampleIdAndBarcodeMap.put("TCGA-FS-A1ZK-06A-11R-A18T-07", "TAGCTT");
        laneSampleIdAndBarcodeMap.put(5, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-FD-A3B4-01A-12R-A206-07", "ACAGTG");
        sampleIdAndBarcodeMap.put("TCGA-FS-A1ZH-06A-11R-A18T-07", "CTTGTA");
        laneSampleIdAndBarcodeMap.put(6, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-GN-A26D-06A-11R-A18T-07", "CTTGTA");
        sampleIdAndBarcodeMap.put("TCGA-GN-A262-06A-11R-A18T-07", "TAGCTT");
        laneSampleIdAndBarcodeMap.put(7, sampleIdAndBarcodeMap);
        sampleIdAndBarcodeMap = new HashMap<String, String>();
        sampleIdAndBarcodeMap.put("TCGA-HQ-A2OE-01A-11R-A206-07", "CAGATC");
        sampleIdAndBarcodeMap.put("TCGA-GN-A26C-01A-11R-A18T-07", "TAGCTT");
        laneSampleIdAndBarcodeMap.put(8, sampleIdAndBarcodeMap);

        persistTestCell(laneSampleIdAndBarcodeMap, "120124_UNC16-SN851_0124_BC0C16ACXX",
                "120124_UNC16-SN851_0124_BC0C16ACXX");

    }

    private Flowcell persistTestCell(Map<Integer, Map<String, String>> laneSampleIdAndBarcodeMap, String studyName,
            String sequencerRunName) {

        Study study = new Study();
        study.setName(studyName);

        em.getTransaction().begin();
        em.persist(study);
        em.getTransaction().commit();

        Flowcell flowcell = new Flowcell();
        flowcell.setName(sequencerRunName);
        flowcell.setBaseDirectory("/proj/.test/roach/TestCells");

        em.getTransaction().begin();
        em.persist(flowcell);
        em.getTransaction().commit();

        for (Integer laneIndex : laneSampleIdAndBarcodeMap.keySet()) {

            for (String sampleId : laneSampleIdAndBarcodeMap.get(laneIndex).keySet()) {
                String barcode = laneSampleIdAndBarcodeMap.get(laneIndex).get(sampleId);

                Sample htsfSample = new Sample();
                htsfSample.setName(sampleId);
                htsfSample.setLaneIndex(laneIndex);
                htsfSample.setFlowcell(flowcell);
                htsfSample.setStudy(study);
                htsfSample.setBarcode(barcode);

                em.getTransaction().begin();
                em.persist(htsfSample);
                em.getTransaction().commit();

            }

        }

        return flowcell;
    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

}
