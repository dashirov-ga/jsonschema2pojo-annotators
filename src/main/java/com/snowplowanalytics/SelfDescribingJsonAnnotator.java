package com.snowplowanalytics;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.Jackson2Annotator;

/**
 * Created by dashirov on 4/3/17.
 */
public class SelfDescribingJsonAnnotator extends Jackson2Annotator {

    public SelfDescribingJsonAnnotator(GenerationConfig generationConfig) {
        super(generationConfig);
    }

    @Override
    public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {
        super.propertyInclusion(clazz, schema);
        System.out.println("Looking..." + clazz.fullName());
        if (schema.isObject() && schema.has("$schema") && schema.get("$schema").asText().equals("http://iglucentral.com/schemas/com.snowplowanalytics.self-desc/schema/jsonschema/1-0-0#")) {
            if (schema.get("self") != null &&
                    schema.get("self").has("vendor") &&
                    schema.get("self").has("name") &&
                    schema.get("self").has("version") &&
                    schema.get("self").has("format")) {

                clazz.annotate(IgluInfo.class)
                        .param("vendor", schema.get("self").get("vendor").textValue())
                        .param("name", schema.get("self").get("name").textValue())
                        .param("version", schema.get("self").get("version").textValue())
                        .param("format", schema.get("self").get("format").textValue());
            }

        }

    }

}
