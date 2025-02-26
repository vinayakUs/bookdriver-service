import {Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output} from '@angular/core';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {debounce, debounceTime, distinctUntilChanged, takeUntil} from 'rxjs';
import {NgForOf, NgIf} from '@angular/common';
import {LocationService, AutoCompleteResult, PlaceDetail} from '../services/location.service';
import { v4 as uuidv4 } from 'uuid';

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
  randomUUID:string='';
  @Output() locationSelected = new EventEmitter<{ lat: number, lng: number }>();
  isDropdownOpen:boolean = false;
  showClearButton = false;


  constructor(private locationService: LocationService,private elementRef:ElementRef) {}
  clearInput() {
    this.searchControl.setValue('');
  }
  ngOnInit() {
    this.generateUUID();
    this.searchControl.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
    ).subscribe(
      value => {
        console.log('value', !!value)
        this.showClearButton = !!value;
        if (value && value.length > 2) {
          this.callApi(value);
        } else {
          this.results = [];
        }
      }
    )
  }

 private callApi(query: string) {
    console.log(this.randomUUID)
    this.locationService.getAutoCompleteResults(query,this.type,this.randomUUID).subscribe(
     (results:AutoCompleteResult[]) => {
       this.results = results;
     },
     error => {
       console.log(error);
     }
   )
  }

  private getPlaceDetails(result: AutoCompleteResult) {
   this.locationService.getPlaceDetail(result.placeId,this.type,this.randomUUID).subscribe(
     result => {
       console.log(result);
       this.placeDetail = result;
     },error =>{
       console.log(error);
     }
   );
  }

  selectResult(result: AutoCompleteResult) {
    this.generateUUID();
    this.searchControl.setValue(result.mainText,{emitEvent:false}); // Set selected value
    this.results = []; // Clear suggestions
    this.getPlaceDetails(result);
    // this.isDropdownOpen = false; // Close dropdown when selecting an item

  }

  generateUUID(){
    this.randomUUID=uuidv4();
  }




}

