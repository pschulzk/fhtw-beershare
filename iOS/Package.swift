//
//  BeerPackage.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import Foundation

struct Package: Identifiable, Codable {
    var id = UUID()
    var sorte : String
    var anzahl : Int
}

