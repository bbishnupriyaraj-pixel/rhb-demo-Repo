package com.rhb.demo.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class HttpLoggingFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		long start = System.currentTimeMillis();
		log.info("HTTP request method={} uri={} query={}", req.getMethod(), req.getRequestURI(), req.getQueryString());
		try {
			chain.doFilter(req, res);
		} finally {
			log.info("HTTP response method={} uri={} status={} durationMs={}", req.getMethod(), req.getRequestURI(),
					res.getStatus(), System.currentTimeMillis() - start);
		}
	}
}
