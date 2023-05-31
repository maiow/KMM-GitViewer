package com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mivanovskaya.gitviewer.shared.domain.model.Repo

class RepoDiffUtil : DiffUtil.ItemCallback<Repo>() {

    override fun areItemsTheSame(oldItem: Repo, newItem: Repo) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo) =
        oldItem == newItem
}