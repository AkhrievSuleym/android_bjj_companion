package com.example.bjjcompanion.presentation.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bjjcompanion.R
import kotlinx.coroutines.delay

/**
 * Welcome screen with animations.
 *
 * Animations used:
 * - Image: Scale + Fade in
 * - Title: Slide from top + Fade in
 * - Subtitle: Slide from bottom + Fade in (delayed)
 * - Button: Scale + Fade in (delayed)
 * - Shimmer effect on button
 */
@Composable
fun WelcomeStep(
    onNext: () -> Unit
) {
    // Animation triggers
    var imageVisible by remember { mutableStateOf(false) }
    var titleVisible by remember { mutableStateOf(false) }
    var subtitleVisible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }

    // Trigger animations sequentially
    LaunchedEffect(Unit) {
        delay(100)
        imageVisible = true
//        delay(300)
        titleVisible = true
//        delay(200)
        subtitleVisible = true
//        delay(300)
        buttonVisible = true
    }

    // Infinite shimmer animation for button
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Animated Image/Icon
        AnimatedVisibility(
            visible = imageVisible,
            enter = fadeIn(animationSpec = tween(800)) +
                    scaleIn(initialScale = 0.8f, animationSpec = tween(800))
        ) {
            // TODO: Replace with Image when welcome_bjj.png is added to res/drawable/
             Image(
                 painter = painterResource(id = R.drawable.welcome_bjj),
                 contentDescription = "BJJ Illustration",
                 modifier = Modifier.size(300.dp).padding(12.dp),
                 contentScale = ContentScale.Fit
             )

            // Temporary placeholder icon
//            Icon(
//                imageVector = Icons.Default.Favorite,
//                contentDescription = "BJJ Placeholder",
//                modifier = Modifier.size(120.dp),
//                tint = MaterialTheme.colorScheme.primary
//            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Animated Title
        AnimatedVisibility(
            visible = titleVisible,
            enter = fadeIn(animationSpec = tween(600)) +
                    slideInVertically(
                        initialOffsetY = { -40 },
                        animationSpec = tween(600)
                    )
        ) {
            Text(
                text = "Welcome to\nBJJ Companion",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Animated Subtitle
        AnimatedVisibility(
            visible = subtitleVisible,
            enter = fadeIn(animationSpec = tween(600)) +
                    slideInVertically(
                        initialOffsetY = { 40 },
                        animationSpec = tween(600)
                    )
        ) {
            Text(
                text = "Your all-in-one app for tracking weight, nutrition, and BJJ training progress",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Animated Button with shimmer effect
        AnimatedVisibility(
            visible = buttonVisible,
            enter = fadeIn(animationSpec = tween(600)) +
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
        ) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(shimmerAlpha),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
