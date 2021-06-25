//
//  MangementDetailView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI

struct ManagementDetailView: View {

    @State var orderType: OrderType
    @State var order: Order
    var callBack: (_ orderData: Order) -> Void
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    func save() {
        callBack(self.order)
        presentationMode.wrappedValue.dismiss()
    }

    var body: some View {
        NavigationView{
            VStack{
                HStack{
                    Text(self.order.beerName ?? "No name")
                    Spacer()
                    Text(String(self.order.amount))
                    Spacer()
                    Text(OrderStatusString[self.order.status])
                }.padding(20)
                HStack{
                    Button(action: {
                        self.order.status = OrderStatusInt.ACCEPTED.rawValue
                        save()
                    }) {
                        Image("Akzeptieren")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(5)

                    }
                    
                    Button(action: {
                        self.order.status = OrderStatusInt.DECLINED.rawValue
                        save()
                    }) {
                        Image("Ablehnen")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(5)
                    }
                    
                }
                .navigationBarTitle( orderType == OrderType.OWN ? "Ausgehende Bestellung" : "Eingehende Bestellung" )
                .navigationBarTitleDisplayMode(.inline)
                .padding()
                Spacer()
            }
        }
    }
}

struct ManagementDetailView_Previews: PreviewProvider {
    static var previews: some View {
        let testCallBack: (_ orderData: Order) -> Void = { d in print("TEST") }
        ManagementDetailView(orderType: OrderType.OWN, order: Order(amount: 3, status: OrderStatusInt.PLACED.rawValue, beerCellar: 1, beer: 1), callBack: testCallBack)
    }
}
