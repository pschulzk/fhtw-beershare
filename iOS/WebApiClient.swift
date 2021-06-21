//
//  WebApiClient.swift
//  BeerShare
//
//  Created by Chris on 12.06.21.
//
import Foundation
import Bagel

enum HttpMethod: String {
    case GET = "GET"
    case POST = "POST"
    case PUT = "PUT"
}

public class WebApiClient {
    
    let BASE_URL = "http://0.0.0.0:8000/api/v1/"
    let credentials = URLCredential(user: "admin", password: "admin", persistence: .forSession)
    
    init() {
        // debug
        // Bagel.start()
    }

    func getData<T : Decodable>(additiveUrl: String, callback: @escaping (_ response: T) -> Void) {

        let urlString = self.BASE_URL + additiveUrl

        do {
            try makeRequest(urlString: urlString, method: .GET, completionHandler: { data, response, error in
                if let data = data {
                    if let decodedResponse = try? JSONDecoder().decode(T.self, from: data){
                        DispatchQueue.main.async{
                            callback(decodedResponse)
                            // everything worked
                            print("Request successful")
                        }
                        return
                    }
                }
                // if we're still here there was a problem
                print("Request failed: \(error?.localizedDescription ?? "Unknown error")")
            })
        }
        catch let error as NSError {
            print("Caught NSError: \(error.localizedDescription), \(error.domain), \(error.code)")
        }
    }
    
    private func makeRequest(urlString: String, method: HttpMethod, completionHandler: @escaping (Data?, URLResponse?, Error?) -> Void) throws {
        let loginString = String(format: "%@:%@", credentials.user!, credentials.password!)
        let loginData = loginString.data(using: String.Encoding.utf8)!
        let base64LoginString = loginData.base64EncodedString()

        guard let url: URL = URL(string: urlString) else {
            throw NSError(domain: "URL format error", code: 999, userInfo: ["Malformed URL strin parameter":urlString] )
        }
        var request = URLRequest(url: url)
        request.httpMethod = method.rawValue
        
        let sessionConfiguration = URLSessionConfiguration.default
        sessionConfiguration.httpAdditionalHeaders = [ "Authorization" : "Basic \(base64LoginString)" ]
        let session = URLSession(configuration: sessionConfiguration)
        let task = session.dataTask(with: request, completionHandler: completionHandler)
        task.resume()
    }

}
