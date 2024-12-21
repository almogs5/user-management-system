package com.tasks.user_management_system.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class StatisticsResponse {

    private long totalUsers;
    private long totalRequests;
    private double averageRequestTime;
}
