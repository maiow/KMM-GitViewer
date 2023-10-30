//
//  ReposError.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 19.10.2023.
//

import Foundation

enum ReposError: Error {
    case noInternet
    case otherError(_ cause: Error)
}
