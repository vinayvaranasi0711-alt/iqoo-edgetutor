package com.iqoo.edgetutor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.edgetutor.engine.LlmInferenceManager
import com.iqoo.edgetutor.engine.OcrEngine
import com.iqoo.edgetutor.engine.OfflineRagStore
import com.iqoo.edgetutor.telemetry.HardwareMonitor
import com.iqoo.edgetutor.ui.screens.CameraScanScreen
import com.iqoo.edgetutor.ui.screens.DashboardScreen
import com.iqoo.edgetutor.ui.screens.SolutionScreen
import com.iqoo.edgetutor.ui.screens.VaultScreen
import com.iqoo.edgetutor.ui.theme.*

enum class Screen {
    DASHBOARD,
    CAMERA_SCAN,
    SOLUTION,
    VAULT
}

class MainActivity : ComponentActivity() {

    private lateinit var hardwareMonitor: HardwareMonitor
    private lateinit var ocrEngine: OcrEngine
    private lateinit var ragStore: OfflineRagStore
    private lateinit var inferenceManager: LlmInferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hardwareMonitor = HardwareMonitor(this)
        ocrEngine = OcrEngine()
        ragStore = OfflineRagStore(this)
        inferenceManager = LlmInferenceManager(this, ragStore)

        setContent {
            IQOOEdgeTutorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = IQOODark
                ) {
                    val hardwareStats by hardwareMonitor.stats.collectAsState()
                    var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }
                    var activeQuestion by remember { mutableStateOf("") }
                    var targetedQuestion by remember { mutableStateOf<String?>(null) }
                    var vaultInitialGrade by remember { mutableStateOf("All") }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
                            when (screen) {
                                Screen.DASHBOARD -> DashboardScreen(
                                    stats = hardwareStats,
                                    onLaunchCamera = {
                                        targetedQuestion = null
                                        currentScreen = Screen.CAMERA_SCAN
                                    },
                                    onOpenVaultGrade = { grade ->
                                        vaultInitialGrade = grade
                                        currentScreen = Screen.VAULT
                                    },
                                    onSelectQuickDoubt = { question ->
                                        activeQuestion = question
                                        currentScreen = Screen.SOLUTION
                                    },
                                    onManualDoubtSubmit = { question ->
                                        activeQuestion = question
                                        currentScreen = Screen.SOLUTION
                                    }
                                )

                                Screen.CAMERA_SCAN -> CameraScanScreen(
                                    ocrEngine = ocrEngine,
                                    targetedQuestion = targetedQuestion,
                                    onQuestionExtracted = { extractedText ->
                                        activeQuestion = extractedText
                                        currentScreen = Screen.SOLUTION
                                    },
                                    onBack = {
                                        currentScreen = Screen.DASHBOARD
                                    }
                                )

                                Screen.SOLUTION -> SolutionScreen(
                                    question = activeQuestion,
                                    stats = hardwareStats,
                                    inferenceManager = inferenceManager,
                                    onRefreshStats = { activeTokenRate ->
                                        hardwareMonitor.refreshStats(activeTokenRate)
                                    },
                                    onBack = {
                                        currentScreen = Screen.DASHBOARD
                                    }
                                )

                                Screen.VAULT -> VaultScreen(
                                    ragStore = ragStore,
                                    initialGrade = vaultInitialGrade,
                                    onTopicScan = { sampleQuestion ->
                                        targetedQuestion = sampleQuestion
                                        currentScreen = Screen.CAMERA_SCAN
                                    }
                                )
                            }
                        }

                        // Docked Bottom Navigation Bar
                        if (currentScreen == Screen.DASHBOARD || currentScreen == Screen.VAULT) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(68.dp)
                                    .background(Color(0xFF0B0D14))
                                    .border(1.dp, Color(0xFF1E2230))
                                    .padding(horizontal = 24.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Home Tab
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.clickable { currentScreen = Screen.DASHBOARD }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = "Home",
                                            tint = if (currentScreen == Screen.DASHBOARD) IQOOAmber else TextMuted,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Text(
                                            text = "Home",
                                            color = if (currentScreen == Screen.DASHBOARD) IQOOAmber else TextMuted,
                                            fontSize = 10.sp,
                                            fontWeight = if (currentScreen == Screen.DASHBOARD) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }

                                    // Center Floating Action Shutter
                                    Box(
                                        modifier = Modifier
                                            .offset(y = (-14).dp)
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(IQOOAmber)
                                            .border(3.dp, Color(0xFF0B0D14), CircleShape)
                                            .clickable {
                                                targetedQuestion = null
                                                currentScreen = Screen.CAMERA_SCAN
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CameraAlt,
                                            contentDescription = "Scan",
                                            tint = Color.Black,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }

                                    // Syllabus Vault Tab
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.clickable {
                                            vaultInitialGrade = "All"
                                            currentScreen = Screen.VAULT
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Book,
                                            contentDescription = "Syllabus",
                                            tint = if (currentScreen == Screen.VAULT) IQOOAmber else TextMuted,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Text(
                                            text = "Syllabus",
                                            color = if (currentScreen == Screen.VAULT) IQOOAmber else TextMuted,
                                            fontSize = 10.sp,
                                            fontWeight = if (currentScreen == Screen.VAULT) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
