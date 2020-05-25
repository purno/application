package com.application.commons.config.restcaller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.io.IOException;

public interface RestCaller {

    <T> ResponseEntity<String> restCall(@Nullable T postBody) throws IOException;
}