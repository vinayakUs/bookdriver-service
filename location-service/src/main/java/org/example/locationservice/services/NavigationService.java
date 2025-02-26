package org.example.locationservice.services;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import lombok.RequiredArgsConstructor;
import org.example.locationservice.model.dto.Location;
import org.example.locationservice.model.dto.RouteRequestDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NavigationService {

    private final GeoApiContext context;



    public Optional<?> getRoute(RouteRequestDto routeRequestDto) throws IOException, InterruptedException, ApiException {
        Location pick = routeRequestDto.getPickupLocation();
        Location dest = routeRequestDto.getDestinationLocation();

        DirectionsResult res = DirectionsApi.newRequest(context).origin(new LatLng(pick.getLat(), pick.getLng()))
                .destination(new LatLng(dest.getLat(), dest.getLng())).mode(TravelMode.DRIVING).await();

        return Optional.ofNullable(res);

    }
}
