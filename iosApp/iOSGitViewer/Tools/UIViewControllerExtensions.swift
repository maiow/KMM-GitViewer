//
//  UIViewControllerExtensions.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 23.10.2023.
//

import Foundation
import UIKit

extension UIViewController {
    func addLogoutButton(selector: Selector) {
        navigationItem.rightBarButtonItem = UIBarButtonItem(
            image: UIImage(named: "logout"),
            style: .plain,
            target: self,
            action: selector
        )
        navigationItem.rightBarButtonItem?.tintColor = .white
    }
}
