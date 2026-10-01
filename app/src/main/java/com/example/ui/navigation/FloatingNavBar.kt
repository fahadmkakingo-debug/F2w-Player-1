package com.example.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.F2WBorderGlow
import com.example.ui.theme.F2WCardBorder
import com.example.ui.theme.F2WCyanPrimary
import com.example.ui.theme.F2WNavBackground
import com.example.ui.theme.F2WTextPrimary
import com.example.ui.theme.F2WTextTertiary
import com.example.ui.theme.F2WVioletAccent

@Composable
fun FloatingNavBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating 3D Navigation Card
        Box(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                // Soft dual-layer ambient and spot shadow for 3D elevation
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(28.dp),
                    spotColor = Color(0xCC000000),
                    ambientColor = Color(0x66000000)
                )
                .clip(RoundedCornerShape(28.dp))
                // 3D Glass / Metallic dual highlight border
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x55FFFFFF),      // Crisp top rim light (3D specular)
                            F2WBorderGlow.copy(alpha = 0.4f),
                            F2WCardBorder.copy(alpha = 0.2f),
                            Color(0x15FFFFFF)       // Subtle bottom reflection
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                // Dark frosted obsidian surface with top sheen
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xF5182030), // Slightly brighter top for 3D depth
                            F2WNavBackground,  // Rich dark obsidian core
                            Color(0xF50D111A)  // Deep grounded bottom
                        )
                    )
                )
                .padding(horizontal = 8.dp, vertical = 7.dp)
                .testTag("floating_bottom_navigation_card")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavTab.entries.forEach { tab ->
                    val isSelected = tab == selectedTab

                    NavCardItem(
                        tab = tab,
                        isSelected = isSelected,
                        onClick = { onTabSelected(tab) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun NavCardItem(
    tab: NavTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Smooth active-state animations
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "nav_icon_scale"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) F2WCyanPrimary else F2WTextTertiary,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "nav_icon_color"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) F2WTextPrimary else F2WTextTertiary,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "nav_text_color"
    )

    val pillBackgroundBrush = if (isSelected) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x3300E5FF), // Subtle cyan glow
                Color(0x228A4DFF), // Subtle violet undertone
                Color(0x18182236)
            )
        )
    } else {
        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
    }

    val pillBorderBrush = if (isSelected) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x6600E5FF),
                Color(0x228A4DFF)
            )
        )
    } else {
        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (isSelected) {
                    Modifier
                        .border(
                            width = 1.dp,
                            brush = pillBorderBrush,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .background(
                            brush = pillBackgroundBrush,
                            shape = RoundedCornerShape(20.dp)
                        )
                } else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = F2WCyanPrimary, bounded = true),
                onClick = onClick
            )
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .testTag(tab.testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                contentDescription = tab.title,
                tint = iconColor,
                modifier = Modifier
                    .size(23.dp)
                    .scale(iconScale)
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = tab.title,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                letterSpacing = 0.3.sp,
                maxLines = 1
            )
        }
    }
}
