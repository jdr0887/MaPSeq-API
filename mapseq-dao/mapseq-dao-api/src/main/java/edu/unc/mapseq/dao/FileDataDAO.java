package edu.unc.mapseq.dao;

import java.util.List;

import edu.unc.mapseq.dao.model.FileData;

public interface FileDataDAO extends BaseDAO<FileData, Long> {

    public List<FileData> findByExample(FileData fileData) throws MaPSeqDAOException;

    public List<FileData> findAll() throws MaPSeqDAOException;

}
