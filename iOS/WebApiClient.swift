//
//  WebApiClient.swift
//  BeerShare
//
//  Created by Chris on 12.06.21.
//

import Foundation

class WebApiClient: ObservableObject {
    

    func loadData<T>(additiveUrl: String, callback: @escaping (_ response: T) -> Void) {
        var returnList : [T]
        let baseURL = "http://0.0.0.0:8000/api/v1/"
        let urlString = baseURL + additiveUrl
            
        guard let url = URL(string: urlString) else {
            print("invalid URL")
            return
        }
        let request = URLRequest(url: url)
        URLSession.shared.dataTask(with: request){data, response, error in
            if let data = data {
                if let decodedResponse = try? JSONDecoder().decode([Beer].self, from: data){
                    DispatchQueue.main.async{
                        returnList = decodedResponse
                        callback(returnList)
                    }
                    // everything worked
                    print("GET Request successful")
                    return
                }
            }
         // if we're still here there was a problem
            print("Fetch failed: \(error?.localizedDescription ?? "Unknown error")")
        
        }.resume()
    }
    
    init(){
    }

}
