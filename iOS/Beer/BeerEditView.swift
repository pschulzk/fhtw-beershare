//
//  BeerEditView.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 24.06.21.
//

import SwiftUI

struct BeerEditView: View {
    var mode: ViewMode
    var beerCellarId: Int?

    @State private var brand: String = ""
    @State private var type: String = ""
    @State private var liter: String = ""
    @State private var country: String = ""

    @State private var amount: String = "0"

    @State private var showAlert = false
    @State private var activeAlert: ActiveAlert = .showSuccess
    private var isDisabled: Bool { self.mode != .CREATE }
    private let client = WebApiClient()
    
    /**
     Create new beer and create new beerCellarEntry with the result.
     */
    func createBeer() {
        if !validate([
            brand,
            type,
            liter,
            country
        ]) {
            self.showAlert = true
            self.activeAlert = .showInvalid
            return
        }
        
        let payload = Beer(
            brand: self.brand,
            type: self.type,
            liter: self.liter,
            country: self.country
        )

        client.postData(additiveUrl: "beer/", ofType: Beer.self, callback: { result in
            self.brand = result.brand
            self.type = result.type
            self.liter = result.liter
            self.country = result.country

            if self.beerCellarId != nil && result.id != nil {
                let _amount: Int = Int(self.amount) ?? 0
                updateBeerCellar(beerCellar: self.beerCellarId!, beer: result.id!, amount: _amount)
            } else {
                print("Error in BeerEditView: beerCellar.id or beer.id not provided!")
            }
        }, payload: payload)
    }
    
    /**
     Add new beer entry to existing beerCellar.
     */
    func updateBeerCellar(
        beerCellar: Int,
        beer: Int,
        amount: Int
    ) {
        let payload = BeerCellarEntry(
            beerCellar: beerCellar,
            beer: beer,
            amount: amount
        )
        client.postData(additiveUrl: "beercellarentry/", ofType: BeerCellarEntry.self, callback: { result in
            self.showAlert = true
            self.activeAlert = .showSuccess
        }, payload: payload)
    }
    
    var body: some View {
        VStack {
            VStack(alignment: .leading) {
                Text("brand")
                    .font(.caption)
                TextField("brand", text: $brand)
                    .disabled(isDisabled)
                    .padding(8.0)
                    .border(isDisabled ? Color.white : Color.gray)
                
                Text("type")
                    .font(.caption)
                TextField("type", text: $type)
                    .disabled(isDisabled)
                    .padding(8.0)
                    .border(isDisabled ? Color.white : Color.gray)
                
                Text("liter")
                    .font(.caption)
                TextField("liter", text: $liter)
                    .disabled(isDisabled)
                    .padding(8.0)
                    .border(isDisabled ? Color.white : Color.gray)
                
                Text("country")
                    .font(.caption)
                TextField("country", text: $country)
                    .disabled(isDisabled)
                    .padding(8.0)
                    .border(isDisabled ? Color.white : Color.gray)
            
                Text("Menge")
                    .font(.caption)
                TextField("Menge", text: $amount)
                    .padding(8.0)
                    .border(Color.gray)
            }
            Spacer()
        }
        .padding()
        .navigationBarTitle(self.mode == ViewMode.CREATE ? "Bier erstellen" : "Bier bearbeiten")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(trailing: Button(action: {
            print("Button pushed!")
            createBeer()
        }) {
            Image(systemName: "checkmark")
                .foregroundColor(.green)
                .padding(.trailing, 8)
        })
        .alert(isPresented: $showAlert){
            switch activeAlert {
                case .showInvalid:
                    return Alert(title: Text("Ungültig"), message: Text("Bitte Eingabefelder korrigieren!"))
                case .showSuccess:
                    return Alert(title: Text("Erfolg"), message: Text("Eingabe erfolgreich!"))
            }
        }
    }

}

struct BeerEditView_Previews: PreviewProvider {
    static var previews: some View {
        BeerEditView(mode: ViewMode.CREATE)
    }
}
