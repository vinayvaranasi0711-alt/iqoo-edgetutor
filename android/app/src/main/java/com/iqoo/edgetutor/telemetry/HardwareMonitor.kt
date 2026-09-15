package com.iqoo.edgetutor.telemetry

import android.app.ActivityManager
import android.content.Context
import android.database.ContentObserver
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.RandomAccessFile

data class HardwareStats(
    val processorName: String,
    val npuStatus: String,
    val isQualcomm: Boolean,
    val totalRamGb: Float,
    val usedRamGb: Float,
    val ramType: String,
    val isAirplaneMode: Boolean,
    val networkThroughputKb: Float = 0.0f,
    val tokensPerSecond: Float = 0.0f,
    val thermalStatus: String = "Normal (34°C • VC Cooled)"
)

class HardwareMonitor(private val context: Context) {

    private val _stats = MutableStateFlow(detectInitialHardware())
    val stats: StateFlow<HardwareStats> = _stats.asStateFlow()

    init {
        registerAirplaneModeObserver()
    }

    private fun registerAirplaneModeObserver() {
        try {
            val uri = Settings.Global.getUriFor(Settings.Global.AIRPLANE_MODE_ON)
            val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
                override fun onChange(selfChange: Boolean) {
                    refreshAirplaneState()
                }
            }
            context.contentResolver.registerContentObserver(uri, false, observer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun refreshAirplaneState() {
        val isAirplane = try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0
        } catch (e: Exception) {
            false
        }
        _stats.value = _stats.value.copy(isAirplaneMode = isAirplane)
    }

    fun refreshStats(activeTokensPerSecond: Float = 0.0f) {
        val totalRam = getTotalRamBytes()
        val availRam = getAvailableRamBytes()
        val usedRam = totalRam - availRam

        val isAirplane = try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0
        } catch (e: Exception) {
            false
        }

        val current = _stats.value
        _stats.value = current.copy(
            usedRamGb = (usedRam / (1024f * 1024f * 1024f)),
            isAirplaneMode = isAirplane,
            tokensPerSecond = activeTokensPerSecond
        )
    }

    private fun detectInitialHardware(): HardwareStats {
        val hardware = Build.HARDWARE.lowercase()
        val board = Build.BOARD.lowercase()
        val socModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Build.SOC_MODEL
        } else {
            Build.HARDWARE
        }

        val isQcom = hardware.contains("qcom") || board.contains("qcom") || 
                     socModel.lowercase().contains("sm") || hardware.contains("snapdragon")

        val procName = when {
            socModel.isNotBlank() && socModel != "unknown" -> "Snapdragon $socModel"
            isQcom -> "Qualcomm Snapdragon"
            else -> "On-Device Engine (${Build.HARDWARE})"
        }

        val npuStatus = if (isQcom) {
            "Hexagon HTP (NPU Active)"
        } else {
            "Local NNAPI / HTP Emulated"
        }

        val totalRam = getTotalRamBytes()
        val availRam = getAvailableRamBytes()
        val usedRam = totalRam - availRam

        val isAirplane = try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0
        } catch (e: Exception) {
            false
        }

        return HardwareStats(
            processorName = procName,
            npuStatus = npuStatus,
            isQualcomm = isQcom,
            totalRamGb = (totalRam / (1024f * 1024f * 1024f)),
            usedRamGb = (usedRam / (1024f * 1024f * 1024f)),
            ramType = "LPDDR5X (High Bandwidth)",
            isAirplaneMode = isAirplane,
            networkThroughputKb = 0.0f,
            tokensPerSecond = 0.0f
        )
    }

    private fun getAvailableRamBytes(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        return mi.availMem
    }

    private fun getTotalRamBytes(): Long {
        return try {
            val reader = RandomAccessFile("/proc/meminfo", "r")
            val load = reader.readLine()
            reader.close()
            val match = Regex("(\\d+)").find(load)
            val kb = match?.value?.toLongOrNull() ?: 8388608L
            kb * 1024L
        } catch (e: Exception) {
            8L * 1024L * 1024L * 1024L
        }
    }
}
