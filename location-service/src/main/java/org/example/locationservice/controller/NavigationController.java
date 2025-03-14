package org.example.locationservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.locationservice.exception.RouteNotFoundException;
import org.example.locationservice.exception.RouteServiceException;
import org.example.locationservice.model.dto.RouteRequestDto;
import org.example.locationservice.model.dto.RouteResponseDto;
import org.example.locationservice.services.NavigationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/navigation")
@RequiredArgsConstructor
public class NavigationController {

    private final NavigationService navigationService;

    @PostMapping("/route")
    public ResponseEntity<RouteResponseDto> route(@RequestBody RouteRequestDto routeRequestDto)   {

      return    navigationService.getRoute(routeRequestDto) .map(y->{
              return ResponseEntity.ok().body(y);
          }).orElse(
                  ResponseEntity.ok().body(new RouteResponseDto())
      );

        };



}
