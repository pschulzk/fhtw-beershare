//
//  TestMapView.swift
//  BeerShare
//
//  Created by Chris on 14.06.21.
//

import SwiftUI
import MapKit

struct BeerSearchView: View {
    @EnvironmentObject var appState: AppState
    // var client = WebApiClient()
    @State private var locations: [BeerCellar] = []
    @State private var coordinateRegion = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 48.20849, longitude: 16.37298),
        span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
    )

    var body: some View {
      Map(
        coordinateRegion: $coordinateRegion,
        annotationItems: locations
      ) { location in
        MapAnnotation(
          coordinate: CLLocationCoordinate2D(
            latitude: location.latitude,
            longitude: location.longitude)
        ) {
            NavigationLink(destination: BierkellerDetailView(mode: ViewMode.READONLY, id: location.id).environmentObject(appState)){
                VStack{
                    Text(location.name)
                      .font(.caption2)
                      .bold()
                    Image(systemName: "mappin")
                        .foregroundColor(.red)
                        .shadow(radius: 5)
                }
            }
                
        }
      }
      .onAppear(perform: {
        appState.client.getData(additiveUrl: "nearbybeercellar?latitude=\(coordinateRegion.center.latitude)&longitude=\(coordinateRegion.center.longitude)&distance=10", ofType: [BeerCellar].self, callback: {
            result in
            self.locations = result
            
            for element in self.locations{
                print("\(element.address)\n")
            }
        })
      })
    }
}

struct BeerSearchView_Previews: PreviewProvider {
    static var previews: some View {
        BeerSearchView()
    }
}
