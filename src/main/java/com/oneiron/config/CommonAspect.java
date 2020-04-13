package com.oneiron.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CommonAspect {
	
	protected static final Logger logger = LoggerFactory.getLogger(CommonAspect.class);
	
	/*@Around("execution(* com.oneiron.service.impl.*.*(..) )")
	public Object logging(ProceedingJoinPoint pjp) throws Throwable{
		Object result = pjp.proceed();
		
		logger.debug("ddd : {}", result);
		
		System.out.println("aop 작동");
		return result;
	}*/
}
