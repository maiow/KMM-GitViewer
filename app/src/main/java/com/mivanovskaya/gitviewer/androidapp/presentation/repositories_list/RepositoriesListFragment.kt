package com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gitviewer.androidapp.R
import com.mivanovskaya.gitviewer.androidapp.databinding.FragmentRepositoriesListBinding
import com.mivanovskaya.gitviewer.androidapp.presentation.base.BaseFragment
import com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.RepositoriesListViewModel.State
import com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.adapter.RepoListAdapter
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.collectInStartedState
import com.mivanovskaya.gitviewer.shared.domain.model.Repo
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoriesListFragment : BaseFragment<FragmentRepositoriesListBinding>() {

    private val viewModel: RepositoriesListViewModel by viewModel()
    override fun initBinding(inflater: LayoutInflater) =
        FragmentRepositoriesListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RepoListAdapter { item -> onItemClick(item) }
        binding.recycler.adapter = adapter
        viewLifecycleOwner.collectInStartedState(viewModel.state) { updateUi(it, adapter) }

        setRetryButton()
        setLogoutButton()
    }

    private fun updateUi(state: State, adapter: RepoListAdapter) {
        with(binding) {
            commonProgress.progressBar.isVisible = (state == State.Loading)
            emptyRepo.isVisible = (state == State.Empty)

            commonConnectError.connectionError.isVisible =
                (state is State.Error && state.isNetworkError)

            commonOtherError.somethingError.isVisible =
                (state is State.Error && !state.isNetworkError)

            val errorText =
                if (state is State.Error && !state.isNetworkError) {
                    state.error.asString(requireContext())
                } else null
            commonOtherError.errorDescription.text = errorText

            recycler.isVisible = (state is State.Loaded)
            retryButton.isVisible =
                (state is State.Error) || (state is State.Empty)
        }
        setRetryButtonText(state)
        submitDataToAdapter(state, adapter)
    }

    private fun submitDataToAdapter(state: State, adapter: RepoListAdapter) {
        val list: List<Repo> =
            if (state is State.Loaded) state.repos
            else emptyList()

        adapter.submitList(list)
    }

    private fun setRetryButtonText(state: State) {
        val text =
            if (state is State.Empty) getString(R.string.refresh)
            else getString(R.string.retry)

        binding.retryButton.text = text
    }

    private fun onItemClick(item: Repo) {
        findNavController().navigate(
            RepositoriesListFragmentDirections
                .actionRepositoriesListFragmentToDetailInfoFragment(item.name)
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
            RepositoriesListFragmentDirections.actionRepositoriesListFragmentToAuthFragment()
        )
    }
}