package com.mivanovskaya.gitviewer.shared.domain

import com.mivanovskaya.gitviewer.shared.data.dto.LicenseDto
import com.mivanovskaya.gitviewer.shared.data.dto.RepoDto
import com.mivanovskaya.gitviewer.shared.data.dto.UserInfoDto
import com.mivanovskaya.gitviewer.shared.domain.model.License
import com.mivanovskaya.gitviewer.shared.domain.model.Repo
import com.mivanovskaya.gitviewer.shared.domain.model.RepoDetails
import com.mivanovskaya.gitviewer.shared.domain.model.UserInfo

fun UserInfoDto.toUserInfo() = UserInfo(login)

fun RepoDto.toRepo() = Repo(description, id, language, name)

fun List<RepoDto>.toListRepo(): List<Repo> =
    this.map { item -> item.toRepo() }

fun RepoDto.toRepoDetails() = RepoDetails(
    description = description,
    forksCount = forksCount,
    htmlUrl = htmlUrl,
    id = id,
    language = language,
    license = license?.toLicense(),
    name = name,
    stargazersCount = stargazersCount,
    watchersCount = watchersCount,
    login = owner.login,
    defaultBranch = defaultBranch
)

fun LicenseDto.toLicense() = License(name)
