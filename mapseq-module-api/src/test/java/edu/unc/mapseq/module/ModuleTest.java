package edu.unc.mapseq.module;

import java.io.File;
import java.util.concurrent.Executors;

import org.junit.Test;

import edu.unc.mapseq.dao.jpa.AttributeDAOImpl;
import edu.unc.mapseq.dao.jpa.FileDataDAOImpl;
import edu.unc.mapseq.dao.jpa.JobDAOImpl;
import edu.unc.mapseq.dao.jpa.MaPSeqDAOBeanServiceImpl;
import edu.unc.mapseq.dao.jpa.SampleDAOImpl;
import edu.unc.mapseq.dao.jpa.WorkflowRunAttemptDAOImpl;
import edu.unc.mapseq.dao.jpa.WorkflowRunDAOImpl;

public class ModuleTest {

    @Test
    public void testUnzipModule() {

        MaPSeqDAOBeanServiceImpl daoBean = new MaPSeqDAOBeanServiceImpl();
        daoBean.setWorkflowRunDAO(new WorkflowRunDAOImpl());
        daoBean.setWorkflowRunAttemptDAO(new WorkflowRunAttemptDAOImpl());
        daoBean.setJobDAO(new JobDAOImpl());
        daoBean.setFileDataDAO(new FileDataDAOImpl());
        daoBean.setSampleDAO(new SampleDAOImpl());
        daoBean.setAttributeDAO(new AttributeDAOImpl());

        UnzipModule app = new UnzipModule();
        app.setZip(new File("/tmp", "asdf.zip"));
        app.setExtract(new File("/home/jdr0887/test"));
        app.setWorkflowName("TEST");
        app.setDryRun(Boolean.TRUE);

        try {
            ModuleExecutor executor = new ModuleExecutor();
            executor.setModule(app);
            if (app.getDryRun()) {
                executor.addObserver(new DryRunJobObserver());
            } else {
                executor.setDaoBean(daoBean);
                executor.addObserver(new PersistantJobObserver(daoBean));
            }
            ModuleOutput output = Executors.newSingleThreadExecutor().submit(executor).get();
            // System.out.println(output.getExitCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEchoModule() {

        MaPSeqDAOBeanServiceImpl daoBean = new MaPSeqDAOBeanServiceImpl();
        daoBean.setWorkflowRunDAO(new WorkflowRunDAOImpl());
        daoBean.setWorkflowRunAttemptDAO(new WorkflowRunAttemptDAOImpl());
        daoBean.setJobDAO(new JobDAOImpl());
        daoBean.setFileDataDAO(new FileDataDAOImpl());
        daoBean.setSampleDAO(new SampleDAOImpl());
        daoBean.setAttributeDAO(new AttributeDAOImpl());

        EchoModule app = new EchoModule();
        app.setMessage("asdfasdfasd");
        app.setOutput(new File("/home/jdr0887/test/echomodule.txt"));
        app.setWorkflowName("TEST");
        app.setDryRun(Boolean.TRUE);

        try {
            ModuleExecutor executor = new ModuleExecutor();
            executor.setModule(app);
            if (app.getDryRun()) {
                executor.addObserver(new DryRunJobObserver());
            } else {
                executor.setDaoBean(daoBean);
                executor.addObserver(new PersistantJobObserver(daoBean));
            }
            ModuleOutput output = Executors.newSingleThreadExecutor().submit(executor).get();
            // System.out.println(output.getExitCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
