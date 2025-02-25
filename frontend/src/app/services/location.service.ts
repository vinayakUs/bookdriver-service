import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {map, Observable} from 'rxjs';





 export interface AutoCompleteResult {
  description: string;
  mainText: string;
  secondaryText: string;
  placeId: string;
}
export interface PlaceDetail {
  formattedAddress: string;
  geometry: Geometry;
  placeId: string;
  vicinity?: string;
  addressComponents: AddressComponent[];
}

export interface Geometry {
  location: Location;
}

export interface Location {
  lat: number;
  lng: number;
}

export interface AddressComponent {
  longName: string;
  shortName: string;
  types: string[];
}

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private apiUrl = 'http://localhost:8080/api/location';
  constructor(private http: HttpClient) { }

  getAutoCompleteResults(q:string,type:string):Observable<AutoCompleteResult[]>{
    const body={
        "q":q,
        "searchToken":"rad",
        "type":type
      }
    return this.http.post<any[]>(`${this.apiUrl}/loadTSSuggestions`, body).pipe(
      map(response => response.map(item => ({
        description: item.description,
        mainText: item.structuredFormatting?.mainText || '',
        secondaryText: item.structuredFormatting?.secondaryText || '',
        placeId: item.placeId
      }) as AutoCompleteResult))
    );

  }

  getPlaceDetail(id:string,type:string):Observable<PlaceDetail>{
    const body={
      "placeId":"ChIJLbZ-NFv9DDkRQJY4FbcFcgM",
      "sessionToken":"123"
    }

    return this.http.post<any>(`${this.apiUrl}/loadPlaceDetails`, body).pipe(
      map(response=>({
        formattedAddress: response.formattedAddress,
        geometry: {
          location:{
            lat: response.geometry.location.lat,
            lng: response.geometry.location.lng
          }
        },
        placeId:response.placeId,
        vicinity:response.vicinity??'',
        addressComponents:response.addressComponents.map((comp:any) => ({
          longName:comp.longName,
          shortName:comp.shortName,
          types:comp.types
        }))
      }) as PlaceDetail)
    );
  }
}
