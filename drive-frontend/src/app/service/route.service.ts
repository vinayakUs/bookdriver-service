import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {Location, PlaceDetail} from './location.service';

export interface RoutePolyline {
  polyline:string,
  distance:number,
  eta:number
}


@Injectable({ providedIn: 'root' })
export class RouteService{
  constructor(private http: HttpClient) {
  }
  private apiUrl = 'http://localhost:8080/api/navigation/route';

  getRoute(p1:PlaceDetail,p2:PlaceDetail):Observable<RoutePolyline>{
    let postData = {
      pickupLocation: ({
        lat: p1.geometry.location.lat,
        lng: p1.geometry.location.lng,
        type: 'PICKUP'
      }) as Location,
      destinationLocation:  ({
        lat: p2.geometry.location.lat,
        lng: p2.geometry.location.lng,
        type: 'DESTINATION'
      })as Location,
    };

    return this.http.post<RoutePolyline>(this.apiUrl,postData).pipe(
      tap(response => {
        console.log(response);
        return response;
      }),
      catchError(this.handleError)
    )

  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';

    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `Client Error: ${error.error.message}`;
    } else {
      // Backend error (HTTP status codes)
      errorMessage = `Server Error (${error.status}): ${error.message}`;
    }

    console.error('Error Occurred:', errorMessage); // Log error to console
    return throwError(() => new Error(errorMessage)); // Return error observable
  }


  }
