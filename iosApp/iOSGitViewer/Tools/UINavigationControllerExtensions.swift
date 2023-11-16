//
//  UINavigationControllerExtensions.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 23.10.2023.
//

import Foundation
import UIKit

extension UINavigationController {
    static func styled() -> UINavigationController {
            let nc = UINavigationController()
            nc.navigationBar.isTranslucent = false
            nc.navigationBar.titleTextAttributes = [
            NSAttributedString.Key.foregroundColor:UIColor.white,
            NSAttributedString.Key.font: UIFont.systemFont(ofSize: 17, weight: .semibold)
        ]
        return nc
    }
}
