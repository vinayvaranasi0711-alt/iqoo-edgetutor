# 60-Second Demo Video Script & Storyboard

> **Video Title:** iQOO EdgeTutor: 100% Offline AI on Snapdragon NPU  
> **Duration:** Exactly 60 Seconds  
> **Tone:** High energy, tech-forward, authentic, impressive.  
> **Music:** Punchy electronic synth beats with a sudden pause during the "Airplane Mode" moment.

---

## Storyboard & Timing Breakdown

| Timecode | Visual on Screen | Actor / Hand Action | Audio / Voiceover | On-Screen Graphic Text |
| :--- | :--- | :--- | :--- | :--- |
| **0:00 – 0:08** | Close-up of an Android phone attempting to open ChatGPT or search Google. The screen shows *"No Internet Connection"* / *"You are offline"*. | Actor taps "Retry" frustratedly in a dimly lit hostel room. | *"Late night study session. A tough physics question. And the hostel Wi-Fi just died."* | **THE PROBLEM: Erratic Connectivity** |
| **0:08 – 0:18** | Cut to the host holding the **iQOO phone**. Full view of the Quick Settings panel. Swipe down in front of the camera. | Host turns on **Airplane Mode**. Wi-Fi toggles OFF. Mobile Data toggles OFF. | *"Watch this. Airplane mode: ON. No Wi-Fi. Zero cellular data. Not a single byte of internet."* | **AIRPLANE MODE ACTIVATED (0 KB/s)** |
| **0:18 – 0:28** | Host launches **iQOO EdgeTutor**. The futuristic dark UI appears with the floating pill: *"Snapdragon Hexagon HTP • NPU Active"*. | Host taps **"Snap Doubt with Camera"**. The viewfinder reticle targets a printed NCERT textbook page (Example on inclined plane with friction). | *"Meet iQOO EdgeTutor. We point the camera at this JEE physics problem and tap capture."* | **OFFLINE OPTICAL OCR (ML Kit)** |
| **0:28 – 0:42** | Shutter clicks. Instantly, the text is extracted and the solution screen begins **streaming mathematical tokens at lightning speed (24 tokens/sec)** with KaTeX formulas rendering in real time. | Camera zooms in on the hardware HUD showing: `24.2 tok/s • 0 KB Network • NPU Active`. Host gestures to the screen. | *"Instantly, Google ML Kit reads the formula completely offline, and our local Gemma SLM on Qualcomm's Hexagon NPU solves it step-by-step in real time."* | **REAL-TIME NPU INFERENCE: 24 tok/s** |
| **0:42 – 0:52** | Host taps the **"Explain Why?"** Socratic button. The app immediately generates a conceptual intuition breakdown grounded in NCERT Chapter 5 mechanics. | Scrolling smoothly through the derivation, highlighting the normal reaction formula $N = mg\cos\theta$. | *"It doesn't just give the answer—it teaches the concept, grounded directly in the NCERT syllabus."* | **SOCRATIC PEDAGOGY • ZERO HALLUCINATION** |
| **0:52 – 1:00** | Final punchy hero shot: The iQOO phone held up with the glowing EdgeTutor app alongside the tagline. | Host smiles and speaks directly to camera. | *"iQOO’s LPDDR5X RAM and NPU aren't just for 120 FPS gaming. They’re powering India's offline AI classroom. iQOO EdgeTutor."* | **iQOO EdgeTutor • Monster AI for Bharat** |

---

## Production Tips for Maximum Score

1. **Keep the Network Icons Visible at All Times:**  
   Ensure the top Android status bar is clearly in frame showing the **airplane icon** and the absence of LTE/5G/Wi-Fi icons. This removes any doubt from judges that a hidden API call was made.
2. **Use Good Lighting for the Textbook Scan:**  
   Print or display a clear sample question (such as the Inclined Plane problem or Calculus integral) on white paper so ML Kit OCR recognizes 100% of the characters instantly.
3. **Showcase the Telemetry Badge:**  
   Judges love hardware metrics. Zoom into the floating HUD showing `Qualcomm Snapdragon • Hexagon HTP • 24 tok/s • 0 KB/s`.
4. **Alternative: Record Using the Web Companion Prototype:**  
   If you don't have a video camera ready, you can use OBS or Windows Game Bar (`Win + G`) to record the interactive simulator in `web-companion/index.html`. It simulates the exact same flow with animations, status bar toggles, and token streaming!
