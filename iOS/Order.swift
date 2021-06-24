//
//  Order.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import Foundation

struct Order: Identifiable, Codable {
    var id: Int?
    var amount: Int
    var status: Int
    var datetime: String?
    var beerCellar: Int
    var beer: Int
    var buyer: String?
    var beerName: String?
    
    init(
        amount: Int,
        status: Int,
        beerCellar: Int,
        beer: Int
    ) {
        self.amount = amount
        self.status = status
        self.beerCellar = beerCellar
        self.beer = beer
    }
}
