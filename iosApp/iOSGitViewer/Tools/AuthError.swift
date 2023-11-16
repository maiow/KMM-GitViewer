//
//  AuthError.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 17.10.2023.
//

import Foundation

enum AuthError: Error {
    case noInternet
    case invalidToken
    case otherError(_ cause: Error)
}
