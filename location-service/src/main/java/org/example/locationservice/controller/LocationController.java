package org.example.locationservice.controller;

import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.locationservice.exception.ErrorCode;
import org.example.locationservice.exception.PlaceNotFoundException;
import org.example.locationservice.exception.ResourceNotFoundException;
import org.example.locationservice.exception.TsAutocompleteException;
import org.example.locationservice.model.dto.ApiResponseDto;
import org.example.locationservice.model.dto.PlaceDetailRequestDto;
import org.example.locationservice.model.dto.TSSuggestionsDto;
import org.example.locationservice.services.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/loadTSSuggestions")
    public ResponseEntity<?> loadTSSuggestions(@Valid @RequestBody TSSuggestionsDto request) throws IOException, InterruptedException, ApiException {

        Optional<AutocompletePrediction[]> res = locationService.getAutocompletePlaces(request);
        return ResponseEntity.status(HttpStatus.OK).body(res.orElse(null));
//      return ResponseEntity.status(HttpStatus.OK).body(locationService.getAutocompletePlaces(request).orElseThrow(()->new TsAutocompleteException(request.getQ(),"Something Unexpected Occurred")));
    }

    @PostMapping("/loadPlaceDetails")
    public ResponseEntity<?> loadPlaceDetails(@Valid @RequestBody PlaceDetailRequestDto requestDto) throws IOException, InterruptedException, ApiException {
        return locationService.getPlaceDetails(requestDto)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new PlaceNotFoundException("Something Wrong",requestDto.getPlaceId(), ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
