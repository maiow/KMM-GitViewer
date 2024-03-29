//
//  DetailsScrenState.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 17.10.2023.
//

import Foundation
import shared

enum DetailScreenState {
    case loading
    case success(repo: shared.RepoDetails, readmeState: ReadmeState?)
    case error(_ cause: NSError)
    case noInternet
    
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
    
    var isSuccessReadme: Bool {
        switch self {
        case .success(_, let readmeState):
            guard let readmeState = readmeState else {
                return false
            }
            return readmeState.isSuccessReadme
        default: return false
        }
    }
    
    var isNoInternetReadme: Bool {
        switch self {
        case .success(_, let readmeState):
            guard let readmeState = readmeState else {
                return false
            }
            return readmeState.isNoInternet
        default: return false
        }
    }
    
    var isErrorReadme: Bool {
        switch self {
        case .success(_, let readmeState):
            guard let readmeState = readmeState else {
                return false
            }
            return readmeState.isError
        default: return false
        }
    }
    
    var isEmptyReadme: Bool {
        switch self {
        case .success(_, let readmeState):
            guard let readmeState = readmeState else {
                return false
            }
            return readmeState.isEmpty
        default: return false
        }
    }
}
