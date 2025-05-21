package com.spectrum.login.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.login.model.OtpEntry;

public interface OtpRepository extends JpaRepository<OtpEntry, Long> {
    Optional<OtpEntry> findByEmail(String email);
    void deleteByEmail(String email);

}