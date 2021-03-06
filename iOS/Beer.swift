//
//  Beer.swift
//  BeerShare
//
//  Created by Chris on 12.06.21.
//

import Foundation

struct Beer: Identifiable, Codable {
    var id : Int?
    var brand: String
    var type: String
    var liter: String // Wird aus JSON als String erkannt --> "liter": "0.33",
    var country: String
    
    init(
        brand: String,
        type: String,
        liter: String,
        country: String
    ) {
        self.brand = brand
        self.type = type
        self.liter = liter
        self.country = country
    }
}
