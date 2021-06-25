//
//  Shared.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 23.06.21.
//

import Foundation

struct AppState {
    static var base_url = "http://0.0.0.0:8000/api/v1/"
    static var credentials = URLCredential(user: "admin", password: "admin", persistence: .forSession)
}

enum OrderType {
    case OWN
    case OTHER
}
enum OrderStatusInt: Int {
    case PLACED = 1
    case ACCEPTED = 2
    case DECLINED = 3
    case DONE = 4
}
let OrderStatusString: [String] = [
    "NO_VALUE",
    "Neu",
    "Akzeptiert",
    "Abgelehnt",
    "Erledigt"
]

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
