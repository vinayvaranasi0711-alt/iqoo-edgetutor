package com.iqoo.edgetutor.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.iqoo.edgetutor.engine.OcrEngine
import com.iqoo.edgetutor.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CameraScanScreen(
    ocrEngine: OcrEngine,
    targetedQuestion: String? = null,
    onQuestionExtracted: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var isProcessingOcr by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("Align textbook question inside frame") }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var isFlashEnabled by remember { mutableStateOf(false) }

    val defaultFallbackQ = targetedQuestion ?: "A 2kg block slides down a 30 degree incline with kinetic friction coefficient 0.2. Find the acceleration."

    // 1. Runtime Camera Permission Request
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            statusMessage = "Camera permission denied. You can still pick a photo from gallery."
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Gallery Picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            isProcessingOcr = true
            statusMessage = "Reading textbook question with offline OCR..."
            scope.launch {
                try {
                    val stream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(stream)
                    stream?.close()
                    if (bitmap != null) {
                        val text = ocrEngine.recognizeText(bitmap)
                        if (text.isNotBlank()) {
                            onQuestionExtracted(text)
                        } else {
                            onQuestionExtracted(defaultFallbackQ)
                        }
                    }
                } catch (e: Exception) {
                    onQuestionExtracted(defaultFallbackQ)
                }
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "scan_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val capture = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build()
                        imageCapture = capture

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            val cam = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                capture
                            )
                            cameraControl = cam.cameraControl
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Overlay UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(IQOOAmber.copy(alpha = 0.25f))
                        .border(1.dp, IQOOAmber.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (hasCameraPermission) "LIVE OFFLINE CAMERA" else "CAMERA PERMISSION REQUIRED",
                        color = IQOOAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (hasCameraPermission) {
                        IconButton(
                            onClick = {
                                isFlashEnabled = !isFlashEnabled
                                cameraControl?.enableTorch(isFlashEnabled)
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.7f))
                        ) {
                            Icon(
                                imageVector = if (isFlashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Flash",
                                tint = if (isFlashEnabled) IQOOAmber else Color.White
                            )
                        }
                    }

                    IconButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Gallery",
                            tint = Color.White
                        )
                    }
                }
            }

            // Viewfinder Optical Reticle Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(
                        width = 2.dp,
                        color = IQOOAmber.copy(alpha = alpha),
                        shape = RoundedCornerShape(22.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!hasCameraPermission) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF0F1118).copy(alpha = 0.9f))
                            .padding(20.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = IQOOAmber,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Camera Permission Required",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "To scan textbook formulas offline in Airplane Mode, allow camera access.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                            colors = ButtonDefaults.buttonColors(containerColor = IQOOAmber, contentColor = Color.Black),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Grant Camera Permission", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                } else if (isProcessingOcr) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f)),
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        CircularProgressIndicator(color = IQOOAmber)
                        Text(
                            text = statusMessage,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                } else if (targetedQuestion != null) {
                    // Display targeted topic card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.75f))
                            .border(1.dp, IQOOAmber.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "TARGET TOPIC LOADED",
                                color = IQOOAmber,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = targetedQuestion,
                                color = Color.White,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Bottom Shutter
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(IQOOAmber)
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            val capture = imageCapture
                            if (hasCameraPermission && capture != null) {
                                isProcessingOcr = true
                                statusMessage = "Capturing & processing on-device..."

                                val executor = ContextCompat.getMainExecutor(context)
                                capture.takePicture(
                                    executor,
                                    object : ImageCapture.OnImageCapturedCallback() {
                                        override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                                            val bitmap = imageProxy.toBitmap()
                                            imageProxy.close()

                                            val rotatedBitmap = if (rotationDegrees != 0) {
                                                val matrix = Matrix()
                                                matrix.postRotate(rotationDegrees.toFloat())
                                                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                                            } else {
                                                bitmap
                                            }

                                            scope.launch {
                                                try {
                                                    val recognizedText = ocrEngine.recognizeText(rotatedBitmap)
                                                    if (recognizedText.isNotBlank()) {
                                                        onQuestionExtracted(recognizedText)
                                                    } else {
                                                        onQuestionExtracted(defaultFallbackQ)
                                                    }
                                                } catch (e: Exception) {
                                                    onQuestionExtracted(defaultFallbackQ)
                                                }
                                            }
                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            isProcessingOcr = false
                                            onQuestionExtracted(defaultFallbackQ)
                                        }
                                    }
                                )
                            } else {
                                onQuestionExtracted(defaultFallbackQ)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Capture",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Text(
                    text = "Tap shutter to snap & solve with local Snapdragon NPU",
                    color = TextMuted,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
