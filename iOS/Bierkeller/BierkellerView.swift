//
//  BierkellerView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerView: View {
    
    var mode: ViewMode
    var id: Int?

    @State private var items = [BeerCellar]()
    private var client = WebApiClient()
    
    init(mode: ViewMode) {
        self.mode = mode
    }
    
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
                            NavigationLink(destination: BierkellerDetailView(mode: self.mode, id: item.id)) {
                                Text(item.name)
                            }
                        }
                    }
                }
                .onAppear(perform: {
                    client.getData(additiveUrl: "beercellar", ofType: [BeerCellar].self, callback: { result in
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
        BierkellerView(mode: ViewMode.CREATE)
    }
}
 
