//
//  AddressEditView.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 25.06.21.
//

import SwiftUI

struct AddressEditView: View {
    
    @State var addressData: Address
    var callBack: ((_ addressData: Address) -> Void)? = nil
    
    @State private var address = ""
    @State private var zipCode = ""
    @State private var city = ""
    @State private var country = ""

    @State private var showAlert = false
    @State private var activeAlert: ActiveAlert = .showSuccess
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    func save() {
        self.addressData.address = self.address
        self.addressData.zipCode = self.zipCode
        self.addressData.city = self.city
        self.addressData.country = self.country
        if (callBack != nil) {
            callBack!(self.addressData)
        }
        presentationMode.wrappedValue.dismiss()
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Addresse")
                .font(.caption)
            TextField("Addresse", text: $address)
                .padding(8.0)
                .border(Color.gray)
            Text("PLZ")
                .font(.caption)
            TextField("PLZ", text: $zipCode)
                .padding(8.0)
                .border(Color.gray)
            Text("Stadt")
                .font(.caption)
            TextField("Stadt", text: $city)
                .padding(8.0)
                .border(Color.gray)
            Text("Land")
                .font(.caption)
            TextField("Land", text: $country)
                .padding(8.0)
                .border(Color.gray)
            
            Spacer()
        }
        .padding()
        .navigationBarTitle("Adresse bearbeiten")
        .navigationBarItems(trailing: Button(action: {
            print("Button pushed!")
            save()
        }) {
            Image(systemName: "checkmark")
                .foregroundColor(.green)
                .padding(.trailing, 8)
            })
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
            self.address = self.addressData.address
            self.zipCode = self.addressData.zipCode
            self.city = self.addressData.city
            self.country = self.addressData.country
        })
    }
}

struct AddressEditView_Previews: PreviewProvider {
    
    static var previews: some View {
        AddressEditView(addressData: Address(
            address: "test-address",
            zipCode: "test-zipCode",
            city: "test-city",
            country: "test-country"
        ))
    }
}
