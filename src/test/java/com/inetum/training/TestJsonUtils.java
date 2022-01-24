package com.inetum.training;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TestJsonUtils {

    static ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    public static String convertObjectToJson(Object object) throws IOException {

        return mapper.writeValueAsString(object);
    }

}
