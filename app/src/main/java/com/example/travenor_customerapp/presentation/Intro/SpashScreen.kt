package com.example.travenor_customerapp.presentation.Intro

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travenor_customerapp.core.persistence.OnboardingPrefs
import com.example.travenor_customerapp.core.ui.theme.brandBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.math.hypot


@Composable
fun SplashScreen(
    onNavigateHome: (Boolean) -> Unit,
) {
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    var startAnim by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val onBoardingPref = remember { OnboardingPrefs(context = context) }

    val density = LocalDensity.current


    val targetSizeDp = remember(parentSize, density) {
        if (parentSize == IntSize.Zero) 0.dp
        else with(density) {
            hypot(
                parentSize.width.toFloat(),
                parentSize.height.toFloat()
            ).toDp()
        }
    }

    val circleSize by animateDpAsState(
        targetValue = if (startAnim) targetSizeDp else 0.dp,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "reveal"
    )

    val radius by animateDpAsState(
        targetValue = if (startAnim) 0.dp else 1000.dp,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "radius"
    )

    LaunchedEffect(parentSize) {
        if (parentSize == IntSize.Zero) return@LaunchedEffect
        startAnim = true
        delay(2200)
        val hasSeen: Boolean = onBoardingPref.hasSeenOnboarding.first()
        onNavigateHome(hasSeen)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .onSizeChanged { parentSize = it },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(RoundedCornerShape(radius))
                .background(brandBlue)
        )

        Text(
            text = "Travenor",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}