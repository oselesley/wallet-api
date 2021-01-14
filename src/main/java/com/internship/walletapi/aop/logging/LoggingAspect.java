package com.internship.walletapi.aop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


import java.util.Arrays;

@Aspect
public class LoggingAspect {
    @Autowired
    private Environment env;

    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            "|| within(@org.springframework.stereotype.Service *)" +
            "|| within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    @Pointcut("within(@com.internship.walletapi.repositories *)" +
            "|| within(@com.internship.walletapi.controllers *)" +
            "|| within(@com.internship.walletapi.services *)")
    public void applicationPointCut () {

    }

    @Before(value = "springBeanPointcut() || applicationPointCut()")
    public void loggerMethods(JoinPoint joinPoint) {
        Logger logger = logger(joinPoint);
       logger.info("Entering method: with arguments {} {}: " + Arrays.toString(joinPoint.getArgs()));
    }

    @Around(value = "springBeanPointcut() || applicationPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw e;
        }
    }

    @AfterThrowing(value = "applicationPointCut() || springBeanPointcut()", throwing = "e")
    public void logExceptions(JoinPoint joinPoint, Throwable e) {

        if (env.getActiveProfiles().length > 0) {
            logger(joinPoint)
                    .error(
                            "Exception in {}() with cause = \'{}\' and exception = \'{}\'",
                            joinPoint.getSignature().getName(),
                            e.getCause() != null ? e.getCause() : "NULL",
                            e.getMessage(),
                            e
                    );
        } else {
            logger(joinPoint)
                    .error(
                            "Exception in {}() with cause = {}",
                            joinPoint.getSignature().getName(),
                            e.getCause() != null ? e.getCause() : "NULL"
                    );
        }
    }
}
