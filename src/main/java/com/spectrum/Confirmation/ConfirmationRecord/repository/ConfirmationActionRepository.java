package com.spectrum.Confirmation.ConfirmationRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.Confirmation.ConfirmationRecord.model.ConfirmationAction;

public interface ConfirmationActionRepository extends JpaRepository<ConfirmationAction, Long> {

}
