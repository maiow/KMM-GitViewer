//
//  UINavigationControllerExtensions.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 23.10.2023.
//

import Foundation
import UIKit

extension UINavigationController {
    func styled() {
        navigationBar.isTranslucent = false
        navigationBar.titleTextAttributes = [
            NSAttributedString.Key.foregroundColor:UIColor.white,
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 17, weight: .semibold)
        ]
    }
}
