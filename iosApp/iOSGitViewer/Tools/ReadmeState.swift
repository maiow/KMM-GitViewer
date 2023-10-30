//
//  ReadmeState.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 24.10.2023.
//

import Foundation

enum ReadmeState {
    case loading
    case success(readme: String)
    case error(error: Error)
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
    
    var isSuccessReadme: Bool {
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

