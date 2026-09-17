package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography

@Composable
fun HotelHeaderView(
    hotelName: String?,
    modifier: Modifier = Modifier
) {
    if (!hotelName.isNullOrBlank()) {
        Row(
            modifier = modifier
                .background(
                    color = DSColors.brandSoft,
                    shape = RoundedCornerShape(percent = 50)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Apartment,
                contentDescription = "Hotel",
                tint = DSColors.appTint,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = hotelName,
                style = DSTypography.caption.copy(fontWeight = FontWeight.Bold),
                color = DSColors.primaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
