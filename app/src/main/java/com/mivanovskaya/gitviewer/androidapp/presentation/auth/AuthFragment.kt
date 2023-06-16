package com.mivanovskaya.gitviewer.androidapp.presentation.auth

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mivanovskaya.gitviewer.androidapp.R
import com.mivanovskaya.gitviewer.androidapp.databinding.FragmentAuthBinding
import com.mivanovskaya.gitviewer.androidapp.presentation.base.BaseFragment
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.colorStateList
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.hideKeyboard
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.setAppColor
import com.mivanovskaya.gitviewer.androidapp.presentation.tools.showAlertDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    private val viewModel by viewModel<AuthViewModel>()

    override fun initBinding(inflater: LayoutInflater) = FragmentAuthBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeActions()
        observeState()
        setListeners()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUiOnState(state)
                }
            }
        }
    }

    private fun observeActions() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect {
                    handleAction(it)
                }
            }
        }
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

    private fun routeToMain() = findNavController().navigate(
        AuthFragmentDirections.actionAuthFragmentToRepositoriesListFragment()
    )

    private fun updateUiOnState(state: AuthViewModel.State) {
        with(binding) {
            progressBar.isVisible = state == AuthViewModel.State.Loading
            setInvalidTokenErrorSign(state)
            setSignButtonTextColor(state)
            setEditTextHintColor(state)
            editTextHint.isVisible = state != AuthViewModel.State.Idle || editText.isFocused
            setEditTextUnderlineColor(state)
        }
    }

    private fun setListeners() {
        binding.signButton.setOnClickListener {
            if (binding.editText.text != null)
                viewModel.onSignButtonPressed(binding.editText.text.toString())
        }

        binding.editText.setOnClickListener {
            updateUiOnEditTextClickOrFocus()
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateUiOnEditTextClickOrFocus()
            }
        }
    }

    private fun setInvalidTokenErrorSign(state: AuthViewModel.State) {
        binding.invalidTokenError.isVisible = state is AuthViewModel.State.InvalidInput
        binding.invalidTokenError.text =
            if (state is AuthViewModel.State.InvalidInput)
                state.reason.asString(requireContext())
            else getString(R.string.error)
    }

    private fun updateUiOnEditTextClickOrFocus() {
        with(binding) {
            editTextHint.isVisible = true
            editTextHint.setTextColor(requireContext().setAppColor(R.color.secondary))
            editText.backgroundTintList = ColorStateList.valueOf(
                requireContext().setAppColor(R.color.secondary)
            )
            invalidTokenError.isVisible = false
        }
    }

    private fun setSignButtonTextColor(state: AuthViewModel.State) {
        val color = if (state == AuthViewModel.State.Loading) R.color.accent else R.color.white
        binding.signButton.setTextColor(requireContext().setAppColor(color))
    }

    private fun setEditTextHintColor(state: AuthViewModel.State) {
        val color = when (state) {
            is AuthViewModel.State.InvalidInput -> R.color.error
            else -> R.color.white_50_opacity
        }
        binding.editTextHint.setTextColor(requireContext().setAppColor(color))

    }

    private fun setEditTextUnderlineColor(state: AuthViewModel.State) {
        binding.editText.backgroundTintList =
            when (state) {
                is AuthViewModel.State.InvalidInput -> requireContext().colorStateList(
                    R.color.error
                )

                else -> requireContext().colorStateList(R.color.grey)
            }
    }
}