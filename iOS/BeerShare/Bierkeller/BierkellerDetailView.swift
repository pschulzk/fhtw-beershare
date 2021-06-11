//
//  BierkellerDetailView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerDetailView: View {
    @State var name = ""
    @State var address = ""
    var body: some View {
        NavigationView{
            VStack{
                VStack(alignment: .leading){
                    Text("Name")
                        .padding(.top, 30.0)
                        .frame(height: 30.0)
                    TextField("Name", text: $name)
                        .frame(height: 40.0)
                        .border(Color.gray)
                    Text("Adresse")
                        .padding(.top, 30.0)
                        .frame(height: 30.0)
                    TextField("Adresse", text: $address)
                        .frame(height: 40.0)
                        .border(Color.gray)
                    
                    NavigationLink(destination: ManagementDetailView()) {
                        Image("AddBeer")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 200.0, height: 40.0)
                            .cornerRadius(10)
                            
                    }
                    .padding(.top, 30.0)
                    HStack{
                        Text("Biersorten")
                        Spacer()
                        Text("Menge")
                    }
                    .padding([.top, .leading, .trailing])
                    Divider()
                    List{
                        HStack{
                            Text("Biersorte 1")
                            Spacer()
                            Text("10000")
                        }
                        HStack{
                            Text("Biersorte 2")
                            Spacer()
                            Text("10000")
                        }
                    }
                    .listStyle(PlainListStyle())
                    
                }
                Spacer()
                
            }
            .navigationBarTitle("Bierkeller Details")
            .navigationBarTitleDisplayMode(.inline)
            .padding()
        }
    }
}

struct BierkellerDetailView_Previews: PreviewProvider {
    static var previews: some View {
        BierkellerDetailView()
    }
}
