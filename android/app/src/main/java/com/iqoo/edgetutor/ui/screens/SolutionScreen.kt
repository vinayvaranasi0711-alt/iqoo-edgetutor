package com.iqoo.edgetutor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.edgetutor.engine.LlmInferenceManager
import com.iqoo.edgetutor.telemetry.HardwareStats
import com.iqoo.edgetutor.ui.components.FormattedSolutionContent
import com.iqoo.edgetutor.ui.components.HardwareTelemetryPill
import com.iqoo.edgetutor.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SolutionScreen(
    question: String,
    stats: HardwareStats,
    inferenceManager: LlmInferenceManager,
    onRefreshStats: (Float) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var streamedSolution by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(true) }
    var activeTokensPerSec by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(question) {
        streamedSolution = ""
        isGenerating = true
        activeTokensPerSec = 0f

        inferenceManager.streamSolution(
            question = question,
            onTokenRateUpdate = { rate ->
                activeTokensPerSec = rate
                onRefreshStats(rate)
            }
        ).collectLatest { token ->
            streamedSolution += token
        }

        isGenerating = false
        onRefreshStats(0f)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(IQOODark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 36.dp)
    ) {
        // 1. Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(IQOOCardBg)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(15.dp),
                            color = IQOOAmber,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "NPU INFERENCE ACTIVE",
                            color = IQOOAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Done",
                            tint = IQOOGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "COMPUTED • 0 KB DATA",
                            color = IQOOGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // 2. Hardware HUD Pill
        item {
            HardwareTelemetryPill(
                stats = stats.copy(tokensPerSecond = activeTokensPerSec)
            )
        }

        // 3. Extracted Doubt Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(IQOOCardBg)
                    .border(1.dp, IQOOCardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "QUESTION CAPTURED (OFFLINE OCR)",
                        color = IQOOAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = question,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        // 4. Solution Streaming Box
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF10121A))
                    .border(1.dp, IQOOCardBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FormattedSolutionContent(content = streamedSolution)

                    if (isGenerating) {
                        Box(
                            modifier = Modifier
                                .size(width = 8.dp, height = 16.dp)
                                .background(IQOOAmber)
                        )
                    }
                }
            }
        }

        // 5. Socratic Follow-Up Action Buttons
        if (!isGenerating) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "SOCRATIC DEEP-DIVES",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.8.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    streamedSolution += "\n\n### 💡 Socratic Intuition: Why mg·cos(θ)?\n" +
                                            "As inclination angle \$\\theta\$ tilts towards 90° (a vertical wall), the block ceases to press against the incline (\$mg\\cos(90^\\circ) = 0\$). Without pressing force, the normal force drops to zero—meaning friction disappears completely!"
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IQOOCardBg,
                                contentColor = IQOOAmber
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, IQOOAmber.copy(alpha = 0.5f))
                        ) {
                            Text("Why mg·cos(θ)?", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    streamedSolution += "\n\n### 🎯 JEE Advanced Twist: Variable Friction\n" +
                                            "Suppose the surface has variable friction \$\\mu(x) = k \\cdot x\$. Motion halts when \$mg\\sin\\theta = k x \\cdot mg\\cos\\theta \\implies x_{\\text{max}} = \\frac{\\tan\\theta}{k}\$."
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IQOOCardBg,
                                contentColor = IQOOCyan
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, IQOOCyan.copy(alpha = 0.5f))
                        ) {
                            Text("JEE Twist", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IQOOAmber,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Scan Next Question", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
