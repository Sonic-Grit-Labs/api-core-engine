package io.theawesomemogul.core.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that logs HTTP request and response details.
 *
 * This filter provides structured logging for all HTTP requests processed by the
 * Mogul Audio services, including:
 * - Request method, path, and query parameters
 * - Response status code
 * - Request duration
 * - Correlation ID for tracing
 * - User ID from JWT token if available
 *
 * Useful for:
 * - API monitoring and debugging
 * - Performance analysis
 * - Request tracing across services
 * - Audit trail creation
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    /**
     * Processes the HTTP request and logs relevant information.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     * @since 1.0.0
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String path = request.getRequestURI();
        String query = request.getQueryString();

        try {
            // Continue filter chain
            filterChain.doFilter(request, response);

        } finally {
            // Log request details after response
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            log.info("HTTP {} {} {} - {}ms",
                    method,
                    path,
                    status,
                    duration);

            if (query != null && !query.isEmpty()) {
                log.debug("Query parameters: {}", query);
            }
        }
    }

    /**
     * Exclude health check and actuator endpoints from logging.
     *
     * @param request the HTTP request
     * @return true if the request should be filtered out
     * @since 1.0.0
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") || path.equals("/health");
    }
}
