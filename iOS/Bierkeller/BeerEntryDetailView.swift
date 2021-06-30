//
//  BeerEntryDetailView.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 23.06.21.
//

import SwiftUI

struct BeerEntryDetailView: View {
    @EnvironmentObject var appState: AppState
    var mode: ViewMode
    var beerCellarId: Int?
    @State var item: BeerCellarEntry?
    
    @State private var name: String = ""
    @State private var amount: String = "0"
    @State private var showSuccess = false
    private var isDisabled: Bool { self.mode == .READONLY }
    // private let client = WebApiClient()
    
    func updateItem() {
        if var payload = self.item {
            payload.beerCellar = self.beerCellarId
            print(String(self.amount))
            payload.amount = Int(self.amount) ?? 0
            appState.client.postData(additiveUrl: "absolutbeercellarentry/", ofType: BeerCellarEntry.self, callback: { result in
                self.item = result
                showSuccess = true
            }, payload: payload)
        }
    }
    
    func orderBeer() {
        let payload = Order(
            amount: Int(self.amount) ?? 0,
            status: OrderStatusInt.PLACED.rawValue,
            beerCellar: self.beerCellarId!,
            beer: self.item!.beer
        )
        appState.client.postData(additiveUrl: "beerorder/", ofType: Order.self, callback: { result in
            showSuccess = true
        }, payload: payload)
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Name")
                .font(.caption)
            TextField("Name", text: $name)
                .disabled(true)
                .padding(8.0)
        
            Text("Menge")
                .font(.caption)
            TextField("Menge", text: $amount)
                .padding(8.0)
                .border(Color.gray)
            
            if self.mode == .READONLY {
                Button(action: {
                    print("Button pushed!")
                    orderBeer()
                }) {
                    HStack(alignment: .top) {
                        Image(systemName: "checkmark")
                            .foregroundColor(.white)
                            .padding(8)
                        Text("Bestellen")
                            .foregroundColor(.white)
                            .padding(8)
                    }
                }
                .background(Color.orange)
                .padding(2)
            }

            Spacer()
        }
        .padding()
        .navigationBarTitle(isDisabled ? "Bier bestellen" : "Bier bearbeiten")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(trailing: !isDisabled ? Button(action: {
            print("Button pushed!")
            updateItem()
        }) {
            Image(systemName: "checkmark")
                .foregroundColor(.green)
                .padding(.trailing, 8)
            } : nil)
        .alert(isPresented: $showSuccess){
            Alert(title: Text("Erfolg"), message: Text("Änderungen erfolgreich!"))
        }
        .onAppear(perform: {
            if self.item != nil {
                self.name = self.item!.beerName ?? "Bier Name"
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
