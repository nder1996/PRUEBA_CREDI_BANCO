package crediBanco.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

public class ApiKeyAuthFilter extends AbstractAuthenticationProcessingFilter {

    private static final String API_KEY_HEADER = "API-Key";
    private static final String API_SECRET_HEADER = "API-Secret";

    public ApiKeyAuthFilter(RequestMatcher requiresAuth) {
        super(requiresAuth);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        try {
            String apiKey = request.getHeader(API_KEY_HEADER);
            String apiSecret = request.getHeader(API_SECRET_HEADER);

            if (apiKey == null || apiSecret == null) {
                throw new BadCredentialsException("Missing API Key or Secret");
            }

            Authentication auth = new ApiKeyAuthenticationToken(apiKey, apiSecret);
            return getAuthenticationManager().authenticate(auth);
        } catch (BadCredentialsException e) {
            throw e; // Propagar la excepción para que Spring Security la maneje adecuadamente
        } catch (Exception e) {
            throw new RuntimeException("Authentication error: " + e.getMessage(), e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            super.successfulAuthentication(request, response, chain, authResult);
            if (!response.isCommitted()) {
                chain.doFilter(request, response);
            } else {
                // Log o manejo de error en caso de que la respuesta ya esté comprometida
                System.out.println("Response already committed, cannot continue chain");
            }
        } catch (IOException | ServletException e) {
            // Manejar la excepción aquí, por ejemplo, logueando el error
            // o enviando una respuesta de error de alguna otra manera.
            e.printStackTrace(); // Asegúrate de manejar adecuadamente la excepción
        }
    }

}
