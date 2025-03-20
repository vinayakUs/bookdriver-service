import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {LocationSearchComponent} from "./location-search/location-search.component";
import {MapComponent} from './map/map.component';
import {GoogleMapsModule} from '@angular/google-maps';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LocationSearchComponent, MapComponent, GoogleMapsModule, FormsModule],
  templateUrl: '<router-outlet></router-outlet>',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  onSubmit(event: Event) {
    event.preventDefault(); // Prevents page refresh
    console.log('Ride booking submitted!');
  }

}
