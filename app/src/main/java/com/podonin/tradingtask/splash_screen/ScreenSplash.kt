package com.podonin.tradingtask.splash_screen

import androidx.activity.compose.LocalActivity
import androidx.annotation.DimenRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import com.podonin.tradingtask.MainActivity
import com.podonin.tradingtask.R
import com.podonin.tradingtask.SharedViewModel

class ScreenSplash : Screen {

    @Composable
    override fun Content() {

        val viewModel = viewModel<SharedViewModel>(
            viewModelStoreOwner = (LocalActivity.current as MainActivity)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.primary_color))
        ) {

            var isScreenVisible by remember { mutableStateOf(false) }
            var isAnimationEnded by remember { mutableStateOf(false) }

            val enterAnimSpec = tween<Float>(FULL_ANIMATION_DURATION)
            val fadeAnimation = animateFloatAsState(
                targetValue = if (isScreenVisible) 0f else 1f,
                animationSpec = enterAnimSpec,
                finishedListener = {
                    isAnimationEnded = true
                }
            )
            LaunchedEffect(fadeAnimation.value) {
                viewModel.closeSplashScreen(fadeAnimation.value)
            }
            LaunchedEffect(Unit) {
                isScreenVisible = true
            }

            val horizontal = dimensionResource(R.dimen.material_margin_huge)
            AnimatedVisibility(
                visible = isScreenVisible,
                enter = fadeIn(enterAnimSpec),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontal)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_realy),
                        contentDescription = stringResource(R.string.splash_title),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = horizontal)
                            .navigationBarsPadding()
                    ) {
                        val hasSubscription by viewModel.hasSubscriptionFlow.collectAsState()

                        Subscription(
                            hasSubscription = hasSubscription,
                            shouldFillColor = isAnimationEnded,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.material_margin_giant))
                        )
                        Text(
                            text = stringResource(R.string.splash_desc),
                            color = colorResource(R.color.secondary_text_color),
                            fontSize = textSizeResource(R.dimen.splash_desc_text_size),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AnimatedVisibilityScope.Subscription(
        hasSubscription: Boolean,
        shouldFillColor: Boolean,
        modifier: Modifier = Modifier,
    ) {
        val subscriptionSpec = tween<Float>(
            durationMillis = SUBSCRIPTION_ANIMATION_DURATION,
            delayMillis = SUBSCRIPTION_ANIMATION_DELAY,
        )

        if (hasSubscription) {
            val cornerShape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius))
            Box(
                modifier = modifier
                    .animateEnterExit(enter = fadeIn(subscriptionSpec))
                    .background(
                        color = if (shouldFillColor) {
                            colorResource(android.R.color.white)
                        } else {
                            colorResource(android.R.color.transparent)
                        },
                        shape = cornerShape
                    )
                    .border(
                        width = dimensionResource(R.dimen.border_width),
                        color = colorResource(android.R.color.white),
                        shape = cornerShape
                    )
                    .padding(
                        horizontal = dimensionResource(R.dimen.material_margin_big),
                        vertical = dimensionResource(R.dimen.material_margin_medium)
                    )
            ) {
                Text(
                    text = stringResource(R.string.splash_subscription_title),
                    fontSize = textSizeResource(R.dimen.splash_subscription_text_size),
                    color = if (shouldFillColor) {
                        colorResource(R.color.primary_color)
                    } else {
                        colorResource(android.R.color.white)
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    @Composable
    fun textSizeResource(@DimenRes id: Int) = dimensionResource(id).value.sp

    companion object {
        private const val FULL_ANIMATION_DURATION = 2000
        private const val SUBSCRIPTION_ANIMATION_DURATION = 1000
        private const val SUBSCRIPTION_ANIMATION_DELAY = 1000
    }
}