package edu.unc.mapseq.config;

import java.io.IOException;
import java.util.Properties;

public class MaPSeqConfigurationServiceImpl implements MaPSeqConfigurationService {

    private Properties properties;

    public MaPSeqConfigurationServiceImpl() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("mapseq.properties"));
            // overload with System props
            this.properties.putAll(System.getProperties());
            this.properties.putAll(System.getenv());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String getVersion(String def) {
        return getProperties().getProperty(MaPSeqConfigurationService.VERSION, def);
    }

    @Override
    public String getWebServiceHost(String def) {
        return getProperties().getProperty(MaPSeqConfigurationService.WEB_SERVICE_HOST, def);
    }

    @Override
    public void setWebServiceHost(String value) {
        getProperties().setProperty(MaPSeqConfigurationService.WEB_SERVICE_HOST, value);
    }

    @Override
    public Integer getWebServicePort(Integer def) {
        String value = getProperties().getProperty(MaPSeqConfigurationService.WEB_SERVICE_PORT, def.toString());
        return Integer.valueOf(value);
    }

    @Override
    public void setWebServicePort(Integer value) {
        getProperties().setProperty(MaPSeqConfigurationService.WEB_SERVICE_PORT, value.toString());
    }

    @Override
    public Long getWebServiceTimeout() {
        Long duration = 5 * 60 * 1000L;
        String timeout = getProperties().getProperty(MaPSeqConfigurationService.WEB_SERVICE_TIMEOUT,
                duration.toString());
        return Long.valueOf(timeout);
    }

    public void setWebServiceTimeout(Long value) {
        getProperties().setProperty(MaPSeqConfigurationService.WEB_SERVICE_TIMEOUT, value.toString());
    }

}