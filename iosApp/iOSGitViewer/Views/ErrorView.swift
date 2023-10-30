//
//  ErrorView.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 27.09.2023.
//

import Foundation
import UIKit

final class ErrorView: UIView {
    
    @IBOutlet private weak var errorDescriptionLabel: UILabel!
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        commonInit()
    }
    
    private func commonInit() {
        let viewFromXib = Bundle.main.loadNibNamed(String(describing: type(of: self)), owner: self, options: nil)!.first as! UIView
        viewFromXib.frame = self.bounds
        addSubview(viewFromXib)
    }
    
    func setErrorDescriptionText(_ text: String) {
        errorDescriptionLabel.text = text
    }
    
}
