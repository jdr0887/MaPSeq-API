package edu.unc.mapseq.module;

import java.io.File;
import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;

import edu.unc.mapseq.dao.MaPSeqDAOBean;
import edu.unc.mapseq.dao.jpa.FileDataDAOImpl;
import edu.unc.mapseq.dao.jpa.JobDAOImpl;
import edu.unc.mapseq.dao.jpa.SampleDAOImpl;
import edu.unc.mapseq.dao.jpa.WorkflowRunDAOImpl;
import edu.unc.mapseq.module.annotations.InputValidations;
import edu.unc.mapseq.module.annotations.OutputValidations;

public class ModuleTest {

    @Test
    public void testModule() {

        MaPSeqDAOBean daoBean = new MaPSeqDAOBean();
        daoBean.setWorkflowRunDAO(new WorkflowRunDAOImpl());
        daoBean.setJobDAO(new JobDAOImpl());
        daoBean.setFileDataDAO(new FileDataDAOImpl());
        daoBean.setSampleDAO(new SampleDAOImpl());

        SampleModule app = new SampleModule();
        // app.setZip(new File("asdf.zip"));
        app.setZip(new File("asdf.zip"));
        app.setExtract(new File("/home/jdr0887/test"));
        app.setWorkflowName("TEST");
        app.setDryRun(Boolean.TRUE);
        // try {
        // new ModuleExecutor(module).call();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        ModuleOutput output = null;
        try {
            Set<ConstraintViolation<SampleModule>> constraintViolations = validator.validate(app,
                    InputValidations.class);
            if (constraintViolations.size() > 0) {
                for (ConstraintViolation<SampleModule> value : constraintViolations) {
                    String errorMessage = MessageFormat.format("The value of {0}.{1} was: {2}.  {3}",
                            value.getRootBeanClass(), value.getPropertyPath().toString(), value.getInvalidValue(),
                            value.getMessage());
                    System.err.println(errorMessage);
                }
            }
            ModuleExecutor executor = new ModuleExecutor();
            executor.setDaoBean(daoBean);
            executor.setModule(app);
            executor.addObserver(new DryRunJobObserver());
            output = Executors.newSingleThreadExecutor().submit(executor).get();
            constraintViolations = validator.validate(app, OutputValidations.class);
            if (constraintViolations.size() > 0) {
                for (ConstraintViolation<SampleModule> value : constraintViolations) {
                    String errorMessage = MessageFormat.format("The value of {0}.{1} was: {2}.  {3}",
                            value.getRootBeanClass(), value.getPropertyPath().toString(), value.getInvalidValue(),
                            value.getMessage());
                    System.err.println(errorMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(output.getExitCode());

    }
}
