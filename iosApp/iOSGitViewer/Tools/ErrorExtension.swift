//
//  ErrorExtension.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 16.11.2023.
//

import Foundation
import shared

extension NSError {
    func userDescription() -> String {
        print("Error Domain: \(self.domain)")
        print("Error Code: \(self.code)")
          if self.code == 0, let serializationError = self.kotlinException as? BadSerializationException {
            return NSLocalizedString("uncorrectServerData", comment: "")
        }
        else if self.code == 0, let serverError = self.kotlinException as? ServerConnectionException {
            return NSLocalizedString("serverConnectionError", comment: "")
        } else {
        return NSLocalizedString("unknownError", comment: "")
        }
    }
    
    //Для NSError надо сравнивать пару: domain + code - это странно, потому что code == 0 и для BadSerializationException, и для ServerConnectionException
//    @OptIn(UnsafeNumber::class)
//    val NSError.isThrowable: Boolean
//        get() = domain == "KotlinException" && code == 0.convert<NSInteger>() && userInfo.containsKey("KotlinException")
}
