package edu.unc.mapseq.dao.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TestSerialization {

    @Test
    public void testWorkflowPlanSerialization() {

        Flowcell flowcell = new Flowcell();
        flowcell.setId(3L);
        flowcell.setBaseDirectory("asdfasdf");
        flowcell.setName("asdfasdf");

        Set<Attribute> attributeSet = new HashSet<Attribute>();

        Attribute attribute = new Attribute();
        attribute.setId(2L);
        attribute.setName("foo");
        attribute.setValue("bar");
        attributeSet.add(attribute);

        // attribute = new EntityAttribute();
        // attribute.setId(3L);
        // attribute.setName("fuzz");
        // attribute.setValue("buzz");
        // attributeSet.add(attribute);

        flowcell.setAttributes(attributeSet);

        WorkflowRun workflowRun = new WorkflowRun();
        workflowRun.setId(4L);
        // workflowRun.setStatus(WorkflowRunStatusType.PENDING);
        workflowRun.setName("asdfasdf");

        WorkflowRunAttempt workflowPlan = new WorkflowRunAttempt();
        workflowPlan.setId(2L);
        // workflowPlan.setFlowcell(flowcell);
        workflowPlan.setWorkflowRun(workflowRun);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
        try {
            FileWriter writer = new FileWriter(new File("/tmp/workflowPlan.json"));
            mapper.writeValue(writer, workflowPlan);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // try {
        // JAXBContext context = JAXBContext.newInstance(WorkflowPlan.class);
        //
        // Configuration config = new Configuration();
        // MappedNamespaceConvention con = new MappedNamespaceConvention(config);
        // FileWriter writer = new FileWriter(new File("/tmp/workflowPlan.json"));
        // XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(con, writer);
        // Marshaller m = context.createMarshaller();
        // m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // m.marshal(workflowPlan, xmlStreamWriter);
        // } catch (IOException e1) {
        // e1.printStackTrace();
        // } catch (PropertyException e1) {
        // e1.printStackTrace();
        // } catch (JAXBException e1) {
        // e1.printStackTrace();
        // }

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
    public void testWorkflowXMLSerialization() {
        Workflow workflow = new Workflow("asdfdsa");
        try {
            JAXBContext context = JAXBContext.newInstance(Workflow.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            FileWriter fw = new FileWriter(new File("/tmp/workflow.xml"));
            m.marshal(workflow, fw);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (PropertyException e1) {
            e1.printStackTrace();
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void testWorkflowJSONSerialization() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter ow = mapper.writer();
            ow.writeValue(new File("/tmp/workflow.json"),
                    new ASDF(Arrays.asList(new Workflow("qwer"), new Workflow("asdf"), new Workflow("zxcv"))));
            // FileWriter writer = new FileWriter(new File("/tmp/workflow.json"));
            // mapper.writeValue(writer,
            // new ASDF(Arrays.asList(new Workflow("qwer"), new Workflow("asdf"), new Workflow("zxcv"))));
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJobSerialization() {

        WorkflowRun workflowRun = new WorkflowRun();
        workflowRun.setId(4L);
        workflowRun.setName("asdfadf");

        WorkflowRunAttempt workflowRunAttempt = new WorkflowRunAttempt();
        workflowRunAttempt.setId(4L);
        workflowRunAttempt.setStatus(WorkflowRunAttemptStatusType.PENDING);
        workflowRun.setName("asdfasdf");

        Set<Attribute> attributeSet = new HashSet<Attribute>();

        Attribute attribute = new Attribute();
        attribute.setId(2L);
        attribute.setName("foo");
        attribute.setValue("bar");
        attributeSet.add(attribute);

        attribute = new Attribute();
        attribute.setId(3L);
        attribute.setName("fuzz");
        attribute.setValue("buzz");
        attributeSet.add(attribute);

        workflowRun.setAttributes(attributeSet);

        Job job = new Job();
        job.setStarted(new Date());
        job.setId(1L);
        job.setWorkflowRunAttempt(workflowRunAttempt);
        job.setName("asdf");
        job.setStatus(JobStatusType.RUNNING);
        job.setExitCode(0);
        job.setFinished(new Date());
        job.setStderr("err");
        job.setStdout("out");

        job.setAttributes(attributeSet);

        Set<FileData> fileDataSet = new HashSet<FileData>();
        FileData fileData = new FileData();
        fileData.setId(2L);
        fileData.setMimeType(MimeType.APPLICATION_GZIP);
        fileData.setName("asdf.fastq.gz");
        fileData.setPath("/tmp");

        fileDataSet.add(fileData);
        job.setFileDatas(fileDataSet);
        try {
            JAXBContext context = JAXBContext.newInstance(WorkflowRunAttempt.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            FileWriter fw = new FileWriter(new File("/tmp/job.xml"));
            m.marshal(job, fw);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (PropertyException e1) {
            e1.printStackTrace();
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }

    }

}
