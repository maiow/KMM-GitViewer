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
        
        self.tableView.reloadData()
        setErrorDescriptionText(with: state)
    }
    
    private func setErrorDescriptionText(with state: ReposScreenState) {
        let errorText = switch state {
        case .error(let error): error.userDescription()
        default: ""
        }
        self.errorView.setErrorDescriptionText(errorText)
    }
    
    private func fetchRepositories() {
        state = .loading
        
        appRepository.getRepositories(limit: 10, page: 1, completionHandler: { [weak self] repos, error in
            guard let self = self else { return }
            
            DispatchQueue.main.async {
                self.spinner.stopAnimating()
                
                guard let repos = repos, error == nil else {
                    guard let error = error as NSError? else {
                        //Crashlytics.shared().log("Unknown error of non-NSError type")
                        //TODO: somehow show error View with "unknown error" message
                        return
                    }
                    self.handleFailure(error)
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
        
        if error.kotlinException is NoInternetException {
            self.state = .noInternet
        } else {
            self.state = .error(error: error)
            print("Repos list error: \(error.localizedDescription)")
        }
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
        
        let vc = RepositoryDetailInfoViewController(repoName: repositories[indexPath.row].name, repoOwner: repositories[indexPath.row].owner)
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
