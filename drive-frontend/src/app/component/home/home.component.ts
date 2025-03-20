import { Component } from '@angular/core';
import {AuthService} from '../../service/auth.service';

import {FormsModule} from '@angular/forms';
import {LocationSearchComponent} from '../location-search/location-search.component';
import {MapComponent} from '../map/map.component';
import {SharedLocationService} from '../../service/shared-location.service';
import {PlaceDetail} from '../../service/location.service';
import {RouteService} from '../../service/route.service';

@Component({
  standalone:true,
  selector: 'app-home',
  imports: [
    FormsModule,
    LocationSearchComponent,
    MapComponent
  ],
  templateUrl: './home.component.html',
 // template:
 //   `
 //    <h2>Welcome to the Dashboard</h2>
 //    <button (click)="logout()">Logout</button>
 //  `,
  styleUrl: './home.component.css'
})
export class HomeComponent {

  constructor(private authService: AuthService,private sharedLocationService: SharedLocationService,private routeService:RouteService) {}
  onSubmit(event: Event) {
    event.preventDefault(); // Prevents page refresh
    const pickup:PlaceDetail  = this.sharedLocationService.getLatestPickupLocation();
    const drop:PlaceDetail  = this.sharedLocationService.getLatestDestinationLocation();

    if(pickup && drop){
      this.routeService.getRoute(pickup, drop).subscribe({
        next: (data) => {
          this.sharedLocationService.updateEncodedPath(data.polyline);
        },
        error: (err) => alert(err.message), // Display error message in UI
      });
    }


    console.log('Ride booking submitted!');
  }
  logout() {
    this.authService.logout();
  }
}
