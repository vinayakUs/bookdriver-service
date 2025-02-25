import {Component, Input, OnInit} from '@angular/core';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {debounce, debounceTime, distinctUntilChanged, takeUntil} from 'rxjs';
import {NgForOf, NgIf} from '@angular/common';
import {LocationService, AutoCompleteResult, PlaceDetail} from '../services/location.service';

@Component({
  selector: 'app-location-search',
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './location-search.component.html',
  styleUrl: './location-search.component.css'
})
export class LocationSearchComponent implements OnInit {
  searchControl = new FormControl();
  results: AutoCompleteResult[] =[];
  @Input() type: string ='';
  placeDetail!: PlaceDetail;


  constructor(private locationService: LocationService) {
  }

  ngOnInit() {
    this.searchControl.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
    ).subscribe(
      value => {
        if (value && value.length > 2) {
          this.callApi(value);
        } else {
          this.results = [];
        }
      }
    )
  }

 private callApi(query: string) {
    this.locationService.getAutoCompleteResults(query,this.type).subscribe(
     (results:AutoCompleteResult[]) => {
       this.results = results;
     },
     error => {
       console.log(error);
     }
   )
  }

  private getPlaceDetails(result: AutoCompleteResult) {

   this.locationService.getPlaceDetail(result.placeId,this.type).subscribe(
     result => {
       console.log(result);
       this.placeDetail = result;
     },error =>{
       console.log(error);
     }
   );

  }

  selectResult(result: AutoCompleteResult) {
    this.searchControl.setValue(result.mainText,{emitEvent:false}); // Set selected value
    this.results = []; // Clear suggestions
    this.getPlaceDetails(result);
  }


}

