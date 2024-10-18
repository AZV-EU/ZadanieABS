package dev.azv.zadanieabs.view.login

import androidx.lifecycle.Observer
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.azv.zadanieabs.data.user.SessionManager
import dev.azv.zadanieabs.data.user.User
import dev.azv.zadanieabs.databinding.FragmentLoginBinding
import dev.azv.zadanieabs.domain.model.login.LoginViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    @Inject lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isBusy.collect { busy ->
                usernameEditText.isEnabled = !busy
                passwordEditText.isEnabled = !busy
                loginButton.isEnabled = !busy
                loadingProgressBar.visibility = if (busy) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginResult.collect { message ->
                if (message != null)
                    showLoginFailed(message)
                else if (sessionManager.isLoggedIn)
                    updateUiWithUser(sessionManager.getUser()!!)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isDataValid.collect {
                usernameEditText.error = if (viewModel.usernameError.value != null) getString(viewModel.usernameError.value!!) else null
                passwordEditText.error = if (viewModel.passwordError.value != null) getString(viewModel.passwordError.value!!) else null
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                viewModel.checkData(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }

        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.tryLogin(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            viewModel.tryLogin(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun updateUiWithUser(user: User) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, user.username, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}