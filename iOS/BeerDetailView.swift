//
//  BeerDetailView.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 23.06.21.
//

import SwiftUI

struct BeerDetailView: View {

    var mode: ViewMode
    var id: Int?

    @State private var item: Beer?
    @State private var name = ""
    private let client = WebApiClient()
    
    var body: some View {
        NavigationView{
            VStack{
                HStack{
                    Text("Wieselburger")
                    Spacer()
                    Text("10")
                    Spacer()
                    Text("Neu")
                }.padding(20)
                HStack{
                    Button(action: {
                        
                    }) {
                        Image("Akzeptieren")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(5)

                    }
                    
                    Button(action: {
                        
                    }) {
                        Image("Ablehnen")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(5)
                    }
                    
                }
                .navigationBarTitle("\(self.name)")
                .navigationBarTitleDisplayMode(.inline)
                .padding()
                Spacer()
            }
        }

    }
}

struct BeerDetailView_Previews: PreviewProvider {
    static var previews: some View {
        BeerDetailView(mode: ViewMode.CREATE)
    }
}
