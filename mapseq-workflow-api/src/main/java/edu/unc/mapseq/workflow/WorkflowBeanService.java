package edu.unc.mapseq.workflow;

import java.util.Map;

import edu.unc.mapseq.config.MaPSeqConfigurationService;
import edu.unc.mapseq.dao.MaPSeqDAOBeanService;

public interface WorkflowBeanService {

    public abstract int getCorePoolSize();

    public abstract void setCorePoolSize(int corePoolSize);

    public abstract int getMaxPoolSize();

    public abstract void setMaxPoolSize(int maxPoolSize);

    public abstract MaPSeqDAOBeanService getMaPSeqDAOBean();

    public abstract void setMaPSeqDAOBean(MaPSeqDAOBeanService maPSeqDAOBean);

    public abstract MaPSeqConfigurationService getMaPSeqConfigurationService();

    public abstract void setMaPSeqConfigurationService(MaPSeqConfigurationService maPSeqConfigurationService);

    public abstract Map<String, String> getAttributes();

    public abstract void setAttributes(Map<String, String> properties);

}