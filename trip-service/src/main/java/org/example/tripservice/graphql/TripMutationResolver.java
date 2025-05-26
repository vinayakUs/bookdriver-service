package org.example.tripservice.graphql;



import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.checkerframework.checker.units.qual.s;
import org.example.tripservice.dto.TripRequest;
import org.example.tripservice.dto.TripResponseDTO;
import org.example.tripservice.service.TripService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TripMutationResolver {
    private final TripService tripService;

    private final HttpServletRequest servletRequest;


    @MutationMapping
    public TripResponseDTO tripRequest(@Argument TripRequest request  ) {
 
        System.out.println(servletRequest.getHeader("X-USER-ID"));


        return tripService.requestTrip(request, servletRequest.getHeader("X-USER-ID"));
    }


}
