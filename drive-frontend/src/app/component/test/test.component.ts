
// @Component({
//   selector: 'app-test',
//   imports: [],
//   templateUrl: './test.component.html',
//   styleUrl: './test.component.css'
// })
// export class TestComponent {
//
// }


import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {MapComponent} from '../map/map.component';
import {LocationSearchComponent} from '../location-search/location-search.component';

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [CommonModule, FormsModule, MapComponent, LocationSearchComponent],
  template: `
    <nav class="flex justify-between items-center px-6 py-3 bg-white border-b border-gray-200">
      <div class="flex items-center">
        <a href="#" class="text-black text-2xl font-semibold">Uber</a>
      </div>
      <div class="flex items-center gap-4">
        <button class="p-2 hover:bg-gray-100 rounded-full">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path d="M8.5 2a4.5 4.5 0 00-4.5 4.5v3a4.5 4.5 0 004.5 4.5h3a4.5 4.5 0 004.5-4.5v-3A4.5 4.5 0 0011.5 2h-3zm5 8a.5.5 0 01-.5.5h-7a.5.5 0 010-1h7a.5.5 0 01.5.5z"/>
          </svg>
        </button>
        <button class="p-2 hover:bg-gray-100 rounded-full">
          <span class="font-medium">Activity</span>
        </button>
        <button class="flex items-center gap-2 p-2 hover:bg-gray-100 rounded-full">
          <div class="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-500" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"/>
            </svg>
          </div>
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"/>
          </svg>
        </button>
      </div>
    </nav>

    <div class="flex gap-4 p-4 h-[calc(100vh-64px)]">

      <div class="w-2/8 flex flex-col rounded-lg bg-white shadow-lg p-6">


        <div class="space-y-4 mb-6">
          <form (ngSubmit)="onSubmit($event)" class="flex flex-col gap-4">

            <h2 class="text-md font-semibold ">Get a ride</h2>

            <!-- Input Fields with Controlled Width -->
            <div class="w-80">
              <app-location-search type="PICKUP"></app-location-search>
            </div>
            <div class="w-80">
              <app-location-search type="DESTINATION"></app-location-search>
            </div>
            <!-- Button "Book Ride" -->
            <div>
              <button
                class="w-28 mt-4 py-1.5 rounded-md border border-transparent flex items-center justify-center text-sm
                 text-white bg-slate-800 hover:bg-slate-700 hover:bg-opacity-90 focus:bg-slate-700
                 active:bg-slate-900 shadow-md transition-all"
                type="submit"
              >
                Book Ride
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-4 h-4 ml-1">
                  <path fill-rule="evenodd" d="M16.72 7.72a.75.75 0 0 1 1.06 0l3.75 3.75a.75.75 0 0 1 0 1.06l-3.75 3.75a.75.75 0 1 1-1.06-1.06l2.47-2.47H3a.75.75 0 0 1 0-1.5h16.19l-2.47-2.47a.75.75 0 0 1 0-1.06Z" clip-rule="evenodd" />
                </svg>
              </button>
            </div>
          </form>



        </div>

        <button
          (click)="toggleSecondColumn()"
          class="w-full px-4 py-3 rounded-lg bg-[#276EF1] text-white hover:bg-blue-600 transition-colors mb-4"
        >
          {{ showSecondColumn ? 'Hide' : 'Show' }} Column 2
        </button>
      </div>

























      <div *ngIf="showSecondColumn" class="flex-1 flex items-center justify-center rounded-lg bg-[#7356BF] text-white">
        Column 2
      </div>


      <div class="flex-1 flex items-center justify-center rounded-lg bg-[#E11900] text-white">
        <app-map class="w-full h-full"></app-map>
      </div>
    </div>
  `,
  styles: []
})
export class TestComponent {
  pickupLocation = '';
  dropLocation = '';
  pickupTime = 'now';
  rideType = 'forme';
  showSecondColumn = false;

  toggleSecondColumn() {
    this.showSecondColumn = !this.showSecondColumn;
  }

  onSubmit($event: any) {

  }
}
