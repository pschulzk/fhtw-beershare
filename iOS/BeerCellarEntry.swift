//
//  BeerCellarEntry.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 23.06.21.
//

import Foundation

struct BeerCellarEntry: Hashable, Codable {
    var id: Int?
    var beerCellar: Int?
    var beer: Int
    var amount: Int
    var beerName: String?
    
    init (
        beerCellar: Int,
        beer: Int,
        amount: Int
    ) {
        self.beerCellar = beerCellar
        self.beer = beer
        self.amount = amount
    }
}
