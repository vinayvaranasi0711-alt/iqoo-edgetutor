# ⚡ iQOO EdgeTutor

> **100% Offline On-Device AI Mentor for Students**  
> *Engineered to showcase Qualcomm Snapdragon NPU, LPDDR5X RAM, and Vapor Chamber cooling on iQOO smartphones.*

[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20%28API%2026%2B%29-brightgreen.svg)](https://developer.android.com)
[![Offline](https://img.shields.io/badge/Offline-100%25%20Zero%20Network-blue.svg)](#strict-offline-architecture)
[![NPU](https://img.shields.io/badge/Hardware-Snapdragon%20Hexagon%20HTP-orange.svg)](#hardware-synergy)
[![Live Demo](https://img.shields.io/badge/🚀%20Live%20Interactive%20Demo-Try%20Now-F7B500?style=for-the-badge)](https://vinayvaranasi0711-alt.github.io/iqoo-edgetutor/)

> 🌐 **Live Web Demo:** [https://vinayvaranasi0711-alt.github.io/iqoo-edgetutor/](https://vinayvaranasi0711-alt.github.io/iqoo-edgetutor/)  
> *(Test the full interactive iQOO 12 simulator, camera scanning, KaTeX formulas, and CBSE/JEE Vault directly in any browser!)*

---

## 🎯 The Vision & Hackathon Concept

* **The Problem:** Over 300 million students in Tier-2/3 cities, small towns, and rural college hostels struggle with erratic, slow, or expensive internet. When studying for competitive exams (JEE, NEET, CBSE) late at night or during train commutes, cloud-based AI tools (like ChatGPT) fail, lag, or demand costly subscriptions.
* **The Concept:** An educational AI tutor that runs **entirely offline on the smartphone’s local silicon** using Small Language Models (SLMs) and on-device OCR.
* **How It Uses iQOO Hardware:** iQOO smartphones package flagship **Snapdragon 8-series and 7-series processors**, **8GB–16GB high-bandwidth LPDDR5X RAM**, and **massive 6000mm² vapor chambers** at accessible prices. By offloading inference to the Snapdragon Hexagon NPU, students can snap any textbook formula and get step-by-step Socratic explanations without a single byte of internet.
* **The "Airplane Mode" Wow Factor:** During live demonstrations, put the phone into Airplane Mode, snap a photo of an advanced physics problem, and watch the local AI solve and explain the concept in real time at **24+ tokens per second**.
* **Why It Helps iQOO:** Proves that iQOO’s benchmark-shattering RAM bandwidth and AnTuTu scores aren’t just for mobile gaming—they power next-generation edge AI productivity that budget competitors (e.g. Exynos/Helio devices) cannot run.

---

## 🏗️ Technical Architecture

```
                       ┌─────────────────────────────────────────────────┐
                       │           iQOO Camera / Gallery UI              │
                       └───────────────────────┬─────────────────────────┘
                                               │
                                      [Camera Snapshot]
                                               │
                                               ▼
                       ┌─────────────────────────────────────────────────┐
                       │        Stage 1: Ultra-Fast Vision / OCR         │
                       │   Google ML Kit Offline Text Recognition        │
                       │   (100% On-Device • Zero Network Calls)         │
                       └───────────────────────┬─────────────────────────┘
                                               │ Extracted Formula / Text
                                               ▼
 ┌─────────────────────────┐   ┌─────────────────────────────────────────┐
 │   NCERT / CBSE Corpus   │   │     Stage 2: On-Device Semantic RAG     │
 │  Classes 9-12 (PCM/PCB) │──▶│   - In-memory structured knowledge base │
 │  Pre-indexed curriculum │   │   - Grounds reasoning in CBSE standards │
 └─────────────────────────┘   └───────────────────┬─────────────────────┘
                                                   │ Grounded Context
                                                   ▼
                       ┌─────────────────────────────────────────────────┐
                       │    Stage 3: On-Device SLM Inference Engine      │
                       │   - Model: Gemma 2B / Phi-3 INT4 Quantized      │
                       │   - Engine: Qualcomm Hexagon HTP / MediaPipe    │
                       │   - Memory Bandwidth: 77 GB/s (LPDDR5X)         │
                       └───────────────────────┬─────────────────────────┘
                                               │ Streaming Output (~24 tok/s)
                                               ▼
                       ┌─────────────────────────────────────────────────┐
                       │           Interactive Socratic UI               │
                       │   - KaTeX Math Formula Rendering                │
                       │   - Hardware Telemetry HUD (Live tok/s, NPU)    │
                       │   - Concept Quiz & "Explain Why?" Drilldown     │
                       └─────────────────────────────────────────────────┘
```

---

## 🔒 Strict Offline Architecture

In `android/app/src/main/AndroidManifest.xml`, **`android.permission.INTERNET` is deliberately omitted**. This guarantees:
1. **Zero Data Leaks:** Complete student privacy—no textbook photos or questions ever leave the device.
2. **Deterministic Latency:** Zero network jitter or cloud server outages.
3. **Auditability:** Any judge or reviewer inspecting the APK manifest can verify zero network permissions.

---

## ⚡ The Hardware Moat: iQOO vs. Budget Rivals

Because autoregressive token generation is **memory-bandwidth bound** ($T_{\text{max}} \approx B_{\text{mem}} / M_{\text{weights}}$):

| Hardware Feature | Budget Competitor (Exynos 850 / Helio G99) | iQOO (Snapdragon 7+ Gen 3 / 8 Gen 2 / 8 Gen 3) | Real-World Impact on Edge AI |
| :--- | :--- | :--- | :--- |
| **RAM Bandwidth** | LPDDR4X (~17 – 34 GB/s) | **LPDDR5X (Up to 77 GB/s)** | **2.3x faster token generation.** Delivers 24+ tok/s instead of lagging. |
| **NPU Acceleration** | None or Basic (~4–10 TOPS) | **Qualcomm Hexagon HTP (Up to 73 TOPS)** | Sub-watt power consumption; draws 80% less battery than CPU/GPU. |
| **RAM Capacity** | 4GB – 6GB (OOM crashes) | **8GB – 16GB** | Keeps a 2B–4B model resident in memory alongside Android OS. |
| **Cooling Solution** | Simple graphite sheet | **6000mm² Vapor Chamber** | Sustains 30+ minutes of continuous inference with zero thermal throttling. |

*(Read the complete technical whitepaper in [`submission/HARDWARE_SYNERGY.md`](submission/HARDWARE_SYNERGY.md))*

---

## 📁 Repository Structure

```
IQ/
├── android/                             # Native Android Studio Project
│   ├── build.gradle.kts                 # Root Gradle build script
│   ├── settings.gradle.kts              # Project settings & repositories
│   ├── gradle.properties               # Memory & AndroidX config
│   ├── gradlew / gradlew.bat            # Gradle 8.9 wrapper scripts
│   ├── local.properties                 # Android SDK configuration
│   └── app/
│       ├── build.gradle.kts             # Compose, ML Kit, MediaPipe, CameraX
│       └── src/main/
│           ├── AndroidManifest.xml      # Zero internet permissions (strict offline)
│           ├── assets/
│           │   └── ncert_knowledge_base.json # Pre-loaded NCERT Class 11-12 syllabus
│           ├── java/com/iqoo/edgetutor/
│           │   ├── MainActivity.kt      # Main edge-to-edge navigation container
│           │   ├── ui/
│           │   │   ├── theme/           # iQOO Cyberpunk theme (Black/Amber/Neon)
│           │   │   ├── components/      # HardwareTelemetryPill, MathFormulaCard
│           │   │   └── screens/         # DashboardScreen, CameraScanScreen, SolutionScreen
│           │   ├── engine/
│           │   │   ├── OcrEngine.kt     # Offline Google ML Kit text recognizer
│           │   │   ├── OfflineRagStore.kt # NCERT knowledge base loader & semantic matcher
│           │   │   └── LlmInferenceManager.kt # MediaPipe / local SLM streaming runner
│           │   └── telemetry/
│           │       └── HardwareMonitor.kt # Real-time NPU status, RAM, & token tracker
│           └── res/                     # Values, colors, themes, drawables
├── web-companion/                       # Interactive Web Prototype & Phone Emulator
│   ├── index.html                       # High-polish iQOO 12 phone simulator with live OCR & streaming
│   └── package.json                     # Lightweight server script
├── submission/                          # Hackathon Submission Package
│   ├── PITCH_DECK.md                    # 5-Slide Pitch Deck with speaker notes & layouts
│   ├── DEMO_VIDEO_SCRIPT.md             # 60-Second Airplane Mode video storyboard & timing
│   └── HARDWARE_SYNERGY.md              # Technical whitepaper on iQOO NPU & RAM moat
└── README.md                            # Comprehensive project guide
```

---

## 🚀 How to Run the Prototype

### Option 1: Launch Interactive Web Companion (Instant Zero-Setup Demo)
You can run the web companion directly in any browser:
```bash
# Simply double-click web-companion/index.html OR serve it locally:
cd web-companion
npx serve -l 3000 .
# Open http://localhost:3000 in your browser
```
**Features in the Web Companion:**
* Authentic **iQOO 12 phone bezel mockup**.
* Working **Airplane Mode switch** in the status bar.
* Optical camera viewfinder with sample JEE questions (Physics, Math, Chemistry).
* Live **token-by-token streaming** with KaTeX mathematical formulas.
* Real-time **Hardware Telemetry HUD** reporting `24 tok/s`, `0 KB data`, and `Hexagon HTP NPU Active`.

### Option 2: Open and Build Native Android App
1. Open **Android Studio** (Hedgehog, Iguana, Koala, or Ladybug).
2. Choose **Open an Existing Project** and select the `android/` directory.
3. Allow Gradle to sync dependencies.
4. Connect an Android device (or start an Android emulator with API 26+).
5. Click **Run (`Shift + F10`)**.
6. Or build directly from command line:
   ```bash
   cd android
   .\gradlew.bat assembleDebug
   ```

---

## 🏆 Hackathon Submission Deliverables

* **5-Slide Pitch Deck:** [`submission/PITCH_DECK.md`](submission/PITCH_DECK.md)
* **60-Second Video Script:** [`submission/DEMO_VIDEO_SCRIPT.md`](submission/DEMO_VIDEO_SCRIPT.md)
* **Hardware Whitepaper:** [`submission/HARDWARE_SYNERGY.md`](submission/HARDWARE_SYNERGY.md)

---

## 👥 Brand & Team Alignment
Designed with passion for the **iQOO Innovation Challenge**, turning high-performance mobile silicon into a transformative educational equalizer for students across India.
