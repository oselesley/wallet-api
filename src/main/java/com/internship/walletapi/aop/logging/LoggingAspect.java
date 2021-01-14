package com.internship.walletapi.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Aspect
public class LoggingAspect {
    private final Environment environment;

    private Logger logger = LogManager.getLogger(LoggingAspect.class);

    public LoggingAspect(Environment environment) {
        this.environment = environment;
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
    public void logger(JoinPoint joinPoint) {
       Object[] args = joinPoint.getArgs();
       StringBuilder argsBuilder = new StringBuilder();


        for (int index = 0; index < args.length; index++) {
            argsBuilder.append(args[index].toString());
        }

       logger.info("Entering method: with arguments {} {}: " + argsBuilder.toString());
    }

    @Around(value = "springBeanPointcut() || applicationPointCut()")
    public void logger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("Proceeding to enter method: ");
        proceedingJoinPoint.proceed();
        logger.info("Finished execution, leaving method: ");
    }

    @AfterThrowing(value = "applicationPointCut() || springBeanPointcut()", throwing = "e")
    public void logExceptions(Exception e) {
        logger.info(e.getMessage());
        e.printStackTrace();
    }
}
