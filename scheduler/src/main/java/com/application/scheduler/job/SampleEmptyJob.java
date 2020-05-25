package com.application.scheduler.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SampleEmptyJob implements Runnable {

    @Override
    public void run() {
        try {
        } catch (Exception e) {
            log.error(" error received while invoking the api " + e.getMessage());
        }
    }
}
