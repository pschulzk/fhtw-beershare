//
//  Order.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import Foundation

class Order : Codable {
    var status : String
    var package : Package
    
    init(package: Package, status: String) {
        self.package = package
        self.status = status
        
    }
}
