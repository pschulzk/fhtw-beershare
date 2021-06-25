//
//  Address.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 13.06.21.
//

import Foundation

// MARK: - Address
struct Address: Hashable, Codable {
    var address, zipCode, city, country: String

    enum CodingKeys: String, CodingKey {
        case address
        case zipCode = "zip_code"
        case city, country
    }
    
    init(
        address: String,
        zipCode: String,
        city: String,
        country: String
    ) {
        self.address = address
        self.zipCode = zipCode
        self.city = city
        self.country = country
    }

}
