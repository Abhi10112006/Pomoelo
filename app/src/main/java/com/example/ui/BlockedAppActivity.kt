package com.example.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.MainActivity
import com.example.service.AppBlockerManager
import com.example.service.AppLimitNotificationHelper
import com.example.service.AppUsageLimitManager
import com.example.service.SettingsManager
import com.example.service.TimerManager
import com.example.ui.components.PaperTextureOverlay
import com.example.ui.components.PomoButton
import com.example.ui.components.pomoShadow
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.MonospaceFontFamily
import com.example.ui.theme.PomoMotion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BlockedAppActivity : ComponentActivity() {

    companion object {
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
        const val EXTRA_PREVIEW_MODE = "extra_preview_mode"
        const val EXTRA_BLOCK_MODE = "extra_block_mode"
        const val EXTRA_USED_MILLIS = "extra_used_millis"
        const val EXTRA_LIMIT_MINUTES = "extra_limit_minutes"
        const val EXTRA_EXTENSIONS_USED = "extra_extensions_used"

        const val MODE_FOCUS_SHIELD = "focus_shield"
        const val MODE_DAILY_LIMIT = "daily_limit"
    }

    private var targetPackageState by mutableStateOf("")
    private var isPreviewState by mutableStateOf(false)
    private var blockModeState by mutableStateOf(MODE_FOCUS_SHIELD)
    private var usedMillisState by mutableLongStateOf(0L)
    private var limitMinutesState by mutableIntStateOf(30)
    private var extensionsUsedState by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Ensure visible even if device was locked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        extractExtras(intent)

        setContent {
            val currentTheme by SettingsManager.themeState.collectAsState()
            val currentFont by SettingsManager.fontState.collectAsState()

            com.example.ui.theme.MyApplicationTheme(
                themeOption = currentTheme,
                fontOption = currentFont
            ) {
                if (blockModeState == MODE_DAILY_LIMIT) {
                    DailyLimitBlockedScreen(
                        packageName = targetPackageState,
                        usedMillis = usedMillisState,
                        limitMinutes = limitMinutesState,
                        extensionsUsedToday = extensionsUsedState,
                        isPreview = isPreviewState,
                        onGoStudy = { returnToPomoPal() },
                        onGrantFiveMoreMins = { grantFiveMinutesAndResumeApp() }
                    )
                } else {
                    BlockedAppScreen(
                        packageName = targetPackageState,
                        isPreview = isPreviewState,
                        onReturnToPomoPal = { returnToPomoPal() }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        extractExtras(intent)
    }

    private fun extractExtras(intent: Intent?) {
        val pkg = intent?.getStringExtra(EXTRA_PACKAGE_NAME) ?: ""
        targetPackageState = pkg
        isPreviewState = intent?.getBooleanExtra(EXTRA_PREVIEW_MODE, false) ?: false
        blockModeState = intent?.getStringExtra(EXTRA_BLOCK_MODE) ?: MODE_FOCUS_SHIELD
        usedMillisState = intent?.getLongExtra(EXTRA_USED_MILLIS, 0L) ?: 0L
        limitMinutesState = intent?.getIntExtra(EXTRA_LIMIT_MINUTES, 30) ?: 30
        extensionsUsedState = intent?.getIntExtra(EXTRA_EXTENSIONS_USED, 0) ?: 0
        AppBlockerManager.currentBlockedPackage = pkg.ifBlank { null }
    }

    override fun onResume() {
        super.onResume()
        AppBlockerManager.isBlockingScreenShowing = true
        AppBlockerManager.currentBlockedPackage = targetPackageState.ifBlank { null }
    }

    override fun onPause() {
        super.onPause()
        AppBlockerManager.isBlockingScreenShowing = false
    }

    override fun onDestroy() {
        super.onDestroy()
        AppBlockerManager.isBlockingScreenShowing = false
        AppBlockerManager.currentBlockedPackage = null
    }

    private fun grantFiveMinutesAndResumeApp() {
        val pkg = targetPackageState
        AppBlockerManager.isBlockingScreenShowing = false
        AppBlockerManager.currentBlockedPackage = null

        if (isPreviewState) {
            Toast.makeText(
                this,
                "Preview: 5 extra minutes granted. App would re-lock in 5 mins!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            return
        }

        if (pkg.isNotBlank()) {
            val graceExpiry = AppUsageLimitManager.grantFiveMinuteExtension(applicationContext, pkg)
            val extCount = AppUsageLimitManager.getExtensionsUsedToday(applicationContext, pkg)
            AppLimitNotificationHelper.notifyFiveMinuteGraceStarted(
                context = applicationContext,
                packageName = pkg,
                graceExpiryMs = graceExpiry,
                extensionsUsedToday = extCount
            )
            // Return cleanly to the app (or close overlay so the underlying app resumes immediately)
            try {
                val launchApp = packageManager.getLaunchIntentForPackage(pkg)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                }
                if (launchApp != null) {
                    startActivity(launchApp)
                }
            } catch (_: Exception) {}
        }
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun returnToPomoPal() {
        AppBlockerManager.isBlockingScreenShowing = false
        AppBlockerManager.currentBlockedPackage = null

        if (!isPreviewState) {
            val mainIntent = Intent(this, MainActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
                putExtra("RETURNED_FROM_BLOCKER", true)
            }
            startActivity(mainIntent)
        }
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}

@Composable
fun DailyLimitBlockedScreen(
    packageName: String,
    usedMillis: Long,
    limitMinutes: Int,
    extensionsUsedToday: Int,
    isPreview: Boolean,
    onGoStudy: () -> Unit,
    onGrantFiveMoreMins: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    // Prevent backing straight into the blocked app
    BackHandler {
        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
        onGoStudy()
    }

    val appName = remember(packageName) {
        if (packageName.isBlank()) "This App"
        else AppBlockerManager.getAppLabel(context, packageName)
    }

    var appIconBitmap by remember(packageName) { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(packageName) {
        if (packageName.isNotBlank()) {
            try {
                val iconDrawable = context.packageManager.getApplicationIcon(packageName)
                appIconBitmap = iconDrawable.toBitmap(width = 160, height = 160)
            } catch (_: Exception) {
                appIconBitmap = null
            }
        }
    }

    val motivation = remember(packageName) { AppBlockerManager.getFreshMotivation(context) }
    val limitMs = (limitMinutes.coerceAtLeast(1) * 60_000L)
    val effectiveUsedMs = if (usedMillis > 0L) usedMillis else limitMs
    val usedFormatted = remember(effectiveUsedMs) { AppUsageLimitManager.formatDurationCompact(effectiveUsedMs) }
    val limitFormatted = remember(limitMinutes) { AppUsageLimitManager.formatMinutesLabel(limitMinutes) }
    val resetInText = remember { AppUsageLimitManager.formatTimeUntilReset() }

    // Smooth entry spring animation for hero ring
    val ringSweepAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        ringSweepAnim.animateTo(1f, animationSpec = PomoMotion.expressiveSpatial())
    }

    // Gentle breathing aura
    val infiniteTransition = rememberInfiniteTransition(label = "limitAura")
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "limitAuraScale"
    )
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.22f,
        targetValue = 0.56f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "limitAuraAlpha"
    )

    val warnAccent = Color(0xFFE65100)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentTheme.background)
    ) {
        PaperTextureOverlay()

        // Warm Material 3 atmospheric radial glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(warnAccent.copy(alpha = 0.14f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.24f),
                    radius = size.width * 0.72f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(currentTheme.primary.copy(alpha = 0.14f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.86f),
                    radius = size.width * 0.85f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .widthIn(max = 560.dp)
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            // 1. Top M3 Status Pill
            Surface(
                shape = CircleShape,
                color = currentTheme.surface,
                border = BorderStroke(1.dp, warnAccent.copy(alpha = 0.35f)),
                modifier = Modifier.shadow(3.dp, CircleShape, spotColor = currentTheme.shadowColor)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.HourglassBottom,
                        contentDescription = null,
                        tint = warnAccent,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isPreview) "PREVIEW • DAILY LIMIT REACHED" else "DAILY LIMIT REACHED",
                        color = currentTheme.textPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = currentFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Hero App Icon with Breathing Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = warnAccent.copy(alpha = auraAlpha * 0.28f),
                        radius = (size.minDimension / 2f) * auraScale
                    )
                    val strokePx = 7.dp.toPx()
                    val pad = 12.dp.toPx()
                    drawArc(
                        color = currentTheme.cardBorder.copy(alpha = 0.35f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(pad, pad),
                        size = androidx.compose.ui.geometry.Size(size.width - pad * 2, size.height - pad * 2),
                        style = Stroke(width = strokePx, cap = StrokeCap.Round)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(currentTheme.primary, warnAccent, currentTheme.primary)
                        ),
                        startAngle = -90f,
                        sweepAngle = 360f * ringSweepAnim.value,
                        useCenter = false,
                        topLeft = Offset(pad, pad),
                        size = androidx.compose.ui.geometry.Size(size.width - pad * 2, size.height - pad * 2),
                        style = Stroke(width = strokePx, cap = StrokeCap.Round)
                    )
                }

                Surface(
                    modifier = Modifier
                        .size(88.dp)
                        .pomoShadow(
                            shape = RoundedCornerShape(26.dp),
                            elevation = 8.dp,
                            shadowColor = currentTheme.shadowColor
                        ),
                    shape = RoundedCornerShape(26.dp),
                    color = currentTheme.surface,
                    border = BorderStroke(2.dp, currentTheme.cardBorder)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        if (appIconBitmap != null) {
                            Image(
                                bitmap = appIconBitmap!!.asImageBitmap(),
                                contentDescription = appName,
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.HourglassBottom,
                                contentDescription = null,
                                tint = warnAccent,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                // Lock badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-18).dp, y = (-18).dp)
                        .size(34.dp)
                        .background(warnAccent, CircleShape)
                        .border(2.5.dp, currentTheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Limit locked",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Headline
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Time's up on $appName",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = currentTheme.textPrimary,
                    fontFamily = currentFont,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "You've hit your $limitFormatted daily limit. Step back and invest the rest of your energy into studying.",
                    fontSize = 14.sp,
                    color = currentTheme.textSecondary,
                    fontFamily = currentFont,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4. Material 3 Usage Telemetry Card (4:00 AM – 4:00 AM)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .pomoShadow(
                        shape = RoundedCornerShape(22.dp),
                        elevation = 3.dp,
                        shadowColor = currentTheme.shadowColor
                    ),
                shape = RoundedCornerShape(22.dp),
                color = currentTheme.surface,
                border = BorderStroke(1.dp, currentTheme.cardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "USED TODAY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = currentTheme.textSecondary,
                                fontFamily = currentFont
                            )
                            Text(
                                text = usedFormatted,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = warnAccent,
                                fontFamily = MonospaceFontFamily
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "DAILY LIMIT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = currentTheme.textSecondary,
                                fontFamily = currentFont
                            )
                            Text(
                                text = limitFormatted,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = currentTheme.textPrimary,
                                fontFamily = MonospaceFontFamily
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Full rounded progress track
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(warnAccent.copy(alpha = 0.16f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(ringSweepAnim.value.coerceIn(0f, 1f))
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(currentTheme.primary, warnAccent)
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Schedule,
                                contentDescription = null,
                                tint = currentTheme.textSecondary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Resets at 4:00 AM (in $resetInText)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = currentTheme.textSecondary,
                                fontFamily = currentFont
                            )
                        }
                        if (extensionsUsedToday > 0) {
                            Surface(
                                shape = CircleShape,
                                color = warnAccent.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "+5m ×$extensionsUsedToday used",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = warnAccent,
                                    fontFamily = currentFont,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Uplifting Study Quote Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = currentTheme.surface.copy(alpha = 0.92f),
                border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.7f))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = currentTheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PROTECT YOUR STUDY TIME",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.primary,
                            fontFamily = currentFont,
                            letterSpacing = 0.9.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "“$motivation”",
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 6. Primary + Secondary Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PomoButton(
                    onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                        onGoStudy()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("limit_go_study_button"),
                    containerColor = currentTheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Go Study • Open PomoPal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont
                    )
                }

                // "Just 5 mins more" M3 Tonal / Outlined Button
                FilledTonalButton(
                    onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                        onGrantFiveMoreMins()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("limit_five_more_mins_button"),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = currentTheme.surface,
                        contentColor = currentTheme.textPrimary
                    ),
                    border = BorderStroke(1.dp, currentTheme.cardBorder)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreTime,
                        contentDescription = null,
                        tint = warnAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Just 5 mins more",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont
                    )
                }

                Text(
                    text = if (extensionsUsedToday == 0) {
                        "We'll notify you and re-lock $appName automatically after 5 minutes."
                    } else {
                        "Extra pass #${extensionsUsedToday + 1} today — locks again automatically in 5 minutes."
                    },
                    fontSize = 11.5.sp,
                    color = currentTheme.textSecondary.copy(alpha = 0.85f),
                    fontFamily = currentFont,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BlockedAppScreen(
    packageName: String,
    isPreview: Boolean,
    onReturnToPomoPal: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    // Never let student accidentally back into the blocked app
    BackHandler {
        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
        onReturnToPomoPal()
    }

    // App info resolution
    val appName = remember(packageName) {
        if (packageName.isBlank()) "Distracting App"
        else AppBlockerManager.getAppLabel(context, packageName)
    }

    var appIconBitmap by remember(packageName) { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(packageName) {
        if (packageName.isNotBlank()) {
            try {
                val iconDrawable = context.packageManager.getApplicationIcon(packageName)
                appIconBitmap = iconDrawable.toBitmap(width = 160, height = 160)
            } catch (e: Exception) {
                appIconBitmap = null
            }
        }
    }

    // Motivational quote: 56 unique, non-repeating uplifting messages
    val motivation = remember { AppBlockerManager.getFreshMotivation(context) }

    // Goal and session info from TimerManager
    val currentTaskName by TimerManager.currentTaskName.collectAsState()
    val timeRemainingSeconds by TimerManager.timeRemainingSeconds.collectAsState()

    // 5-second countdown with high resolution progression
    var secondsLeft by remember { mutableIntStateOf(5) }
    val animatedProgress = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Animate the progress ring smoothly over 5 seconds (5000ms)
        launch {
            animatedProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
            )
        }

        // Discrete second ticks
        for (sec in 5 downTo 1) {
            secondsLeft = sec
            delay(1000)
        }
        secondsLeft = 0
        // Short grace pause at 0s, then automatically return to PomoPal
        delay(350)
        onReturnToPomoPal()
    }

    // Breathing halo animation (calming 4-second cycle)
    val infiniteTransition = rememberInfiniteTransition(label = "breathingAura")
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraScale"
    )
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentTheme.background)
    ) {
        // Subtle paper texture overlay for tactile depth
        PaperTextureOverlay()

        // Background gentle aura blobs
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(currentTheme.primary.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.28f),
                    radius = size.width * 0.7f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(currentTheme.secondary.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.85f),
                    radius = size.width * 0.8f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Top Serene Status Badge
            Surface(
                shape = CircleShape,
                color = currentTheme.surface,
                border = BorderStroke(1.dp, currentTheme.cardBorder),
                modifier = Modifier
                    .shadow(3.dp, CircleShape, spotColor = currentTheme.shadowColor)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Spa,
                        contentDescription = null,
                        tint = currentTheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isPreview) "PREVIEW • MINDFUL PAUSE" else "MINDFUL FOCUS SHIELD",
                        color = currentTheme.textPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = currentFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Hero Icon with Breathing Aura
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(150.dp)
            ) {
                // Expanding breathing rings
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = currentTheme.primary.copy(alpha = auraAlpha * 0.35f),
                        radius = (size.minDimension / 2f) * auraScale
                    )
                    drawCircle(
                        color = currentTheme.secondary.copy(alpha = auraAlpha * 0.2f),
                        radius = (size.minDimension / 2f) * (auraScale * 0.82f)
                    )
                }

                // App Icon Container
                Surface(
                    modifier = Modifier
                        .size(88.dp)
                        .pomoShadow(
                            shape = RoundedCornerShape(26.dp),
                            elevation = 8.dp,
                            shadowColor = currentTheme.shadowColor
                        ),
                    shape = RoundedCornerShape(26.dp),
                    color = currentTheme.surface,
                    border = BorderStroke(2.dp, currentTheme.cardBorder)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        if (appIconBitmap != null) {
                            Image(
                                bitmap = appIconBitmap!!.asImageBitmap(),
                                contentDescription = appName,
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = currentTheme.primary,
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }
                }

                // Paused / Shield Badge on bottom-right of icon
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-14).dp, y = (-14).dp)
                        .size(32.dp)
                        .background(currentTheme.primary, CircleShape)
                        .border(2.dp, currentTheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Spa,
                        contentDescription = "Protected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 3. Clear, gentle explanation
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$appName is Paused",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary,
                    fontFamily = currentFont,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "PomoPal is protecting your focus and momentum.",
                    fontSize = 14.sp,
                    color = currentTheme.textSecondary,
                    fontFamily = currentFont,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Current Goal Banner (reconnecting student with their goal)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .pomoShadow(
                        shape = RoundedCornerShape(18.dp),
                        elevation = 2.dp,
                        shadowColor = currentTheme.shadowColor
                    ),
                shape = RoundedCornerShape(18.dp),
                color = currentTheme.surface.copy(alpha = 0.9f),
                border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(currentTheme.primary.copy(alpha = 0.14f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CURRENT STUDY GOAL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.textSecondary,
                            fontFamily = currentFont,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = if (currentTaskName.isNotBlank()) currentTaskName else "Deep Focus Session",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = currentTheme.textPrimary,
                            fontFamily = currentFont,
                            maxLines = 1
                        )
                    }
                    val minutes = timeRemainingSeconds / 60
                    val secs = timeRemainingSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", minutes, secs),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.primary,
                        fontFamily = currentFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Meaningful Student Motivation Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .pomoShadow(
                        shape = RoundedCornerShape(22.dp),
                        elevation = 4.dp,
                        shadowColor = currentTheme.shadowColor
                    ),
                shape = RoundedCornerShape(22.dp),
                color = currentTheme.surface,
                border = BorderStroke(1.5.dp, currentTheme.cardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✦ MOMENT OF MINDFULNESS ✦",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.primary,
                        fontFamily = currentFont,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "“$motivation”",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont,
                        textAlign = TextAlign.Center,
                        lineHeight = 23.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Elegant 5-Second Circular Progression Countdown
            val guidanceText = when (secondsLeft) {
                5 -> "Take a gentle breath..."
                4 -> "Notice the urge to scroll..."
                3 -> "Remember your dreams..."
                2 -> "Realigning with your goals..."
                1 -> "Returning to focus..."
                else -> "Welcome back!"
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(90.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize().padding(6.dp)) {
                        // Background track
                        drawArc(
                            color = currentTheme.cardBorder.copy(alpha = 0.4f),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )
                        // Active progress arc
                        drawArc(
                            color = currentTheme.primary,
                            startAngle = -90f,
                            sweepAngle = animatedProgress.value * 360f,
                            useCenter = false,
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Dynamic second counter with bounce transition
                    AnimatedContent(
                        targetState = secondsLeft,
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(200)) + scaleIn(initialScale = 1.3f))
                                .togetherWith(fadeOut(animationSpec = tween(150)))
                        },
                        label = "secondsCountdown"
                    ) { sec ->
                        Text(
                            text = if (sec > 0) "${sec}s" else "✓",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = currentTheme.primary,
                            fontFamily = currentFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedContent(
                    targetState = guidanceText,
                    transitionSpec = { fadeIn(tween(250)).togetherWith(fadeOut(tween(150))) },
                    label = "guidanceText"
                ) { text ->
                    Text(
                        text = text,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 7. Tactile 3D Action Button: Return to Focus Now
            PomoButton(
                onClick = {
                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                    onReturnToPomoPal()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                containerColor = currentTheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Return to Focus Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Auto-returning in $secondsLeft seconds...",
                fontSize = 12.sp,
                color = currentTheme.textSecondary.copy(alpha = 0.75f),
                fontFamily = currentFont
            )

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
