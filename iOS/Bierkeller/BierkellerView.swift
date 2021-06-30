//
//  BierkellerView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerView: View {
    @EnvironmentObject var appState: AppState
    var mode: ViewMode
    var id: Int?

    @State private var items = [BeerCellar]()
    // private var client = WebApiClient()
    
    init(mode: ViewMode) {
        self.mode = mode
    }
    
    private func getData() {
        appState.client.getData(additiveUrl: "beercellar", ofType: [BeerCellar].self, callback: { result in
            self.items = result
        })
    }
    
    var body: some View {
        VStack {
            VStack {
                NavigationLink(destination: BierkellerDetailView(mode: .CREATE).environmentObject(appState)) {
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
                            NavigationLink(destination: BierkellerDetailView(mode: self.mode, id: item.id).environmentObject(appState)) {
                                Text(item.name)
                            }
                        }
                    }
                }
                .onAppear(perform: {
                    getData()
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
 
