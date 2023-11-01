package com.mivanovskaya.gitviewer.shared.domain

import com.mivanovskaya.gitviewer.shared.data.dto.LicenseDto
import com.mivanovskaya.gitviewer.shared.data.dto.RepoDto
import com.mivanovskaya.gitviewer.shared.data.dto.UserInfoDto
import com.mivanovskaya.gitviewer.shared.domain.model.License
import com.mivanovskaya.gitviewer.shared.domain.model.Repo
import com.mivanovskaya.gitviewer.shared.domain.model.RepoDetails
import com.mivanovskaya.gitviewer.shared.domain.model.UserInfo

internal fun UserInfoDto.toUserInfo() = UserInfo(login)

internal fun RepoDto.toRepo() = Repo(
    descriptionText = description,
    id = id,
    language = language,
    name = name
)

internal fun RepoDto.toRepoDetails() = RepoDetails(
    descriptionText = description,
    forksCount = forksCount,
    htmlUrl = htmlUrl,
    id = id,
    language = language,
    license = license?.toLicense(),
    name = name,
    stargazersCount = stargazersCount,
    watchersCount = watchersCount,
    owner = owner.login,
    defaultBranch = defaultBranch
)

internal fun LicenseDto.toLicense() = License(name)
