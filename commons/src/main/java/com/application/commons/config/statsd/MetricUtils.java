package com.application.commons.config.statsd;

import com.application.commons.constants.MetricConstants;
import com.timgroup.statsd.StatsDClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
@Slf4j
public class MetricUtils {

    private static final String PRODUCER_FAILURE = "producer-failure";
    private static final String CONSUMER_FAILURE = "consumer-failure";
    @Autowired
    StatsDClient statsdClient;

    private void pushCounterMetrics(String metricName, String... tags) {
        statsdClient.incrementCounter(metricName, tags);
    }

    private void pushGaugeMetrics(String metricName, Long gaugeValue, String... gaugeTag) {
        statsdClient.recordGaugeValue(metricName, gaugeValue, gaugeTag);
    }

    private void pushHistogramValues(String metricName, double elapsedTime, String... histogramTags) {
        statsdClient.recordHistogramValue(metricName, elapsedTime, histogramTags);
    }

    private void pushLatency(String event, long elapsedTime){
        statsdClient.recordExecutionTime(MetricConstants.MetricsName.LATENCY, elapsedTime, "event", event);
    }


    public void pushExternalApiMetrics(long start, long end, String url, int statusCode, String integrationMetricConstant, String method) {
        String[] externalApiTags = MetricConstants.createExternalApiTags(url, statusCode, integrationMetricConstant, method);
        log.info(String.format("Metric [%s] Tags %s time[%s]", MetricConstants.MetricsName.EXTERNAL_API, Arrays.toString(externalApiTags), end - start));
        pushCounterMetrics(MetricConstants.MetricsName.EXTERNAL_API, externalApiTags);
        pushGaugeMetrics(MetricConstants.MetricsName.EXTERNAL_API_LATENCY, end - start, externalApiTags);
        double elapsedTime = (end - start) / 1000.0;
        pushHistogramValues(MetricConstants.MetricsName.EXTERNAL_API_LATENCY_HISTOGRAM, elapsedTime, externalApiTags);
    }

    public void pushExternalApiErrorMetrics(String exceptionType, String url, int statusCode, String integrationMetricConstant, String method) {
        String[] externalApiTags = MetricConstants.createExternalApiExceptionTags(url, statusCode, integrationMetricConstant, method, exceptionType);
        log.info(String.format("Exception Metric [%s] Tags [%s] ", MetricConstants.MetricsName.EXTERNAL_API, Arrays.toString(externalApiTags)));
        pushCounterMetrics(MetricConstants.MetricsName.EXTERNAL_API, externalApiTags);
    }

    public void pushInternalApiMetricsCounter(String url, int statusCode, String integrationMetricConstant, String method) {
        String[] internalApiTags = MetricConstants.createInternalApiTags(url, statusCode, integrationMetricConstant, method);
        log.info(String.format("Metric [%s] Tags %s ", MetricConstants.MetricsName.INTERNAL_API, Arrays.toString(internalApiTags)));
        pushCounterMetrics(MetricConstants.MetricsName.INTERNAL_API, internalApiTags);
    }

    public void pushInternalApiMetrics(long start, long end, String url, int statusCode, String integrationMetricConstant, String method) {
        String[] internalApiTags = MetricConstants.createInternalApiTags(url, statusCode, integrationMetricConstant, method);
        log.info(String.format("Metric [%s] Tags %s time[%s]", MetricConstants.MetricsName.INTERNAL_API, Arrays.toString(internalApiTags), end - start));
        pushCounterMetrics(MetricConstants.MetricsName.INTERNAL_API, internalApiTags);
        pushGaugeMetrics(MetricConstants.MetricsName.INTERNAL_API_LATENCY, end - start, internalApiTags);
        double elapsedTime = (end - start) / 1000.0;
        pushHistogramValues(MetricConstants.MetricsName.INTERNAL_API_LATENCY_HISTOGRAM, elapsedTime, internalApiTags);
    }


    public void pushKafkaProducerExceptionMetrics(String exceptionType) {
        String[] exceptionTags = MetricConstants.createKafkaProducerFailureTags(exceptionType, PRODUCER_FAILURE);
        pushCounterMetrics(MetricConstants.MetricsName.KAFKA, exceptionTags);
    }

    public void  pushEventLatency(String eventName, long elapsedTime){
        pushLatency(eventName, elapsedTime);
        pushCounterMetrics(eventName);
    }

    /**
     * The below metrics are used for pushing order to success bucket into Listener
     *
     * @param taskName   :
     * @param orderId    :
     * @param taskStatus :
     */
    public void pushKafkaMetrics(String taskName, String orderId, String taskStatus) {
        String[] taskTags = MetricConstants.createTaskTags(taskName, taskStatus, orderId);
        pushCounterMetrics(MetricConstants.MetricsName.KAFKA, taskTags);
    }


}
