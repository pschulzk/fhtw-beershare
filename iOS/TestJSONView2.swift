//
//  TestJSONView2.swift
//  BeerShare
//
//  Created by Chris on 13.06.21.
//

import SwiftUI

struct TestJSONView2: View {
    @State private var beerList = [Beer]()
    
    var body: some View {
        List(beerList, id: \.id) { item in
                    VStack(alignment: .leading) {
                        Text(item.brand)
                        Text(item.type)
                        Text("\(item.liter)")
                        Text(item.country)
                    }
        }
        .onAppear(perform: loadData)
    }
    
    func loadData(){
        guard let url = URL(string: "http://0.0.0.0:8000/api/v1/beer/") else {
            print("invalid URL")
            return
        }
        let request = URLRequest(url: url)
        let dataTask = URLSession.shared.dataTask(with: request){data, response, error in
            
            if let error = error {
                       print("Request error: ", error)
                       return
                   }

                   guard let response = response as? HTTPURLResponse else { return }

                   if response.statusCode == 200 {
                       guard let data = data else { return }
                       DispatchQueue.main.async {
                           do {
                               let decodedJSON = try JSONDecoder().decode([Beer].self, from: data)
                            self.beerList = decodedJSON
                           } catch let error {
                               print("Error decoding: ", error)
                           }
                       }
                   }
               }
//            if let data = data {
//                if let decodedResponse = try? JSONDecoder().decode([Beer].self, from: data){
//                    DispatchQueue.main.async{
//                        print(decodedResponse.count)
//                        self.beerList = decodedResponse
//                    }
//                    // everything worked
//                    return
//                }
//            }
         // if we're still here there was a problem
//            print("Fetch failed: \(error?.localizedDescription ?? "Unknown error")")
        dataTask.resume()
//        }
    
    }
}

struct TestJSONView2_Previews: PreviewProvider {
    static var previews: some View {
        TestJSONView2()
    }
}
