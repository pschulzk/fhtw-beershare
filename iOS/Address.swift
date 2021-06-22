//
//  Address.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 13.06.21.
//

import Foundation

// MARK: - Address
struct Address: Identifiable, Codable {
    let id = UUID()
    var address, zipCode, city, country: String

    enum CodingKeys: String, CodingKey {
        case address
        case zipCode = "zip_code"
        case city, country
    }

}
