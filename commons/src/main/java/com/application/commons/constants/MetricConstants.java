package com.application.commons.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetricConstants {

    public static final String SERVICE_ID = "accounting.service.id";
    public static final String SERVICE_ENV = "accounting.service.env";
    public static final String SERVICE_NODE = "accounting.service.node";

    public static String[] createExternalApiTags(String url, int statusCode, String externalDomain, String httpMethod) {
        return new String[]{
                TagName.EXTERNAL_SERVICE_URL + ":" + url,
                TagName.SERVICE_METHOD + ":" + httpMethod,
                TagName.RESPONSE_STATUS_CODE + ":" + statusCode,
                TagName.DOMAIN + ":" + externalDomain};
    }

    public static String[] createInternalApiTags(String url, int statusCode, String externalDomain, String httpMethod) {
        return new String[]{
                TagName.INTERNAL_SERVICE_URL + ":" + url,
                TagName.SERVICE_METHOD + ":" + httpMethod,
                TagName.RESPONSE_STATUS_CODE + ":" + statusCode,
                TagName.DOMAIN + ":" + externalDomain};
    }

    public static String[] createKafkaProducerFailureTags(String exceptionType, String failureType) {
        return new String[]{
                TagName.EXCEPTION_TYPE + ":" + exceptionType, TagName.KAFKA_FAILURE + ":" + failureType
        };
    }

    public static String[] createKafkaConsumerFailureTags(String exceptionType, String failureType) {
        return new String[]{
                TagName.EXCEPTION_TYPE + ":" + exceptionType, TagName.KAFKA_FAILURE + ":" + failureType
        };
    }

    public static String[] createExternalApiExceptionTags(String url, int statusCode, String externalDomain, String httpMethod, String exceptionType) {
        List<String> tags = new ArrayList<>(Arrays.asList(createExternalApiTags(url, statusCode, externalDomain, httpMethod)));
        tags.add(TagName.EXCEPTION_TYPE + ":" + exceptionType);
        return tags.toArray(new String[0]);
    }

    public static String[] createTaskOrderTags(String name, String orderId) {
        return new String[]{
                TagName.TASK_NAME + ":" + name,
                TagName.ORDER + ":" + orderId
        };
    }

    public static String[] createTaskTags(String name, String status, String orderId) {
        List<String> tags = new ArrayList<>(Arrays.asList(createTaskOrderTags(name, orderId)));
        tags.add(TagName.TASK_STATUS + ":" + status);
        return tags.toArray(new String[0]);
    }

    public static String[] createOrderTags(String orderId, String status) {
        return new String[]{
                TagName.ORDER + ":" + orderId,
                TagName.STATUS + ":" + status
        };
    }

    public static final class MetricsName {
        public static final String EXTERNAL_API = "external_services";
        public static final String KAFKA = "kafka_producer_consumer";
        public static final String INTERNAL_API_LATENCY = "internal_apis_latency";
        public static final String EXTERNAL_API_LATENCY = "external_services_latency";
        public static final String EXTERNAL_API_LATENCY_HISTOGRAM = "external_services_latency_hist";
        public static final String INTERNAL_API_LATENCY_HISTOGRAM = "internal_services_latency_hist";
        public static final String INTERNAL_API = "acc_apis";
        public static final String LATENCY = "acs_latency";
    }

    public static final class TagName {
        public static final String EXTERNAL_SERVICE_URL = "external-service-url";
        public static final String SERVICE_METHOD = "service-method";
        public static final String RESPONSE_STATUS_CODE = "response-status-code";
        public static final String BUSINESS_EXCEPTION_CODE = "business-exception-code";
        public static final String INTERNAL_RESPONSE_STATUS_CODE = "internal-response-status-code";
        public static final String RESPONSE_TIME = "response-time";
        public static final String INTERNAL_SERVICE_URL = "internal-service-url";
        public static final String EXCEPTION_TYPE = "exception-type";
        public static final String DOMAIN = "domain";
        public static final String TASK_NAME = "task-name";
        public static final String TASK_STATUS = "task-status";
        public static final String KAFKA_FAILURE = "kafka_failure";
        // not sure but will add later
        // start
        public static final String EXPECTED_TASK_STATUS = "expected-task-status";
        public static final String ALLOWED_TASK_STATUS = "allowed-task-status";
        // end
        public static final String CHANNEL = "channel";
        public static final String CONNECTION_POOL_AVAILABLE = "connection_pool_available";
        public static final String CONNECTION_POOL_MAX = "connection_pool_max";
        public static final String CONNECTION_POOL_LEASED = "connection_pool_leased";
        public static final String CONNECTION_POOL_PENDING = "connection_pool_pending";
        public static final String CONNECTION_POOL_NAME = "connection_pool_name";
        public static final String ORDER = "order";
        public static final String STATUS = "status";

    }

}
