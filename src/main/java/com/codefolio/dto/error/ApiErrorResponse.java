package com.codefolio.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * DTO class for error response from the API
 */
@Data
public class ApiErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private Timestamp timestamp;
    private int code;
    private HttpStatusCode status;
    private Object reason;
    private String path;

    public ApiErrorResponse(HttpStatusCode status, Object reason, String path) {
        this.timestamp = Timestamp.from(Instant.now());
        this.code = status.value();
        this.status = status;
        this.reason = reason;
        this.path = path;
    }
}
