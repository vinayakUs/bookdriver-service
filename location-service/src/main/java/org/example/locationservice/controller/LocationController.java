package org.example.locationservice.controller;

import com.google.maps.errors.ApiException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.locationservice.model.dto.ApiResponseDto;
import org.example.locationservice.model.dto.TSSuggestionsDto;
import org.example.locationservice.services.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/loadTSSuggestions")
    public ResponseEntity<?> loadTSSuggestions(@Valid @RequestBody TSSuggestionsDto request) throws IOException, InterruptedException, ApiException {
      return ResponseEntity.status(HttpStatus.OK).body(locationService.getAutocompletePlaces(request).orElseThrow(()->new RuntimeException("sdas")));
    }

}
