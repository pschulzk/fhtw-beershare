//
//  BeerCellar.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 13.06.21.
//

import Foundation

struct BeerCellar: Identifiable, Codable {
    
    var id: Int
    var name: String
    var latitude, longitude: Double
    var address: Address
    var owner: String
    var entries: [BeerCellarEntry]?
    
    /*
    enum CodingKeys: String, CodingKey {
        case id
        case name
        case latitude
        case longitude
        case address
        case owner
        case entries
    }
    */

    /*
    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(Int.self, forKey: .id)
        name = try container.decode(String.self, forKey: .name)
        latitude = try container.decode(Double.self, forKey: .latitude)
        longitude = try container.decode(Double.self, forKey: .longitude)

        let addressContainer = try container.nestedContainer(keyedBy: Address.CodingKeys.self, forKey: .address)
        address = try addressContainer.decode(Address.self, forKey: .address)
        
        let entriesContainer = try container.nestedContainer(keyedBy: BeerCellarEntry.CodingKeys.self, forKey: .entries)
        entries = try entriesContainer.decode([BeerCellarEntry].self, forKey: .entries)
    }
    */
    
    /*
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(id, forKey: .id)
        try container.encode(name, forKey: .name)
        try container.encode(latitude, forKey: .latitude)
        try container.encode(longitude, forKey: .longitude)

        var addressContainer = container.nestedContainer(keyedBy: Address.CodingKeys.self, forKey: .address)
        try addressContainer.encode(address, forKey: .address)
    }
    */

}
