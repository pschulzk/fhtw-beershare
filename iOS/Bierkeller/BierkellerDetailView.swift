//
//  BierkellerDetailView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerDetailView: View {

    var mode: ViewMode
    var id: Int?

    @State private var item: BeerCellar?
    @State private var name: String = ""
    @State private var address: String = "NOT IMPLEMENTED"
    @State private var showAlert = false
    private let client = WebApiClient()
    
    func getItem(id: Int) {
        client.getData(additiveUrl: "beercellar/\(id)", ofType: BeerCellar.self, callback: { result in
            self.item = result
            self.name = result.name
        })
    }
    
    func updateItem() {
        if self.item != nil {
            self.item?.name = self.name
            client.putData(additiveUrl: "beercellar/\(self.item!.id)", ofType: BeerCellar.self, callback: { result in
                self.item = result
                showAlert = true
            }, payload: self.item)
        }
    }

    var body: some View {
        VStack {
            VStack(alignment: .leading) {
                Text("Name")
                    .font(.caption)
                TextField("Name", text: $name)
                    .padding(8.0)
                    .border(Color.gray)
                Text("Adresse")
                    .font(.caption)
                TextField("Adresse", text: $address)
                    .padding(8.0)
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

                if let entries = self.item?.entries {
                    List{
                        ForEach(entries, id: \.self) { item in
                            NavigationLink(destination: BeerEntryDetailView(mode: self.mode, beerCellarId: self.item!.id, item: item)) {
                                HStack{
                                    Text(item.beerName)
                                    Spacer()
                                    Text(String(item.amount))
                                }
                            }
                        }
                    }
                    .listStyle(PlainListStyle())
                } else {
                    VStack{
                        Text("Keine Biere enthalten.")
                        Spacer()
                    }
                }

            }
            Spacer()
            
        }
        .padding()
        .navigationBarTitle("Bierkeller Details")
        .navigationBarItems(trailing: Button(action: {
            print("Button pushed!")
            updateItem()
        }) {
            Image(systemName: "checkmark")
                .foregroundColor(.green)
                .padding(.trailing, 8)
        })
        .navigationBarTitleDisplayMode(.inline)
        .alert(isPresented: $showAlert){
            Alert(title: Text("Erfolg"), message: Text("Änderungen erfolgreich!"))
        }
        .onAppear(perform: {
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
