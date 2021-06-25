//
//  OrderView.swift
//  BeerShare
//
//  Created by Chris on 07.06.21.
//

import SwiftUI

struct OrderView: View {
    
    @State var inputMenge = ""
    var body: some View {
        VStack(alignment: .leading){
            Text("Biersorte")
            Text("Menge")
            TextField("Menge", text: $inputMenge)
                .frame(height: 30.0)
                .border(Color.gray)
            Button(action: {}) {
                Image("Akzeptieren")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 150.0)
                    .cornerRadius(5)
            }
            Spacer()
            
        }
        .navigationBarTitle("Meine Bestellung")
        .frame(height: 30.0)
        .navigationBarTitleDisplayMode(.inline)
        .padding()
    }
}

struct OrderView_Previews: PreviewProvider {
    static var previews: some View {
        OrderView()
    }
}
