//
//  BierkellerDetailView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI
import CoreLocation

struct BierkellerDetailView: View {
    @EnvironmentObject var appState: AppState
    @State public var mode: ViewMode
    @State private var id: Int?

    @State private var item: BeerCellar
    @State private var name: String = ""
    @State private var address = ""

    @State private var showAlert = false
    @State private var activeAlert: ActiveAlert = .showSuccess
    private var isDisabled: Bool { self.mode == .READONLY }
    // private let client = WebApiClient()
    
    init(mode: ViewMode, id: Int? = nil) {
        self.mode = mode
        self.id = id
        self.item = BeerCellar(
            name: "",
            latitude: 0.0,
            longitude: 0.0,
            address: Address(
                address: "",
                zipCode: "",
                city: "",
                country: ""
            )
        )
    }
    
    func getItem(id: Int) {
        appState.client.getData(additiveUrl: "beercellar/\(id)", ofType: BeerCellar.self, callback: { result in
            self.item = result
            self.name = result.name
            self.address = result.getAddressLabel()
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
            address: self.item.address
        )
        appState.client.postData(additiveUrl: "beercellar/", ofType: BeerCellar.self, callback: { result in
            self.id = result.id
            self.item = result
            self.name = self.item.name
            self.address = self.item.getAddressLabel()
            showAlert = true
            self.activeAlert = .showSuccess
        }, payload: payload)
    }
    
    func updateBeerCellar() {
        var payload = self.item
        payload.name = self.name
        payload.address = self.item.address
        appState.client.putData(additiveUrl: "beercellar/\(self.item.id!)", ofType: BeerCellar.self, callback: { result in
            self.item = result
            self.name = self.item.name
            self.address = self.item.getAddressLabel()
            showAlert = true
        }, payload: payload)
    }
    
    func callBack(_ addressData: Address) {
        self.item.address = addressData

        let geoCoder = CLGeocoder()
        geoCoder.geocodeAddressString(self.item.getAddressString()) { (placemarks, error) in
            guard let placemarks = placemarks else {
                // handle no location found
                print("no placemarks found!!!")
                print(error.debugDescription)
                return
            }
            guard let location = placemarks.first?.location else {
                // handle no location found
                print("no location found!!!")
                print(error.debugDescription)
                return
            }

            print(location.coordinate.latitude)
            self.item.latitude = location.coordinate.latitude
            print(location.coordinate.longitude)
            self.item.longitude = location.coordinate.longitude

            if self.item.id != nil {
                print("CALL updateBeerCellar()")
                updateBeerCellar()
            } else {
                print("CALL createBeerCellar()")
                createBeerCellar()
            }
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

                NavigationLink(destination: AddressEditView(
                                addressData: Address(address: self.item.address.address, zipCode: self.item.address.zipCode, city: self.item.address.city, country: self.item.address.city),
                                callBack: callBack
                    ).environmentObject(appState)
                ) {
                    VStack(alignment: .leading) {
                        Text("Adresse")
                            .font(.caption)
                            .foregroundColor(.black)
                        Text("\(self.address)")
                            .padding(4.0)
                            .foregroundColor(.black)
                        Text("bearbeiten")
                            .font(.caption2)
                    }
                }
                
                if self.mode != .READONLY && self.item.id != nil {
                    NavigationLink(destination: BeerEditView(mode: .CREATE, beerCellarId: self.id).environmentObject(appState)) {
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

                if let entries = self.item.entries {
                    List{
                        ForEach(entries, id: \.self) { item in
                            NavigationLink(destination: BeerEntryDetailView(mode: self.mode, beerCellarId: self.item.id, item: item)) {
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
        .navigationBarTitle(self.mode == .CREATE ? "Bierkeller erstellen" : "Bierkeller Details")
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
        BierkellerDetailView(mode: ViewMode.CREATE)
    }
}
