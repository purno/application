package com.application.commons.config.restcaller;

import lombok.*;

import java.util.Map;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse {
    private int statusCode;
    private String body;
    private Map<String, String> headers;

}