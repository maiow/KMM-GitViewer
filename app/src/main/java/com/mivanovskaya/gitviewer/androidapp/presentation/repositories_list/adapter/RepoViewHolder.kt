package com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mivanovskaya.gitviewer.androidapp.R
import com.mivanovskaya.gitviewer.androidapp.domain.model.Repo
import com.mivanovskaya.gitviewer.androidapp.databinding.RepositoriesViewHolderBinding

class RepoViewHolder(private val binding: RepositoriesViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Repo, onClick: (item: Repo) -> Unit) {

        with(binding) {
            repoCard.setOnClickListener { onClick(item) }

            title.text = item.name
            description.text = item.description
            language.text = item.language
            when (item.language) {
                JAVA -> language.setTextColor(
                    ContextCompat.getColor(repoCard.context, R.color.yellow)
                )

                KOTLIN -> language.setTextColor(
                    ContextCompat.getColor(repoCard.context, R.color.lilac)
                )

                PYTHON -> language.setTextColor(
                    ContextCompat.getColor(repoCard.context, R.color.accent)
                )

                JAVA_SCRIPT -> language.setTextColor(
                    ContextCompat.getColor(repoCard.context, R.color.secondary)
                )

                else -> language.setTextColor(
                    ContextCompat.getColor(repoCard.context, R.color.white_50_opacity)
                )
            }
        }
    }

    companion object {
        const val JAVA = "Java"
        const val KOTLIN = "Kotlin"
        const val PYTHON = "Python"
        const val JAVA_SCRIPT = "JavaScript"
    }
}

