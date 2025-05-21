package com.spectrum.PerformanceManagement.Feedback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.Feedback.model.FeedBackGoalSetting;

@Repository

public interface FeedBackGoalSettingRepository extends JpaRepository<FeedBackGoalSetting, Long> {


   

    @Query("SELECT f.employeeGoalSetting.id, f.ratingForGoal, f.note, f.date, f.employees.firstName, f.employees.lastName " +
           "FROM FeedBackGoalSetting f WHERE f.employeeGoalSetting.id IN :goalIds")
    List<Object[]> findFeedbackDetailsForGoal(@Param("goalIds") List<Long> goalIds);

    @Query("SELECT f.employeeGoalSetting.id, AVG(f.ratingForGoal) AS avgRating, COUNT(f.id) AS totalRatings " +
           "FROM FeedBackGoalSetting f WHERE f.employeeGoalSetting.id IN :goalIds GROUP BY f.employeeGoalSetting.id")
    List<Object[]> findAverageRatingForGoal(@Param("goalIds") List<Long> goalIds);
}


