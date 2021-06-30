//
//  BeerEditView.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 24.06.21.
//

import SwiftUI

struct BeerEditView: View {
    @EnvironmentObject var appState: AppState
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
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
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
            alertInvalid()
            return
        }
        
        let payload = Beer(
            brand: self.brand,
            type: self.type,
            liter: self.liter,
            country: self.country
        )

        appState.client.postData(additiveUrl: "beer/", ofType: Beer.self, callback: { result in
            self.brand = result.brand
            self.type = result.type
            self.liter = result.liter
            self.country = result.country
            
            guard let _beerCellarId: Int = self.beerCellarId else {
                print("Error in BeerEditView: beerCellar.id not provided!")
                alertInvalid()
                return
            }
            guard let _beerId: Int = result.id else {
                print("Error in BeerEditView: beer.id not provided!")
                alertInvalid()
                return
            }
            guard let _amount: Int = Int(self.amount) else {
                print("Error in BeerEditView: amount not provided!")
                alertInvalid()
                return
            }
            updateBeerCellar(beerCellar: _beerCellarId, beer: _beerId, amount: _amount)
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
        appState.client.postData(additiveUrl: "beercellarentry/", ofType: BeerCellarEntry.self, callback: { result in
            presentationMode.wrappedValue.dismiss()
        }, payload: payload)
    }
    
    func alertInvalid() {
        self.showAlert = true
        self.activeAlert = .showInvalid
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
