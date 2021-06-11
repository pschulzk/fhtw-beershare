//
//  Location.swift
//  BeerShare
//
//  Created by Chris on 04.06.21.
//

import Foundation
import MapKit

struct Location: Identifiable {
    let id = UUID()
    var name: String
    var coordinate: CLLocationCoordinate2D
    
    init(name: String, latitude: Double, longitude: Double){
        self.name = name
        self.coordinate = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    }
}
