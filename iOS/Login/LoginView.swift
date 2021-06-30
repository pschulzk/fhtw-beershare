//
//  LoginView.swift
//  BeerShare
//
//  Created by Philip Schulz-Klingauf on 27.06.21.
//

import SwiftUI

struct LoginResponse : Codable {
    var message: String;
}

struct LoginView: View {
    @EnvironmentObject var appState: AppState
    @State private var user: String = ""
    @State private var password: String = ""
    // private let client = WebApiClient()
    
    private func login() {
        // check if credentials are valid
        appState.credentials = URLCredential(user: self.user, password: self.password, persistence: .forSession)
        appState.client = WebApiClient(credentials: appState.credentials)
        appState.client.getData(additiveUrl: "auth", ofType: LoginResponse.self, callback: { result in
            appState.loggedIn = true
            print("!!! appState.loggedIn: " + (appState.loggedIn ? "TRUE" : "FALSE"))
        })
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text("Benutzername")
                .font(.caption)
            TextField("Benutzername", text: $user)
                .padding(8.0)
                .border(Color.gray)
            
            Text("Passwort")
                .font(.caption)
            TextField("Passwort", text: $password)
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
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
