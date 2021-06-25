//
//  BeerCellar.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 13.06.21.
//

import Foundation

struct BeerCellar: Identifiable, Codable {
    
    var id: Int?
    var name: String
    var latitude, longitude: Double
    var address: Address
    var owner: String?
    var entries: [BeerCellarEntry]?
    
    init(
        name: String,
        latitude: Double,
        longitude: Double,
        address: Address
    ) {
        self.name = name
        self.latitude = latitude
        self.longitude = longitude
        self.address = address
    }
    
    func getAddressString() -> String {
        print(self.address.address)
        return "\(self.address.address), \(self.address.zipCode) \(self.address.city), \(self.address.country)"
    }

}
