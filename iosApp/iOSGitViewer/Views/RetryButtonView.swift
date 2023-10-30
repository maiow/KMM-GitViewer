//
//  RetryButtonView.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 28.09.2023.
//

import Foundation
import UIKit

protocol RetryButtonDelegate {
   func retryButtonTapped(_ sender: UIButton)
}

final class RetryButtonView: UIView {
    
    var delegate: RetryButtonDelegate?
   
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
    
    @IBAction func retryButtonClick(_ sender: UIButton) {
        self.delegate?.retryButtonTapped(sender)
    }
    
}
