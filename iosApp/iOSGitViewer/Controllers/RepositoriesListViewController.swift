//
//  RepositoriesListViewController.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 20.09.2023.
//

import UIKit
import shared

final class RepositoriesListViewController: UIViewController {
    
    private let appRepository: AppRepository = AppRepositoryHelper().repository
    
    private var repositories: Array<shared.Repo> {
        get {
            switch state {
            case .success(let repos): repos
            default: []
            }
        }
    }
    
    private var state: ReposScreenState = .loading {
        didSet {
            updateUi(with: state)
        }
    }
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet private weak var spinner: UIActivityIndicatorView!
    @IBOutlet private weak var noInternetView: NoInternetView!
    @IBOutlet private weak var errorView: ErrorView!
    @IBOutlet private weak var emptyView: EmptyView!
    @IBOutlet private weak var retryButton: RetryButtonView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupNavigationBar()
        self.addLogoutButton(selector: #selector(didTapLogout))
        retryButton.delegate = self
        fetchRepositories()
        configureTable()
    }
    
    private func configureTable() {
        tableView.delegate = self
        tableView.dataSource = self
        let cell = UINib(nibName: "RepoTableViewCell", bundle: nil)
        tableView.register(cell, forCellReuseIdentifier: RepoTableViewCell.cellIdentifier)
        
    }
    
    private func setupNavigationBar() {
        self.navigationItem.title = NSLocalizedString("repositories", comment: "")
        self.navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
        
    }
    
    private func updateUi(with state: ReposScreenState) {
        tableView.isHidden = !state.isSuccess
        noInternetView.isHidden = !state.isNoInternet
        errorView.isHidden = !state.isError
        retryButton.isHidden = (state.isLoading || state.isSuccess)
        emptyView.isHidden = !state.isEmpty
        
        if state.isSuccess {
            self.tableView.reloadData()
        }
        
        setErrorDescriptionText(with: state)
    }
    
    private func setErrorDescriptionText(with state: ReposScreenState) {
        let errorText = switch state {
        case .error(let error): {
            if let serializationError = error as? SerializationError {
                return serializationError.userDescription
            } else if let otherError = error as? ServerConnectionError {
                return otherError.userDescription
            } else {
                fatalError("Unknown error type, nothing to show to the users: \(error)")
            }
        }()
        default: ""
        }
        self.errorView.setErrorDescriptionText(errorText)
    }
    
    private func fetchRepositories() {
        state = .loading
        
        appRepository.getRepositories(limit: 10, page: 1, completionHandler: { [weak self] repos, error in
            guard let self = self else { return }
            //TODO: clean up
            print("Running completion handler on \(Thread.current)")
            
            DispatchQueue.main.async {
                print("Now switched completion handler to \(Thread.current)")
                self.spinner.stopAnimating()
                
                guard let repos = repos, error == nil else {
                    self.handleFailure(error! as NSError/*as! ReposError*/)
                    return
                }
                self.handleSuccess(repos)
            }
        })
    }
    
    private func handleSuccess(_ repos: Array<shared.Repo>) {
        if repos.isEmpty == true {
            self.state = .empty
        } else {
            self.state = .success(repos: repos)
        }
    }
    
    private func handleFailure(_ error: NSError) {
//        switch error {
//        case .noInternet:
//            self.state = .noInternet
//            
//        case .otherError(let error):
//            self.state = .error(error: error)
            print("Repos list error: \(error.localizedDescription)")
//        }
    }
    
    @objc
    private func didTapLogout() {
        appRepository.logout()
        
        let vc = AuthViewController()
        self.view.window!.rootViewController = vc
    }
}

extension RepositoriesListViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return repositories.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: RepoTableViewCell.cellIdentifier, for: indexPath) as? RepoTableViewCell else { fatalError("Failed to dequeue cell for table view") }
        
        let repo = repositories[indexPath.row]
        cell.update(with: repo)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        let vc = RepositoryDetailInfoViewController(repo: repositories[indexPath.row])
        navigationController!.pushViewController(vc, animated: true)
    }
}

extension RepositoriesListViewController: RetryButtonDelegate {
    func retryButtonTapped(_ sender: UIButton) {
        spinner.startAnimating()
        state = .loading
        fetchRepositories()
    }
}
