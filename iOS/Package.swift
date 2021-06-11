//
//  BeerPackage.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import Foundation

class Package : Codable {
    var sorte : String
    var anzahl : Int
    
    init(sorte: String, anzahl: Int){
        self.sorte = sorte
        self.anzahl = anzahl
    }
}

