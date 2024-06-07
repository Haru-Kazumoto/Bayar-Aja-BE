package project.bayaraja.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class HandlerLogout implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        Map<String, String> responseJson = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        responseJson.put("status", String.valueOf(response.getStatus()));
        responseJson.put("message", "Logout success");

        String jsonResponse = objectMapper.writeValueAsString(responseJson);

        response.getWriter().write(jsonResponse);
    }

}
