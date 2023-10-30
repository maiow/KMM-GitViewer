//
//  ReadmeError.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 19.10.2023.
//

import Foundation

enum ReadmeError: Error {
    case noInternet
    case noReadme
    case otherError(_ cause: Error)
}
