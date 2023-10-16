package com.mivanovskaya.gitviewer.androidapp.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.mivanovskaya.gitviewer.androidapp.R
import com.mivanovskaya.gitviewer.androidapp.databinding.FragmentAuthBinding
import com.mivanovskaya.gitviewer.androidapp.presentation.base.BaseFragment
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.collectInStartedState
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.getAppColor
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.hideKeyboard
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.showAlertDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    private val viewModel: AuthViewModel by viewModel()

    override fun initBinding(inflater: LayoutInflater) = FragmentAuthBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeActions()
        observeState()
        setListeners()
    }

    private fun observeState() {
        viewLifecycleOwner.collectInStartedState(viewModel.state, ::updateUiOnState)
    }

    private fun observeActions() {
        viewLifecycleOwner.collectInStartedState(viewModel.actions, ::handleAction)
    }

    private fun handleAction(action: AuthViewModel.Action) {
        when (action) {
            AuthViewModel.Action.RouteToMain -> routeToMain()

            is AuthViewModel.Action.ShowError -> {
                requireContext().hideKeyboard(requireView())
                showAlertDialog(action.message, requireContext())
            }
        }
    }

    private fun routeToMain(): Unit = findNavController().navigate(
        AuthFragmentDirections.actionAuthFragmentToRepositoriesListFragment()
    )

    private fun updateUiOnState(state: AuthViewModel.State) {
        binding.progressBar.isVisible = state == AuthViewModel.State.Loading
        setInvalidTokenErrorSign(state)
        setSignButtonTextColor(state)
    }

    private fun setListeners() {
        with(binding) {
            signButton.setOnClickListener {
                if (editText.text != null) viewModel.onSignButtonPressed(binding.editText.text.toString())
            }

            editText.setOnClickListener {
                inputLayout.error = null
            }

            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    inputLayout.error = null
                }
            }
        }
    }

    private fun setInvalidTokenErrorSign(state: AuthViewModel.State) {
        val errorText =
            if (state is AuthViewModel.State.InvalidInput) state.reason.asString(requireContext())
            else null

        binding.inputLayout.error = errorText
    }

    private fun setSignButtonTextColor(state: AuthViewModel.State) {
        val color: Int =
            if (state == AuthViewModel.State.Loading) getAppColor(requireContext(), R.color.accent)
            else getAppColor(requireContext(), R.color.white)

        binding.signButton.setTextColor(color)
    }
}