package com.application.commons.config.restcaller;


import com.application.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.application.commons.config.statsd.MetricUtils;
import com.application.commons.utils.BeanUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Useful Source: https://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html
 */
@Getter
@Setter
@Slf4j
public class DefaultRestCaller extends AbstractRestApiService implements RestCaller {

    /**
     * The HTTP specification does not specify how long a persistent connection may be and should be kept alive.
     * Some HTTP servers use a non-standard Keep-Alive header to communicate to the client the period of time in seconds they intend to keep the connection alive on the server side.
     * HttpClient makes use of this information if available. If the Keep-Alive header is not present in the response, HttpClient assumes the connection can be kept alive indefinitely.
     * However, many HTTP servers in general use are configured to drop persistent connections after a certain period of inactivity in order to conserve system resources,
     * quite often without informing the client. In case the default strategy turns out to be too optimistic, one may want to provide a custom keep-alive strategy
     */
    static ConnectionKeepAliveStrategy connectionKeepAliveStrategy = (response, context) -> {
        HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && param.equalsIgnoreCase
                    ("timeout")) {
                return Long.parseLong(value) * 1000;
            }
        }
        return (long) 5 * 1000;
    };
    private static MetricUtils metricUtils;
    private static final String DEFAULT_API_TIMEOUT = "DEFAULT_API_TIMEOUT";

    private static DefaultRestCallerConfig defRestConf = BeanUtil.getBean(DefaultRestCallerConfig.class);
    private static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    private static CloseableHttpClient closeableHttpClient = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setStaleConnectionCheckEnabled(true)
                    .setConnectTimeout(defRestConf.getDefaultConnectionTimeout())
                    .setConnectionRequestTimeout(defRestConf.getDefaultConnectionRequestTimeout())
                    .build())
            .setKeepAliveStrategy(connectionKeepAliveStrategy)
            .setConnectionManager(connManager)
            .build();
    private static Map<String, RouteConfigs.ApiTimeoutConfig> apiTimeoutMap = new HashMap<>();
    private static RouteConfigs.ApiTimeoutConfig defaultApiTimeoutConfig = new RouteConfigs.ApiTimeoutConfig();
    ;


    private String metricId;

    public DefaultRestCaller(String metricId) {
        this.metricId = metricId;
    }

    public static void init(List<RouteConfigs.RouteConfig> routeConfigList) {

        metricUtils = BeanUtil.getBean(MetricUtils.class);

        //set default timeouts
        initConnManagerTimeouts(defRestConf, connManager);
        initDefaultApiTimeouts(defRestConf, defaultApiTimeoutConfig);

        for (RouteConfigs.RouteConfig routeConfig : routeConfigList) {
            connManager.setMaxPerRoute(new HttpRoute(new HttpHost(routeConfig.getHost(), routeConfig.getPort(), routeConfig.getScheme())), routeConfig.getMaxConnections());
            // populate endpoint specific timeout
            initSpecificEndpointTimeouts(routeConfig);
            // populating host timeout
            if (Objects.nonNull(routeConfig.getHostTimeout())) {
                apiTimeoutMap.put(routeConfig.getHost(), routeConfig.getHostTimeout());
            }
        }
        // populating system timeout
        apiTimeoutMap.put(DEFAULT_API_TIMEOUT, defaultApiTimeoutConfig);
        log.info(String.format("api timeout map [%s]", apiTimeoutMap));
        log.debug("Default Rest Caller Config done ");

    }

    private static void initSpecificEndpointTimeouts(RouteConfigs.RouteConfig routeConfig) {
        List<RouteConfigs.ApiTimeoutConfig> apiTimeout = routeConfig.getApiTimeout();
        if (!CollectionUtils.isEmpty(apiTimeout)) {
            apiTimeout.forEach(apiTimeoutConfig -> apiTimeoutMap.put(apiTimeoutConfig.getEndPoint(), apiTimeoutConfig)
            );
        }
    }

    private static void initConnManagerTimeouts(DefaultRestCallerConfig defRestConf, PoolingHttpClientConnectionManager connManager) {
        connManager.setDefaultMaxPerRoute(defRestConf.getDefaultMaxConnPerRoute());
        connManager.setMaxTotal(defRestConf.getMaxConn());
        connManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(defRestConf.getDefaultSocketConfigTimeout()).build());
    }

    private static void initDefaultApiTimeouts(DefaultRestCallerConfig defRestConf, RouteConfigs.ApiTimeoutConfig defaultApiTimeoutConfig) {
        defaultApiTimeoutConfig.setSocketTimeout(defRestConf.getDefaultSocketConfigTimeout());
        defaultApiTimeoutConfig.setConnectionRequestTimeout(defRestConf.getDefaultConnectionRequestTimeout());
        defaultApiTimeoutConfig.setConnectTimeout(defRestConf.getDefaultConnectionTimeout());
    }


    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static void closeConn() {

        log.debug("Closing DefaultRestCaller connection");

        try {
            log.debug("Closing Client");
            closeableHttpClient.close();
            log.debug("Client Closed");

        } catch (IOException e) {
            log.error("Error while closing pooling connection client", e);
        }
        connManager.close();
        log.debug("DefaultRestCaller connection closed");

    }

    @Override
    public <T> ResponseEntity<String> restCall(@Nullable T postBody) {
        String body = null;
        if (this.isPostBodyRequired() && Objects.nonNull(postBody)) {
            try {
                body = JsonUtils.serializeWithIgnoreNull(postBody);
            } catch (JsonProcessingException ex) {
                pushExternalApiErrorMetrics(HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName());
                log.info(" Invalid body " + ex.getMessage());
                return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        this.validateParameters();
        CloseableHttpResponse response = null;
        log.debug(String.format("Invoking url %s with body [%s]", this.getUrl(), postBody));
        String responseBody;

        long start = System.currentTimeMillis();

        try {
            switch (this.getMethod()) {
                case GET:
                    HttpGet httpGet = new HttpGet(this.getUrl() + "?" + this.getQueryParamsString());
                    setRequestHeaders(httpGet);
                    setRequestConfig(httpGet);
                    response = closeableHttpClient.execute(httpGet);
                    break;
                case POST:
                    HttpPost httpPost = new HttpPost(this.getUrl() + "?" + this.getQueryParamsString());
                    setRequestHeaders(httpPost);
                    setRequestConfig(httpPost);
                    httpPost.setEntity(new StringEntity(body));
                    response = closeableHttpClient.execute(httpPost);
                    break;
                case PUT:
                    HttpPut httpPut = new HttpPut(this.getUrl() + "?" + this.getQueryParamsString());
                    setRequestHeaders(httpPut);
                    setRequestConfig(httpPut);
                    httpPut.setEntity(new StringEntity(body));
                    response = closeableHttpClient.execute(httpPut);
                    break;
                case PATCH:
                    HttpPatch httpPatch = new HttpPatch(this.getUrl() + "?" + this.getQueryParamsString());
                    setRequestHeaders(httpPatch);
                    setRequestConfig(httpPatch);
                    httpPatch.setEntity(new StringEntity(body));
                    response = closeableHttpClient.execute(httpPatch);
                    break;
                case DELETE:
                    HttpDelete httpDelete = new HttpDelete(this.getUrl() + "?" + this.getQueryParamsString());
                    setRequestHeaders(httpDelete);
                    setRequestConfig(httpDelete);
                    response = closeableHttpClient.execute(httpDelete);
                    break;
            }

            long now = System.currentTimeMillis();

            if (response == null) {
                log.info(" Response received is : null ");
                return null;
            }

            logRequest(response.getStatusLine().getStatusCode(), body);


            responseBody = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : null;

            logResponse(responseBody);

            pushMetrics(response.getStatusLine().getStatusCode(), start, now);

            try {

                return new ResponseEntity<>(StringUtils.isEmpty(responseBody) ? null
                        : (responseBody), HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
            } catch (Exception e) {
                log.error("Error in rest call ", e);
                pushExternalApiErrorMetrics(HttpStatus.NOT_ACCEPTABLE, e.getClass().getSimpleName());
                e.printStackTrace();
                return new ResponseEntity<>(responseBody, HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (SocketTimeoutException e) {
            log.warn(String.format("Read time out on restcall %s", this.getUrl()));
            pushExternalApiErrorMetrics(HttpStatus.REQUEST_TIMEOUT, e.getClass().getSimpleName());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.REQUEST_TIMEOUT);
        } catch (ConnectTimeoutException | HttpHostConnectException e) {
            log.warn(String.format("Connect time out on restcall %s", this.getUrl()));
            pushExternalApiErrorMetrics(HttpStatus.REQUEST_TIMEOUT, e.getClass().getSimpleName());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.REQUEST_TIMEOUT);
        } catch (IOException e) {
            log.warn(String.format("Connect time out on restcall %s", this.getUrl()));
            pushExternalApiErrorMetrics(HttpStatus.INTERNAL_SERVER_ERROR, e.getClass().getSimpleName());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.GATEWAY_TIMEOUT);
        } finally {
            try {
                if (response != null) {
                    log.debug("Going to close rest response");
                    response.close();
                }
            } catch (IOException ex) {
                log.info(" Unhandled io exception ");
            }
        }
    }


    private void logResponse(String responseBody) {
        log.debug(String.format(" response body: %s ", responseBody));
    }

    private void logRequest(int statusCode, String body) {
        log.info(String.format("headers: %s url: %s : response: %s", this.getHeaders(), this.getUrl() + (CollectionUtils.isEmpty(this.getRequestParams()) ? "" : "?") + this.getQueryParamsString(), statusCode));
        if (this.getMethod() == HttpMethod.POST || this.getMethod() == HttpMethod.PUT || this.getMethod() == HttpMethod.PATCH) {
            log.info(String.format(" postBody : %s ", body));
        }
    }


    private void pushMetrics(int statusCode, long start, long now) {
        try {
            metricUtils.pushExternalApiMetrics(start, now, this.getUrl(), statusCode, this.getMetricId(), this.getMethod().name());
        } catch (Exception e) {
            log.error("Error occurred while pushing external api metrics to statsD", e);
        }
    }

    private void pushExternalApiErrorMetrics(HttpStatus statusCode, String exceptionType) {
        try {
            metricUtils.pushExternalApiErrorMetrics(exceptionType, this.getUrl(), statusCode.value(), this.getMetricId(), this.getMethod().name());
        } catch (Exception e) {
            log.error("Error occurred while pushing external api error metrics to statsD", e);
        }
    }

    private void setRequestConfig(HttpRequestBase httpRequestBase) {
        RouteConfigs.ApiTimeoutConfig apiTimeoutConfig = getApplicableApiTimeoutConfig(httpRequestBase);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(apiTimeoutConfig.getConnectTimeout())
                .setSocketTimeout(apiTimeoutConfig.getSocketTimeout())
                .setConnectionRequestTimeout(apiTimeoutConfig.getConnectionRequestTimeout())
                .build();
        httpRequestBase.setConfig(config);
    }

    /*
     * URL>HOST>DEFAULT
     */
    private RouteConfigs.ApiTimeoutConfig getApplicableApiTimeoutConfig(HttpRequestBase httpRequestBase) {
        URI uri = httpRequestBase.getURI();
        String host = uri.getHost();
        log.debug(String.format(" current config [%s] ", httpRequestBase.getConfig()));
        log.debug(String.format("host [%s]", host));
        log.debug(String.format("Applicable timeoutMap [%s]", apiTimeoutMap));
        log.debug(String.format(" Url %s", this.getUrl()));
        RouteConfigs.ApiTimeoutConfig apiTimeoutConfig = apiTimeoutMap.get(this.getUrl());
        log.debug(String.format(" api timeout [key  , %s ]  [%s]", this.getUrl(), apiTimeoutConfig));
        if (Objects.isNull(apiTimeoutConfig)) {
            apiTimeoutConfig = apiTimeoutMap.get(host);
            log.debug(String.format(" host timeout [%s]", apiTimeoutConfig));
        }
        if (Objects.isNull(apiTimeoutConfig)) {
            apiTimeoutConfig = apiTimeoutMap.get(DEFAULT_API_TIMEOUT);
            log.debug(String.format(" system timeout [%s]", apiTimeoutConfig));
        }
        return apiTimeoutConfig;
    }


    private void setRequestHeaders(HttpRequestBase httpRequestBase) {
        if (this.getHeaders() != null && !this.getHeaders().isEmpty()) {
            this.getHeaders().forEach(httpRequestBase::setHeader);
        }
    }

    private String getQueryParamsString() {
        StringBuilder sb = new StringBuilder();
        if (this.getRequestParams() == null) {
            return Strings.EMPTY;
        }
        for (Map.Entry<?, ?> entry : this.getRequestParams().entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    entry.getValue() == null ? null : urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

}
