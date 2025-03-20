package org.example.locationservice.services;


import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import lombok.RequiredArgsConstructor;
import org.example.locationservice.exception.RouteServiceException;
import org.example.locationservice.model.dto.Location;
import org.example.locationservice.model.dto.RouteRequestDto;
import org.example.locationservice.model.dto.RouteResponseDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NavigationService {

    private final GeoApiContext context;


@Async
    public Optional<RouteResponseDto> getRoute(RouteRequestDto routeRequestDto)   {
        Location pick = routeRequestDto.getPickupLocation();
        Location dest = routeRequestDto.getDestinationLocation();

        try {
//
            DirectionsResult res = DirectionsApi.newRequest(context).origin(new LatLng(pick.getLat(), pick.getLng()))
                    .destination(new LatLng(dest.getLat(), dest.getLng())).mode(TravelMode.DRIVING).await();

            if(res.routes.length>0){
                RouteResponseDto responseDto = new RouteResponseDto();
            responseDto.setPolyline(res.routes[0].overviewPolyline.getEncodedPath());
            responseDto.setDistance(BigInteger.valueOf(res.routes[0].legs[0].distance.inMeters));
                return Optional.of(responseDto);
            }else {
                return  Optional.empty();
            }

        }
        catch ( com.google.maps.errors.ZeroResultsException ex){
            System.out.println("ssssssssssssssssssssssssssssssssssssssssss");
            return  Optional.empty();
        }
        catch ( IOException | InterruptedException | ApiException e){
            System.out.println(e.getMessage());
            throw new RouteServiceException("Error fetching route : " + e.getMessage()  );
        }





    }
}
