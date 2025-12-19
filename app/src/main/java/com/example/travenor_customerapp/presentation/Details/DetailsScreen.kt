package com.example.travenor_customerapp.presentation.Details

import android.widget.ProgressBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.travenor_customerapp.data.model.Destination
import com.example.travenor_customerapp.R
import com.example.travenor_customerapp.presentation.Details.viewmodel.CustomerRequestUi
import com.example.travenor_customerapp.presentation.Details.viewmodel.DetailViewModel
import kotlinx.coroutines.delay


@Composable
fun DetailScreen(
    vm: DetailViewModel,
    destination: Destination,
    customerId: String,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onBookNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val loading by vm.loading.collectAsStateWithLifecycle()
    val btnEnabled by vm.btnEnabled.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose { vm.onDispose() }
    }


    LaunchedEffect(ui) {
        when (ui) {
//            CustomerRequestUi.Accepted -> onBookNow()
            CustomerRequestUi.Rejected -> {
                delay(timeMillis = 900)
                onBack()
            }
            else -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
//            .padding(paddingValues)
    ) {
        Image(
            painter = painterResource(id = destination.imageRes),
            contentDescription = destination.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "Details",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.size(36.dp))
        }

        DetailsSheet(
            destination = destination,
            ui = ui,
            loading = loading,
            btnEnabled = btnEnabled,
            onBookClicked = onBookNow,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun DetailsSheet(
    destination: Destination,
    ui: CustomerRequestUi,
    loading: Boolean,
    btnEnabled: Boolean,
    onBookClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator(color = Color.Black)
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 20.dp)
        ) {




            val statusText = when (ui) {
                CustomerRequestUi.Idle -> {"Book Now"}
                CustomerRequestUi.Creating -> "Creating request..."
                CustomerRequestUi.Waiting -> "Waiting for owner response..."
                CustomerRequestUi.Accepted -> "Accepted!"
                CustomerRequestUi.Rejected -> "Rejected."
                is CustomerRequestUi.Error -> "Error: ${ui.message}"
            }


            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(48.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(Color(0xFFE6E6E6))
            )

            Spacer(Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF111111)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = destination.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF7B7B7B)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_avatar_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = destination.location.substringBefore(","),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF444444)
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "4.7 (2498)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF444444)
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "$${destination.pricePerPersonUsd}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1677FF)
                    )
                )
                Text(
                    text = "/Person",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7B7B7B)
                )
            }

            Spacer(Modifier.height(14.dp))

            val gallery = listOf(
                R.drawable.img_gallery,
                R.drawable.mumbai,
                R.drawable.delhi,
                R.drawable.kolkata,
                R.drawable.banglore
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(gallery) { res ->
                    Image(
                        painter = painterResource(id = res),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "About Destination",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111111)
            )
            Spacer(Modifier.height(8.dp))

            val shownText =
                if (expanded) destination.description
                else destination.description.take(200).trimEnd() + "..."

            Text(
                text = buildAnnotatedString {
                    append(shownText)
                    append(" ")
                    withStyle(SpanStyle(color = Color(0xFFE85D04), fontWeight = FontWeight.SemiBold)) {
                        append(if (expanded) "Read Less" else "Read More")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7B7B7B),
                modifier = Modifier.clickable { expanded = !expanded }
            )

            Spacer(Modifier.height(25.dp))

            Button(
                onClick = onBookClicked,
                enabled = btnEnabled && !loading && ui !is CustomerRequestUi.Waiting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1677FF),
                    disabledContainerColor = Color(0xFF1677FF))
            ) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
            }

            Spacer(Modifier.height(50.dp))
        }
    }
}