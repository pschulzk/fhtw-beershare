//
//  BierkellerView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerView: View {
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
                NavigationLink(destination: BierkellerDetailView()) {
                    Text("Bierkeller 1")
                }
                NavigationLink(destination: BierkellerDetailView()) {
                    Text("Bierkeller 2")
                }
                NavigationLink(destination: BierkellerDetailView()) {
                    Text("Bierkeller 3")
                }
            }
//            .onAppear(loadData())
            
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
 
