//
//  RepositoryDetailInfoViewController.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 26.09.2023.
//

import UIKit
import Down
import shared

final class RepositoryDetailInfoViewController: UIViewController {
    
    private let appRepository: AppRepository = AppRepositoryHelper().repository
    
    private let repo: shared.Repo
    
    private var state: DetailScreenState = .loading {
        didSet {
            updateUi(with: state)
        }
    }
    
    init(repo: shared.Repo) {
        self.repo = repo
        super.init(nibName: "RepositoryDetailInfoViewController", bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("Unsupported")
    }
    
    @IBOutlet private weak var errorView: ErrorView!
    @IBOutlet private weak var noInternetView: NoInternetView!
    @IBOutlet private weak var retryButton: RetryButtonView!
    
    @IBOutlet weak var readmeNoInternetView: NoInternetView!
    @IBOutlet weak var readmeErrorView: ErrorView!
    
    @IBOutlet private weak var linkLabel: UILabel!
    @IBOutlet private weak var licenseLabel: UILabel!
    @IBOutlet private weak var starsLabel: UILabel!
    @IBOutlet private weak var forksLabel: UILabel!
    @IBOutlet private weak var watchersLabel: UILabel!
    @IBOutlet private weak var readmeLabel: UILabel!
    
    @IBOutlet private weak var spinner: UIActivityIndicatorView!
    @IBOutlet private weak var readmeSpinner: UIActivityIndicatorView!
    
    @IBOutlet private weak var linkIcon: UIImageView!
    @IBOutlet private weak var licenseIcon: UIImageView!
    @IBOutlet private weak var licenseTitleLabel: UILabel!
    @IBOutlet private weak var starsIcon: UIImageView!
    @IBOutlet private weak var forksIcon: UIImageView!
    @IBOutlet private weak var watchersIcon: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationItem.title = repo.name
        licenseTitleLabel.text = NSLocalizedString("license", comment: "")
        
        self.addLogoutButton(selector: #selector(didTapLogout))
        retryButton.delegate = self
        fetchRepositoryInfo()
        
        let linkTapGesture = UITapGestureRecognizer(target:self,action:#selector(self.didTapLink))
        linkLabel.addGestureRecognizer(linkTapGesture)
    }
    
    private func updateUi(with state: DetailScreenState) {
        let repoInfoOutlets = [linkLabel, licenseLabel, starsLabel, forksLabel, watchersLabel, readmeLabel, linkIcon, licenseIcon, licenseTitleLabel, starsIcon, forksIcon, watchersIcon]
        repoInfoOutlets.forEach {
            $0?.isHidden = (!state.isSuccess)
        }
        readmeLabel.isHidden = !state.isSuccessReadme && !state.isEmptyReadme
        noInternetView.isHidden = !state.isNoInternet
        errorView.isHidden = !state.isError
        
        retryButton.isHidden = !state.isNoInternet && !state.isError &&
        !(state.isSuccess && (state.isNoInternetReadme || state.isErrorReadme))
        
        readmeNoInternetView.isHidden = !(state.isSuccess && state.isNoInternetReadme)
        readmeErrorView.isHidden = !(state.isSuccess && state.isErrorReadme)
        
        setErrorDescriptionText(with: state)
        showReadmeText(with: state)
    }
    
    private func setErrorDescriptionText(with state: DetailScreenState) {
        let errorText = switch state {
        case .error(let error),
                .success(_, readmeState: .error(let error)): {
                    if let serializationError = error.kotlinException as? BadSerializationException  {
                        NSLocalizedString("uncorrectServerData", comment: "")
                    } else {
                        NSLocalizedString("serverConnectionError", comment: "")
                    }
                }()
        default: ""
        }
        self.errorView.setErrorDescriptionText(errorText)
        self.readmeErrorView.setErrorDescriptionText(errorText)
    }
    
    private func showReadmeText(with state: DetailScreenState) {
        self.readmeLabel.attributedText = switch state {
        case .success(_, let readmeState):
            switch readmeState {
                
            case .empty:
                NSAttributedString(string: NSLocalizedString("noReadme", comment: ""))
                
            case .success(let readmeString): {
                let down = Down(markdownString: readmeString)
                let attributedString = try? down.toAttributedString()
                return attributedString
            }()
            default: NSAttributedString(string: "")
            }
        default: NSAttributedString(string: "")
        }
        self.readmeLabel.textColor = .white
    }
    
    private func fetchRepositoryInfo() {
        state = .loading
        
        appRepository.getRepository(repoName: repo.name) { [weak self] repo, error in
            guard let self = self else { return }
            
            DispatchQueue.main.async {
                self.spinner.stopAnimating()
                
                guard let repoInfo = repo, error == nil else {
                    guard let error = error as NSError? else {
                        fatalError("Unknown error of non-NSError type")
                    }
                    self.handleRepoInfoFailure(error)
                    return
                }
                self.handleRepoInfoSuccess(repoInfo)
            }
        }
    }
    
    private func handleRepoInfoSuccess(_ repoInfo: shared.RepoDetails) {
        self.showRepoInfo(with: repoInfo)
        self.state = .success(repo: repoInfo, readmeState: ReadmeState.loading)
        self.readmeSpinner.startAnimating()
        getRepoReadme(repo: repoInfo, ownerName: repoInfo.owner, repositoryName: repo.name, branchName: repoInfo.defaultBranch)
    }
    
    private func handleRepoInfoFailure(_ error: NSError) {
        
        if error.kotlinException is NoInternetException {
            self.state = .noInternet
        } else {
            self.state = .error(error)
            print("Repo info error: \(error.localizedDescription)")
        }
    }
    
    private func getRepoReadme(repo: shared.RepoDetails, ownerName: String, repositoryName: String, branchName: String) {
        
//        appRepository.getRepositoryReadme(ownerName: ownerName, repositoryName: repositoryName, branchName: branchName, queue: DispatchQueue.main) { [weak self] result in
//            
//            guard let self = self else { return }
//            
//            switch result {
//            case .success(let readmeString): self.handleReadmeSuccess(repo: repo, readme: readmeString)
//            case .failure(let error): self.handleReadmeFailure(repo: repo, error: error)
//            }
//        }
    }
    
    private func handleReadmeSuccess(repo repoInfo: shared.RepoDetails, readme readmeString: String) {
        
        self.state = .success(repo: repoInfo, readmeState: ReadmeState.success(readme: readmeString))
        self.readmeSpinner.stopAnimating()
    }
    
    private func handleReadmeFailure(repo repoInfo: shared.RepoDetails, error: ReadmeError) {
        
        self.readmeSpinner.stopAnimating()
        
//        switch error {
//        case .noInternet:
//            self.state = .success(repo: repoInfo, readmeState: ReadmeState.noInternet)
//            
//        case .noReadme:
//            self.state = .success(
//                repo: repoInfo,
//                readmeState: ReadmeState.empty
//            )
//            
//        case .otherError(let error):
//            self.state = .success(
//                repo: repoInfo,
//                readmeState: ReadmeState.error(error: error)
//            )
//            print("Readme error: \(error.localizedDescription)")
//        }
    }
    
    private func showRepoInfo(with repo: shared.RepoDetails) {
        self.linkLabel.text = repo.htmlUrl
        self.licenseLabel.text = repo.license?.name ?? NSLocalizedString("noLicense", comment: "")
        
        if #available(iOS 15, *) {
            self.starsLabel.text = String(localized: "\(repo.stargazersCount) stars")
            self.forksLabel.text = String(localized: "\(repo.forksCount) forks")
            self.watchersLabel.text = String(localized: "\(repo.watchersCount) watchers")
        } else {
            self.starsLabel.text = NSLocalizedString("\(repo.stargazersCount) stars", comment: "")
            self.forksLabel.text = NSLocalizedString("\(repo.forksCount) forks", comment: "")
            self.watchersLabel.text = NSLocalizedString("\(repo.watchersCount) watchers", comment: "")
        }
    }
    
    @objc
    private func didTapLink() {
        guard let url = URL(string: linkLabel.text ?? "") else { return }
        UIApplication.shared.open(url)
    }
    
    @objc
    private func didTapLogout() {
        appRepository.logout()
        
        let vc = AuthViewController()
        self.view.window!.rootViewController = vc
    }
}

extension RepositoryDetailInfoViewController: RetryButtonDelegate {
    func retryButtonTapped(_ sender: UIButton) {
        spinner.startAnimating()
        state = .loading
        fetchRepositoryInfo()
    }
}
