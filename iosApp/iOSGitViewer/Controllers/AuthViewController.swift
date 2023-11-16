//
//  AuthViewController.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 20.09.2023.
//

import UIKit
import shared

/// Controller for authentication
final class AuthViewController: UIViewController, UITextFieldDelegate {
    
    private let appRepository: AppRepository = AppRepositoryHelper().repository
    
    private var state: AuthScreenState = .initial {
        didSet {
            updateUi(with: state)
        }
    }
    
    @IBOutlet private weak var tokenTextField: UITextField!
    @IBOutlet private weak var signInButton: UIButton!
    @IBOutlet private weak var invalidTokenLabel: UILabel!
    
    @IBOutlet private weak var spinner: UIActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setTokenTextField()
        signInButton.layer.cornerRadius = 8
        self.state = .initial
        spinner.stopAnimating()
        
    }
    
    private func updateUi(with state: AuthScreenState) {
        invalidTokenLabel.isHidden = !state.isInvalidToken
        
        let color: String = switch state {
        case .initial, .loading: "DarkGrey"
        case .invalidToken: "Error"
        default: "AppBlue"
        }
        tokenTextField.layer.borderColor = UIColor(named: color)?.cgColor
        
        signInButton.isEnabled = !state.isLoading
        tokenTextField.isEnabled = !state.isLoading
    }
    
    private func setTokenTextField() {
        tokenTextField.delegate = self
        tokenTextField.layer.borderWidth = 1
        tokenTextField.layer.cornerRadius = 8
        tokenTextField.placeholder = NSLocalizedString("personalAccessToken", comment: "")
        invalidTokenLabel.text = NSLocalizedString("invalidToken", comment: "")
        
    }
    
    @IBAction private func tokenEditingChanged(_ sender: UITextField) {
        invalidTokenLabel.isHidden = true
        tokenTextField.layer.borderColor = UIColor(named: "AppBlue")?.cgColor
    }
    
    
    @IBAction private func signInButtonTapped(_ sender: UIButton) {
        guard let token = tokenTextField.text, !token.isEmpty else { return }
        
        guard isValid(token: token) else {
            self.state = .invalidToken
            return
        }
        
        appRepository.signIn(token: token) { [weak self] result, error  in
            guard let self = self else { return }
            DispatchQueue.main.async {
                self.state = .loading
                self.spinner.startAnimating()
                
                guard let userInfo = result, error == nil else {
                    guard let error = error as NSError? else {
                        fatalError("Unknown error of non-NSError type")
                    }
                    self.handleError(error)
                    return
                }
                self.handleSuccess(userInfo, token)
            }
        }
    }
    
    private func handleSuccess(_ userInfo: shared.UserInfo, _ token: String) {
        self.appRepository.saveCredentials(login: userInfo.login, token: token)
        self.navigateToRepositoriesList()
    }
    
    private func handleError(_ error: NSError) {
        self.spinner.stopAnimating()
        
        if error.kotlinException is NoInternetException {
            self.state = .error(.noInternet)
            self.showAlert(error: NSLocalizedString("checkYourInternetConnection", comment: ""))
            
        } else if error.kotlinException is BadSerializationException {
            self.state = .error(.otherError(error))
            self.showAlert(error: NSLocalizedString("uncorrectServerData", comment: ""))
            
        } else if error.kotlinException is InvalidTokenException {
            self.state = .invalidToken
            
        } else {
            self.state = .error(.otherError(error))
            print("Auth error: \(error.localizedDescription)")
            self.showAlert(error: NSLocalizedString("serverConnectionError", comment: ""))
        }
    }
    
    private func showAlert(error: String) {
        
        let alert = UIAlertController(title: NSLocalizedString("error", comment: ""), message: error, preferredStyle: .alert)
        
        let ok = UIAlertAction(title: NSLocalizedString("ok", comment: ""), style: .cancel, handler: { (action) -> Void in
            self.tokenTextField.layer.borderColor = UIColor(named: "AppBlue")?.cgColor
        })
        
        alert.addAction(ok)
        self.present(alert, animated: true, completion: nil)
    }
    
    private func isValid(token: String) -> Bool {
        if token.starts(with: "ghp_") || token.starts(with: "ghs_") {
            return token.count == 40
        }
        else if token.starts(with: "github_pat_") {
            return token.count == 93
        }
        else {
            return false
        }
    }
    
    private func navigateToRepositoriesList() {
        
        let navController = UINavigationController.styled()
        let vc = RepositoriesListViewController()
        self.view.window?.rootViewController = navController
        navController.pushViewController(vc, animated: true)
    }
}
