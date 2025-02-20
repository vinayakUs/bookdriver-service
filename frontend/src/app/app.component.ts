import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {LocationSearchComponent} from "./location-search/location-search.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LocationSearchComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
