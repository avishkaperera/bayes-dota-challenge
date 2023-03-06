package gg.bayes.challenge.rest.model;

import lombok.Value;

@Value
public class ErrorResponse {
    private String errorCode;
    private String errorDescription;
}
