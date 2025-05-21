package com.spectrum.Training.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.Training.model.DocumentTraining;

public interface DocumentTrainingRepo extends  JpaRepository <DocumentTraining,Long> {
    List<DocumentTraining> findByTrainingId(Long trainingID);
}
