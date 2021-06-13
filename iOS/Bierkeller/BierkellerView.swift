//
//  BierkellerView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerView: View {
    
    @State private var items = [BeerCellar]()
    
    var body: some View {
        VStack{
            NavigationLink(destination: ManagementDetailView()) {
                Image("AddKeller")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .cornerRadius(5)

            }
            .padding(.horizontal, 50.0)
            .padding(.vertical, 20.0)
            List{
                ForEach(self.items) { item in
                    NavigationLink(destination: BierkellerDetailView()) {
                        Text("Bierkeller 1")
                    }
                }
            }
            .onAppear(perform: {
                WebApiClient.loadData(additiveUrl: "beercellar", callback: { result in
                    self.items = result
                })
            })
            
        }
        .navigationBarTitle("Meine Bierkeller")
        .navigationBarTitleDisplayMode(.inline)
        .listStyle(PlainListStyle())
    }
    
    func loadData(){
        
    }
}


struct BierkellerView_Previews: PreviewProvider {
    static var previews: some View {
        BierkellerView()
    }
}
 
