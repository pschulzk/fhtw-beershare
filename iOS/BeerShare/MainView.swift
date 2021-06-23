//
//  ContentView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI
struct MainView: View {
    func loadData(){
        
    }
    
    
    var body: some View {
        NavigationView{
            HStack{
                VStack{
                    Spacer()
                    NavigationLink(destination: BeerSearchView()){//OrderPositionView()) {
                        Image("Biersuche")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(10)
                            .shadow(radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                        }
                    .padding(5)
                    Spacer()
                    NavigationLink(destination: OrderManagementView()) {
                        Image("Bestellungen")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(10)
                            .shadow(radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                        }
                    .padding(5)
                    
                    Spacer()
                    NavigationLink(destination: BierkellerView(mode: .MODIFY)) {
                        Image("Bierkeller")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(10)
                            .shadow(radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                        }
                    .padding(5)
                    Spacer()
                }
                .padding(10)
            }
            .navigationBarTitle("Hauptmenü")
            .navigationBarTitleDisplayMode(.inline)
        }
        .navigationViewStyle(StackNavigationViewStyle())
        
    }
    
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
