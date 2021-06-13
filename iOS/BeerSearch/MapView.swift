//
//  MapView.swift
//  BeerShare
//
//  Created by Chris on 13.06.21.
//

import SwiftUI
import MapKit

struct MapView: UIViewRepresentable {
    func makeUIView(context: Context) -> MKMapView {
        let mapView = MKMapView()
        return mapView
    }

    func updateUIView(_ view: MKMapView, context: Context) {
    }
}
