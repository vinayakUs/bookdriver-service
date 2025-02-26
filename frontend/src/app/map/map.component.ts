import { Component, OnInit } from '@angular/core';
import { CUSTOM_MAP_STYLE} from '../config/custom_map_theme';


import {Location} from '../services/location.service';

declare var google: any;

@Component({
  selector: 'app-map',
  imports: [],
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements OnInit {
  map!: google.maps.Map;
  marker !:google.maps.marker.AdvancedMarkerElement;
  pickUpMarker!: google.maps.marker.AdvancedMarkerElement;
  destinationMarker!: google.maps.marker.AdvancedMarkerElement;
  bounds!: google.maps.LatLngBounds;

  constructor() {
  }

  ngOnInit(): void {


    this.initMap();
    this.setPickUpMarker(({lat:18.732848,lng:73.664258}) as Location);
    this.setDestinationMarker(({lat:17.631174,lng:73.859010}) as Location);
  }

  setPickUpMarker(location: Location): void {
    if (this.pickUpMarker) {
      this.pickUpMarker.map=null;
    }
    this.pickUpMarker = new google.maps.Marker({
       position: new google.maps.LatLng(location.lat, location.lng),
      map: this.map,
      title: "this.pickUpMarker.title",
    })
    this.updateBounds( );

  // this.map.setCenter(new google.maps.LatLng(location.lat, location.lng));
  }

  setDestinationMarker(location: Location): void {
    if (this.pickUpMarker) {
      this.pickUpMarker.map=null;
    }
    this.destinationMarker = new google.maps.Marker({
     position: new google.maps.LatLng(location.lat, location.lng),

      map: this.map,
      title: "this.pickUpMarker.title",
    })
    // this.map.setCenter(new google.maps.LatLng(location.lat, location.lng));
     this.updateBounds( );

  }

  private initMap() {
    const mapOptions: google.maps.MapOptions = {
      center: { lat: 28.6139, lng: 77.2090 }, // Delhi
      zoom: 12,
      disableDefaultUI: true,
      styles:CUSTOM_MAP_STYLE,
      mapId: "MapId1" // You can configure your Map ID from Google Cloud
    };
    this.map = new google.maps.Map(document.getElementById('map') as HTMLElement, mapOptions);
    this.bounds = new google.maps.LatLngBounds();


  }

  // updateBounds(location: Location): void {
  //   this.bounds.extend(new google.maps.LatLngBounds(location.lat, location.lng));
  //   this.map.fitBounds(this.bounds);
  // }
  private updateBounds() {

    if (this.pickUpMarker) {
      this.bounds.extend(this.pickUpMarker.position as google.maps.LatLng);
    }
    if (this.destinationMarker) {
      this.bounds.extend(this.destinationMarker.position as google.maps.LatLng);
    }

    if (this.pickUpMarker || this.destinationMarker) {
      setTimeout(() => {
        this.map.fitBounds(this.bounds); // Adjust zoom to fit both markers
      }, 100); // Short delay to allow markers to render
    }
  }


}
