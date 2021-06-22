//
//  WebApiClient.swift
//  BeerShare
//
//  Created by Chris on 12.06.21.
//
import Foundation

enum HttpMethod: String {
    case GET = "GET"
    case POST = "POST"
    case PUT = "PUT"
    case DELETE = "DELETE"
}

public class WebApiClient {
    
    let BASE_URL = "http://0.0.0.0:8000/api/v1/"
    let credentials = URLCredential(user: "admin", password: "admin", persistence: .forSession)

    /**
     * Generic GET method.
     */
    func getData<T : Decodable>(additiveUrl: String, ofType: T.Type, callback: @escaping (_ response: T) -> Void) {
        let urlString = self.BASE_URL + additiveUrl

        do {
            try makeRequest(urlString: urlString, method: .GET, completionHandler: { data, response, error in
                if let data = data {
                    if let decodedResponse: T = try? JSONDecoder().decode(T.self, from: data){
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
            print("Caught GET request error to \(urlString): \(error.localizedDescription), \(error.domain), \(error.code)")
        }
    }
    
    /**
     * Generic POST method.
     */
    func postData<T : Decodable>(additiveUrl: String, ofType: T.Type, callback: @escaping (_ response: T) -> Void, payload: T? = nil) {
        let urlString = self.BASE_URL + additiveUrl

        do {
            try makeRequest(urlString: urlString, method: .POST, completionHandler: { data, response, error in
                if let data = data {
                    if let decodedResponse: T = try? JSONDecoder().decode(T.self, from: data){
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
            }, payload: payload)
        }
        catch let error as NSError {
            print("Caught POST request error to \(urlString): \(error.localizedDescription), \(error.domain), \(error.code)")
        }
    }
    
    /**
     * Generic request method.
     */
    private func makeRequest(urlString: String, method: HttpMethod, completionHandler: @escaping (Data?, URLResponse?, Error?) -> Void) throws {
        guard let url: URL = URL(string: urlString) else {
            throw NSError(domain: "URL format error", code: 999, userInfo: ["Malformed URL strin parameter":urlString] )
        }
        var request = URLRequest(url: url)
        request.httpMethod = method.rawValue
        
        let task = makeSession().dataTask(with: request, completionHandler: completionHandler)
        task.resume()
    }
    
    /**
     * Generic request method.
     */
    private func makeRequest<T : Decodable>(urlString: String, method: HttpMethod, completionHandler: @escaping (Data?, URLResponse?, Error?) -> Void, payload: T? = nil) throws {
        guard let url: URL = URL(string: urlString) else {
            throw NSError(domain: "URL format error", code: 999, userInfo: ["Malformed URL strin parameter":urlString] )
        }
        var request = URLRequest(url: url)
        request.httpMethod = method.rawValue
        
        if payload != nil {
            if ((try? JSONSerialization.data(withJSONObject: payload!)) != nil) {
                let task = makeSession().dataTask(with: request, completionHandler: completionHandler)
                task.resume()
            }
        } else {
            let task = makeSession().dataTask(with: request, completionHandler: completionHandler)
            task.resume()
        }
        
    }
    
    /**
     * Initialize authorized session for user with credentials.
     */
    private func makeSession() -> URLSession {
        let loginString = String(format: "%@:%@", credentials.user!, credentials.password!)
        let loginData = loginString.data(using: String.Encoding.utf8)!
        let base64LoginString = loginData.base64EncodedString()
        let sessionConfiguration = URLSessionConfiguration.default
        sessionConfiguration.httpAdditionalHeaders = [ "Authorization" : "Basic \(base64LoginString)" ]
        let session = URLSession(configuration: sessionConfiguration)
        return session
    }

}
