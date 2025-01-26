package com.example.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.entity.vo.TimeSlot;

public class JsonTimeUtil {
    public static List<TimeSlot> convertToTimeSlots(List<?> objects) {
        if(objects == null) return null;
        
        return objects.stream()
                .map(obj -> {
                    if(obj instanceof TimeSlot) {
                        return (TimeSlot) obj;
                    }
                    if(obj instanceof Map) {
                        Map<?,?> map = (Map<?,?>)obj;
                        TimeSlot timeSlot = new TimeSlot();
                        timeSlot.setWeekday(((Number)map.get("weekday")).intValue());
                        timeSlot.setTime((String)map.get("time"));
                        return timeSlot;
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }
    
}
