package com.money_track.demo.exceptions;

import java.time.Instant;
import java.time.LocalDateTime;

public class ErrorResponse {
    private Integer status;
    private String error;
    private String message;
    private String path;
    private Instant timeStamp;

    public ErrorResponse(Integer status, String error, String message, String path, Instant timeStamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timeStamp = timeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }
}
