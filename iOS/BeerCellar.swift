//
//  BeerCellar.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 13.06.21.
//

import Foundation

struct BeerCellar:  Identifiable, Codable {
    static func == (lhs: BeerCellar, rhs: BeerCellar) -> Bool {
        return lhs.name != rhs.name
    }
    
    var id: Int
    var name: String
    var latitude, longitude: Double
    var address: Address
}
