package com.mycompany.lastfmgetrecs.engine;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LastFMLogger {

    final static Logger logger = LoggerFactory.getLogger(LastFMLogger.class);

    @Before("call(* *.printStackTrace(..))")
    public void log1(JoinPoint joinPoint) {
        logger.info("Exception occurred: " + joinPoint.getSignature() + "\n"
                + "\t" + joinPoint.getThis());
    }

    @Before("call(public void com.mycompany.lastfmgetrecs.engine.LastFMRec.test(..))")
    public void log(JoinPoint joinPoint) {
        logger.info("Exec LastFMRec.test():" + joinPoint.getSignature());
    }

}
