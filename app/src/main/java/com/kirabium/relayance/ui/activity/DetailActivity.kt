package com.kirabium.relayance.ui.activity

import android.os.Bundle
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.kirabium.relayance.data.DummyData
import com.kirabium.relayance.ui.composable.DetailScreen

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CUSTOMER_ID = "customer_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val customerId = intent.getIntExtra(EXTRA_CUSTOMER_ID, -1)
        DummyData.findCustomerById(customerId)?.let { customer ->
            setContent {
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
        } ?: finish()
    }

    override fun onResume() {
        super.onResume()
        setupUI()
    }
}

