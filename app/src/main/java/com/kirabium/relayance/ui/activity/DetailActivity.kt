package com.kirabium.relayance.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kirabium.relayance.ui.composable.DetailScreen
import com.kirabium.relayance.ui.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CUSTOMER_ID = "customer_id"
    }

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val customerId = intent.getIntExtra(EXTRA_CUSTOMER_ID, -1)
        viewModel.loadCustomer(customerId)
        setupUI()
    }

    private fun setupUI() {
        setContent {
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            val customer = state.value.customer
            if (state.value.shouldClose) {
                LaunchedEffect(state.value.shouldClose) {
                    viewModel.onCloseHandled()
                    this@DetailActivity.finish()
                }
            }
            if (customer != null) {
                DetailScreen(
                    customer = customer,
                    onBackClick = { onBackPressedDispatcher.onBackPressed() },
                    onEditClick = {
                        startActivity(
                            Intent(this, AddCustomerActivity::class.java).apply {
                                putExtra(AddCustomerActivity.EXTRA_CUSTOMER_ID, customer.id)
                            }
                        )
                    }
                )
            }
        }
    }
}
