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
        let _credentials = URLCredential(user: self.user, password: self.password, persistence: .forSession)
        appState.credentials = _credentials
        appState.client = WebApiClient(credentials: _credentials)
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
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                    .padding(8.0)
                    .border(Color.gray)
                
                Text("Passwort")
                    .font(.caption)
                SecureField("Passwort", text: $password)
                    .padding(8.0)
                    .border(Color.gray)
                
                Button(action: {
                    print("Button pushed!")
                    login()
                }) {
                    HStack {
                        Spacer()
                        Image(systemName: "checkmark")
                            .foregroundColor(.white)
                            .padding(8)
                        Text("Login")
                            .foregroundColor(.white)
                            .padding(8)
                        Spacer()
                    }
                }
                .background(Color.orange)
                .padding(.top)
                
                Spacer()
            }
            .padding()
            .navigationBarTitle("Login")
            .navigationBarTitleDisplayMode(.inline)

        } else {

            HStack {
                VStack {
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
            .navigationBarItems(
                leading: appState.loggedIn
                    ? Text("Hello \(self.user)!")
                        .font(.caption)
                    : nil,
                trailing: appState.loggedIn ? Button(action: {
                    print("Button pushed!")
                    logout()
                }) {
                    Text("logout")
                } : nil)
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
