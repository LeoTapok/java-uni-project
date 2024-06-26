package com.example.countryinfo.aop.aspect;

import com.example.countryinfo.service.CounterService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
  @Around(
      "@within(com.example.countryinfo.aop.annotation.Logging) ||"
          + " @annotation(com.example.countryinfo.aop.annotation.Logging)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.info("Called method {}() with args: {}", methodName, args);
    long start = System.currentTimeMillis();
    long executionTime;
    try {
      Object output = joinPoint.proceed();
      executionTime = System.currentTimeMillis() - start;
      log.info("Method {} returned value: {} in {} ms", methodName, output, executionTime);
      return output;
    } catch (Throwable exception) {
      executionTime = System.currentTimeMillis() - start;
      log.error(
          "Method {}() failed in {} ms with message: {}",
          methodName,
          executionTime,
          exception.getMessage());
      int requestCountInCatch = CounterService.get();
      log.info("Current count of requests: {}", requestCountInCatch);
      throw exception;
    }
  }
}
