package org.example.locationservice.controller;

import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.example.locationservice.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("query") String query,@RequestParam("sessionToken") String sessionToken) throws IOException, InterruptedException, ApiException {
        return ResponseEntity.ok().body(locationService.getPlaceAutocomplete(query,sessionToken));
    }


}
