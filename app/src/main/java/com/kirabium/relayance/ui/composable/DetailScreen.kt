package com.kirabium.relayance.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirabium.relayance.R
import com.kirabium.relayance.domain.model.Customer
import com.kirabium.relayance.extension.DateExt.Companion.toHumanDate
import java.util.Date

object DetailTestTags {
    const val CUSTOMER_NAME = "detail_customer_name"
    const val CUSTOMER_EMAIL = "detail_customer_email"
    const val CUSTOMER_DATE = "detail_customer_date"
    const val NEW_RIBBON = "detail_new_ribbon"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    customer: Customer,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.detail_screen_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.contentDescription_go_back)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onEditClick) {
                        Text(text = stringResource(id = R.string.edit))
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(Color(0xFFF3F3F3))
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(modifier = Modifier.padding(24.dp)) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = customer.name,
                            modifier = Modifier.testTag(DetailTestTags.CUSTOMER_NAME),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = customer.email,
                            modifier = Modifier.testTag(DetailTestTags.CUSTOMER_EMAIL),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text =  stringResource(id = R.string.created_at, customer.createdAt.toHumanDate()),
                            modifier = Modifier.testTag(DetailTestTags.CUSTOMER_DATE),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            )
                        )
                    }
                    if (customer.isNewCustomer())
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 24.dp, y = (-24).dp)
                                .rotate(45f)
                                .background(Color.Red)
                                .testTag(DetailTestTags.NEW_RIBBON)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.new_ribbon),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        customer = Customer(0, "Nom du Client", "email@client.com", Date()),
        onBackClick = {}
    )
}
