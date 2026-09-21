package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography

data class CheckinnItemModel(
    val id: Int,
    val orderNo: String = "1001",
    val name: String,
    val phoneNumber: String,
    val checkinDate: String = "-",
    val checkoutDate: String? = null,
    val roomNo: String? = null,
    val roomType: String? = null,
    val bookingSource: String? = null,
    val idProofTypeName: String = "Aadhaar Card",
    val noOfGuest: Int? = null,
    val status: BookingStatusType
)

@Composable
fun CheckinnsItem(
    checkin: BookingOrder,
    onShowDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    CheckinnsItem(
        item = CheckinnItemModel(
            id = checkin.id,
            orderNo = checkin.orderNo,
            name = checkin.customer?.customerName ?: "Guest",
            phoneNumber = checkin.customer?.customerMobile ?: "-",
            checkinDate = checkin.checkinDate?.take(10) ?: "-",
            checkoutDate = checkin.checkoutDate?.take(10),
            roomNo = checkin.roomNo?.takeIf { it.isNotBlank() },
            roomType = checkin.roomType?.takeIf { it.isNotBlank() },
            bookingSource = checkin.bookingSource?.takeIf { it.isNotBlank() },
            idProofTypeName = checkin.idProofTypeName,
            noOfGuest = checkin.noOfGuest,
            status = checkin.status
        ),
        onShowDetails = onShowDetails,
        modifier = modifier
    )
}

@Composable
fun CheckinnsItem(
    item: CheckinnItemModel,
    onShowDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    DSCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)
        ) {
            // ── Header: avatar + name + status badge ──────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gradient avatar with first-letter initial
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(DSColors.brand, DSColors.indigo)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.name.firstOrNull()?.uppercase() ?: "G",
                        style = DSTypography.caption.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(DS.Spacing.md))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = DSTypography.sectionTitle,
                        color = DSColors.primaryText,
                        maxLines = 1
                    )
                    Text(
                        text = "Order #${item.orderNo}",
                        style = DSTypography.caption,
                        color = DSColors.secondaryText
                    )
                }

                StatusBadge(status = item.status)
            }

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            // ── Info grid ─────────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {

                // Row 1: Phone | ID proof type
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoChip(
                        icon = Icons.Default.Phone,
                        text = item.phoneNumber,
                        iconColor = DSColors.slate,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(DS.Spacing.sm))
                    InfoChip(
                        icon = Icons.Default.Badge,
                        text = item.idProofTypeName,
                        iconColor = DSColors.indigo,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: Check-in date | Checkout date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoChip(
                        icon = Icons.Default.CalendarToday,
                        text = "In: ${item.checkinDate}",
                        iconColor = DSColors.success,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(DS.Spacing.sm))
                    InfoChip(
                        icon = Icons.Default.EventAvailable,
                        text = if (!item.checkoutDate.isNullOrBlank()) "Out: ${item.checkoutDate}" else "Checkout TBD",
                        iconColor = if (!item.checkoutDate.isNullOrBlank()) DSColors.amber else DSColors.slate,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3: Room info | Number of guests
                val hasRoom = !item.roomNo.isNullOrBlank() || !item.roomType.isNullOrBlank()
                val roomText = when {
                    !item.roomNo.isNullOrBlank() && !item.roomType.isNullOrBlank() -> "Room ${item.roomNo} · ${item.roomType}"
                    !item.roomNo.isNullOrBlank() -> "Room ${item.roomNo}"
                    !item.roomType.isNullOrBlank() -> item.roomType!!
                    else -> "Room TBD"
                }
                val guestCount = item.noOfGuest ?: 1
                val guestText = "$guestCount Guest${if (guestCount > 1) "s" else ""}"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoChip(
                        icon = Icons.Default.MeetingRoom,
                        text = roomText,
                        iconColor = if (hasRoom) DSColors.appTint else DSColors.slate,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(DS.Spacing.sm))
                    InfoChip(
                        icon = Icons.Default.Person,
                        text = guestText,
                        iconColor = DSColors.brand,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 4: Booking source (if available)
                if (!item.bookingSource.isNullOrBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoChip(
                            icon = Icons.Default.Bed,
                            text = item.bookingSource,
                            iconColor = DSColors.indigo,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(DS.Spacing.xxs))

            // ── CTA ───────────────────────────────────────────────────────
            DSButton(
                title = "Show Details",
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                variant = DSButtonVariant.Primary,
                action = onShowDetails
            )
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    text: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(DS.Spacing.xs))
        Text(
            text = text,
            style = DSTypography.footnote,
            color = DSColors.secondaryText,
            maxLines = 1
        )
    }
}

// Keep the old InfoRow private so nothing outside breaks if referenced elsewhere
@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String,
    iconColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(DS.Spacing.xs))
        Text(
            text = text,
            style = DSTypography.footnote,
            color = DSColors.secondaryText
        )
    }
}
