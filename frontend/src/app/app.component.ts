import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {LocationSearchComponent} from "./location-search/location-search.component";
import {MapComponent} from './map/map.component';
import {GoogleMapsModule} from '@angular/google-maps';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LocationSearchComponent, MapComponent,GoogleMapsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
