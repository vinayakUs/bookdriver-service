

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {MapComponent} from '../map/map.component';
import {LocationSearchComponent} from '../location-search/location-search.component';

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [CommonModule, FormsModule, MapComponent, LocationSearchComponent],
  templateUrl: 'test.component.html',
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
