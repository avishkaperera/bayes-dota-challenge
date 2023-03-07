package gg.bayes.challenge.rest.model;

import lombok.Value;

@Value
public class ErrorResponse {
    String errorCode;
    String errorDescription;
}
