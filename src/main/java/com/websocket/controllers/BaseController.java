package com.websocket.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.data.ErrorData;

import java.io.IOException;

public class BaseController {
    protected String toErrorJson(Exception e) {
        String res = "";

        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorData dt = getErrorData(e);

            res = mapper.writeValueAsString(dt);
        } catch (Exception e1) {
            // no implementation
        }

        return res;
    }

    protected String toJson(Object dt) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(dt);
    }

    protected Object fromJson(Class cls, String request) throws IOException {
        Object res = null;

        if (request != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            res = mapper.readValue(request, cls);
        }

        return res;
    }

    protected Object fromJsonForcedProperties(Class cls, String request) throws IOException {
        Object res = null;

        if (request != null) {
            ObjectMapper mapper = new ObjectMapper();

            res = mapper.readValue(request, cls);
        }

        return res;
    }

    private ErrorData getErrorData(Exception e) {
        ErrorData dt = new ErrorData();

        dt.setMessage(e.getMessage());

        return dt;
    }
}
