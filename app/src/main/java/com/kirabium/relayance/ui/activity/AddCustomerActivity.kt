package com.kirabium.relayance.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.kirabium.relayance.R
import com.kirabium.relayance.databinding.ActivityAddCustomerBinding
import com.kirabium.relayance.ui.event.AddCustomerUiEvent
import com.kirabium.relayance.ui.viewmodel.AddCustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCustomerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CUSTOMER_ID = "customer_id"
    }

    private val viewModel: AddCustomerViewModel by viewModels()

    private lateinit var binding: ActivityAddCustomerBinding
    private var isFormInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
        observeUiState()
        observeUiEvents()
        setupMode()
        setupSaveAction()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBinding() {
        binding = ActivityAddCustomerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun setupMode() {
        val customerId = intent.getIntExtra(EXTRA_CUSTOMER_ID, -1).takeIf { it > 0 }
        viewModel.initialize(customerId = customerId)
    }

    private fun setupSaveAction() {
        binding.saveFab.setOnClickListener {
            viewModel.saveCustomer(
                nameInput = binding.nameEditText.text?.toString().orEmpty(),
                emailInput = binding.emailEditText.text?.toString().orEmpty(),
                noNameFallback = getString(R.string.no_name),
                noEmailFallback = getString(R.string.no_email),
            )
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    supportActionBar?.title = getString(state.titleResId)
                    if (!isFormInitialized && state.editedCustomerId != null) {
                        binding.nameEditText.setText(state.name)
                        binding.emailEditText.setText(state.email)
                        isFormInitialized = true
                    }
                }
            }
        }
    }

    private fun observeUiEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvents.collect { event ->
                    when (event) {
                        is AddCustomerUiEvent.ShowMessage -> {
                            Snackbar.make(
                                binding.root,
                                event.messageResId,
                                Snackbar.LENGTH_SHORT,
                            ).show()
                        }

                        AddCustomerUiEvent.CustomerSaved -> {
                            setResult(RESULT_OK, Intent())
                            finish()
                        }
                    }
                }
            }
        }
    }
}
