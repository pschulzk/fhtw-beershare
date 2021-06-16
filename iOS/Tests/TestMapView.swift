//
//  TestMapView.swift
//  BeerShare
//
//  Created by Chris on 14.06.21.
//

import SwiftUI
import MapKit

struct TestMapView: View {
    @State private var centerCoordinate = CLLocationCoordinate2D()
    
    var body: some View {
        ZStack{
            MapView(centerCoordinate: $centerCoordinate)
            Circle()
                .fill(Color.blue)
            opacity(0.3)
            frame(width: 32, height: 32)
        }
    }
}

//struct TestMapView_Previews: PreviewProvider {
//    static var previews: some View {
//
//        MapView()
//    }
//}
