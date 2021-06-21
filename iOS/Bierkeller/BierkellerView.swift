//
//  BierkellerView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerView: View {
    
    @State private var items = [BeerCellar]()
    private var client = WebApiClient()
    
    var body: some View {
        VStack {
            VStack {
                NavigationLink(destination: ManagementDetailView()) {
                    Image("AddKeller")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .cornerRadius(5)

                }
                .padding(.horizontal, 50.0)
                .padding(.vertical, 20.0)
                
                List {
                    ForEach(items, id: \.id) { item in
                        VStack(alignment: .leading) {
                            NavigationLink(destination: BierkellerDetailView()) {
                                Text(item.name)
                            }
                        }
                    }
                }
                .onAppear(perform: {
                    client.getData(additiveUrl: "beercellar", callback: { result in
                        self.items = result
                    })
                })
                

            }
            .navigationBarTitle("Meine Bierkeller")
            .navigationBarTitleDisplayMode(.inline)
            .listStyle(PlainListStyle())
        }
    }
    
}


struct BierkellerView_Previews: PreviewProvider {
    static var previews: some View {
        BierkellerView()
    }
}
 
