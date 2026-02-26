package com.kirabium.relayance.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kirabium.relayance.R
import com.kirabium.relayance.data.DummyData
import com.kirabium.relayance.databinding.ActivityAddCustomerBinding

class AddCustomerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CUSTOMER_ID = "customer_id"
    }

    private lateinit var binding: ActivityAddCustomerBinding
    private var editedCustomerId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupToolbar()
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
        editedCustomerId = intent.getIntExtra(EXTRA_CUSTOMER_ID, -1)
        val customer = DummyData.findCustomerById(editedCustomerId)
        if (customer != null) {
            binding.nameEditText.setText(customer.name)
            binding.emailEditText.setText(customer.email)
            supportActionBar?.title = getString(R.string.edit_customer)
        } else {
            supportActionBar?.title = getString(R.string.add_customer)
        }
    }

    private fun setupSaveAction() {
        binding.saveFab.setOnClickListener {
            val name = binding.nameEditText.text?.toString()?.trim().orEmpty().ifBlank {
                getString(R.string.no_name)
            }
            val email = binding.emailEditText.text?.toString()?.trim().orEmpty().ifBlank {
                getString(R.string.no_email)
            }

            val updated = if (DummyData.findCustomerById(editedCustomerId) != null) {
                DummyData.updateCustomer(editedCustomerId, name, email)
            } else {
                DummyData.addCustomer(name, email)
            }

            if (updated == null) {
                Toast.makeText(this, R.string.customer_save_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            setResult(RESULT_OK, Intent())
            finish()
        }
    }
}
