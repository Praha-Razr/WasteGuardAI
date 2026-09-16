package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RiskCritical
import com.example.ui.theme.RiskHigh
import com.example.ui.theme.RiskLow
import com.example.ui.theme.RiskMedium

@Composable
fun RiskBadge(
    riskLevel: String,
    score: Int? = null,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (riskLevel.uppercase()) {
        "CRITICAL" -> Triple(RiskCritical.copy(alpha = 0.15f), RiskCritical, "🔴 CRITICAL")
        "HIGH" -> Triple(RiskHigh.copy(alpha = 0.15f), RiskHigh, "🟠 HIGH")
        "MEDIUM" -> Triple(RiskMedium.copy(alpha = 0.15f), RiskMedium, "🟡 MEDIUM")
        else -> Triple(RiskLow.copy(alpha = 0.15f), RiskLow, "🟢 LOW")
    }

    val text = if (score != null) "$label ($score)" else label

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
