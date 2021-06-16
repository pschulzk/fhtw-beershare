//
//  TestView.swift
//  BeerShare
//
//  Created by Chris on 12.06.21.
//

//import SwiftUI
//
//struct TestView: View {
//    @State private var items = [Beer]()
//
//    var body: some View {
//        List(items, id: \.id) { item in
//                    VStack(alignment: .leading) {
//                        Text(item.brand)
//                        Text(item.type)
//                        Text("\(item.liter)")
//                        Text(item.country)
//                    }
//        }
//        .onAppear(perform: {
//            WebApiClient.loadData(additiveUrl: "beer", callback: { result in
//                self.items = result
//            })
//
//            /*
//                The following is the more extended, readable version of the above:
//
//                func myCallBack(mydata: [Beer]) {
//                    self.beerList = mydata
//                }
//                WebApiClient.loadData(additiveUrl: "beer", callback: myCallBack)
//            */
//        })
//    }
//
//    func loadData(){
//        guard let url = URL(string: "http://0.0.0.0:8000/api/v1/beer/") else {
//            print("invalid URL")
//            return
//        }
//        let request = URLRequest(url: url)
//        URLSession.shared.dataTask(with: request){data, response, error in
//            if let data = data {
//                if let decodedResponse = try? JSONDecoder().decode([Beer].self, from: data){
//                    DispatchQueue.main.async{
//                        self.items = decodedResponse
//
//                    }
//                    // everything worked
//                    print("GET Request successful")
//                    return
//                }
//            }
//         // if we're still here there was a problem
//            print("Fetch failed: \(error?.localizedDescription ?? "Unknown error")")
//
//        }.resume()
//    }
//}
//
//struct TestView_Previews: PreviewProvider {
//    static var previews: some View {
//        TestView()
//    }
//}
