package com.example.ui.screens.privacy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.F2WCardBorder
import com.example.ui.theme.F2WCyanPrimary
import com.example.ui.theme.F2WSurface
import com.example.ui.theme.F2WSurfaceElevated
import com.example.ui.theme.F2WTextPrimary
import com.example.ui.theme.F2WTextSecondary
import com.example.ui.theme.F2WTextTertiary
import com.example.ui.theme.F2WVaultGradient
import com.example.ui.theme.F2WVioletAccent

@Composable
fun PrivacyScreen(
    modifier: Modifier = Modifier
) {
    var showSetupPreviewDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(F2WSurface)
            .testTag("privacy_screen_container"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Glowing Vault Lock Emblem
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                F2WVioletAccent.copy(alpha = 0.35f),
                                F2WCyanPrimary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(F2WVioletAccent, F2WCyanPrimary)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Secure Vault",
                    tint = F2WCyanPrimary,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Media Privacy Vault",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = F2WTextPrimary,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Safeguard personal videos and audio clips in an isolated, encrypted safe zone.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = F2WTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Setup CTA
            Button(
                onClick = { showSetupPreviewDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(F2WVioletAccent, F2WCyanPrimary)
                        )
                    )
                    .testTag("setup_vault_btn")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Setup Vault Protection",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Feature Highlights Section
            Text(
                text = "VAULT CAPABILITIES",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = F2WTextTertiary,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        item {
            VaultFeatureCard(
                icon = Icons.Filled.VisibilityOff,
                iconTint = F2WCyanPrimary,
                title = "Invisible to Other Apps",
                description = "Hidden media is excluded from the Android media scanner, gallery, and file explorers."
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            VaultFeatureCard(
                icon = Icons.Filled.Fingerprint,
                iconTint = F2WVioletAccent,
                title = "Biometrics & Master PIN",
                description = "Quick unlock with your phone's fingerprint sensor or a customized private security code."
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            VaultFeatureCard(
                icon = Icons.Filled.Shield,
                iconTint = Color(0xFF10B981),
                title = "Zero Cloud Leakage",
                description = "All keys and media remain 100% on your device, fully under your local control."
            )
            Spacer(modifier = Modifier.height(90.dp)) // padding for bottom nav
        }
    }

    if (showSetupPreviewDialog) {
        AlertDialog(
            onDismissRequest = { showSetupPreviewDialog = false },
            containerColor = F2WSurfaceElevated,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Security,
                    contentDescription = null,
                    tint = F2WCyanPrimary,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Vault Setup Foundation",
                    color = F2WTextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "F2W Player's encrypted media vault foundation is active. In subsequent updates, you will configure your secure master PIN, fingerprint authentication, and move files directly from the video library into the safe vault.",
                    color = F2WTextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = { showSetupPreviewDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = F2WCyanPrimary),
                    modifier = Modifier.testTag("dismiss_vault_preview_btn")
                ) {
                    Text("Got It", color = Color(0xFF070B12), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun VaultFeatureCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(F2WSurfaceElevated)
            .border(1.dp, F2WCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = F2WTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = F2WTextSecondary,
                    lineHeight = 18.sp
                )
            )
        }
    }
}
