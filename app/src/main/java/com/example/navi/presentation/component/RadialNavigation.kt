package com.example.navi.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

private data class NavigationItem(
    val title: String,
    val symbol: String,
    val route: String
)

@Composable
fun RadialNavigation(
    currentRoute: String?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationItem("Трекер", "⌂", "tracker"),
        NavigationItem("Привычки", "✓", "habits"),
        NavigationItem("Вода и калории", "◉", "nutrition"),
        NavigationItem("Дневник", "▤", "diary"),
        NavigationItem("Среда", "☁", "environment")
    )

    val positions = listOf(
        (-128).dp to (-85).dp,
        (-68).dp to (-145).dp,
        0.dp to (-170).dp,
        68.dp to (-145).dp,
        128.dp to (-85).dp
    )

    val rotation = animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        animationSpec = tween(250),
        label = "navigationRotation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        items.forEachIndexed { index, item ->
            AnimatedVisibility(
                visible = expanded,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(
                        x = positions[index].first,
                        y = positions[index].second
                    ),
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 180,
                        delayMillis = index * 55
                    )
                ) + scaleIn(
                    initialScale = 0.5f,
                    animationSpec = tween(
                        durationMillis = 180,
                        delayMillis = index * 55
                    )
                ),
                exit = fadeOut(tween(100)) +
                        scaleOut(tween(100), targetScale = 0.5f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        onNavigate(item.route)
                        onExpandedChange(false)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                if (currentRoute == item.route) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.secondaryContainer
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.symbol,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (currentRoute == item.route) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            }
                        )
                    }

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Центральная кнопка
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onExpandedChange(!expanded)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                modifier = Modifier.rotate(rotation.value),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Кнопка настроек
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 28.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    onExpandedChange(false)
                    onNavigate("settings")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚙",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}