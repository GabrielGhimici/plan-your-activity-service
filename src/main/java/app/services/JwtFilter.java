package app.services;


import app.response.Login;
import app.services.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@WebFilter(urlPatterns = { "/service/*" })
public class JwtFilter implements Filter {

    @Autowired
    private JwtService jwtTokenService;

    @Value("${jwt.auth.header}")
    String authHeader;

    private static ArrayList<String> tokens = new ArrayList<>();

    @Override public void init(FilterConfig filterConfig) throws ServletException  {}
    @Override public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String authHeaderVal = httpRequest.getHeader(authHeader);

        if (null==authHeaderVal)
        {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try
        {
            Random rn = new Random();
            if(((rn.nextInt(999) + 1) % 5) == 0) {
                cleanTokens();
            }

            if(!tokens.contains(authHeaderVal)) {
                Login jwtUser = jwtTokenService.getUser(authHeaderVal);
                httpRequest.setAttribute("jwtUser", jwtUser);
            }
            else
            {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        catch(JwtException e)
        {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    public static void logout(String token)
    {
        tokens.add(token);
    }

    private void cleanTokens()
    {
        for(int i = 0; i < tokens.size(); i++)
        {
            try
            {
                Login jwtUser = jwtTokenService.getUser(tokens.get(i));
            }
            catch(JwtException e)
            {
                tokens.remove(i);
            }
        }
    }

}