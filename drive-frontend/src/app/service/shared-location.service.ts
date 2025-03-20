import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {PlaceDetail} from './location.service';


@Injectable({
  providedIn: 'root'
})
export class SharedLocationService{
  private pickupSource= new BehaviorSubject<any>(null);
  private destinationSource= new BehaviorSubject<any>(null);
  private encodedPathSource  = new BehaviorSubject<string | null>(null);

  pickupSource$ = this.pickupSource.asObservable();
  destinationSource$ = this.destinationSource.asObservable();
  encodedPathSource$ = this.encodedPathSource.asObservable(); // Observable for polyline

  updatePickupSourceLocation(location:PlaceDetail|null){
    console.log("inside shared pickup "+location);
    this.pickupSource.next(location);
  }

  updateDestinationLocation(destination:PlaceDetail|null){
    console.log("inside shared destination "+location);
    this.destinationSource.next(destination);
  }
  updateEncodedPath(path: string) {  // New method
    console.log("inside encoded path "+location);
    this.encodedPathSource.next(path);
  }
  getLatestPickupLocation():PlaceDetail{
    return this.pickupSource.getValue();
  }
  getLatestDestinationLocation():PlaceDetail{
    return this.destinationSource.getValue();
  }
  getEncodedPath(): string|null {
    return this.encodedPathSource.getValue();
  }

}
