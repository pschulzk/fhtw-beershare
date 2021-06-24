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
    @State private var amount: String = "0"
    @State private var showAlert = false
    private var isDisabled: Bool { self.mode != .CREATE }
    private let client = WebApiClient()
    
    func updateItem() {
        if var payload = self.item {
            payload.beerCellar = self.beerCellarId
            print(String(self.amount))
            payload.amount = Int(self.amount) ?? 0
            client.postData(additiveUrl: "absolutbeercellarentry/", ofType: BeerCellarEntry.self, callback: { result in
                self.item = result
                showAlert = true
            }, payload: payload)
        }
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Name")
                .font(.caption)
            TextField("Name", text: $name)
                .disabled(isDisabled)
                .padding(8.0)
                .border(isDisabled ? Color.white : Color.gray)
        
            Text("Menge")
                .font(.caption)
            TextField("Menge", text: $amount)
                .keyboardType(.numberPad)
                .padding(8.0)
                .border(Color.gray)

            Spacer()
        }
        .padding()
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
        .alert(isPresented: $showAlert){
            Alert(title: Text("Erfolg"), message: Text("Änderungen erfolgreich!"))
        }
        .onAppear(perform: {
            if self.item != nil {
                self.name = self.item!.beerName
                self.amount = String(self.item!.amount)
            }
        })
    }
}

struct BeerEntryDetailView_Previews: PreviewProvider {
    static var previews: some View {
        BeerEntryDetailView(mode: ViewMode.CREATE)
    }
}
