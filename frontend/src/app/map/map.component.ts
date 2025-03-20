  import { Component, OnInit } from '@angular/core';
  import { CUSTOM_MAP_STYLE} from '../config/custom_map_theme';


  import {Location} from '../services/location.service';
  import {SharedLocationService} from '../services/shared-location.service';

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


    constructor(private sharedLocationService: SharedLocationService,) {
    }

    ngOnInit(): void {


      this.initMap();
      this.sharedLocationService.destinationSource$.subscribe(location => {
        console.log("inside map destination"+location);

        if(location){
          console.log(location);
          this.setDestinationMarker(({lat:location.geometry.location.lat,lng:location.geometry.location.lng}) as Location);
        }
      })
      this.sharedLocationService.pickupSource$.subscribe(location => {
        // console.log("inside map pickup"+location.geometry.location.lat);
        if(location){
          this.setPickUpMarker(({lat:location.geometry.location.lat,lng:location.geometry.location.lng}) as Location);
        }
      })
      // this.setPickUpMarker(({lat:18.732848,lng:73.664258}) as Location);
      // this.setDestinationMarker(({lat:17.631174,lng:73.859010}) as Location);
    }

    setPickUpMarker(location: Location): void {

      if(!this.pickUpMarker){
        this.pickUpMarker = new google.maps.marker.AdvancedMarkerElement({
          position: new google.maps.LatLng(location.lat, location.lng),
          map: this.map,
          title: "Pickup Location",
          content: this.createCustomPickupMarker(), // Set custom marker

        });

      }else {
        this.pickUpMarker.position = new google.maps.LatLng(location.lat, location.lng);

      }
      this.updateBounds();
    }

      setDestinationMarker(location: Location): void {

        if(!this.destinationMarker){
          this.destinationMarker = new google.maps.marker.AdvancedMarkerElement({
            position: new google.maps.LatLng(location.lat, location.lng),
            map: this.map,
            title: "Destination Location",
            content: this.createCircleMarker(), // Custom circular marker

          });

        }else {
          this.destinationMarker.position = new google.maps.LatLng(location.lat, location.lng);

        }
        this.updateBounds();
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
    private createCircleMarker(): HTMLElement {
      const markerContainer = document.createElement("div");

      // Outer Black Circle
      const blackCircle = document.createElement("div");
      blackCircle.style.width = "20px";
      blackCircle.style.height = "20px";
      blackCircle.style.backgroundColor = "black";
      blackCircle.style.borderRadius = "50%"; // Makes it circular
      blackCircle.style.display = "flex";
      blackCircle.style.justifyContent = "center";
      blackCircle.style.alignItems = "center";

      // Inner White Circle
      const whiteCircle = document.createElement("div");
      whiteCircle.style.width = "10px";
      whiteCircle.style.height = "10px";
      whiteCircle.style.backgroundColor = "white";
      whiteCircle.style.borderRadius = "50%"; // Inner circle

      blackCircle.appendChild(whiteCircle);
      markerContainer.appendChild(blackCircle);

      return markerContainer;
    }

    private createCustomPickupMarker(): HTMLElement {
      const markerContainer = document.createElement("div");

      // Outer Black Square
      const blackSquare = document.createElement("div");
      blackSquare.style.width = "20px";
      blackSquare.style.height = "20px";
      blackSquare.style.backgroundColor = "black";
      blackSquare.style.display = "flex";
      blackSquare.style.justifyContent = "center";
      blackSquare.style.alignItems = "center";

      // Inner White Square
      const whiteSquare = document.createElement("div");
      whiteSquare.style.width = "10px";
      whiteSquare.style.height = "10px";
      whiteSquare.style.backgroundColor = "white";

      blackSquare.appendChild(whiteSquare);
      markerContainer.appendChild(blackSquare);

      return markerContainer;
    }


    // updateBounds(location: Location): void {
    //   this.bounds.extend(new google.maps.LatLngBounds(location.lat, location.lng));
    //   this.map.fitBounds(this.bounds);
    // }
    private updateBounds() {
      if(!this.map) return;
      const bounds = new google.maps.LatLngBounds();

      if (this.pickUpMarker && this.pickUpMarker.position) {
        bounds.extend(this.pickUpMarker.position as google.maps.LatLng);
      }

      if (this.destinationMarker && this.destinationMarker.position) {
        bounds.extend(this.destinationMarker.position as google.maps.LatLng);
      }

      if (!bounds.isEmpty()) {
        this.map.fitBounds(bounds);

        // Optional: Set a minimum zoom level so the map doesn't zoom out too much
        google.maps.event.addListenerOnce(this.map, "bounds_changed", () => {
          // @ts-ignore
          if (this.map && this.map.getZoom() > 15) {
            this.map.setZoom(15); // Restrict max zoom in
          }
        });
      }

      // if (this.pickUpMarker) {
      //   this.bounds.extend(this.pickUpMarker.position as google.maps.LatLng);
      // }
      // if (this.destinationMarker) {
      //   this.bounds.extend(this.destinationMarker.position as google.maps.LatLng);
      // }
      //
      // if (this.pickUpMarker || this.destinationMarker) {
      //   setTimeout(() => {
      //     this.map.fitBounds(this.bounds); // Adjust zoom to fit both markers
      //   }, 100); // Short delay to allow markers to render
      // }
    }


  }
