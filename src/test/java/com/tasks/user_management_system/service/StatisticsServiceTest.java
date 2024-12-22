package com.tasks.user_management_system.service;

import com.tasks.user_management_system.dto.StatisticsResponse;
import com.tasks.user_management_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsService(userRepository);
    }

    @Test
    void whenNoRequests_returnZeroAverages() {
        when(userRepository.count()).thenReturn(5L);

        StatisticsResponse expectedStatisticsResponse = StatisticsResponse.builder().totalUsers(5L).build();

        assertEquals(expectedStatisticsResponse, statisticsService.getStatistics());
    }

    @ParameterizedTest
    @MethodSource("requestTimesAndExpectedAveragesArguments")
    void recordRequestTimeShouldCalculateCorrectAverage(List<Long> requestTimes, long expectedTotalRequests, double expectedAverage) {
        requestTimes.forEach(time -> {
            statisticsService.recordRequest();
            statisticsService.recordRequestTime(time);
        });

        StatisticsResponse actualStatistics = statisticsService.getStatistics();

        assertEquals(expectedTotalRequests, actualStatistics.getTotalRequests());
        assertEquals(expectedAverage, actualStatistics.getAverageRequestTime(), 0.001);
        verify(userRepository).count();
    }

    @Test
    void recordRequest_ConcurrentRequests_ShouldHandleRaceConditions() throws InterruptedException {
        int numberOfThreads = 10;
        int requestsPerThread = 100;
        long requestTime = 1000000L;  // 1ms

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < requestsPerThread; j++) {
                        statisticsService.recordRequest();
                        statisticsService.recordRequestTime(requestTime);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        completionLatch.await();
        executor.shutdown();

        StatisticsResponse actualStatistics = statisticsService.getStatistics();
        assertEquals(numberOfThreads * requestsPerThread, actualStatistics.getTotalRequests());
        assertEquals(requestTime, actualStatistics.getAverageRequestTime(), 0.001);

        verify(userRepository).count();
    }

    private static List<Arguments> requestTimesAndExpectedAveragesArguments() {
        return List.of(
                Arguments.of(
                        List.of(1000000L, 2000000L, 3000000L),
                        3L,
                        2000000.0
                ),
                Arguments.of(
                        List.of(1000000L, 1000000L, 1000000L, 1000000L),
                        4L,
                        1000000.0
                ),
                Arguments.of(
                        List.of(1000000L, 2000000L, 3000000L, 4000000L, 5000000L),
                        5L,
                        3000000.0
                )
        );
    }
}