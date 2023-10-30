//
//  AppRepository.swift
//  iOSGitViewer
//
//  Created by mivanovskaya on 20.09.2023.
//

//import Foundation
//import Alamofire
//
//final class AppRepository {
//    
//    static let shared = AppRepository()
//    
//    private let url = "https://api.github.com/user"
//    
//    func getRepositories(limit: Int, page: Int, queue: DispatchQueue, completion: @escaping (Result<Array<Repo>, ReposError>) -> Void) {
//        guard let user = KeyValueStorage.sharedDataContainer.login,
//              let token = KeyValueStorage.sharedDataContainer.authToken else {
//            print("Error: authorized username/token not found in storage")
//            return
//        }
//        
//        let url = "https://api.github.com/users/\(user)/repos"
//        let params = [
//            "per_page" : limit,
//            "page" : page
//        ]
//        
//        AF.request(url, method: .get, parameters: params, headers: generateHeaders(token: token)).validate()
//            .responseDecodable(of: [Repo].self, queue: queue) {
//                response in
//                
//                switch response.result {
//                case .success(let repos):
//                    completion(.success(repos))
//                    
//                case .failure(let error):
//                    
//                    if (error._code) == 13 {
//                        print("Internet connection error: \(error)")
//                        completion(.failure(.noInternet))
//                        
//                    } else if (error.isResponseSerializationError == true) {
//                        ///Logging the Serialization error, passing further the custom one with description, good for user understanding
//                        print("Serialization error: \(error)")
//                        completion(.failure(
//                            .otherError(SerializationError())
//                        ))
//                        
//                    } else {
//                        ///Logging the non-serialization error, passing further the custom one with description, good for user understanding
//                        print("Error: \(error)")
//                        
//                        completion(.failure(
//                            .otherError(ServerConnectionError())
//                        ))
//                    }
//                }
//            }
//    }
//    
//    func getRepository(repoName: String, completion: @escaping (Result<Repo, ReposError>) -> Void) {
//        guard let user = KeyValueStorage.sharedDataContainer.login,
//              let token = KeyValueStorage.sharedDataContainer.authToken else {
//            print("Error: authorized username/token not found in storage")
//            return
//        }
//        let url = "https://api.github.com/repos/\(user)/\(repoName)"
//        
//        AF.request(url, method: .get, headers: generateHeaders(token: token)).validate()
//            .responseDecodable(of: Repo.self) { response in
//                
//                switch response.result {
//                case .success(let repoInfo):
//                    completion(.success(repoInfo))
//                    
//                case .failure(let error):
//                    
//                    if (error._code) == 13 {
//                        print("Internet connection error: \(error)")
//                        completion(.failure(.noInternet))
//                        
//                    } else if (error.isResponseSerializationError == true) {
//                        print("Serialization error: \(error)")
//                        completion(.failure(
//                            .otherError(SerializationError())
//                        ))
//                        
//                    } else {
//                        print("Error: \(error)")
//                        completion(.failure(
//                            .otherError(ServerConnectionError())
//                        ))
//                    }
//                }
//            }
//    }
//    
//    func getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String, queue: DispatchQueue, completion: @escaping (Result<String, ReadmeError>) -> Void) {
//        
//        let url = "https://raw.githubusercontent.com/\(ownerName)/\(repositoryName)/\(branchName)/README.md"
//        
//        AF.request(url, method: .get).validate().responseString(queue: queue) { response in
//            
//            switch response.result {
//            case .success(let readme):
//                completion(.success(readme))
//                
//            case .failure(let error):
//                
//                if (error._code) == 13 {
//                    print("Internet connection error: \(error)")
//                    completion(.failure(.noInternet))
//                    
//                } else if error.responseCode == 404 {
//                    print("No Readme file error: \(error)")
//                    completion(.failure(.noReadme))
//                    
//                } else if (error.isResponseSerializationError == true) {
//                    print("Serialization error: \(error)")
//                    completion(.failure(
//                        .otherError(SerializationError())
//                    ))
//                    
//                } else {
//                    print("Error: \(error)")
//                    completion(.failure(
//                        .otherError(ServerConnectionError())
//                    ))
//                }
//            }
//        }
//    }
//    
//    func signIn(token: String, queue: DispatchQueue, completion: @escaping (Result<UserInfo, AuthScreenError>) -> Void) {
//        
//        AF.request(url, method: .get, headers: generateHeaders(token: token)).validate()
//            .responseDecodable(of: UserInfo.self, queue: queue) {
//                response in
//                
//                switch response.result {
//                case .success(let userInfo):
//                    completion(.success(userInfo))
//                    
//                case .failure(let error):
//                    
//                    if (error._code) == 13 {
//                        print("Internet connection error: \(error)")
//                        completion(.failure(.noInternet))
//                        
//                    } else if error.responseCode == 401 {
//                        print("Invalid token error: \(error)")
//                        completion(.failure(.invalidToken))
//                        
//                    } else if (error.isResponseSerializationError == true) {
//                        print("Serialization error: \(error)")
//                        completion(.failure(.otherError(error)))
//                        
//                    } else {
//                        print("Error: \(error)")
//                        completion(.failure(.otherError(error)))
//                    }
//                }
//            }
//    }
//    
//    private func generateHeaders(token: String) -> HTTPHeaders {
//        return [
//            "Authorization" : "Bearer \(token)",
//            "X-GitHub-Api-Version" : "2022-11-28",
//            "Accept" : "application/json"
//        ]
//    }
//    
//    func getToken() -> String? {
//        return KeyValueStorage.sharedDataContainer.authToken
//    }
//    
//    func saveCredentials(login: String, token: String) {
//        KeyValueStorage.sharedDataContainer.authToken = token
//        KeyValueStorage.sharedDataContainer.login = login
//    }
//    
//    func signOut() {
//        KeyValueStorage.sharedDataContainer.authToken = nil
//        KeyValueStorage.sharedDataContainer.login = nil
//    }
//}
