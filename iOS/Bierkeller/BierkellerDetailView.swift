//
//  BierkellerDetailView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerDetailView: View {

    @State var mode: ViewMode
    @State var id: Int?

    @State private var item: BeerCellar?
    @State private var name: String = ""
    @State private var address: String = ""

    @State private var showAlert = false
    @State private var activeAlert: ActiveAlert = .showSuccess
    private var isDisabled: Bool { self.mode == .READONLY }
    private let client = WebApiClient()
    
    func getItem(id: Int) {
        client.getData(additiveUrl: "beercellar/\(id)", ofType: BeerCellar.self, callback: { result in
            self.item = result
            self.name = result.name
            self.address = self.item!.getAddressString()
        })
    }
    
    func createBeerCellar() {
        if !validate([
            name
        ]) {
            self.showAlert = true
            self.activeAlert = .showInvalid
            return
        }
        let payload = BeerCellar(
            name: self.name,
            latitude: 0.0,
            longitude: 0.0,
            address: Address(
                address: "ttt",
                zipCode: "ttt",
                city: "ttt",
                country: "ttt"
            )
        )
        client.postData(additiveUrl: "beercellar/", ofType: BeerCellar.self, callback: { result in
            self.item = result
            self.id = result.id
            showAlert = true
            self.activeAlert = .showSuccess
        }, payload: payload)
    }
    
    func updateBeerCellar() {
        if var payload = self.item {
            payload.name = self.name
            client.putData(additiveUrl: "beercellar/\(self.item!.id!)", ofType: BeerCellar.self, callback: { result in
                self.item = result
                showAlert = true
            }, payload: payload)
        }
    }

    var body: some View {
        VStack {
            VStack(alignment: .leading) {
                Text("Name")
                    .font(.caption)
                TextField("Name", text: $name)
                    .disabled(isDisabled)
                    .padding(8.0)
                    .border(isDisabled ? Color.white : Color.gray)

                Text("Adresse")
                    .font(.caption)
                TextField("Adresse", text: $address)
                    .disabled(isDisabled)
                    .padding(8.0)
                    .border(isDisabled ? Color.white : Color.gray)
                
                if self.item?.id != nil {
                    NavigationLink(destination: BeerEditView(mode: .CREATE, beerCellarId: self.id)) {
                        Image("AddBeer")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 200.0, height: 40.0)
                            .cornerRadius(10)
                    }
                    .padding(.top, 30.0)
                }

                HStack{
                    Text("Biersorten")
                    Spacer()
                    Text("Menge")
                }
                .padding([.top, .leading, .trailing])
                Divider()

                if let entries = self.item?.entries {
                    List{
                        ForEach(entries, id: \.self) { item in
                            NavigationLink(destination: BeerEntryDetailView(mode: self.mode, beerCellarId: self.item!.id, item: item)) {
                                HStack{
                                    Text(item.beerName ?? "Bier Name")
                                    Spacer()
                                    Text(String(item.amount))
                                }
                            }
                        }
                    }
                    .listStyle(PlainListStyle())
                } else {
                    VStack(alignment: .center) {
                        Text("Keine Biere enthalten.")
                    }
                }

            }
            Spacer()
            
        }
        .padding()
        .navigationBarTitle("Bierkeller Details")
        .navigationBarItems(trailing: !isDisabled ? Button(action: {
            print("Button pushed!")
            if self.mode == .CREATE {
                createBeerCellar()
            } else {
                updateBeerCellar()
            }
        }) {
            Image(systemName: "checkmark")
                .foregroundColor(.green)
                .padding(.trailing, 8)
            } : nil)
        .navigationBarTitleDisplayMode(.inline)
        .alert(isPresented: $showAlert){
            switch activeAlert {
                case .showInvalid:
                    return Alert(title: Text("Ungültig"), message: Text("Bitte Eingabefelder korrigieren!"))
                case .showSuccess:
                    return Alert(title: Text("Erfolg"), message: Text("Eingabe erfolgreich!"))
            }
        }
        .onAppear(perform: {
            print("Current View is: BierkellerDetailView")
            print("MODE is: \(self.mode.rawValue)")
            if self.id != nil {
                getItem(id: self.id!)
            }
        })
    }

}

struct BierkellerDetailView_Previews: PreviewProvider {
    static var previews: some View {
        BierkellerDetailView(mode: ViewMode.CREATE, id: 3)
    }
}
