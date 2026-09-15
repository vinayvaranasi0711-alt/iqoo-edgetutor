import os
import shutil
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE

def create_deck():
    prs = Presentation()
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    blank_layout = prs.slide_layouts[6]

    # Colors
    BG_COLOR = RGBColor(8, 10, 16)
    CARD_BG = RGBColor(16, 20, 30)
    CARD_BORDER = RGBColor(38, 45, 65)
    IQOO_AMBER = RGBColor(247, 181, 0)
    IQOO_ORANGE = RGBColor(255, 62, 0)
    IQOO_CYAN = RGBColor(0, 229, 255)
    IQOO_GREEN = RGBColor(0, 230, 118)
    WHITE = RGBColor(255, 255, 255)
    GRAY_TEXT = RGBColor(165, 175, 195)
    ACCENT_RED = RGBColor(255, 23, 68)

    def set_slide_background(slide):
        bg = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
        bg.fill.solid()
        bg.fill.fore_color.rgb = BG_COLOR
        bg.line.color.rgb = BG_COLOR
        return bg

    def add_card(slide, left, top, width, height, bg_color=CARD_BG, border_color=CARD_BORDER):
        card = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
        card.fill.solid()
        card.fill.fore_color.rgb = bg_color
        card.line.color.rgb = border_color
        card.line.width = Pt(1.5)
        return card

    def add_header(slide, title_text, category_text="iQOO EDGETUTOR • 100% ON-DEVICE AI"):
        cat_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.4), Inches(11.7), Inches(0.35))
        tf_c = cat_box.text_frame
        tf_c.word_wrap = True
        p_c = tf_c.paragraphs[0]
        p_c.text = category_text.upper()
        p_c.font.size = Pt(11)
        p_c.font.bold = True
        p_c.font.color.rgb = IQOO_AMBER

        title_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.75), Inches(11.7), Inches(0.7))
        tf_t = title_box.text_frame
        tf_t.word_wrap = True
        p_t = tf_t.paragraphs[0]
        p_t.text = title_text
        p_t.font.size = Pt(26)
        p_t.font.bold = True
        p_t.font.color.rgb = WHITE

    # ==========================================
    # SLIDE 1: COVER / TITLE
    # ==========================================
    s1 = prs.slides.add_slide(blank_layout)
    set_slide_background(s1)

    # Accent decorative top bar
    bar = s1.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0.8), Inches(0.7), Inches(2.2), Inches(0.08))
    bar.fill.solid()
    bar.fill.fore_color.rgb = IQOO_AMBER
    bar.line.fill.background()

    # Main Title Box
    tbox = s1.shapes.add_textbox(Inches(0.8), Inches(1.0), Inches(11.7), Inches(2.2))
    tf = tbox.text_frame
    tf.word_wrap = True

    p0 = tf.paragraphs[0]
    p0.text = "⚡ iQOO EdgeTutor"
    p0.font.size = Pt(44)
    p0.font.bold = True
    p0.font.color.rgb = WHITE

    p1 = tf.add_paragraph()
    p1.text = "100% Offline, On-Device AI Educational Mentor in Airplane Mode"
    p1.font.size = Pt(22)
    p1.font.bold = True
    p1.font.color.rgb = IQOO_AMBER
    p1.space_before = Pt(8)

    p2 = tf.add_paragraph()
    p2.text = "Turning iQOO Gaming Silicon into India's Most Powerful Distraction-Free AI Study Station"
    p2.font.size = Pt(14)
    p2.font.color.rgb = GRAY_TEXT
    p2.space_before = Pt(6)

    # 3 Key Highlights Cards
    card1 = add_card(s1, Inches(0.8), Inches(3.5), Inches(3.6), Inches(2.4))
    tf1 = card1.text_frame
    tf1.word_wrap = True
    p = tf1.paragraphs[0]
    p.text = "✈️ Pure Airplane Mode"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = IQOO_GREEN
    p_b = tf1.add_paragraph()
    p_b.text = "• Zero internet permission (0 KB data)\n• No Instagram / WhatsApp notifications\n• 100% private: Doubts never leave phone"
    p_b.font.size = Pt(12)
    p_b.font.color.rgb = WHITE
    p_b.space_before = Pt(8)

    card2 = add_card(s1, Inches(4.8), Inches(3.5), Inches(3.6), Inches(2.4))
    tf2 = card2.text_frame
    tf2.word_wrap = True
    p = tf2.paragraphs[0]
    p.text = "⚡ Sub-Second Speed"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = IQOO_AMBER
    p_b = tf2.add_paragraph()
    p_b.text = "• Snapdragon Hexagon NPU offload\n• 77 GB/s LPDDR5X RAM bandwidth\n• Socratic steps in < 800ms vs 8s cloud"
    p_b.font.size = Pt(12)
    p_b.font.color.rgb = WHITE
    p_b.space_before = Pt(8)

    card3 = add_card(s1, Inches(8.8), Inches(3.5), Inches(3.6), Inches(2.4))
    tf3 = card3.text_frame
    tf3.word_wrap = True
    p = tf3.paragraphs[0]
    p.text = "📚 CBSE & JEE Aligned"
    p.font.size = Pt(16)
    p.font.bold = True
    p.font.color.rgb = IQOO_CYAN
    p_b = tf3.add_paragraph()
    p_b.text = "• Class 9-10 CBSE & Class 11-12 JEE\n• Pre-indexed NCERT offline knowledge vault\n• Free forever for students (₹0 cloud bills)"
    p_b.font.size = Pt(12)
    p_b.font.color.rgb = WHITE
    p_b.space_before = Pt(8)

    # Footer presenter box
    fbox = s1.shapes.add_textbox(Inches(0.8), Inches(6.2), Inches(11.7), Inches(0.8))
    tff = fbox.text_frame
    pf = tff.paragraphs[0]
    pf.text = "👤 Presenter: VARANASI SAI VINAY   |   🔗 GitHub: github.com/vinayvaranasi0711-alt/iqoo-edgetutor"
    pf.font.size = Pt(12)
    pf.font.bold = True
    pf.font.color.rgb = GRAY_TEXT

    # ==========================================
    # SLIDE 2: THE PROBLEM
    # ==========================================
    s2 = prs.slides.add_slide(blank_layout)
    set_slide_background(s2)
    add_header(s2, "The Real-World Problem: Why Online EdTech Fails Students", "01 / PROBLEM STATEMENT")

    p_cards = [
        ("📱 The Distraction Trap", "When students go online to clear doubts, social notifications (Instagram, WhatsApp, YouTube) shatter their concentration. Deep study requires isolation, but cloud AI demands an active internet connection.", ACCENT_RED),
        ("📶 Connectivity & Coverage Barrier", "Over 300M students in Tier-2/3 cities, rural villages, hostel rooms, or train commutes face unreliable or zero internet. During exams or late-night study, cloud apps completely freeze or fail.", IQOO_ORANGE),
        ("💰 Prohibitive Server & Subscription Costs", "Cloud AI services charge ₹1,000–₹2,000/month or incur massive API token bills for manufacturers. EdTech companies cannot sustain free unlimited AI queries for millions of students without bleeding money.", IQOO_AMBER)
    ]

    for i, (title, desc, col) in enumerate(p_cards):
        top_pos = Inches(1.7 + i * 1.7)
        c = add_card(s2, Inches(0.8), top_pos, Inches(11.7), Inches(1.45))
        tfc = c.text_frame
        tfc.word_wrap = True
        pt = tfc.paragraphs[0]
        pt.text = title
        pt.font.size = Pt(16)
        pt.font.bold = True
        pt.font.color.rgb = col
        pd = tfc.add_paragraph()
        pd.text = desc
        pd.font.size = Pt(12)
        pd.font.color.rgb = WHITE
        pd.space_before = Pt(4)

    # ==========================================
    # SLIDE 3: THE EDGETUTOR SOLUTION
    # ==========================================
    s3 = prs.slides.add_slide(blank_layout)
    set_slide_background(s3)
    add_header(s3, "The EdgeTutor Paradigm: Zero-Cloud, 100% On-Device AI", "02 / THE SOLUTION")

    sol_cards = [
        ("✈️ 100% Airplane Mode Ready", "The app operates with strictly zero internet permissions in AndroidManifest.xml. It guarantees 0 KB network consumption, ensuring pure focus and complete data privacy.", IQOO_GREEN),
        ("⚡ Sub-Second Socratic Steps", "Instead of dumping whole answers, EdgeTutor acts as an interactive coach. It gives step-by-step guidance, formula definitions, and exam traps in under 800 milliseconds.", IQOO_AMBER),
        ("📚 Built-in Indian Curriculum Vault", "Class 9-10 CBSE Science & Math, plus Class 11-12 JEE Physics, Chemistry, and Calculus pre-indexed into a lightweight local knowledge base with instant search.", IQOO_CYAN),
        ("🆓 Free Forever For Everyone", "Because compute runs 100% locally on the student's iQOO device, iQOO pays ₹0 in cloud server infrastructure, and students get free, unlimited offline tutoring for life.", WHITE)
    ]

    for i, (title, desc, col) in enumerate(sol_cards):
        col_idx = i % 2
        row_idx = i // 2
        left_pos = Inches(0.8 + col_idx * 6.0)
        top_pos = Inches(1.8 + row_idx * 2.5)
        c = add_card(s3, left_pos, top_pos, Inches(5.7), Inches(2.2))
        tfc = c.text_frame
        tfc.word_wrap = True
        pt = tfc.paragraphs[0]
        pt.text = title
        pt.font.size = Pt(16)
        pt.font.bold = True
        pt.font.color.rgb = col
        pd = tfc.add_paragraph()
        pd.text = desc
        pd.font.size = Pt(12)
        pd.font.color.rgb = GRAY_TEXT
        pd.space_before = Pt(6)

    # ==========================================
    # SLIDE 4: HARDWARE SYNERGY (WHY iQOO?)
    # ==========================================
    s4 = prs.slides.add_slide(blank_layout)
    set_slide_background(s4)
    add_header(s4, "Hardware Synergy: Unlocking iQOO Gaming Silicon for AI", "03 / HARDWARE ARCHITECTURE")

    hw_cards = [
        ("Qualcomm Hexagon NPU", "Dedicated Neural Processing", "INT4 / INT8 quantized neural acceleration runs LLM tokens locally with ultra-low 0.8W power draw. Battery lasts all day.", IQOO_CYAN),
        ("77 GB/s LPDDR5X RAM", "Overcoming the Memory Wall", "LLM inference is memory-bandwidth bound. iQOO's blazing 77 GB/s bus ensures instant token generation (24+ tok/s) without lag.", IQOO_AMBER),
        ("6000mm² Vapor Chamber", "Zero Thermal Throttling", "Built for intense BGMI gaming, the aircraft-grade VC heat dissipation keeps the NPU cool during heavy student study sessions.", IQOO_GREEN)
    ]

    for i, (title, sub, desc, col) in enumerate(hw_cards):
        left_pos = Inches(0.8 + i * 4.0)
        c = add_card(s4, left_pos, Inches(1.8), Inches(3.7), Inches(4.8))
        tfc = c.text_frame
        tfc.word_wrap = True
        pt = tfc.paragraphs[0]
        pt.text = title
        pt.font.size = Pt(17)
        pt.font.bold = True
        pt.font.color.rgb = col
        
        ps = tfc.add_paragraph()
        ps.text = sub.upper()
        ps.font.size = Pt(10)
        ps.font.bold = True
        ps.font.color.rgb = GRAY_TEXT
        ps.space_before = Pt(4)

        pd = tfc.add_paragraph()
        pd.text = desc
        pd.font.size = Pt(12)
        pd.font.color.rgb = WHITE
        pd.space_before = Pt(14)

    # ==========================================
    # SLIDE 5: SYSTEM PIPELINE
    # ==========================================
    s5 = prs.slides.add_slide(blank_layout)
    set_slide_background(s5)
    add_header(s5, "End-to-End On-Device Pipeline (No Server Required)", "04 / TECHNICAL PIPELINE")

    steps = [
        ("Step 1: Vision", "CameraX / Gallery\nOn-device Optical Scanner\nZero network calls", IQOO_ORANGE),
        ("Step 2: OCR", "Google ML Kit (Offline)\nLocal text extraction\nFormula & symbol parsing", IQOO_AMBER),
        ("Step 3: RAG Store", "NCERT Knowledge Vault\nLocal embedding search\nCBSE 9-10 & JEE syllabus", IQOO_CYAN),
        ("Step 4: NPU Engine", "Snapdragon Hexagon\nLocal Socratic reasoning\n< 800ms step-by-step steps", IQOO_GREEN)
    ]

    for i, (title, desc, col) in enumerate(steps):
        left_pos = Inches(0.8 + i * 3.0)
        c = add_card(s5, left_pos, Inches(2.0), Inches(2.8), Inches(4.3))
        tfc = c.text_frame
        tfc.word_wrap = True
        pt = tfc.paragraphs[0]
        pt.text = title
        pt.font.size = Pt(16)
        pt.font.bold = True
        pt.font.color.rgb = col
        pd = tfc.add_paragraph()
        pd.text = desc
        pd.font.size = Pt(12)
        pd.font.color.rgb = WHITE
        pd.space_before = Pt(14)

    # ==========================================
    # SLIDE 6: COMPETITIVE MATRIX
    # ==========================================
    s6 = prs.slides.add_slide(blank_layout)
    set_slide_background(s6)
    add_header(s6, "Competitive Matrix: Why EdgeTutor Wins for Indian Students", "05 / BENCHMARKS & COMPARISON")

    # Add Table
    rows, cols = 6, 5
    left = Inches(0.8)
    top = Inches(1.8)
    width = Inches(11.7)
    height = Inches(4.8)
    table_shape = s6.shapes.add_table(rows, cols, left, top, width, height)
    table = table_shape.table

    headers = ["Feature", "iQOO EdgeTutor", "ChatGPT Plus", "Google Lens", "Doubtnut / Vedantu"]
    data = [
        ["Internet Required?", "❌ ZERO (Airplane Mode)", "✅ Mandatory High Speed", "✅ Mandatory High Speed", "✅ Mandatory Active Data"],
        ["Inference Location", "📱 On-Device NPU", "☁️ US Cloud Servers", "☁️ Google Cloud Vision", "☁️ Commercial Servers"],
        ["Response Latency", "⚡ < 800 ms (Instant)", "⏳ 5 - 10 Seconds", "⏳ 3 - 6 Seconds", "⏳ 8 - 15 Seconds"],
        ["Student Distraction", "🛡️ Zero (Offline Focus)", "⚠️ High (Browser/Apps)", "⚠️ Moderate", "⚠️ Ads & Notifications"],
        ["Cost to Student", "🆓 100% Free Forever", "💰 ₹1,999 / Month", "🆓 Ad-Supported", "💰 ₹500 - ₹2,000 / Mo"]
    ]

    for c_idx, h in enumerate(headers):
        cell = table.cell(0, c_idx)
        cell.text = h
        cell.fill.solid()
        cell.fill.fore_color.rgb = CARD_BG
        p = cell.text_frame.paragraphs[0]
        p.font.size = Pt(13)
        p.font.bold = True
        p.font.color.rgb = IQOO_AMBER if c_idx == 1 else WHITE

    for r_idx, row_data in enumerate(data):
        for c_idx, val in enumerate(row_data):
            cell = table.cell(r_idx + 1, c_idx)
            cell.text = val
            cell.fill.solid()
            cell.fill.fore_color.rgb = RGBColor(12, 15, 24)
            p = cell.text_frame.paragraphs[0]
            p.font.size = Pt(11)
            p.font.bold = (c_idx == 1 or c_idx == 0)
            if c_idx == 1:
                p.font.color.rgb = IQOO_GREEN
            elif "❌" in val or "💰" in val or "⚠️" in val:
                p.font.color.rgb = GRAY_TEXT
            else:
                p.font.color.rgb = WHITE

    # ==========================================
    # SLIDE 7: LIVE DEMO & ROADMAP
    # ==========================================
    s7 = prs.slides.add_slide(blank_layout)
    set_slide_background(s7)
    add_header(s7, "60-Second Live Stage Demo & Future Roadmap", "06 / SUBMISSION & ROADMAP")

    # Left Card: Live Demo Steps
    c_demo = add_card(s7, Inches(0.8), Inches(1.8), Inches(5.6), Inches(4.8))
    tfd = c_demo.text_frame
    tfd.word_wrap = True
    pt = tfd.paragraphs[0]
    pt.text = "🎬 The 60-Second 'Airplane Mode' Wow Factor"
    pt.font.size = Pt(16)
    pt.font.bold = True
    pt.font.color.rgb = IQOO_AMBER
    
    steps_txt = (
        "1. 0:00-0:10 | The Proof:\n"
        "   Swipe down status bar, turn on Airplane Mode (0 KB/s badge turns green).\n\n"
        "2. 0:10-0:30 | The Scan:\n"
        "   Point camera at complex textbook physics question (e.g. Incline Friction).\n\n"
        "3. 0:30-0:50 | The NPU Magic:\n"
        "   Instant step-by-step Socratic breakdown rendered with real KaTeX formulas.\n\n"
        "4. 0:50-1:00 | The Impact:\n"
        "   Browse CBSE 9-10 & JEE materials vault with zero buffering anywhere."
    )
    pd = tfd.add_paragraph()
    pd.text = steps_txt
    pd.font.size = Pt(11)
    pd.font.color.rgb = WHITE
    pd.space_before = Pt(8)

    # Right Card: Roadmap
    c_road = add_card(s7, Inches(6.8), Inches(1.8), Inches(5.7), Inches(4.8))
    tfr = c_road.text_frame
    tfr.word_wrap = True
    ptr = tfr.paragraphs[0]
    ptr.text = "🚀 Future Roadmap & Scaling"
    ptr.font.size = Pt(16)
    ptr.font.bold = True
    ptr.font.color.rgb = IQOO_CYAN

    road_txt = (
        "🌐 Vernacular Voice Mentor (Q3 2026):\n"
        "   Integrate offline on-device Whisper models for voice doubt queries in Telugu, Hindi, and Tamil.\n\n"
        "📐 Interactive 3D AR Models (Q4 2026):\n"
        "   Leverage Adreno GPU to render interactive 3D physics experiments (free-body diagrams, optics lenses).\n\n"
        "🤝 Pre-Loaded iQOO System Feature:\n"
        "   Opportunity to ship EdgeTutor as a default Monster Feature on iQOO Z & Neo series smartphones across India."
    )
    pdr = tfr.add_paragraph()
    pdr.text = road_txt
    pdr.font.size = Pt(11)
    pdr.font.color.rgb = WHITE
    pdr.space_before = Pt(8)

    # Save presentation
    output_path = r"c:\Users\varanasi saivinay\OneDrive\private\IQ\submission\iQOO_EdgeTutor_PitchDeck.pptx"
    prs.save(output_path)
    print(f"Pitch deck saved to: {output_path}")

    # Copy to user's downloads folder for immediate access
    downloads_path = os.path.expanduser(r"~\Downloads\iQOO_EdgeTutor_PitchDeck.pptx")
    try:
        shutil.copy2(output_path, downloads_path)
        print(f"Copied pitch deck to: {downloads_path}")
    except Exception as e:
        print(f"Could not copy to Downloads: {e}")

if __name__ == "__main__":
    create_deck()
