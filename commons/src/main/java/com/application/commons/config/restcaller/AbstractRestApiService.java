package com.application.commons.config.restcaller;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRestApiService implements RestCaller {
    private String url;
    private HttpMethod method;
    private Map<String, Object> requestParams;
    private Map<String, String> headers;
    private Integer connectionTimeout;
    private Integer readTimeout;
//    private Map<String, Object> postBody;

    public AbstractRestApiService() {
    }

    public AbstractRestApiService(String url, HttpMethod method, Map<String, Object> requestParams, Map<String, String> headers, Integer connectionTimeout, Integer readTimeout) {
        this.url = url;
        this.method = method;
        this.requestParams = requestParams;
        this.headers = headers;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.headers = new HashMap();
    }

    public void validateParameters() throws IllegalArgumentException {
        if (this.url == null || this.method == null) {
            throw new IllegalArgumentException("URL and method cannot be null");
        }
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getRequestParams() {
        return this.requestParams;
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap();
        }

        this.headers = headers;
    }

    public Integer getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }


    public HttpMethod getMethod() {
        return this.method;
    }

    public boolean isPostBodyRequired() {
        switch (this.method) {
            case POST:
            case PUT:
            case PATCH:
                return true;
        }
        return false;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }
}
