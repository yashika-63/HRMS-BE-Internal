package com.spectrum.PerformanceManagement.GoalSetting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spectrum.PerformanceManagement.GoalSetting.model.OrganizationGoalSettingDetail;

@Repository
public interface OrganizationGoalSettingDetailRepository extends JpaRepository<OrganizationGoalSettingDetail, Long> {

    
}
