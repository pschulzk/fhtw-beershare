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
    var callBack: (_ orderData: Order, _ orderType: OrderType) -> Void
    
    private var isOwn: Bool { self.orderType == OrderType.OWN }
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    func save() {
        callBack(self.order, self.orderType)
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
                    if isOwn {
                        Button(action: {
                            self.order.status = OrderStatusInt.DECLINED.rawValue
                            save()
                        }) {
                            Image(systemName: "xmark")
                                .foregroundColor(.white)
                                .padding(8)
                            Text("Abbrechen")
                                .foregroundColor(.white)
                                .padding(8)
                        }
                        .background(Color.orange)
                        .padding(.top)
                    } else {
                        Button(action: {
                            self.order.status = OrderStatusInt.ACCEPTED.rawValue
                            save()
                        }) {
                            Image(systemName: "checkmark")
                                .foregroundColor(.white)
                                .padding(8)
                            Text("Akzeptieren")
                                .foregroundColor(.white)
                                .padding(8)
                        }
                        .background(Color.orange)
                        .padding(.top)
                        
                        Button(action: {
                            self.order.status = OrderStatusInt.DECLINED.rawValue
                            save()
                        }) {
                            Image(systemName: "xmark")
                                .foregroundColor(.white)
                                .padding(8)
                            Text("Ablehnen")
                                .foregroundColor(.white)
                                .padding(8)
                        }
                        .background(Color.orange)
                        .padding(.top)
                    }
                    
                }
                .navigationBarTitle( self.isOwn ? "Ausgehende Bestellung" : "Eingehende Bestellung" )
                .navigationBarTitleDisplayMode(.inline)
                .padding()
                Spacer()
            }
        }
    }
}

// struct ManagementDetailView_Previews: PreviewProvider {
//     static var previews: some View {
//         let testCallBack: (_ orderData: Order, _ orderType: OrderType) -> Void = { d in print("TEST") }
//         ManagementDetailView(orderType: OrderType.OWN, order: Order(amount: 3, status: OrderStatusInt.PLACED.rawValue, beerCellar: 1, beer: 1), callBack: testCallBack)
//     }
// }
