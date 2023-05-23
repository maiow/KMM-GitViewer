package com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mivanovskaya.gitviewer.androidapp.domain.model.Repo
import com.mivanovskaya.gitviewer.androidapp.databinding.RepositoriesViewHolderBinding

class RepoListAdapter(
    private val onItemClick: (Repo) -> Unit
) : ListAdapter<Repo, RepoViewHolder>(RepoDiffUtil()) {

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item) { Repo -> onItemClick(Repo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RepoViewHolder(
        RepositoriesViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    )
}