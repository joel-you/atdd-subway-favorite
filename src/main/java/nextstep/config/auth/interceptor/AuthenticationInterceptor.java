package nextstep.config.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.common.exception.InvalidTokenException;
import nextstep.config.auth.context.Authentication;
import nextstep.config.auth.context.AuthenticationContextHolder;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    public static final String AUTHENTICATION_TYPE = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;

    public AuthenticationInterceptor(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Objects.nonNull(AuthenticationContextHolder.getContext().getAccessToken())) {
            return true;
        }

        Authentication authentication = extractAuthenticationContext(request);
        if (Objects.nonNull(authentication)) {
            AuthenticationContextHolder.setContext(authentication);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthenticationContextHolder.clearContext();
    }

    private void validateAuthorization(String accessToken) {
        if (Objects.isNull(accessToken)) {
            throw new InvalidTokenException("UnAuthorized accessToken type.");
        }

        if (!accessToken.startsWith(AUTHENTICATION_TYPE)) {
            throw new InvalidTokenException("UnAuthorized accessToken type.");
        }
    }

    private Authentication extractAuthenticationContext(HttpServletRequest request) {
        try {
            String value = request.getHeader(HttpHeaders.AUTHORIZATION);

            validateAuthorization(value);
            String accessToken = value.replace(String.format("%s ", AUTHENTICATION_TYPE), "");
            if (!jwtTokenProvider.validateToken(accessToken)) {
                throw new InvalidTokenException(String.format("%s is UnAuthorized token", value));
            }

            MemberResponse findMember = memberService.findMemberByEmail(jwtTokenProvider.getPrincipal(accessToken));

            return Authentication.of(accessToken, findMember);

        } catch (Exception e) {
            throw new InvalidTokenException(e);
        }
    }
}
