import {Component, OnInit} from '@angular/core';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {debounce, debounceTime, distinctUntilChanged} from 'rxjs';
import {NgForOf, NgIf} from '@angular/common';

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
  results: string[] =[];

  constructor() {
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
    // Simulating an API call (replace with real API call)
    console.log('Calling API for:', query);
    setTimeout(() => {
      this.results = [
        `${query} City`,
        `${query} Street`,
        `${query} Avenue`
      ]; // Example mock data
    }, 1000);
  }
  selectResult(result: string) {
    this.searchControl.setValue(result,{emitEvent:false}); // Set selected value
    this.results = []; // Clear suggestions
  }

}

