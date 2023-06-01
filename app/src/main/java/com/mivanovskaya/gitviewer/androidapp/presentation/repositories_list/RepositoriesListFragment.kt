package com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mivanovskaya.gitviewer.androidapp.R
import com.mivanovskaya.gitviewer.androidapp.databinding.FragmentRepositoriesListBinding
import com.mivanovskaya.gitviewer.androidapp.presentation.base.BaseFragment
import com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.RepositoriesListViewModel.Companion.NO_INTERNET
import com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.RepositoriesListViewModel.State
import com.mivanovskaya.gitviewer.androidapp.presentation.repositories_list.adapter.RepoListAdapter
import com.mivanovskaya.gitviewer.shared.domain.model.Repo
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoriesListFragment : BaseFragment<FragmentRepositoriesListBinding>() {

    private val viewModel by viewModel<RepositoriesListViewModel>()
    private val adapter by lazy {
        RepoListAdapter { item -> onItemClick(item) }
    }

    override fun initBinding(inflater: LayoutInflater) =
        FragmentRepositoriesListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        observeState()
        setRetryButton()
        setLogoutButton()
    }

    private fun setAdapter() {
        binding.recycler.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun updateUi(state: State) {
        with(binding) {
            commonProgress.progressBar.isVisible = (state == State.Loading)
            emptyRepo.isVisible = (state == State.Empty)

            commonConnectError.connectionError.isVisible =
                ((state is State.Error) && (state.error == NO_INTERNET))

            if ((state is State.Error) && (state.error != NO_INTERNET)) {
                commonOtherError.somethingError.isVisible = true
                commonOtherError.errorDescription.text =
                    getString(R.string.error_with_description, state.error)
            }

            recycler.isVisible = (state is State.Loaded)
            retryButton.isVisible = (state is State.Error) || (state is State.Empty)
        }
        setRetryButtonText(state)
        submitDataToAdapter(state)
    }

    private fun submitDataToAdapter(state: State) {
        if (state is State.Loaded)
            adapter.submitList(state.repos)
        else adapter.submitList(emptyList())
    }

    private fun setRetryButtonText(state: State) {
        binding.retryButton.text =
            if (state is State.Empty)
                getString(R.string.refresh)
            else getString(R.string.retry)
    }

    private fun onItemClick(item: Repo) {
        findNavController().navigate(
            RepositoriesListFragmentDirections.actionRepositoriesListFragmentToDetailInfoFragment(
                item.name
            )
        )
    }

    private fun setRetryButton() {
        binding.retryButton.setOnClickListener {
            viewModel.onRetryButtonClick()
        }
    }

    private fun setLogoutButton() {
        val button = binding.repositoriesBar.menu.getItem(0)
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
            .setNegativeButton(R.string.no) { _, _ ->
                dialog.create().hide()
            }
        dialog.create().show()
    }

    private fun navigateToAuth() {
        findNavController().navigate(
            RepositoriesListFragmentDirections.actionRepositoriesListFragmentToAuthFragment()
        )
    }
}
