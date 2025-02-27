package org.example.locationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.PlaceDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.locationservice.exception.PlaceNotFoundException;
import org.example.locationservice.exception.TsAutocompleteException;
import org.example.locationservice.exception.ErrorCode;
import org.example.locationservice.model.dto.PlaceDetailDto;
import org.example.locationservice.model.dto.PlaceDetailRequestDto;
import org.example.locationservice.model.dto.TSSuggestionsDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService{
     private final GeoApiContext context;
     private final ObjectMapper objectMapper;

     /**
      * Returns a list of nullable Places as per the user Query
      * @return Optional<AutocompletePrediction[]>
      */
     public Optional<AutocompletePrediction[]> getAutocompletePlaces( TSSuggestionsDto requestDto) throws IOException, InterruptedException, ApiException {
          try {
               // Convert sessionToken string to Google API SessionToken object
               PlaceAutocompleteRequest.SessionToken token = new PlaceAutocompleteRequest.SessionToken(requestDto.getSearchToken());
               // Create the autocomplete request with session token
               AutocompletePrediction[] request = PlacesApi.placeAutocomplete(context, requestDto.getQ().trim(), token).await();
               return Optional.ofNullable(request);

          }catch (ApiException|IOException|InterruptedException e){
               throw new TsAutocompleteException(e.getMessage(),requestDto.getQ());
          }
     }

     /**
      * reruns Optional PlaceDetail
      */

     public Optional<PlaceDetailDto> getPlaceDetails(PlaceDetailRequestDto requestDto) throws IOException, InterruptedException, ApiException {
          try {

               PlaceAutocompleteRequest.SessionToken token = new PlaceAutocompleteRequest.SessionToken(requestDto.getSessionToken());
               PlaceDetails placeDetails = PlacesApi.placeDetails(context, requestDto.getPlaceId(), token).await();
               PlaceDetailDto placeDetailDto = new PlaceDetailDto(placeDetails);

               return Optional.of(placeDetailDto);
          }catch (IOException |InterruptedException e){
               log.error(e.getMessage(),e);
               throw new PlaceNotFoundException(e.getMessage(),requestDto.getPlaceId(), ErrorCode.NOT_FOUND);
          }catch (ApiException e){
               log.error(e.getMessage(),e);
               throw new PlaceNotFoundException(e.getMessage(),requestDto.getPlaceId(), ErrorCode.FORBIDDEN);
          }
     }
}
