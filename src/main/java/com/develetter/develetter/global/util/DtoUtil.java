package com.develetter.develetter.global.util;

import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;

public class DtoUtil {

    public static Map<String, Object> toMap(Object record) {
        Map<String, Object> result = new HashMap<>();

        // record 만 사용 가능
        if (record instanceof Record) {
            RecordComponent[] components = record.getClass().getRecordComponents();
            for (RecordComponent component : components) {
                try {
                    result.put(component.getName(), component.getAccessor().invoke(record));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
}
