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
    // return this.http.post<any[]>(`${this.apiUrl}/loadTSSuggestions`, body).pipe(
    //   map((res) => {
    //     res.map(item=>({
    //       description: item.description,
    //       mainText: item.structuredFormatting?.mainText || '',
    //       secondaryText: item.structuredFormatting?.secondaryText || '',
    //       placeId: item.placeId
    //     }) as AutoCompleteResult)
    //
    //   })
    // );
  }
}
//
// export class LocationService {
//
//   private apiUrl:string = `http://localhost:8080/api/location`; // Use environment variable
//
//
//   constructor(private http: HttpClient) {}
//
//   getAutoCompleteResults(q:string , type:string): Observable<AutoCompleteResult[]> {
//       const body={
//         "q":q,
//         "searchToken":"rad",
//         "type":type
//       }
//     return this.http.post<any[]>(`${this.apiUrl}/loadTSSuggestions`, body).pipe(
//
//     );
//   }
//
//   }
//


