package com.kirabium.relayance.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirabium.relayance.R
import com.kirabium.relayance.databinding.ActivityMainBinding
import com.kirabium.relayance.ui.adapter.CustomerAdapter
import com.kirabium.relayance.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val addCustomerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Snackbar.make(binding.root, R.string.customer_saved_success, Snackbar.LENGTH_SHORT).show()
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupCustomerRecyclerView()
        setupFab()
        observeUiState()
    }

    private fun setupFab() {
        binding.addCustomerFab.setOnClickListener {
            val intent = Intent(this, AddCustomerActivity::class.java)
            addCustomerLauncher.launch(intent)
        }
    }

    private fun setupCustomerRecyclerView() {
        binding.customerRecyclerView.layoutManager = LinearLayoutManager(this)
        customerAdapter = CustomerAdapter { customer ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_CUSTOMER_ID, customer.id)
            }
            startActivity(intent)
        }
        binding.customerRecyclerView.adapter = customerAdapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    customerAdapter.submitList(state.customers)
                }
            }
        }
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
