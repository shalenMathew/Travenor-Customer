package com.example.travenor_customerapp.presentation.Intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travenor_customerapp.R
import com.example.travenor_customerapp.core.persistence.OnboardingPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    paddingValues: PaddingValues,
    onNavigateHome:()-> Unit){

    val wideOrange = Color(0xFFFF7A00)
    val buttonBlue = Color(0xFF1F6FEB)
    val context = LocalContext.current
    val onBoardingPref = remember { OnboardingPrefs(context = context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.25f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.intro_img),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Skip",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

        }

        Spacer(Modifier.height(22.dp))


        Text(
            text = buildAnnotatedString {
                append("Life is short and the\nworld is ")
                withStyle(SpanStyle(color = wideOrange, fontWeight = FontWeight.ExtraBold)) {
                    append("wide")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
            color = Color(0xFF111827),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 34.sp
        )

        Spacer(Modifier.height(12.dp))


        Text(
            text = "At Friends tours and travel, we customize\nreliable and trustworthy educational tours to\ndestinations all over the world",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
            color = Color(0xFF6B7280),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(Modifier.weight(0.6f))


        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    onBoardingPref.setHasSeenOnboarding(true)
                    onNavigateHome()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = paddingValues.calculateTopPadding())
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
        ) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(24.dp))
    }

}