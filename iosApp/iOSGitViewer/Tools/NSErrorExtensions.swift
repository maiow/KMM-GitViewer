//
//  NSErrorExtensions.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 30.10.2023.
//

import Foundation
import shared

extension NSError {
    
//    var isThrowable: Bool {
//        get { domain == "KotlinException" && code == 0 && CFDictionaryContainsKey(userInfo as CFDictionary, "KotlinException")
//        }
//    }
    func isThrowable() -> Bool {
        return self.domain == "KotlinException" && self.code == 0
    }
    
    func isNoInternet() -> Bool {
        print("True story: \(self.userInfo.values.contains { $0 is NoInternetException })")
        return self.userInfo.values.contains { $0 is NoInternetException }
    }
}
