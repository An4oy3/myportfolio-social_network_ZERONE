package ru.skillbox.socialnetwork.data.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostStatisticResponse {
    private String error;
    private long timestamp;
    private long totalPostCount;
    private long foundPostCount;
    private Map<Timestamp, Long> GraphData;
    private Map<Integer, Double> postsByHour;
}
