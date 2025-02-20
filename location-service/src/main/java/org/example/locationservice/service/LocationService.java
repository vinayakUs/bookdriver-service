package org.example.locationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final GeoApiContext geoApiContext;

    public AutocompletePrediction[] getPlaceAutocomplete(String location , String sessionId) throws IOException, InterruptedException, ApiException {

        // Convert sessionToken string to Google API SessionToken object
        PlaceAutocompleteRequest.SessionToken token = new PlaceAutocompleteRequest.SessionToken();

        // Create the autocomplete request with session token
        AutocompletePrediction[] request = PlacesApi.placeAutocomplete(geoApiContext, location, token).await();
        ObjectMapper mapper = new ObjectMapper();
//        List<AutocompletePrediction> pred = mapper.readVal
        return request;

//        System.out.println(Arrays.toString(request));
    }




}
