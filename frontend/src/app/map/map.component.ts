import { Component, OnInit } from '@angular/core';
import { CUSTOM_MAP_STYLE} from '../config/custom_map_theme';


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

  constructor() {
  }

  ngOnInit(): void {
  this.initMap();
  }

  private initMap() {
    const mapOptions: google.maps.MapOptions = {
      center: { lat: 28.6139, lng: 77.2090 }, // Delhi
      zoom: 12,
      styles:CUSTOM_MAP_STYLE,
      mapId: "MapId1" // You can configure your Map ID from Google Cloud
    };
    this.map = new google.maps.Map(document.getElementById('map') as HTMLElement, mapOptions);
    // Add a marker at Delhi using AdvancedMarkerElement
    this.marker = new google.maps.marker.AdvancedMarkerElement({
      position: { lat: 28.6139, lng: 77.2090 },
      map: this.map,
      title: "Delhi"
    });

  }


}
