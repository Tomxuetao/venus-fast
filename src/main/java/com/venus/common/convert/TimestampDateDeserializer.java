package com.venus.common.convert;

import com.google.gson.*;
import com.venus.common.exception.VenusException;

import java.lang.reflect.Type;
import java.util.Date;


public class TimestampDateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String timestamp = json.getAsString();
            return new Date(Long.parseLong(timestamp));
        } catch (NumberFormatException e) {
            throw new VenusException("Invalid timestamp value: " + json.getAsString());
        }
    }
}