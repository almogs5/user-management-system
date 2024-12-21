package com.tasks.user_management_system.service;

import com.tasks.user_management_system.dto.StatisticsResponse;
import com.tasks.user_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserRepository userRepository;
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final DoubleAdder totalRequestTime = new DoubleAdder();


//
//    public Timer.Sample startTimer() {
//        return Timer.start();
//    }
//
//    public void stopTimer(Timer.Sample sample) {
//        sample.stop(requestLatencyTimer);
//    }

    public void recordRequest() {
        totalRequests.incrementAndGet();
    }

    public void recordRequestTime(long nanoSeconds) {
        totalRequestTime.add(nanoSeconds);
    }

    public StatisticsResponse getStatistics() {
        long requests = totalRequests.get();
        double avgTime = requests > 0 ? totalRequestTime.sum() / requests : 0;

        return StatisticsResponse.builder()
                .totalUsers(userRepository.count())
                .totalRequests(requests)
                .averageRequestTime(avgTime)
                .build();
    }
}
