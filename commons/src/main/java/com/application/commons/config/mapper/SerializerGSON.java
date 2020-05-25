package com.application.commons.config.mapper;

import com.application.commons.config.annotation.Exclude;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
public class SerializerGSON {

    public static class AnnotationExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(Exclude.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    @Bean
    @Primary
    public Gson gson(){
       return new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
    }
}
