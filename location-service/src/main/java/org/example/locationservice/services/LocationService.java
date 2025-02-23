package org.example.locationservice.services;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.locationservice.model.dto.TSSuggestionsDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService{
     private final GeoApiContext context;

     /**
      * Returns a list of nullable Places as per the user Query
      * @return Optional<AutocompletePrediction[]>
      */
     public Optional<AutocompletePrediction[]> getAutocompletePlaces(@Valid TSSuggestionsDto requestDto) throws IOException, InterruptedException, ApiException {
          // Convert sessionToken string to Google API SessionToken object
          PlaceAutocompleteRequest.SessionToken token = new PlaceAutocompleteRequest.SessionToken(requestDto.getSearchToken());
          // Create the autocomplete request with session token
          AutocompletePrediction[] request = PlacesApi.placeAutocomplete(context, requestDto.getQ().trim(), token).await();
          return Optional.ofNullable(request);
     }
}
