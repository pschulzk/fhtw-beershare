//
//  BierkellerDetailView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct BierkellerDetailView: View {
    @ObservedObject var item: BeerCellar
    @State var name = ""
    @State var address = ""
    private var client = WebApiClient()
    
    init(item: BeerCellar) {
        self.item = item
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
                List{
                    HStack{
                        Text("Biersorte 1")
                        Spacer()
                        Text("10000")
                    }
                    HStack{
                        Text("Biersorte 2")
                        Spacer()
                        Text("10000")
                    }
                }
                .listStyle(PlainListStyle())
                
            }
            Spacer()
            
        }
        .navigationBarTitle("Bierkeller Details")
        .navigationBarItems(trailing: Button(action: {
            print("Button pushed!")
        }) {
            Image(systemName: "multiply.circle.fill")
                .foregroundColor(.gray)
                .padding(.trailing, 8)
        })
        .navigationBarTitleDisplayMode(.inline)
        .padding()
        .onAppear(perform: {
            self.name = self.item.name
            self.address = "placeholder for address function"
            /*
            client.getData(additiveUrl: "beercellar/\(self.id)", ofType: BeerCellar.self, callback: { result in
                self.item = result
                self.name = self.item.name
                self.address = "placeholder for address function"
            })
            */
        })
    }

}

struct BierkellerDetailView_Previews: PreviewProvider {
    static var previews: some View {
        BierkellerDetailView(item: BeerCellar(id: 42, name: "test"))
    }
}
