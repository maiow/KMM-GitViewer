//
//  AuthScreenError.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 17.10.2023.
//

import Foundation

enum AuthScreenError: Error {
    case noInternet
    case invalidToken
    case otherError(_ cause: Error)
}
