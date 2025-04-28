import {Injectable} from '@angular/core';
import {from, map, Observable, of, timeout} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';


interface VehicleInfoApiResponse {
  data:{
    products:{
      tiers: Tier[];
    };
  };
}
interface Tier{
  products: Products[];
}

export interface Products {
  description: string;
  displayName: string;
  detailedDescription: string;
  etaStringShort: string;
  fares: Fare[];
  productImageUrl: string;
  discountPrimary?: string;
}
interface Fare {
  capacity: number;
  fare: string;
  preAdjustmentValue: string;

}

@Injectable({
  providedIn: 'root',
})
export class FareService {
  private apiUrl = 'http://localhost:8080/trip-service/api/auth';

  constructor(private http: HttpClient) {
  }


  getVehicleTypes(data:{}):Observable<VehicleInfoApiResponse>  {

    const apiResponse: VehicleInfoApiResponse = {
      "data": {
        "products": {
          "tiers": [
            {
              "products": [
                {
                  "displayName": "Uber Go",
                  "description": "Uber Go",
                  "detailedDescription": "Affordable compact rides",
                  "discountPrimary": "10%",
                  "etaStringShort": "9 mins",
                  "fares": [
                    {
                      "capacity": 4,
                      "fare": "₹102.90",
                      "preAdjustmentValue": "₹114.33"
                    }
                  ],
                  "productImageUrl": "https://d1a3f4spazzrp4.cloudfront.net/car-types/haloProductImages/Hatchback.png"
                },
                {
                  "displayName": "Premier",
                  "description": "Premier",
                  "detailedDescription": "Comfortable sedans, top-quality drivers",
                  "discountPrimary": "",
                  "etaStringShort": "5 mins",
                  "fares": [
                    {
                      "capacity": 4,
                      "fare": "₹132.24",
                      "preAdjustmentValue": ""
                    }
                  ],
                  "productImageUrl": "https://d1a3f4spazzrp4.cloudfront.net/car-types/haloProductImages/package_UberComfort_new_2022.png"
                },
                {
                  "displayName": "Auto",
                  "description": "Auto",
                  "detailedDescription": "Pay directly to driver, cash/UPI only",
                  "discountPrimary": "",
                  "etaStringShort": "1 min",
                  "fares": [
                    {
                      "capacity": 3,
                      "fare": "₹51.78",
                      "preAdjustmentValue": ""
                    }
                  ],
                  "productImageUrl": "https://d1a3f4spazzrp4.cloudfront.net/car-types/haloProductImages/v1.1/TukTuk_Green_v1.png"
                }
              ]
            }

          ]
        }
      }
    };


    return from(this.sleep(4000)).pipe(
      map( ()=> apiResponse),
    )


  }

  sleep(ms:number){
    return new Promise(resolve => setTimeout(resolve, ms));
  }






}


