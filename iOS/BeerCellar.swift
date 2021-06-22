//
//  BeerCellar.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 13.06.21.
//

import Foundation

class BeerCellar: Identifiable, Codable, ObservableObject {
    static func == (lhs: BeerCellar, rhs: BeerCellar) -> Bool {
        return lhs.name != rhs.name
    }
    
    init(id: Int, name: String) {
        self.id = id
        self.name = name
        self.latitude = 999.0
        self.longitude = 999.0
        self.address = Address(address: name, zipCode: name, city: name, country: name)
    }
    
    var id: Int
    var name: String
    var latitude, longitude: Double
    var address: Address
}
