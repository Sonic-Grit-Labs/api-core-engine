package io.theawesomemogul.core.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter that generates and propagates correlation IDs across HTTP requests.
 *
 * This filter ensures that all requests have a unique correlation ID that can be
 * used to trace and link all logs and operations related to a single request across
 * multiple services and threads.
 *
 * Functionality:
 * - Checks for existing X-Correlation-Id header in request
 * - Generates a new UUID if no correlation ID exists
 * - Adds correlation ID to MDC for automatic inclusion in all logs
 * - Adds correlation ID to response headers
 * - Clears MDC on request completion
 *
 * Usage in logs:
 * All log messages automatically include the correlation ID from MDC during this request.
 *
 * @author Mogul Audio Core Team
 * @since 1.0.0
 */
@Slf4j
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";

    /**
     * Processes the request to generate/propagate correlation ID.
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
        try {
            // Get correlation ID from request header or generate new one
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.isEmpty()) {
                correlationId = UUID.randomUUID().toString();
            }

            // Add to MDC for automatic inclusion in logs
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

            // Add to response headers
            response.addHeader(CORRELATION_ID_HEADER, correlationId);

            // Continue filter chain
            filterChain.doFilter(request, response);

        } finally {
            // Always clear MDC when request completes
            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }

    /**
     * This filter should be applied to all requests.
     *
     * @return false (apply to all requests)
     * @since 1.0.0
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false;
    }
}
