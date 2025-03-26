package org.example.tripservice.service;

import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.async.RedisModulesAsyncCommands;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.example.tripservice.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {
    private final KafkaProducerService kafkaProducerService;

    private GenericObjectPool<StatefulRedisModulesConnection<String, String>> pool;


    public TripResponseDTO requestTrip(TripRequest request) {
        String tripId = UUID.randomUUID().toString();

        TripResponseDTO response =  TripResponseDTO.builder()
                .uuid(tripId)
                .errorCode(null)
                .errorKey(null)
                .plusOne(null)
                .riderHasCompletedTrips(true)  // Assuming true for now
                .build();

//            restTemplate.opsForValue().set("KEY_TRIP "+tripId, request);
        kafkaProducerService.publishTripEvent(request,tripId);

        return  response;
    }


    private void saveTripToRedis(String key,String value) throws Exception {
        try (StatefulRedisModulesConnection<String, String> connection = pool.borrowObject()) { // (3)
            RedisModulesAsyncCommands<String, String> commands = connection.async(); // (4)
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
}
