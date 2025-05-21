package com.spectrum.Training.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.Training.model.TrainingAcknowledge;

@Repository
public interface TrainingAcknowledegeRepo  extends JpaRepository<TrainingAcknowledge, Long>{

        List<TrainingAcknowledge> findAllByTrainingHRMSId(Long trainingId);}
