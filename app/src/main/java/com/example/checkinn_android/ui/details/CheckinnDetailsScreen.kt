package com.example.checkinn_android.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.checkinn_android.R
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography
import com.example.checkinn_android.ui.designsystem.components.DSAlertDialog
import com.example.checkinn_android.ui.designsystem.components.DSButton
import com.example.checkinn_android.ui.designsystem.components.DSButtonVariant
import com.example.checkinn_android.ui.designsystem.components.DSCard
import com.example.checkinn_android.ui.designsystem.components.DSTextField
import com.example.checkinn_android.ui.designsystem.components.StatusTagView
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckinnDetailsScreen(
    booking: BookingOrder,
    viewModel: CheckinnDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    // Prevent screenshots and screen recording on this screen (Disabled)
    // val context = LocalContext.current
    // DisposableEffect(Unit) {
    //     val window = (context as? Activity)?.window
    //     window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    //     onDispose {
    //         window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    //     }
    // }

    LaunchedEffect(booking) {
        viewModel.initBooking(booking)
    }

    LaunchedEffect(true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CheckinnDetailsUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    val state by viewModel.uiState.collectAsState()
    val currentState = state ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Check-in Details",
                        style = DSTypography.sectionTitle,
                        color = DSColors.primaryText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DSColors.primaryText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DSColors.background
                )
            )
        },
        bottomBar = {
            when {
                currentState.showActionButtons -> {
                    BottomActionBar(
                        isProcessing = currentState.isProcessing,
                        onDeny = { viewModel.onEvent(CheckinnDetailsUiEvent.DenyCheckin) },
                        onApprove = { viewModel.onEvent(CheckinnDetailsUiEvent.ApproveCheckin) }
                    )
                }
                currentState.showCheckoutButton -> {
                    CheckoutBottomBar(
                        isProcessing = currentState.isProcessing,
                        onCheckout = { viewModel.onEvent(CheckinnDetailsUiEvent.CheckoutCheckin) }
                    )
                }
            }
        },
        containerColor = DSColors.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DSColors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                HeroImageSection(
                    state = currentState,
                    onEvent = viewModel::onEvent
                )

                Column(
                    modifier = Modifier.padding(
                        start = DS.Spacing.md,
                        top = DS.Spacing.md,
                        end = DS.Spacing.md,
                        bottom = 5.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(DS.Spacing.lg)
                ) {
                    GuestHeaderSection(state = currentState)
                    EditableBookingSection(
                        state = currentState,
                        onEvent = viewModel::onEvent
                    )
                    GuestInfoSection(state = currentState)
                    BookingDetailsSection(state = currentState)

                    currentState.specialRequest?.takeIf { it.isNotBlank() }?.let { request ->
                        SpecialRequestSection(text = request)
                    }
                }
            }

            DSAlertDialog(
                alert = currentState.alert,
                onDismissRequest = { viewModel.onEvent(CheckinnDetailsUiEvent.DismissAlert) }
            )
        }
    }
}

private fun maskIdNumber(idNo: String?): String {
    if (idNo.isNullOrBlank()) return "••••-••••-••••"
    val clean = idNo.trim()
    return if (clean.length > 4) {
        "••••-••••-" + clean.takeLast(4)
    } else {
        "••••-" + clean
    }
}

@Composable
private fun HeroImageSection(
    state: CheckinnDetailsUiState,
    onEvent: (CheckinnDetailsUiEvent) -> Unit
) {
    val isApprovedOrDenied = state.status == BookingStatusType.Approved ||
            state.status == BookingStatusType.Rejected

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(DSColors.surface),
        contentAlignment = Alignment.BottomStart
    ) {
        val activeBitmap = state.activeBitmap
        if (!isApprovedOrDenied && activeBitmap != null) {
            Image(
                bitmap = activeBitmap.asImageBitmap(),
                contentDescription = "Identity Document",
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (state.showFlipButton) Modifier.clickable { onEvent(CheckinnDetailsUiEvent.FlipIdProofImage) }
                        else Modifier
                    ),
                contentScale = ContentScale.Crop
            )
            if (state.showFlipButton) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = DS.Spacing.sm, end = DS.Spacing.sm)
                        .background(Color.Black.copy(alpha = 0.55f), shape = RoundedCornerShape(20.dp))
                        .clickable { onEvent(CheckinnDetailsUiEvent.FlipIdProofImage) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flip,
                            contentDescription = "Flip ID image",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${state.activeImageIndex + 1}/2",
                            style = DSTypography.caption,
                            color = Color.White
                        )
                    }
                }
            }
        } else if (!isApprovedOrDenied && state.isImageLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DSColors.brand)
            }
        } else {
            // Placeholder ID card for Approved, Denied, or missing image using R.drawable.id_card
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.id_card),
                    contentDescription = "ID Card Placeholder",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (isApprovedOrDenied) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.9f),
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(DS.Spacing.xs))
                            Text(
                                text = "ID Masked & Secured",
                                style = DSTypography.title,
                                color = Color.White
                            )
                            Text(
                                text = "${state.idProofTypeName} (Verified)",
                                style = DSTypography.caption,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }
        }

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                    )
                )
        )

        // ID Overlay details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DS.Spacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xxs)) {
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                        .padding(horizontal = DS.Spacing.sm, vertical = DS.Spacing.xxs)
                ) {
                    Text(
                        text = state.idProofTypeName,
                        style = DSTypography.caption,
                        color = Color.White
                    )
                }
                state.idProofNo?.takeIf { it.isNotBlank() }?.let { idNo ->
                    val displayId = if (isApprovedOrDenied) maskIdNumber(idNo) else idNo
                    Text(
                        text = displayId,
                        style = DSTypography.bodyEmphasized,
                        color = Color.White
                    )
                }
            }

            Text(
                text = if (isApprovedOrDenied) "Masked Document" else "Identity Document",
                style = DSTypography.caption,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(DS.Radius.xs))
                    .padding(horizontal = DS.Spacing.xs, vertical = DS.Spacing.xxs)
            )
        }
    }
}

@Composable
private fun GuestHeaderSection(state: CheckinnDetailsUiState) {
    DSCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xxs)) {
                Text(
                    text = state.guestName,
                    style = DSTypography.title,
                    color = DSColors.primaryText
                )
                Text(
                    text = "Order #${state.orderNo}",
                    style = DSTypography.caption,
                    color = DSColors.secondaryText
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                StatusTagView(status = state.status)
                Spacer(modifier = Modifier.height(DS.Spacing.xxs))
                Text(
                    text = state.checkinTimeAgo,
                    style = DSTypography.footnote,
                    color = DSColors.secondaryText
                )
            }
        }
    }
}

@Composable
private fun EditableBookingSection(
    state: CheckinnDetailsUiState,
    onEvent: (CheckinnDetailsUiEvent) -> Unit
) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (state.showActionButtons) Icons.Default.Tune else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (state.showActionButtons) DSColors.appTint else DSColors.secondaryText,
                    modifier = Modifier.size(DS.Icon.sm)
                )
                Spacer(modifier = Modifier.width(DS.Spacing.xs))
                Text(
                    text = if (state.showActionButtons) "Update Check-in Details" else "Check-in Details",
                    style = DSTypography.sectionTitle,
                    color = DSColors.primaryText
                )
            }

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            // Room Number
            DSTextField(
                title = "Room Number",
                placeholder = "Enter room number",
                leadingIcon = Icons.Default.DoorFront,
                value = state.roomNumber,
                onValueChange = { onEvent(CheckinnDetailsUiEvent.RoomNumberChanged(it)) },
                errorMessage = state.roomNumberError
            )

            // Number of Guests Stepper
            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
                Text(
                    text = "Number of Guests",
                    style = DSTypography.caption,
                    color = DSColors.secondaryText
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(DSColors.surface, shape = RoundedCornerShape(DS.Radius.lg))
                        .border(width = DS.Stroke.thin, color = DSColors.separator.copy(alpha = 0.55f), shape = RoundedCornerShape(DS.Radius.lg))
                        .padding(horizontal = DS.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = DSColors.appTint,
                        modifier = Modifier.size(DS.Icon.sm)
                    )
                    Spacer(modifier = Modifier.width(DS.Spacing.sm))

                    Text(
                        text = "${state.numberOfGuests} Guest${if (state.numberOfGuests > 1) "s" else ""}",
                        style = DSTypography.bodyEmphasized,
                        color = DSColors.primaryText,
                        modifier = Modifier.weight(1f)
                    )

                    if (state.showActionButtons) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(DS.Spacing.xs)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(DSColors.fill, shape = RoundedCornerShape(DS.Radius.sm))
                                    .clickable(enabled = state.numberOfGuests > 1) {
                                        onEvent(CheckinnDetailsUiEvent.DecrementGuests)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = if (state.numberOfGuests > 1) DSColors.primaryText else DSColors.disabledText,
                                    modifier = Modifier.size(DS.Icon.sm)
                                )
                            }

                            Text(
                                text = "${state.numberOfGuests}",
                                style = DSTypography.bodyEmphasized,
                                color = DSColors.primaryText
                            )

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(DSColors.fill, shape = RoundedCornerShape(DS.Radius.sm))
                                    .clickable(enabled = state.numberOfGuests < 10) {
                                        onEvent(CheckinnDetailsUiEvent.IncrementGuests)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = if (state.numberOfGuests < 10) DSColors.primaryText else DSColors.disabledText,
                                    modifier = Modifier.size(DS.Icon.sm)
                                )
                            }
                        }
                    }
                }

                state.numberOfGuestsError?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        style = DSTypography.caption,
                        color = DSColors.danger,
                        modifier = Modifier.padding(start = DS.Spacing.xs)
                    )
                }
            }

            // Checkout Date
            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
                Text(
                    text = "Checkout Date",
                    style = DSTypography.caption,
                    color = DSColors.secondaryText
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(DSColors.surface, shape = RoundedCornerShape(DS.Radius.lg))
                        .border(width = DS.Stroke.thin, color = DSColors.separator.copy(alpha = 0.55f), shape = RoundedCornerShape(DS.Radius.lg))
                        .padding(horizontal = DS.Spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = DSColors.appTint,
                        modifier = Modifier.size(DS.Icon.sm)
                    )
                    Spacer(modifier = Modifier.width(DS.Spacing.sm))

                    Text(
                        text = state.checkoutDate.ifBlank { state.checkinDateFormatted },
                        style = DSTypography.bodyEmphasized,
                        color = DSColors.primaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun GuestInfoSection(state: CheckinnDetailsUiState) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = DSColors.appTint,
                    modifier = Modifier.size(DS.Icon.sm)
                )
                Spacer(modifier = Modifier.width(DS.Spacing.xs))
                Text(
                    text = "Guest Contact & ID",
                    style = DSTypography.sectionTitle,
                    color = DSColors.primaryText
                )
            }

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                DetailRow(icon = Icons.Default.Phone, title = "Mobile", value = state.phoneNumber)
                if (state.email.isNotBlank() && state.email != "-") {
                    DetailRow(icon = Icons.Default.Email, title = "Email", value = state.email)
                }
                state.idProofNo?.takeIf { it.isNotBlank() }?.let { idNo ->
                    val isApprovedOrDenied = state.status == BookingStatusType.Approved ||
                            state.status == BookingStatusType.Rejected
                    val displayId = if (isApprovedOrDenied) maskIdNumber(idNo) else idNo
                    DetailRow(icon = Icons.Default.CreditCard, title = state.idProofTypeName, value = displayId)
                }
                if (state.address.isNotBlank() && state.address != "-") {
                    AddressDetailBlock(icon = Icons.Default.LocationOn, title = "Address", value = state.address)
                }
                if (state.city.isNotBlank() && state.city != "-") {
                    DetailRow(icon = Icons.Default.LocationCity, title = "City & Residency", value = "${state.city} • ${state.residentStatus}")
                }
            }
        }
    }
}

@Composable
private fun BookingDetailsSection(state: CheckinnDetailsUiState) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bed,
                    contentDescription = null,
                    tint = DSColors.appTint,
                    modifier = Modifier.size(DS.Icon.sm)
                )
                Spacer(modifier = Modifier.width(DS.Spacing.xs))
                Text(
                    text = "Stay Information",
                    style = DSTypography.sectionTitle,
                    color = DSColors.primaryText
                )
            }

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                DetailRow(icon = Icons.Default.CalendarMonth, title = "Check-in Date", value = state.checkinDateFormatted)
                DetailRow(icon = Icons.Default.Person, title = "Guests", value = "${state.numberOfGuests} Guest${if (state.numberOfGuests > 1) "s" else ""}")
                DetailRow(icon = Icons.Default.MeetingRoom, title = "Source", value = state.bookingSource)
                DetailRow(icon = Icons.Default.LocationCity, title = "Floor", value = state.roomFloor)
                state.externalBookingId?.takeIf { it.isNotBlank() }?.let { extId ->
                    DetailRow(icon = Icons.Default.Tag, title = "External ID", value = extId)
                }
            }
        }
    }
}

@Composable
private fun SpecialRequestSection(text: String) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = DSColors.amber,
                    modifier = Modifier.size(DS.Icon.sm)
                )
                Spacer(modifier = Modifier.width(DS.Spacing.xs))
                Text(
                    text = "Special Request",
                    style = DSTypography.sectionTitle,
                    color = DSColors.primaryText
                )
            }

            Text(
                text = text,
                style = DSTypography.body,
                color = DSColors.secondaryText,
                modifier = Modifier.padding(top = DS.Spacing.xxs)
            )
        }
    }
}

@Composable
private fun AddressDetailBlock(
    icon: ImageVector,
    title: String,
    value: String
) {

    fun String.normalizeWhitespace(): String {
        return this.replace(Regex("\\s+"), " ").trim()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = DSColors.slate,
            modifier = Modifier
                .size(DS.Icon.sm)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(DS.Spacing.sm))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(DS.Spacing.xxs)
        ) {
            Text(
                text = title,
                style = DSTypography.body,
                color = DSColors.secondaryText
            )
            Text(
                text = value.normalizeWhitespace(),
                style = DSTypography.bodyEmphasized,
                color = DSColors.primaryText,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = DS.Spacing.md)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DSColors.slate,
                modifier = Modifier.size(DS.Icon.sm)
            )
            Spacer(modifier = Modifier.width(DS.Spacing.sm))
            Text(
                text = title,
                style = DSTypography.body,
                color = DSColors.secondaryText
            )
        }

        Text(
            text = value,
            style = DSTypography.bodyEmphasized,
            color = DSColors.primaryText,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

@Composable
private fun BottomActionBar(
    isProcessing: Boolean,
    onDeny: () -> Unit,
    onApprove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DSColors.background)
            .navigationBarsPadding()
            .padding(DS.Spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DS.Spacing.md)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                DSButton(
                    title = "Deny",
                    icon = Icons.Default.Close,
                    variant = DSButtonVariant.Destructive,
                    isLoading = isProcessing,
                    isDisabled = isProcessing,
                    action = onDeny
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                DSButton(
                    title = "Approve",
                    icon = Icons.Default.CheckCircle,
                    variant = DSButtonVariant.Primary,
                    isLoading = isProcessing,
                    isDisabled = isProcessing,
                    action = onApprove
                )
            }
        }
    }
}

@Composable
private fun CheckoutBottomBar(
    isProcessing: Boolean,
    onCheckout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DSColors.background)
            .navigationBarsPadding()
            .padding(DS.Spacing.md)
    ) {
        DSButton(
            title = "Checkout",
            icon = Icons.Default.ExitToApp,
            variant = DSButtonVariant.Primary,
            isLoading = isProcessing,
            isDisabled = isProcessing,
            action = onCheckout
        )
    }
}
