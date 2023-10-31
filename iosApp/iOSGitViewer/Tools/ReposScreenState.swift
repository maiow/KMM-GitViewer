//
//  ReposScreenState.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 27.09.2023.
//

import Foundation
import shared

enum ReposScreenState {
    case loading
    case success(repos: Array<shared.Repo>)
    case error(error: NSError)
    case noInternet
    case empty
    
    var isLoading: Bool {
        switch self {
        case .loading: true
        default: false
        }
    }
    
    var isNoInternet: Bool {
        switch self {
        case .noInternet: true
        default: false
        }
    }
    
    var isSuccess: Bool {
        switch self {
        case .success: true
        default: false
        }
    }
    
    var isError: Bool {
        switch self {
        case .error: true
        default: false
        }
    }
    
    var isEmpty: Bool {
        switch self {
        case .empty: true
        default: false
        }
    }
}
