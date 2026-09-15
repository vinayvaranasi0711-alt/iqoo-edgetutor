package com.iqoo.edgetutor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.iqoo.edgetutor.engine.NcertTopic
import com.iqoo.edgetutor.engine.OfflineRagStore
import com.iqoo.edgetutor.ui.theme.*

@Composable
fun VaultScreen(
    ragStore: OfflineRagStore,
    initialGrade: String = "All",
    onTopicScan: (String) -> Unit
) {
    val allTopics = remember { ragStore.getAllTopics() }
    var selectedGrade by remember(initialGrade) { mutableStateOf(initialGrade) }
    var searchQuery by remember { mutableStateOf("") }

    val grades = listOf("All", "Class 9", "Class 10", "Class 11", "Class 12")

    val filteredTopics = allTopics.filter { topic ->
        val matchesGrade = (selectedGrade == "All" || topic.grade.contains(selectedGrade, ignoreCase = true))
        val matchesSearch = searchQuery.isBlank() || 
            topic.chapter.contains(searchQuery, ignoreCase = true) ||
            topic.subject.contains(searchQuery, ignoreCase = true) ||
            topic.keywords.any { it.contains(searchQuery, ignoreCase = true) }
        matchesGrade && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(IQOODark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 85.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Curriculum & Materials",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "CBSE 9-10 & JEE 11-12 Syllabus",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(IQOOAmber.copy(alpha = 0.15f))
                        .border(1.dp, IQOOAmber.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "100% OFFLINE",
                        color = IQOOAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Grade Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(grades) { grade ->
                    val isSelected = selectedGrade == grade
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) IQOOAmber else IQOOCardBg)
                            .border(1.dp, if (isSelected) IQOOAmber else IQOOCardBorder, RoundedCornerShape(12.dp))
                            .clickable { selectedGrade = grade }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (grade == "All") "All Grades" else grade,
                            color = if (isSelected) Color.Black else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Search Box
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Search laws, formulas, or chapters...", color = TextMuted, fontSize = 12.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = IQOOAmber,
                    unfocusedBorderColor = IQOOCardBorder,
                    focusedContainerColor = Color(0xFF10121A),
                    unfocusedContainerColor = Color(0xFF10121A)
                )
            )
        }

        // List of Material Cards
        items(filteredTopics) { topic ->
            val sampleQ = when {
                topic.chapter.contains("Light", ignoreCase = true) ->
                    "An object is placed at a distance of 10 cm in front of a concave mirror of focal length 15 cm. Find position, nature, and magnification of image."
                topic.chapter.contains("Motion", ignoreCase = true) ->
                    "A car traveling at 18 km/h accelerates uniformly to 36 km/h in 5 seconds. Calculate the acceleration and total distance covered."
                topic.chapter.contains("Electricity", ignoreCase = true) ->
                    "A wire of length l and cross-section A has resistance R. If its length is doubled by stretching, what will be its new resistance?"
                topic.chapter.contains("Incline", ignoreCase = true) || topic.chapter.contains("Friction", ignoreCase = true) ->
                    "A 2kg block slides down a 30 degree incline with kinetic friction coefficient 0.2. Find the acceleration."
                topic.chapter.contains("Integral", ignoreCase = true) ->
                    "Evaluate the indefinite integral of x * sin(x) dx using integration by parts."
                topic.chapter.contains("Equilibrium", ignoreCase = true) ->
                    "Derive the relation between Kp and Kc for a gaseous reaction. When does Kp equal Kc?"
                else ->
                    "Explain and solve ${topic.chapter} principles with step-by-step NCERT formulas."
            }
            TopicMaterialCard(topic = topic, onScan = { onTopicScan(sampleQ) })
        }
    }
}

@Composable
fun TopicMaterialCard(
    topic: NcertTopic,
    onScan: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(IQOOCardBg)
            .border(1.dp, IQOOCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(IQOOAmber.copy(alpha = 0.15f))
                        .border(1.dp, IQOOAmber.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${topic.exam ?: topic.grade} • ${topic.subject}",
                        color = IQOOAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = topic.grade,
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Text(
                text = topic.chapter,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            if (topic.corePrinciples.isNotEmpty()) {
                Text(
                    text = topic.corePrinciples.first(),
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            if (topic.formulas.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F1118))
                        .border(1.dp, Color(0xFF222634), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        topic.formulas.take(2).forEach { formula ->
                            Text(
                                text = formula,
                                color = IQOOCyan,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (topic.pitfalls.isNotEmpty()) {
                    Text(
                        text = "⚠️ Tip: ${topic.pitfalls.first().take(32)}...",
                        color = IQOOAmber,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = onScan,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IQOOAmber, contentColor = Color.Black),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CropFree,
                        contentDescription = "Scan",
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Scan Topic", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
