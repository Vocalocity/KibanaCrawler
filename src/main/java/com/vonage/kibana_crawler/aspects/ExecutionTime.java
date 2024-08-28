package com.vonage.kibana_crawler.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTime {

    @Around("@annotation(com.vonage.kibana_crawler.aspects.annotation.ExecutionTime)")
    public Object calculateExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("{} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
        return proceed;
    }
}
