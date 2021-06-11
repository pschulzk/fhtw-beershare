//
//  OrderManagementView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct OrderManagementView: View {

    var body: some View {
        VStack{
            VStack{
                Text("Eingehende Bestellungen")
                List{
                    NavigationLink(destination: ManagementDetailView()) {
                        HStack{
                            Text("Wieselburger")
                            Spacer()
                            Text("10")
                            Spacer()
                            Text("Akzeptiert")
                        }
                    }
                    
                    NavigationLink(destination: ManagementDetailView()) {
                        HStack{
                            Text("Wieselburger")
                            Spacer()
                            Text("10")
                            Spacer()
                            Text("Akzeptiert")
                        }
                    }
                    
                }
                .listStyle(PlainListStyle())
            }
            
            Spacer()
            
            VStack{
                Text("Ausgehende Bestellungen")
                List{
                    NavigationLink(destination: ManagementDetailView()) {
                        HStack{
                            Text("Wieselburger")
                            Spacer()
                            Text("10")
                            Spacer()
                            Text("Akzeptiert")
                        }
                    }
                    NavigationLink(destination: ManagementDetailView()) {
                        HStack{
                            Text("Wieselburger")
                            Spacer()
                            Text("10")
                            Spacer()
                            Text("Akzeptiert")
                        }
                    }
                }
                .listStyle(PlainListStyle())
            }
        }
        .navigationTitle("Meine Bestellungen")
        .navigationBarTitleDisplayMode(.inline)
        .padding()
    }
}

struct OrderManagementView_Previews: PreviewProvider {
    static var previews: some View {
        OrderManagementView()
    }
}
