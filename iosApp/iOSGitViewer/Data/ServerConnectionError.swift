//
//  ServerConnectionError.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 25.10.2023.
//

import Foundation

struct ServerConnectionError: Error {
    let userDescription = NSLocalizedString("serverConnectionError", comment: "")
}
