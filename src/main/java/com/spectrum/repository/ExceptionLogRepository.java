package com.spectrum.repository;

import com.spectrum.model.ExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, Long> {
}
