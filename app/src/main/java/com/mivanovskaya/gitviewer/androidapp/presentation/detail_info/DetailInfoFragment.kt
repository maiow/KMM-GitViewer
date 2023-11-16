package com.mivanovskaya.gitviewer.androidapp.presentation.detail_info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gitviewer.androidapp.R
import com.mivanovskaya.gitviewer.androidapp.databinding.FragmentDetailInfoBinding
import com.mivanovskaya.gitviewer.androidapp.presentation.base.BaseFragment
import com.mivanovskaya.gitviewer.androidapp.presentation.detail_info.RepositoryInfoViewModel.ReadmeState
import com.mivanovskaya.gitviewer.androidapp.presentation.detail_info.RepositoryInfoViewModel.State
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.GlideImageGetter
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.collectInStartedState
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailInfoFragment : BaseFragment<FragmentDetailInfoBinding>() {

    private val args: DetailInfoFragmentArgs by navArgs()
    private val viewModel: RepositoryInfoViewModel by viewModel {
        parametersOf(
            args.repoName,
            args.ownerName
        )
    }

    override fun initBinding(inflater: LayoutInflater) = FragmentDetailInfoBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle()
        observeDetailInfoState()
        setRetryButton()
        setLogoutButton()
    }

    private fun setToolbarTitle() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = args.repoName
    }

    private fun observeDetailInfoState() {
        viewLifecycleOwner.collectInStartedState(viewModel.state, ::updateUiOnDetailInfo)
    }

    private fun updateUiOnDetailInfo(state: State) {
        with(binding) {
            commonProgress.progressBar.isVisible = (state == State.Loading)
            commonError.connectionError.isVisible =
                (state is State.Error && state.isNetworkError ||
                        state is State.Loaded && state.readmeState is ReadmeState.Error
                        && state.readmeState.isNetworkError)

            error.somethingError.isVisible =
                (state is State.Error && !state.isNetworkError ||
                        state is State.Loaded && state.readmeState is ReadmeState.Error
                        && !state.readmeState.isNetworkError)

            val errorText =
                if (state is State.Error && !state.isNetworkError)
                    state.error.asString(requireContext())
                else if (state is State.Loaded && state.readmeState is ReadmeState.Error
                    && !state.readmeState.isNetworkError
                )
                    state.readmeState.error.asString(requireContext())
                else null

            error.errorDescription.text = errorText

            retryButton.isVisible =
                commonError.connectionError.isVisible || error.somethingError.isVisible


            repoInfoGroup.isVisible = state is State.Loaded
            if (state is State.Loaded) showRepoInfo(state)

            readmeProgress.isVisible =
                state is State.Loaded && state.readmeState is ReadmeState.Loading
            readme.isVisible =
                state is State.Loaded && state.readmeState is ReadmeState.Loaded ||
                        state is State.Loaded && state.readmeState is ReadmeState.Empty

            val readmeText = if (state is State.Loaded && state.readmeState is ReadmeState.Loaded) {
                parseReadmeMarkdown(state.readmeState.markdown, readme)
            } else if (state is State.Loaded && state.readmeState is ReadmeState.Empty) {
                getString(R.string.no_readme)
            } else null

            readme.text = readmeText
        }
    }

    private fun showRepoInfo(state: State.Loaded) {
        with(binding) {
            license.text = state.githubRepo.license?.name ?: getString(R.string.no_license)
            stars.text = resources.getQuantityString(
                R.plurals.stars,
                state.githubRepo.stargazersCount,
                state.githubRepo.stargazersCount
            )
            forks.text = resources.getQuantityString(
                R.plurals.forks,
                state.githubRepo.forksCount,
                state.githubRepo.forksCount
            )
            watchers.text = resources.getQuantityString(
                R.plurals.watchers,
                state.githubRepo.watchersCount,
                state.githubRepo.watchersCount
            )
            link.text = state.githubRepo.htmlUrl
            setClickListenerForLink(state.githubRepo.htmlUrl)
        }
    }

    private fun setClickListenerForLink(url: String) {
        binding.link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun parseReadmeMarkdown(markdown: String, readmeView: TextView): Spanned {
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
        val html = HtmlGenerator(markdown, parsedTree, flavour).generateHtml()
        return HtmlCompat.fromHtml(
            html, HtmlCompat.FROM_HTML_MODE_COMPACT, GlideImageGetter(readmeView), null
        )
    }

    private fun setRetryButton() {
        binding.retryButton.setOnClickListener {
            viewModel.onRetryButtonClick()
        }
    }

    private fun setLogoutButton() {
        val button = requireActivity().findViewById<Toolbar>(R.id.toolbar).menu.getItem(0)
        button.setOnMenuItemClickListener {
            setLogoutAlertDialog()
            true
        }
    }

    private fun setLogoutAlertDialog() {
        val dialog = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MyThemeOverlay_Material_MaterialAlertDialog
        )
        dialog.setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onLogoutButtonPressed()
                navigateToAuth()
            }
            .setNegativeButton(R.string.no) { currentDialog, _ ->
                currentDialog.cancel()
            }
        dialog.create().show()
    }

    private fun navigateToAuth() {
        findNavController().navigate(
            DetailInfoFragmentDirections.actionDetailInfoFragmentToAuthFragment()
        )
    }
}

