package pe.edu.utp.adaptive.persistence;

import java.util.List;
import java.util.Optional;

public interface ProcessingRecordDao {

    ProcessingRecord create(ProcessingRecord record);

    Optional<ProcessingRecord> findById(long id);

    List<ProcessingRecord> findAll();

    boolean update(ProcessingRecord record);

    boolean delete(long id);
}