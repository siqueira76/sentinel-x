package br.com.sentinelx.core.shared.error;

import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String code,
        String message,
        String path,
        List<ApiFieldError> details
) {
    public static ApiErrorResponse of(
            HttpStatus status,
            ErrorCode code,
            String message,
            String path,
            List<ApiFieldError> details
    ) {
        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                code.name(),
                message,
                path,
                details == null ? List.of() : List.copyOf(details)
        );
    }
}