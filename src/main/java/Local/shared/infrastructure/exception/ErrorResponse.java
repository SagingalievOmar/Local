package Local.shared.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private String message;
    private LocalDateTime timestamp;

}