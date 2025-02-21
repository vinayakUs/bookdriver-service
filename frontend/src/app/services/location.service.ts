import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
providedIn: 'root'
})
export class LocationService {

  private apiUrl:string = `http://localhost:8083/api/location`; // Use environment variable


  constructor(private http: HttpClient) {}

  getAutoCompleteResults(query:string):Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}?query=${query}`);
  }

}

