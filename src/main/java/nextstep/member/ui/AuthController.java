package nextstep.member.ui;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok().body(authService.createToken(request));
    }

    @PostMapping("/login/github")
    public ResponseEntity<TokenResponse> createTokenWithGithub(@RequestBody GithubTokenRequest request) {
        return ResponseEntity.ok(authService.createTokenWithGithub(request));
    }
}
