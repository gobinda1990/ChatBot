package gov.nic.filter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ActuatorKeyFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ActuatorKeyFilter.class);

    @Value("${actuator.security.key}")
    private String expectedActuatorKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain)
            throws ServletException, IOException {

        String key = request.getHeader("X-ACTUATOR-KEY");

        if (!expectedActuatorKey.equals(key)) {
            logger.warn("Unauthorized Actuator access attempt to {} with key: {}", request.getRequestURI(), key);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized - Invalid Actuator Key");
            return;
        }

        filterChain.doFilter(request, response);
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        boolean skip = !uri.startsWith("/ChatBot/actuator/");
        logger.debug("ActuatorKeyFilter shouldNotFilter URI [{}] => {}", uri, skip);
        return skip;
    }
}
