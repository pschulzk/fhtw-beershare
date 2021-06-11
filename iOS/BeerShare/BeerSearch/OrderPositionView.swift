//
//  OrderView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI
import MapKit
struct OrderPositionView: View {
    
    @State var region = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 48.201333, longitude: 16.376922),
        span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.01))

    
     var locations = [
            Location(name: "Position I", latitude: 48.210033, longitude: 16.363449),
            Location(name: "Position II", latitude: 48.2050491798, longitude: 16.3701485194),
            Location(name: "Position III", latitude: 48.192832562, longitude: 16.368665192),
            Location(name: "Position IV", latitude: 48.202832522 , longitude: 16.3749985)
            ]
        
    var body: some View {

            VStack{
                Map(coordinateRegion: $region, interactionModes:[], showsUserLocation: true,  annotationItems: locations) { location in
                        MapPin(coordinate: location.coordinate)
                    
                        }
            }
            .navigationBarTitle(Text("Locationsuche"))
            .navigationBarTitleDisplayMode(.inline)

        }
        
}

struct OrderPositionView_Previews: PreviewProvider {
    static var previews: some View {
        OrderPositionView()
    }
}
