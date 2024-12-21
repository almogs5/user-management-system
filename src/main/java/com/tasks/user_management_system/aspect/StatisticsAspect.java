package com.tasks.user_management_system.aspect;

import com.tasks.user_management_system.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class StatisticsAspect {

    private final StatisticsService statisticsService;

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public Object measureRequestTime(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!joinPoint.getSignature().toString().contains("Stats")) {
            statisticsService.recordRequest();
            long startTime = System.nanoTime();

            try {
                return joinPoint.proceed();
            } finally {
                statisticsService.recordRequestTime(System.nanoTime() - startTime);
            }
        }

        return joinPoint.proceed();
    }
}
