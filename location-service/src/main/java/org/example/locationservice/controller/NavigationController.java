package org.example.locationservice.controller;

import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.example.locationservice.model.dto.RouteRequestDto;
import org.example.locationservice.services.NavigationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/navigation")
@RequiredArgsConstructor
public class NavigationController {

    private final NavigationService navigationService;

    @PostMapping("/route")
    public ResponseEntity<?> route(@RequestBody RouteRequestDto routeRequestDto) throws IOException, InterruptedException, ApiException {
        return ResponseEntity.ok().body(navigationService.getRoute(routeRequestDto));
    }

}
