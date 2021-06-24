//
//  BeerCellar.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 13.06.21.
//

import Foundation

struct BeerCellar: Identifiable, Codable {
    
    var id: Int
    var name: String
    var latitude, longitude: Double
    var address: Address
    var owner: String
    var entries: [BeerCellarEntry]?
    
    func getAddressString() -> String {
        print(self.address.address)
        return "\(self.address.address), \(self.address.zipCode) \(self.address.city), \(self.address.country)"
    }

}
