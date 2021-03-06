package edu.unc.mapseq.dao.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import edu.unc.mapseq.dao.JobDAO;
import edu.unc.mapseq.dao.MaPSeqDAOException;
import edu.unc.mapseq.dao.model.Job;

@Component
public class JobDAOImpl extends NamedEntityDAOImpl<Job, Long> implements JobDAO {

    private static final Logger logger = LoggerFactory.getLogger(JobDAOImpl.class);

    public JobDAOImpl() {
        super(Job.class);
    }

    @Override
    public List<Job> findByFileDataIdAndWorkflowId(Long fileDataId, String clazzName, Long workflowId) throws MaPSeqDAOException {
        return null;
    }

    @Override
    public Long save(Job entity) throws MaPSeqDAOException {
        logger.debug("ENTERING save(Job)");
        // tried to use WstxOutputFactory to replace control chars on server side....to no avail
        if (StringUtils.isNotEmpty(entity.getStderr())) {
            StringBuilder sb = new StringBuilder(entity.getStderr());
            int idx = sb.length();
            while (idx-- > 0) {
                if (sb.charAt(idx) < 0x20 && sb.charAt(idx) != 0x9 && sb.charAt(idx) != 0xA && sb.charAt(idx) != 0xD) {
                    sb.deleteCharAt(idx);
                }
            }
            entity.setStderr(sb.toString());
        }

        if (StringUtils.isNotEmpty(entity.getStdout())) {
            StringBuilder sb = new StringBuilder(entity.getStdout());
            int idx = sb.length();
            while (idx-- > 0) {
                if (sb.charAt(idx) < 0x20 && sb.charAt(idx) != 0x9 && sb.charAt(idx) != 0xA && sb.charAt(idx) != 0xD) {
                    sb.deleteCharAt(idx);
                }
            }
            entity.setStdout(sb.toString());
        }
        WebClient client = WebClient.create(getRestServiceURL(), getProviders(), true).type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        Response response = client.path("/").post(entity);
        Long id = response.readEntity(Long.class);
        return id;
    }

    @Override
    public List<Job> findByWorkflowRunAttemptIdAndName(Long jobId, String name) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowRunAttemptIdAndName(Long, Date, Date)");
        WebClient client = WebClient.create(getRestServiceURL(), getProviders());
        Collection<? extends Job> ret = client.path("findByWorkflowRunAttemptIdAndName/{name}/{name}", jobId, name)
                .accept(MediaType.APPLICATION_JSON).getCollection(Job.class);
        return new ArrayList<Job>(ret);
    }

    @Override
    public List<Job> findByWorkflowIdAndCreatedDateRange(Long workflowId, Date startDate, Date endDate) throws MaPSeqDAOException {
        logger.debug("ENTERING findByWorkflowIdAndCreatedDateRange(Long, Date, Date)");
        WebClient client = WebClient.create(getRestServiceURL(), getProviders());
        Collection<? extends Job> ret = client
                .path("findByWorkflowIdAndCreatedDateRange/{workflowId}/{startDate}/{endDate}", workflowId, startDate, endDate)
                .accept(MediaType.APPLICATION_JSON).getCollection(Job.class);
        return new ArrayList<Job>(ret);
    }

    @Override
    public List<Job> findByFileDataId(Long fileDataId, String clazzName) throws MaPSeqDAOException {
        return null;
    }

    @Override
    public List<Job> findByWorkflowRunAttemptId(Long id) throws MaPSeqDAOException {
        return null;
    }

    @Override
    public List<Job> findByName(String arg0) throws MaPSeqDAOException {
        return null;
    }

    @Override
    public Job findById(Long id) throws MaPSeqDAOException {
        logger.debug("ENTERING findById(Long)");
        WebClient client = WebClient.create(getRestServiceURL(), getProviders());
        Job job = client.path("findById/{id}", id).accept(MediaType.APPLICATION_JSON).get(Job.class);
        return job;
    }

    @Override
    public void addFileData(Long fileDataId, Long jobId) throws MaPSeqDAOException {
        logger.debug("ENTERING addFileData(Long, Long)");
        WebClient client = WebClient.create(getRestServiceURL(), getProviders(), true);
        client.path("/addFileData/{fileDataId}/{jobId}", fileDataId, jobId).post(null);
    }

}
