package com.application.listener.config;

import com.application.commons.config.restcaller.DefaultRestCaller;
import com.application.commons.config.restcaller.RouteConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author ankitsingodia
 */
@Component
@Slf4j
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RouteConfigs routeConfigs;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        try {
            initialiseRouteConfigs();
            log.info("****** LISTENER STARTED *****");
        } catch (Exception e) {
            log.error(String.format("exception occurred in startup listener.. %s", e.getMessage()), e);
        }

    }

    private void initialiseRouteConfigs() {
        DefaultRestCaller.init(routeConfigs.getConfigs());
    }

}
