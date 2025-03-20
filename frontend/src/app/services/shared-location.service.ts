import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {PlaceDetail} from './location.service';


@Injectable({
  providedIn: 'root'
})
export class SharedLocationService{
  private pickupSource= new BehaviorSubject<any>(null);
  private destinationSource= new BehaviorSubject<any>(null);

  pickupSource$ = this.pickupSource.asObservable();
  destinationSource$ = this.destinationSource.asObservable();

  updatePickupSourceLocation(location:PlaceDetail){
    console.log("inside shared destination"+location);
    this.pickupSource.next(location);
  }

  updateDestinationLocation(destination:PlaceDetail){

    console.log("inside shared destination"+location);

    this.destinationSource.next(destination);
  }


}
