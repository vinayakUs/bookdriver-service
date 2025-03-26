//package org.example.tripservice;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.data.redis.hash.HashMapper;
//import java.util.Map;
//
//public class CustomJacksonHashMapper<T> implements HashMapper<T, String, Object> {
//
//    private final ObjectMapper objectMapper;
//    private final Class<T> type;
//
//    public CustomJacksonHashMapper(Class<T> type) {
//        this.objectMapper = new ObjectMapper();
//        this.type = type;
//    }
//
//    @Override
//    public Map<String, Object> toHash(T object) {
//        try {
//            // Convert object to Map without @class
//            return objectMapper.convertValue(object, Map.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting to Hash", e);
//        }
//    }
//
//    @Override
//    public T fromHash(Map<String, Object> hash) {
//        try {
//            // Convert map back to object without @class
//            return objectMapper.convertValue(hash, type);
//        } catch (Exception e) {
//            throw new RuntimeException("Error converting from Hash", e);
//        }
//    }
//}
