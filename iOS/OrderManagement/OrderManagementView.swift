//
//  OrderManagementView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct OrderManagementView: View {
    
    @State private var userName: String = AppState.credentials.user!
    @State private var allOrders = [Order]()
    @State private var myOrders = [Order]()
    @State private var otherOrders = [Order]()
    private let client = WebApiClient()
    
    func getItems() {
        client.getData(additiveUrl: "beerorder/", ofType: [Order].self, callback: { result in
            self.allOrders = result
            self.myOrders = self.allOrders.filter({ i in i.buyer == self.userName })
            self.otherOrders = self.allOrders.filter({ i in i.buyer != self.userName })
        })
    }
    
    func callBack(_ orderData: Order) {
        client.postData(additiveUrl: "beerorder/", ofType: Order.self, callback: { result in
            getItems()
        }, payload: orderData)
    }
    
    var body: some View {
        
        VStack {
            VStack{
                Text("Eingehende Bestellungen")
                List {
                    ForEach(self.myOrders, id: \.self) { item in
                        NavigationLink(destination: ManagementDetailView(orderType: OrderType.OTHER, order: item, callBack: callBack)) {
                            HStack{
                                Text(item.beerName ?? "No name")
                                Spacer()
                                Text(String(item.amount))
                                Spacer()
                                Text(OrderStatusString[item.status])
                            }
                        }
                    }
                }
                .listStyle(PlainListStyle())
            }
            .border(Color.gray)
            
            Divider()
            Spacer()
            
            VStack {
                Text("Ausgehende Bestellungen")
                List{
                    ForEach(self.otherOrders, id: \.self) { item in
                        NavigationLink(destination: ManagementDetailView(orderType: OrderType.OWN, order: item, callBack: callBack)) {
                            HStack{
                                Text(item.beerName ?? "No name")
                                Spacer()
                                Text(String(item.amount))
                                Spacer()
                                Text(OrderStatusString[item.status])
                            }
                        }
                    }
                }
                .listStyle(PlainListStyle())
            }
            .border(Color.gray)
        }
        .navigationTitle("Meine Bestellungen")
        .navigationBarTitleDisplayMode(.inline)
        .padding()
        .onAppear(perform: {
            getItems()
        })
    }
}

struct OrderManagementView_Previews: PreviewProvider {
    static var previews: some View {
        OrderManagementView()
    }
}
