package edu.unc.mapseq.dao.jpa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Sample;

public class Scratch {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-mapseq", null);
        em = emf.createEntityManager();
    }

    @Test
    public void dx() {

        SampleDAOImpl sampleDAO = new SampleDAOImpl();
        sampleDAO.setEntityManager(em);

        try (FileWriter fw = new FileWriter(new File("/tmp", "NCGenesDXiRODSRegistrationMap.txt"));
                BufferedWriter bw = new BufferedWriter(fw)) {
            Reader in = new FileReader("/tmp/diagnosticBinningJobs.txt");
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                String version = record.get(0);
                String dx = record.get(1);
                String sampleName = record.get(2);
                String flowcellBarcodeLane = record.get(3);

                String flowcellName = flowcellBarcodeLane.substring(0, flowcellBarcodeLane.length() - 5);
                flowcellName = flowcellName.substring(0, flowcellName.lastIndexOf("_"));
                System.out.println(flowcellName);

                Integer laneIndex = Integer.valueOf(
                        flowcellBarcodeLane.substring(flowcellBarcodeLane.length() - 3, flowcellBarcodeLane.length()));
                System.out.println(laneIndex);

                Sample sample = null;
                List<Sample> samples = sampleDAO.findByFlowcellNameAndSampleNameAndLaneIndex(flowcellName, sampleName,
                        laneIndex);
                if (CollectionUtils.isNotEmpty(samples)) {
                    sample = samples.get(0);
                    bw.write(String.format("ncgenes-dx:register-to-irods %s %s %s", sample.getId().toString(), version, dx));
                    bw.newLine();
                    bw.flush();
                }

            }
        } catch (IOException | MaPSeqDAOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void incidental() {

        SampleDAOImpl sampleDAO = new SampleDAOImpl();
        sampleDAO.setEntityManager(em);

        try (FileWriter fw = new FileWriter(new File("/tmp", "NCGenesIncidentaliRODSRegistrationMap.txt"));
                BufferedWriter bw = new BufferedWriter(fw)) {
            Reader in = new FileReader("/tmp/incidentalBinningJobs.txt");
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord record : records) {
                String version = record.get(0);
                String incidental = record.get(1);
                String sampleName = record.get(2);
                String flowcellBarcodeLane = record.get(3);

                String flowcellName = flowcellBarcodeLane.substring(0, flowcellBarcodeLane.length() - 5);
                flowcellName = flowcellName.substring(0, flowcellName.lastIndexOf("_"));
                System.out.println(flowcellName);

                Integer laneIndex = Integer.valueOf(
                        flowcellBarcodeLane.substring(flowcellBarcodeLane.length() - 3, flowcellBarcodeLane.length()));
                System.out.println(laneIndex);

                Sample sample = null;
                List<Sample> samples = sampleDAO.findByFlowcellNameAndSampleNameAndLaneIndex(flowcellName, sampleName,
                        laneIndex);
                if (CollectionUtils.isNotEmpty(samples)) {
                    sample = samples.get(0);
                    bw.write(String.format("ncgenes-incidental-variantcalling:register-to-irods %s %s %s", sample.getId().toString(), version, incidental));
                    bw.newLine();
                    bw.flush();
                }

            }
        } catch (IOException | MaPSeqDAOException e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

}
