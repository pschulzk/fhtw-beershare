//
//  TestMapView.swift
//  BeerShare
//
//  Created by Chris on 14.06.21.
//

import SwiftUI
import MapKit

struct TestMapView: View {
    private var client = WebApiClient()
    @State private var locations: [BeerCellar] = []

    @State private var coordinateRegion = MKCoordinateRegion(
      center: CLLocationCoordinate2D(latitude: 19.43, longitude: -99.13),
      span: MKCoordinateSpan(latitudeDelta: 50, longitudeDelta: 50)
    )

    var body: some View {
      Map(
        coordinateRegion: $coordinateRegion,
        annotationItems: locations
      ) { location in
        MapAnnotation(
          coordinate: CLLocationCoordinate2D(
            latitude: location.latitude,
            longitude: location.longitude
          )
        ) {
          VStack {
            Text(location.name)
              .font(.caption2)
              .bold()
            Image(systemName: "mappin")
          }
        }
      }
      .onAppear(perform: client.getData(additiveUrl: "beercellar", callback: {
        result in self.locations
        self.locations = result
      }))
    }
}

struct TestMapView_Previews: PreviewProvider {
    static var previews: some View {

        TestMapView()
    }
}
