package edu.unc.mapseq.config;

public interface MaPSeqConfigurationService {

    public static final String VERSION = "version";

    public static final String WEB_SERVICE_HOST = "MAPSEQ_WEB_SERVICE_HOST";

    public static final String WEB_SERVICE_PORT = "MAPSEQ_WEB_SERVICE_PORT";

    public static final String WEB_SERVICE_TIMEOUT = "MAPSEQ_WEB_SERVICE_TIMEOUT";

    public String getVersion(String def);

    public String getWebServiceHost(String def);

    public void setWebServiceHost(String value);

    public Integer getWebServicePort(Integer def);

    public void setWebServicePort(Integer value);

    public Long getWebServiceTimeout();

}
