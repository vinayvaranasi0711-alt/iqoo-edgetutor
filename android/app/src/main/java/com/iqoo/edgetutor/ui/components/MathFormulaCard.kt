package com.iqoo.edgetutor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.edgetutor.ui.theme.*

@Composable
fun FormattedSolutionContent(
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val lines = content.split("\n")
        var inMathBlock = false
        val mathBlockBuffer = mutableListOf<String>()

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("$$")) {
                if (inMathBlock) {
                    // Close math block
                    MathDisplayBox(mathBlockBuffer.joinToString(" "))
                    mathBlockBuffer.clear()
                    inMathBlock = false
                } else {
                    inMathBlock = true
                    val rest = trimmed.removePrefix("$$").removeSuffix("$$").trim()
                    if (rest.isNotEmpty()) {
                        mathBlockBuffer.add(rest)
                    }
                }
            } else if (inMathBlock) {
                mathBlockBuffer.add(trimmed)
            } else if (trimmed.startsWith("### ")) {
                Text(
                    text = trimmed.removePrefix("### "),
                    color = IQOOAmber,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            } else if (trimmed.startsWith("#### ")) {
                Text(
                    text = trimmed.removePrefix("#### "),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 6.dp)
                )
            } else if (trimmed.startsWith("💡 ") || trimmed.startsWith("⚠️ ")) {
                CalloutBox(text = trimmed)
            } else if (trimmed.isNotEmpty()) {
                val annotated = buildAnnotatedString {
                    val parts = trimmed.split("**")
                    for (i in parts.indices) {
                        if (i % 2 == 1) {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                                append(parts[i])
                            }
                        } else {
                            withStyle(SpanStyle(color = TextSecondary)) {
                                append(parts[i])
                            }
                        }
                    }
                }
                Text(
                    text = annotated,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

@Composable
fun MathDisplayBox(formula: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1B202D))
            .border(1.dp, IQOOCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = formula,
            color = IQOOCyan,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun CalloutBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(IQOOAmber.copy(alpha = 0.1f))
            .border(1.dp, IQOOAmber.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = IQOOAmber,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}
