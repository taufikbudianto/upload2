package prosia.app.security.jwt;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import prosia.app.web.rest.util.BodyUtil;
import prosia.app.web.rest.util.HeaderUtil;
import prosia.app.web.rest.util.ResponseCode;

/**
 * Filters incoming requests and installs a Spring Security principal 
 * if a header corresponding to a valid user is found.
 */
public class JWTFilter extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(JWTFilter.class);

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        try {
            String jwt = resolveToken(httpServletRequest);

            if (StringUtils.hasText(jwt)) {
                if (this.tokenProvider.validateToken(jwt)) {
                    Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new SignatureException(jwt);
                }
            } else if (httpServletRequest.getServletPath() != null
                    && httpServletRequest.getServletPath().equals("/sarcore")) {

                throw new SignatureException(jwt);

            }else if (httpServletRequest.getServletPath() != null
                    && httpServletRequest.getServletPath().equals("/document")) {

                if (!httpServletRequest.getPathInfo().substring(0,6).equals("/temp/")) {
                    throw new SignatureException(jwt);
                }

            }

            filterChain.doFilter(servletRequest, servletResponse);

        } catch (SignatureException se) {

            log.warn("Invalid JWT signature for url {} with {}",
                    httpServletRequest.getServletPath().concat(
                            httpServletRequest.getPathInfo() != null ? 
                            httpServletRequest.getPathInfo() : ""), 
                    se.getMessage());

            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ((HttpServletResponse) servletResponse).setContentType("application/json;charset=UTF-8");
            ((HttpServletResponse) servletResponse).addHeader(
                    HeaderUtil.X_ALERT, "Failed verify user's credential.");
            ((HttpServletResponse) servletResponse).addHeader(HeaderUtil.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            ((HttpServletResponse) servletResponse).addHeader(HeaderUtil.ACCESS_CONTROL_ALLOW_METHOD,
                    "POST, GET, OPTIONS, DELETE");
            ((HttpServletResponse) servletResponse).addHeader(HeaderUtil.ACCESS_CONTROL_ALLOW_HEADER,
                    "Content-Type, Accept, X-Requested-With, remember-me, Authorization");
            ((HttpServletResponse) servletResponse).addHeader(HeaderUtil.ACCESS_CONTROL_MAX_AGE, "1800");

            BodyUtil.create(ResponseCode.INVALID_TOKEN, BodyUtil.X_MESSAGE_INVALID_TOKEN)
                    .write(((HttpServletResponse) servletResponse).getWriter());

        } catch (ExpiredJwtException eje) {

            log.warn("Security exception for user {} - {}", eje.getClaims().getSubject(), eje.getMessage());

            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ((HttpServletResponse) servletResponse).setContentType("application/json;charset=UTF-8");
            ((HttpServletResponse) servletResponse).addHeader(
                    HeaderUtil.X_ALERT, "Failed verify user's credential.");
            ((HttpServletResponse) servletResponse).addHeader(HeaderUtil.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            ((HttpServletResponse) servletResponse).addHeader(HeaderUtil.ACCESS_CONTROL_ALLOW_METHOD,
                    "POST, GET, OPTIONS, DELETE");
            ((HttpServletResponse) servletResponse).addHeader(HeaderUtil.ACCESS_CONTROL_ALLOW_HEADER,
                    "Content-Type, Accept, X-Requested-With, remember-me, Authorization");
            ((HttpServletResponse) servletResponse).addHeader(HeaderUtil.ACCESS_CONTROL_MAX_AGE, "1800");

            BodyUtil.create(ResponseCode.INVALID_TOKEN, BodyUtil.X_MESSAGE_INVALID_TOKEN)
                    .write(((HttpServletResponse) servletResponse).getWriter());
        }
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }

}
