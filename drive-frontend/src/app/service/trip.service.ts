import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {PlaceDetail} from './location.service';
import {RoutePolyline} from './route.service';

@Injectable({
  providedIn: 'root'
})

export class TripService {
  constructor(private http: HttpClient) {}
      url:string = '';

    requestTrip(orgin:):Observable<String>{

      return of("");

    }

}
