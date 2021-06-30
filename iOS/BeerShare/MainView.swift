//
//  ContentView.swift
//  BeerShare
//
//  Created by Chris on 03.06.21.
//

import SwiftUI
struct MainView: View {
    
    @StateObject var appState: AppState = AppState()
    
    @State private var user: String = ""
    @State private var password: String = ""
    
    private func login() {
        // check if credentials are valid
        appState.credentials = URLCredential(user: self.user, password: self.password, persistence: .forSession)
        appState.client = WebApiClient(credentials: appState.credentials)
        appState.client.getData(additiveUrl: "auth", ofType: LoginResponse.self, callback: { result in
            appState.loggedIn = true
        })
    }
    
    private func logout() {
        appState.loggedIn = false
    }
    
    var body: some View {
        NavigationView{
        if appState.loggedIn == false {
            VStack(alignment: .leading) {
                Text("Benutzername")
                    .font(.caption)
                TextField("Benutzername", text: $user)
                    .padding(8.0)
                    .border(Color.gray)
                
                Text("Passwort")
                    .font(.caption)
                SecureField("Passwort", text: $password)
                    .padding(8.0)
                    .border(Color.gray)
                
                HStack(alignment: .top) {
                    Button(action: {
                        print("Button pushed!")
                        login()
                    }) {
                        HStack(alignment: .top) {
                            Image(systemName: "checkmark")
                                .foregroundColor(.white)
                                .padding(8)
                            Text("Login")
                                .foregroundColor(.white)
                                .padding(8)
                        }
                    }
                    .background(Color.orange)
                    .padding(2)
                    .frame(maxWidth: .infinity)
                    .overlay(
                        RoundedRectangle(cornerRadius: 25)
                            .stroke(Color.white, lineWidth: 2)
                    )
                }
                Spacer()
            }
            .padding()
            .navigationBarTitle("Login")
            .navigationBarTitleDisplayMode(.inline)
        } else {
            HStack{
                VStack{
                    Spacer()
                    NavigationLink(destination: BeerSearchView().environmentObject(appState)){
                        Image("Biersuche")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(10)
                            .shadow(radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                        }
                    .padding(5)
                    Spacer()
                    NavigationLink(destination: OrderManagementView().environmentObject(appState)) {
                        Image("Bestellungen")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(10)
                            .shadow(radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                        }
                    .padding(5)
                    
                    Spacer()
                    NavigationLink(destination: BierkellerView(mode: .MODIFY).environmentObject(appState)) {
                        Image("Bierkeller")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .cornerRadius(10)
                            .shadow(radius: /*@START_MENU_TOKEN@*/10/*@END_MENU_TOKEN@*/)
                        }
                    .padding(5)
                    Spacer()
                }
                .padding(10)
            }
            .navigationBarTitle("Hauptmenü")
            .navigationBarItems(trailing: Button(action: {
                    print("Button pushed!")
                    logout()
                }) {
                    Text("logout")
                        .foregroundColor(.black)
                })
                .navigationBarTitleDisplayMode(.inline)
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
