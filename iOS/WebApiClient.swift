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
}

public class WebApiClient {
    
    let BASE_URL = "http://0.0.0.0:8000/api/v1/"

    func getData<T : Decodable>(additiveUrl: String, callback: @escaping (_ response: T) -> Void) {

        let urlString = self.BASE_URL + additiveUrl
            
        guard let request = makeRequest(urlString: urlString, method: .GET) else {
            print("Invalid URL")
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
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
        
        }.resume()
    }
    
    private func makeRequest(urlString: String, method: HttpMethod) -> URLRequest? {
        let username = "admin"
        let password = "admin"
        let loginString = String(format: "%@:%@", username, password)
        print("!!!! loginString: \(loginString)")
        let loginData = loginString.data(using: String.Encoding.utf8)!
        let base64LoginString = loginData.base64EncodedString()
        print("!!!! base64LoginString: \(base64LoginString)")

        guard let url = URL(string: urlString) else {
            print("invalid URL")
            return nil
        }
        var request = URLRequest(url: url)

        request.httpMethod = method.rawValue
        request.setValue("Basic \(base64LoginString)", forHTTPHeaderField: "Authorization")
        return request
    }

}
