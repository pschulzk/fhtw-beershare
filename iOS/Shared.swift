//
//  Shared.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 23.06.21.
//

import Foundation

enum OrderStatus: Int {
    case PLACED = 1
    case ACCEPTED = 2
    case DECLINED = 3
    case DONE = 4
}

enum ViewMode: String {
    case CREATE = "CREATE"
    case MODIFY = "MODIFY"
    case READONLY = "READONLY"
}

enum ActiveAlert {
    case showSuccess, showInvalid
}

func validate(_ data: [String]) -> Bool {
    var isValid: Bool = true
    data.forEach({ i in
        if i == "" {
            isValid = false
        }
    })
    return isValid
}
