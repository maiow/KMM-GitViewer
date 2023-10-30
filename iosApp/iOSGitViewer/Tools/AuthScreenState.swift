//
//  AuthScreenState.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 17.10.2023.
//

import Foundation

enum AuthScreenState {
    case initial
    case loading
    case invalidToken
    case error(_ cause: AuthScreenError)
    
    var isLoading: Bool {
        switch self {
        case .loading: true
        default: false
        }
    }
    
    var isInvalidToken: Bool {
        switch self {
        case .invalidToken: true
        default: false
        }
    }
}
