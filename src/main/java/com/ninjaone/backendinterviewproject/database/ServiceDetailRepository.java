package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.ServiceDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceDetailRepository extends CrudRepository<ServiceDetail, Long> {

    Optional<ServiceDetail> findByNameIgnoreCaseAndDeviceTypeId(String systemName, Long id);

    Iterable<ServiceDetail> findByAutomatic(boolean automatic);

}
