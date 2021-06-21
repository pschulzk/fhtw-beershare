//
//  Order.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import Foundation

struct Order: Identifiable, Codable {
    var id = UUID()
    var status : String
    var package : Package
}
