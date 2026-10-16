package com.dat.cms.repository;

import com.dat.cms.model.EmailRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRecordRepository extends JpaRepository<EmailRecord, Long> {
}
