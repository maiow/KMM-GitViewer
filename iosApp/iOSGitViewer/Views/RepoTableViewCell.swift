//
//  RepoTableViewCell.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 26.09.2023.
//

import UIKit
import shared

final class RepoTableViewCell: UITableViewCell {
    static let cellIdentifier = "cell"
    
    @IBOutlet private weak var repoNameLabel: UILabel!
    @IBOutlet private weak var languageLabel: UILabel!
    @IBOutlet private weak var repoDescriptionLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        repoNameLabel.text = nil
        languageLabel.text = nil
        repoDescriptionLabel.text = nil
    }
    
    func update(with repo: shared.Repo) {
        
        repoNameLabel.text = repo.name
        languageLabel.text = repo.language ?? ""
        setLanguageLabelColor(language: repo.language)
    }
    
    private func setLanguageLabelColor(language: String?) {
        
        let color = switch (language) {
        case "Kotlin" : UIColor(named: "AppLilac")
        case "Swift" : UIColor(named: "AppYellow")
        default : UIColor(named: "AppGreen")
        }
        
        languageLabel.textColor = color
    }
}
