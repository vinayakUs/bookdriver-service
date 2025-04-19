import { Component } from '@angular/core';
import {AuthService} from '../../service/auth.service';

import {FormsModule} from '@angular/forms';
import {LocationSearchComponent} from '../location-search/location-search.component';
import {MapComponent} from '../map/map.component';
import {SharedLocationService} from '../../service/shared-location.service';
import {PlaceDetail} from '../../service/location.service';
import {RouteService} from '../../service/route.service';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {MessageService} from 'primeng/api';
import {FareService} from '../../service/fare.service';
import {Products} from '../../service/fare.service';
import {lastValueFrom} from 'rxjs';

@Component({
  standalone:true,
  selector: 'app-home',
  imports: [
    FormsModule,
    LocationSearchComponent,
    MapComponent,
    NgIf,
    NgForOf,
    NgOptimizedImage
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
  isLoading: boolean = false;
  availableOption: Products[] = [];

  selectedRide: string | null = null;


  constructor(private authService: AuthService,
              private sharedLocationService: SharedLocationService,
              private routeService:RouteService,
              private messageService:MessageService,
              private fareService:FareService
  ) {}

  showSecondColumn = false;

  toggleSecondColumn() {
    this.showSecondColumn = !this.showSecondColumn;
  }

 async onSubmit(event: Event) {
    event.preventDefault(); // Prevents page refresh

    const pickup:PlaceDetail  = this.sharedLocationService.getLatestPickupLocation();
    const drop:PlaceDetail  = this.sharedLocationService.getLatestDestinationLocation();

    if(!pickup || !drop){
      this.messageService.add({
        severity: 'error',
        summary: 'Missing Locations',
        detail: 'Select pickup and Drop location',
      });
      return;
    }

    try {
     const [routeResponse , vehicleInfoResponse ]  = await Promise.all([
       lastValueFrom(this.routeService.getRoute(pickup,drop)),
       lastValueFrom(this.fareService.getVehicleTypes({}))
     ]);

      this.sharedLocationService.updateEncodedPath(routeResponse.polyline);
      this.availableOption =
        vehicleInfoResponse.data.products.tiers.flatMap(tier =>
          tier.products
        );
      if(this.availableOption!=null){
        this.toggleSecondColumn();
      }

    } catch(err:any) {
      // Extract error details
      const errorResponse = err.error;
      const success = errorResponse?.success ?? false;
      const message = errorResponse?.data ?? 'An error occurred';
      this.messageService.add({
        severity: success ? 'success' : 'error',
        summary: 'Something went wrong',
        detail: message
      });

    }


    // console.log('Home', "onsubmit Home 1 ");
    //
    // // if(pickup && drop){
    // //   this.routeService.getRoute(pickup, drop).subscribe({
    // //     next: (data) => {
    // //       this.sharedLocationService.updateEncodedPath(data.polyline);
    // //     },
    // //     error: (err) => {
    // //       console.log("Get route polyline : "+err);
    // //       const errorResponse = err.error;
    // //       const success = errorResponse?.success ?? false;
    // //       const message = errorResponse?.data ?? 'An unexpected error occurred';
    // //
    // //       this.messageService.add({
    // //         severity: success ? 'success' : 'error',
    // //         summary: 'GetRoute Failed',
    // //         detail: message ,
    // //       })
    // //
    // //     } // Display error message in UI
    // //   });
    // // }
    //
    // console.log('Home', "onsubmit Home 2 ");
    //
    // this.fareService.getVehicleTypes({}).subscribe({
    //   next: (data) => {
    //    this.availableOption =
    //       data.data.products.tiers.flatMap(tier =>
    //         tier.products
    //     );
    //    if(this.availableOption!=null){
    //      this.toggleSecondColumn();
    //    }
    //
    //   }
    // });


    console.log('Ride booking submitted!');
  }
  logout() {
    this.authService.logout();
  }

  selectRide(ride: string) {
    this.selectedRide = ride;
  }

}
