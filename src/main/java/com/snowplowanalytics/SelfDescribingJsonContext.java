package com.snowplowanalytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davidashirov on 3/24/17.
 */

public  class SelfDescribingJsonContext {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public SelfDescribingJson getSelfDescribingJson() {
        Annotation annotation = this.getClass().getAnnotation(IgluInfo.class);
        IgluInfo igluInfo = (IgluInfo) annotation;
        String iglu = "iglu:" + igluInfo.vendor() + "/" + igluInfo.name() + "/" + igluInfo.format() + "/" + igluInfo.version();
        Map<String, Object> out = new HashMap<>();
        LOGGER.info("vedor: {}", igluInfo.vendor());
        LOGGER.info("name: {}", igluInfo.name());
        LOGGER.info("format: {}", igluInfo.format());
        LOGGER.info("version: {}", igluInfo.version());
        LOGGER.info("Iglu: {}", iglu);
        LOGGER.debug("Generating SDJ Map {}", this.getClass().getDeclaredFields().length);
        Field[] fields = this.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (Field f : fields) {
            LOGGER.debug("Examining field {}", f);
            JsonProperty property = f.getAnnotation(JsonProperty.class);
            if (property != null) {
                try {
                    String key = property.value();
                    String fieldName = f.getName();
                    Object fieldType = f.getType();
                    Object val = f.get(this);
                    if ( property.required() && val == null) {
                        LOGGER.error("Field required: key: {} type: {} name: {}", key, fieldType, fieldName);
                    }
                    out.put(key, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.debug("Completed SDJ Map");
        return new SelfDescribingJson(iglu, out);
    }
}
