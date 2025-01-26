package com.example.entity.vo;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class TimeSlotDeserializer extends JsonDeserializer<TimeSlot> {
    
    @Override
    public TimeSlot deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setWeekday(node.get("weekday").asInt());
        timeSlot.setTime(node.get("time").asText());
        
        return timeSlot;
    }
}