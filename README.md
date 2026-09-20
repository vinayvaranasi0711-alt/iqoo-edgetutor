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

* **The Problem:** Students preparing for board exams (CBSE 9–10) and competitive tests (JEE) face three major barriers:
  1. **Connectivity Drops:** Cloud-dependent AI apps (like ChatGPT) fail or freeze in college hostels, small towns, rural areas, or during train commutes.
  2. **The Distraction Trap:** Opening a browser or online app to clear a doubt exposes students to social notifications (Instagram, WhatsApp, YouTube), shattering deep focus.
  3. **Answer-Dumping vs. True Learning:** Most AI tools simply vomit the final answer, encouraging passive copying rather than teaching the derivation.
* **The Concept:** An educational AI tutor that runs **entirely offline on the smartphone’s local silicon**, utilizing on-device OCR, an embedded NCERT curriculum vault, and Small Language Models (SLMs) to deliver interactive Socratic guidance.
* **How It Uses iQOO Hardware:** iQOO smartphones feature flagship **Snapdragon processors**, **high-bandwidth LPDDR5X RAM (up to 77 GB/s)**, and **large vapor chamber cooling**. These capabilities provide the ideal platform for running local, memory-bandwidth-intensive edge AI inference without thermal throttling.
* **The "Airplane Mode" Live Demonstration:** Put the smartphone into physical Airplane Mode, snap a photo of a textbook problem, and watch the local pipeline parse the question and guide the student through step-by-step reasoning with **zero internet connectivity**.

---

## 🏗️ Technical Architecture & Pipeline

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
 │ CBSE 9-10 & JEE 11-12   │   │     Stage 2: On-Device Semantic RAG     │
 │ Core Curriculum Vault   │──▶│   - In-memory structured knowledge base │
 │ Pre-indexed JSON Store  │   │   - Grounds reasoning in NCERT syllabus │
 └─────────────────────────┘   └───────────────────┬─────────────────────┘
                                                   │ Grounded Context
                                                   ▼
                       ┌─────────────────────────────────────────────────┐
                       │    Stage 3: On-Device SLM Inference Engine      │
                       │   - Model Target: Gemma 2B / Phi-3.5 INT4       │
                       │   - Target Runtime: Qualcomm Hexagon NPU / QNN  │
                       │   - Memory Bus: LPDDR5X High-Bandwidth Pipeline │
                       └───────────────────────┬─────────────────────────┘
                                               │ Guided Reasoning Stream
                                               ▼
                       ┌─────────────────────────────────────────────────┐
                       │           Interactive Socratic UI               │
                       │   - Step-by-Step Breakdown & Socratic Dialogue  │
                       │   - KaTeX Math Formula Rendering                │
                       │   - Real-time Airplane Mode / Air-Gapped HUD    │
                       └─────────────────────────────────────────────────┘
```

---

## 📊 Implementation Status & Validation Matrix

To maintain transparent engineering credibility, here is the exact separation between our **working demonstrated prototype** and our **target production hardware deployment**:

| Component | Current Demonstrated Prototype | Target Production Deployment | Validation Method |
| :--- | :--- | :--- | :--- |
| **Network Status** | **0 KB (Air-Gapped)** | **0 KB (Air-Gapped)** | `android.permission.INTERNET` omitted from Manifest; tested in Airplane Mode |
| **Camera & Vision** | CameraX + Google ML Kit (Offline) | CameraX + Google ML Kit (Offline) | Functional on device & web simulator |
| **Curriculum Vault** | CBSE 9–10 & JEE 11–12 Core Vault (`ncert_knowledge_base.json`) | Complete K-12 Vector Embedding Store | Local JSON asset parsed in-memory |
| **SLM Inference** | Quantized Socratic Pipeline Emulator | Qualcomm AI Engine Direct (QNN) / MediaPipe | On-device pipeline & interactive web runner |
| **Target Silicon** | Android 8.0+ (API 26+) Baseline | Snapdragon 8 Gen 3 / Dimensity 9300 | Android Gradle build + Compose UI |
| **Inference Speed** | Interactive Prototype Stream | **Target: $\ge$ 24 tokens/sec** | Hardware validation pending on target testbench |
| **Pedagogical Flow** | Step-by-Step + Interactive Socratic Dialogue | Multi-Turn Adaptive Voice Socratic Dialog | Evaluated on physics/math benchmark doubts |

---

## 🔒 Strict Air-Gapped Offline Architecture

In `android/app/src/main/AndroidManifest.xml`, **`android.permission.INTERNET` is deliberately omitted**. This ensures:
1. **Verifiable Air-Gap:** Any judge or reviewer inspecting the APK manifest can verify zero network permissions.
2. **Student Privacy:** Questions and textbook photos are processed in memory and never leave the device.
3. **Deterministic Performance:** Zero network jitter, zero reliance on external cloud servers, and zero subscription costs.

---

## ⚡ Hardware Synergy: Why iQOO Devices Fit Edge AI

Autoregressive token generation is heavily **memory-bandwidth bound**. Under a simplified theoretical upper-bound model ($T_{\text{max}} \approx B_{\text{mem}} / M_{\text{weights}}$), high memory bandwidth is essential for responsive on-device reasoning:

| Architectural Metric | Standard Mid-Range Silicon | Flagship iQOO Hardware (e.g., iQOO 12 / Neo 9 Pro) | Relevance to On-Device Education AI |
| :--- | :--- | :--- | :--- |
| **Memory Bandwidth** | LPDDR4X (~17 – 34 GB/s) | **LPDDR5X (Up to 77 GB/s)** | High memory throughput is required to sustain smooth token streaming for Small Language Models. |
| **Dedicated NPU** | Basic or Shared DSP (~4–10 TOPS) | **Qualcomm Hexagon HTP (Up to 73 TOPS)** | Offloads INT4 quantized matrix multiplication at sub-watt power efficiency. |
| **RAM Capacity** | 4GB – 6GB | **8GB – 16GB** | Ensures a 2B–3B quantized model fits resident in RAM alongside Android OS and UI. |
| **Thermal Dissipation** | Basic graphite film | **6000mm² Vapor Chamber System** | Engineered for long gaming sessions, preventing thermal throttling during extended study hours. |

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
│           │   └── ncert_knowledge_base.json # Pre-loaded CBSE 9-10 & JEE 11-12 Core Curriculum Vault
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
* Working **Airplane Mode switch** in the status bar with real-time UI reaction.
* Optical camera viewfinder with sample CBSE & JEE problems (Physics, Math, Chemistry).
* Live **Socratic guidance** with KaTeX mathematical formula rendering.
* **Interactive Socratic Dialogue Mode** simulating student-mentor guiding turns.
* **Hardware Architecture Target HUD** highlighting `Target: ≥24 tok/s`, `0 KB Air-Gapped`, and `Hexagon INT4 Target`.

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
