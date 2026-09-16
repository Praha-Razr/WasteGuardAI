package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary

@Composable
fun DemoNoticeBanner(currentLanguage: AppLanguage) {
    Text(
        text = "🛡️ " + StringsDictionary.get("prototype_notice", currentLanguage),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFEF3C7))
            .padding(vertical = 4.dp, horizontal = 12.dp),
        color = Color(0xFF92400E),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}
