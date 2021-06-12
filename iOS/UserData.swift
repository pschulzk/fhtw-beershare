//
//  UserData.swift
//  BeerShare
//
//  Created by Chris on 11.06.21.
//

import Foundation

class UserData {
    func connect(urlString: String){
        guard let url = URL(string: urlString) else {
            print("Cannot convert URL")
        }
        let task = URLSession.shared.dataTask(with: url){
            data, response, error in
            guard error == nil else {
                print("Cannot open URL \(url). System error: \(error!).")
                return
            }
            guard let responseData = data else {
                print("Data is nil")
                return
            }
            guard let responseString = String(data: responseData, encoding: String.Encoding.utf8)
            else {
                print("Could not convert data to string.")
                return
            }
            print(responseString)
            DispatchQueue.main.async {
                //ToDo Update SwiftUI View
            }
        }
        task.resume()
    }

}

