//
//  SceneDelegate.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 20.09.2023.
//

import UIKit
import shared

class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?
    
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        let myWindow = UIWindow(windowScene: windowScene)
        
        let token = AppRepositoryHelper().getToken()
        let vc: UIViewController
        if (token == "") {
            vc = AuthViewController()
            myWindow.rootViewController = vc
        } else {
            let navController = UINavigationController()
            vc = RepositoriesListViewController()
            navController.viewControllers = [vc]
            navController.styled()
            myWindow.rootViewController = navController
        }
        self.window = myWindow
        myWindow.makeKeyAndVisible()
        myWindow.layer.backgroundColor = UIColor(named: "Background")?.cgColor
    }

    func sceneDidDisconnect(_ scene: UIScene) {
    }

    func sceneDidBecomeActive(_ scene: UIScene) {
    }

    func sceneWillResignActive(_ scene: UIScene) {
    }

    func sceneWillEnterForeground(_ scene: UIScene) {
    }

    func sceneDidEnterBackground(_ scene: UIScene) {
    }


}

