package com.rhb.demo.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	@Around("within(com.rhb.demo..*) && @within(org.springframework.web.bind.annotation.RestController)")
	public Object logControllerCall(ProceedingJoinPoint jp) throws Throwable {
		long start = System.currentTimeMillis();
		log.info("Controller request method={} args={}", jp.getSignature().toShortString(),
				Arrays.deepToString(jp.getArgs()));
		try {
			Object result = jp.proceed();
			log.info("Controller response method={} resultType={} durationMs={}", jp.getSignature().toShortString(),
					result == null ? "null" : result.getClass().getSimpleName(), System.currentTimeMillis() - start);
			return result;
		} catch (Throwable ex) {
			log.error("Controller error method={} durationMs={} error={}", jp.getSignature().toShortString(),
					System.currentTimeMillis() - start, ex.toString());
			throw ex;
		}
	}
}
