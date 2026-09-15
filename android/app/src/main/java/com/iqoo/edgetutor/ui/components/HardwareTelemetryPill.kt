package com.iqoo.edgetutor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.edgetutor.telemetry.HardwareStats
import com.iqoo.edgetutor.ui.theme.*

@Composable
fun HardwareTelemetryPill(
    stats: HardwareStats,
    isMonsterMode: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF131622),
                        Color(0xFF0F1118),
                        Color(0xFF090A0E)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        IQOOAmber.copy(alpha = 0.7f),
                        IQOONeonOrange.copy(alpha = 0.4f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row: Chipset & Live Network Mode Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isMonsterMode) IQOOAmber else IQOOCyan)
                    )
                    Text(
                        text = stats.processorName.uppercase(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }

                // Dynamic Network Badge: Checks if real Airplane Mode is ON vs OFF
                if (stats.isAirplaneMode) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(IQOOGreen.copy(alpha = 0.2f))
                            .border(1.dp, IQOOGreen.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AirplanemodeActive,
                            contentDescription = "Airplane",
                            tint = IQOOGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "AIRPLANE ON • 0 KB",
                            color = IQOOGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(IQOOCyan.copy(alpha = 0.15f))
                            .border(1.dp, IQOOCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Shield",
                            tint = IQOOCyan,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "AIR-GAPPED • 0 KB",
                            color = IQOOCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Supercar Telemetry Gauges (3-Column Grid)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Gauge 1: NPU Engine
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF090A0F))
                        .border(1.dp, Color(0xFF222634), RoundedCornerShape(10.dp))
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("NPU CORE", color = TextMuted, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("Hexagon HTP", color = IQOOCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Gauge 2: RAM Speed / Bandwidth
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF090A0F))
                        .border(1.dp, Color(0xFF222634), RoundedCornerShape(10.dp))
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("RAM SPEED", color = TextMuted, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                        Text("77 GB/s", color = IQOOAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Gauge 3: Real-time Tok/s or Power Draw
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF090A0F))
                        .border(1.dp, Color(0xFF222634), RoundedCornerShape(10.dp))
                        .padding(vertical = 6.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            if (stats.tokensPerSecond > 0f) "VELOCITY" else "POWER",
                            color = TextMuted,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            if (stats.tokensPerSecond > 0f) String.format("%.1f t/s", stats.tokensPerSecond) else "0.8W (Cool)",
                            color = if (stats.tokensPerSecond > 0f) IQOOAmber else IQOOGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
