package org.example.locationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.PlaceDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDetailDto {



    private String formattedAddress;
    private Geometry geometry;
    private String placeId;
    private String vicinity;
    private List<AddressComponent> addressComponents;


    public PlaceDetailDto(PlaceDetails placeDetails){
        this.addressComponents = Arrays.stream(placeDetails.addressComponents).map((comp)->{
            AddressComponent addressComponent = new AddressComponent();
            addressComponent.longName = comp.longName;
            addressComponent.shortName = comp.shortName;
            addressComponent.types = Arrays.stream(comp.types).map(AddressComponentType::toString).toList();
            return addressComponent;
        }).toList();
        this.placeId = placeDetails.placeId;
        this.formattedAddress = placeDetails.formattedAddress;
        Location location = new Location(placeDetails.geometry.location.lat, placeDetails.geometry.location.lng);
        Geometry geometry = new Geometry();
        geometry.location = location;
        this.geometry=geometry;
    }

    @Setter
    @Getter
    public static class Geometry {
        private Location location;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Location {
        private double lat;
        private double lng;
    }

    @Setter
    @Getter
//    @AllArgsConstructor
    public static class AddressComponent {
        private String longName;
        private String shortName;
        private List<String> types;
    }
}



/*
{
    "addressComponents": [
        {
            "longName": "Talegaon Dabhade",
            "shortName": "Talegaon Dabhade",
            "types": [
                "LOCALITY",
                "POLITICAL"
            ]
        },
        {
            "longName": "Pune",
            "shortName": "Pune",
            "types": [
                "ADMINISTRATIVE_AREA_LEVEL_3",
                "POLITICAL"
            ]
        },
        {
            "longName": "Pune Division",
            "shortName": "Pune Division",
            "types": [
                "ADMINISTRATIVE_AREA_LEVEL_2",
                "POLITICAL"
            ]
        },
        {
            "longName": "Maharashtra",
            "shortName": "MH",
            "types": [
                "ADMINISTRATIVE_AREA_LEVEL_1",
                "POLITICAL"
            ]
        },
        {
            "longName": "India",
            "shortName": "IN",
            "types": [
                "COUNTRY",
                "POLITICAL"
            ]
        }
    ],
    "adrAddress": "<span class=\"locality\">Talegaon Dabhade</span>, <span class=\"region\">Maharashtra</span>, <span class=\"country-name\">India</span>",
    "businessStatus": null,
    "curbsidePickup": null,
    "currentOpeningHours": null,
    "delivery": null,
    "dineIn": null,
    "editorialSummary": null,
    "formattedAddress": "Talegaon Dabhade, Maharashtra, India",
    "formattedPhoneNumber": null,
    "geometry": {
        "bounds": null,
        "location": {
            "lat": 18.7375763,
            "lng": 73.67466259999999
        },
        "locationType": null,
        "viewport": {
            "northeast": {
                "lat": 18.75766508242529,
                "lng": 73.71260753539946
            },
            "southwest": {
                "lat": 18.70277919230993,
                "lng": 73.64882404249458
            }
        }
    },
    "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
    "internationalPhoneNumber": null,
    "name": "Talegaon Dabhade",
    "openingHours": null,
    "permanentlyClosed": false,
    "photos": [
        {
            "photoReference": "AVzFdbkfJFuYy8h8AG7M4WF6hU4CQQWLvSXEm35po9cnmjXSGJYSA3c5GReyqCk19tIolZrB0ez6jRKIj5BQ4f74jqxIJvno4edelHALJuioQH_Wrr8Gb1WFg2bGpM-UZ277KZI6zYXky0UESco8JdOwqNOxJO-Bsc0kUBb2eiiPAA6BgxmYsXIP_-UW",
            "height": 3000,
            "width": 4000,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/110837228710190192514\">Yogesh Mehta</a>"
            ]
        },
        {
            "photoReference": "AVzFdbnAdxwprgSuuRwD_xV1wX_fDJHvpZ-tT4Avfe3dc9XzpJ3wRMYqOMwbhbWZsPp8A-hrB4GEJ7WlRGSKjl1pnegw4e9HYf19JT8pRYTGJeuFgQnePDQj85ddqjMDzKb_inLFnJlspWOBZynwIBzXg6TS-tlsLV1uOm39DwFiDaq2BcyVRYEsIItD",
            "height": 1908,
            "width": 4032,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/100869925926421447886\">VINAYAK KUMBHAR</a>"
            ]
        },
        {
            "photoReference": "AVzFdbnPJPTJn-IK8Pj7bG9a7-j8WVZ5O8oy3MkUyK0AQSu1FXHU52Qeq4fjrn5680eXLNecLd9OhucNgh40lCsFBikVtUn84JIEaSZbo0wuKrd8SQ1hoqbd0wilsficmnC2kBWucMW7Zl68e3TNg9ZlAz_DYNjgV1euxXz3n7dGN9v56B3GrjtsV12-",
            "height": 3120,
            "width": 4160,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/101211544080415572943\">POOJA RASHINKAR</a>"
            ]
        },
        {
            "photoReference": "AVzFdblirzaErAdo583k5c7wNi-rfGgzNTrtDTG3QgvOTB3AYdD9uEhZQtikqNVl4KLwjgiqIM2zdgMwZMBTGb1xDaCOUNe6WsdHUdFyaIyy0fAhJG6SFMnYP5pGoJFDzniTt1tnh_cLOkYayhALckfw2uTT9Yqe-ROG0HQyL_vYojWAcV_bTZO1lMeF",
            "height": 3000,
            "width": 4000,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/110837228710190192514\">Yogesh Mehta</a>"
            ]
        },
        {
            "photoReference": "AVzFdbnzdTQDDCgb8-YFsTnGKJ7dixoxYoDe9CWnJj307YJoZL_Or7FdLlb8dxByw6At18UgFPyCcOx7yWGwOO4DTkIS7eBEjP4Xi5FgkrYc-If1-LRMEkVHNfcJHXue8Gh6QPPX2r1W3YKgQYgTzR6gle_83xKsLgcV902cR_4SyfDX6ZoTSlkVyEUp",
            "height": 4608,
            "width": 2176,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/106971003794821003677\">ramesh desai</a>"
            ]
        },
        {
            "photoReference": "AVzFdbkQCDYvpUBlSF07m8i86uN8dHq1dG47mj0aMyp9TrC19pXdRJ-w7D4gBiO3HpX0UxtgYWJ6L0QU8uBue5pRuHNjLx6rKdYn5AqwoZkwzQ85OFe2wOZIlFFEmuNaQ2bvTBxdwDSd62LWtEgUUnsvAW0dfyExzpRNC0qWQ5iojKnqEIfMrfpdJR86",
            "height": 2632,
            "width": 3177,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/114248937217843931951\">Anirudha Jadhav</a>"
            ]
        },
        {
            "photoReference": "AVzFdbnp_hZODVmYJnSv1Hlq6qPhdpVSsFyD2JifL15J0gwi-XzMKsgfOvb1EaHFzmZEYojegvVZJSjcDnPAUNBhlV_CLxDFp7C1Y0JQ-f9c9u3w1Xn4B7uUeu9XHsA9ngRrKPr7QkYtX3FEIc11dRJ42sQqWgPpUZMHHt9vdPOXaEEtveVQAKUZ5wrU",
            "height": 3000,
            "width": 4000,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/114248937217843931951\">Anirudha Jadhav</a>"
            ]
        },
        {
            "photoReference": "AVzFdbn8J0VHNG0TrQ_0LYjriw0mpqMj2hUAUNDhv2WNBw0e_UNE1VaJHyPLPyYYV84awZI6jrRcz6G5o3NT2bqI2JDfrcKLE1tmf_w7K8gDEGZEda6Jqtx5AmvX32M4IjeWo4PXby54hqnBqZCwDHMgRI9ajqhwuS4-Tr5v2uPVqRaawTiQViDJscQE",
            "height": 3000,
            "width": 4000,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/111545146368603656752\">Pixaanch</a>"
            ]
        },
        {
            "photoReference": "AVzFdbn8C-2fwbJh55wugymBk4hSK8e4Cc7Ds6Yfyga0P4Jg7SVCOY4P7791pGohbwEDIhyY27sygU-2_wW3Nu1AVlWIHX_aIvVEw1Cv38ONjX3oZWWH1UPJAJkSxOa7h03tfQG-VkXCGpfaEB6QPBy_Lo6XKWwbXZScnFBF6tZmj-eGuKCYcG8v55Vg",
            "height": 3000,
            "width": 4000,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/100466131103733605158\">Shrikrishna Kulkarni</a>"
            ]
        },
        {
            "photoReference": "AVzFdbnZP8aDTT0FcKPk4nLnOJA9Z44oRqHxOvZSIoT-OFQeAZD4Zuo8UAVBKBCxGzHZ73AvudFZKW1qAHZ1gBwZdi6TsLT6h3VskV6YoBQdSKh2Aeb0_-VOUP8DElPyj1YG-VsEOnxJ_mFnoi5jDIFFdz8vgDxcqYKvPfsDHz-nSrREPdhIIcF0-Mr2",
            "height": 4032,
            "width": 2268,
            "htmlAttributions": [
                "<a href=\"https://maps.google.com/maps/contrib/118052971864468388064\">mayur karpe</a>"
            ]
        }
    ],
    "placeId": "ChIJqyhvn4yxwjsRbfp2pni_SvU",
    "scope": null,
    "plusCode": null,
    "priceLevel": null,
    "altIds": null,
    "rating": 0.0,
    "reservable": null,
    "reviews": null,
    "secondaryOpeningHours": null,
    "servesBeer": null,
    "servesBreakfast": null,
    "servesBrunch": null,
    "servesDinner": null,
    "servesLunch": null,
    "servesVegetarianFood": null,
    "servesWine": null,
    "takeout": null,
    "types": [
        "LOCALITY",
        "POLITICAL"
    ],
    "url": "https://maps.google.com/?q=Talegaon+Dabhade,+Maharashtra,+India&ftid=0x3bc2b18c9f6f28ab:0xf54abf78a676fa6d",
    "userRatingsTotal": 0,
    "utcOffset": 330,
    "vicinity": "Talegaon Dabhade",
    "website": null,
    "wheelchairAccessibleEntrance": null,
    "htmlAttributions": []
}
 */