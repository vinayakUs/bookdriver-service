package org.example.tripservice.graphql;



import lombok.RequiredArgsConstructor;
import org.example.tripservice.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
import org.example.tripservice.service.TripService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TripMutationResolver {
    private final TripService tripService;


    @MutationMapping
    public TripResponseDTO tripRequest(@Argument TripRequest request) {
        return tripService.requestTrip(request);
    }


}
