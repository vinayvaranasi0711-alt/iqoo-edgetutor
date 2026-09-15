package com.iqoo.edgetutor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.iqoo.edgetutor.ui.components.HardwareTelemetryPill
import com.iqoo.edgetutor.ui.theme.*

data class QuickDoubt(
    val title: String,
    val subject: String,
    val question: String,
    val examTag: String,
    val examColor: Color,
    val grade: String
)

@Composable
fun DashboardScreen(
    stats: HardwareStats,
    onLaunchCamera: () -> Unit,
    onOpenVaultGrade: (String) -> Unit,
    onSelectQuickDoubt: (String) -> Unit,
    onManualDoubtSubmit: (String) -> Unit
) {
    var showManualInputDialog by remember { mutableStateOf(false) }
    var manualQuestionText by remember { mutableStateOf("") }

    val quickDoubts = listOf(
        QuickDoubt(
            title = "Mirror Formula & Magnification",
            subject = "Physics",
            question = "An object is placed at a distance of 10 cm in front of a concave mirror of focal length 15 cm. Find position, nature, and magnification of image.",
            examTag = "CBSE 10 • Light",
            examColor = IQOOCyan,
            grade = "Class 10"
        ),
        QuickDoubt(
            title = "Equations of Motion Calculation",
            subject = "Physics",
            question = "A car traveling at 18 km/h accelerates uniformly to 36 km/h in 5 seconds. Calculate the acceleration and total distance covered.",
            examTag = "CBSE 9 • Motion",
            examColor = Color(0xFF60A5FA),
            grade = "Class 9"
        ),
        QuickDoubt(
            title = "Block on 30° Incline with Friction",
            subject = "Physics",
            question = "A 2kg block slides down a 30 degree incline with kinetic friction coefficient 0.2. Find the acceleration.",
            examTag = "JEE Main • Mechanics",
            examColor = IQOOAmber,
            grade = "Class 11"
        ),
        QuickDoubt(
            title = "Electricity & Wire Resistance",
            subject = "Physics",
            question = "A wire of length l and cross-section A has resistance R. If its length is doubled by stretching, what will be its new resistance?",
            examTag = "CBSE 10 • Circuits",
            examColor = Color(0xFFA78BFA),
            grade = "Class 10"
        ),
        QuickDoubt(
            title = "Definite Integral by Parts",
            subject = "Mathematics",
            question = "Evaluate the indefinite integral of x * sin(x) dx using integration by parts.",
            examTag = "JEE 12 • Calculus",
            examColor = Color(0xFF34D399),
            grade = "Class 12"
        ),
        QuickDoubt(
            title = "Equilibrium Constant Kp vs Kc",
            subject = "Chemistry",
            question = "Derive the relation between Kp and Kc for a gaseous reaction. When does Kp equal Kc?",
            examTag = "JEE 11 • Equilibrium",
            examColor = IQOONeonOrange,
            grade = "Class 11"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(IQOODark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 85.dp)
    ) {
        // 1. Supercar Telemetry HUD
        item {
            HardwareTelemetryPill(stats = stats)
        }

        // 2. Monster Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF261D0F),
                                Color(0xFF161922),
                                Color(0xFF0F1118)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(IQOOAmber.copy(alpha = 0.8f), IQOONeonOrange.copy(alpha = 0.5f), Color.Transparent)
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MONSTER AI ENGINE",
                            color = IQOOAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "100% On-Device",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Text(
                        text = "Snap. Solve. Master.",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Point camera at any CBSE 9-10 or JEE question in Airplane Mode. Instant step-by-step Socratic solution powered by Snapdragon NPU.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = onLaunchCamera,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IQOOAmber,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scan",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DIRECT CAMERA SCAN (OFFLINE)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // 3. Browse Syllabus & Materials (4 Grade Cards matching web companion!)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BROWSE SYLLABUS & MATERIALS",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "View All (11 Chapters)",
                        color = IQOOAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onOpenVaultGrade("All") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GradeBox(
                        gradeTag = "CBSE 9",
                        title = "Class 9 Science & Math",
                        subtitle = "Motion, Force, Gravitation",
                        tagColor = Color(0xFF60A5FA),
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenVaultGrade("Class 9") }
                    )
                    GradeBox(
                        gradeTag = "CBSE 10",
                        title = "Class 10 Science & Math",
                        subtitle = "Light, Electricity, Trig",
                        tagColor = Color(0xFFA78BFA),
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenVaultGrade("Class 10") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GradeBox(
                        gradeTag = "JEE 11",
                        title = "Class 11 Physics & Chem",
                        subtitle = "Incline Friction, Kp/Kc",
                        tagColor = IQOOAmber,
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenVaultGrade("Class 11") }
                    )
                    GradeBox(
                        gradeTag = "JEE 12",
                        title = "Class 12 Math Calculus",
                        subtitle = "Integration by Parts",
                        tagColor = Color(0xFF34D399),
                        modifier = Modifier.weight(1f),
                        onClick = { onOpenVaultGrade("Class 12") }
                    )
                }
            }
        }

        // 4. Fast Stage Demo Presets Header
        item {
            Text(
                text = "FAST STAGE DEMO PRESETS",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.8.sp
            )
        }

        items(quickDoubts) { doubt ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(IQOOCardBg)
                    .border(1.dp, IQOOCardBorder, RoundedCornerShape(16.dp))
                    .clickable { onSelectQuickDoubt(doubt.question) }
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = doubt.title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(doubt.examColor.copy(alpha = 0.15f))
                                .border(1.dp, doubt.examColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = doubt.examTag,
                                color = doubt.examColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Text(
                        text = doubt.question,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Solve Offline",
                            color = IQOOAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = IQOOAmber,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }

    if (showManualInputDialog) {
        AlertDialog(
            onDismissRequest = { showManualInputDialog = false },
            containerColor = IQOOSurface,
            title = { Text("Type Question", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = manualQuestionText,
                    onValueChange = { manualQuestionText = it },
                    placeholder = { Text("e.g. A 2kg block slides on a 30° incline...", color = TextMuted, fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = IQOOAmber,
                        unfocusedBorderColor = IQOOCardBorder
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (manualQuestionText.isNotBlank()) {
                            val q = manualQuestionText
                            showManualInputDialog = false
                            manualQuestionText = ""
                            onManualDoubtSubmit(q)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IQOOAmber)
                ) {
                    Text("Solve Offline", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualInputDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun GradeBox(
    gradeTag: String,
    title: String,
    subtitle: String,
    tagColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(IQOOCardBg)
            .border(1.dp, IQOOCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(tagColor.copy(alpha = 0.15f))
                    .border(1.dp, tagColor.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    text = gradeTag,
                    color = tagColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}
