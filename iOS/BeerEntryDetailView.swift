//
//  BeerEntryDetailView.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 23.06.21.
//

import SwiftUI

struct BeerEntryDetailView: View {
    var mode: ViewMode
    var beerCellarId: Int?
    @State var item: BeerCellarEntry?
    
    @State private var name: String = ""
    @State private var amount: Int = 0
    private let client = WebApiClient()
    
    let formatter: NumberFormatter = {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        return formatter
    } ()
    
    func updateItem() {
        if self.item != nil {
            self.item!.beerCellar = self.beerCellarId
            self.item!.beerName = self.name
            self.item!.amount = self.amount
            client.postData(additiveUrl: "absolutbeercellarentry/", ofType: BeerCellarEntry.self, callback: { result in
                self.item = result
            }, payload: self.item)
        }
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Name")
                .font(.caption)
            TextField("Name", text: $name)
                .padding(8.0)
                .border(Color.gray)
        
            Text("Menge")
                .font(.caption)
            TextField("Menge", value: $amount, formatter: formatter)
                .keyboardType(.numberPad)
                .padding(8.0)
                .border(Color.gray)

            Spacer()
        }
        .navigationBarTitle("Bier bearbeiten")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(trailing: Button(action: {
            print("Button pushed!")
            updateItem()
        }) {
            Image(systemName: "checkmark")
                .foregroundColor(.green)
                .padding(.trailing, 8)
        })
        .padding()
        .onAppear(perform: {
            if self.item != nil {
                self.name = self.item!.beerName
                self.amount = self.item!.amount
            }
        })
    }
}

struct BeerEntryDetailView_Previews: PreviewProvider {
    static var previews: some View {
        BeerEntryDetailView(mode: ViewMode.CREATE)
    }
}
