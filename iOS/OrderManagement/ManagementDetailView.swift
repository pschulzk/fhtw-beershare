//
//  MangementDetailView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct ManagementDetailView: View {
    @State var orderType = "Eingehende Bestellung "
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
                .navigationBarTitle("\(orderType)")
                .navigationBarTitleDisplayMode(.inline)
                .padding()
                Spacer()
            }
        }
    }
}

struct ManagementDetailView_Previews: PreviewProvider {
    static var previews: some View {
        ManagementDetailView()
    }
}
