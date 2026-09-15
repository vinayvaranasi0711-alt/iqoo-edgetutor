package com.iqoo.edgetutor.engine

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.InputStreamReader

data class NcertDatabase(
    val version: String,
    val topics: List<NcertTopic>
)

data class NcertTopic(
    val id: String,
    val grade: String,
    val subject: String,
    val chapter: String,
    val exam: String? = null,
    val keywords: List<String>,
    @SerializedName("core_principles") val corePrinciples: List<String>,
    val pitfalls: List<String>,
    val formulas: List<String>
)

class OfflineRagStore(private val context: Context) {

    private var database: NcertDatabase? = null

    init {
        loadKnowledgeBase()
    }

    private fun loadKnowledgeBase() {
        try {
            context.assets.open("ncert_knowledge_base.json").use { stream ->
                val reader = InputStreamReader(stream)
                database = Gson().fromJson(reader, NcertDatabase::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllTopics(): List<NcertTopic> {
        return database?.topics ?: emptyList()
    }

    /**
     * Retrieve relevant NCERT curriculum principles and formulas matching question keywords
     */
    fun findRelevantContext(question: String): NcertTopic? {
        val db = database ?: return null
        val lowerQ = question.lowercase()

        var bestTopic: NcertTopic? = null
        var maxScore = 0

        for (topic in db.topics) {
            var score = 0
            for (kw in topic.keywords) {
                if (lowerQ.contains(kw.lowercase())) {
                    score += 2
                }
            }
            if (lowerQ.contains(topic.subject.lowercase())) score += 1
            if (lowerQ.contains(topic.chapter.lowercase())) score += 3

            if (score > maxScore) {
                maxScore = score
                bestTopic = topic
            }
        }

        return if (maxScore > 0) bestTopic else db.topics.firstOrNull()
    }
}
