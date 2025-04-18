  import { Component, OnInit } from '@angular/core';
  import { CUSTOM_MAP_STYLE} from '../config/custom_map_theme';
  // @ts-ignore
  import polyline from '@mapbox/polyline';

  import {Location} from '../../service/location.service';

  import {SharedLocationService} from '../../service/shared-location.service';

  declare var google: any;

  @Component({
    selector: 'app-map',
    standalone: true,
    imports: [],
    templateUrl: './map.component.html',
    styleUrl: './map.component.css'
  })
  export class MapComponent implements OnInit {
    map!: google.maps.Map;
    marker !:google.maps.marker.AdvancedMarkerElement;
    pickUpMarker!: google.maps.marker.AdvancedMarkerElement | null;
    destinationMarker!: google.maps.marker.AdvancedMarkerElement | null;
    bounds!: google.maps.LatLngBounds;
    polyline!: google.maps.Polyline | null;


    constructor(private sharedLocationService: SharedLocationService,) {
    }
    removeDestinationMarker(): void {
      if (this.destinationMarker) {
        this.destinationMarker.map = null; // Remove from map
        this.destinationMarker = null;
      }
    }
    removePickupMarker(): void {
      if (this.pickUpMarker) {
        this.pickUpMarker.map = null; // Remove marker from the map
        this.pickUpMarker = null; // Now it's safe to assign null
      }
    }

    removeEncodedPath(): void {

      console.log("removeEncodedPath");
      if (this.polyline) {
        this.polyline.setMap(null);
        this.polyline = null;
      }
      console.log("removeEncodedPath > Done end");

      // this.polyline = new google.maps.Polyline({
      //   path: null,
      //   strokeColor: '#0000FF',
      //   strokeOpacity: 1.0,
      //   strokeWeight: 4,
      //   map: this.map // Attach to the map
      // });
    }
    ngOnInit(): void {
      this.initMap();

      this.sharedLocationService.encodedPathSource$.subscribe({next: (data) => {
          console.log('encoded path ---- ' +data);
          if(data){
            this.loadPolyline(data);
          }else {
            this.removeEncodedPath();
          }
        }});

      this.sharedLocationService.destinationSource$.subscribe(location => {
        console.log("inside map -> destination location "+location);


        if(location){
          console.log("inside map -> destination location -> subscribe "+location);
          this.setDestinationMarker(({lat:location.geometry.location.lat,lng:location.geometry.location.lng}) as Location);
        }else {
          console.log("inside map -> destination location > location null" )
          this.removeDestinationMarker(); // Remove marker when null

            // this.removeEncodedPath();
        }
      });
      this.sharedLocationService.pickupSource$.subscribe(location => {
        console.log("inside map -> pickup location "+location);
        if(location){
          console.log("inside map -> pickup location -> subscribe "+location);
          this.setPickUpMarker(({lat:location.geometry.location.lat,lng:location.geometry.location.lng}) as Location);
        }else {
          this.removePickupMarker(); // Remove marker when null
        }
      });
      // this.loadPolyline();
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

    private loadPolyline(encodedPoly:string): void {
      const  decodedPath = google.maps.geometry.encoding.decodePath(encodedPoly);
      // Draw the polyline on the map
      this.polyline = new google.maps.Polyline({
        path: decodedPath,
        strokeColor: '#0000FF',
        strokeOpacity: 1.0,
        strokeWeight: 4,
        map: this.map // Attach to the map
      });
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
