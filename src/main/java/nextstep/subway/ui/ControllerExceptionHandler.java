package nextstep.subway.ui;

import io.jsonwebtoken.MalformedJwtException;
import nextstep.member.domain.exception.InvalidUserInfoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidUserInfoException.class)
    public ResponseEntity<Void> handleInvalidUserInfoException(InvalidUserInfoException e) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("errorMessage", e.getMessage()));
    }
}
