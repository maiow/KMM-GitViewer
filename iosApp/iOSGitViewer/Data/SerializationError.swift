//
//  SerializationError.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 25.10.2023.
//

import Foundation

struct SerializationError: Error {
    let userDescription = NSLocalizedString("uncorrectServerData", comment: "")
}
