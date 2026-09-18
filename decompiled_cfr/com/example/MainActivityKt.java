/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.NotificationManager
 *  android.content.Context
 *  android.content.Intent
 *  android.media.AudioManager
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Vibrator
 *  android.util.Log
 *  android.view.View
 *  androidx.activity.compose.ActivityResultRegistryKt
 *  androidx.activity.compose.BackHandlerKt
 *  androidx.activity.compose.ManagedActivityResultLauncher
 *  androidx.activity.result.contract.ActivityResultContract
 *  androidx.activity.result.contract.ActivityResultContracts$OpenDocument
 *  androidx.compose.animation.AnimatedContentKt
 *  androidx.compose.animation.AnimatedContentScope
 *  androidx.compose.animation.AnimatedContentTransitionScope
 *  androidx.compose.animation.AnimatedVisibilityKt
 *  androidx.compose.animation.AnimatedVisibilityScope
 *  androidx.compose.animation.AnimationModifierKt
 *  androidx.compose.animation.ContentTransform
 *  androidx.compose.animation.EnterExitTransitionKt
 *  androidx.compose.animation.EnterTransition
 *  androidx.compose.animation.ExitTransition
 *  androidx.compose.animation.SingleValueAnimationKt
 *  androidx.compose.animation.TransitionKt
 *  androidx.compose.animation.core.Animatable
 *  androidx.compose.animation.core.AnimatableKt
 *  androidx.compose.animation.core.AnimateAsStateKt
 *  androidx.compose.animation.core.AnimationSpec
 *  androidx.compose.animation.core.AnimationSpecKt
 *  androidx.compose.animation.core.DurationBasedAnimationSpec
 *  androidx.compose.animation.core.Easing
 *  androidx.compose.animation.core.EasingKt
 *  androidx.compose.animation.core.FiniteAnimationSpec
 *  androidx.compose.animation.core.InfiniteRepeatableSpec
 *  androidx.compose.animation.core.InfiniteTransition
 *  androidx.compose.animation.core.InfiniteTransitionKt
 *  androidx.compose.animation.core.RepeatMode
 *  androidx.compose.animation.core.SpringSpec
 *  androidx.compose.foundation.BackgroundKt
 *  androidx.compose.foundation.BorderKt
 *  androidx.compose.foundation.BorderStroke
 *  androidx.compose.foundation.BorderStrokeKt
 *  androidx.compose.foundation.CanvasKt
 *  androidx.compose.foundation.ClickableKt
 *  androidx.compose.foundation.Indication
 *  androidx.compose.foundation.ScrollKt
 *  androidx.compose.foundation.ScrollState
 *  androidx.compose.foundation.interaction.InteractionSource
 *  androidx.compose.foundation.interaction.InteractionSourceKt
 *  androidx.compose.foundation.interaction.MutableInteractionSource
 *  androidx.compose.foundation.interaction.PressInteractionKt
 *  androidx.compose.foundation.layout.Arrangement
 *  androidx.compose.foundation.layout.Arrangement$Horizontal
 *  androidx.compose.foundation.layout.Arrangement$HorizontalOrVertical
 *  androidx.compose.foundation.layout.Arrangement$Vertical
 *  androidx.compose.foundation.layout.AspectRatioKt
 *  androidx.compose.foundation.layout.BoxKt
 *  androidx.compose.foundation.layout.BoxScope
 *  androidx.compose.foundation.layout.BoxScopeInstance
 *  androidx.compose.foundation.layout.BoxWithConstraintsKt
 *  androidx.compose.foundation.layout.BoxWithConstraintsScope
 *  androidx.compose.foundation.layout.ColumnKt
 *  androidx.compose.foundation.layout.ColumnScope
 *  androidx.compose.foundation.layout.ColumnScopeInstance
 *  androidx.compose.foundation.layout.OffsetKt
 *  androidx.compose.foundation.layout.PaddingKt
 *  androidx.compose.foundation.layout.PaddingValues
 *  androidx.compose.foundation.layout.RowKt
 *  androidx.compose.foundation.layout.RowScope
 *  androidx.compose.foundation.layout.RowScopeInstance
 *  androidx.compose.foundation.layout.SizeKt
 *  androidx.compose.foundation.layout.SpacerKt
 *  androidx.compose.foundation.layout.WindowInsets
 *  androidx.compose.foundation.layout.WindowInsets$Companion
 *  androidx.compose.foundation.layout.WindowInsetsKt
 *  androidx.compose.foundation.layout.WindowInsetsPaddingKt
 *  androidx.compose.foundation.layout.WindowInsetsPadding_androidKt
 *  androidx.compose.foundation.layout.WindowInsets_androidKt
 *  androidx.compose.foundation.lazy.LazyDslKt
 *  androidx.compose.foundation.lazy.LazyItemScope
 *  androidx.compose.foundation.lazy.LazyListScope
 *  androidx.compose.foundation.lazy.LazyListState
 *  androidx.compose.foundation.lazy.LazyListStateKt
 *  androidx.compose.foundation.shape.RoundedCornerShape
 *  androidx.compose.foundation.shape.RoundedCornerShapeKt
 *  androidx.compose.foundation.text.BasicTextFieldKt
 *  androidx.compose.foundation.text.KeyboardActionScope
 *  androidx.compose.foundation.text.KeyboardActions
 *  androidx.compose.foundation.text.KeyboardOptions
 *  androidx.compose.material.icons.Icons
 *  androidx.compose.material.icons.Icons$Filled
 *  androidx.compose.material.icons.Icons$Rounded
 *  androidx.compose.material.icons.filled.AddKt
 *  androidx.compose.material.icons.filled.CheckCircleKt
 *  androidx.compose.material.icons.filled.CloseKt
 *  androidx.compose.material.icons.filled.DirectionsRunKt
 *  androidx.compose.material.icons.filled.HomeKt
 *  androidx.compose.material.icons.filled.ListKt
 *  androidx.compose.material.icons.filled.LockKt
 *  androidx.compose.material.icons.filled.NotificationsKt
 *  androidx.compose.material.icons.filled.SettingsKt
 *  androidx.compose.material.icons.filled.StopKt
 *  androidx.compose.material.icons.filled.SyncKt
 *  androidx.compose.material.icons.rounded.SecurityKt
 *  androidx.compose.material3.AndroidAlertDialog_androidKt
 *  androidx.compose.material3.ButtonColors
 *  androidx.compose.material3.ButtonDefaults
 *  androidx.compose.material3.ButtonElevation
 *  androidx.compose.material3.ButtonKt
 *  androidx.compose.material3.CardColors
 *  androidx.compose.material3.CardDefaults
 *  androidx.compose.material3.CardElevation
 *  androidx.compose.material3.CardKt
 *  androidx.compose.material3.DividerKt
 *  androidx.compose.material3.IconButtonKt
 *  androidx.compose.material3.IconKt
 *  androidx.compose.material3.ModalBottomSheetKt
 *  androidx.compose.material3.OutlinedTextFieldDefaults
 *  androidx.compose.material3.OutlinedTextFieldKt
 *  androidx.compose.material3.ProgressIndicatorKt
 *  androidx.compose.material3.RippleKt
 *  androidx.compose.material3.ScaffoldKt
 *  androidx.compose.material3.SliderColors
 *  androidx.compose.material3.SliderDefaults
 *  androidx.compose.material3.SliderKt
 *  androidx.compose.material3.TextFieldColors
 *  androidx.compose.material3.TextKt
 *  androidx.compose.runtime.Applier
 *  androidx.compose.runtime.Composable
 *  androidx.compose.runtime.ComposableInferredTarget
 *  androidx.compose.runtime.ComposableTarget
 *  androidx.compose.runtime.ComposablesKt
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.ComposerKt
 *  androidx.compose.runtime.CompositionLocal
 *  androidx.compose.runtime.CompositionLocalMap
 *  androidx.compose.runtime.CompositionScopedCoroutineScopeCanceller
 *  androidx.compose.runtime.DisposableEffectResult
 *  androidx.compose.runtime.DisposableEffectScope
 *  androidx.compose.runtime.EffectsKt
 *  androidx.compose.runtime.IntState
 *  androidx.compose.runtime.MutableIntState
 *  androidx.compose.runtime.MutableState
 *  androidx.compose.runtime.RecomposeScopeImplKt
 *  androidx.compose.runtime.SnapshotIntStateKt
 *  androidx.compose.runtime.SnapshotStateKt
 *  androidx.compose.runtime.State
 *  androidx.compose.runtime.Updater
 *  androidx.compose.runtime.internal.ComposableLambdaKt
 *  androidx.compose.runtime.saveable.RememberSaveableKt
 *  androidx.compose.runtime.snapshots.SnapshotStateList
 *  androidx.compose.ui.Alignment
 *  androidx.compose.ui.Alignment$Horizontal
 *  androidx.compose.ui.Alignment$Vertical
 *  androidx.compose.ui.ComposedModifierKt
 *  androidx.compose.ui.Modifier
 *  androidx.compose.ui.draw.AlphaKt
 *  androidx.compose.ui.draw.ClipKt
 *  androidx.compose.ui.draw.ScaleKt
 *  androidx.compose.ui.draw.ShadowKt
 *  androidx.compose.ui.focus.FocusManager
 *  androidx.compose.ui.geometry.Size
 *  androidx.compose.ui.graphics.AndroidPath_androidKt
 *  androidx.compose.ui.graphics.Brush
 *  androidx.compose.ui.graphics.Brush$Companion
 *  androidx.compose.ui.graphics.Color
 *  androidx.compose.ui.graphics.ColorKt
 *  androidx.compose.ui.graphics.GraphicsLayerModifierKt
 *  androidx.compose.ui.graphics.GraphicsLayerScope
 *  androidx.compose.ui.graphics.Path
 *  androidx.compose.ui.graphics.Shape
 *  androidx.compose.ui.graphics.StrokeCap
 *  androidx.compose.ui.graphics.drawscope.DrawScope
 *  androidx.compose.ui.graphics.drawscope.DrawStyle
 *  androidx.compose.ui.graphics.drawscope.Stroke
 *  androidx.compose.ui.graphics.vector.ImageVector
 *  androidx.compose.ui.input.pointer.SuspendingPointerInputFilterKt
 *  androidx.compose.ui.layout.MeasurePolicy
 *  androidx.compose.ui.node.ComposeUiNode
 *  androidx.compose.ui.platform.AndroidCompositionLocals_androidKt
 *  androidx.compose.ui.platform.CompositionLocalsKt
 *  androidx.compose.ui.text.TextStyle
 *  androidx.compose.ui.text.font.FontFamily
 *  androidx.compose.ui.text.font.FontStyle
 *  androidx.compose.ui.text.font.FontWeight
 *  androidx.compose.ui.text.input.ImeAction
 *  androidx.compose.ui.text.input.KeyboardType
 *  androidx.compose.ui.text.style.TextAlign
 *  androidx.compose.ui.unit.Dp
 *  androidx.compose.ui.unit.TextUnitKt
 *  androidx.compose.ui.window.AndroidDialog_androidKt
 *  androidx.compose.ui.window.DialogProperties
 *  androidx.lifecycle.Lifecycle$Event
 *  androidx.lifecycle.LifecycleEventObserver
 *  androidx.lifecycle.LifecycleObserver
 *  androidx.lifecycle.LifecycleOwner
 *  androidx.lifecycle.compose.LocalLifecycleOwnerKt
 *  androidx.navigation.NavBackStackEntry
 *  androidx.navigation.NavController
 *  androidx.navigation.NavGraphBuilder
 *  androidx.navigation.NavHostController
 *  androidx.navigation.NavOptionsBuilder
 *  androidx.navigation.Navigator
 *  androidx.navigation.PopUpToBuilder
 *  androidx.navigation.compose.NavGraphBuilderKt
 *  androidx.navigation.compose.NavHostControllerKt
 *  androidx.navigation.compose.NavHostKt
 *  com.example.ComposableSingletons$MainActivityKt
 *  com.example.MainActivity
 *  com.example.MainActivityKt$AddTaskCard$1$1$1
 *  com.example.MainActivityKt$AddTaskCard$1$2$1
 *  com.example.MainActivityKt$AddTaskCard$1$3$2$1
 *  com.example.MainActivityKt$AddTaskCard$1$3$3$1$1
 *  com.example.MainActivityKt$AlarmRingingLockScreen$lambda$500$lambda$499$$inlined$onDispose$1
 *  com.example.MainActivityKt$HomeScreen$1$1
 *  com.example.MainActivityKt$HomeScreen$2$1
 *  com.example.MainActivityKt$HomeScreen$3$1
 *  com.example.MainActivityKt$HomeScreen$4$1$1$1
 *  com.example.MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$$inlined$items$default$1
 *  com.example.MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$$inlined$items$default$2
 *  com.example.MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$$inlined$items$default$3
 *  com.example.MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$$inlined$items$default$4
 *  com.example.MainActivityKt$PomoPalApp$1$1
 *  com.example.MainActivityKt$PremiumJumpingTextPreview$1$1$1$1
 *  com.example.MainActivityKt$PremiumJumpingTextPreview$1$1$2$1
 *  com.example.MainActivityKt$PremiumJumpingTextPreview$1$1$3$1
 *  com.example.MainActivityKt$SeriousFullscreenOverlay$lambda$453$lambda$452$$inlined$onDispose$1
 *  com.example.MainActivityKt$SettingsOverlay$1$1
 *  com.example.MainActivityKt$SettingsOverlay$4$1
 *  com.example.MainActivityKt$SettingsOverlay$6$1
 *  com.example.MainActivityKt$SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282$$inlined$onDispose$1
 *  com.example.MainActivityKt$TimerDisplay$1$1
 *  com.example.MainActivityKt$TimerDisplay$2$1
 *  com.example.data.TaskItem
 *  com.example.service.AlarmReceiver
 *  com.example.service.AlarmState
 *  com.example.service.AppBlockerManager
 *  com.example.service.AppBlockerManager$AccessibilityServiceStatus
 *  com.example.service.SettingsManager
 *  com.example.service.SoundPlayer
 *  com.example.service.SquatSensorService
 *  com.example.service.TimerManager
 *  com.example.service.TimerManager$TimerState
 *  com.example.service.TimerService
 *  com.example.ui.AlarmScreenKt
 *  com.example.ui.BlockedAppsScreenKt
 *  com.example.ui.HistoryScreenKt
 *  com.example.ui.SignInScreenKt
 *  com.example.ui.SquatCalibrationScreenKt
 *  com.example.ui.SystemProtectionScreenKt
 *  com.example.ui.SystemProtectionStateKt
 *  com.example.ui.SystemProtectionStates
 *  com.example.ui.TimerViewModel
 *  com.example.ui.WorkoutScreenKt
 *  com.example.ui.components.AboutScreenKt
 *  com.example.ui.components.AdaptiveDimensions
 *  com.example.ui.components.AdaptiveSystemKt
 *  com.example.ui.components.MascotExpression
 *  com.example.ui.components.PomoPalMascotKt
 *  com.example.ui.components.PomoUIKt
 *  com.example.ui.components.ScaledDpKt
 *  com.example.ui.components.ScaledSpKt
 *  com.example.ui.components.ThemeFontCustomizerKt
 *  com.example.ui.theme.ThemeKt
 *  com.example.ui.theme.ThemeOption
 *  com.example.ui.theme.TypographyKt
 *  kotlin.Metadata
 *  kotlin.Pair
 *  kotlin.ULong
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.coroutines.CoroutineContext
 *  kotlin.coroutines.EmptyCoroutineContext
 *  kotlin.internal.ProgressionUtilKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.functions.Function2
 *  kotlin.jvm.functions.Function3
 *  kotlin.jvm.functions.Function4
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.Ref$IntRef
 *  kotlin.jvm.internal.SourceDebugExtension
 *  kotlin.ranges.ClosedFloatingPointRange
 *  kotlin.ranges.RangesKt
 *  kotlin.text.StringsKt
 *  kotlinx.coroutines.flow.StateFlow
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.example;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import androidx.activity.compose.ActivityResultRegistryKt;
import androidx.activity.compose.BackHandlerKt;
import androidx.activity.compose.ManagedActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.animation.AnimatedContentKt;
import androidx.compose.animation.AnimatedContentScope;
import androidx.compose.animation.AnimatedContentTransitionScope;
import androidx.compose.animation.AnimatedVisibilityKt;
import androidx.compose.animation.AnimatedVisibilityScope;
import androidx.compose.animation.AnimationModifierKt;
import androidx.compose.animation.ContentTransform;
import androidx.compose.animation.EnterExitTransitionKt;
import androidx.compose.animation.EnterTransition;
import androidx.compose.animation.ExitTransition;
import androidx.compose.animation.SingleValueAnimationKt;
import androidx.compose.animation.TransitionKt;
import androidx.compose.animation.core.Animatable;
import androidx.compose.animation.core.AnimatableKt;
import androidx.compose.animation.core.AnimateAsStateKt;
import androidx.compose.animation.core.AnimationSpec;
import androidx.compose.animation.core.AnimationSpecKt;
import androidx.compose.animation.core.DurationBasedAnimationSpec;
import androidx.compose.animation.core.Easing;
import androidx.compose.animation.core.EasingKt;
import androidx.compose.animation.core.FiniteAnimationSpec;
import androidx.compose.animation.core.InfiniteRepeatableSpec;
import androidx.compose.animation.core.InfiniteTransition;
import androidx.compose.animation.core.InfiniteTransitionKt;
import androidx.compose.animation.core.RepeatMode;
import androidx.compose.animation.core.SpringSpec;
import androidx.compose.foundation.BackgroundKt;
import androidx.compose.foundation.BorderKt;
import androidx.compose.foundation.BorderStroke;
import androidx.compose.foundation.BorderStrokeKt;
import androidx.compose.foundation.CanvasKt;
import androidx.compose.foundation.ClickableKt;
import androidx.compose.foundation.Indication;
import androidx.compose.foundation.ScrollKt;
import androidx.compose.foundation.ScrollState;
import androidx.compose.foundation.interaction.InteractionSource;
import androidx.compose.foundation.interaction.InteractionSourceKt;
import androidx.compose.foundation.interaction.MutableInteractionSource;
import androidx.compose.foundation.interaction.PressInteractionKt;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.AspectRatioKt;
import androidx.compose.foundation.layout.BoxKt;
import androidx.compose.foundation.layout.BoxScope;
import androidx.compose.foundation.layout.BoxScopeInstance;
import androidx.compose.foundation.layout.BoxWithConstraintsKt;
import androidx.compose.foundation.layout.BoxWithConstraintsScope;
import androidx.compose.foundation.layout.ColumnKt;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.foundation.layout.ColumnScopeInstance;
import androidx.compose.foundation.layout.OffsetKt;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.layout.RowKt;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.RowScopeInstance;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.layout.WindowInsets;
import androidx.compose.foundation.layout.WindowInsetsKt;
import androidx.compose.foundation.layout.WindowInsetsPaddingKt;
import androidx.compose.foundation.layout.WindowInsetsPadding_androidKt;
import androidx.compose.foundation.layout.WindowInsets_androidKt;
import androidx.compose.foundation.lazy.LazyDslKt;
import androidx.compose.foundation.lazy.LazyItemScope;
import androidx.compose.foundation.lazy.LazyListScope;
import androidx.compose.foundation.lazy.LazyListState;
import androidx.compose.foundation.lazy.LazyListStateKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.foundation.text.BasicTextFieldKt;
import androidx.compose.foundation.text.KeyboardActionScope;
import androidx.compose.foundation.text.KeyboardActions;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.AddKt;
import androidx.compose.material.icons.filled.CheckCircleKt;
import androidx.compose.material.icons.filled.CloseKt;
import androidx.compose.material.icons.filled.DirectionsRunKt;
import androidx.compose.material.icons.filled.HomeKt;
import androidx.compose.material.icons.filled.ListKt;
import androidx.compose.material.icons.filled.LockKt;
import androidx.compose.material.icons.filled.NotificationsKt;
import androidx.compose.material.icons.filled.SettingsKt;
import androidx.compose.material.icons.filled.StopKt;
import androidx.compose.material.icons.filled.SyncKt;
import androidx.compose.material.icons.rounded.SecurityKt;
import androidx.compose.material3.AndroidAlertDialog_androidKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonElevation;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.CardColors;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.CardElevation;
import androidx.compose.material3.CardKt;
import androidx.compose.material3.DividerKt;
import androidx.compose.material3.IconButtonKt;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.ModalBottomSheetKt;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.OutlinedTextFieldKt;
import androidx.compose.material3.ProgressIndicatorKt;
import androidx.compose.material3.RippleKt;
import androidx.compose.material3.ScaffoldKt;
import androidx.compose.material3.SliderColors;
import androidx.compose.material3.SliderDefaults;
import androidx.compose.material3.SliderKt;
import androidx.compose.material3.TextFieldColors;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Applier;
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.ComposableInferredTarget;
import androidx.compose.runtime.ComposableTarget;
import androidx.compose.runtime.ComposablesKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.CompositionLocal;
import androidx.compose.runtime.CompositionLocalMap;
import androidx.compose.runtime.CompositionScopedCoroutineScopeCanceller;
import androidx.compose.runtime.DisposableEffectResult;
import androidx.compose.runtime.DisposableEffectScope;
import androidx.compose.runtime.EffectsKt;
import androidx.compose.runtime.IntState;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.SnapshotIntStateKt;
import androidx.compose.runtime.SnapshotStateKt;
import androidx.compose.runtime.State;
import androidx.compose.runtime.Updater;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.runtime.saveable.RememberSaveableKt;
import androidx.compose.runtime.snapshots.SnapshotStateList;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.ComposedModifierKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.draw.AlphaKt;
import androidx.compose.ui.draw.ClipKt;
import androidx.compose.ui.draw.ScaleKt;
import androidx.compose.ui.draw.ShadowKt;
import androidx.compose.ui.focus.FocusManager;
import androidx.compose.ui.geometry.Size;
import androidx.compose.ui.graphics.AndroidPath_androidKt;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.ColorKt;
import androidx.compose.ui.graphics.GraphicsLayerModifierKt;
import androidx.compose.ui.graphics.GraphicsLayerScope;
import androidx.compose.ui.graphics.Path;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.graphics.StrokeCap;
import androidx.compose.ui.graphics.drawscope.DrawScope;
import androidx.compose.ui.graphics.drawscope.DrawStyle;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.input.pointer.SuspendingPointerInputFilterKt;
import androidx.compose.ui.layout.MeasurePolicy;
import androidx.compose.ui.node.ComposeUiNode;
import androidx.compose.ui.platform.AndroidCompositionLocals_androidKt;
import androidx.compose.ui.platform.CompositionLocalsKt;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.ImeAction;
import androidx.compose.ui.text.input.KeyboardType;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.unit.Dp;
import androidx.compose.ui.unit.TextUnitKt;
import androidx.compose.ui.window.AndroidDialog_androidKt;
import androidx.compose.ui.window.DialogProperties;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.compose.LocalLifecycleOwnerKt;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavGraphBuilder;
import androidx.navigation.NavHostController;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigator;
import androidx.navigation.PopUpToBuilder;
import androidx.navigation.compose.NavGraphBuilderKt;
import androidx.navigation.compose.NavHostControllerKt;
import androidx.navigation.compose.NavHostKt;
import com.example.ComposableSingletons;
import com.example.MainActivity;
import com.example.MainActivityKt;
import com.example.MainActivityKt$$ExternalSyntheticLambda0;
import com.example.MainActivityKt$$ExternalSyntheticLambda1;
import com.example.MainActivityKt$$ExternalSyntheticLambda10;
import com.example.MainActivityKt$$ExternalSyntheticLambda100;
import com.example.MainActivityKt$$ExternalSyntheticLambda101;
import com.example.MainActivityKt$$ExternalSyntheticLambda102;
import com.example.MainActivityKt$$ExternalSyntheticLambda103;
import com.example.MainActivityKt$$ExternalSyntheticLambda104;
import com.example.MainActivityKt$$ExternalSyntheticLambda105;
import com.example.MainActivityKt$$ExternalSyntheticLambda106;
import com.example.MainActivityKt$$ExternalSyntheticLambda107;
import com.example.MainActivityKt$$ExternalSyntheticLambda108;
import com.example.MainActivityKt$$ExternalSyntheticLambda109;
import com.example.MainActivityKt$$ExternalSyntheticLambda11;
import com.example.MainActivityKt$$ExternalSyntheticLambda110;
import com.example.MainActivityKt$$ExternalSyntheticLambda111;
import com.example.MainActivityKt$$ExternalSyntheticLambda112;
import com.example.MainActivityKt$$ExternalSyntheticLambda113;
import com.example.MainActivityKt$$ExternalSyntheticLambda114;
import com.example.MainActivityKt$$ExternalSyntheticLambda115;
import com.example.MainActivityKt$$ExternalSyntheticLambda116;
import com.example.MainActivityKt$$ExternalSyntheticLambda117;
import com.example.MainActivityKt$$ExternalSyntheticLambda118;
import com.example.MainActivityKt$$ExternalSyntheticLambda119;
import com.example.MainActivityKt$$ExternalSyntheticLambda12;
import com.example.MainActivityKt$$ExternalSyntheticLambda120;
import com.example.MainActivityKt$$ExternalSyntheticLambda121;
import com.example.MainActivityKt$$ExternalSyntheticLambda122;
import com.example.MainActivityKt$$ExternalSyntheticLambda123;
import com.example.MainActivityKt$$ExternalSyntheticLambda124;
import com.example.MainActivityKt$$ExternalSyntheticLambda125;
import com.example.MainActivityKt$$ExternalSyntheticLambda126;
import com.example.MainActivityKt$$ExternalSyntheticLambda127;
import com.example.MainActivityKt$$ExternalSyntheticLambda128;
import com.example.MainActivityKt$$ExternalSyntheticLambda129;
import com.example.MainActivityKt$$ExternalSyntheticLambda13;
import com.example.MainActivityKt$$ExternalSyntheticLambda130;
import com.example.MainActivityKt$$ExternalSyntheticLambda131;
import com.example.MainActivityKt$$ExternalSyntheticLambda132;
import com.example.MainActivityKt$$ExternalSyntheticLambda133;
import com.example.MainActivityKt$$ExternalSyntheticLambda134;
import com.example.MainActivityKt$$ExternalSyntheticLambda135;
import com.example.MainActivityKt$$ExternalSyntheticLambda136;
import com.example.MainActivityKt$$ExternalSyntheticLambda137;
import com.example.MainActivityKt$$ExternalSyntheticLambda138;
import com.example.MainActivityKt$$ExternalSyntheticLambda139;
import com.example.MainActivityKt$$ExternalSyntheticLambda14;
import com.example.MainActivityKt$$ExternalSyntheticLambda140;
import com.example.MainActivityKt$$ExternalSyntheticLambda141;
import com.example.MainActivityKt$$ExternalSyntheticLambda142;
import com.example.MainActivityKt$$ExternalSyntheticLambda143;
import com.example.MainActivityKt$$ExternalSyntheticLambda144;
import com.example.MainActivityKt$$ExternalSyntheticLambda145;
import com.example.MainActivityKt$$ExternalSyntheticLambda146;
import com.example.MainActivityKt$$ExternalSyntheticLambda147;
import com.example.MainActivityKt$$ExternalSyntheticLambda148;
import com.example.MainActivityKt$$ExternalSyntheticLambda149;
import com.example.MainActivityKt$$ExternalSyntheticLambda15;
import com.example.MainActivityKt$$ExternalSyntheticLambda150;
import com.example.MainActivityKt$$ExternalSyntheticLambda151;
import com.example.MainActivityKt$$ExternalSyntheticLambda152;
import com.example.MainActivityKt$$ExternalSyntheticLambda153;
import com.example.MainActivityKt$$ExternalSyntheticLambda154;
import com.example.MainActivityKt$$ExternalSyntheticLambda155;
import com.example.MainActivityKt$$ExternalSyntheticLambda156;
import com.example.MainActivityKt$$ExternalSyntheticLambda157;
import com.example.MainActivityKt$$ExternalSyntheticLambda158;
import com.example.MainActivityKt$$ExternalSyntheticLambda159;
import com.example.MainActivityKt$$ExternalSyntheticLambda16;
import com.example.MainActivityKt$$ExternalSyntheticLambda160;
import com.example.MainActivityKt$$ExternalSyntheticLambda161;
import com.example.MainActivityKt$$ExternalSyntheticLambda162;
import com.example.MainActivityKt$$ExternalSyntheticLambda163;
import com.example.MainActivityKt$$ExternalSyntheticLambda164;
import com.example.MainActivityKt$$ExternalSyntheticLambda165;
import com.example.MainActivityKt$$ExternalSyntheticLambda166;
import com.example.MainActivityKt$$ExternalSyntheticLambda167;
import com.example.MainActivityKt$$ExternalSyntheticLambda168;
import com.example.MainActivityKt$$ExternalSyntheticLambda169;
import com.example.MainActivityKt$$ExternalSyntheticLambda17;
import com.example.MainActivityKt$$ExternalSyntheticLambda170;
import com.example.MainActivityKt$$ExternalSyntheticLambda171;
import com.example.MainActivityKt$$ExternalSyntheticLambda172;
import com.example.MainActivityKt$$ExternalSyntheticLambda173;
import com.example.MainActivityKt$$ExternalSyntheticLambda174;
import com.example.MainActivityKt$$ExternalSyntheticLambda175;
import com.example.MainActivityKt$$ExternalSyntheticLambda176;
import com.example.MainActivityKt$$ExternalSyntheticLambda177;
import com.example.MainActivityKt$$ExternalSyntheticLambda178;
import com.example.MainActivityKt$$ExternalSyntheticLambda179;
import com.example.MainActivityKt$$ExternalSyntheticLambda18;
import com.example.MainActivityKt$$ExternalSyntheticLambda180;
import com.example.MainActivityKt$$ExternalSyntheticLambda181;
import com.example.MainActivityKt$$ExternalSyntheticLambda182;
import com.example.MainActivityKt$$ExternalSyntheticLambda19;
import com.example.MainActivityKt$$ExternalSyntheticLambda2;
import com.example.MainActivityKt$$ExternalSyntheticLambda20;
import com.example.MainActivityKt$$ExternalSyntheticLambda21;
import com.example.MainActivityKt$$ExternalSyntheticLambda22;
import com.example.MainActivityKt$$ExternalSyntheticLambda23;
import com.example.MainActivityKt$$ExternalSyntheticLambda24;
import com.example.MainActivityKt$$ExternalSyntheticLambda25;
import com.example.MainActivityKt$$ExternalSyntheticLambda26;
import com.example.MainActivityKt$$ExternalSyntheticLambda27;
import com.example.MainActivityKt$$ExternalSyntheticLambda28;
import com.example.MainActivityKt$$ExternalSyntheticLambda29;
import com.example.MainActivityKt$$ExternalSyntheticLambda3;
import com.example.MainActivityKt$$ExternalSyntheticLambda30;
import com.example.MainActivityKt$$ExternalSyntheticLambda31;
import com.example.MainActivityKt$$ExternalSyntheticLambda32;
import com.example.MainActivityKt$$ExternalSyntheticLambda33;
import com.example.MainActivityKt$$ExternalSyntheticLambda34;
import com.example.MainActivityKt$$ExternalSyntheticLambda35;
import com.example.MainActivityKt$$ExternalSyntheticLambda36;
import com.example.MainActivityKt$$ExternalSyntheticLambda37;
import com.example.MainActivityKt$$ExternalSyntheticLambda38;
import com.example.MainActivityKt$$ExternalSyntheticLambda39;
import com.example.MainActivityKt$$ExternalSyntheticLambda4;
import com.example.MainActivityKt$$ExternalSyntheticLambda40;
import com.example.MainActivityKt$$ExternalSyntheticLambda41;
import com.example.MainActivityKt$$ExternalSyntheticLambda42;
import com.example.MainActivityKt$$ExternalSyntheticLambda43;
import com.example.MainActivityKt$$ExternalSyntheticLambda44;
import com.example.MainActivityKt$$ExternalSyntheticLambda45;
import com.example.MainActivityKt$$ExternalSyntheticLambda46;
import com.example.MainActivityKt$$ExternalSyntheticLambda47;
import com.example.MainActivityKt$$ExternalSyntheticLambda48;
import com.example.MainActivityKt$$ExternalSyntheticLambda49;
import com.example.MainActivityKt$$ExternalSyntheticLambda5;
import com.example.MainActivityKt$$ExternalSyntheticLambda50;
import com.example.MainActivityKt$$ExternalSyntheticLambda51;
import com.example.MainActivityKt$$ExternalSyntheticLambda52;
import com.example.MainActivityKt$$ExternalSyntheticLambda53;
import com.example.MainActivityKt$$ExternalSyntheticLambda54;
import com.example.MainActivityKt$$ExternalSyntheticLambda55;
import com.example.MainActivityKt$$ExternalSyntheticLambda56;
import com.example.MainActivityKt$$ExternalSyntheticLambda57;
import com.example.MainActivityKt$$ExternalSyntheticLambda58;
import com.example.MainActivityKt$$ExternalSyntheticLambda59;
import com.example.MainActivityKt$$ExternalSyntheticLambda6;
import com.example.MainActivityKt$$ExternalSyntheticLambda60;
import com.example.MainActivityKt$$ExternalSyntheticLambda61;
import com.example.MainActivityKt$$ExternalSyntheticLambda62;
import com.example.MainActivityKt$$ExternalSyntheticLambda63;
import com.example.MainActivityKt$$ExternalSyntheticLambda64;
import com.example.MainActivityKt$$ExternalSyntheticLambda65;
import com.example.MainActivityKt$$ExternalSyntheticLambda66;
import com.example.MainActivityKt$$ExternalSyntheticLambda67;
import com.example.MainActivityKt$$ExternalSyntheticLambda68;
import com.example.MainActivityKt$$ExternalSyntheticLambda69;
import com.example.MainActivityKt$$ExternalSyntheticLambda7;
import com.example.MainActivityKt$$ExternalSyntheticLambda70;
import com.example.MainActivityKt$$ExternalSyntheticLambda71;
import com.example.MainActivityKt$$ExternalSyntheticLambda72;
import com.example.MainActivityKt$$ExternalSyntheticLambda73;
import com.example.MainActivityKt$$ExternalSyntheticLambda74;
import com.example.MainActivityKt$$ExternalSyntheticLambda75;
import com.example.MainActivityKt$$ExternalSyntheticLambda76;
import com.example.MainActivityKt$$ExternalSyntheticLambda77;
import com.example.MainActivityKt$$ExternalSyntheticLambda78;
import com.example.MainActivityKt$$ExternalSyntheticLambda79;
import com.example.MainActivityKt$$ExternalSyntheticLambda8;
import com.example.MainActivityKt$$ExternalSyntheticLambda80;
import com.example.MainActivityKt$$ExternalSyntheticLambda81;
import com.example.MainActivityKt$$ExternalSyntheticLambda82;
import com.example.MainActivityKt$$ExternalSyntheticLambda83;
import com.example.MainActivityKt$$ExternalSyntheticLambda84;
import com.example.MainActivityKt$$ExternalSyntheticLambda85;
import com.example.MainActivityKt$$ExternalSyntheticLambda86;
import com.example.MainActivityKt$$ExternalSyntheticLambda87;
import com.example.MainActivityKt$$ExternalSyntheticLambda88;
import com.example.MainActivityKt$$ExternalSyntheticLambda89;
import com.example.MainActivityKt$$ExternalSyntheticLambda9;
import com.example.MainActivityKt$$ExternalSyntheticLambda90;
import com.example.MainActivityKt$$ExternalSyntheticLambda91;
import com.example.MainActivityKt$$ExternalSyntheticLambda92;
import com.example.MainActivityKt$$ExternalSyntheticLambda93;
import com.example.MainActivityKt$$ExternalSyntheticLambda94;
import com.example.MainActivityKt$$ExternalSyntheticLambda95;
import com.example.MainActivityKt$$ExternalSyntheticLambda96;
import com.example.MainActivityKt$$ExternalSyntheticLambda97;
import com.example.MainActivityKt$$ExternalSyntheticLambda98;
import com.example.MainActivityKt$$ExternalSyntheticLambda99;
import com.example.MainActivityKt$AlarmRingingLockScreen$lambda$500$lambda$499$;
import com.example.MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$;
import com.example.MainActivityKt$SeriousFullscreenOverlay$lambda$453$lambda$452$;
import com.example.MainActivityKt$SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282$;
import com.example.data.TaskItem;
import com.example.service.AlarmReceiver;
import com.example.service.AlarmState;
import com.example.service.AppBlockerManager;
import com.example.service.SettingsManager;
import com.example.service.SoundPlayer;
import com.example.service.SquatSensorService;
import com.example.service.TimerManager;
import com.example.service.TimerService;
import com.example.ui.AlarmScreenKt;
import com.example.ui.BlockedAppsScreenKt;
import com.example.ui.HistoryScreenKt;
import com.example.ui.SignInScreenKt;
import com.example.ui.SquatCalibrationScreenKt;
import com.example.ui.SystemProtectionScreenKt;
import com.example.ui.SystemProtectionStateKt;
import com.example.ui.SystemProtectionStates;
import com.example.ui.TimerViewModel;
import com.example.ui.WorkoutScreenKt;
import com.example.ui.components.AboutScreenKt;
import com.example.ui.components.AdaptiveDimensions;
import com.example.ui.components.AdaptiveSystemKt;
import com.example.ui.components.MascotExpression;
import com.example.ui.components.PomoPalMascotKt;
import com.example.ui.components.PomoUIKt;
import com.example.ui.components.ScaledDpKt;
import com.example.ui.components.ScaledSpKt;
import com.example.ui.components.ThemeFontCustomizerKt;
import com.example.ui.theme.ThemeKt;
import com.example.ui.theme.ThemeOption;
import com.example.ui.theme.TypographyKt;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ULong;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.ClosedFloatingPointRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.flow.StateFlow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(d1={"\u0000\u00b0\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0015\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u00a2\u0006\u0002\u0010\u0004\u001a)\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u00a2\u0006\u0004\b\n\u0010\u000b\u001a%\u0010\f\u001a\u00020\u00012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0007\u00a2\u0006\u0002\u0010\u0011\u001aK\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00162\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00010\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u001bH\u0007\u00a2\u0006\u0002\u0010\u001c\u001a\u0015\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u00a2\u0006\u0002\u0010 \u001a;\u0010!\u001a\u00020\u00012\u001e\u0010\"\u001a\u001a\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u00010#2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00a2\u0006\u0002\u0010&\u001ab\u0010'\u001a\u00020\u00012\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010)\u001a\u00020\u00162\b\b\u0002\u0010*\u001a\u00020+2\b\b\u0002\u0010,\u001a\u00020\t2\b\b\u0002\u0010-\u001a\u00020.2\u0011\u0010/\u001a\r\u0012\u0004\u0012\u00020\u00010\u000e\u00a2\u0006\u0002\b0H\u0007\u00a2\u0006\u0004\b1\u00102\u001a-\u00103\u001a\u00020\u00012\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u0002072\u000e\b\u0002\u00108\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00a2\u0006\u0002\u00109\u001a!\u0010:\u001a\u00020\u00012\u0006\u0010;\u001a\u00020\u00102\b\b\u0002\u0010<\u001a\u00020=H\u0007\u00a2\u0006\u0004\b>\u0010?\u001aU\u0010@\u001a\u00020\u00012\u0006\u0010;\u001a\u00020\u00102\u0006\u0010A\u001a\u00020\u00102\u0006\u00104\u001a\u0002052\u0006\u0010B\u001a\u00020\u00162\u0006\u0010C\u001a\u00020\u001f2\n\b\u0002\u0010D\u001a\u0004\u0018\u00010\u00142\u0006\u00106\u001a\u0002072\b\b\u0002\u0010E\u001a\u00020\tH\u0007\u00a2\u0006\u0004\bF\u0010G\u001a\u001d\u0010H\u001a\u00020\u00012\u0006\u0010I\u001a\u00020\u001f2\u0006\u0010J\u001a\u00020\u0016H\u0007\u00a2\u0006\u0002\u0010K\u001a3\u0010L\u001a\u00020\u00012\u0006\u0010I\u001a\u00020\u001f2\u0006\u0010C\u001a\u00020\u001f2\u0006\u0010;\u001a\u00020\u00102\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00a2\u0006\u0002\u0010M\u001a\u001d\u0010N\u001a\u00020\u00012\u0006\u0010O\u001a\u00020\u001f2\u0006\u0010P\u001a\u00020\u0010H\u0007\u00a2\u0006\u0002\u0010Q\u00a8\u0006R\u00b2\u0006\f\u0010S\u001a\u0004\u0018\u00010TX\u008a\u0084\u0002\u00b2\u0006\n\u0010U\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\n\u0010V\u001a\u000205X\u008a\u0084\u0002\u00b2\u0006\n\u0010W\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\n\u0010X\u001a\u00020\u0010X\u008a\u0084\u0002\u00b2\u0006\f\u0010Y\u001a\u0004\u0018\u00010\u001fX\u008a\u008e\u0002\u00b2\u0006\n\u0010Z\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\n\u0010\\\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\n\u0010]\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\n\u0010^\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\n\u0010V\u001a\u000205X\u008a\u0084\u0002\u00b2\u0006\n\u0010B\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\n\u0010_\u001a\u00020\u001fX\u008a\u0084\u0002\u00b2\u0006\n\u0010;\u001a\u00020\u0010X\u008a\u0084\u0002\u00b2\u0006\n\u0010`\u001a\u00020\u001fX\u008a\u0084\u0002\u00b2\u0006\n\u0010a\u001a\u00020\u0010X\u008a\u0084\u0002\u00b2\u0006\f\u0010b\u001a\u0004\u0018\u00010$X\u008a\u0084\u0002\u00b2\u0006\u0010\u0010c\u001a\b\u0012\u0004\u0012\u00020\u00140dX\u008a\u0084\u0002\u00b2\u0006\n\u0010U\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\n\u0010e\u001a\u00020\u0016X\u008a\u008e\u0002\u00b2\u0006\n\u0010f\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\n\u0010X\u001a\u00020\u0010X\u008a\u0084\u0002\u00b2\u0006\n\u0010g\u001a\u00020\u0016X\u008a\u008e\u0002\u00b2\u0006\n\u0010h\u001a\u00020\u0016X\u008a\u008e\u0002\u00b2\u0006\n\u0010i\u001a\u00020.X\u008a\u0084\u0002\u00b2\u0006\n\u0010j\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\n\u0010k\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\n\u0010l\u001a\u00020\u0010X\u008a\u008e\u0002\u00b2\u0006\n\u0010m\u001a\u00020\u0016X\u008a\u008e\u0002\u00b2\u0006\n\u0010n\u001a\u00020[X\u008a\u008e\u0002\u00b2\u0006\n\u0010o\u001a\u00020[X\u008a\u008e\u0002\u00b2\u0006\n\u0010p\u001a\u00020\u0010X\u008a\u008e\u0002\u00b2\u0006\n\u0010q\u001a\u00020[X\u008a\u008e\u0002\u00b2\u0006\n\u0010r\u001a\u00020[X\u008a\u008e\u0002\u00b2\u0006\n\u0010s\u001a\u00020.X\u008a\u0084\u0002\u00b2\u0006\n\u0010t\u001a\u00020.X\u008a\u0084\u0002\u00b2\u0006\n\u0010u\u001a\u00020\tX\u008a\u0084\u0002\u00b2\u0006\n\u0010v\u001a\u00020\u001fX\u008a\u008e\u0002\u00b2\u0006\n\u0010w\u001a\u00020\u001fX\u008a\u008e\u0002\u00b2\u0006\n\u0010x\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\n\u0010y\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\n\u0010z\u001a\u00020{X\u008a\u008e\u0002\u00b2\u0006\n\u0010|\u001a\u00020.X\u008a\u0084\u0002\u00b2\u0006\n\u0010}\u001a\u00020\tX\u008a\u0084\u0002\u00b2\u0006\n\u0010~\u001a\u00020\u0016X\u008a\u008e\u0002\u00b2\u0006\n\u0010\u007f\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u0080\u0001\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u0081\u0001\u001a\u00020\u001fX\u008a\u008e\u0002\u00b2\u0006\u0018\u0010\u0082\u0001\u001a\u000f\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020.0\u0083\u0001X\u008a\u008e\u0002\u00b2\u0006\u000b\u0010\u0084\u0001\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\n\u0010\u007f\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u0085\u0001\u001a\u00020\tX\u008a\u0084\u0002\u00b2\u0006\n\u0010B\u001a\u00020\u0016X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u0086\u0001\u001a\u00020\u001fX\u008a\u008e\u0002\u00b2\u0006\u000b\u0010\u0087\u0001\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u0088\u0001\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u0089\u0001\u001a\u00020.X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u008a\u0001\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u008b\u0001\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u008c\u0001\u001a\u00020\u0010X\u008a\u008e\u0002\u00b2\u0006\u000b\u0010\u008d\u0001\u001a\u00020\u0016X\u008a\u008e\u0002\u00b2\u0006\u000b\u0010\u008e\u0001\u001a\u00020\u001fX\u008a\u008e\u0002\u00b2\u0006\u000b\u0010\u008f\u0001\u001a\u00020\u0016X\u008a\u008e\u0002\u00b2\u0006\n\u0010\u007f\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u0090\u0001\u001a\u00020[X\u008a\u0084\u0002\u00b2\u0006\u000b\u0010\u0091\u0001\u001a\u00020[X\u008a\u0084\u0002"}, d2={"PomoPalApp", "", "viewModel", "Lcom/example/ui/TimerViewModel;", "(Lcom/example/ui/TimerViewModel;Landroidx/compose/runtime/Composer;I)V", "HomeScreen", "navController", "Landroidx/navigation/NavController;", "bottomPadding", "Landroidx/compose/ui/unit/Dp;", "HomeScreen-6a0pyJM", "(Lcom/example/ui/TimerViewModel;Landroidx/navigation/NavController;FLandroidx/compose/runtime/Composer;II)V", "SettingsOverlay", "onDismiss", "Lkotlin/Function0;", "initialTab", "", "(Lkotlin/jvm/functions/Function0;ILandroidx/compose/runtime/Composer;II)V", "TaskItemRow", "task", "Lcom/example/data/TaskItem;", "isSelected", "", "onSelect", "Lkotlin/Function1;", "onDelete", "modifier", "Landroidx/compose/ui/Modifier;", "(Lcom/example/data/TaskItem;ZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "PremiumJumpingTextPreview", "text", "", "(Ljava/lang/String;Landroidx/compose/runtime/Composer;I)V", "AddTaskCard", "onSave", "Lkotlin/Function3;", "", "onCancel", "(Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "AnimatedScaleBox", "onClick", "enabled", "shape", "Landroidx/compose/ui/graphics/Shape;", "elevation", "shadowColor", "Landroidx/compose/ui/graphics/Color;", "content", "Landroidx/compose/runtime/Composable;", "AnimatedScaleBox-gXMAzVA", "(Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;ZLandroidx/compose/ui/graphics/Shape;FJLkotlin/jvm/functions/Function2;Landroidx/compose/runtime/Composer;II)V", "AppControls", "state", "Lcom/example/service/TimerManager$TimerState;", "context", "Landroid/content/Context;", "onStartAttempt", "(Lcom/example/service/TimerManager$TimerState;Landroid/content/Context;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "SlidingTimer", "timeRemaining", "fontSize", "Landroidx/compose/ui/unit/TextUnit;", "SlidingTimer-KmRG4DE", "(IJLandroidx/compose/runtime/Composer;II)V", "TimerDisplay", "totalTime", "isBreakMode", "taskName", "currentTask", "circleSize", "TimerDisplay-egy_3UM", "(IILcom/example/service/TimerManager$TimerState;ZLjava/lang/String;Lcom/example/data/TaskItem;Landroid/content/Context;FLandroidx/compose/runtime/Composer;II)V", "AnimatedQuoteCard", "quote", "isBreak", "(Ljava/lang/String;ZLandroidx/compose/runtime/Composer;I)V", "SeriousFullscreenOverlay", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "AlarmRingingLockScreen", "label", "targetCount", "(Ljava/lang/String;ILandroidx/compose/runtime/Composer;I)V", "app", "navBackStackEntry", "Landroidx/navigation/NavBackStackEntry;", "isAddingTask", "timerState", "isSettingsOpen", "settingsInitialTab", "updateUrl", "homeWeight", "", "historyWeight", "alarmWeight", "workoutWeight", "currentQuote", "currentTaskName", "currentTaskId", "currentTaskColor", "allTasks", "", "showAllTasksSheet", "showSettings", "showFullScreenSeriousness", "showJustInTimeSetup", "stateColor", "isSettingsPressed", "settingsRotation", "selectedTabIndex", "showBlockedApps", "localFocus", "localBreak", "localCompletion", "localCompletionDuration", "localVolume", "tabBgColor", "tabTextColor", "tabElevation", "focusInput", "breakInput", "isBlockedPressed", "blockedScale", "accessibilityStatus", "Lcom/example/service/AppBlockerManager$AccessibilityServiceStatus;", "borderColor", "borderWidth", "animateIn", "scale", "alpha", "title", "selectedCategory", "Lkotlin/Pair;", "isPressed", "currentElevation", "animatedText", "pulseAlpha", "pulseScale", "glowColor", "scaleGlow", "animatedAlpha", "currentCount", "shakeWarning", "sensorMessage", "isFinished", "animatedProgress", "waveOffset"}, k=2, mv={2, 2, 0}, xi=48)
@SourceDebugExtension(value={"SMAP\nMainActivity.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MainActivity.kt\ncom/example/MainActivityKt\n+ 2 CompositionLocal.kt\nandroidx/compose/runtime/CompositionLocal\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n+ 4 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 5 Effects.kt\nandroidx/compose/runtime/EffectsKt\n+ 6 Effects.kt\nandroidx/compose/runtime/EffectsKt$rememberCoroutineScope$1\n+ 7 Row.kt\nandroidx/compose/foundation/layout/RowKt\n+ 8 Layout.kt\nandroidx/compose/ui/layout/LayoutKt\n+ 9 Composables.kt\nandroidx/compose/runtime/ComposablesKt\n+ 10 Composer.kt\nandroidx/compose/runtime/Updater\n+ 11 _Strings.kt\nkotlin/text/StringsKt___StringsKt\n+ 12 Box.kt\nandroidx/compose/foundation/layout/BoxKt\n+ 13 Column.kt\nandroidx/compose/foundation/layout/ColumnKt\n+ 14 SnapshotState.kt\nandroidx/compose/runtime/SnapshotStateKt__SnapshotStateKt\n+ 15 Dp.kt\nandroidx/compose/ui/unit/Dp\n+ 16 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 17 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 18 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt\n+ 19 SnapshotIntState.kt\nandroidx/compose/runtime/SnapshotIntStateKt__SnapshotIntStateKt\n+ 20 Effects.kt\nandroidx/compose/runtime/DisposableEffectScope\n*L\n1#1,3084:1\n77#2:3085\n77#2:3092\n77#2:3093\n77#2:3108\n77#2:3115\n77#2:3116\n77#2:3147\n77#2:3148\n77#2:3174\n77#2:3175\n77#2:3281\n77#2:3282\n77#2:3283\n77#2:3284\n77#2:3285\n77#2:3299\n77#2:3300\n77#2:3385\n77#2:3386\n77#2:3388\n77#2:3397\n77#2:3455\n77#2:3456\n77#2:3516\n77#2:3556\n77#2:3557\n77#2:3571\n77#2:3572\n77#2:3573\n77#2:3691\n77#2:3734\n77#2:3746\n77#2:3747\n77#2:3761\n77#2:3762\n77#2:3787\n77#2:4127\n77#2:4128\n77#2:4986\n77#2:5933\n77#2:6008\n77#2:6403\n77#2:6404\n77#2:6405\n77#2:6596\n77#2:6597\n77#2:6692\n77#2:6777\n77#2:6778\n1225#3,6:3086\n1225#3,6:3094\n1225#3,6:3101\n1225#3,6:3109\n1225#3,6:3117\n1225#3,6:3123\n1225#3,6:3129\n1225#3,6:3135\n1225#3,6:3141\n1225#3,6:3149\n1225#3,6:3155\n1225#3,6:3161\n1225#3,6:3168\n1225#3,6:3176\n1225#3,6:3182\n1225#3,6:3188\n1225#3,6:3194\n1225#3,6:3200\n1225#3,6:3206\n1225#3,6:3212\n1225#3,6:3218\n1225#3,6:3224\n1225#3,6:3230\n1225#3,6:3236\n1225#3,6:3242\n1225#3,6:3248\n1225#3,6:3254\n1225#3,6:3260\n1225#3,3:3271\n1228#3,3:3277\n1225#3,6:3292\n1225#3,6:3337\n1225#3,6:3343\n1225#3,6:3349\n1225#3,6:3355\n1225#3,6:3361\n1225#3,6:3367\n1225#3,6:3373\n1225#3,6:3389\n1225#3,6:3398\n1225#3,6:3404\n1225#3,6:3449\n1225#3,6:3490\n1225#3,6:3498\n1225#3,6:3506\n1225#3,6:3559\n1225#3,6:3574\n1225#3,6:3580\n1225#3,6:3586\n1225#3,6:3592\n1225#3,6:3635\n1225#3,6:3646\n1225#3,6:3753\n1225#3,6:3763\n1225#3,6:3769\n1225#3,6:3775\n1225#3,6:3781\n1225#3,6:3788\n1225#3,6:3794\n1225#3,6:3800\n1225#3,6:3806\n1225#3,6:3812\n1225#3,6:3818\n1225#3,6:3938\n1225#3,6:4021\n1225#3,6:4028\n1225#3,6:4085\n1225#3,6:4111\n1225#3,6:4117\n1225#3,6:4204\n1225#3,6:4255\n1225#3,6:4306\n1225#3,6:4357\n1225#3,6:4415\n1225#3,6:4421\n1225#3,6:4428\n1225#3,6:4434\n1225#3,6:4440\n1225#3,6:4446\n1225#3,6:4452\n1225#3,6:4458\n1225#3,6:4464\n1225#3,6:4491\n1225#3,6:4569\n1225#3,6:4581\n1225#3,6:4587\n1225#3,6:4594\n1225#3,6:4645\n1225#3,6:4854\n1225#3,6:4863\n1225#3,6:4904\n1225#3,6:4918\n1225#3,6:4960\n1225#3,6:4974\n1225#3,6:4980\n1225#3,6:4995\n1225#3,6:5002\n1225#3,6:5008\n1225#3,6:5089\n1225#3,6:5096\n1225#3,6:5162\n1225#3,6:5226\n1225#3,6:5244\n1225#3,6:5380\n1225#3,6:5390\n1225#3,6:5430\n1225#3,6:5440\n1225#3,6:5447\n1225#3,6:5495\n1225#3,6:5547\n1225#3,6:5650\n1225#3,6:5778\n1225#3,6:5784\n1225#3,6:5913\n1225#3,6:5919\n1225#3,6:5927\n1225#3,6:5934\n1225#3,6:5940\n1225#3,6:5961\n1225#3,6:6011\n1225#3,6:6053\n1225#3,6:6189\n1225#3,6:6210\n1225#3,6:6216\n1225#3,6:6222\n1225#3,6:6268\n1225#3,6:6274\n1225#3,6:6321\n1225#3,6:6368\n1225#3,6:6377\n1225#3,6:6391\n1225#3,6:6397\n1225#3,6:6406\n1225#3,6:6448\n1225#3,6:6456\n1225#3,6:6604\n1225#3,6:6648\n1225#3,6:6909\n1225#3,6:6931\n1225#3,6:7093\n1225#3,6:7230\n1225#3,6:7324\n149#4:3100\n149#4:3107\n149#4:3167\n159#4:3286\n149#4:3287\n149#4:3288\n149#4:3289\n149#4:3290\n149#4:3291\n149#4:3298\n149#4:3301\n149#4:3302\n149#4:3379\n149#4:3387\n159#4:3395\n149#4:3396\n149#4:3489\n149#4:3496\n149#4:3497\n149#4:3504\n149#4:3505\n149#4:3558\n149#4:3570\n149#4:3634\n149#4:3641\n149#4:3642\n149#4:3643\n149#4:3644\n149#4:3645\n149#4:3689\n149#4:3690\n149#4:3696\n149#4:3735\n149#4:3736\n149#4:3737\n149#4:3748\n149#4:3749\n149#4:3750\n149#4:3751\n149#4:3752\n159#4:3759\n149#4:3760\n149#4:3824\n149#4:3892\n149#4:3928\n149#4:3933\n149#4:3934\n149#4:3935\n149#4:3936\n149#4:3937\n149#4:3944\n149#4:3945\n149#4:3978\n149#4:3983\n149#4:3984\n149#4:3985\n149#4:4027\n149#4:4034\n149#4:4080\n149#4:4081\n149#4:4082\n149#4:4083\n149#4:4084\n149#4:4091\n149#4:4092\n149#4:4093\n149#4:4094\n149#4:4164\n149#4:4165\n149#4:4166\n149#4:4167\n149#4:4203\n149#4:4210\n149#4:4247\n149#4:4248\n149#4:4254\n149#4:4261\n149#4:4298\n149#4:4299\n149#4:4305\n149#4:4312\n149#4:4349\n149#4:4350\n149#4:4356\n149#4:4363\n149#4:4400\n149#4:4401\n149#4:4427\n149#4:4500\n149#4:4575\n149#4:4600\n149#4:4641\n149#4:4642\n149#4:4643\n149#4:4644\n149#4:4651\n149#4:4762\n149#4:4763\n149#4:4764\n149#4:4765\n149#4:4801\n149#4:4806\n149#4:4844\n149#4:4845\n149#4:4860\n149#4:4861\n149#4:4862\n149#4:4987\n149#4:4988\n149#4:4989\n149#4:4991\n149#4:4992\n149#4:4993\n149#4:5001\n149#4:5014\n149#4:5015\n149#4:5095\n149#4:5122\n149#4:5159\n149#4:5160\n149#4:5161\n149#4:5193\n149#4:5239\n149#4:5240\n149#4:5241\n149#4:5242\n149#4:5243\n149#4:5286\n149#4:5309\n149#4:5346\n149#4:5379\n149#4:5396\n149#4:5429\n149#4:5446\n149#4:5453\n149#4:5454\n149#4:5455\n149#4:5494\n149#4:5501\n149#4:5546\n149#4:5567\n149#4:5641\n149#4:5642\n149#4:5647\n149#4:5648\n149#4:5649\n149#4:5660\n149#4:5734\n149#4:5735\n149#4:5740\n149#4:5741\n149#4:5742\n149#4:5798\n149#4:5875\n149#4:5912\n149#4:5925\n149#4:5926\n149#4:5946\n149#4:5947\n149#4:5948\n149#4:5949\n149#4:5950\n149#4:5951\n149#4:5952\n149#4:5953\n149#4:5954\n149#4:5955\n149#4:5956\n149#4:5957\n149#4:5958\n149#4:5959\n149#4:5960\n149#4:5967\n149#4:6009\n149#4:6010\n149#4:6069\n149#4:6183\n149#4:6184\n149#4:6228\n149#4:6265\n149#4:6266\n149#4:6267\n149#4:6280\n149#4:6281\n149#4:6282\n149#4:6319\n149#4:6320\n149#4:6332\n149#4:6374\n149#4:6375\n149#4:6376\n149#4:6447\n149#4:6454\n149#4:6455\n149#4:6462\n149#4:6470\n159#4:6471\n149#4:6507\n149#4:6512\n149#4:6513\n149#4:6554\n159#4:6555\n149#4:6591\n149#4:6601\n149#4:6602\n149#4:6603\n149#4:6647\n149#4:6654\n149#4:6691\n149#4:6701\n149#4:6738\n149#4:6739\n149#4:6783\n149#4:6784\n149#4:6827\n149#4:6828\n149#4:6829\n149#4:6834\n149#4:6871\n149#4:6872\n149#4:6919\n149#4:6938\n149#4:6939\n149#4:6972\n149#4:7046\n149#4:7047\n149#4:7052\n149#4:7053\n149#4:7058\n149#4:7091\n149#4:7092\n149#4:7099\n149#4:7100\n149#4:7101\n149#4:7102\n149#4:7107\n149#4:7144\n149#4:7145\n149#4:7146\n149#4:7147\n149#4:7221\n149#4:7222\n149#4:7227\n149#4:7228\n149#4:7229\n149#4:7236\n149#4:7237\n149#4:7242\n149#4:7279\n149#4:7280\n149#4:7281\n149#4:7282\n149#4:7315\n159#4:7320\n149#4:7321\n149#4:7322\n149#4:7323\n149#4:7362\n481#5:3266\n480#5,4:3267\n484#5,2:3274\n488#5:3280\n480#6:3276\n99#7,3:3303\n102#7:3334\n106#7:3384\n99#7,3:3457\n102#7:3488\n106#7:3515\n99#7:3517\n95#7,7:3518\n102#7:3553\n106#7:3569\n99#7:3652\n95#7,7:3653\n102#7:3688\n106#7:3695\n99#7:3697\n95#7,7:3698\n102#7:3733\n106#7:3741\n99#7,3:3946\n102#7:3977\n106#7:3982\n99#7:4168\n97#7,5:4169\n102#7:4202\n99#7:4211\n96#7,6:4212\n102#7:4246\n106#7:4253\n99#7:4262\n96#7,6:4263\n102#7:4297\n106#7:4304\n99#7:4313\n96#7,6:4314\n102#7:4348\n106#7:4355\n99#7:4364\n96#7,6:4365\n102#7:4399\n106#7:4406\n106#7:4410\n99#7,3:4537\n102#7:4568\n106#7:4579\n99#7,3:4693\n102#7:4724\n99#7:4725\n95#7,7:4726\n102#7:4761\n106#7:4853\n106#7:4917\n99#7,3:5194\n102#7:5225\n106#7:5235\n99#7,3:5347\n102#7:5378\n106#7:5389\n99#7,3:5397\n102#7:5428\n106#7:5439\n99#7:5456\n96#7,6:5457\n102#7:5491\n106#7:5545\n99#7:5604\n95#7,7:5605\n102#7:5640\n106#7:5646\n99#7:5697\n95#7,7:5698\n102#7:5733\n106#7:5739\n99#7:5743\n97#7,5:5744\n102#7:5777\n106#7:5793\n99#7,3:5799\n102#7:5830\n106#7:5874\n99#7:6070\n96#7,6:6071\n102#7:6105\n99#7:6146\n95#7,7:6147\n102#7:6182\n106#7:6188\n106#7:6198\n99#7:6283\n97#7,5:6284\n102#7:6317\n106#7:6331\n99#7:6333\n97#7,5:6334\n102#7:6367\n106#7:6386\n99#7:6702\n96#7,6:6703\n102#7:6737\n106#7:6788\n99#7:7009\n95#7,7:7010\n102#7:7045\n106#7:7051\n99#7:7184\n95#7,7:7185\n102#7:7220\n106#7:7226\n99#7,3:7283\n102#7:7314\n106#7:7319\n79#8,6:3306\n86#8,4:3321\n90#8,2:3331\n94#8:3383\n79#8,6:3416\n86#8,4:3431\n90#8,2:3441\n94#8:3447\n79#8,6:3460\n86#8,4:3475\n90#8,2:3485\n94#8:3514\n79#8,6:3525\n86#8,4:3540\n90#8,2:3550\n94#8:3568\n79#8,6:3605\n86#8,4:3620\n90#8,2:3630\n79#8,6:3660\n86#8,4:3675\n90#8,2:3685\n94#8:3694\n79#8,6:3705\n86#8,4:3720\n90#8,2:3730\n94#8:3740\n94#8:3744\n79#8,6:3831\n86#8,4:3846\n90#8,2:3856\n79#8,6:3863\n86#8,4:3878\n90#8,2:3888\n79#8,6:3899\n86#8,4:3914\n90#8,2:3924\n94#8:3931\n79#8,6:3949\n86#8,4:3964\n90#8,2:3974\n94#8:3981\n79#8,6:3992\n86#8,4:4007\n90#8,2:4017\n79#8,6:4043\n86#8,4:4058\n90#8,2:4068\n94#8:4074\n94#8:4078\n94#8:4097\n94#8:4101\n79#8,6:4135\n86#8,4:4150\n90#8,2:4160\n79#8,6:4174\n86#8,4:4189\n90#8,2:4199\n79#8,6:4218\n86#8,4:4233\n90#8,2:4243\n94#8:4252\n79#8,6:4269\n86#8,4:4284\n90#8,2:4294\n94#8:4303\n79#8,6:4320\n86#8,4:4335\n90#8,2:4345\n94#8:4354\n79#8,6:4371\n86#8,4:4386\n90#8,2:4396\n94#8:4405\n94#8:4409\n94#8:4413\n79#8,6:4508\n86#8,4:4523\n90#8,2:4533\n79#8,6:4540\n86#8,4:4555\n90#8,2:4565\n94#8:4578\n94#8:4603\n79#8,6:4612\n86#8,4:4627\n90#8,2:4637\n94#8:4655\n79#8,6:4664\n86#8,4:4679\n90#8,2:4689\n79#8,6:4696\n86#8,4:4711\n90#8,2:4721\n79#8,6:4733\n86#8,4:4748\n90#8,2:4758\n79#8,6:4772\n86#8,4:4787\n90#8,2:4797\n94#8:4804\n79#8,6:4815\n86#8,4:4830\n90#8,2:4840\n94#8:4848\n94#8:4852\n79#8,6:4875\n86#8,4:4890\n90#8,2:4900\n94#8:4912\n94#8:4916\n79#8,6:4931\n86#8,4:4946\n90#8,2:4956\n94#8:4968\n94#8:4972\n79#8,6:5023\n86#8,4:5038\n90#8,2:5048\n79#8,6:5060\n86#8,4:5075\n90#8,2:5085\n94#8:5104\n94#8:5108\n79#8,6:5130\n86#8,4:5145\n90#8,2:5155\n94#8:5170\n79#8,6:5197\n86#8,4:5212\n90#8,2:5222\n94#8:5234\n79#8,6:5257\n86#8,4:5272\n90#8,2:5282\n94#8:5289\n79#8,6:5317\n86#8,4:5332\n90#8,2:5342\n79#8,6:5350\n86#8,4:5365\n90#8,2:5375\n94#8:5388\n79#8,6:5400\n86#8,4:5415\n90#8,2:5425\n94#8:5438\n79#8,6:5463\n86#8,4:5478\n90#8,2:5488\n79#8,6:5508\n86#8,4:5523\n90#8,2:5533\n94#8:5539\n94#8:5544\n94#8:5555\n79#8,6:5575\n86#8,4:5590\n90#8,2:5600\n79#8,6:5612\n86#8,4:5627\n90#8,2:5637\n94#8:5645\n94#8:5658\n79#8,6:5668\n86#8,4:5683\n90#8,2:5693\n79#8,6:5705\n86#8,4:5720\n90#8,2:5730\n94#8:5738\n79#8,6:5749\n86#8,4:5764\n90#8,2:5774\n94#8:5792\n94#8:5796\n79#8,6:5802\n86#8,4:5817\n90#8,2:5827\n79#8,6:5838\n86#8,4:5853\n90#8,2:5863\n94#8:5869\n94#8:5873\n79#8,6:5883\n86#8,4:5898\n90#8,2:5908\n94#8:5970\n79#8,6:5979\n86#8,4:5994\n90#8,2:6004\n79#8,6:6024\n86#8,4:6039\n90#8,2:6049\n94#8:6061\n94#8:6065\n79#8,6:6077\n86#8,4:6092\n90#8,2:6102\n79#8,6:6113\n86#8,4:6128\n90#8,2:6138\n94#8:6144\n79#8,6:6154\n86#8,4:6169\n90#8,2:6179\n94#8:6187\n94#8:6197\n79#8,6:6236\n86#8,4:6251\n90#8,2:6261\n79#8,6:6289\n86#8,4:6304\n90#8,2:6314\n94#8:6330\n79#8,6:6339\n86#8,4:6354\n90#8,2:6364\n94#8:6385\n94#8:6389\n79#8,6:6418\n86#8,4:6433\n90#8,2:6443\n94#8:6465\n79#8,6:6478\n86#8,4:6493\n90#8,2:6503\n94#8:6510\n79#8,6:6520\n86#8,4:6535\n90#8,2:6545\n94#8:6551\n79#8,6:6562\n86#8,4:6577\n90#8,2:6587\n94#8:6594\n79#8,6:6618\n86#8,4:6633\n90#8,2:6643\n79#8,6:6662\n86#8,4:6677\n90#8,2:6687\n94#8:6695\n94#8:6699\n79#8,6:6709\n86#8,4:6724\n90#8,2:6734\n79#8,6:6748\n86#8,4:6763\n90#8,2:6773\n94#8:6781\n94#8:6787\n79#8,6:6798\n86#8,4:6813\n90#8,2:6823\n94#8:6832\n79#8,6:6842\n86#8,4:6857\n90#8,2:6867\n79#8,6:6880\n86#8,4:6895\n90#8,2:6905\n94#8:6917\n94#8:6922\n79#8,6:6943\n86#8,4:6958\n90#8,2:6968\n79#8,6:6980\n86#8,4:6995\n90#8,2:7005\n79#8,6:7017\n86#8,4:7032\n90#8,2:7042\n94#8:7050\n94#8:7056\n79#8,6:7062\n86#8,4:7077\n90#8,2:7087\n94#8:7105\n79#8,6:7115\n86#8,4:7130\n90#8,2:7140\n79#8,6:7155\n86#8,4:7170\n90#8,2:7180\n79#8,6:7192\n86#8,4:7207\n90#8,2:7217\n94#8:7225\n94#8:7240\n79#8,6:7250\n86#8,4:7265\n90#8,2:7275\n79#8,6:7286\n86#8,4:7301\n90#8,2:7311\n94#8:7318\n94#8:7332\n94#8:7336\n94#8:7340\n368#9,9:3312\n377#9:3333\n378#9,2:3381\n368#9,9:3422\n377#9:3443\n378#9,2:3445\n368#9,9:3466\n377#9:3487\n378#9,2:3512\n368#9,9:3531\n377#9:3552\n378#9,2:3566\n368#9,9:3611\n377#9:3632\n368#9,9:3666\n377#9:3687\n378#9,2:3692\n368#9,9:3711\n377#9:3732\n378#9,2:3738\n378#9,2:3742\n368#9,9:3837\n377#9:3858\n368#9,9:3869\n377#9:3890\n368#9,9:3905\n377#9:3926\n378#9,2:3929\n368#9,9:3955\n377#9:3976\n378#9,2:3979\n368#9,9:3998\n377#9:4019\n368#9,9:4049\n377#9:4070\n378#9,2:4072\n378#9,2:4076\n378#9,2:4095\n378#9,2:4099\n368#9,9:4141\n377#9:4162\n368#9,9:4180\n377#9:4201\n368#9,9:4224\n377#9:4245\n378#9,2:4250\n368#9,9:4275\n377#9:4296\n378#9,2:4301\n368#9,9:4326\n377#9:4347\n378#9,2:4352\n368#9,9:4377\n377#9:4398\n378#9,2:4403\n378#9,2:4407\n378#9,2:4411\n368#9,9:4514\n377#9:4535\n368#9,9:4546\n377#9:4567\n378#9,2:4576\n378#9,2:4601\n368#9,9:4618\n377#9:4639\n378#9,2:4653\n368#9,9:4670\n377#9:4691\n368#9,9:4702\n377#9:4723\n368#9,9:4739\n377#9:4760\n368#9,9:4778\n377#9:4799\n378#9,2:4802\n368#9,9:4821\n377#9:4842\n378#9,2:4846\n378#9,2:4850\n368#9,9:4881\n377#9:4902\n378#9,2:4910\n378#9,2:4914\n368#9,9:4937\n377#9:4958\n378#9,2:4966\n378#9,2:4970\n368#9,9:5029\n377#9:5050\n368#9,9:5066\n377#9:5087\n378#9,2:5102\n378#9,2:5106\n368#9,9:5136\n377#9:5157\n378#9,2:5168\n368#9,9:5203\n377#9:5224\n378#9,2:5232\n368#9,9:5263\n377#9:5284\n378#9,2:5287\n368#9,9:5323\n377#9:5344\n368#9,9:5356\n377#9:5377\n378#9,2:5386\n368#9,9:5406\n377#9:5427\n378#9,2:5436\n368#9,9:5469\n377#9:5490\n368#9,9:5514\n377#9:5535\n378#9,2:5537\n378#9,2:5542\n378#9,2:5553\n368#9,9:5581\n377#9:5602\n368#9,9:5618\n377#9:5639\n378#9,2:5643\n378#9,2:5656\n368#9,9:5674\n377#9:5695\n368#9,9:5711\n377#9:5732\n378#9,2:5736\n368#9,9:5755\n377#9:5776\n378#9,2:5790\n378#9,2:5794\n368#9,9:5808\n377#9:5829\n368#9,9:5844\n377#9:5865\n378#9,2:5867\n378#9,2:5871\n368#9,9:5889\n377#9:5910\n378#9,2:5968\n368#9,9:5985\n377#9:6006\n368#9,9:6030\n377#9:6051\n378#9,2:6059\n378#9,2:6063\n368#9,9:6083\n377#9:6104\n368#9,9:6119\n377#9:6140\n378#9,2:6142\n368#9,9:6160\n377#9:6181\n378#9,2:6185\n378#9,2:6195\n368#9,9:6242\n377#9:6263\n368#9,9:6295\n377#9:6316\n378#9,2:6328\n368#9,9:6345\n377#9:6366\n378#9,2:6383\n378#9,2:6387\n368#9,9:6424\n377#9:6445\n378#9,2:6463\n368#9,9:6484\n377#9:6505\n378#9,2:6508\n368#9,9:6526\n377#9:6547\n378#9,2:6549\n368#9,9:6568\n377#9:6589\n378#9,2:6592\n368#9,9:6624\n377#9:6645\n368#9,9:6668\n377#9:6689\n378#9,2:6693\n378#9,2:6697\n368#9,9:6715\n377#9:6736\n368#9,9:6754\n377#9:6775\n378#9,2:6779\n378#9,2:6785\n368#9,9:6804\n377#9:6825\n378#9,2:6830\n368#9,9:6848\n377#9:6869\n368#9,9:6886\n377#9:6907\n378#9,2:6915\n378#9,2:6920\n368#9,9:6949\n377#9:6970\n368#9,9:6986\n377#9:7007\n368#9,9:7023\n377#9:7044\n378#9,2:7048\n378#9,2:7054\n368#9,9:7068\n377#9:7089\n378#9,2:7103\n368#9,9:7121\n377#9:7142\n368#9,9:7161\n377#9:7182\n368#9,9:7198\n377#9:7219\n378#9,2:7223\n378#9,2:7238\n368#9,9:7256\n377#9:7277\n368#9,9:7292\n377#9:7313\n378#9,2:7316\n378#9,2:7330\n378#9,2:7334\n378#9,2:7338\n4034#10,6:3325\n4034#10,6:3435\n4034#10,6:3479\n4034#10,6:3544\n4034#10,6:3624\n4034#10,6:3679\n4034#10,6:3724\n4034#10,6:3850\n4034#10,6:3882\n4034#10,6:3918\n4034#10,6:3968\n4034#10,6:4011\n4034#10,6:4062\n4034#10,6:4154\n4034#10,6:4193\n4034#10,6:4237\n4034#10,6:4288\n4034#10,6:4339\n4034#10,6:4390\n4034#10,6:4527\n4034#10,6:4559\n4034#10,6:4631\n4034#10,6:4683\n4034#10,6:4715\n4034#10,6:4752\n4034#10,6:4791\n4034#10,6:4834\n4034#10,6:4894\n4034#10,6:4950\n4034#10,6:5042\n4034#10,6:5079\n4034#10,6:5149\n4034#10,6:5216\n4034#10,6:5276\n4034#10,6:5336\n4034#10,6:5369\n4034#10,6:5419\n4034#10,6:5482\n4034#10,6:5527\n4034#10,6:5594\n4034#10,6:5631\n4034#10,6:5687\n4034#10,6:5724\n4034#10,6:5768\n4034#10,6:5821\n4034#10,6:5857\n4034#10,6:5902\n4034#10,6:5998\n4034#10,6:6043\n4034#10,6:6096\n4034#10,6:6132\n4034#10,6:6173\n4034#10,6:6255\n4034#10,6:6308\n4034#10,6:6358\n4034#10,6:6437\n4034#10,6:6497\n4034#10,6:6539\n4034#10,6:6581\n4034#10,6:6637\n4034#10,6:6681\n4034#10,6:6728\n4034#10,6:6767\n4034#10,6:6817\n4034#10,6:6861\n4034#10,6:6899\n4034#10,6:6962\n4034#10,6:6999\n4034#10,6:7036\n4034#10,6:7081\n4034#10,6:7134\n4034#10,6:7174\n4034#10,6:7211\n4034#10,6:7269\n4034#10,6:7305\n1188#11,2:3335\n1190#11:3380\n1188#11,2:3554\n1190#11:3565\n434#11:5297\n507#11,5:5298\n434#11:5303\n507#11,5:5304\n71#12:3410\n69#12,5:3411\n74#12:3444\n78#12:3448\n71#12:3825\n69#12,5:3826\n74#12:3859\n71#12:3893\n69#12,5:3894\n74#12:3927\n78#12:3932\n71#12:3986\n69#12,5:3987\n74#12:4020\n78#12:4079\n78#12:4102\n71#12:4129\n69#12,5:4130\n74#12:4163\n78#12:4414\n71#12:4766\n69#12,5:4767\n74#12:4800\n78#12:4805\n71#12:4869\n69#12,5:4870\n74#12:4903\n78#12:4913\n71#12:4924\n68#12,6:4925\n74#12:4959\n78#12:4969\n71#12:5016\n68#12,6:5017\n74#12:5051\n78#12:5109\n71#12:5250\n68#12,6:5251\n74#12:5285\n78#12:5290\n71#12:5502\n69#12,5:5503\n74#12:5536\n78#12:5540\n71#12:6017\n68#12,6:6018\n74#12:6052\n78#12:6062\n71#12:6412\n69#12,5:6413\n74#12:6446\n78#12:6466\n71#12:6472\n69#12,5:6473\n74#12:6506\n78#12:6511\n71#12:6514\n69#12,5:6515\n74#12:6548\n78#12:6552\n71#12:6556\n69#12,5:6557\n74#12:6590\n78#12:6595\n71#12:6655\n68#12,6:6656\n74#12:6690\n78#12:6696\n71#12:6835\n68#12,6:6836\n74#12:6870\n78#12:6923\n86#13:3598\n83#13,6:3599\n89#13:3633\n93#13:3745\n86#13,3:3860\n89#13:3891\n86#13:4035\n82#13,7:4036\n89#13:4071\n93#13:4075\n93#13:4098\n86#13:4502\n84#13,5:4503\n89#13:4536\n93#13:4604\n86#13:4605\n83#13,6:4606\n89#13:4640\n93#13:4656\n86#13:4657\n83#13,6:4658\n89#13:4692\n86#13:4807\n82#13,7:4808\n89#13:4843\n93#13:4849\n93#13:4973\n86#13:5052\n82#13,7:5053\n89#13:5088\n93#13:5105\n86#13:5123\n83#13,6:5124\n89#13:5158\n93#13:5171\n86#13:5310\n83#13,6:5311\n89#13:5345\n93#13:5556\n86#13:5568\n83#13,6:5569\n89#13:5603\n93#13:5659\n86#13:5661\n83#13,6:5662\n89#13:5696\n93#13:5797\n86#13:5831\n83#13,6:5832\n89#13:5866\n93#13:5870\n86#13:5876\n83#13,6:5877\n89#13:5911\n93#13:5971\n86#13:5972\n83#13,6:5973\n89#13:6007\n93#13:6066\n86#13:6106\n83#13,6:6107\n89#13:6141\n93#13:6145\n86#13:6229\n83#13,6:6230\n89#13:6264\n93#13:6390\n86#13:6610\n82#13,7:6611\n89#13:6646\n93#13:6700\n86#13:6740\n82#13,7:6741\n89#13:6776\n93#13:6782\n86#13:6791\n83#13,6:6792\n89#13:6826\n93#13:6833\n86#13:6873\n83#13,6:6874\n89#13:6908\n93#13:6918\n86#13,3:6940\n89#13:6971\n86#13:6973\n83#13,6:6974\n89#13:7008\n93#13:7057\n86#13,3:7059\n89#13:7090\n93#13:7106\n86#13:7108\n83#13,6:7109\n89#13:7143\n86#13:7148\n83#13,6:7149\n89#13:7183\n93#13:7241\n86#13:7243\n83#13,6:7244\n89#13:7278\n93#13:7333\n93#13:7337\n93#13:7341\n81#14:4103\n81#14:4104\n81#14:4105\n81#14:4106\n81#14:4107\n81#14:4108\n107#14,2:4109\n81#14:4123\n81#14:4124\n81#14:4125\n81#14:4126\n81#14:4470\n81#14:4471\n81#14:4472\n81#14:4473\n81#14:4474\n81#14:4475\n81#14:4476\n81#14:4477\n81#14:4478\n81#14:4479\n107#14,2:4480\n81#14:4482\n81#14:4483\n81#14:4484\n107#14,2:4485\n81#14:4487\n107#14,2:4488\n81#14:4490\n81#14:4497\n81#14:4498\n81#14:5175\n107#14,2:5176\n81#14:5178\n107#14,2:5179\n81#14:5181\n107#14,2:5182\n81#14:5184\n107#14,2:5185\n81#14:5187\n107#14,2:5188\n81#14:5190\n107#14,2:5191\n81#14:5236\n81#14:5237\n81#14:5238\n81#14:5291\n107#14,2:5292\n81#14:5294\n107#14,2:5295\n81#14:5557\n81#14:5558\n81#14:5559\n107#14,2:5560\n81#14:6067\n81#14:6068\n81#14:6199\n107#14,2:6200\n81#14:6202\n81#14:6203\n81#14:6204\n107#14,2:6205\n81#14:6207\n107#14,2:6208\n81#14:6467\n81#14:6468\n81#14:6469\n81#14:6553\n81#14:6598\n107#14,2:6599\n81#14:6789\n81#14:6790\n81#14:6929\n81#14:6930\n81#14:6937\n81#14:7342\n107#14,2:7343\n81#14:7345\n107#14,2:7346\n81#14:7348\n107#14,2:7349\n81#14:7351\n107#14,2:7352\n81#14:7359\n81#14:7360\n81#14:7361\n84#15:4249\n84#15:4300\n84#15:4351\n84#15:4402\n51#15:4501\n51#15:4652\n84#15:4990\n84#15:4994\n1#16:4499\n1869#17:4580\n1870#17:4593\n1878#17,2:5492\n1880#17:5541\n1869#17:6318\n1870#17:6327\n143#18,12:5110\n78#19:5172\n111#19,2:5173\n64#20,5:5562\n64#20,5:6924\n64#20,5:7354\n*S KotlinDebug\n*F\n+ 1 MainActivity.kt\ncom/example/MainActivityKt\n*L\n173#1:3085\n183#1:3092\n185#1:3093\n483#1:3108\n496#1:3115\n497#1:3116\n520#1:3147\n521#1:3148\n931#1:3174\n932#1:3175\n1003#1:3281\n1452#1:3282\n1453#1:3283\n1454#1:3284\n1455#1:3285\n1561#1:3299\n1562#1:3300\n1630#1:3385\n1631#1:3386\n1809#1:3388\n1822#1:3397\n1858#1:3455\n1859#1:3456\n1968#1:3516\n1980#1:3556\n1981#1:3557\n2029#1:3571\n2030#1:3572\n2031#1:3573\n2195#1:3691\n2204#1:3734\n2228#1:3746\n2229#1:3747\n2359#1:3761\n2361#1:3762\n2729#1:3787\n244#1:4127\n246#1:4128\n543#1:4986\n1297#1:5933\n1047#1:6008\n1658#1:6403\n1659#1:6404\n1660#1:6405\n2009#1:6596\n2010#1:6597\n2125#1:6692\n2164#1:6777\n2170#1:6778\n182#1:3086,6\n187#1:3094,6\n196#1:3101,6\n484#1:3109,6\n498#1:3117,6\n502#1:3123,6\n503#1:3129,6\n507#1:3135,6\n514#1:3141,6\n824#1:3149,6\n878#1:3155,6\n884#1:3161,6\n890#1:3168,6\n933#1:3176,6\n935#1:3182,6\n938#1:3188,6\n939#1:3194,6\n941#1:3200,6\n948#1:3206,6\n952#1:3212,6\n953#1:3218,6\n954#1:3224,6\n955#1:3230,6\n959#1:3236,6\n961#1:3242,6\n968#1:3248,6\n981#1:3254,6\n997#1:3260,6\n1002#1:3271,3\n1002#1:3277,3\n1480#1:3292,6\n1582#1:3337,6\n1583#1:3343,6\n1584#1:3349,6\n1586#1:3355,6\n1595#1:3361,6\n1601#1:3367,6\n1615#1:3373,6\n1810#1:3389,6\n1826#1:3398,6\n1843#1:3404,6\n1857#1:3449,6\n1869#1:3490,6\n1893#1:3498,6\n1933#1:3506,6\n1987#1:3559,6\n2032#1:3574,6\n2035#1:3580,6\n2044#1:3586,6\n2046#1:3592,6\n2059#1:3635,6\n2142#1:3646,6\n2266#1:3753,6\n2364#1:3763,6\n2369#1:3769,6\n2437#1:3775,6\n2439#1:3781,6\n2730#1:3788,6\n2731#1:3794,6\n2732#1:3800,6\n2733#1:3806,6\n2736#1:3812,6\n2741#1:3818,6\n2820#1:3938,6\n2935#1:4021,6\n2944#1:4028,6\n3001#1:4085,6\n200#1:4111,6\n212#1:4117,6\n268#1:4204,6\n303#1:4255,6\n338#1:4306,6\n373#1:4357,6\n235#1:4415,6\n238#1:4421,6\n442#1:4428,6\n467#1:4434,6\n409#1:4440,6\n416#1:4446,6\n419#1:4452,6\n426#1:4458,6\n429#1:4464,6\n582#1:4491,6\n730#1:4569,6\n747#1:4581,6\n748#1:4587,6\n754#1:4594,6\n773#1:4645,6\n629#1:4854,6\n647#1:4863,6\n659#1:4904,6\n683#1:4918,6\n705#1:4960,6\n809#1:4974,6\n818#1:4980,6\n558#1:4995,6\n814#1:5002,6\n815#1:5008,6\n835#1:5089,6\n861#1:5096,6\n911#1:5162,6\n1025#1:5226,6\n1083#1:5244,6\n1150#1:5380,6\n1165#1:5390,6\n1184#1:5430,6\n1199#1:5440,6\n1218#1:5447,6\n1245#1:5495,6\n1266#1:5547,6\n1335#1:5650,6\n1387#1:5778,6\n1394#1:5784,6\n1133#1:5913,6\n1134#1:5919,6\n1289#1:5927,6\n1298#1:5934,6\n1300#1:5940,6\n1410#1:5961,6\n1053#1:6011,6\n1107#1:6053,6\n1546#1:6189,6\n1688#1:6210,6\n1696#1:6216,6\n1701#1:6222,6\n1737#1:6268,6\n1721#1:6274,6\n1760#1:6321,6\n1772#1:6368,6\n1784#1:6377,6\n1640#1:6391,6\n1641#1:6397,6\n1666#1:6406,6\n1676#1:6448,6\n1683#1:6456,6\n2070#1:6604,6\n2103#1:6648,6\n2302#1:6909,6\n2543#1:6931,6\n2528#1:7093,6\n2625#1:7230,6\n2693#1:7324,6\n222#1:3100\n482#1:3107\n892#1:3167\n1462#1:3286\n1462#1:3287\n1473#1:3288\n1477#1:3289\n1478#1:3290\n1479#1:3291\n1486#1:3298\n1567#1:3301\n1568#1:3302\n1621#1:3379\n1805#1:3387\n1818#1:3395\n1818#1:3396\n1867#1:3489\n1886#1:3496\n1891#1:3497\n1925#1:3504\n1931#1:3505\n1982#1:3558\n2027#1:3570\n2056#1:3634\n2065#1:3641\n2133#1:3642\n2137#1:3643\n2140#1:3644\n2141#1:3645\n2186#1:3689\n2190#1:3690\n2199#1:3696\n2213#1:3735\n2216#1:3736\n2217#1:3737\n2255#1:3748\n2257#1:3749\n2260#1:3750\n2262#1:3751\n2263#1:3752\n2271#1:3759\n2273#1:3760\n2774#1:3824\n2786#1:3892\n2795#1:3928\n2799#1:3933\n2810#1:3934\n2818#1:3935\n2850#1:3936\n2853#1:3937\n2878#1:3944\n2879#1:3945\n2883#1:3978\n2906#1:3983\n2917#1:3984\n2922#1:3985\n2938#1:4027\n2947#1:4034\n2972#1:4080\n2979#1:4081\n2982#1:4082\n2999#1:4083\n3000#1:4084\n3024#1:4091\n3032#1:4092\n3035#1:4093\n3077#1:4094\n253#1:4164\n254#1:4165\n256#1:4166\n258#1:4167\n266#1:4203\n274#1:4210\n281#1:4247\n284#1:4248\n301#1:4254\n309#1:4261\n316#1:4298\n319#1:4299\n336#1:4305\n344#1:4312\n351#1:4349\n354#1:4350\n371#1:4356\n379#1:4363\n386#1:4400\n389#1:4401\n242#1:4427\n716#1:4500\n730#1:4575\n755#1:4600\n771#1:4641\n778#1:4642\n779#1:4643\n780#1:4644\n792#1:4651\n596#1:4762\n597#1:4763\n598#1:4764\n600#1:4765\n605#1:4801\n609#1:4806\n617#1:4844\n624#1:4845\n639#1:4860\n640#1:4861\n643#1:4862\n546#1:4987\n547#1:4988\n548#1:4989\n552#1:4991\n553#1:4992\n554#1:4993\n562#1:5001\n828#1:5014\n830#1:5015\n859#1:5095\n897#1:5122\n905#1:5159\n908#1:5160\n910#1:5161\n1012#1:5193\n1065#1:5239\n1073#1:5240\n1076#1:5241\n1079#1:5242\n1081#1:5243\n1094#1:5286\n1142#1:5309\n1144#1:5346\n1159#1:5379\n1178#1:5396\n1193#1:5429\n1213#1:5446\n1233#1:5453\n1236#1:5454\n1237#1:5455\n1243#1:5494\n1256#1:5501\n1262#1:5546\n1320#1:5567\n1322#1:5641\n1323#1:5642\n1326#1:5647\n1333#1:5648\n1342#1:5649\n1371#1:5660\n1373#1:5734\n1374#1:5735\n1377#1:5740\n1384#1:5741\n1385#1:5742\n1424#1:5798\n1128#1:5875\n1131#1:5912\n1138#1:5925\n1140#1:5926\n1314#1:5946\n1315#1:5947\n1317#1:5948\n1318#1:5949\n1350#1:5950\n1351#1:5951\n1353#1:5952\n1354#1:5953\n1365#1:5954\n1366#1:5955\n1368#1:5956\n1369#1:5957\n1417#1:5958\n1420#1:5959\n1421#1:5960\n1441#1:5967\n1051#1:6009\n1052#1:6010\n1541#1:6069\n1521#1:6183\n1525#1:6184\n1704#1:6228\n1713#1:6265\n1717#1:6266\n1724#1:6267\n1746#1:6280\n1748#1:6281\n1750#1:6282\n1756#1:6319\n1759#1:6320\n1769#1:6332\n1781#1:6374\n1789#1:6375\n1790#1:6376\n1672#1:6447\n1681#1:6454\n1682#1:6455\n1686#1:6462\n1876#1:6470\n1879#1:6471\n1882#1:6507\n1904#1:6512\n1911#1:6513\n1949#1:6554\n1952#1:6555\n1959#1:6591\n2076#1:6601\n2083#1:6602\n2070#1:6603\n2093#1:6647\n2119#1:6654\n2123#1:6691\n2149#1:6701\n2153#1:6738\n2157#1:6739\n2173#1:6783\n2178#1:6784\n2323#1:6827\n2325#1:6828\n2329#1:6829\n2279#1:6834\n2290#1:6871\n2296#1:6872\n2346#1:6919\n2442#1:6938\n2449#1:6939\n2457#1:6972\n2465#1:7046\n2469#1:7047\n2490#1:7052\n2493#1:7053\n2513#1:7058\n2522#1:7091\n2531#1:7092\n2550#1:7099\n2562#1:7100\n2565#1:7101\n2567#1:7102\n2574#1:7107\n2596#1:7144\n2597#1:7145\n2598#1:7146\n2599#1:7147\n2609#1:7221\n2611#1:7222\n2621#1:7227\n2630#1:7228\n2631#1:7229\n2634#1:7236\n2643#1:7237\n2667#1:7242\n2673#1:7279\n2674#1:7280\n2675#1:7281\n2676#1:7282\n2681#1:7315\n2698#1:7320\n2699#1:7321\n2700#1:7322\n2701#1:7323\n3020#1:7362\n1002#1:3266\n1002#1:3267,4\n1002#1:3274,2\n1002#1:3280\n1002#1:3276\n1564#1:3303,3\n1564#1:3334\n1564#1:3384\n1860#1:3457,3\n1860#1:3488\n1860#1:3515\n1970#1:3517\n1970#1:3518,7\n1970#1:3553\n1970#1:3569\n2183#1:3652\n2183#1:3653,7\n2183#1:3688\n2183#1:3695\n2200#1:3697\n2200#1:3698,7\n2200#1:3733\n2200#1:3741\n2876#1:3946,3\n2876#1:3977\n2876#1:3982\n251#1:4168\n251#1:4169,5\n251#1:4202\n264#1:4211\n264#1:4212,6\n264#1:4246\n264#1:4253\n299#1:4262\n299#1:4263,6\n299#1:4297\n299#1:4304\n334#1:4313\n334#1:4314,6\n334#1:4348\n334#1:4355\n369#1:4364\n369#1:4365,6\n369#1:4399\n369#1:4406\n251#1:4410\n718#1:4537,3\n718#1:4568\n718#1:4579\n588#1:4693,3\n588#1:4724\n593#1:4725\n593#1:4726,7\n593#1:4761\n593#1:4853\n588#1:4917\n1008#1:5194,3\n1008#1:5225\n1008#1:5235\n1146#1:5347,3\n1146#1:5378\n1146#1:5389\n1180#1:5397,3\n1180#1:5428\n1180#1:5439\n1237#1:5456\n1237#1:5457,6\n1237#1:5491\n1237#1:5545\n1321#1:5604\n1321#1:5605,7\n1321#1:5640\n1321#1:5646\n1372#1:5697\n1372#1:5698,7\n1372#1:5733\n1372#1:5739\n1385#1:5743\n1385#1:5744,5\n1385#1:5777\n1385#1:5793\n1423#1:5799,3\n1423#1:5830\n1423#1:5874\n1488#1:6070\n1488#1:6071,6\n1488#1:6105\n1517#1:6146\n1517#1:6147,7\n1517#1:6182\n1517#1:6188\n1488#1:6198\n1749#1:6283\n1749#1:6284,5\n1749#1:6317\n1749#1:6331\n1770#1:6333\n1770#1:6334,5\n1770#1:6367\n1770#1:6386\n2147#1:6702\n2147#1:6703,6\n2147#1:6737\n2147#1:6788\n2459#1:7009\n2459#1:7010,7\n2459#1:7045\n2459#1:7051\n2601#1:7184\n2601#1:7185,7\n2601#1:7220\n2601#1:7226\n2669#1:7283,3\n2669#1:7314\n2669#1:7319\n1564#1:3306,6\n1564#1:3321,4\n1564#1:3331,2\n1564#1:3383\n1824#1:3416,6\n1824#1:3431,4\n1824#1:3441,2\n1824#1:3447\n1860#1:3460,6\n1860#1:3475,4\n1860#1:3485,2\n1860#1:3514\n1970#1:3525,6\n1970#1:3540,4\n1970#1:3550,2\n1970#1:3568\n2050#1:3605,6\n2050#1:3620,4\n2050#1:3630,2\n2183#1:3660,6\n2183#1:3675,4\n2183#1:3685,2\n2183#1:3694\n2200#1:3705,6\n2200#1:3720,4\n2200#1:3730,2\n2200#1:3740\n2050#1:3744\n2770#1:3831,6\n2770#1:3846,4\n2770#1:3856,2\n2777#1:3863,6\n2777#1:3878,4\n2777#1:3888,2\n2784#1:3899,6\n2784#1:3914,4\n2784#1:3924,2\n2784#1:3931\n2876#1:3949,6\n2876#1:3964,4\n2876#1:3974,2\n2876#1:3981\n2920#1:3992,6\n2920#1:4007,4\n2920#1:4017,2\n2951#1:4043,6\n2951#1:4058,4\n2951#1:4068,2\n2951#1:4074\n2920#1:4078\n2777#1:4097\n2770#1:4101\n247#1:4135,6\n247#1:4150,4\n247#1:4160,2\n251#1:4174,6\n251#1:4189,4\n251#1:4199,2\n264#1:4218,6\n264#1:4233,4\n264#1:4243,2\n264#1:4252\n299#1:4269,6\n299#1:4284,4\n299#1:4294,2\n299#1:4303\n334#1:4320,6\n334#1:4335,4\n334#1:4345,2\n334#1:4354\n369#1:4371,6\n369#1:4386,4\n369#1:4396,2\n369#1:4405\n251#1:4409\n247#1:4413\n714#1:4508,6\n714#1:4523,4\n714#1:4533,2\n718#1:4540,6\n718#1:4555,4\n718#1:4565,2\n718#1:4578\n714#1:4603\n762#1:4612,6\n762#1:4627,4\n762#1:4637,2\n762#1:4655\n564#1:4664,6\n564#1:4679,4\n564#1:4689,2\n588#1:4696,6\n588#1:4711,4\n588#1:4721,2\n593#1:4733,6\n593#1:4748,4\n593#1:4758,2\n594#1:4772,6\n594#1:4787,4\n594#1:4797,2\n594#1:4804\n610#1:4815,6\n610#1:4830,4\n610#1:4840,2\n610#1:4848\n593#1:4852\n637#1:4875,6\n637#1:4890,4\n637#1:4900,2\n637#1:4912\n588#1:4916\n698#1:4931,6\n698#1:4946,4\n698#1:4956,2\n698#1:4968\n564#1:4972\n825#1:5023,6\n825#1:5038,4\n825#1:5048,2\n832#1:5060,6\n832#1:5075,4\n832#1:5085,2\n832#1:5104\n825#1:5108\n894#1:5130,6\n894#1:5145,4\n894#1:5155,2\n894#1:5170\n1008#1:5197,6\n1008#1:5212,4\n1008#1:5222,2\n1008#1:5234\n1069#1:5257,6\n1069#1:5272,4\n1069#1:5282,2\n1069#1:5289\n1142#1:5317,6\n1142#1:5332,4\n1142#1:5342,2\n1146#1:5350,6\n1146#1:5365,4\n1146#1:5375,2\n1146#1:5388\n1180#1:5400,6\n1180#1:5415,4\n1180#1:5425,2\n1180#1:5438\n1237#1:5463,6\n1237#1:5478,4\n1237#1:5488,2\n1240#1:5508,6\n1240#1:5523,4\n1240#1:5533,2\n1240#1:5539\n1237#1:5544\n1142#1:5555\n1320#1:5575,6\n1320#1:5590,4\n1320#1:5600,2\n1321#1:5612,6\n1321#1:5627,4\n1321#1:5637,2\n1321#1:5645\n1320#1:5658\n1371#1:5668,6\n1371#1:5683,4\n1371#1:5693,2\n1372#1:5705,6\n1372#1:5720,4\n1372#1:5730,2\n1372#1:5738\n1385#1:5749,6\n1385#1:5764,4\n1385#1:5774,2\n1385#1:5792\n1371#1:5796\n1423#1:5802,6\n1423#1:5817,4\n1423#1:5827,2\n1428#1:5838,6\n1428#1:5853,4\n1428#1:5863,2\n1428#1:5869\n1423#1:5873\n1125#1:5883,6\n1125#1:5898,4\n1125#1:5908,2\n1125#1:5970\n1042#1:5979,6\n1042#1:5994,4\n1042#1:6004,2\n1102#1:6024,6\n1102#1:6039,4\n1102#1:6049,2\n1102#1:6061\n1042#1:6065\n1488#1:6077,6\n1488#1:6092,4\n1488#1:6102,2\n1501#1:6113,6\n1501#1:6128,4\n1501#1:6138,2\n1501#1:6144\n1517#1:6154,6\n1517#1:6169,4\n1517#1:6179,2\n1517#1:6187\n1488#1:6197\n1698#1:6236,6\n1698#1:6251,4\n1698#1:6261,2\n1749#1:6289,6\n1749#1:6304,4\n1749#1:6314,2\n1749#1:6330\n1770#1:6339,6\n1770#1:6354,4\n1770#1:6364,2\n1770#1:6385\n1698#1:6389\n1662#1:6418,6\n1662#1:6433,4\n1662#1:6443,2\n1662#1:6465\n1874#1:6478,6\n1874#1:6493,4\n1874#1:6503,2\n1874#1:6510\n1902#1:6520,6\n1902#1:6535,4\n1902#1:6545,2\n1902#1:6551\n1947#1:6562,6\n1947#1:6577,4\n1947#1:6587,2\n1947#1:6594\n2087#1:6618,6\n2087#1:6633,4\n2087#1:6643,2\n2098#1:6662,6\n2098#1:6677,4\n2098#1:6687,2\n2098#1:6695\n2087#1:6699\n2147#1:6709,6\n2147#1:6724,4\n2147#1:6734,2\n2158#1:6748,6\n2158#1:6763,4\n2158#1:6773,2\n2158#1:6781\n2147#1:6787\n2310#1:6798,6\n2310#1:6813,4\n2310#1:6823,2\n2310#1:6832\n2276#1:6842,6\n2276#1:6857,4\n2276#1:6867,2\n2293#1:6880,6\n2293#1:6895,4\n2293#1:6905,2\n2293#1:6917\n2276#1:6922\n2444#1:6943,6\n2444#1:6958,4\n2444#1:6968,2\n2455#1:6980,6\n2455#1:6995,4\n2455#1:7005,2\n2459#1:7017,6\n2459#1:7032,4\n2459#1:7042,2\n2459#1:7050\n2455#1:7056\n2509#1:7062,6\n2509#1:7077,4\n2509#1:7087,2\n2509#1:7105\n2572#1:7115,6\n2572#1:7130,4\n2572#1:7140,2\n2592#1:7155,6\n2592#1:7170,4\n2592#1:7180,2\n2601#1:7192,6\n2601#1:7207,4\n2601#1:7217,2\n2601#1:7225\n2592#1:7240\n2665#1:7250,6\n2665#1:7265,4\n2665#1:7275,2\n2669#1:7286,6\n2669#1:7301,4\n2669#1:7311,2\n2669#1:7318\n2665#1:7332\n2572#1:7336\n2444#1:7340\n1564#1:3312,9\n1564#1:3333\n1564#1:3381,2\n1824#1:3422,9\n1824#1:3443\n1824#1:3445,2\n1860#1:3466,9\n1860#1:3487\n1860#1:3512,2\n1970#1:3531,9\n1970#1:3552\n1970#1:3566,2\n2050#1:3611,9\n2050#1:3632\n2183#1:3666,9\n2183#1:3687\n2183#1:3692,2\n2200#1:3711,9\n2200#1:3732\n2200#1:3738,2\n2050#1:3742,2\n2770#1:3837,9\n2770#1:3858\n2777#1:3869,9\n2777#1:3890\n2784#1:3905,9\n2784#1:3926\n2784#1:3929,2\n2876#1:3955,9\n2876#1:3976\n2876#1:3979,2\n2920#1:3998,9\n2920#1:4019\n2951#1:4049,9\n2951#1:4070\n2951#1:4072,2\n2920#1:4076,2\n2777#1:4095,2\n2770#1:4099,2\n247#1:4141,9\n247#1:4162\n251#1:4180,9\n251#1:4201\n264#1:4224,9\n264#1:4245\n264#1:4250,2\n299#1:4275,9\n299#1:4296\n299#1:4301,2\n334#1:4326,9\n334#1:4347\n334#1:4352,2\n369#1:4377,9\n369#1:4398\n369#1:4403,2\n251#1:4407,2\n247#1:4411,2\n714#1:4514,9\n714#1:4535\n718#1:4546,9\n718#1:4567\n718#1:4576,2\n714#1:4601,2\n762#1:4618,9\n762#1:4639\n762#1:4653,2\n564#1:4670,9\n564#1:4691\n588#1:4702,9\n588#1:4723\n593#1:4739,9\n593#1:4760\n594#1:4778,9\n594#1:4799\n594#1:4802,2\n610#1:4821,9\n610#1:4842\n610#1:4846,2\n593#1:4850,2\n637#1:4881,9\n637#1:4902\n637#1:4910,2\n588#1:4914,2\n698#1:4937,9\n698#1:4958\n698#1:4966,2\n564#1:4970,2\n825#1:5029,9\n825#1:5050\n832#1:5066,9\n832#1:5087\n832#1:5102,2\n825#1:5106,2\n894#1:5136,9\n894#1:5157\n894#1:5168,2\n1008#1:5203,9\n1008#1:5224\n1008#1:5232,2\n1069#1:5263,9\n1069#1:5284\n1069#1:5287,2\n1142#1:5323,9\n1142#1:5344\n1146#1:5356,9\n1146#1:5377\n1146#1:5386,2\n1180#1:5406,9\n1180#1:5427\n1180#1:5436,2\n1237#1:5469,9\n1237#1:5490\n1240#1:5514,9\n1240#1:5535\n1240#1:5537,2\n1237#1:5542,2\n1142#1:5553,2\n1320#1:5581,9\n1320#1:5602\n1321#1:5618,9\n1321#1:5639\n1321#1:5643,2\n1320#1:5656,2\n1371#1:5674,9\n1371#1:5695\n1372#1:5711,9\n1372#1:5732\n1372#1:5736,2\n1385#1:5755,9\n1385#1:5776\n1385#1:5790,2\n1371#1:5794,2\n1423#1:5808,9\n1423#1:5829\n1428#1:5844,9\n1428#1:5865\n1428#1:5867,2\n1423#1:5871,2\n1125#1:5889,9\n1125#1:5910\n1125#1:5968,2\n1042#1:5985,9\n1042#1:6006\n1102#1:6030,9\n1102#1:6051\n1102#1:6059,2\n1042#1:6063,2\n1488#1:6083,9\n1488#1:6104\n1501#1:6119,9\n1501#1:6140\n1501#1:6142,2\n1517#1:6160,9\n1517#1:6181\n1517#1:6185,2\n1488#1:6195,2\n1698#1:6242,9\n1698#1:6263\n1749#1:6295,9\n1749#1:6316\n1749#1:6328,2\n1770#1:6345,9\n1770#1:6366\n1770#1:6383,2\n1698#1:6387,2\n1662#1:6424,9\n1662#1:6445\n1662#1:6463,2\n1874#1:6484,9\n1874#1:6505\n1874#1:6508,2\n1902#1:6526,9\n1902#1:6547\n1902#1:6549,2\n1947#1:6568,9\n1947#1:6589\n1947#1:6592,2\n2087#1:6624,9\n2087#1:6645\n2098#1:6668,9\n2098#1:6689\n2098#1:6693,2\n2087#1:6697,2\n2147#1:6715,9\n2147#1:6736\n2158#1:6754,9\n2158#1:6775\n2158#1:6779,2\n2147#1:6785,2\n2310#1:6804,9\n2310#1:6825\n2310#1:6830,2\n2276#1:6848,9\n2276#1:6869\n2293#1:6886,9\n2293#1:6907\n2293#1:6915,2\n2276#1:6920,2\n2444#1:6949,9\n2444#1:6970\n2455#1:6986,9\n2455#1:7007\n2459#1:7023,9\n2459#1:7044\n2459#1:7048,2\n2455#1:7054,2\n2509#1:7068,9\n2509#1:7089\n2509#1:7103,2\n2572#1:7121,9\n2572#1:7142\n2592#1:7161,9\n2592#1:7182\n2601#1:7198,9\n2601#1:7219\n2601#1:7223,2\n2592#1:7238,2\n2665#1:7256,9\n2665#1:7277\n2669#1:7292,9\n2669#1:7313\n2669#1:7316,2\n2665#1:7330,2\n2572#1:7334,2\n2444#1:7338,2\n1564#1:3325,6\n1824#1:3435,6\n1860#1:3479,6\n1970#1:3544,6\n2050#1:3624,6\n2183#1:3679,6\n2200#1:3724,6\n2770#1:3850,6\n2777#1:3882,6\n2784#1:3918,6\n2876#1:3968,6\n2920#1:4011,6\n2951#1:4062,6\n247#1:4154,6\n251#1:4193,6\n264#1:4237,6\n299#1:4288,6\n334#1:4339,6\n369#1:4390,6\n714#1:4527,6\n718#1:4559,6\n762#1:4631,6\n564#1:4683,6\n588#1:4715,6\n593#1:4752,6\n594#1:4791,6\n610#1:4834,6\n637#1:4894,6\n698#1:4950,6\n825#1:5042,6\n832#1:5079,6\n894#1:5149,6\n1008#1:5216,6\n1069#1:5276,6\n1142#1:5336,6\n1146#1:5369,6\n1180#1:5419,6\n1237#1:5482,6\n1240#1:5527,6\n1320#1:5594,6\n1321#1:5631,6\n1371#1:5687,6\n1372#1:5724,6\n1385#1:5768,6\n1423#1:5821,6\n1428#1:5857,6\n1125#1:5902,6\n1042#1:5998,6\n1102#1:6043,6\n1488#1:6096,6\n1501#1:6132,6\n1517#1:6173,6\n1698#1:6255,6\n1749#1:6308,6\n1770#1:6358,6\n1662#1:6437,6\n1874#1:6497,6\n1902#1:6539,6\n1947#1:6581,6\n2087#1:6637,6\n2098#1:6681,6\n2147#1:6728,6\n2158#1:6767,6\n2310#1:6817,6\n2276#1:6861,6\n2293#1:6899,6\n2444#1:6962,6\n2455#1:6999,6\n2459#1:7036,6\n2509#1:7081,6\n2572#1:7134,6\n2592#1:7174,6\n2601#1:7211,6\n2665#1:7269,6\n2669#1:7305,6\n1581#1:3335,2\n1581#1:3380\n1974#1:3554,2\n1974#1:3565\n1151#1:5297\n1151#1:5298,5\n1185#1:5303\n1185#1:5304,5\n1824#1:3410\n1824#1:3411,5\n1824#1:3444\n1824#1:3448\n2770#1:3825\n2770#1:3826,5\n2770#1:3859\n2784#1:3893\n2784#1:3894,5\n2784#1:3927\n2784#1:3932\n2920#1:3986\n2920#1:3987,5\n2920#1:4020\n2920#1:4079\n2770#1:4102\n247#1:4129\n247#1:4130,5\n247#1:4163\n247#1:4414\n594#1:4766\n594#1:4767,5\n594#1:4800\n594#1:4805\n637#1:4869\n637#1:4870,5\n637#1:4903\n637#1:4913\n698#1:4924\n698#1:4925,6\n698#1:4959\n698#1:4969\n825#1:5016\n825#1:5017,6\n825#1:5051\n825#1:5109\n1069#1:5250\n1069#1:5251,6\n1069#1:5285\n1069#1:5290\n1240#1:5502\n1240#1:5503,5\n1240#1:5536\n1240#1:5540\n1102#1:6017\n1102#1:6018,6\n1102#1:6052\n1102#1:6062\n1662#1:6412\n1662#1:6413,5\n1662#1:6446\n1662#1:6466\n1874#1:6472\n1874#1:6473,5\n1874#1:6506\n1874#1:6511\n1902#1:6514\n1902#1:6515,5\n1902#1:6548\n1902#1:6552\n1947#1:6556\n1947#1:6557,5\n1947#1:6590\n1947#1:6595\n2098#1:6655\n2098#1:6656,6\n2098#1:6690\n2098#1:6696\n2276#1:6835\n2276#1:6836,6\n2276#1:6870\n2276#1:6923\n2050#1:3598\n2050#1:3599,6\n2050#1:3633\n2050#1:3745\n2777#1:3860,3\n2777#1:3891\n2951#1:4035\n2951#1:4036,7\n2951#1:4071\n2951#1:4075\n2777#1:4098\n714#1:4502\n714#1:4503,5\n714#1:4536\n714#1:4604\n762#1:4605\n762#1:4606,6\n762#1:4640\n762#1:4656\n564#1:4657\n564#1:4658,6\n564#1:4692\n610#1:4807\n610#1:4808,7\n610#1:4843\n610#1:4849\n564#1:4973\n832#1:5052\n832#1:5053,7\n832#1:5088\n832#1:5105\n894#1:5123\n894#1:5124,6\n894#1:5158\n894#1:5171\n1142#1:5310\n1142#1:5311,6\n1142#1:5345\n1142#1:5556\n1320#1:5568\n1320#1:5569,6\n1320#1:5603\n1320#1:5659\n1371#1:5661\n1371#1:5662,6\n1371#1:5696\n1371#1:5797\n1428#1:5831\n1428#1:5832,6\n1428#1:5866\n1428#1:5870\n1125#1:5876\n1125#1:5877,6\n1125#1:5911\n1125#1:5971\n1042#1:5972\n1042#1:5973,6\n1042#1:6007\n1042#1:6066\n1501#1:6106\n1501#1:6107,6\n1501#1:6141\n1501#1:6145\n1698#1:6229\n1698#1:6230,6\n1698#1:6264\n1698#1:6390\n2087#1:6610\n2087#1:6611,7\n2087#1:6646\n2087#1:6700\n2158#1:6740\n2158#1:6741,7\n2158#1:6776\n2158#1:6782\n2310#1:6791\n2310#1:6792,6\n2310#1:6826\n2310#1:6833\n2293#1:6873\n2293#1:6874,6\n2293#1:6908\n2293#1:6918\n2444#1:6940,3\n2444#1:6971\n2455#1:6973\n2455#1:6974,6\n2455#1:7008\n2455#1:7057\n2509#1:7059,3\n2509#1:7090\n2509#1:7106\n2572#1:7108\n2572#1:7109,6\n2572#1:7143\n2592#1:7148\n2592#1:7149,6\n2592#1:7183\n2592#1:7241\n2665#1:7243\n2665#1:7244,6\n2665#1:7278\n2665#1:7333\n2572#1:7337\n2444#1:7341\n176#1:4103\n178#1:4104\n179#1:4105\n180#1:4106\n181#1:4107\n182#1:4108\n182#1:4109,2\n262#1:4123\n297#1:4124\n332#1:4125\n367#1:4126\n487#1:4470\n488#1:4471\n489#1:4472\n490#1:4473\n491#1:4474\n492#1:4475\n493#1:4476\n494#1:4477\n495#1:4478\n498#1:4479\n498#1:4480,2\n500#1:4482\n501#1:4483\n502#1:4484\n502#1:4485,2\n503#1:4487\n503#1:4488,2\n523#1:4490\n630#1:4497\n631#1:4498\n939#1:5175\n939#1:5176,2\n952#1:5178\n952#1:5179,2\n953#1:5181\n953#1:5182,2\n954#1:5184\n954#1:5185,2\n955#1:5187\n955#1:5188,2\n959#1:5190\n959#1:5191,2\n1056#1:5236\n1060#1:5237\n1064#1:5238\n1133#1:5291\n1133#1:5292,2\n1134#1:5294\n1134#1:5295,2\n1290#1:5557\n1291#1:5558\n1298#1:5559\n1298#1:5560,2\n1457#1:6067\n1461#1:6068\n1640#1:6199\n1640#1:6200,2\n1644#1:6202\n1652#1:6203\n1688#1:6204\n1688#1:6205,2\n1696#1:6207\n1696#1:6208,2\n1811#1:6467\n1812#1:6468\n1817#1:6469\n1927#1:6553\n2032#1:6598\n2032#1:6599,2\n2235#1:6789\n2244#1:6790\n2412#1:6929\n2422#1:6930\n2655#1:6937\n2730#1:7342\n2730#1:7343,2\n2731#1:7345\n2731#1:7346,2\n2732#1:7348\n2732#1:7349,2\n2733#1:7351\n2733#1:7352,2\n2866#1:7359\n2927#1:7360\n2986#1:7361\n284#1:4249\n319#1:4300\n354#1:4351\n389#1:4402\n716#1:4501\n792#1:4652\n545#1:4990\n551#1:4994\n743#1:4580\n743#1:4593\n1239#1:5492,2\n1239#1:5541\n1753#1:6318\n1753#1:6327\n912#1:5110,12\n935#1:5172\n935#1:5173,2\n1307#1:5562,5\n2391#1:6924,5\n2764#1:7354,5\n*E\n"})
public final class MainActivityKt {
    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void AddTaskCard(@NotNull Function3<? super String, ? super String, ? super Long, Unit> function3, @NotNull Function0<Unit> function0, @Nullable Composer composer, int n) {
        int n2;
        Intrinsics.checkNotNullParameter(function3, (String)"onSave");
        Intrinsics.checkNotNullParameter(function0, (String)"onCancel");
        composer = composer.startRestartGroup(361970074);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(AddTaskCard)P(1)1629@85296L7,1630@85374L7,1638@85767L7660,1631@85413L8014:MainActivity.kt#to5c3");
        int n3 = n2 = n;
        if ((n & 6) == 0) {
            n3 = composer.changedInstance(function3) ? 4 : 2;
            n3 = n2 | n3;
        }
        n2 = n3;
        if ((n & 0x30) == 0) {
            n2 = composer.changedInstance(function0) ? 32 : 16;
            n2 = n3 | n2;
        }
        if ((n2 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)361970074, (int)n2, (int)-1, (String)"com.example.AddTaskCard (MainActivity.kt:1628)");
            }
            Object object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume(object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = (Context)object;
            object = (CompositionLocal)CompositionLocalsKt.getLocalFocusManager();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume(object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = (FocusManager)object;
            AndroidDialog_androidKt.Dialog(function0, (DialogProperties)new DialogProperties(true, false, false), (Function2)((Function2)ComposableLambdaKt.rememberComposableLambda((int)1331493425, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda12((FocusManager)object, function3, function0), (Composer)composer, (int)54)), (Composer)composer, (int)(n2 >> 3 & 0xE | 0x1B0), (int)0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        composer = composer.endRestartGroup();
        if (composer != null) {
            composer.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda13(function3, function0, n));
        }
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit AddTaskCard$lambda$368(FocusManager focusManager, Function3 function3, Function0 function0, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C1639@85794L34,1640@85858L32,1640@85837L53,1643@85912L282,1651@86216L174,1657@86450L7,1658@86499L7,1659@86546L7,1665@86735L96,1661@86563L6858:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1331493425, (int)n, (int)-1, (String)"com.example.AddTaskCard.<anonymous> (MainActivity.kt:1639)");
            }
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-335071885, (String)"CC(remember):MainActivity.kt#9igjgp");
            Object object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)false, null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Unit unit = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-335069839, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = (Function2)new AddTaskCard.1.1.1(mutableState, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)unit, (Function2)object, (Composer)composer, (int)6);
            float f = MainActivityKt.AddTaskCard$lambda$368$lambda$333((MutableState<Boolean>)mutableState) ? 1.0f : 0.8f;
            object = (AnimationSpec)AnimationSpecKt.spring$default((float)0.5f, (float)200.0f, null, (int)4, null);
            State state = AnimateAsStateKt.animateFloatAsState((float)f, (AnimationSpec)object, (float)0.0f, (String)"dialogScale", null, (Composer)composer, (int)3120, (int)20);
            f = MainActivityKt.AddTaskCard$lambda$368$lambda$333((MutableState<Boolean>)mutableState) ? 1.0f : 0.0f;
            object = (AnimationSpec)AnimationSpecKt.tween$default((int)250, (int)0, null, (int)6, null);
            State state2 = AnimateAsStateKt.animateFloatAsState((float)f, (AnimationSpec)object, (float)0.0f, (String)"dialogAlpha", null, (Composer)composer, (int)3120, (int)20);
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalView();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            mutableState = (View)object;
            object = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            unit = (ThemeOption)object;
            object = (CompositionLocal)TypographyKt.getLocalAppFont();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            FontFamily fontFamily = (FontFamily)object;
            Modifier modifier = BackgroundKt.background-bw27NRU$default((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (long)Color.copy-wmQWz5c$default((long)Color.Companion.getBlack-0d7_KjU(), (float)(MainActivityKt.AddTaskCard$lambda$368$lambda$337((State<Float>)state2) * 0.4f), (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), null, (int)2, null);
            Unit unit2 = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-335041711, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance((Object)focusManager);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = (Function2)new AddTaskCard.1.2.1(focusManager, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            unit2 = SuspendingPointerInputFilterKt.pointerInput((Modifier)modifier, (Object)unit2, (Function2)object);
            object = Alignment.Companion.getCenter();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            object = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)object, (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            modifier = composer.getCurrentCompositionLocalMap();
            unit2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)unit2);
            Function0 function02 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function02);
            } else {
                composer.useNode();
            }
            function02 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function02, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function02, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                function02.updateRememberedValue((Object)n);
                function02.apply((Object)n, (Function2)object);
            }
            Updater.set-impl((Composer)function02, (Object)unit2, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            object = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1223313278, (String)"C1672@87000L49,1675@87162L144,1682@87553L104,1685@87700L38,1686@87753L5658,1670@86905L6506:MainActivity.kt#to5c3");
            modifier = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)28));
            unit2 = CardDefaults.INSTANCE.cardColors-ro_MJ88(unit.getSurface-0d7_KjU(), 0L, 0L, 0L, composer, CardDefaults.$stable << 12, 14);
            function02 = SizeKt.fillMaxWidth((Modifier)((Modifier)Modifier.Companion), (float)0.9f);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-316554521, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl2 = composer.changed((Object)state);
            bl = composer.changed((Object)state2);
            object = composer.rememberedValue();
            if (bl2 | bl || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda157(state, state2);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            state = BorderKt.border-xT4_qwU((Modifier)ShadowKt.shadow-s4CzXII$default((Modifier)GraphicsLayerModifierKt.graphicsLayer((Modifier)function02, (Function1)object), (float)Dp.constructor-impl((float)12), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)28))), (boolean)false, (long)0L, (long)unit.getShadowColor-0d7_KjU(), (int)12, null), (float)Dp.constructor-impl((float)1.0f), (long)unit.getCardBorder-0d7_KjU(), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)28))));
            state2 = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-316542049, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changedInstance((Object)focusManager);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = (Function2)new AddTaskCard.1.3.2.1(focusManager, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            state = SuspendingPointerInputFilterKt.pointerInput((Modifier)state, (Object)state2, (Function2)object);
            object = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl((float)0.0f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, composer, CardDefaults.$stable << 18 | 6, 62);
            state2 = (Shape)modifier;
            CardKt.Card((Modifier)state, (Shape)state2, (CardColors)unit2, (CardElevation)object, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-1433604311, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda158(focusManager, (ThemeOption)unit, (View)mutableState, function3, function0, fontFamily), (Composer)composer, (int)54)), (Composer)composer, (int)196608, (int)16);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    private static final boolean AddTaskCard$lambda$368$lambda$333(MutableState<Boolean> mutableState) {
        return (Boolean)((State)mutableState).getValue();
    }

    private static final void AddTaskCard$lambda$368$lambda$334(MutableState<Boolean> mutableState, boolean bl) {
        mutableState.setValue((Object)bl);
    }

    private static final float AddTaskCard$lambda$368$lambda$336(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    private static final float AddTaskCard$lambda$368$lambda$337(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit AddTaskCard$lambda$368$lambda$367$lambda$340$lambda$339(State state, State state2, GraphicsLayerScope graphicsLayerScope) {
        Intrinsics.checkNotNullParameter((Object)graphicsLayerScope, (String)"$this$graphicsLayer");
        graphicsLayerScope.setScaleX(MainActivityKt.AddTaskCard$lambda$368$lambda$336((State<Float>)state));
        graphicsLayerScope.setScaleY(MainActivityKt.AddTaskCard$lambda$368$lambda$336((State<Float>)state));
        graphicsLayerScope.setAlpha(MainActivityKt.AddTaskCard$lambda$368$lambda$337((State<Float>)state2));
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366(FocusManager object, ThemeOption themeOption, View view, Function3 function3, Function0 function0, FontFamily fontFamily, ColumnScope object2, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$Card");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1687@87784L31,1695@88144L42,1700@88336L112,1697@88204L5193:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            float f;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1433604311, (int)n, (int)-1, (String)"com.example.AddTaskCard.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1687)");
            }
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)990760168, (String)"CC(remember):MainActivity.kt#9igjgp");
            object2 = composer.rememberedValue();
            if (object2 == Composer.Companion.getEmpty()) {
                object2 = SnapshotStateKt.mutableStateOf$default((Object)"", null, (int)2, null);
                composer.updateRememberedValue(object2);
            }
            MutableState mutableState = (MutableState)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object2 = new Pair((Object)"Study", (Object)Color.box-impl((long)ColorKt.Color((long)4286698746L)));
            Pair pair = new Pair((Object)"Work", (Object)Color.box-impl((long)ColorKt.Color((long)4289058471L)));
            Object object3 = new Pair((Object)"Read", (Object)Color.box-impl((long)ColorKt.Color((long)4291728344L)));
            Object object4 = new Pair((Object)"Rest", (Object)Color.box-impl((long)ColorKt.Color((long)4294945681L)));
            object4 = CollectionsKt.listOf((Object[])new Pair[]{object2, pair, object3, object4});
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)990771699, (String)"CC(remember):MainActivity.kt#9igjgp");
            object2 = composer.rememberedValue();
            if (object2 == Composer.Companion.getEmpty()) {
                object2 = SnapshotStateKt.mutableStateOf$default(object4.get(0), null, (int)2, null);
                composer.updateRememberedValue(object2);
            }
            pair = (MutableState)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Modifier modifier = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            object3 = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)990777913, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance(object);
            object2 = composer.rememberedValue();
            if (!bl && object2 != Composer.Companion.getEmpty()) {
                object = object2;
            } else {
                object = (Function2)new AddTaskCard.1.3.3.1.1(object, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object3 = PaddingKt.padding-3ABfNKs((Modifier)SuspendingPointerInputFilterKt.pointerInput((Modifier)modifier, (Object)object3, (Function2)object), (float)Dp.constructor-impl((float)24));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            object = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            n = 0 << 3 & 0x70;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            int n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object2 = composer.getCurrentCompositionLocalMap();
            object3 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object3);
            modifier = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)modifier);
            } else {
                composer.useNode();
            }
            modifier = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)modifier, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)modifier, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (modifier.getInserting() || !Intrinsics.areEqual((Object)modifier.rememberedValue(), (Object)n2)) {
                modifier.updateRememberedValue((Object)n2);
                modifier.apply((Object)n2, (Function2)object);
            }
            Updater.set-impl((Composer)modifier, (Object)object3, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)42452114, (String)"C1707@88615L17,1708@88672L8,1705@88529L285,1712@88835L40,1714@88917L39,1716@88998L41,1724@89428L426,1736@90190L376,1720@89179L14,1718@89081L1576,1745@90678L41,1746@90771L8,1746@90740L76,1747@90837L40,1748@90898L1106,1768@92025L41,1769@92087L1292:MainActivity.kt#to5c3");
            object2 = TypographyKt.getCursiveFontFamily((Composer)composer, (int)0);
            long l = ScaledSpKt.getScaledSp((int)28, (Composer)composer, (int)6);
            object = FontWeight.Companion.getBold();
            long l2 = themeOption.getTextPrimary-0d7_KjU();
            TextKt.Text--4IGK_g((String)"New Task", null, (long)l2, (long)l, null, (FontWeight)object, (FontFamily)object2, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)130962);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            MainActivityKt.PremiumJumpingTextPreview(MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343((MutableState<String>)mutableState), composer, 0);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)12)), (Composer)composer, (int)6);
            object3 = MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343((MutableState<String>)mutableState);
            Modifier modifier2 = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            RoundedCornerShape roundedCornerShape = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
            modifier = OutlinedTextFieldDefaults.INSTANCE.colors-0hiis_0(Color.Companion.getBlack-0d7_KjU(), Color.Companion.getBlack-0d7_KjU(), 0L, 0L, 0L, 0L, 0L, 0L, Color.Companion.getBlack-0d7_KjU(), 0L, null, themeOption.getTextPrimary-0d7_KjU(), Color.Companion.getLightGray-0d7_KjU(), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, themeOption.getTextPrimary-0d7_KjU(), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, composer, 0x6000036, 384, 0, 0, 3072, 2139088636, 4095);
            object2 = new KeyboardOptions(0, null, 0, ImeAction.Companion.getDone-eUduSuo(), null, null, null, 119, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2076791881, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changedInstance((Object)view);
            boolean bl2 = composer.changed((Object)function3);
            object = composer.rememberedValue();
            if (bl | bl2 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda71(view, function3, mutableState, (MutableState)pair);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            KeyboardActions keyboardActions = new KeyboardActions((Function1)object, null, null, null, null, null, 62, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2076824595, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda72(mutableState);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Function2 function2 = ComposableSingletons.MainActivityKt.INSTANCE.getLambda$-651787847$app();
            roundedCornerShape = (Shape)roundedCornerShape;
            OutlinedTextFieldKt.OutlinedTextField((String)object3, (Function1)object, (Modifier)modifier2, (boolean)false, (boolean)false, null, (Function2)function2, null, null, null, null, null, null, (boolean)false, null, (KeyboardOptions)object2, (KeyboardActions)keyboardActions, (boolean)true, (int)0, (int)0, null, (Shape)roundedCornerShape, (TextFieldColors)modifier, (Composer)composer, (int)1573296, (int)0xC30000, (int)0, (int)1867704);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
            l2 = ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)"Category", null, (long)themeOption.getTextSecondary-0d7_KjU(), (long)l2, null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)131058);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            object2 = Arrangement.INSTANCE.spacedBy-0680j_4(Dp.constructor-impl((float)12));
            object = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            object2 = (Arrangement.Horizontal)object2;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            object2 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)object2, (Alignment.Vertical)Alignment.Companion.getTop(), (Composer)composer, (int)(54 >> 3 & 0xE | 54 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object3 = composer.getCurrentCompositionLocalMap();
            object = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object);
            modifier = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)modifier);
            } else {
                composer.useNode();
            }
            modifier = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)modifier, (Object)object2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)modifier, (Object)object3, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (modifier.getInserting() || !Intrinsics.areEqual((Object)modifier.rememberedValue(), (Object)n2)) {
                modifier.updateRememberedValue((Object)n2);
                modifier.apply((Object)n2, (Function2)object2);
            }
            Updater.set-impl((Composer)modifier, (Object)object, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object2 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)636320321, (String)"C:MainActivity.kt#to5c3");
            composer.startReplaceGroup(159074135);
            ComposerKt.sourceInformation((Composer)composer, (String)"*1759@91590L336,1753@91143L813");
            object2 = (Iterable)object4;
            bl = false;
            object3 = object2.iterator();
            while (object3.hasNext()) {
                modifier2 = (Pair)object3.next();
                object4 = ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)40)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()));
                l2 = Intrinsics.areEqual(MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346((MutableState<Pair<String, Color>>)pair), (Object)modifier2) ? ((Color)modifier2.getSecond()).unbox-impl() : Color.copy-wmQWz5c$default((long)((Color)modifier2.getSecond()).unbox-impl(), (float)0.3f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
                object4 = BackgroundKt.background-bw27NRU$default((Modifier)object4, (long)l2, null, (int)2, null);
                f = Intrinsics.areEqual(MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346((MutableState<Pair<String, Color>>)pair), (Object)modifier2) ? Dp.constructor-impl((float)2) : Dp.constructor-impl((float)0.0f);
                modifier = BorderKt.border-xT4_qwU((Modifier)object4, (float)f, (long)Color.Companion.getBlack-0d7_KjU(), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1608725844, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl2 = composer.changedInstance((Object)view);
                boolean bl3 = composer.changed((Object)modifier2);
                object4 = composer.rememberedValue();
                if (bl2 | bl3 || object4 == Composer.Companion.getEmpty()) {
                    object4 = new MainActivityKt$$ExternalSyntheticLambda73(view, (Pair)modifier2, (MutableState)pair);
                    composer.updateRememberedValue(object4);
                }
                object4 = (Function0)object4;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                BoxKt.Box((Modifier)ClickableKt.clickable-XHw0xAI$default((Modifier)modifier, (boolean)false, null, null, (Function0)object4, (int)7, null), (Composer)composer, (int)0);
            }
            composer.endReplaceGroup();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)24)), (Composer)composer, (int)6);
            object = Arrangement.INSTANCE.getEnd();
            object4 = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            object = RowKt.rowMeasurePolicy((Arrangement.Horizontal)object, (Alignment.Vertical)Alignment.Companion.getTop(), (Composer)composer, (int)(54 >> 3 & 0xE | 54 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object2 = composer.getCurrentCompositionLocalMap();
            object4 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object4);
            object3 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)object3);
            } else {
                composer.useNode();
            }
            object3 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)object3, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object3, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object3.getInserting() || !Intrinsics.areEqual((Object)object3.rememberedValue(), (Object)n)) {
                object3.updateRememberedValue((Object)n);
                object3.apply((Object)n, (Function2)object);
            }
            Updater.set-impl((Composer)object3, (Object)object4, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1233190302, (String)"C1771@92244L284,1777@92555L164,1770@92194L525,1780@92744L40,1783@92933L166,1781@92835L522:MainActivity.kt#to5c3");
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)871065046, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl2 = composer.changedInstance((Object)view);
            bl = composer.changed((Object)function0);
            object = composer.rememberedValue();
            if (bl2 | bl || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda74(view, function0);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ButtonKt.TextButton((Function0)object, null, (boolean)false, null, null, null, null, null, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)1385847927, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda75(themeOption, fontFamily), (Composer)composer, (int)54)), (Composer)composer, (int)0x30000000, (int)510);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)12)), (Composer)composer, (int)6);
            l2 = themeOption.getPrimary-0d7_KjU();
            l = Color.Companion.getWhite-0d7_KjU();
            themeOption = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
            f = Dp.constructor-impl((float)4);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)871086976, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changed((Object)function3);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda76(function3, mutableState, (MutableState)pair);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            themeOption = (Shape)themeOption;
            PomoUIKt.PomoButton-6-Wz7xo((String)"Save Task", (Function0)object, null, (boolean)false, (Shape)themeOption, (long)l2, (long)l, (float)f, (long)0L, null, null, (Composer)composer, (int)14155782, (int)0, (int)1804);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    private static final String AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343(MutableState<String> mutableState) {
        return (String)((State)mutableState).getValue();
    }

    private static final void AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$344(MutableState<String> mutableState, String string) {
        mutableState.setValue((Object)string);
    }

    private static final Pair<String, Color> AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346(MutableState<Pair<String, Color>> mutableState) {
        return (Pair)((State)mutableState).getValue();
    }

    private static final void AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$347(MutableState<Pair<String, Color>> mutableState, Pair<String, Color> pair) {
        mutableState.setValue(pair);
    }

    static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$351$lambda$350(View object, Function3 function3, MutableState mutableState, MutableState mutableState2, KeyboardActionScope keyboardActionScope) {
        Intrinsics.checkNotNullParameter((Object)keyboardActionScope, (String)"$this$KeyboardActions");
        try {
            object.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        object = MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343((MutableState<String>)mutableState);
        boolean bl = object.length() == 0;
        if (bl) {
            object = "Do nothing";
        }
        function3.invoke(object, MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346((MutableState<Pair<String, Color>>)mutableState2).getFirst(), (Object)((Color)MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346((MutableState<Pair<String, Color>>)mutableState2).getSecond()).unbox-impl());
        return Unit.INSTANCE;
    }

    static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$353$lambda$352(MutableState mutableState, String string) {
        Intrinsics.checkNotNullParameter((Object)string, (String)"it");
        MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$344((MutableState<String>)mutableState, string);
        return Unit.INSTANCE;
    }

    static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$357$lambda$356$lambda$355$lambda$354(View view, Pair pair, MutableState mutableState) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$347((MutableState<Pair<String, Color>>)mutableState, (Pair<String, Color>)pair);
        return Unit.INSTANCE;
    }

    static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$364$lambda$359$lambda$358(View view, Function0 function0) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        function0.invoke();
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$364$lambda$360(ThemeOption themeOption, FontFamily fontFamily, RowScope rowScope, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)rowScope, (String)"$this$TextButton");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1778@92585L108:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1385847927, (int)n, (int)-1, (String)"com.example.AddTaskCard.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1778)");
            }
            TextKt.Text--4IGK_g((String)"Cancel", null, (long)themeOption.getTextSecondary-0d7_KjU(), (long)0L, null, (FontWeight)FontWeight.Companion.getMedium(), (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)130970);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$364$lambda$363$lambda$362(Function3 function3, MutableState object, MutableState mutableState) {
        boolean bl = (object = (CharSequence)MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343((MutableState<String>)object)).length() == 0;
        if (bl) {
            object = "Focus Task";
        }
        function3.invoke(object, MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346((MutableState<Pair<String, Color>>)mutableState).getFirst(), (Object)((Color)MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346((MutableState<Pair<String, Color>>)mutableState).getSecond()).unbox-impl());
        return Unit.INSTANCE;
    }

    static final Unit AddTaskCard$lambda$369(Function3 function3, Function0 function0, int n, Composer composer, int n2) {
        MainActivityKt.AddTaskCard((Function3<? super String, ? super String, ? super Long, Unit>)function3, (Function0<Unit>)function0, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n | 1)));
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void AlarmRingingLockScreen(@NotNull String string, int n, @Nullable Composer object, int n2) {
        block44: {
            int n3;
            Intrinsics.checkNotNullParameter((Object)string, (String)"label");
            Composer composer = object.startRestartGroup(349459721);
            ComposerKt.sourceInformation((Composer)composer, (String)"C(AlarmRingingLockScreen)2728@134861L7,2729@134893L30,2730@134948L34,2731@135008L90,2732@135121L34,2735@135282L56,2735@135254L84,2740@135434L969,2740@135411L992,2769@136475L14222:MainActivity.kt#to5c3");
            int n4 = n3 = n2;
            if ((n2 & 6) == 0) {
                n4 = composer.changed((Object)string) ? 4 : 2;
                n4 = n3 | n4;
            }
            n3 = n4;
            if ((n2 & 0x30) == 0) {
                n3 = composer.changed(n) ? 32 : 16;
                n3 = n4 | n3;
            }
            if ((n3 & 0x13) == 18 && composer.getSkipping()) {
                composer.skipToGroupEnd();
            } else {
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventStart((int)349459721, (int)n3, (int)-1, (String)"com.example.AlarmRingingLockScreen (MainActivity.kt:2727)");
                }
                object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
                object = composer.consume((CompositionLocal)object);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                Context context = (Context)object;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)823348455, (String)"CC(remember):MainActivity.kt#9igjgp");
                object = composer.rememberedValue();
                if (object == Composer.Companion.getEmpty()) {
                    object = SnapshotStateKt.mutableStateOf$default((Object)0, null, (int)2, null);
                    composer.updateRememberedValue(object);
                }
                Object object2 = (MutableState)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)823350219, (String)"CC(remember):MainActivity.kt#9igjgp");
                object = composer.rememberedValue();
                if (object == Composer.Companion.getEmpty()) {
                    object = SnapshotStateKt.mutableStateOf$default((Object)false, null, (int)2, null);
                    composer.updateRememberedValue(object);
                }
                MutableState mutableState = (MutableState)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)823352195, (String)"CC(remember):MainActivity.kt#9igjgp");
                object = composer.rememberedValue();
                if (object == Composer.Companion.getEmpty()) {
                    object = SnapshotStateKt.mutableStateOf$default((Object)"Ready to register. Put the device in your pocket and begin!", null, (int)2, null);
                    composer.updateRememberedValue(object);
                }
                MutableState mutableState2 = (MutableState)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)823355755, (String)"CC(remember):MainActivity.kt#9igjgp");
                object = composer.rememberedValue();
                if (object == Composer.Companion.getEmpty()) {
                    object = SnapshotStateKt.mutableStateOf$default((Object)false, null, (int)2, null);
                    composer.updateRememberedValue(object);
                }
                MutableState mutableState3 = (MutableState)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)823360929, (String)"CC(remember):MainActivity.kt#9igjgp");
                object = composer.rememberedValue();
                if (object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda5();
                    composer.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                BackHandlerKt.BackHandler((boolean)true, (Function0)object, (Composer)composer, (int)54, (int)0);
                Unit unit = Unit.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)823366706, (String)"CC(remember):MainActivity.kt#9igjgp");
                int n5 = composer.changedInstance((Object)context);
                n4 = (n3 & 0x70) == 32 ? 1 : 0;
                object = composer.rememberedValue();
                if ((n4 | n5) != 0 || object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda6(context, n, mutableState3, (MutableState)object2, mutableState2, mutableState);
                    composer.updateRememberedValue(object);
                }
                object = (Function1)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                EffectsKt.DisposableEffect((Object)unit, (Function1)object, (Composer)composer, (int)6);
                object = PaddingKt.padding-3ABfNKs((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (long)ColorKt.Color((long)4279045649L), null, (int)2, null), (float)Dp.constructor-impl((float)24));
                unit = Alignment.Companion.getCenter();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
                MeasurePolicy measurePolicy = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)unit, (boolean)false);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n4 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                unit = composer.getCurrentCompositionLocalMap();
                object = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object);
                Function0 function0 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function0);
                } else {
                    composer.useNode();
                }
                function0 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function0, (Object)unit, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                unit = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n4)) {
                    function0.updateRememberedValue((Object)n4);
                    function0.apply((Object)n4, (Function2)unit);
                }
                Updater.set-impl((Composer)function0, (Object)object, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
                object = (BoxScope)BoxScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1679825372, (String)"C2776@136691L14000:MainActivity.kt#to5c3");
                unit = Alignment.Companion.getCenterHorizontally();
                measurePolicy = Arrangement.INSTANCE.getCenter();
                object = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
                measurePolicy = (Arrangement.Vertical)measurePolicy;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
                unit = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)measurePolicy, (Alignment.Horizontal)unit, (Composer)composer, (int)(438 >> 3 & 0xE | 438 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n4 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                measurePolicy = composer.getCurrentCompositionLocalMap();
                object = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object);
                function0 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function0);
                } else {
                    composer.useNode();
                }
                function0 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function0, (Object)unit, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                unit = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n4)) {
                    function0.updateRememberedValue((Object)n4);
                    function0.apply((Object)n4, (Function2)unit);
                }
                Updater.set-impl((Composer)function0, (Object)object, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
                unit = (ColumnScope)ColumnScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1039713699, (String)"C:MainActivity.kt#to5c3");
                if (MainActivityKt.AlarmRingingLockScreen$lambda$491((MutableState<Boolean>)mutableState3)) {
                    composer.startReplaceGroup(-1039998497);
                    ComposerKt.sourceInformation((Composer)composer, (String)"2783@136991L568,2798@137577L41,2802@137727L8,2806@137916L13,2800@137636L311,2809@137965L41,2813@138281L8,2816@138422L8,2811@138024L493,2848@140407L48,2819@138551L1810,2819@138534L2450");
                    mutableState2 = BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)96)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4278249078L), (float)0.15f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), null, (int)2, null);
                    object = Alignment.Companion.getCenter();
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
                    object = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)object, (boolean)false);
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                    n4 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                    mutableState = composer.getCurrentCompositionLocalMap();
                    mutableState2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)mutableState2);
                    object2 = ComposeUiNode.Companion.getConstructor();
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                    if (!(composer.getApplier() instanceof Applier)) {
                        ComposablesKt.invalidApplier();
                    }
                    composer.startReusableNode();
                    if (composer.getInserting()) {
                        composer.createNode((Function0)object2);
                    } else {
                        composer.useNode();
                    }
                    object2 = Updater.constructor-impl((Composer)composer);
                    Updater.set-impl((Composer)object2, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                    Updater.set-impl((Composer)object2, (Object)mutableState, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                    object = ComposeUiNode.Companion.getSetCompositeKeyHash();
                    if (object2.getInserting() || !Intrinsics.areEqual((Object)object2.rememberedValue(), (Object)n4)) {
                        object2.updateRememberedValue((Object)n4);
                        object2.apply((Object)n4, (Function2)object);
                    }
                    Updater.set-impl((Composer)object2, (Object)mutableState2, (Function2)ComposeUiNode.Companion.getSetModifier());
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
                    object = (BoxScope)BoxScopeInstance.INSTANCE;
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-790286144, (String)"C2790@137288L253:MainActivity.kt#to5c3");
                    object = CheckCircleKt.getCheckCircle((Icons.Filled)Icons.Filled.INSTANCE);
                    long l = ColorKt.Color((long)4278249078L);
                    mutableState = (Modifier)Modifier.Companion;
                    mutableState = SizeKt.size-3ABfNKs((Modifier)mutableState, (float)Dp.constructor-impl((float)56));
                    IconKt.Icon-ww6aTOc((ImageVector)object, (String)"Success", (Modifier)mutableState, (long)l, (Composer)composer, (int)3504, (int)0);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    composer.endNode();
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)24)), (Composer)composer, (int)6);
                    l = ScaledSpKt.getScaledSp((int)26, (Composer)composer, (int)6);
                    object = FontWeight.Companion.getExtraBold();
                    long l2 = Color.Companion.getWhite-0d7_KjU();
                    n4 = TextAlign.Companion.getCenter-e0LSkKk();
                    mutableState = TypographyKt.getAppFontFamily((Composer)composer, (int)0);
                    TextKt.Text--4IGK_g((String)"Sleep Defeated! \ud83e\udde0\ud83d\udd25", null, (long)l2, (long)l, null, (FontWeight)object, (FontFamily)mutableState, (long)0L, null, (TextAlign)TextAlign.box-impl((int)n4), (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196998, (int)0, (int)130450);
                    SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)12)), (Composer)composer, (int)6);
                    long l3 = ScaledSpKt.getScaledSp((int)13, (Composer)composer, (int)6);
                    l2 = Color.Companion.getLightGray-0d7_KjU();
                    n4 = TextAlign.Companion.getCenter-e0LSkKk();
                    l = ScaledSpKt.getScaledSp((int)20, (Composer)composer, (int)6);
                    mutableState = PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16), (float)0.0f, (int)2, null);
                    object = TextAlign.box-impl((int)n4);
                    TextKt.Text--4IGK_g((String)"Your neuromuscular system is fully activated. Oxygenated blood flow has rushed to your brain, washing sleep inertia away instantly.\n\nYou are chemically primed for hyper-focus studying!", (Modifier)mutableState, (long)l2, (long)l3, null, null, null, (long)0L, null, (TextAlign)object, (long)l, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)438, (int)0, (int)129520);
                    mutableState = ButtonDefaults.INSTANCE.buttonColors-ro_MJ88(ColorKt.Color((long)4278249078L), 0L, 0L, 0L, composer, ButtonDefaults.$stable << 12 | 6, 14);
                    mutableState2 = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
                    object2 = SizeKt.height-3ABfNKs((Modifier)SizeKt.fillMaxWidth((Modifier)((Modifier)Modifier.Companion), (float)0.85f), (float)Dp.constructor-impl((float)56));
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1767617399, (String)"CC(remember):MainActivity.kt#9igjgp");
                    boolean bl = composer.changedInstance((Object)context);
                    object = composer.rememberedValue();
                    if (bl || object == Composer.Companion.getEmpty()) {
                        object = new MainActivityKt$$ExternalSyntheticLambda7(context);
                        composer.updateRememberedValue(object);
                    }
                    object = (Function0)object;
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ButtonKt.Button((Function0)object, (Modifier)object2, (boolean)false, (Shape)((Shape)mutableState2), (ButtonColors)mutableState, null, null, null, null, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$-690978352$app(), (Composer)composer, (int)0x30000030, (int)484);
                    composer.endReplaceGroup();
                } else {
                    composer.startReplaceGroup(-1035758224);
                    ComposerKt.sourceInformation((Composer)composer, (String)"2864@141106L43,2865@141198L369,2875@141585L909,2898@142586L8,2902@142770L13,2896@142512L289,2905@142819L40,2909@142969L8,2912@143115L8,2907@142877L314,2916@143209L41,2919@143327L2271,2971@145616L41,2975@145822L8,2973@145675L352,2981@146045L41,2984@146194L42,2985@146281L367,3000@146864L1068,2995@146666L1266,3023@147950L41,3027@148091L8,3025@148009L380,3034@148407L41,3037@148517L1541");
                    object = InfiniteTransitionKt.rememberInfiniteTransition((String)"pulse", (Composer)composer, (int)6, (int)0);
                    object = InfiniteTransitionKt.animateFloat((InfiniteTransition)object, (float)0.95f, (float)1.05f, (InfiniteRepeatableSpec)AnimationSpecKt.infiniteRepeatable-9IiC70o$default((DurationBasedAnimationSpec)((DurationBasedAnimationSpec)AnimationSpecKt.tween$default((int)1000, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (RepeatMode)RepeatMode.Reverse, (long)0L, (int)4, null), (String)"alarmScale", (Composer)composer, (int)(InfiniteTransition.$stable | 0x61B0 | InfiniteRepeatableSpec.$stable << 9), (int)0);
                    mutableState3 = Alignment.Companion.getCenterVertically();
                    measurePolicy = Arrangement.INSTANCE.spacedBy-0680j_4(Dp.constructor-impl((float)8));
                    context = PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)16), (int)7, null);
                    measurePolicy = (Arrangement.Horizontal)measurePolicy;
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
                    mutableState3 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)measurePolicy, (Alignment.Vertical)mutableState3, (Composer)composer, (int)(438 >> 3 & 0xE | 438 >> 3 & 0x70));
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                    n4 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                    measurePolicy = composer.getCurrentCompositionLocalMap();
                    context = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)context);
                    function0 = ComposeUiNode.Companion.getConstructor();
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                    if (!(composer.getApplier() instanceof Applier)) {
                        ComposablesKt.invalidApplier();
                    }
                    composer.startReusableNode();
                    if (composer.getInserting()) {
                        composer.createNode(function0);
                    } else {
                        composer.useNode();
                    }
                    function0 = Updater.constructor-impl((Composer)composer);
                    Updater.set-impl((Composer)function0, (Object)mutableState3, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                    Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                    mutableState3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
                    if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n4)) {
                        function0.updateRememberedValue((Object)n4);
                        function0.apply((Object)n4, (Function2)mutableState3);
                    }
                    Updater.set-impl((Composer)function0, (Object)context, (Function2)ComposeUiNode.Companion.getSetModifier());
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
                    context = (RowScope)RowScopeInstance.INSTANCE;
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)646005972, (String)"C2880@141834L265,2888@142228L8,2891@142388L8,2886@142120L356:MainActivity.kt#to5c3");
                    BoxKt.Box((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)10)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)ColorKt.Color((long)4278249078L), null, (int)2, null), (Composer)composer, (int)0);
                    long l = ScaledSpKt.getScaledSp((int)10, (Composer)composer, (int)6);
                    context = FontWeight.Companion.getBlack();
                    long l4 = ColorKt.Color((long)4278249078L);
                    long l5 = ScaledSpKt.getScaledSp((double)1.5, (Composer)composer, (int)6);
                    mutableState3 = TypographyKt.getMonospaceFontFamily();
                    TextKt.Text--4IGK_g((String)"KINETIC G-SENSOR SYNCHRONIZED", null, (long)l4, (long)l, null, (FontWeight)context, (FontFamily)mutableState3, (long)l5, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196998, (int)0, (int)130834);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    composer.endNode();
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    l = ScaledSpKt.getScaledSp((int)24, (Composer)composer, (int)6);
                    mutableState3 = FontWeight.Companion.getBold();
                    l4 = Color.Companion.getWhite-0d7_KjU();
                    n4 = TextAlign.Companion.getCenter-e0LSkKk();
                    context = TypographyKt.getAppFontFamily((Composer)composer, (int)0);
                    TextKt.Text--4IGK_g((String)string, null, (long)l4, (long)l, null, (FontWeight)mutableState3, (FontFamily)context, (long)0L, null, (TextAlign)TextAlign.box-impl((int)n4), (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)(n3 & 0xE | 0x30180), (int)0, (int)130450);
                    SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)4)), (Composer)composer, (int)6);
                    l5 = ScaledSpKt.getScaledSp((int)11, (Composer)composer, (int)6);
                    context = FontWeight.Companion.getBlack();
                    l = ColorKt.Color((long)4294947584L);
                    l4 = ScaledSpKt.getScaledSp((int)2, (Composer)composer, (int)6);
                    n4 = TextAlign.Companion.getCenter-e0LSkKk();
                    TextKt.Text--4IGK_g((String)"SOMATIC WAKE-UP AUDIT", null, (long)l, (long)l5, null, (FontWeight)context, null, (long)l4, null, (TextAlign)TextAlign.box-impl((int)n4), (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196998, (int)0, (int)130386);
                    SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)32)), (Composer)composer, (int)6);
                    mutableState3 = ScaleKt.scale((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)190)), (float)MainActivityKt.AlarmRingingLockScreen$lambda$518$lambda$517$lambda$505((State<Float>)object));
                    object = Alignment.Companion.getCenter();
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
                    context = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)object, (boolean)false);
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                    n4 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                    object = composer.getCurrentCompositionLocalMap();
                    mutableState3 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)mutableState3);
                    measurePolicy = ComposeUiNode.Companion.getConstructor();
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                    if (!(composer.getApplier() instanceof Applier)) {
                        ComposablesKt.invalidApplier();
                    }
                    composer.startReusableNode();
                    if (composer.getInserting()) {
                        composer.createNode((Function0)measurePolicy);
                    } else {
                        composer.useNode();
                    }
                    measurePolicy = Updater.constructor-impl((Composer)composer);
                    Updater.set-impl((Composer)measurePolicy, (Object)context, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                    Updater.set-impl((Composer)measurePolicy, (Object)object, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                    object = ComposeUiNode.Companion.getSetCompositeKeyHash();
                    if (measurePolicy.getInserting() || !Intrinsics.areEqual((Object)measurePolicy.rememberedValue(), (Object)n4)) {
                        measurePolicy.updateRememberedValue((Object)n4);
                        measurePolicy.apply((Object)n4, (Function2)object);
                    }
                    Updater.set-impl((Composer)measurePolicy, (Object)mutableState3, (Function2)ComposeUiNode.Companion.getSetModifier());
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
                    object = (BoxScope)BoxScopeInstance.INSTANCE;
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2059749983, (String)"C2926@143680L214,2934@144046L6,2933@143984L312,2943@144431L20,2942@144369L312,2950@144703L877:MainActivity.kt#to5c3");
                    float f = n > 0 ? (float)MainActivityKt.AlarmRingingLockScreen$lambda$482((MutableState<Integer>)object2) / (float)n : 0.0f;
                    object = (AnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null);
                    context = AnimateAsStateKt.animateFloatAsState((float)f, (AnimationSpec)object, (float)0.0f, (String)"progress", null, (Composer)composer, (int)3120, (int)20);
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2011780437, (String)"CC(remember):MainActivity.kt#9igjgp");
                    object = composer.rememberedValue();
                    if (object == Composer.Companion.getEmpty()) {
                        object = new MainActivityKt$$ExternalSyntheticLambda8();
                        composer.updateRememberedValue(object);
                    }
                    object = (Function0)object;
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ProgressIndicatorKt.CircularProgressIndicator-IyT6zlY((Function0)object, (Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (long)Color.copy-wmQWz5c$default((long)Color.Companion.getWhite-0d7_KjU(), (float)0.05f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (float)Dp.constructor-impl((float)14), (long)0L, (int)StrokeCap.Companion.getRound-KaPHkGw(), (float)0.0f, (Composer)composer, (int)3510, (int)80);
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2011792771, (String)"CC(remember):MainActivity.kt#9igjgp");
                    boolean bl = composer.changed((Object)context);
                    object = composer.rememberedValue();
                    if (bl || object == Composer.Companion.getEmpty()) {
                        object = new MainActivityKt$$ExternalSyntheticLambda9((State)context);
                        composer.updateRememberedValue(object);
                    }
                    object = (Function0)object;
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ProgressIndicatorKt.CircularProgressIndicator-IyT6zlY((Function0)object, (Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (long)ColorKt.Color((long)4278249078L), (float)Dp.constructor-impl((float)14), (long)0L, (int)StrokeCap.Companion.getRound-KaPHkGw(), (float)0.0f, (Composer)composer, (int)3504, (int)80);
                    mutableState3 = Alignment.Companion.getCenterHorizontally();
                    object = (Arrangement.Vertical)Arrangement.INSTANCE.getCenter();
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
                    context = (Modifier)Modifier.Companion;
                    mutableState3 = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)object, (Alignment.Horizontal)mutableState3, (Composer)composer, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                    n4 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                    object = composer.getCurrentCompositionLocalMap();
                    context = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)context);
                    measurePolicy = ComposeUiNode.Companion.getConstructor();
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                    if (!(composer.getApplier() instanceof Applier)) {
                        ComposablesKt.invalidApplier();
                    }
                    composer.startReusableNode();
                    if (composer.getInserting()) {
                        composer.createNode((Function0)measurePolicy);
                    } else {
                        composer.useNode();
                    }
                    measurePolicy = Updater.constructor-impl((Composer)composer);
                    Updater.set-impl((Composer)measurePolicy, (Object)mutableState3, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                    Updater.set-impl((Composer)measurePolicy, (Object)object, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                    object = ComposeUiNode.Companion.getSetCompositeKeyHash();
                    if (measurePolicy.getInserting() || !Intrinsics.areEqual((Object)measurePolicy.rememberedValue(), (Object)n4)) {
                        measurePolicy.updateRememberedValue((Object)n4);
                        measurePolicy.apply((Object)n4, (Function2)object);
                    }
                    Updater.set-impl((Composer)measurePolicy, (Object)context, (Function2)ComposeUiNode.Companion.getSetModifier());
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
                    object = (ColumnScope)ColumnScopeInstance.INSTANCE;
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1974513748, (String)"C2956@145000L8,2954@144900L308,2963@145341L8,2966@145524L8,2961@145233L325:MainActivity.kt#to5c3");
                    n4 = MainActivityKt.AlarmRingingLockScreen$lambda$482((MutableState<Integer>)object2);
                    l4 = ScaledSpKt.getScaledSp((int)60, (Composer)composer, (int)6);
                    object2 = FontWeight.Companion.getExtraBold();
                    l = Color.Companion.getWhite-0d7_KjU();
                    object = TypographyKt.getMonospaceFontFamily();
                    TextKt.Text--4IGK_g((String)String.valueOf(n4), null, (long)l, (long)l4, null, (FontWeight)object2, (FontFamily)object, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196992, (int)0, (int)130962);
                    object2 = "/ " + n + " SQUATS";
                    l = ScaledSpKt.getScaledSp((int)11, (Composer)composer, (int)6);
                    object = FontWeight.Companion.getBlack();
                    l4 = Color.copy-wmQWz5c$default((long)Color.Companion.getWhite-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
                    l5 = ScaledSpKt.getScaledSp((int)1, (Composer)composer, (int)6);
                    TextKt.Text--4IGK_g((String)object2, null, (long)l4, (long)l, null, (FontWeight)object, null, (long)l5, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196992, (int)0, (int)130898);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    composer.endNode();
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    composer.endNode();
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
                    l4 = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
                    l = Color.copy-wmQWz5c$default((long)Color.Companion.getWhite-0d7_KjU(), (float)0.7f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
                    n4 = TextAlign.Companion.getCenter-e0LSkKk();
                    object = PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)32), (float)0.0f, (int)2, null);
                    TextKt.Text--4IGK_g((String)"For accurate tracking, hold your phone firmly with any hand without shaking.", (Modifier)object, (long)l, (long)l4, null, null, null, (long)0L, null, (TextAlign)TextAlign.box-impl((int)n4), (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)438, (int)0, (int)130544);
                    SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)28)), (Composer)composer, (int)6);
                    object = InfiniteTransitionKt.rememberInfiniteTransition((String)"wave", (Composer)composer, (int)6, (int)0);
                    context = InfiniteTransitionKt.animateFloat((InfiniteTransition)object, (float)0.0f, (float)6.28318f, (InfiniteRepeatableSpec)AnimationSpecKt.infiniteRepeatable-9IiC70o$default((DurationBasedAnimationSpec)((DurationBasedAnimationSpec)AnimationSpecKt.tween$default((int)1200, (int)0, (Easing)EasingKt.getLinearEasing(), (int)2, null)), (RepeatMode)RepeatMode.Restart, (long)0L, (int)4, null), (String)"waveOffset", (Composer)composer, (int)(InfiniteTransition.$stable | 0x61B0 | InfiniteRepeatableSpec.$stable << 9), (int)0);
                    object2 = PaddingKt.padding-VpY3zN4$default((Modifier)SizeKt.height-3ABfNKs((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)Dp.constructor-impl((float)36)), (float)Dp.constructor-impl((float)32), (float)0.0f, (int)2, null);
                    ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1767882673, (String)"CC(remember):MainActivity.kt#9igjgp");
                    boolean bl2 = composer.changed((Object)context);
                    object = composer.rememberedValue();
                    if (bl2 || object == Composer.Companion.getEmpty()) {
                        object = new MainActivityKt$$ExternalSyntheticLambda10(mutableState, (State)context);
                        composer.updateRememberedValue(object);
                    }
                    object = (Function1)object;
                    ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                    CanvasKt.Canvas((Modifier)object2, (Function1)object, (Composer)composer, (int)6);
                    SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
                    object = MainActivityKt.AlarmRingingLockScreen$lambda$488((MutableState<String>)mutableState2);
                    l = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
                    mutableState2 = FontWeight.Companion.getMedium();
                    l4 = MainActivityKt.AlarmRingingLockScreen$lambda$485((MutableState<Boolean>)mutableState) ? ColorKt.Color((long)4294937216L) : Color.copy-wmQWz5c$default((long)Color.Companion.getWhite-0d7_KjU(), (float)0.8f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
                    n4 = TextAlign.Companion.getCenter-e0LSkKk();
                    object2 = PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16), (float)0.0f, (int)2, null);
                    TextKt.Text--4IGK_g((String)object, (Modifier)object2, (long)l4, (long)l, null, (FontWeight)mutableState2, null, (long)0L, null, (TextAlign)TextAlign.box-impl((int)n4), (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196656, (int)0, (int)130512);
                    SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)32)), (Composer)composer, (int)6);
                    AnimatedVisibilityKt.AnimatedVisibility((ColumnScope)unit, (boolean)MainActivityKt.AlarmRingingLockScreen$lambda$485((MutableState<Boolean>)mutableState), null, (EnterTransition)EnterExitTransitionKt.fadeIn$default(null, (float)0.0f, (int)3, null).plus(EnterExitTransitionKt.expandVertically$default(null, null, (boolean)false, null, (int)15, null)), (ExitTransition)EnterExitTransitionKt.fadeOut$default(null, (float)0.0f, (int)3, null).plus(EnterExitTransitionKt.shrinkVertically$default(null, null, (boolean)false, null, (int)15, null)), null, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$1883655041$app(), (Composer)composer, (int)((438 >> 6 & 0x70 | 6) & 0xE | 0x186C00), (int)18);
                    if (!MainActivityKt.AlarmRingingLockScreen$lambda$485((MutableState<Boolean>)mutableState)) {
                        composer.startReplaceGroup(-1026970313);
                        ComposerKt.sourceInformation((Composer)composer, (String)"3072@150377L8,3075@150546L8,3070@150171L478");
                        l5 = ScaledSpKt.getScaledSp((int)11, (Composer)composer, (int)6);
                        l4 = Color.copy-wmQWz5c$default((long)Color.Companion.getWhite-0d7_KjU(), (float)0.35f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
                        n4 = TextAlign.Companion.getCenter-e0LSkKk();
                        l = ScaledSpKt.getScaledSp((int)16, (Composer)composer, (int)6);
                        mutableState = PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)24), (float)0.0f, (int)2, null);
                        object = TextAlign.box-impl((int)n4);
                        TextKt.Text--4IGK_g((String)"Phone loops sensory ringtones at full volume. No snooze, no exit! Pocket the device or hold it, then complete squats to unlock.", (Modifier)mutableState, (long)l4, (long)l5, null, null, null, (long)0L, null, (TextAlign)object, (long)l, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)438, (int)0, (int)129520);
                    } else {
                        composer.startReplaceGroup(-1175935171);
                    }
                    composer.endReplaceGroup();
                    composer.endReplaceGroup();
                }
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
            }
            object = composer.endRestartGroup();
            if (object == null) break block44;
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda11(string, n, n2));
        }
    }

    private static final int AlarmRingingLockScreen$lambda$482(MutableState<Integer> mutableState) {
        return ((Number)((State)mutableState).getValue()).intValue();
    }

    private static final void AlarmRingingLockScreen$lambda$483(MutableState<Integer> mutableState, int n) {
        mutableState.setValue((Object)n);
    }

    private static final boolean AlarmRingingLockScreen$lambda$485(MutableState<Boolean> mutableState) {
        return (Boolean)((State)mutableState).getValue();
    }

    private static final void AlarmRingingLockScreen$lambda$486(MutableState<Boolean> mutableState, boolean bl) {
        mutableState.setValue((Object)bl);
    }

    private static final String AlarmRingingLockScreen$lambda$488(MutableState<String> mutableState) {
        return (String)((State)mutableState).getValue();
    }

    private static final void AlarmRingingLockScreen$lambda$489(MutableState<String> mutableState, String string) {
        mutableState.setValue((Object)string);
    }

    private static final boolean AlarmRingingLockScreen$lambda$491(MutableState<Boolean> mutableState) {
        return (Boolean)((State)mutableState).getValue();
    }

    private static final void AlarmRingingLockScreen$lambda$492(MutableState<Boolean> mutableState, boolean bl) {
        mutableState.setValue((Object)bl);
    }

    static final Unit AlarmRingingLockScreen$lambda$494$lambda$493() {
        return Unit.INSTANCE;
    }

    static final DisposableEffectResult AlarmRingingLockScreen$lambda$500$lambda$499(Context context, int n, MutableState mutableState, MutableState mutableState2, MutableState mutableState3, MutableState mutableState4, DisposableEffectScope disposableEffectScope) {
        Intrinsics.checkNotNullParameter((Object)disposableEffectScope, (String)"$this$DisposableEffect");
        disposableEffectScope = new SquatSensorService(context);
        SquatSensorService.startTracking$default((SquatSensorService)disposableEffectScope, (int)n, (Function0)new MainActivityKt$$ExternalSyntheticLambda93(context, mutableState), (Function1)new MainActivityKt$$ExternalSyntheticLambda94(mutableState2, mutableState3), (Function1)new MainActivityKt$$ExternalSyntheticLambda95(mutableState4, mutableState3), null, (int)16, null);
        context = (DisposableEffectResult)new AlarmRingingLockScreen$lambda$500$lambda$499$$inlined$onDispose$1((SquatSensorService)disposableEffectScope);
        return context;
    }

    static final Unit AlarmRingingLockScreen$lambda$500$lambda$499$lambda$495(Context context, MutableState mutableState) {
        SoundPlayer.INSTANCE.stopContinuousAlarm();
        AlarmReceiver.Companion.dismissNotification(context);
        MainActivityKt.AlarmRingingLockScreen$lambda$492((MutableState<Boolean>)mutableState, true);
        return Unit.INSTANCE;
    }

    static final Unit AlarmRingingLockScreen$lambda$500$lambda$499$lambda$496(MutableState mutableState, MutableState mutableState2, int n) {
        MainActivityKt.AlarmRingingLockScreen$lambda$483((MutableState<Integer>)mutableState, n);
        MainActivityKt.AlarmRingingLockScreen$lambda$489((MutableState<String>)mutableState2, "Awesome! Perfect squat registered.");
        return Unit.INSTANCE;
    }

    static final Unit AlarmRingingLockScreen$lambda$500$lambda$499$lambda$497(MutableState mutableState, MutableState mutableState2, boolean bl) {
        MainActivityKt.AlarmRingingLockScreen$lambda$486((MutableState<Boolean>)mutableState, bl);
        if (bl) {
            MainActivityKt.AlarmRingingLockScreen$lambda$489((MutableState<String>)mutableState2, "Locked: Please stop shaking the phone!");
        } else {
            MainActivityKt.AlarmRingingLockScreen$lambda$489((MutableState<String>)mutableState2, "Standing stable. Keep performing smooth physical squats.");
        }
        return Unit.INSTANCE;
    }

    static final Unit AlarmRingingLockScreen$lambda$518$lambda$517$lambda$504$lambda$503(Context context) {
        Object object;
        Intent intent;
        block11: {
            intent = null;
            object = context.getSystemService("vibrator");
            object = object instanceof Vibrator ? (Vibrator)object : null;
            if (object == null) break block11;
            try {
                object.vibrate(100L);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        AlarmState.INSTANCE.setAlarmRinging(false);
        AlarmReceiver.Companion.dismissNotification(context);
        object = intent;
        if (context instanceof Activity) {
            object = (Activity)context;
        }
        try {
            intent = new Intent(context, MainActivity.class);
            intent.addFlags(0x10008000);
            context.startActivity(intent);
        }
        catch (Exception exception) {
            Log.e((String)"SquatDebug", (String)"Failed to launch main activity", (Throwable)exception);
        }
        if (Build.VERSION.SDK_INT >= 27) {
            if (object != null) {
                object.setShowWhenLocked(false);
            }
            if (object != null) {
                object.setTurnScreenOn(false);
            }
        }
        if (object != null && (context = object.getWindow()) != null) {
            context.clearFlags(0x680080);
        }
        if (object != null) {
            object.finish();
        }
        return Unit.INSTANCE;
    }

    private static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$505(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    private static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$513$lambda$507(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$513$lambda$509$lambda$508() {
        return 1.0f;
    }

    static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$513$lambda$511$lambda$510(State state) {
        return MainActivityKt.AlarmRingingLockScreen$lambda$518$lambda$517$lambda$513$lambda$507((State<Float>)state);
    }

    private static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$514(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit AlarmRingingLockScreen$lambda$518$lambda$517$lambda$516$lambda$515(MutableState mutableState, State state, DrawScope drawScope) {
        long l;
        Intrinsics.checkNotNullParameter((Object)drawScope, (String)"$this$Canvas");
        Path path = AndroidPath_androidKt.Path();
        float f = Size.getWidth-impl((long)drawScope.getSize-NH-jbRc());
        float f2 = Size.getHeight-impl((long)drawScope.getSize-NH-jbRc()) / 2.0f;
        float f3 = MainActivityKt.AlarmRingingLockScreen$lambda$485((MutableState<Boolean>)mutableState) ? 16.0f : 6.0f;
        path.moveTo(0.0f, f2);
        int n = (int)f;
        int n2 = 0;
        n = ProgressionUtilKt.getProgressionLastElement((int)0, (int)n, (int)6);
        if (n <= 0) {
            while (true) {
                float f4 = (float)Math.sin((float)n2 / f * 3.5f * 2.0f * 3.14159f + MainActivityKt.AlarmRingingLockScreen$lambda$518$lambda$517$lambda$514((State<Float>)state));
                path.lineTo((float)n2, f4 * f3 + f2);
                if (n2 == n) break;
                n2 += 6;
            }
        }
        if (MainActivityKt.AlarmRingingLockScreen$lambda$485((MutableState<Boolean>)mutableState)) {
            l = ColorKt.Color((long)0xFFFF5252L);
            f3 = 0.8f;
        } else {
            l = ColorKt.Color((long)4278249078L);
            f3 = 0.5f;
        }
        l = Color.copy-wmQWz5c$default((long)l, (float)f3, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
        DrawScope.drawPath-LG529CI$default((DrawScope)drawScope, (Path)path, (long)l, (float)0.0f, (DrawStyle)((DrawStyle)new Stroke(drawScope.toPx-0680j_4(Dp.constructor-impl((float)2)), 0.0f, StrokeCap.Companion.getRound-KaPHkGw(), 0, null, 26, null)), null, (int)0, (int)52, null);
        return Unit.INSTANCE;
    }

    static final Unit AlarmRingingLockScreen$lambda$519(String string, int n, int n2, Composer composer, int n3) {
        MainActivityKt.AlarmRingingLockScreen(string, n, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n2 | 1)));
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void AnimatedQuoteCard(@NotNull String string, boolean bl, @Nullable Composer object, int n) {
        int n2;
        Intrinsics.checkNotNullParameter((Object)string, (String)"quote");
        Composer composer = object.startRestartGroup(-1284705171);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(AnimatedQuoteCard)P(1)2227@112942L7,2228@112985L7,2233@113266L43,2234@113351L266,2243@113659L268,2255@114012L69,2256@114116L38,2265@114462L87,2274@114735L3164,2253@113933L3966:MainActivity.kt#to5c3");
        int n3 = n2 = n;
        if ((n & 6) == 0) {
            n3 = composer.changed((Object)string) ? 4 : 2;
            n3 = n2 | n3;
        }
        n2 = n3;
        if ((n & 0x30) == 0) {
            n2 = composer.changed(bl) ? 32 : 16;
            n2 = n3 | n2;
        }
        if ((n2 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1284705171, (int)n2, (int)-1, (String)"com.example.AnimatedQuoteCard (MainActivity.kt:2226)");
            }
            object = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = (ThemeOption)object;
            Object object2 = (CompositionLocal)TypographyKt.getLocalAppFont();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object2 = composer.consume(object2);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object2 = (FontFamily)object2;
            long l = bl ? object.getSecondary-0d7_KjU() : object.getTextPrimary-0d7_KjU();
            long l2 = bl ? object.getSecondary-0d7_KjU() : object.getPrimary-0d7_KjU();
            InfiniteTransition infiniteTransition = InfiniteTransitionKt.rememberInfiniteTransition((String)"pulse", (Composer)composer, (int)6, (int)0);
            InfiniteRepeatableSpec infiniteRepeatableSpec = AnimationSpecKt.infiniteRepeatable-9IiC70o$default((DurationBasedAnimationSpec)((DurationBasedAnimationSpec)AnimationSpecKt.tween$default((int)4000, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (RepeatMode)RepeatMode.Reverse, (long)0L, (int)4, null);
            n3 = InfiniteTransition.$stable;
            State state = InfiniteTransitionKt.animateFloat((InfiniteTransition)infiniteTransition, (float)0.4f, (float)0.7f, (InfiniteRepeatableSpec)infiniteRepeatableSpec, (String)"alpha", (Composer)composer, (int)(InfiniteRepeatableSpec.$stable << 9 | (n3 | 0x61B0)), (int)0);
            State state2 = InfiniteTransitionKt.animateFloat((InfiniteTransition)infiniteTransition, (float)0.98f, (float)1.02f, (InfiniteRepeatableSpec)AnimationSpecKt.infiniteRepeatable-9IiC70o$default((DurationBasedAnimationSpec)((DurationBasedAnimationSpec)AnimationSpecKt.tween$default((int)4000, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (RepeatMode)RepeatMode.Reverse, (long)0L, (int)4, null), (String)"scale", (Composer)composer, (int)(InfiniteTransition.$stable | 0x61B0 | InfiniteRepeatableSpec.$stable << 9), (int)0);
            RoundedCornerShape roundedCornerShape = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)28));
            infiniteTransition = CardDefaults.INSTANCE.cardColors-ro_MJ88(Color.copy-wmQWz5c$default((long)object.getSurface-0d7_KjU(), (float)0.85f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), 0L, 0L, 0L, composer, CardDefaults.$stable << 12, 14);
            infiniteRepeatableSpec = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl((float)0.0f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, composer, CardDefaults.$stable << 18 | 6, 62);
            Modifier modifier = PaddingKt.padding-qDBjuR0$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)0.0f, (float)Dp.constructor-impl((float)12), (float)0.0f, (float)Dp.constructor-impl((float)24), (int)5, null);
            float f = Dp.constructor-impl((float)6);
            Shape shape = (Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)28));
            shape = ShadowKt.shadow-s4CzXII$default((Modifier)modifier, (float)f, (Shape)shape, (boolean)false, (long)0L, (long)object.getShadowColor-0d7_KjU(), (int)12, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1946003260, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl2 = composer.changed((Object)state2);
            object = composer.rememberedValue();
            if (bl2 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda98(state2);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = BorderKt.border-xT4_qwU((Modifier)GraphicsLayerModifierKt.graphicsLayer((Modifier)shape, (Function1)object), (float)Dp.constructor-impl((float)((float)1.5)), (long)Color.copy-wmQWz5c$default((long)l2, (float)MainActivityKt.AnimatedQuoteCard$lambda$434((State<Float>)state), (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)28))));
            roundedCornerShape = (Shape)roundedCornerShape;
            CardKt.Card((Modifier)object, (Shape)roundedCornerShape, (CardColors)infiniteTransition, (CardElevation)infiniteRepeatableSpec, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-878029765, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda99(l2, (FontFamily)object2, string, l), (Composer)composer, (int)54)), (Composer)composer, (int)196608, (int)16);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda100(string, bl, n));
        }
    }

    private static final float AnimatedQuoteCard$lambda$434(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    private static final float AnimatedQuoteCard$lambda$435(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit AnimatedQuoteCard$lambda$437$lambda$436(State state, GraphicsLayerScope graphicsLayerScope) {
        Intrinsics.checkNotNullParameter((Object)graphicsLayerScope, (String)"$this$graphicsLayer");
        graphicsLayerScope.setScaleX(MainActivityKt.AnimatedQuoteCard$lambda$435((State<Float>)state));
        graphicsLayerScope.setScaleY(MainActivityKt.AnimatedQuoteCard$lambda$435((State<Float>)state));
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit AnimatedQuoteCard$lambda$447(long l, FontFamily fontFamily, String string, long l2, ColumnScope object, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object, (String)"$this$Card");
        ComposerKt.sourceInformation((Composer)composer, (String)"C2275@114745L3148:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-878029765, (int)n, (int)-1, (String)"com.example.AnimatedQuoteCard.<anonymous> (MainActivity.kt:2275)");
            }
            object = PaddingKt.padding-3ABfNKs((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)Dp.constructor-impl((float)24));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            MeasurePolicy measurePolicy = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)Alignment.Companion.getTopStart(), (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            object = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            compositionLocalMap = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)compositionLocalMap);
            }
            Updater.set-impl((Composer)function0, (Object)object, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            compositionLocalMap = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)299708987, (String)"C2283@114984L8,2281@114920L370,2292@115304L2146,2339@117582L8,2337@117518L365:MainActivity.kt#to5c3");
            long l3 = ScaledSpKt.getScaledSp((int)80, (Composer)composer, (int)6);
            object = FontWeight.Companion.getBold();
            long l4 = Color.copy-wmQWz5c$default((long)l, (float)0.15f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
            measurePolicy = compositionLocalMap.align((Modifier)Modifier.Companion, Alignment.Companion.getTopStart());
            measurePolicy = OffsetKt.offset-VpY3zN4((Modifier)measurePolicy, (float)Dp.constructor-impl((float)-8), (float)Dp.constructor-impl((float)-35));
            TextKt.Text--4IGK_g((String)"\u201c", (Modifier)measurePolicy, (long)l4, (long)l3, null, (FontWeight)object, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)130960);
            object = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            float f = Dp.constructor-impl((float)12);
            float f2 = Dp.constructor-impl((float)8);
            function0 = PaddingKt.padding-VpY3zN4((Modifier)object, (float)f2, (float)f);
            object = Alignment.Companion.getCenterHorizontally();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            measurePolicy = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)object, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object = composer.getCurrentCompositionLocalMap();
            function0 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)function0);
            Function0 function02 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function02);
            } else {
                composer.useNode();
            }
            function02 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function02, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function02, (Object)object, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                function02.updateRememberedValue((Object)n);
                function02.apply((Object)n, (Function2)object);
            }
            Updater.set-impl((Composer)function02, (Object)function0, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1271655214, (String)"C2301@115685L328,2308@116132L1304,2299@115590L1846:MainActivity.kt#to5c3");
            measurePolicy = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1287948647, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda122();
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            AnimatedContentKt.AnimatedContent((Object)string, (Modifier)measurePolicy, (Function1)object, null, (String)"quoteContent", null, (Function4)((Function4)ComposableLambdaKt.rememberComposableLambda((int)-431559979, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda123(l2, fontFamily, l), (Composer)composer, (int)54)), (Composer)composer, (int)1597872, (int)40);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            l2 = ScaledSpKt.getScaledSp((int)80, (Composer)composer, (int)6);
            string = FontWeight.Companion.getBold();
            l = Color.copy-wmQWz5c$default((long)l, (float)0.15f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
            object = OffsetKt.offset-VpY3zN4((Modifier)compositionLocalMap.align((Modifier)Modifier.Companion, Alignment.Companion.getBottomEnd()), (float)Dp.constructor-impl((float)8), (float)Dp.constructor-impl((float)15));
            TextKt.Text--4IGK_g((String)"\u201d", (Modifier)object, (long)l, (long)l2, null, (FontWeight)string, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)130960);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final ContentTransform AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$441$lambda$440(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$AnimatedContent");
        return AnimatedContentKt.togetherWith((EnterTransition)EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)600, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.slideInVertically((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)600, (int)0, null, (int)6, null)), (Function1)new MainActivityKt$$ExternalSyntheticLambda164())), (ExitTransition)EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.slideOutVertically((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, null, (int)6, null)), (Function1)new MainActivityKt$$ExternalSyntheticLambda165())));
    }

    static final int AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$441$lambda$440$lambda$438(int n) {
        return 20;
    }

    static final int AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$441$lambda$440$lambda$439(int n) {
        return -20;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$444(long l, FontFamily fontFamily, long l2, AnimatedContentScope object, String string, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object, (String)"$this$AnimatedContent");
        Intrinsics.checkNotNullParameter((Object)string, (String)"targetQuote");
        ComposerKt.sourceInformation((Composer)composer, (String)"C2309@116169L1249:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-431559979, (int)n, (int)-1, (String)"com.example.AnimatedQuoteCard.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:2309)");
        }
        object = Alignment.Companion.getCenterHorizontally();
        Modifier modifier = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
        MeasurePolicy measurePolicy = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)object, (Composer)composer, (int)(390 >> 3 & 0xE | 390 >> 3 & 0x70));
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
        n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
        object = composer.getCurrentCompositionLocalMap();
        modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
        Object object2 = ComposeUiNode.Companion.getConstructor();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
        if (!(composer.getApplier() instanceof Applier)) {
            ComposablesKt.invalidApplier();
        }
        composer.startReusableNode();
        if (composer.getInserting()) {
            composer.createNode(object2);
        } else {
            composer.useNode();
        }
        object2 = Updater.constructor-impl((Composer)composer);
        Updater.set-impl((Composer)object2, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
        Updater.set-impl((Composer)object2, (Object)object, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
        object = ComposeUiNode.Companion.getSetCompositeKeyHash();
        if (object2.getInserting() || !Intrinsics.areEqual((Object)object2.rememberedValue(), (Object)n)) {
            object2.updateRememberedValue((Object)n);
            object2.apply((Object)n, (Function2)object);
        }
        Updater.set-impl((Composer)object2, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
        object = (ColumnScope)ColumnScopeInstance.INSTANCE;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1103330632, (String)"C2315@116502L8,2321@116895L8,2313@116360L645,2324@117030L41,2326@117147L249:MainActivity.kt#to5c3");
        object = string;
        n = object.length() == 0 ? 1 : 0;
        if (n != 0) {
            object = "Stay present and fully engaged.";
        }
        object2 = (String)object;
        long l3 = ScaledSpKt.getScaledSp((int)18, (Composer)composer, (int)6);
        n = FontStyle.Companion.getItalic-_-LCdwA();
        object = FontWeight.Companion.getMedium();
        int n2 = TextAlign.Companion.getCenter-e0LSkKk();
        long l4 = ScaledSpKt.getScaledSp((int)28, (Composer)composer, (int)6);
        measurePolicy = PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)4), (float)0.0f, (int)2, null);
        string = FontStyle.box-impl((int)n);
        modifier = TextAlign.box-impl((int)n2);
        TextKt.Text--4IGK_g((String)object2, (Modifier)measurePolicy, (long)l, (long)l3, (FontStyle)string, (FontWeight)object, (FontFamily)fontFamily, (long)0L, null, (TextAlign)modifier, (long)l4, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196656, (int)0, (int)129408);
        SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
        BoxKt.Box((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)6)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)Color.copy-wmQWz5c$default((long)l2, (float)0.6f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), null, (int)2, null), (Composer)composer, (int)0);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        composer.endNode();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit AnimatedQuoteCard$lambda$448(String string, boolean bl, int n, Composer composer, int n2) {
        MainActivityKt.AnimatedQuoteCard(string, bl, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n | 1)));
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableInferredTarget(scheme="[androidx.compose.ui.UiComposable[androidx.compose.ui.UiComposable]]")
    public static final void AnimatedScaleBox-gXMAzVA(@NotNull Function0<Unit> function0, @Nullable Modifier modifier, boolean bl, @Nullable Shape shape, float f, long l, @NotNull Function2<? super Composer, ? super Integer, Unit> function2, @Nullable Composer object, int n, int n2) {
        int n3;
        int n4;
        int n5;
        Intrinsics.checkNotNullParameter(function0, (String)"onClick");
        Intrinsics.checkNotNullParameter(function2, (String)"content");
        Composer composer = object.startRestartGroup(-780888465);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(AnimatedScaleBox)P(4,3,2,6,1:c#ui.unit.Dp,5:c#ui.graphics.Color)1808@93852L7,1809@93888L39,1810@93967L25,1811@94010L226,1816@94265L210,1821@94530L7,1825@94607L127,1842@95245L223,1823@94547L1012:MainActivity.kt#to5c3");
        int n6 = n5 = n;
        if ((n & 6) == 0) {
            n6 = composer.changedInstance(function0) ? 4 : 2;
            n6 = n5 | n6;
        }
        if ((n4 = n2 & 2) != 0) {
            n6 |= 0x30;
        } else if ((n & 0x30) == 0) {
            n5 = composer.changed(modifier) ? 32 : 16;
            n6 |= n5;
        }
        int n7 = n2 & 4;
        if (n7 != 0) {
            n6 |= 0x180;
        } else if ((n & 0x180) == 0) {
            n5 = composer.changed(bl) ? 256 : 128;
            n6 |= n5;
        }
        if ((n & 0xC00) == 0) {
            n5 = (n2 & 8) == 0 && composer.changed(shape) ? 2048 : 1024;
            n6 |= n5;
        }
        if ((n3 = n2 & 0x10) != 0) {
            n6 |= 0x6000;
        } else if ((n & 0x6000) == 0) {
            n5 = composer.changed(f) ? 16384 : 8192;
            n6 |= n5;
        }
        int n8 = n2 & 0x20;
        if (n8 != 0) {
            n6 |= 0x30000;
        } else if ((n & 0x30000) == 0) {
            n5 = composer.changed(l) ? 131072 : 65536;
            n6 |= n5;
        }
        n5 = n6;
        if ((n & 0x180000) == 0) {
            n5 = composer.changedInstance(function2) ? 0x100000 : 524288;
            n5 = n6 | n5;
        }
        if ((n5 & 0x92493) == 599186 && composer.getSkipping()) {
            composer.skipToGroupEnd();
            object = shape;
            shape = modifier;
            modifier = object;
        } else {
            composer.startDefaults();
            if ((n & 1) != 0 && !composer.getDefaultsInvalid()) {
                composer.skipToGroupEnd();
                n6 = n5;
                if ((n2 & 8) != 0) {
                    n6 = n5 & 0xFFFFE3FF;
                }
                object = shape;
                shape = modifier;
                modifier = object;
            } else {
                object = n4 != 0 ? (Modifier)Modifier.Companion : modifier;
                if (n7 != 0) {
                    bl = true;
                }
                n6 = n5;
                if ((n2 & 8) != 0) {
                    shape = (Shape)RoundedCornerShapeKt.getCircleShape();
                    n6 = n5 & 0xFFFFE3FF;
                }
                if (n3 != 0) {
                    f = Dp.constructor-impl((float)6);
                }
                if (n8 != 0) {
                    l = Color.copy-wmQWz5c$default((long)Color.Companion.getBlack-0d7_KjU(), (float)0.2f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
                    modifier = shape;
                    shape = object;
                } else {
                    modifier = shape;
                    shape = object;
                }
            }
            composer.endDefaults();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-780888465, (int)n6, (int)-1, (String)"com.example.AnimatedScaleBox (MainActivity.kt:1807)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = (Context)object;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1185476598, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = InteractionSourceKt.MutableInteractionSource();
                composer.updateRememberedValue(object);
            }
            MutableInteractionSource mutableInteractionSource = (MutableInteractionSource)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = PressInteractionKt.collectIsPressedAsState((InteractionSource)((InteractionSource)mutableInteractionSource), (Composer)composer, (int)6);
            float f2 = MainActivityKt.AnimatedScaleBox_gXMAzVA$lambda$371((State<Boolean>)object) && bl ? 0.9f : 1.0f;
            AnimationSpec animationSpec = (AnimationSpec)AnimationSpecKt.spring$default((float)0.5f, (float)200.0f, null, (int)4, null);
            State state = AnimateAsStateKt.animateFloatAsState((float)f2, (AnimationSpec)animationSpec, (float)0.0f, (String)"scale", null, (Composer)composer, (int)3120, (int)20);
            f2 = MainActivityKt.AnimatedScaleBox_gXMAzVA$lambda$371((State<Boolean>)object) && bl ? Dp.constructor-impl((float)((float)1.5)) : (bl ? f : Dp.constructor-impl((float)0.0f));
            object = (AnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null);
            State state2 = AnimateAsStateKt.animateDpAsState-AjpBEmI((float)f2, (AnimationSpec)object, (String)"elevation", null, (Composer)composer, (int)432, (int)8);
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalView();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            animationSpec = (View)object;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1185499694, (String)"CC(remember):MainActivity.kt#9igjgp");
            int n9 = composer.changed((Object)state);
            n5 = (n6 & 0x380) == 256 ? 1 : 0;
            object = composer.rememberedValue();
            if ((n9 | n5) != 0 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda37(bl, state);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = GraphicsLayerModifierKt.graphicsLayer((Modifier)shape, (Function1)object);
            f2 = MainActivityKt.AnimatedScaleBox_gXMAzVA$lambda$373((State<Dp>)state2);
            long l2 = Color.copy-wmQWz5c$default((long)l, (float)(Color.getAlpha-impl((long)l) * 0.4f), (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
            state2 = ClipKt.clip((Modifier)ShadowKt.shadow-s4CzXII((Modifier)object, (float)f2, (Shape)modifier, (boolean)false, (long)l2, (long)l), (Shape)modifier);
            object = RippleKt.ripple-H2RKhps$default((boolean)true, (float)0.0f, (long)0L, (int)6, null);
            state = (Indication)object;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1185520206, (String)"CC(remember):MainActivity.kt#9igjgp");
            int n10 = composer.changedInstance((Object)animationSpec);
            n5 = (n6 & 0xE) == 4 ? 1 : 0;
            object = composer.rememberedValue();
            if ((n10 | n5) != 0 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda38((View)animationSpec, function0);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = ClickableKt.clickable-O2vRcR0$default((Modifier)state2, (MutableInteractionSource)mutableInteractionSource, (Indication)state, (boolean)bl, null, null, (Function0)object, (int)24, null);
            mutableInteractionSource = Alignment.Companion.getCenter();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            animationSpec = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)mutableInteractionSource, (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n5 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            mutableInteractionSource = composer.getCurrentCompositionLocalMap();
            object = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object);
            state2 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)state2);
            } else {
                composer.useNode();
            }
            state2 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)state2, (Object)animationSpec, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)state2, (Object)mutableInteractionSource, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            mutableInteractionSource = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (state2.getInserting() || !Intrinsics.areEqual((Object)state2.rememberedValue(), (Object)n5)) {
                state2.updateRememberedValue((Object)n5);
                state2.apply((Object)n5, (Function2)mutableInteractionSource);
            }
            Updater.set-impl((Composer)state2, (Object)object, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            object = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-653471855, (String)"C1851@95544L9:MainActivity.kt#to5c3");
            function2.invoke((Object)composer, (Object)(n6 >> 18 & 0xE));
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda39(function0, (Modifier)shape, bl, (Shape)modifier, f, l, function2, n, n2));
        }
    }

    private static final boolean AnimatedScaleBox_gXMAzVA$lambda$371(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    private static final float AnimatedScaleBox_gXMAzVA$lambda$372(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    private static final float AnimatedScaleBox_gXMAzVA$lambda$373(State<Dp> state) {
        return ((Dp)state.getValue()).unbox-impl();
    }

    static final Unit AnimatedScaleBox_gXMAzVA$lambda$375$lambda$374(boolean bl, State state, GraphicsLayerScope graphicsLayerScope) {
        Intrinsics.checkNotNullParameter((Object)graphicsLayerScope, (String)"$this$graphicsLayer");
        graphicsLayerScope.setScaleX(MainActivityKt.AnimatedScaleBox_gXMAzVA$lambda$372((State<Float>)state));
        graphicsLayerScope.setScaleY(MainActivityKt.AnimatedScaleBox_gXMAzVA$lambda$372((State<Float>)state));
        float f = bl ? 1.0f : 0.5f;
        graphicsLayerScope.setAlpha(f);
        return Unit.INSTANCE;
    }

    static final Unit AnimatedScaleBox_gXMAzVA$lambda$377$lambda$376(View view, Function0 function0) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        function0.invoke();
        return Unit.INSTANCE;
    }

    static final Unit AnimatedScaleBox_gXMAzVA$lambda$379(Function0 function0, Modifier modifier, boolean bl, Shape shape, float f, long l, Function2 function2, int n, int n2, Composer composer, int n3) {
        MainActivityKt.AnimatedScaleBox-gXMAzVA((Function0<Unit>)function0, modifier, bl, shape, f, l, (Function2<? super Composer, ? super Integer, Unit>)function2, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n | 1)), n2);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void AppControls(@NotNull TimerManager.TimerState timerState, @NotNull Context context, @Nullable Function0<Unit> mainActivityKt$$ExternalSyntheticLambda54, @Nullable Composer object, int n, int n2) {
        int n3;
        int n4;
        Intrinsics.checkNotNullParameter((Object)timerState, (String)"state");
        Intrinsics.checkNotNullParameter((Object)context, (String)"context");
        Composer composer = object.startRestartGroup(-1842064179);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(AppControls)P(2)1856@95686L2,1857@95752L7,1858@95797L7,1859@95809L4283:MainActivity.kt#to5c3");
        int n5 = n4 = n;
        if ((n & 6) == 0) {
            n5 = composer.changed(((Enum)timerState).ordinal()) ? 4 : 2;
            n5 = n4 | n5;
        }
        if ((n3 = n2 & 4) != 0) {
            n5 |= 0x180;
        } else if ((n & 0x180) == 0) {
            n4 = composer.changedInstance(mainActivityKt$$ExternalSyntheticLambda54) ? 256 : 128;
            n5 |= n4;
        }
        if ((n5 & 0x83) == 130 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (n3 != 0) {
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-697146129, (String)"CC(remember):MainActivity.kt#9igjgp");
                mainActivityKt$$ExternalSyntheticLambda54 = composer.rememberedValue();
                if (mainActivityKt$$ExternalSyntheticLambda54 == Composer.Companion.getEmpty()) {
                    mainActivityKt$$ExternalSyntheticLambda54 = new MainActivityKt$$ExternalSyntheticLambda54();
                    composer.updateRememberedValue(mainActivityKt$$ExternalSyntheticLambda54);
                }
                mainActivityKt$$ExternalSyntheticLambda54 = (Function0)mainActivityKt$$ExternalSyntheticLambda54;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1842064179, (int)n5, (int)-1, (String)"com.example.AppControls (MainActivity.kt:1856)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Context context2 = (Context)object;
            object = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ThemeOption themeOption = (ThemeOption)object;
            Modifier modifier = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            Arrangement.Horizontal horizontal = (Arrangement.Horizontal)Arrangement.INSTANCE.getCenter();
            object = Alignment.Companion.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            object = RowKt.rowMeasurePolicy((Arrangement.Horizontal)horizontal, (Alignment.Vertical)object, (Composer)composer, (int)(438 >> 3 & 0xE | 438 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n4 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            horizontal = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)horizontal, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n4)) {
                function0.updateRememberedValue((Object)n4);
                function0.apply((Object)n4, (Function2)object);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1999928401, (String)"C1868@96143L175,1872@96329L482,1864@95981L830,1885@96821L40,1892@97091L297,1900@97399L980,1888@96936L1443,1924@98389L40,1926@98483L16,1932@98704L770,1945@99485L601,1927@98508L1578:MainActivity.kt#to5c3");
            horizontal = RoundedCornerShapeKt.getCircleShape();
            float f = Dp.constructor-impl((float)5);
            long l = Color.copy-wmQWz5c$default((long)Color.Companion.getBlack-0d7_KjU(), (float)0.15f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1320958248, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance((Object)context2);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda55(context2);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            horizontal = (Shape)horizontal;
            MainActivityKt.AnimatedScaleBox-gXMAzVA((Function0<Unit>)object, null, false, (Shape)horizontal, f, l, (Function2<? super Composer, ? super Integer, Unit>)((Function2)ComposableLambdaKt.rememberComposableLambda((int)-835819694, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda56(themeOption), (Composer)composer, (int)54)), composer, 1794048, 6);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)26)), (Composer)composer, (int)6);
            bl = timerState == TimerManager.TimerState.RUNNING;
            horizontal = RoundedCornerShapeKt.getCircleShape();
            f = Dp.constructor-impl((float)8);
            l = themeOption.getShadowColor-0d7_KjU();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1320927790, (String)"CC(remember):MainActivity.kt#9igjgp");
            int n6 = composer.changed(bl);
            int n7 = composer.changedInstance((Object)context2);
            n4 = (n5 & 0x380) == 256 ? 1 : 0;
            object = composer.rememberedValue();
            if ((n6 | n7 | n4) != 0 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda57(bl, context2, (Function0)mainActivityKt$$ExternalSyntheticLambda54);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            horizontal = (Shape)horizontal;
            MainActivityKt.AnimatedScaleBox-gXMAzVA((Function0<Unit>)object, null, false, (Shape)horizontal, f, l, (Function2<? super Composer, ? super Integer, Unit>)((Function2)ComposableLambdaKt.rememberComposableLambda((int)1229501435, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda58(themeOption, bl), (Composer)composer, (int)54)), composer, 1597440, 6);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)26)), (Composer)composer, (int)6);
            modifier = SnapshotStateKt.collectAsState((StateFlow)TimerManager.INSTANCE.isBreakMode(), null, (Composer)composer, (int)0, (int)1);
            bl = !bl;
            horizontal = RoundedCornerShapeKt.getCircleShape();
            f = Dp.constructor-impl((float)5);
            l = Color.copy-wmQWz5c$default((long)Color.Companion.getBlack-0d7_KjU(), (float)0.15f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1320875701, (String)"CC(remember):MainActivity.kt#9igjgp");
            n5 = (n5 & 0xE) == 4 ? 1 : 0;
            int n8 = composer.changedInstance((Object)context2);
            int n9 = composer.changed((Object)modifier);
            object = composer.rememberedValue();
            if ((n5 | n8 | n9) != 0 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda59(timerState, context2, (State)modifier);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            context2 = (Shape)horizontal;
            MainActivityKt.AnimatedScaleBox-gXMAzVA((Function0<Unit>)object, null, bl, (Shape)context2, f, l, (Function2<? super Composer, ? super Integer, Unit>)((Function2)ComposableLambdaKt.rememberComposableLambda((int)-1726702438, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda60(themeOption), (Composer)composer, (int)54)), composer, 1794048, 2);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda61(timerState, context, (Function0)mainActivityKt$$ExternalSyntheticLambda54, n, n2));
        }
    }

    static final Unit AppControls$lambda$381$lambda$380() {
        return Unit.INSTANCE;
    }

    static final Unit AppControls$lambda$398$lambda$384$lambda$383(Context context) {
        Intent intent = new Intent(context, TimerService.class);
        intent.setAction("ACTION_STOP");
        context.startService(intent);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit AppControls$lambda$398$lambda$386(ThemeOption themeOption, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C1873@96343L458:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-835819694, (int)n, (int)-1, (String)"com.example.AppControls.<anonymous>.<anonymous> (MainActivity.kt:1873)");
            }
            Modifier modifier = BorderKt.border-xT4_qwU((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)56)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)themeOption.getSurface-0d7_KjU(), null, (int)2, null), (float)Dp.constructor-impl((float)((float)1.5)), (long)themeOption.getCardBorder-0d7_KjU(), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()));
            Alignment alignment = Alignment.Companion.getCenter();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            alignment = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)alignment, (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)alignment, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            alignment = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)alignment);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            modifier = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)0x7178881, (String)"C1881@96669L118:MainActivity.kt#to5c3");
            modifier = StopKt.getStop((Icons.Filled)Icons.Filled.INSTANCE);
            long l = themeOption.getTextPrimary-0d7_KjU();
            themeOption = (Modifier)Modifier.Companion;
            IconKt.Icon-ww6aTOc((ImageVector)modifier, (String)"Stop", (Modifier)SizeKt.size-3ABfNKs((Modifier)themeOption, (float)Dp.constructor-impl((float)28)), (long)l, (Composer)composer, (int)432, (int)0);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit AppControls$lambda$398$lambda$389$lambda$388(boolean bl, Context context, Function0 function0) {
        if (bl) {
            function0 = new Intent(context, TimerService.class);
            function0.setAction("ACTION_PAUSE");
            context.startService((Intent)function0);
        } else {
            function0.invoke();
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit AppControls$lambda$398$lambda$391(ThemeOption themeOption, boolean bl, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C1901@97413L956:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1229501435, (int)n, (int)-1, (String)"com.example.AppControls.<anonymous>.<anonymous> (MainActivity.kt:1901)");
            }
            Modifier modifier = BorderKt.border-xT4_qwU((Modifier)BackgroundKt.background$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)80)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (Brush)Brush.Companion.radialGradient-P_Vx-Ks$default((Brush.Companion)Brush.Companion, (List)CollectionsKt.listOf((Object[])new Color[]{Color.box-impl((long)themeOption.getPrimaryLight-0d7_KjU()), Color.box-impl((long)themeOption.getPrimary-0d7_KjU())}), (long)0L, (float)0.0f, (int)0, (int)14, null), null, (float)0.0f, (int)6, null), (float)Dp.constructor-impl((float)2), (long)Color.copy-wmQWz5c$default((long)Color.Companion.getWhite-0d7_KjU(), (float)0.8f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()));
            themeOption = Alignment.Companion.getCenter();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            MeasurePolicy measurePolicy = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)themeOption, (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            themeOption = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)themeOption, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            themeOption = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)themeOption);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            themeOption = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1812100906, (String)"C1913@97947L408:MainActivity.kt#to5c3");
            AnimatedContentKt.AnimatedContent((Object)bl, null, null, null, (String)"playPause", null, (Function4)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$-2134722094$app(), (Composer)composer, (int)1597440, (int)46);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    private static final boolean AppControls$lambda$398$lambda$392(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    static final Unit AppControls$lambda$398$lambda$395$lambda$394(TimerManager.TimerState object, Context context, State state) {
        boolean bl = object != TimerManager.TimerState.STOPPED;
        if (bl) {
            object = new Intent(context, TimerService.class);
            object.setAction("ACTION_TOGGLE_MODE");
            context.startService((Intent)object);
        } else {
            boolean bl2 = true ^ MainActivityKt.AppControls$lambda$398$lambda$392((State<Boolean>)state);
            TimerManager.INSTANCE.setBreakMode(bl2);
            TimerManager.INSTANCE.updateState(TimerManager.TimerState.STOPPED);
            context = TimerManager.INSTANCE;
            object = TimerManager.INSTANCE;
            object = bl2 ? object.getBreakTimeSeconds() : object.getFocusTimeSeconds();
            context.updateTime(((Number)object.getValue()).intValue());
            context = TimerManager.INSTANCE;
            object = bl2 ? "Break Time!" : "Focus Time!";
            TimerManager.setTask$default((TimerManager)context, (int)-1, (String)object, null, (int)4, null);
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit AppControls$lambda$398$lambda$397(ThemeOption themeOption, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C1946@99499L577:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1726702438, (int)n, (int)-1, (String)"com.example.AppControls.<anonymous>.<anonymous> (MainActivity.kt:1946)");
            }
            Modifier modifier = BorderKt.border-xT4_qwU((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)56)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)themeOption.getSurface-0d7_KjU(), null, (int)2, null), (float)Dp.constructor-impl((float)((float)1.5)), (long)themeOption.getCardBorder-0d7_KjU(), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()));
            Alignment alignment = Alignment.Companion.getCenter();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            MeasurePolicy measurePolicy = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)alignment, (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            alignment = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)alignment, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            alignment = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)alignment);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            alignment = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1777282270, (String)"C1954@99825L237:MainActivity.kt#to5c3");
            alignment = SyncKt.getSync((Icons.Filled)Icons.Filled.INSTANCE);
            long l = themeOption.getTextPrimary-0d7_KjU();
            themeOption = (Modifier)Modifier.Companion;
            themeOption = SizeKt.size-3ABfNKs((Modifier)themeOption, (float)Dp.constructor-impl((float)28));
            IconKt.Icon-ww6aTOc((ImageVector)alignment, (String)"Switch Mode", (Modifier)themeOption, (long)l, (Composer)composer, (int)432, (int)0);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit AppControls$lambda$399(TimerManager.TimerState timerState, Context context, Function0 function0, int n, int n2, Composer composer, int n3) {
        MainActivityKt.AppControls(timerState, context, (Function0<Unit>)function0, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n | 1)), n2);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void HomeScreen-6a0pyJM(@NotNull TimerViewModel timerViewModel, @NotNull NavController navController, float f, @Nullable Composer composer, int n, int n2) {
        int n3;
        int n4;
        Intrinsics.checkNotNullParameter((Object)timerViewModel, (String)"viewModel");
        Intrinsics.checkNotNullParameter((Object)navController, (String)"navController");
        composer = composer.startRestartGroup(-1473829622);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(HomeScreen)P(2,1,0:c#ui.unit.Dp)482@24033L7,483@24066L47,483@24045L68,486@24157L16,487@24222L16,488@24286L16,489@24359L16,490@24429L16,491@24518L16,492@24613L16,493@24669L16,494@24733L16,495@24820L7,496@24882L7,497@24919L34,499@25004L16,500@25073L16,501@25127L34,502@25193L34,503@25270L32,506@25342L47,506@25313L76,513@25516L139,513@25476L179,519@25694L7,520@25737L7,522@25768L344,532@26215L15169,532@26118L15266:MainActivity.kt#to5c3");
        int n5 = n4 = n;
        if ((n & 6) == 0) {
            n5 = composer.changedInstance((Object)timerViewModel) ? 4 : 2;
            n5 = n4 | n5;
        }
        if ((n3 = n2 & 4) != 0) {
            n5 |= 0x180;
        } else if ((n & 0x180) == 0) {
            n4 = composer.changed(f) ? 256 : 128;
            n5 |= n4;
        }
        if ((n5 & 0x83) == 130 && composer.getSkipping()) {
            composer.skipToGroupEnd();
            Composer composer2 = composer;
        } else {
            if (n3 != 0) {
                f = Dp.constructor-impl((float)0.0f);
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1473829622, (int)n5, (int)-1, (String)"com.example.HomeScreen (MainActivity.kt:481)");
            }
            Object object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume(object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Context context = (Context)object;
            Object object2 = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)988950265, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance((Object)timerViewModel);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = (Function2)new HomeScreen.1.1(timerViewModel, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)object2, (Function2)object, (Composer)composer, (int)6);
            State state = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.getTimerState(), null, (Composer)composer, (int)0, (int)1);
            State state2 = SnapshotStateKt.collectAsState((StateFlow)TimerManager.INSTANCE.isBreakMode(), null, (Composer)composer, (int)0, (int)1);
            State state3 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.getCurrentQuote(), null, (Composer)composer, (int)0, (int)1);
            State state4 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.getTimeRemainingSeconds(), null, (Composer)composer, (int)0, (int)1);
            State state5 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.getCurrentTaskName(), null, (Composer)composer, (int)0, (int)1);
            State state6 = SnapshotStateKt.collectAsState((StateFlow)TimerManager.INSTANCE.getCurrentTaskId(), null, (Composer)composer, (int)0, (int)1);
            SnapshotStateKt.collectAsState((StateFlow)TimerManager.INSTANCE.getCurrentTaskColor(), null, (Composer)composer, (int)0, (int)1);
            State state7 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.getAllTasks(), null, (Composer)composer, (int)0, (int)1);
            State state8 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.isAddingTask(), null, (Composer)composer, (int)0, (int)1);
            object = (CompositionLocal)CompositionLocalsKt.getLocalFocusManager();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume(object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            FocusManager focusManager = (FocusManager)object;
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalView();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume(object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            View view = (View)object;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)988977548, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)false, null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            State state9 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.isSettingsOpen(), null, (Composer)composer, (int)0, (int)1);
            State state10 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.getSettingsTab(), null, (Composer)composer, (int)0, (int)1);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)988984204, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)false, null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState2 = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)988986316, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)false, null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState3 = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SystemProtectionStates systemProtectionStates = SystemProtectionStateKt.rememberSystemProtectionStates((Composer)composer, (int)0);
            bl = MainActivityKt.HomeScreen_6a0pyJM$lambda$94((State<Boolean>)state9);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)988991097, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl2 = composer.changed((Object)state9);
            object = composer.rememberedValue();
            if (bl2 || object == Composer.Companion.getEmpty()) {
                object = (Function2)new HomeScreen.2.1(state9, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)bl, (Function2)object, (Composer)composer, (int)0);
            object2 = SettingsManager.INSTANCE.getUserName();
            object = object2;
            if (object2 == null) {
                object = "Friend";
            }
            TimerManager.TimerState timerState = MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)state);
            bl = MainActivityKt.HomeScreen_6a0pyJM$lambda$83((State<Boolean>)state2);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)988996757, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl3 = composer.changed((Object)state);
            bl2 = composer.changed((Object)state2);
            object2 = composer.rememberedValue();
            if (bl3 | bl2 || object2 == Composer.Companion.getEmpty()) {
                object2 = (Function2)new HomeScreen.3.1(state, state2, mutableState2, null);
                composer.updateRememberedValue(object2);
            }
            object2 = (Function2)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)timerState, (Object)bl, (Function2)object2, (Composer)composer, (int)0);
            object2 = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object2 = composer.consume((CompositionLocal)object2);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            timerState = (ThemeOption)object2;
            object2 = (CompositionLocal)TypographyKt.getLocalAppFont();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object2 = composer.consume((CompositionLocal)object2);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object2 = (FontFamily)object2;
            long l = MainActivityKt.HomeScreen_6a0pyJM$lambda$83((State<Boolean>)state2) ? timerState.getBreakBg-0d7_KjU() : (MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)state) == TimerManager.TimerState.RUNNING ? timerState.getRunningBg-0d7_KjU() : timerState.getBackground-0d7_KjU());
            AnimationSpec animationSpec = (AnimationSpec)AnimationSpecKt.tween$default((int)800, (int)0, (Easing)EasingKt.getLinearEasing(), (int)2, null);
            animationSpec = SingleValueAnimationKt.animateColorAsState-euL9pac((long)l, (AnimationSpec)animationSpec, (String)"bgColor", null, (Composer)composer, (int)384, (int)8);
            animationSpec = BackgroundKt.background-bw27NRU$default((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (long)Color.copy-wmQWz5c$default((long)MainActivityKt.HomeScreen_6a0pyJM$lambda$104((State<Color>)animationSpec), (float)0.85f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), null, (int)2, null);
            object = (Function3)ComposableLambdaKt.rememberComposableLambda((int)-583363852, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda14((ThemeOption)timerState, systemProtectionStates, state2, state, focusManager, context, view, mutableState3, timerViewModel, (FontFamily)object2, (String)object, state4, state5, state7, state6, f, mutableState, state3, mutableState2, state9, state10), (Composer)composer, (int)54);
            BoxWithConstraintsKt.BoxWithConstraints((Modifier)animationSpec, null, (boolean)false, (Function3)object, (Composer)composer, (int)3072, (int)6);
            if (MainActivityKt.HomeScreen_6a0pyJM$lambda$100((MutableState<Boolean>)mutableState3)) {
                composer.startReplaceGroup(610023461);
                ComposerKt.sourceInformation((Composer)composer, (String)"823@41518L31,823@41551L2733,823@41492L2792");
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)989508713, (String)"CC(remember):MainActivity.kt#9igjgp");
                object = composer.rememberedValue();
                if (object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda15(mutableState3);
                    composer.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                AndroidDialog_androidKt.Dialog((Function0)object, null, (Function2)((Function2)ComposableLambdaKt.rememberComposableLambda((int)-255882600, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda16((ThemeOption)timerState, systemProtectionStates, context, timerViewModel, mutableState3), (Composer)composer, (int)54)), (Composer)composer, (int)390, (int)2);
            } else {
                composer.startReplaceGroup(568811224);
            }
            object = composer;
            object.endReplaceGroup();
            if (MainActivityKt.HomeScreen_6a0pyJM$lambda$90((State<Boolean>)state8)) {
                object.startReplaceGroup(612782275);
                ComposerKt.sourceInformation((Composer)object, (String)"877@44358L222,883@44605L34,876@44324L325");
                ComposerKt.sourceInformationMarkerStart((Composer)object, (int)989599784, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl = object.changedInstance((Object)timerViewModel);
                object2 = object.rememberedValue();
                if (bl || object2 == Composer.Companion.getEmpty()) {
                    object2 = new MainActivityKt$$ExternalSyntheticLambda17(timerViewModel);
                    object.updateRememberedValue(object2);
                }
                context = (Function3)object2;
                ComposerKt.sourceInformationMarkerEnd((Composer)object);
                ComposerKt.sourceInformationMarkerStart((Composer)object, (int)989607500, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl = object.changedInstance((Object)timerViewModel);
                object2 = object.rememberedValue();
                if (bl || object2 == Composer.Companion.getEmpty()) {
                    object2 = new MainActivityKt$$ExternalSyntheticLambda18(timerViewModel);
                    object.updateRememberedValue(object2);
                }
                object2 = (Function0)object2;
                ComposerKt.sourceInformationMarkerEnd((Composer)object);
                MainActivityKt.AddTaskCard((Function3<? super String, ? super String, ? super Long, Unit>)context, (Function0<Unit>)object2, (Composer)object, 0);
            } else {
                object.startReplaceGroup(568811224);
            }
            object.endReplaceGroup();
            if (MainActivityKt.HomeScreen_6a0pyJM$lambda$92((MutableState<Boolean>)mutableState)) {
                object.startReplaceGroup(613187724);
                ComposerKt.sourceInformation((Composer)object, (String)"889@44743L29,892@44911L1347,888@44694L1564");
                l = timerState.getBackground-0d7_KjU();
                context = RoundedCornerShapeKt.RoundedCornerShape-a9UjIt4$default((float)Dp.constructor-impl((float)24), (float)Dp.constructor-impl((float)24), (float)0.0f, (float)0.0f, (int)12, null);
                ComposerKt.sourceInformationMarkerStart((Composer)object, (int)989611911, (String)"CC(remember):MainActivity.kt#9igjgp");
                object2 = object.rememberedValue();
                if (object2 == Composer.Companion.getEmpty()) {
                    object2 = new MainActivityKt$$ExternalSyntheticLambda19(mutableState);
                    object.updateRememberedValue(object2);
                }
                object2 = (Function0)object2;
                ComposerKt.sourceInformationMarkerEnd((Composer)object);
                context = (Shape)context;
                ModalBottomSheetKt.ModalBottomSheet-dYc4hso((Function0)object2, null, null, (float)0.0f, (Shape)context, (long)l, (long)0L, (float)0.0f, (long)0L, null, null, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-1829454694, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda20((ThemeOption)timerState, state7, state6, timerViewModel, mutableState), (Composer)object, (int)54)), (Composer)object, (int)6, (int)384, (int)4046);
            } else {
                object.startReplaceGroup(568811224);
            }
            object = composer;
            object.endReplaceGroup();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        composer = composer.endRestartGroup();
        if (composer != null) {
            composer.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda21(timerViewModel, navController, f, n, n2));
        }
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$100(MutableState<Boolean> mutableState) {
        return (Boolean)((State)mutableState).getValue();
    }

    private static final void HomeScreen_6a0pyJM$lambda$101(MutableState<Boolean> mutableState, boolean bl) {
        mutableState.setValue((Object)bl);
    }

    private static final long HomeScreen_6a0pyJM$lambda$104(State<Color> state) {
        return ((Color)state.getValue()).unbox-impl();
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160(ThemeOption themeOption, SystemProtectionStates systemProtectionStates, State state, State state2, FocusManager focusManager, Context context, View view, MutableState mutableState, TimerViewModel timerViewModel, FontFamily fontFamily, String string, State state3, State state4, State state5, State state6, float f, MutableState mutableState2, State state7, MutableState mutableState3, State state8, State state9, BoxWithConstraintsScope boxWithConstraintsScope, Composer composer, int n) {
        int n2;
        Intrinsics.checkNotNullParameter((Object)boxWithConstraintsScope, (String)"$this$BoxWithConstraints");
        ComposerKt.sourceInformation((Composer)composer, (String)"C541@26643L14699,537@26492L14850:MainActivity.kt#to5c3");
        int n3 = n2 = n;
        if ((n & 6) == 0) {
            n = composer.changed((Object)boxWithConstraintsScope) ? 4 : 2;
            n3 = n2 | n;
        }
        if ((n3 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-583363852, (int)n3, (int)-1, (String)"com.example.HomeScreen.<anonymous> (MainActivity.kt:533)");
            }
            boolean bl = Intrinsics.areEqual((Object)themeOption.getId(), (Object)"premium");
            boolean bl2 = false;
            if (bl && !MainActivityKt.HomeScreen_6a0pyJM$lambda$83((State<Boolean>)state)) {
                composer.startReplaceGroup(-1554773407);
                ComposerKt.sourceInformation((Composer)composer, (String)"534@26315L27");
                PomoUIKt.PremiumBackgroundOverlays((Composer)composer, (int)0);
            } else {
                composer.startReplaceGroup(-1580841586);
            }
            composer.endReplaceGroup();
            bl = bl2;
            if (systemProtectionStates.getNeedsAttention()) {
                bl = bl2;
                if (MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)state2) == TimerManager.TimerState.STOPPED) {
                    bl = true;
                }
            }
            float f2 = boxWithConstraintsScope.getMaxWidth-D9Ej5fM();
            float f3 = boxWithConstraintsScope.getMaxHeight-D9Ej5fM();
            AdaptiveSystemKt.ProvideAdaptiveDimensions-if577FI((float)f2, (float)f3, (boolean)bl, (Function2)((Function2)ComposableLambdaKt.rememberComposableLambda((int)-1821730338, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda53(focusManager, themeOption, systemProtectionStates, context, view, state2, mutableState, timerViewModel, fontFamily, string, state3, state, state4, state5, state6, f, bl, mutableState2, state7, mutableState3, state8, state9), (Composer)composer, (int)54)), (Composer)composer, (int)3072);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159(FocusManager object, ThemeOption themeOption, SystemProtectionStates systemProtectionStates, Context context, View view, State state, MutableState mutableState, TimerViewModel timerViewModel, FontFamily fontFamily, String string, State state2, State state3, State state4, State state5, State state6, float f, boolean bl, MutableState mutableState2, State state7, MutableState mutableState3, State state8, State state9, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C542@26732L7,557@27296L88,562@27510L12589,556@27221L12878,803@40512L230,799@40109L633,813@40862L6,814@41037L6,816@41210L122,811@40751L581:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1821730338, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous> (MainActivity.kt:542)");
            }
            Object object2 = (CompositionLocal)AdaptiveSystemKt.getLocalAdaptiveDimensions();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object2 = composer.consume(object2);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            AdaptiveDimensions adaptiveDimensions = (AdaptiveDimensions)object2;
            float f2 = adaptiveDimensions.isVeryCompact() ? Dp.constructor-impl((float)180) : (adaptiveDimensions.isCompact() ? Dp.constructor-impl((float)220) : Dp.constructor-impl((float)260));
            float f3 = adaptiveDimensions.getScale();
            f3 = Dp.constructor-impl((float)(f2 * f3));
            f2 = adaptiveDimensions.isVeryCompact() ? Dp.constructor-impl((float)12) : (adaptiveDimensions.isCompact() ? Dp.constructor-impl((float)16) : Dp.constructor-impl((float)24));
            float f4 = adaptiveDimensions.getScale();
            f2 = Dp.constructor-impl((float)(f2 * f4));
            Modifier modifier = SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            Unit unit = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1663822934, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl2 = composer.changedInstance(object);
            object2 = composer.rememberedValue();
            if (!bl2 && object2 != Composer.Companion.getEmpty()) {
                object = object2;
            } else {
                object = (Function2)new HomeScreen.4.1.1.1(object, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ScaffoldKt.Scaffold-TvnljyQ((Modifier)WindowInsetsPadding_androidKt.imePadding((Modifier)SuspendingPointerInputFilterKt.pointerInput((Modifier)modifier, (Object)unit, (Function2)object)), null, null, null, null, (int)0, (long)Color.Companion.getTransparent-0d7_KjU(), (long)0L, (WindowInsets)WindowInsetsKt.WindowInsets-a9UjIt4$default((float)Dp.constructor-impl((float)0.0f), (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)1114272045, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda151(systemProtectionStates, f2, context, f3, view, state, mutableState, themeOption, timerViewModel, fontFamily, string, state2, state3, state4, state5, state6, f, adaptiveDimensions, bl, mutableState2, state7, mutableState3), (Composer)composer, (int)54)), (Composer)composer, (int)806879232, (int)190);
            AnimatedVisibilityKt.AnimatedVisibility((boolean)MainActivityKt.HomeScreen_6a0pyJM$lambda$97((MutableState<Boolean>)mutableState3), null, (EnterTransition)EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)500, (int)0, null, (int)6, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.scaleIn-L8ZKh-E$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.5f, (float)200.0f, null, (int)4, null)), (float)0.95f, (long)0L, (int)4, null)), (ExitTransition)EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, null, (int)6, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.scaleOut-L8ZKh-E$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)1500.0f, null, (int)5, null)), (float)0.95f, (long)0L, (int)4, null)), null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-1686702922, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda152(state7, state4, state2, mutableState3), (Composer)composer, (int)54)), (Composer)composer, (int)196608, (int)18);
            bl = MainActivityKt.HomeScreen_6a0pyJM$lambda$94((State<Boolean>)state8);
            systemProtectionStates = (FiniteAnimationSpec)AnimationSpecKt.spring$default((float)1.0f, (float)200.0f, null, (int)4, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1664256964, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda153();
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            systemProtectionStates = EnterExitTransitionKt.slideInVertically((FiniteAnimationSpec)systemProtectionStates, (Function1)object).plus(EnterExitTransitionKt.fadeIn$default(null, (float)0.0f, (int)3, null));
            context = (FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)1500.0f, null, (int)5, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1664262564, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda154();
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = EnterExitTransitionKt.slideOutVertically((FiniteAnimationSpec)context, (Function1)object).plus(EnterExitTransitionKt.fadeOut$default(null, (float)0.0f, (int)3, null));
            themeOption = BackgroundKt.background-bw27NRU$default((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (long)themeOption.getBackground-0d7_KjU(), null, (int)2, null);
            AnimatedVisibilityKt.AnimatedVisibility((boolean)bl, (Modifier)themeOption, (EnterTransition)systemProtectionStates, (ExitTransition)object, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)382343725, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda155(timerViewModel, state9), (Composer)composer, (int)54)), (Composer)composer, (int)196608, (int)16);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148(SystemProtectionStates object, float f, Context context, float f2, View view, State state, MutableState mutableState, ThemeOption themeOption, TimerViewModel timerViewModel, FontFamily object22, String string, State object3, State state2, State object4, State state3, State state4, float f3, AdaptiveDimensions adaptiveDimensions, boolean bl, MutableState mutableState2, State state5, MutableState mutableState3, PaddingValues paddingValues, Composer composer, int n) {
        int n2;
        Intrinsics.checkNotNullParameter((Object)paddingValues, (String)"innerPadding");
        ComposerKt.sourceInformation((Composer)composer, (String)"C568@27724L8,563@27536L12557:MainActivity.kt#to5c3");
        int n3 = n2 = n;
        if ((n & 6) == 0) {
            n = composer.changed((Object)paddingValues) ? 4 : 2;
            n3 = n2 | n;
        }
        if ((n3 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            boolean bl2;
            boolean bl3;
            block40: {
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventStart((int)1114272045, (int)n3, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:563)");
                }
                Modifier modifier = PaddingKt.padding-VpY3zN4$default((Modifier)WindowInsetsPadding_androidKt.statusBarsPadding((Modifier)PaddingKt.padding((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (PaddingValues)paddingValues)), (float)ScaledDpKt.getScaledDp((int)24, (Composer)composer, (int)6), (float)0.0f, (int)2, null);
                paddingValues = Alignment.Companion.getCenterHorizontally();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
                Object object5 = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)paddingValues, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                paddingValues = composer.getCurrentCompositionLocalMap();
                modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
                Function0 function0 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function0);
                } else {
                    composer.useNode();
                }
                function0 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function0, (Object)object5, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function0, (Object)paddingValues, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                paddingValues = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                    function0.updateRememberedValue((Object)n);
                    function0.apply((Object)n, (Function2)paddingValues);
                }
                Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
                paddingValues = (ColumnScope)ColumnScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)11001895, (String)"C571@27859L8,571@27822L47,578@28319L244,574@27960L603,588@28650L8,587@28577L4001,662@32591L51,665@32685L475,676@33174L51,682@33381L597,679@33263L729,695@34006L51,697@34071L6012:MainActivity.kt#to5c3");
                SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)ScaledDpKt.getScaledDp((int)16, (Composer)composer, (int)6)), (Composer)composer, (int)0);
                bl3 = object.getNeedsAttention() && MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)state) == TimerManager.TimerState.STOPPED;
                AnimatedVisibilityKt.AnimatedVisibility((boolean)bl3, null, (EnterTransition)EnterExitTransitionKt.expandVertically$default(null, null, (boolean)false, null, (int)15, null).plus(EnterExitTransitionKt.fadeIn$default(null, (float)0.0f, (int)3, null)), (ExitTransition)EnterExitTransitionKt.shrinkVertically$default(null, null, (boolean)false, null, (int)15, null).plus(EnterExitTransitionKt.fadeOut$default(null, (float)0.0f, (int)3, null)), null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)1831168415, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda116((SystemProtectionStates)object, mutableState), (Composer)composer, (int)54)), (Composer)composer, (int)200064, (int)18);
                object5 = PaddingKt.padding-qDBjuR0$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)0.0f, (float)ScaledDpKt.getScaledDp((int)16, (Composer)composer, (int)6), (float)0.0f, (float)0.0f, (int)13, null);
                modifier = (Arrangement.Horizontal)Arrangement.INSTANCE.getSpaceBetween();
                function0 = Alignment.Companion.getCenterVertically();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
                modifier = RowKt.rowMeasurePolicy((Arrangement.Horizontal)modifier, (Alignment.Vertical)function0, (Composer)composer, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                function0 = composer.getCurrentCompositionLocalMap();
                object5 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object5);
                Function0 function02 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function02);
                } else {
                    composer.useNode();
                }
                function02 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function02, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function02, (Object)function0, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                modifier = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                    function02.updateRememberedValue((Object)n);
                    function02.apply((Object)n, (Function2)modifier);
                }
                Updater.set-impl((Composer)function02, (Object)object5, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
                object5 = (RowScope)RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1359472242, (String)"C592@28822L1779,628@30661L79,629@30802L25,630@30900L376,646@31866L288,636@31310L1254:MainActivity.kt#to5c3");
                object5 = Alignment.Companion.getCenterVertically();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
                function0 = (Modifier)Modifier.Companion;
                modifier = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object5, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                object5 = composer.getCurrentCompositionLocalMap();
                function0 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)function0);
                function02 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function02);
                } else {
                    composer.useNode();
                }
                function02 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function02, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function02, (Object)object5, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                object5 = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                    function02.updateRememberedValue((Object)n);
                    function02.apply((Object)n, (Function2)object5);
                }
                Updater.set-impl((Composer)function02, (Object)function0, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
                object5 = (RowScope)RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2116719491, (String)"C593@28896L830,608@29747L40,609@29808L775:MainActivity.kt#to5c3");
                object5 = BorderKt.border-xT4_qwU((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)ShadowKt.shadow-s4CzXII$default((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)44)), (float)Dp.constructor-impl((float)3), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16))), (boolean)false, (long)0L, (long)themeOption.getShadowColor-0d7_KjU(), (int)12, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16)))), (long)themeOption.getSurface-0d7_KjU(), null, (int)2, null), (float)Dp.constructor-impl((float)1.0f), (long)themeOption.getCardBorder-0d7_KjU(), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16))));
                modifier = Alignment.Companion.getCenter();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
                modifier = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)modifier, (boolean)false);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                function0 = composer.getCurrentCompositionLocalMap();
                object5 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object5);
                function02 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function02);
                } else {
                    composer.useNode();
                }
                function02 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function02, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function02, (Object)function0, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                modifier = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                    function02.updateRememberedValue((Object)n);
                    function02.apply((Object)n, (Function2)modifier);
                }
                Updater.set-impl((Composer)function02, (Object)object5, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
                object5 = (BoxScope)BoxScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-891514051, (String)"C602@29445L259:MainActivity.kt#to5c3");
                object5 = MascotExpression.IDLE;
                modifier = (Modifier)Modifier.Companion;
                modifier = PaddingKt.padding-qDBjuR0$default((Modifier)SizeKt.size-3ABfNKs((Modifier)modifier, (float)Dp.constructor-impl((float)32)), (float)0.0f, (float)Dp.constructor-impl((float)2), (float)0.0f, (float)0.0f, (int)13, null);
                PomoPalMascotKt.PomoPalMascot((MascotExpression)object5, (Modifier)modifier, (boolean)false, (boolean)false, (Composer)composer, (int)438, (int)8);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)12)), (Composer)composer, (int)6);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
                function0 = (Modifier)Modifier.Companion;
                modifier = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                object5 = composer.getCurrentCompositionLocalMap();
                function0 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)function0);
                function02 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function02);
                } else {
                    composer.useNode();
                }
                function02 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function02, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function02, (Object)object5, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                object5 = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                    function02.updateRememberedValue((Object)n);
                    function02.apply((Object)n, (Function2)object5);
                }
                Updater.set-impl((Composer)function02, (Object)function0, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
                object5 = (ColumnScope)ColumnScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-132418470, (String)"C612@29935L8,610@29841L374,620@30340L8,618@30240L321:MainActivity.kt#to5c3");
                long l = ScaledSpKt.getScaledSp((int)24, (Composer)composer, (int)6);
                modifier = FontWeight.Companion.getBold();
                long l2 = themeOption.getTextPrimary-0d7_KjU();
                object5 = PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)0.0f), (int)7, null);
                TextKt.Text--4IGK_g((String)"PomoPal", (Modifier)object5, (long)l2, (long)l, null, (FontWeight)modifier, (FontFamily)object22, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196662, (int)0, (int)130960);
                object5 = "Hi, " + string;
                l = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
                l2 = themeOption.getTextSecondary-0d7_KjU();
                string = PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)Dp.constructor-impl((float)0.0f), (float)0.0f, (float)0.0f, (int)13, null);
                TextKt.Text--4IGK_g((String)object5, (Modifier)string, (long)l2, (long)l, null, null, (FontFamily)object22, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)48, (int)0, (int)130992);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1983571778, (String)"CC(remember):MainActivity.kt#9igjgp");
                object22 = composer.rememberedValue();
                if (object22 == Composer.Companion.getEmpty()) {
                    object22 = InteractionSourceKt.MutableInteractionSource();
                    composer.updateRememberedValue(object22);
                }
                object5 = (MutableInteractionSource)object22;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                float f4 = MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$113((State<Boolean>)PressInteractionKt.collectIsPressedAsState((InteractionSource)((InteractionSource)object5), (Composer)composer, (int)6)) ? 90.0f : 0.0f;
                object22 = (AnimationSpec)AnimationSpecKt.spring$default((float)0.5f, (float)200.0f, null, (int)4, null);
                string = AnimateAsStateKt.animateFloatAsState((float)f4, (AnimationSpec)object22, (float)0.0f, (String)"settingsRotation", null, (Composer)composer, (int)3120, (int)20);
                modifier = BorderKt.border-xT4_qwU((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)ShadowKt.shadow-s4CzXII$default((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)42)), (float)Dp.constructor-impl((float)3), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()), (boolean)false, (long)0L, (long)themeOption.getShadowColor-0d7_KjU(), (int)12, null), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)themeOption.getSurface-0d7_KjU(), null, (int)2, null), (float)Dp.constructor-impl((float)1.0f), (long)themeOption.getCardBorder-0d7_KjU(), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()));
                function0 = (Indication)RippleKt.ripple-H2RKhps$default((boolean)false, (float)0.0f, (long)0L, (int)7, null);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1983610547, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl2 = composer.changedInstance((Object)view);
                bl3 = composer.changedInstance((Object)timerViewModel);
                object22 = composer.rememberedValue();
                if (bl2 | bl3 || object22 == Composer.Companion.getEmpty()) {
                    object22 = new MainActivityKt$$ExternalSyntheticLambda117(view, timerViewModel);
                    composer.updateRememberedValue(object22);
                }
                object22 = (Function0)object22;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                object22 = ClickableKt.clickable-O2vRcR0$default((Modifier)modifier, (MutableInteractionSource)object5, (Indication)function0, (boolean)false, null, null, (Function0)object22, (int)28, null);
                object5 = Alignment.Companion.getCenter();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
                object5 = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)object5, (boolean)false);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                modifier = composer.getCurrentCompositionLocalMap();
                object22 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object22);
                function0 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function0);
                } else {
                    composer.useNode();
                }
                function0 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function0, (Object)object5, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                object5 = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                    function0.updateRememberedValue((Object)n);
                    function0.apply((Object)n, (Function2)object5);
                }
                Updater.set-impl((Composer)function0, (Object)object22, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
                object22 = (BoxScope)BoxScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1893904431, (String)"C658@32492L32,654@32253L293:MainActivity.kt#to5c3");
                object5 = SettingsKt.getSettings((Icons.Filled)Icons.Filled.INSTANCE);
                l = themeOption.getTextPrimary-0d7_KjU();
                modifier = (Modifier)Modifier.Companion;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1462934349, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl3 = composer.changed((Object)string);
                object22 = composer.rememberedValue();
                if (bl3 || object22 == Composer.Companion.getEmpty()) {
                    object22 = new MainActivityKt$$ExternalSyntheticLambda118((State)string);
                    composer.updateRememberedValue(object22);
                }
                object22 = (Function1)object22;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                object22 = GraphicsLayerModifierKt.graphicsLayer((Modifier)modifier, (Function1)object22);
                IconKt.Icon-ww6aTOc((ImageVector)object5, (String)"Settings", (Modifier)object22, (long)l, (Composer)composer, (int)48, (int)0);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)f), (Composer)composer, (int)0);
                n3 = MainActivityKt.HomeScreen_6a0pyJM$lambda$85((State<Integer>)object3);
                n = MainActivityKt.HomeScreen_6a0pyJM$lambda$83((State<Boolean>)state2) ? SettingsManager.INSTANCE.getBreakTimeMins() : SettingsManager.INSTANCE.getFocusTimeMins();
                n *= 60;
                string = MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)state);
                bl3 = MainActivityKt.HomeScreen_6a0pyJM$lambda$83((State<Boolean>)state2);
                object3 = MainActivityKt.HomeScreen_6a0pyJM$lambda$86((State<String>)object4);
                for (Object object22 : (Iterable)MainActivityKt.HomeScreen_6a0pyJM$lambda$89((State<? extends List<TaskItem>>)state3)) {
                    n2 = ((TaskItem)object22).getId() == MainActivityKt.HomeScreen_6a0pyJM$lambda$87((State<Integer>)state4) ? 1 : 0;
                    if (n2 == 0) continue;
                    break block40;
                }
                object22 = null;
            }
            object22 = (TaskItem)object22;
            MainActivityKt.TimerDisplay-egy_3UM(n3, n, (TimerManager.TimerState)string, bl3, (String)object3, (TaskItem)object22, context, f2, composer, 0, 0);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)f), (Composer)composer, (int)0);
            string = MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)state);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)277615788, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl3 = composer.changed(object);
            boolean bl4 = composer.changedInstance((Object)view);
            bl2 = composer.changedInstance((Object)context);
            object22 = composer.rememberedValue();
            if (!(bl3 | bl4 | bl2) && object22 != Composer.Companion.getEmpty()) {
                object = object22;
            } else {
                object = new MainActivityKt$$ExternalSyntheticLambda119((SystemProtectionStates)object, view, context, mutableState);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            MainActivityKt.AppControls((TimerManager.TimerState)string, context, (Function0<Unit>)object, composer, 0, 0);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)f), (Composer)composer, (int)0);
            object = paddingValues.weight(SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), 1.0f, false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            context = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)Alignment.Companion.getTopStart(), (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            mutableState = composer.getCurrentCompositionLocalMap();
            object = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object);
            object22 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)object22);
            } else {
                composer.useNode();
            }
            object22 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)object22, (Object)context, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object22, (Object)mutableState, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            context = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object22.getInserting() || !Intrinsics.areEqual((Object)object22.rememberedValue(), (Object)n)) {
                object22.updateRememberedValue((Object)n);
                object22.apply((Object)n, (Function2)context);
            }
            Updater.set-impl((Composer)object22, (Object)object, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            object = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1467317375, (String)"C704@34361L598,711@35023L5046,702@34226L5843:MainActivity.kt#to5c3");
            bl3 = MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)state) == TimerManager.TimerState.STOPPED;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1709899879, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda120();
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            AnimatedContentKt.AnimatedContent((Object)bl3, null, (Function1)object, null, (String)"bottomContent", null, (Function4)((Function4)ComposableLambdaKt.rememberComposableLambda((int)-1191917170, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda121(f3, adaptiveDimensions, bl, themeOption, timerViewModel, state3, state4, mutableState2, view, state5, state2, state, mutableState3), (Composer)composer, (int)54)), (Composer)composer, (int)1597824, (int)42);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$108(SystemProtectionStates systemProtectionStates, MutableState object, AnimatedVisibilityScope object2, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$AnimatedVisibility");
        ComposerKt.sourceInformation((Composer)composer, (String)"C581@28457L74,579@28352L197:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)1831168415, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:579)");
        }
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1183740055, (String)"CC(remember):MainActivity.kt#9igjgp");
        object2 = composer.rememberedValue();
        if (object2 == Composer.Companion.getEmpty()) {
            object = new MainActivityKt$$ExternalSyntheticLambda111((MutableState)object);
            composer.updateRememberedValue(object);
        } else {
            object = object2;
        }
        object = (Function0)object;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        SystemProtectionStateKt.SystemProtectionBanner((SystemProtectionStates)systemProtectionStates, (Function0)object, (Composer)composer, (int)48);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$108$lambda$107$lambda$106(MutableState mutableState) {
        MainActivityKt.HomeScreen_6a0pyJM$lambda$101((MutableState<Boolean>)mutableState, true);
        return Unit.INSTANCE;
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$113(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    private static final float HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$114(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$116$lambda$115(View view, TimerViewModel timerViewModel) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        timerViewModel.setSettingsOpen(true, 0);
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$119$lambda$118$lambda$117(State state, GraphicsLayerScope graphicsLayerScope) {
        Intrinsics.checkNotNullParameter((Object)graphicsLayerScope, (String)"$this$graphicsLayer");
        graphicsLayerScope.setRotationZ(MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$114((State<Float>)state));
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$124$lambda$123(SystemProtectionStates systemProtectionStates, View view, Context context, MutableState mutableState) {
        if (systemProtectionStates.getNeedsAttention()) {
            try {
                view.performHapticFeedback(0);
            }
            catch (Exception exception) {
                // empty catch block
            }
            MainActivityKt.HomeScreen_6a0pyJM$lambda$101((MutableState<Boolean>)mutableState, true);
        } else {
            systemProtectionStates = new Intent(context, TimerService.class);
            systemProtectionStates.setAction("ACTION_START");
            context.startService((Intent)systemProtectionStates);
        }
        return Unit.INSTANCE;
    }

    static final ContentTransform HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$128$lambda$127(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$AnimatedContent");
        SpringSpec springSpec = AnimationSpecKt.spring$default((float)0.5f, (float)200.0f, null, (int)4, null);
        animatedContentTransitionScope = AnimationSpecKt.spring$default((float)0.0f, (float)1500.0f, null, (int)5, null);
        return AnimatedContentKt.togetherWith((EnterTransition)EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.slideInVertically((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec), (Function1)new MainActivityKt$$ExternalSyntheticLambda2())), (ExitTransition)EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)1500.0f, null, (int)5, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.slideOutVertically((FiniteAnimationSpec)((FiniteAnimationSpec)animatedContentTransitionScope), (Function1)new MainActivityKt$$ExternalSyntheticLambda3())));
    }

    static final int HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$128$lambda$127$lambda$125(int n) {
        return 100;
    }

    static final int HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$128$lambda$127$lambda$126(int n) {
        return -100;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145(float f, AdaptiveDimensions object, boolean bl, ThemeOption themeOption, TimerViewModel object2, State state, State state2, MutableState mutableState, View view, State object3, State object4, State object5, MutableState mutableState2, AnimatedContentScope object6, boolean bl2, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object6, (String)"$this$AnimatedContent");
        ComposerKt.sourceInformation((Composer)composer, (String)"C:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-1191917170, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:712)");
        }
        if (bl2) {
            composer.startReplaceGroup(-1909641547);
            ComposerKt.sourceInformation((Composer)composer, (String)"714@35177L8,713@35095L2799");
            view = Arrangement.INSTANCE.spacedBy-0680j_4(ScaledDpKt.getScaledDp((int)12, (Composer)composer, (int)6));
            object3 = PaddingKt.padding-qDBjuR0$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)(f + Dp.constructor-impl((float)88))), (int)7, null);
            view = (Arrangement.Vertical)view;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            view = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)view, (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object4 = composer.getCurrentCompositionLocalMap();
            object3 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object3);
            object5 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)object5);
            } else {
                composer.useNode();
            }
            object5 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)object5, (Object)view, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object5, (Object)object4, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            view = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object5.getInserting() || !Intrinsics.areEqual((Object)object5.rememberedValue(), (Object)n)) {
                object5.updateRememberedValue((Object)n);
                object5.apply((Object)n, (Function2)view);
            }
            Updater.set-impl((Composer)object5, (Object)object3, (Function2)ComposeUiNode.Companion.getSetModifier());
            view = composer;
            ComposerKt.sourceInformationMarkerStart((Composer)view, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            mutableState2 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)view, (int)-223427509, (String)"C718@35438L8,717@35347L994:MainActivity.kt#to5c3");
            object3 = PaddingKt.padding-qDBjuR0$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)0.0f, (float)0.0f, (float)0.0f, (float)ScaledDpKt.getScaledDp((int)4, (Composer)view, (int)6), (int)7, null);
            object4 = (Arrangement.Horizontal)Arrangement.INSTANCE.getSpaceBetween();
            object5 = Alignment.Companion.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart((Composer)view, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            object4 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)object4, (Alignment.Vertical)object5, (Composer)view, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)view, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)view, (int)0);
            object5 = view.getCurrentCompositionLocalMap();
            object3 = ComposedModifierKt.materializeModifier((Composer)view, (Modifier)object3);
            object6 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)view, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(view.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            view.startReusableNode();
            if (view.getInserting()) {
                view.createNode((Function0)object6);
            } else {
                view.useNode();
            }
            object6 = Updater.constructor-impl((Composer)view);
            Updater.set-impl((Composer)object6, (Object)object4, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object6, (Object)object5, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object4 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object6.getInserting() || !Intrinsics.areEqual((Object)object6.rememberedValue(), (Object)n)) {
                object6.updateRememberedValue((Object)n);
                object6.apply((Object)n, (Function2)object4);
            }
            Updater.set-impl((Composer)object6, (Object)object3, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object3 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-814024253, (String)"C724@35787L8,725@35846L13,722@35674L355,729@36083L33,729@36151L160,729@36062L249:MainActivity.kt#to5c3");
            long l = ScaledSpKt.getScaledSp((int)20, (Composer)composer, (int)6);
            object3 = TypographyKt.getAppFontFamily((Composer)composer, (int)0);
            object4 = FontWeight.Companion.getBold();
            long l2 = themeOption.getTextPrimary-0d7_KjU();
            TextKt.Text--4IGK_g((String)"Your Tasks", null, (long)l2, (long)l, null, (FontWeight)object4, (FontFamily)object3, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)130962);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1134625020, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl2 = composer.changedInstance(object2);
            object3 = composer.rememberedValue();
            if (bl2 || object3 == Composer.Companion.getEmpty()) {
                object3 = new MainActivityKt$$ExternalSyntheticLambda29((TimerViewModel)object2);
                composer.updateRememberedValue(object3);
            }
            object3 = (Function0)object3;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            IconButtonKt.IconButton((Function0)object3, (Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)36)), (boolean)false, null, null, (Function2)((Function2)ComposableLambdaKt.rememberComposableLambda((int)1563276960, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda30(themeOption), (Composer)composer, (int)54)), (Composer)composer, (int)196656, (int)28);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (object.isVeryCompact()) {
                n = bl ? 1 : 2;
            } else {
                bl2 = object.isCompact();
                n = 3;
                if (bl2) {
                    if (bl) {
                        n = 2;
                    }
                } else if (bl) {
                    // empty if block
                }
            }
            object4 = CollectionsKt.take((Iterable)MainActivityKt.HomeScreen_6a0pyJM$lambda$89((State<? extends List<TaskItem>>)state), (int)n);
            view.startReplaceGroup(-976991851);
            ComposerKt.sourceInformation((Composer)view, (String)"*746@37109L61,747@37219L30,743@36925L358");
            object3 = (Iterable)object4;
            object6 = object3.iterator();
            object = view;
            while (object6.hasNext()) {
                TaskItem taskItem = (TaskItem)object6.next();
                bl = MainActivityKt.HomeScreen_6a0pyJM$lambda$87((State<Integer>)state2) == taskItem.getId();
                ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1836367085, (String)"CC(remember):MainActivity.kt#9igjgp");
                boolean bl3 = object.changedInstance(object2);
                bl2 = object.changed((Object)taskItem);
                object5 = object.rememberedValue();
                if (bl3 | bl2 || object5 == Composer.Companion.getEmpty()) {
                    object5 = new MainActivityKt$$ExternalSyntheticLambda31((TimerViewModel)object2, taskItem);
                    object.updateRememberedValue(object5);
                }
                Function1 function1 = (Function1)object5;
                ComposerKt.sourceInformationMarkerEnd((Composer)object);
                ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1836363596, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl2 = object.changedInstance(object2);
                bl3 = object.changed((Object)taskItem);
                object5 = object.rememberedValue();
                if (bl2 | bl3 || object5 == Composer.Companion.getEmpty()) {
                    object5 = new MainActivityKt$$ExternalSyntheticLambda32((TimerViewModel)object2, taskItem);
                    object.updateRememberedValue(object5);
                }
                object5 = (Function0)object5;
                ComposerKt.sourceInformationMarkerEnd((Composer)object);
                MainActivityKt.TaskItemRow(taskItem, bl, (Function1<? super TaskItem, Unit>)function1, (Function0<Unit>)object5, null, (Composer)object, 0, 16);
            }
            object.endReplaceGroup();
            if (MainActivityKt.HomeScreen_6a0pyJM$lambda$89((State<? extends List<TaskItem>>)state).size() > n) {
                object.startReplaceGroup(-221446889);
                ComposerKt.sourceInformation((Composer)object, (String)"753@37500L28,755@37676L162,752@37442L396");
                ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-976972261, (String)"CC(remember):MainActivity.kt#9igjgp");
                object2 = object.rememberedValue();
                if (object2 == Composer.Companion.getEmpty()) {
                    object2 = new MainActivityKt$$ExternalSyntheticLambda33(mutableState);
                    object.updateRememberedValue(object2);
                }
                object2 = (Function0)object2;
                ComposerKt.sourceInformationMarkerEnd((Composer)object);
                ButtonKt.TextButton((Function0)object2, (Modifier)PaddingKt.padding-qDBjuR0$default((Modifier)mutableState2.align((Modifier)Modifier.Companion, Alignment.Companion.getCenterHorizontally()), (float)0.0f, (float)Dp.constructor-impl((float)8), (float)0.0f, (float)0.0f, (int)13, null), (boolean)false, null, null, null, null, null, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-1572464585, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda34(themeOption), (Composer)object, (int)54)), (Composer)object, (int)0x30000006, (int)508);
            } else {
                object.startReplaceGroup(-258570877);
            }
            object.endReplaceGroup();
            ComposerKt.sourceInformationMarkerEnd((Composer)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)view);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endReplaceGroup();
        } else {
            composer.startReplaceGroup(-1906833629);
            ComposerKt.sourceInformation((Composer)composer, (String)"764@38100L21,761@37948L2081");
            object2 = ScrollKt.verticalScroll$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (ScrollState)ScrollKt.rememberScrollState((int)0, (Composer)composer, (int)0, (int)1), (boolean)false, null, (boolean)false, (int)14, null);
            object = Alignment.Companion.getCenterHorizontally();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            themeOption = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)object, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object = composer.getCurrentCompositionLocalMap();
            object2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object2);
            state = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)state);
            } else {
                composer.useNode();
            }
            state = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)state, (Object)themeOption, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)state, (Object)object, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (state.getInserting() || !Intrinsics.areEqual((Object)state.rememberedValue(), (Object)n)) {
                state.updateRememberedValue((Object)n);
                state.apply((Object)n, (Function2)object);
            }
            Updater.set-impl((Composer)state, (Object)object2, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-788601397, (String)"C767@38259L62,791@39946L57:MainActivity.kt#to5c3");
            MainActivityKt.AnimatedQuoteCard(MainActivityKt.HomeScreen_6a0pyJM$lambda$84((State<String>)object3), MainActivityKt.HomeScreen_6a0pyJM$lambda$83((State<Boolean>)object4), composer, 0);
            if (!MainActivityKt.HomeScreen_6a0pyJM$lambda$83((State<Boolean>)object4) && MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)object5) == TimerManager.TimerState.RUNNING) {
                composer.startReplaceGroup(-788424357);
                ComposerKt.sourceInformation((Composer)composer, (String)"770@38480L41,776@38932L48,777@39045L40,772@38608L262,771@38554L1333");
                SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
                themeOption = ButtonDefaults.INSTANCE.buttonColors-ro_MJ88(ColorKt.Color((long)0xFFFF5252L), 0L, 0L, 0L, composer, ButtonDefaults.$stable << 12 | 6, 14);
                state = ButtonDefaults.INSTANCE.buttonElevation-R_JCAzs(Dp.constructor-impl((float)4), 0.0f, 0.0f, 0.0f, 0.0f, composer, ButtonDefaults.$stable << 15 | 6, 30);
                state2 = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
                object2 = SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)48));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1272355058, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl = composer.changedInstance((Object)view);
                object = composer.rememberedValue();
                if (bl || object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda35(view, mutableState2);
                    composer.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                state2 = (Shape)state2;
                ButtonKt.Button((Function0)object, (Modifier)object2, (boolean)false, (Shape)state2, (ButtonColors)themeOption, (ButtonElevation)state, null, null, null, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$-1375187853$app(), (Composer)composer, (int)0x30000030, (int)452);
                object = composer;
            } else {
                object = composer;
                object.startReplaceGroup(-826609382);
            }
            object = composer;
            object.endReplaceGroup();
            themeOption = (Modifier)Modifier.Companion;
            float f2 = Dp.constructor-impl((float)88);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)themeOption, (float)Dp.constructor-impl((float)(f + f2))), (Composer)object, (int)0);
            ComposerKt.sourceInformationMarkerEnd((Composer)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endReplaceGroup();
        }
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$132$lambda$130$lambda$129(TimerViewModel timerViewModel) {
        timerViewModel.setAddingTask(true);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$132$lambda$131(ThemeOption themeOption, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C730@36189L88:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1563276960, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:730)");
            }
            IconKt.Icon-ww6aTOc((ImageVector)AddKt.getAdd((Icons.Filled)Icons.Filled.INSTANCE), (String)"Add Task", null, (long)themeOption.getTextPrimary-0d7_KjU(), (Composer)composer, (int)48, (int)4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$137$lambda$134$lambda$133(TimerViewModel timerViewModel, TaskItem taskItem, TaskItem taskItem2) {
        Intrinsics.checkNotNullParameter((Object)taskItem2, (String)"it");
        timerViewModel.setTask(taskItem.getId(), taskItem.getName(), Long.valueOf(taskItem.getCategoryColor()));
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$137$lambda$136$lambda$135(TimerViewModel timerViewModel, TaskItem taskItem) {
        timerViewModel.deleteTask(taskItem);
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$139$lambda$138(MutableState mutableState) {
        MainActivityKt.HomeScreen_6a0pyJM$lambda$93((MutableState<Boolean>)mutableState, true);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$140(ThemeOption themeOption, RowScope rowScope, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)rowScope, (String)"$this$TextButton");
        ComposerKt.sourceInformation((Composer)composer, (String)"C756@37714L90:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1572464585, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:756)");
            }
            TextKt.Text--4IGK_g((String)"View all tasks \u2192", null, (long)themeOption.getTextSecondary-0d7_KjU(), (long)0L, null, (FontWeight)FontWeight.Companion.getBold(), null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131034);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$144$lambda$143$lambda$142(View view, MutableState mutableState) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        MainActivityKt.HomeScreen_6a0pyJM$lambda$98((MutableState<Boolean>)mutableState, true);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$151(State object, State object2, State state, MutableState mutableState, AnimatedVisibilityScope object3, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object3, (String)"$this$AnimatedVisibility");
        ComposerKt.sourceInformation((Composer)composer, (String)"C808@40689L37,804@40522L214:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-1686702922, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:804)");
        }
        object3 = MainActivityKt.HomeScreen_6a0pyJM$lambda$84((State<String>)object);
        object2 = MainActivityKt.HomeScreen_6a0pyJM$lambda$86((State<String>)object2);
        n = MainActivityKt.HomeScreen_6a0pyJM$lambda$85((State<Integer>)state);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1718831749, (String)"CC(remember):MainActivity.kt#9igjgp");
        object = composer.rememberedValue();
        if (object == Composer.Companion.getEmpty()) {
            object = new MainActivityKt$$ExternalSyntheticLambda40(mutableState);
            composer.updateRememberedValue(object);
        }
        object = (Function0)object;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        MainActivityKt.SeriousFullscreenOverlay((String)object3, (String)object2, n, (Function0<Unit>)object, composer, 3072);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$151$lambda$150$lambda$149(MutableState mutableState) {
        MainActivityKt.HomeScreen_6a0pyJM$lambda$98((MutableState<Boolean>)mutableState, false);
        return Unit.INSTANCE;
    }

    static final int HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$153$lambda$152(int n) {
        return n;
    }

    static final int HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$155$lambda$154(int n) {
        return n;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$158(TimerViewModel object, State state, AnimatedVisibilityScope object2, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$AnimatedVisibility");
        ComposerKt.sourceInformation((Composer)composer, (String)"C817@41252L36,817@41224L98:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)382343725, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:817)");
        }
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1934866351, (String)"CC(remember):MainActivity.kt#9igjgp");
        boolean bl = composer.changedInstance(object);
        object2 = composer.rememberedValue();
        if (!bl && object2 != Composer.Companion.getEmpty()) {
            object = object2;
        } else {
            object = new MainActivityKt$$ExternalSyntheticLambda51((TimerViewModel)object);
            composer.updateRememberedValue(object);
        }
        object = (Function0)object;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        MainActivityKt.SettingsOverlay((Function0<Unit>)object, MainActivityKt.HomeScreen_6a0pyJM$lambda$95((State<Integer>)state), composer, 0, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$158$lambda$157$lambda$156(TimerViewModel timerViewModel) {
        TimerViewModel.setSettingsOpen$default((TimerViewModel)timerViewModel, (boolean)false, (int)0, (int)2, null);
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$162$lambda$161(MutableState mutableState) {
        MainActivityKt.HomeScreen_6a0pyJM$lambda$101((MutableState<Boolean>)mutableState, false);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$172(ThemeOption themeOption, SystemProtectionStates object, Context context, TimerViewModel object2, MutableState mutableState, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C824@41600L2674:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-255882600, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous> (MainActivity.kt:824)");
            }
            Modifier modifier = PaddingKt.padding-3ABfNKs((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24)))), (long)themeOption.getSurface-0d7_KjU(), null, (int)2, null), (float)Dp.constructor-impl((float)24));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            Object object3 = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)Alignment.Companion.getTopStart(), (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            int n2 = (0 << 3 & 0x70) << 6 & 0x380 | 6;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)object3);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            object3 = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)424467913, (String)"C831@41852L2408:MainActivity.kt#to5c3");
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            object3 = (Modifier)Modifier.Companion;
            modifier = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            compositionLocalMap = composer.getCurrentCompositionLocalMap();
            object3 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object3);
            function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            modifier = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)modifier);
            }
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object3 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1319494542, (String)"C834@42004L1440,832@41896L1570,858@43487L41,860@43622L418,867@44123L119,859@43576L666:MainActivity.kt#to5c3");
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1150946012, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance((Object)context);
            boolean bl2 = composer.changedInstance(object2);
            object3 = composer.rememberedValue();
            if (!(bl | bl2) && object3 != Composer.Companion.getEmpty()) {
                object2 = object3;
            } else {
                object2 = new MainActivityKt$$ExternalSyntheticLambda62(context, (TimerViewModel)object2, mutableState);
                composer.updateRememberedValue(object2);
            }
            object2 = (Function1)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SystemProtectionStateKt.ContextualSetupMessage((SystemProtectionStates)object, (Function1)object2, (Composer)composer, (int)0);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1150996766, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl2 = composer.changedInstance((Object)context);
            object = composer.rememberedValue();
            if (bl2 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda63(context, mutableState);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ButtonKt.TextButton((Function0)object, (Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (boolean)false, null, null, null, null, null, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-1953007553, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda64(themeOption), (Composer)composer, (int)54)), (Composer)composer, (int)0x30000030, (int)508);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$172$lambda$171$lambda$170$lambda$165$lambda$164(Context context, TimerViewModel timerViewModel, MutableState mutableState, String string) {
        Intrinsics.checkNotNullParameter((Object)string, (String)"actionId");
        try {
            switch (string.hashCode()) {
                default: {
                    break;
                }
                case 386742765: {
                    if (!string.equals("BATTERY")) break;
                    string = new Intent("android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS");
                    context.startActivity((Intent)string);
                    break;
                }
                case 93629640: {
                    if (string.equals("NOTIFICATIONS")) {
                        string = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
                        string.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
                        context.startActivity((Intent)string);
                    }
                    break;
                }
                case 67834: {
                    if (!string.equals("DND")) break;
                    string = new Intent("android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS");
                    context.startActivity((Intent)string);
                    break;
                }
            }
        }
        catch (Exception exception) {
            timerViewModel.setSettingsOpen(true, 3);
        }
        MainActivityKt.HomeScreen_6a0pyJM$lambda$101((MutableState<Boolean>)mutableState, false);
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$172$lambda$171$lambda$170$lambda$168$lambda$167(Context context, MutableState mutableState) {
        MainActivityKt.HomeScreen_6a0pyJM$lambda$101((MutableState<Boolean>)mutableState, false);
        mutableState = new Intent(context, TimerService.class);
        mutableState.setAction("ACTION_START");
        context.startService((Intent)mutableState);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$172$lambda$171$lambda$170$lambda$169(ThemeOption themeOption, RowScope rowScope, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)rowScope, (String)"$this$TextButton");
        ComposerKt.sourceInformation((Composer)composer, (String)"C868@44149L71:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1953007553, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:868)");
            }
            TextKt.Text--4IGK_g((String)"Continue without protection", null, (long)themeOption.getTextSecondary-0d7_KjU(), (long)0L, null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$175$lambda$174(TimerViewModel timerViewModel, String string, String string2, long l) {
        Intrinsics.checkNotNullParameter((Object)string, (String)"name");
        Intrinsics.checkNotNullParameter((Object)string2, (String)"cat");
        timerViewModel.saveTask(string, string2, l, (Function1)new MainActivityKt$$ExternalSyntheticLambda36(timerViewModel, string, l));
        timerViewModel.setAddingTask(false);
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$175$lambda$174$lambda$173(TimerViewModel timerViewModel, String string, long l, int n) {
        timerViewModel.setTask(n, string, Long.valueOf(l));
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$177$lambda$176(TimerViewModel timerViewModel) {
        timerViewModel.setAddingTask(false);
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$179$lambda$178(MutableState mutableState) {
        MainActivityKt.HomeScreen_6a0pyJM$lambda$93((MutableState<Boolean>)mutableState, false);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit HomeScreen_6a0pyJM$lambda$187(ThemeOption object, State state, State state2, TimerViewModel timerViewModel, MutableState mutableState, ColumnScope columnScope, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)columnScope, (String)"$this$ModalBottomSheet");
        ComposerKt.sourceInformation((Composer)composer, (String)"C893@44925L1323:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1829454694, (int)n, (int)-1, (String)"com.example.HomeScreen.<anonymous> (MainActivity.kt:893)");
            }
            columnScope = PaddingKt.padding-VpY3zN4((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)Dp.constructor-impl((float)24), (float)Dp.constructor-impl((float)16));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            MeasurePolicy measurePolicy = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(6 >> 3 & 0xE | 6 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            columnScope = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)columnScope);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            measurePolicy = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)measurePolicy);
            }
            Updater.set-impl((Composer)function0, (Object)columnScope, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            columnScope = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1278916087, (String)"C900@45184L8,901@45227L13,898@45104L323,910@45668L566,906@45444L790:MainActivity.kt#to5c3");
            long l = ScaledSpKt.getScaledSp((int)24, (Composer)composer, (int)6);
            columnScope = TypographyKt.getAppFontFamily((Composer)composer, (int)0);
            measurePolicy = FontWeight.Companion.getBold();
            long l2 = object.getTextPrimary-0d7_KjU();
            object = PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)16), (int)7, null);
            TextKt.Text--4IGK_g((String)"All Tasks", (Modifier)object, (long)l2, (long)l, null, (FontWeight)measurePolicy, (FontFamily)columnScope, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196662, (int)0, (int)130960);
            object = Arrangement.INSTANCE.spacedBy-0680j_4(Dp.constructor-impl((float)12));
            measurePolicy = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            columnScope = PaddingKt.PaddingValues-a9UjIt4$default((float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)32), (int)7, null);
            compositionLocalMap = (Arrangement.Vertical)object;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-318332538, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changed((Object)state);
            boolean bl2 = composer.changed((Object)state2);
            boolean bl3 = composer.changedInstance((Object)timerViewModel);
            object = composer.rememberedValue();
            if (bl | bl2 | bl3 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda180(state, timerViewModel, state2, mutableState);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            LazyDslKt.LazyColumn((Modifier)measurePolicy, null, (PaddingValues)columnScope, (boolean)false, (Arrangement.Vertical)compositionLocalMap, null, null, (boolean)false, (Function1)object, (Composer)composer, (int)24966, (int)234);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184(State state, TimerViewModel timerViewModel, State state2, MutableState mutableState, LazyListScope lazyListScope) {
        Intrinsics.checkNotNullParameter((Object)lazyListScope, (String)"$this$LazyColumn");
        List<TaskItem> list = MainActivityKt.HomeScreen_6a0pyJM$lambda$89((State<? extends List<TaskItem>>)state);
        MainActivityKt$$ExternalSyntheticLambda4 mainActivityKt$$ExternalSyntheticLambda4 = new MainActivityKt$$ExternalSyntheticLambda4();
        state = (Function1)HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$$inlined$items$default$1.INSTANCE;
        int n = list.size();
        lazyListScope.items(n, (Function1)new HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$$inlined$items$default$2((Function1)mainActivityKt$$ExternalSyntheticLambda4, list), (Function1)new HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$$inlined$items$default$3((Function1)state, list), (Function4)ComposableLambdaKt.composableLambdaInstance((int)-632812321, (boolean)true, (Object)new HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$$inlined$items$default$4(list, timerViewModel, state2, mutableState)));
        return Unit.INSTANCE;
    }

    static final Object HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$lambda$180(TaskItem taskItem) {
        Intrinsics.checkNotNullParameter((Object)taskItem, (String)"it");
        return taskItem.getId();
    }

    static final Unit HomeScreen_6a0pyJM$lambda$188(TimerViewModel timerViewModel, NavController navController, float f, int n, int n2, Composer composer, int n3) {
        MainActivityKt.HomeScreen-6a0pyJM(timerViewModel, navController, f, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n | 1)), n2);
        return Unit.INSTANCE;
    }

    private static final TimerManager.TimerState HomeScreen_6a0pyJM$lambda$82(State<? extends TimerManager.TimerState> state) {
        return (TimerManager.TimerState)state.getValue();
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$83(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    private static final String HomeScreen_6a0pyJM$lambda$84(State<String> state) {
        return (String)state.getValue();
    }

    private static final int HomeScreen_6a0pyJM$lambda$85(State<Integer> state) {
        return ((Number)state.getValue()).intValue();
    }

    private static final String HomeScreen_6a0pyJM$lambda$86(State<String> state) {
        return (String)state.getValue();
    }

    private static final int HomeScreen_6a0pyJM$lambda$87(State<Integer> state) {
        return ((Number)state.getValue()).intValue();
    }

    private static final Long HomeScreen_6a0pyJM$lambda$88(State<Long> state) {
        return (Long)state.getValue();
    }

    private static final List<TaskItem> HomeScreen_6a0pyJM$lambda$89(State<? extends List<TaskItem>> state) {
        return (List)state.getValue();
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$90(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$92(MutableState<Boolean> mutableState) {
        return (Boolean)((State)mutableState).getValue();
    }

    private static final void HomeScreen_6a0pyJM$lambda$93(MutableState<Boolean> mutableState, boolean bl) {
        mutableState.setValue((Object)bl);
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$94(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    private static final int HomeScreen_6a0pyJM$lambda$95(State<Integer> state) {
        return ((Number)state.getValue()).intValue();
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$97(MutableState<Boolean> mutableState) {
        return (Boolean)((State)mutableState).getValue();
    }

    private static final void HomeScreen_6a0pyJM$lambda$98(MutableState<Boolean> mutableState, boolean bl) {
        mutableState.setValue((Object)bl);
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void PomoPalApp(@NotNull TimerViewModel timerViewModel, @Nullable Composer object, int n) {
        int n2;
        Intrinsics.checkNotNullParameter((Object)timerViewModel, (String)"viewModel");
        Composer composer = object.startRestartGroup(-1210837019);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(PomoPalApp)172@7182L7,174@7318L23,175@7385L30,177@7537L16,178@7597L16,179@7665L16,180@7734L16,181@7772L42,182@7869L7,184@7936L7,185@7986L32,186@8044L122,186@8023L143,228@9793L10745,403@20545L2972,225@9676L13841:MainActivity.kt#to5c3");
        int n3 = n2 = n;
        if ((n & 6) == 0) {
            n3 = composer.changedInstance((Object)timerViewModel) ? 4 : 2;
            n3 = n2 | n3;
        }
        if ((n3 & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            long l;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1210837019, (int)n3, (int)-1, (String)"com.example.PomoPalApp (MainActivity.kt:171)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Context context = (Context)object;
            object = SettingsManager.INSTANCE.getUserName() == null ? "signin" : "home";
            NavHostController navHostController = NavHostControllerKt.rememberNavController((Navigator[])new Navigator[0], (Composer)composer, (int)0);
            State state = NavHostControllerKt.currentBackStackEntryAsState((NavController)((NavController)navHostController), (Composer)composer, (int)0);
            Object object2 = MainActivityKt.PomoPalApp$lambda$0((State<NavBackStackEntry>)state);
            if (object2 == null || (object2 = object2.getDestination()) == null || (object2 = object2.getRoute()) == null) {
                object2 = object;
            }
            State state2 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.isAddingTask(), null, (Composer)composer, (int)0, (int)1);
            State state3 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.getTimerState(), null, (Composer)composer, (int)0, (int)1);
            State state4 = SnapshotStateKt.collectAsState((StateFlow)timerViewModel.isSettingsOpen(), null, (Composer)composer, (int)0, (int)1);
            SnapshotStateKt.collectAsState((StateFlow)timerViewModel.getSettingsTab(), null, (Composer)composer, (int)0, (int)1);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)321784591, (String)"CC(remember):MainActivity.kt#9igjgp");
            Object object3 = composer.rememberedValue();
            if (object3 == Composer.Companion.getEmpty()) {
                object3 = SnapshotStateKt.mutableStateOf$default(null, null, (int)2, null);
                composer.updateRememberedValue(object3);
            }
            MutableState mutableState = (MutableState)object3;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object3 = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalView();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object3 = composer.consume((CompositionLocal)object3);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            View view = (View)object3;
            object3 = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object3 = composer.consume((CompositionLocal)object3);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ThemeOption themeOption = (ThemeOption)object3;
            SystemProtectionStateKt.rememberSystemProtectionStates((Composer)composer, (int)0);
            Unit unit = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)321793375, (String)"CC(remember):MainActivity.kt#9igjgp");
            object3 = composer.rememberedValue();
            if (object3 == Composer.Companion.getEmpty()) {
                object3 = (Function2)new PomoPalApp.1.1(mutableState, null);
                composer.updateRememberedValue(object3);
            }
            object3 = (Function2)object3;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)unit, (Function2)object3, (Composer)composer, (int)6);
            if (MainActivityKt.PomoPalApp$lambda$6((MutableState<String>)mutableState) != null) {
                composer.startReplaceGroup(1385851770);
                ComposerKt.sourceInformation((Composer)composer, (String)"195@8249L20,198@8581L574,210@9185L374,196@8291L92,197@8404L147,194@8205L1459");
                l = Color.Companion.getTransparent-0d7_KjU();
                unit = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)321799833, (String)"CC(remember):MainActivity.kt#9igjgp");
                object3 = composer.rememberedValue();
                if (object3 == Composer.Companion.getEmpty()) {
                    object3 = new MainActivityKt$$ExternalSyntheticLambda172(mutableState);
                    composer.updateRememberedValue(object3);
                }
                object3 = (Function0)object3;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                context = (Function2)ComposableLambdaKt.rememberComposableLambda((int)-675414984, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda173(view, context, mutableState), (Composer)composer, (int)54);
                Function2 function2 = (Function2)ComposableLambdaKt.rememberComposableLambda((int)1222081782, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda174(view, mutableState, themeOption), (Composer)composer, (int)54);
                mutableState = (Function2)ComposableLambdaKt.rememberComposableLambda((int)-1175388748, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda175(themeOption), (Composer)composer, (int)54);
                themeOption = (Function2)ComposableLambdaKt.rememberComposableLambda((int)1920843283, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda176(themeOption), (Composer)composer, (int)54);
                unit = (Shape)unit;
                AndroidAlertDialog_androidKt.AlertDialog-Oix01E0((Function0)object3, (Function2)context, null, (Function2)function2, null, (Function2)mutableState, (Function2)themeOption, (Shape)unit, (long)l, (long)0L, (long)0L, (long)0L, (float)0.0f, null, (Composer)composer, (int)102435894, (int)0, (int)15892);
            } else {
                composer.startReplaceGroup(1377675613);
            }
            composer.endReplaceGroup();
            object3 = SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            l = Color.Companion.getTransparent-0d7_KjU();
            object2 = (Function2)ComposableLambdaKt.rememberComposableLambda((int)1378613994, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda177((String)object2, (String)object, state, state2, state3, state4, view, navHostController), (Composer)composer, (int)54);
            ScaffoldKt.Scaffold-TvnljyQ((Modifier)object3, null, (Function2)object2, null, null, (int)0, (long)l, (long)0L, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)238820404, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda178(navHostController, (String)object, timerViewModel), (Composer)composer, (int)54)), (Composer)composer, (int)806879622, (int)442);
            if (AlarmState.INSTANCE.isAlarmRinging()) {
                composer.startReplaceGroup(1401068151);
                ComposerKt.sourceInformation((Composer)composer, (String)"472@23584L182");
                MainActivityKt.AlarmRingingLockScreen(AlarmState.INSTANCE.getActiveAlarmLabel(), AlarmState.INSTANCE.getActiveAlarmSquatTarget(), composer, 0);
            } else {
                composer.startReplaceGroup(1377675613);
            }
            composer.endReplaceGroup();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda179(timerViewModel, n));
        }
    }

    private static final NavBackStackEntry PomoPalApp$lambda$0(State<NavBackStackEntry> state) {
        return (NavBackStackEntry)state.getValue();
    }

    private static final boolean PomoPalApp$lambda$1(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    static final Unit PomoPalApp$lambda$10$lambda$9(MutableState mutableState) {
        MainActivityKt.PomoPalApp$lambda$7((MutableState<String>)mutableState, null);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$13(View object, Context context, MutableState mutableState, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C199@8620L406,199@8599L542:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-675414984, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous> (MainActivity.kt:199)");
            }
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-533736274, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance(object);
            boolean bl2 = composer.changedInstance((Object)context);
            Object object2 = composer.rememberedValue();
            if (!(bl | bl2) && object2 != Composer.Companion.getEmpty()) {
                object = object2;
            } else {
                object = new MainActivityKt$$ExternalSyntheticLambda150((View)object, context, mutableState);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ButtonKt.TextButton((Function0)object, null, (boolean)false, null, null, null, null, null, null, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$1933997979$app(), (Composer)composer, (int)0x30000000, (int)510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$13$lambda$12$lambda$11(View view, Context context, MutableState mutableState) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)MainActivityKt.PomoPalApp$lambda$6((MutableState<String>)mutableState))));
        MainActivityKt.PomoPalApp$lambda$7((MutableState<String>)mutableState, null);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$17(View object, MutableState mutableState, ThemeOption themeOption, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C211@9224L230,216@9456L89,211@9203L342:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1222081782, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous> (MainActivity.kt:211)");
            }
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1894522172, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance(object);
            Object object2 = composer.rememberedValue();
            if (!bl && object2 != Composer.Companion.getEmpty()) {
                object = object2;
            } else {
                object = new MainActivityKt$$ExternalSyntheticLambda112((View)object, mutableState);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ButtonKt.TextButton((Function0)object, null, (boolean)false, null, null, null, null, null, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-463472551, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda113(themeOption), (Composer)composer, (int)54)), (Composer)composer, (int)0x30000000, (int)510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$17$lambda$15$lambda$14(View view, MutableState mutableState) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        MainActivityKt.PomoPalApp$lambda$7((MutableState<String>)mutableState, null);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$17$lambda$16(ThemeOption themeOption, RowScope rowScope, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)rowScope, (String)"$this$TextButton");
        ComposerKt.sourceInformation((Composer)composer, (String)"C217@9478L49:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-463472551, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous>.<anonymous> (MainActivity.kt:217)");
            }
            TextKt.Text--4IGK_g((String)"Later", null, (long)themeOption.getTextSecondary-0d7_KjU(), (long)0L, null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$18(ThemeOption themeOption, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C196@8293L88:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1175388748, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous> (MainActivity.kt:196)");
            }
            TextKt.Text--4IGK_g((String)"Update Available", null, (long)themeOption.getTextPrimary-0d7_KjU(), (long)0L, null, (FontWeight)FontWeight.Companion.getBold(), null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131034);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$19(ThemeOption themeOption, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C197@8406L143:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1920843283, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous> (MainActivity.kt:197)");
            }
            TextKt.Text--4IGK_g((String)"A new version of PomoPal is available! Please download it to enjoy the latest features and bug fixes.", null, (long)themeOption.getTextPrimary-0d7_KjU(), (long)0L, null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    private static final TimerManager.TimerState PomoPalApp$lambda$2(State<? extends TimerManager.TimerState> state) {
        return (TimerManager.TimerState)state.getValue();
    }

    private static final boolean PomoPalApp$lambda$3(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    private static final int PomoPalApp$lambda$4(State<Integer> state) {
        return ((Number)state.getValue()).intValue();
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$51(String string, String object, State state, State state2, State state3, State state4, View view, NavHostController navHostController, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C234@10249L10,237@10515L10,240@10723L14,242@10810L9718,231@10022L10506:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            Object object2;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1378613994, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous> (MainActivity.kt:230)");
            }
            n = !(Intrinsics.areEqual((Object)string, (Object)"signin") || Intrinsics.areEqual((Object)(object2 = (object2 = MainActivityKt.PomoPalApp$lambda$0((State<NavBackStackEntry>)state)) != null && (object2 = object2.getDestination()) != null ? object2.getRoute() : null), (Object)"signin") || MainActivityKt.PomoPalApp$lambda$0((State<NavBackStackEntry>)state) == null && Intrinsics.areEqual((Object)object, (Object)"signin")) ? 0 : 1;
            boolean bl = n == 0 && !MainActivityKt.PomoPalApp$lambda$1((State<Boolean>)state2) && MainActivityKt.PomoPalApp$lambda$2((State<? extends TimerManager.TimerState>)state3) == TimerManager.TimerState.STOPPED && !MainActivityKt.PomoPalApp$lambda$3((State<Boolean>)state4);
            state = (FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.75f, (float)200.0f, null, (int)4, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1998135668, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda135();
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            state = EnterExitTransitionKt.slideInVertically((FiniteAnimationSpec)state, (Function1)object).plus(EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)220, null, (int)4, null)), (float)0.0f, (int)2, null));
            state2 = (FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1998144180, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda136();
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = EnterExitTransitionKt.slideOutVertically((FiniteAnimationSpec)state2, (Function1)object).plus(EnterExitTransitionKt.fadeOut$default(null, (float)0.0f, (int)3, null));
            state2 = WindowInsetsPaddingKt.windowInsetsPadding((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (WindowInsets)WindowInsets_androidKt.getNavigationBars((WindowInsets.Companion)WindowInsets.Companion, (Composer)composer, (int)6));
            float f = Dp.constructor-impl((float)16);
            float f2 = Dp.constructor-impl((float)8);
            state2 = PaddingKt.padding-qDBjuR0$default((Modifier)state2, (float)0.0f, (float)f2, (float)0.0f, (float)f, (int)5, null);
            AnimatedVisibilityKt.AnimatedVisibility((boolean)bl, (Modifier)state2, (EnterTransition)state, (ExitTransition)object, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)629002178, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda137(string, view, navHostController), (Composer)composer, (int)54)), (Composer)composer, (int)196608, (int)16);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final int PomoPalApp$lambda$51$lambda$21$lambda$20(int n) {
        return n * 2;
    }

    static final int PomoPalApp$lambda$51$lambda$23$lambda$22(int n) {
        return n * 2;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$51$lambda$50(String object, View view, NavHostController navHostController, AnimatedVisibilityScope object2, Composer composer, int n) {
        long l;
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$AnimatedVisibility");
        ComposerKt.sourceInformation((Composer)composer, (String)"C243@10882L7,244@10932L32,245@11033L7,246@11057L9457:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)629002178, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous>.<anonymous> (MainActivity.kt:243)");
        }
        object2 = (CompositionLocal)ThemeKt.getLocalAppTheme();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
        object2 = composer.consume((CompositionLocal)object2);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ThemeOption themeOption = (ThemeOption)object2;
        SystemProtectionStateKt.rememberSystemProtectionStates((Composer)composer, (int)0);
        object2 = (CompositionLocal)TypographyKt.getLocalAppFont();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
        object2 = composer.consume((CompositionLocal)object2);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        FontFamily fontFamily = (FontFamily)object2;
        Modifier modifier = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
        object2 = Alignment.Companion.getCenter();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
        MeasurePolicy measurePolicy = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)object2, (boolean)false);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
        n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
        object2 = composer.getCurrentCompositionLocalMap();
        modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
        Function0 function0 = ComposeUiNode.Companion.getConstructor();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
        if (!(composer.getApplier() instanceof Applier)) {
            ComposablesKt.invalidApplier();
        }
        composer.startReusableNode();
        if (composer.getInserting()) {
            composer.createNode(function0);
        } else {
            composer.useNode();
        }
        function0 = Updater.constructor-impl((Composer)composer);
        Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
        Updater.set-impl((Composer)function0, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
        object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
        if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
            function0.updateRememberedValue((Object)n);
            function0.apply((Object)n, (Function2)object2);
        }
        Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
        object2 = (BoxScope)BoxScopeInstance.INSTANCE;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1171492231, (String)"C250@11214L9282:MainActivity.kt#to5c3");
        measurePolicy = AnimationModifierKt.animateContentSize$default((Modifier)PaddingKt.padding-VpY3zN4((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)ShadowKt.shadow-s4CzXII$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)32))), (boolean)false, (long)0L, (long)themeOption.getShadowColor-0d7_KjU(), (int)12, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)32)))), (long)themeOption.getSurface-0d7_KjU(), null, (int)2, null), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)12)), (FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null)), null, (int)2, null);
        object2 = (Arrangement.Horizontal)Arrangement.INSTANCE.spacedBy-0680j_4(Dp.constructor-impl((float)16));
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
        object2 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)object2, (Alignment.Vertical)Alignment.Companion.getTop(), (Composer)composer, (int)(48 >> 3 & 0xE | 48 >> 3 & 0x70));
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
        n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
        modifier = composer.getCurrentCompositionLocalMap();
        measurePolicy = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)measurePolicy);
        function0 = ComposeUiNode.Companion.getConstructor();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
        if (!(composer.getApplier() instanceof Applier)) {
            ComposablesKt.invalidApplier();
        }
        composer.startReusableNode();
        if (composer.getInserting()) {
            composer.createNode(function0);
        } else {
            composer.useNode();
        }
        function0 = Updater.constructor-impl((Composer)composer);
        Updater.set-impl((Composer)function0, (Object)object2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
        Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
        object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
        if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
            function0.updateRememberedValue((Object)n);
            function0.apply((Object)n, (Function2)object2);
        }
        Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetModifier());
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
        object2 = (RowScope)RowScopeInstance.INSTANCE;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-31041869, (String)"C261@12011L84,267@12432L39,267@12492L352,263@12145L1889,296@14147L87,302@14550L39,302@14610L358,298@14260L1916,331@16283L85,337@16682L39,337@16742L354,333@16394L1906,366@18413L87,372@18840L39,372@18900L358,368@18550L1924:MainActivity.kt#to5c3");
        measurePolicy = AnimationSpecKt.spring$default((float)0.5f, (float)200.0f, null, (int)4, null);
        boolean bl = Intrinsics.areEqual((Object)object, (Object)"home");
        float f = bl ? 1.0f : 0.0f;
        modifier = AnimateAsStateKt.animateFloatAsState((float)f, (AnimationSpec)((AnimationSpec)measurePolicy), (float)0.0f, null, null, (Composer)composer, (int)48, (int)28);
        object2 = ClipKt.clip((Modifier)((Modifier)Modifier.Companion), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24))));
        long l2 = bl ? themeOption.getPillActiveBg-0d7_KjU() : Color.Companion.getTransparent-0d7_KjU();
        function0 = BackgroundKt.background-bw27NRU$default((Modifier)object2, (long)l2, null, (int)2, null);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1386463021, (String)"CC(remember):MainActivity.kt#9igjgp");
        object2 = composer.rememberedValue();
        if (object2 == Composer.Companion.getEmpty()) {
            object2 = InteractionSourceKt.MutableInteractionSource();
            composer.updateRememberedValue(object2);
        }
        MutableInteractionSource mutableInteractionSource = (MutableInteractionSource)object2;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1386460788, (String)"CC(remember):MainActivity.kt#9igjgp");
        boolean bl2 = composer.changedInstance((Object)view);
        boolean bl3 = composer.changed(bl);
        boolean bl4 = composer.changedInstance((Object)navHostController);
        object2 = composer.rememberedValue();
        if (bl2 | bl3 | bl4 || object2 == Composer.Companion.getEmpty()) {
            object2 = new MainActivityKt$$ExternalSyntheticLambda125(view, bl, navHostController);
            composer.updateRememberedValue(object2);
        }
        object2 = (Function0)object2;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        function0 = PaddingKt.padding-VpY3zN4((Modifier)ClickableKt.clickable-O2vRcR0$default((Modifier)function0, (MutableInteractionSource)mutableInteractionSource, null, (boolean)false, null, null, (Function0)object2, (int)28, null), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)8));
        object2 = Alignment.Companion.getCenterVertically();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
        object2 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object2, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
        n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
        mutableInteractionSource = composer.getCurrentCompositionLocalMap();
        function0 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)function0);
        Function0 function02 = ComposeUiNode.Companion.getConstructor();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
        if (!(composer.getApplier() instanceof Applier)) {
            ComposablesKt.invalidApplier();
        }
        composer.startReusableNode();
        if (composer.getInserting()) {
            composer.createNode(function02);
        } else {
            composer.useNode();
        }
        function02 = Updater.constructor-impl((Composer)composer);
        Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
        Updater.set-impl((Composer)function02, (Object)mutableInteractionSource, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
        object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
        if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
            function02.updateRememberedValue((Object)n);
            function02.apply((Object)n, (Function2)object2);
        }
        Updater.set-impl((Composer)function02, (Object)function0, (Function2)ComposeUiNode.Companion.getSetModifier());
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
        object2 = (RowScope)RowScopeInstance.INSTANCE;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2039182310, (String)"C276@13055L349:MainActivity.kt#to5c3");
        function0 = HomeKt.getHome((Icons.Filled)Icons.Filled.INSTANCE);
        l2 = bl ? themeOption.getPrimary-0d7_KjU() : Color.copy-wmQWz5c$default((long)themeOption.getTextSecondary-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
        object2 = SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)24));
        IconKt.Icon-ww6aTOc((ImageVector)function0, (String)"Home", (Modifier)object2, (long)l2, (Composer)composer, (int)432, (int)0);
        if (MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$24((State<Float>)modifier) > 0.1f) {
            composer.startReplaceGroup(-2038796950);
            ComposerKt.sourceInformation((Composer)composer, (String)"283@13490L52,284@13575L403");
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)(Dp.constructor-impl((float)8) * MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$24((State<Float>)modifier)))), (Composer)composer, (int)0);
            l = themeOption.getPrimary-0d7_KjU();
            l2 = TextUnitKt.getSp((float)(MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$24((State<Float>)modifier) * 14.0f));
            object2 = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"Timer", null, (long)l, (long)l2, null, (FontWeight)object2, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)1, (int)0, null, null, (Composer)composer, (int)196614, (int)3072, (int)122770);
        } else {
            composer.startReplaceGroup(-2052163406);
        }
        composer.endReplaceGroup();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        composer.endNode();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        bl3 = Intrinsics.areEqual((Object)object, (Object)"history");
        f = bl3 ? 1.0f : 0.0f;
        modifier = AnimateAsStateKt.animateFloatAsState((float)f, (AnimationSpec)((AnimationSpec)measurePolicy), (float)0.0f, null, null, (Composer)composer, (int)48, (int)28);
        object2 = ClipKt.clip((Modifier)((Modifier)Modifier.Companion), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24))));
        l2 = bl3 ? themeOption.getPillActiveBg-0d7_KjU() : Color.Companion.getTransparent-0d7_KjU();
        function0 = BackgroundKt.background-bw27NRU$default((Modifier)object2, (long)l2, null, (int)2, null);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1386395245, (String)"CC(remember):MainActivity.kt#9igjgp");
        object2 = composer.rememberedValue();
        if (object2 == Composer.Companion.getEmpty()) {
            object2 = InteractionSourceKt.MutableInteractionSource();
            composer.updateRememberedValue(object2);
        }
        mutableInteractionSource = (MutableInteractionSource)object2;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1386393006, (String)"CC(remember):MainActivity.kt#9igjgp");
        bl = composer.changedInstance((Object)view);
        bl2 = composer.changed(bl3);
        bl4 = composer.changedInstance((Object)navHostController);
        object2 = composer.rememberedValue();
        if (bl | bl2 | bl4 || object2 == Composer.Companion.getEmpty()) {
            object2 = new MainActivityKt$$ExternalSyntheticLambda126(view, bl3, navHostController);
            composer.updateRememberedValue(object2);
        }
        object2 = (Function0)object2;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        object2 = PaddingKt.padding-VpY3zN4((Modifier)ClickableKt.clickable-O2vRcR0$default((Modifier)function0, (MutableInteractionSource)mutableInteractionSource, null, (boolean)false, null, null, (Function0)object2, (int)28, null), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)8));
        function0 = Alignment.Companion.getCenterVertically();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
        function0 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)function0, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
        n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
        mutableInteractionSource = composer.getCurrentCompositionLocalMap();
        object2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object2);
        function02 = ComposeUiNode.Companion.getConstructor();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
        if (!(composer.getApplier() instanceof Applier)) {
            ComposablesKt.invalidApplier();
        }
        composer.startReusableNode();
        if (composer.getInserting()) {
            composer.createNode(function02);
        } else {
            composer.useNode();
        }
        function02 = Updater.constructor-impl((Composer)composer);
        Updater.set-impl((Composer)function02, (Object)function0, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
        Updater.set-impl((Composer)function02, (Object)mutableInteractionSource, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
        function0 = ComposeUiNode.Companion.getSetCompositeKeyHash();
        if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
            function02.updateRememberedValue((Object)n);
            function02.apply((Object)n, (Function2)function0);
        }
        Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetModifier());
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
        object2 = (RowScope)RowScopeInstance.INSTANCE;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1784742095, (String)"C311@15179L356:MainActivity.kt#to5c3");
        object2 = ListKt.getList((Icons.Filled)Icons.INSTANCE.getDefault());
        l2 = bl3 ? themeOption.getPrimary-0d7_KjU() : Color.copy-wmQWz5c$default((long)themeOption.getTextSecondary-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
        function0 = SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)24));
        IconKt.Icon-ww6aTOc((ImageVector)object2, (String)"History", (Modifier)function0, (long)l2, (Composer)composer, (int)432, (int)0);
        if (MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$30((State<Float>)modifier) > 0.1f) {
            composer.startReplaceGroup(-1784347125);
            ComposerKt.sourceInformation((Composer)composer, (String)"318@15624L55,319@15712L408");
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)(Dp.constructor-impl((float)8) * MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$30((State<Float>)modifier)))), (Composer)composer, (int)0);
            l = themeOption.getPrimary-0d7_KjU();
            l2 = TextUnitKt.getSp((float)(MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$30((State<Float>)modifier) * 14.0f));
            object2 = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"History", null, (long)l, (long)l2, null, (FontWeight)object2, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)1, (int)0, null, null, (Composer)composer, (int)196614, (int)3072, (int)122770);
        } else {
            composer.startReplaceGroup(-1799830757);
        }
        composer.endReplaceGroup();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        composer.endNode();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        bl3 = Intrinsics.areEqual((Object)object, (Object)"alarm");
        f = bl3 ? 1.0f : 0.0f;
        modifier = AnimateAsStateKt.animateFloatAsState((float)f, (AnimationSpec)((AnimationSpec)measurePolicy), (float)0.0f, null, null, (Composer)composer, (int)48, (int)28);
        object2 = ClipKt.clip((Modifier)((Modifier)Modifier.Companion), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24))));
        l2 = bl3 ? themeOption.getPillActiveBg-0d7_KjU() : Color.Companion.getTransparent-0d7_KjU();
        function0 = BackgroundKt.background-bw27NRU$default((Modifier)object2, (long)l2, null, (int)2, null);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1386327021, (String)"CC(remember):MainActivity.kt#9igjgp");
        object2 = composer.rememberedValue();
        if (object2 == Composer.Companion.getEmpty()) {
            object2 = InteractionSourceKt.MutableInteractionSource();
            composer.updateRememberedValue(object2);
        }
        mutableInteractionSource = (MutableInteractionSource)object2;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1386324786, (String)"CC(remember):MainActivity.kt#9igjgp");
        bl2 = composer.changedInstance((Object)view);
        bl = composer.changed(bl3);
        bl4 = composer.changedInstance((Object)navHostController);
        object2 = composer.rememberedValue();
        if (bl2 | bl | bl4 || object2 == Composer.Companion.getEmpty()) {
            object2 = new MainActivityKt$$ExternalSyntheticLambda127(view, bl3, navHostController);
            composer.updateRememberedValue(object2);
        }
        object2 = (Function0)object2;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        object2 = PaddingKt.padding-VpY3zN4((Modifier)ClickableKt.clickable-O2vRcR0$default((Modifier)function0, (MutableInteractionSource)mutableInteractionSource, null, (boolean)false, null, null, (Function0)object2, (int)28, null), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)8));
        function0 = Alignment.Companion.getCenterVertically();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
        function0 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)function0, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
        n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
        mutableInteractionSource = composer.getCurrentCompositionLocalMap();
        object2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object2);
        function02 = ComposeUiNode.Companion.getConstructor();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
        if (!(composer.getApplier() instanceof Applier)) {
            ComposablesKt.invalidApplier();
        }
        composer.startReusableNode();
        if (composer.getInserting()) {
            composer.createNode(function02);
        } else {
            composer.useNode();
        }
        function02 = Updater.constructor-impl((Composer)composer);
        Updater.set-impl((Composer)function02, (Object)function0, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
        Updater.set-impl((Composer)function02, (Object)mutableInteractionSource, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
        function0 = ComposeUiNode.Companion.getSetCompositeKeyHash();
        if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
            function02.updateRememberedValue((Object)n);
            function02.apply((Object)n, (Function2)function0);
        }
        Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetModifier());
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
        object2 = (RowScope)RowScopeInstance.INSTANCE;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1860580780, (String)"C346@17307L360:MainActivity.kt#to5c3");
        function0 = NotificationsKt.getNotifications((Icons.Filled)Icons.Filled.INSTANCE);
        l2 = bl3 ? themeOption.getPrimary-0d7_KjU() : Color.copy-wmQWz5c$default((long)themeOption.getTextSecondary-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
        object2 = SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)24));
        IconKt.Icon-ww6aTOc((ImageVector)function0, (String)"Alarm", (Modifier)object2, (long)l2, (Composer)composer, (int)432, (int)0);
        if (MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$36((State<Float>)modifier) > 0.1f) {
            composer.startReplaceGroup(-1860183888);
            ComposerKt.sourceInformation((Composer)composer, (String)"353@17754L53,354@17840L404");
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)(Dp.constructor-impl((float)8) * MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$36((State<Float>)modifier)))), (Composer)composer, (int)0);
            l = themeOption.getPrimary-0d7_KjU();
            l2 = TextUnitKt.getSp((float)(MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$36((State<Float>)modifier) * 14.0f));
            object2 = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"Alarm", null, (long)l, (long)l2, null, (FontWeight)object2, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)1, (int)0, null, null, (Composer)composer, (int)196614, (int)3072, (int)122770);
        } else {
            composer.startReplaceGroup(-1877780294);
        }
        composer.endReplaceGroup();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        composer.endNode();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        bl2 = Intrinsics.areEqual((Object)object, (Object)"workout");
        f = bl2 ? 1.0f : 0.0f;
        object2 = AnimateAsStateKt.animateFloatAsState((float)f, (AnimationSpec)((AnimationSpec)measurePolicy), (float)0.0f, null, null, (Composer)composer, (int)48, (int)28);
        object = ClipKt.clip((Modifier)((Modifier)Modifier.Companion), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24))));
        l2 = bl2 ? themeOption.getPillActiveBg-0d7_KjU() : Color.Companion.getTransparent-0d7_KjU();
        measurePolicy = BackgroundKt.background-bw27NRU$default((Modifier)object, (long)l2, null, (int)2, null);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1386257965, (String)"CC(remember):MainActivity.kt#9igjgp");
        object = composer.rememberedValue();
        if (object == Composer.Companion.getEmpty()) {
            object = InteractionSourceKt.MutableInteractionSource();
            composer.updateRememberedValue(object);
        }
        modifier = (MutableInteractionSource)object;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1386255726, (String)"CC(remember):MainActivity.kt#9igjgp");
        bl = composer.changedInstance((Object)view);
        bl3 = composer.changed(bl2);
        bl4 = composer.changedInstance((Object)navHostController);
        object = composer.rememberedValue();
        if (bl | bl3 | bl4 || object == Composer.Companion.getEmpty()) {
            object = new MainActivityKt$$ExternalSyntheticLambda128(view, bl2, navHostController);
            composer.updateRememberedValue(object);
        }
        object = (Function0)object;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        navHostController = PaddingKt.padding-VpY3zN4((Modifier)ClickableKt.clickable-O2vRcR0$default((Modifier)measurePolicy, (MutableInteractionSource)modifier, null, (boolean)false, null, null, (Function0)object, (int)28, null), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)8));
        object = Alignment.Companion.getCenterVertically();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
        object = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
        n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
        view = composer.getCurrentCompositionLocalMap();
        navHostController = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)navHostController);
        measurePolicy = ComposeUiNode.Companion.getConstructor();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
        if (!(composer.getApplier() instanceof Applier)) {
            ComposablesKt.invalidApplier();
        }
        composer.startReusableNode();
        if (composer.getInserting()) {
            composer.createNode((Function0)measurePolicy);
        } else {
            composer.useNode();
        }
        measurePolicy = Updater.constructor-impl((Composer)composer);
        Updater.set-impl((Composer)measurePolicy, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
        Updater.set-impl((Composer)measurePolicy, (Object)view, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
        object = ComposeUiNode.Companion.getSetCompositeKeyHash();
        if (measurePolicy.getInserting() || !Intrinsics.areEqual((Object)measurePolicy.rememberedValue(), (Object)n)) {
            measurePolicy.updateRememberedValue((Object)n);
            measurePolicy.apply((Object)n, (Function2)object);
        }
        Updater.set-impl((Composer)measurePolicy, (Object)navHostController, (Function2)ComposeUiNode.Companion.getSetModifier());
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
        object = (RowScope)RowScopeInstance.INSTANCE;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1936418969, (String)"C381@19469L364:MainActivity.kt#to5c3");
        view = DirectionsRunKt.getDirectionsRun((Icons.Filled)Icons.Filled.INSTANCE);
        l2 = bl2 ? themeOption.getPrimary-0d7_KjU() : Color.copy-wmQWz5c$default((long)themeOption.getTextSecondary-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
        object = SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)24));
        IconKt.Icon-ww6aTOc((ImageVector)view, (String)"Workout", (Modifier)object, (long)l2, (Composer)composer, (int)432, (int)0);
        if (MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$42((State<Float>)object2) > 0.1f) {
            composer.startReplaceGroup(-1936016311);
            ComposerKt.sourceInformation((Composer)composer, (String)"388@19922L55,389@20010L408");
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)(Dp.constructor-impl((float)8) * MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$42((State<Float>)object2)))), (Composer)composer, (int)0);
            l = themeOption.getPrimary-0d7_KjU();
            l2 = TextUnitKt.getSp((float)(14.0f * MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$42((State<Float>)object2)));
            object = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"Workout", null, (long)l, (long)l2, null, (FontWeight)object, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)1, (int)0, null, null, (Composer)composer, (int)196614, (int)3072, (int)122770);
        } else {
            composer.startReplaceGroup(-1955763559);
        }
        composer.endReplaceGroup();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        composer.endNode();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        composer.endNode();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        composer.endNode();
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    private static final float PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$24(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$28$lambda$27(View view, boolean bl, NavHostController navHostController) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!bl) {
            navHostController.navigate("home", (Function1)new MainActivityKt$$ExternalSyntheticLambda47());
        }
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$28$lambda$27$lambda$26(NavOptionsBuilder navOptionsBuilder) {
        Intrinsics.checkNotNullParameter((Object)navOptionsBuilder, (String)"$this$navigate");
        NavOptionsBuilder.popUpTo$default((NavOptionsBuilder)navOptionsBuilder, (int)0, null, (int)2, null);
        return Unit.INSTANCE;
    }

    private static final float PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$30(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$34$lambda$33(View view, boolean bl, NavHostController navHostController) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!bl) {
            navHostController.navigate("history", (Function1)new MainActivityKt$$ExternalSyntheticLambda48());
        }
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$34$lambda$33$lambda$32(NavOptionsBuilder navOptionsBuilder) {
        Intrinsics.checkNotNullParameter((Object)navOptionsBuilder, (String)"$this$navigate");
        NavOptionsBuilder.popUpTo$default((NavOptionsBuilder)navOptionsBuilder, (int)0, null, (int)2, null);
        return Unit.INSTANCE;
    }

    private static final float PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$36(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$40$lambda$39(View view, boolean bl, NavHostController navHostController) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!bl) {
            navHostController.navigate("alarm", (Function1)new MainActivityKt$$ExternalSyntheticLambda171());
        }
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$40$lambda$39$lambda$38(NavOptionsBuilder navOptionsBuilder) {
        Intrinsics.checkNotNullParameter((Object)navOptionsBuilder, (String)"$this$navigate");
        NavOptionsBuilder.popUpTo$default((NavOptionsBuilder)navOptionsBuilder, (int)0, null, (int)2, null);
        return Unit.INSTANCE;
    }

    private static final float PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$42(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$46$lambda$45(View view, boolean bl, NavHostController navHostController) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!bl) {
            navHostController.navigate("workout", (Function1)new MainActivityKt$$ExternalSyntheticLambda79());
        }
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$46$lambda$45$lambda$44(NavOptionsBuilder navOptionsBuilder) {
        Intrinsics.checkNotNullParameter((Object)navOptionsBuilder, (String)"$this$navigate");
        NavOptionsBuilder.popUpTo$default((NavOptionsBuilder)navOptionsBuilder, (int)0, null, (int)2, null);
        return Unit.INSTANCE;
    }

    private static final String PomoPalApp$lambda$6(MutableState<String> mutableState) {
        return (String)((State)mutableState).getValue();
    }

    private static final void PomoPalApp$lambda$7(MutableState<String> mutableState, String string) {
        mutableState.setValue((Object)string);
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$79(NavHostController navHostController, String string, TimerViewModel object, PaddingValues paddingValues, Composer composer, int n) {
        int n2;
        Intrinsics.checkNotNullParameter((Object)paddingValues, (String)"paddingValues");
        ComposerKt.sourceInformation((Composer)composer, (String)"C408@20743L283,415@21057L97,418@21189L283,425@21506L97,428@21614L1897,404@20572L2939:MainActivity.kt#to5c3");
        int n3 = n2 = n;
        if ((n & 6) == 0) {
            n = composer.changed((Object)paddingValues) ? 4 : 2;
            n3 = n2 | n;
        }
        if ((n3 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)238820404, (int)n3, (int)-1, (String)"com.example.PomoPalApp.<anonymous> (MainActivity.kt:404)");
            }
            Modifier modifier = SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1873148175, (String)"CC(remember):MainActivity.kt#9igjgp");
            Object object2 = composer.rememberedValue();
            if (object2 == Composer.Companion.getEmpty()) {
                object2 = new MainActivityKt$$ExternalSyntheticLambda159();
                composer.updateRememberedValue(object2);
            }
            Function1 function1 = (Function1)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1873158037, (String)"CC(remember):MainActivity.kt#9igjgp");
            object2 = composer.rememberedValue();
            if (object2 == Composer.Companion.getEmpty()) {
                object2 = new MainActivityKt$$ExternalSyntheticLambda160();
                composer.updateRememberedValue(object2);
            }
            Function1 function12 = (Function1)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1873162447, (String)"CC(remember):MainActivity.kt#9igjgp");
            object2 = composer.rememberedValue();
            if (object2 == Composer.Companion.getEmpty()) {
                object2 = new MainActivityKt$$ExternalSyntheticLambda161();
                composer.updateRememberedValue(object2);
            }
            Function1 function13 = (Function1)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1873172405, (String)"CC(remember):MainActivity.kt#9igjgp");
            object2 = composer.rememberedValue();
            if (object2 == Composer.Companion.getEmpty()) {
                object2 = new MainActivityKt$$ExternalSyntheticLambda162();
                composer.updateRememberedValue(object2);
            }
            Function1 function14 = (Function1)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1873177661, (String)"CC(remember):MainActivity.kt#9igjgp");
            n = (n3 & 0xE) == 4 ? 1 : 0;
            int n4 = composer.changedInstance((Object)navHostController);
            int n5 = composer.changedInstance(object);
            object2 = composer.rememberedValue();
            if ((n | n4 | n5) == 0 && object2 != Composer.Companion.getEmpty()) {
                object = object2;
            } else {
                object = new MainActivityKt$$ExternalSyntheticLambda163(paddingValues, navHostController, (TimerViewModel)object);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            NavHostKt.NavHost((NavHostController)navHostController, (String)string, (Modifier)modifier, null, null, (Function1)function1, (Function1)function12, (Function1)function13, (Function1)function14, null, (Function1)object, (Composer)composer, (int)115016064, (int)0, (int)536);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final EnterTransition PomoPalApp$lambda$79$lambda$54$lambda$53(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$NavHost");
        return EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getLinearOutSlowInEasing(), (int)2, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.slideInVertically((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getLinearOutSlowInEasing(), (int)2, null)), (Function1)new MainActivityKt$$ExternalSyntheticLambda92()));
    }

    static final int PomoPalApp$lambda$79$lambda$54$lambda$53$lambda$52(int n) {
        return n / 12;
    }

    static final ExitTransition PomoPalApp$lambda$79$lambda$56$lambda$55(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$NavHost");
        return EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)350, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (float)0.0f, (int)2, null);
    }

    static final EnterTransition PomoPalApp$lambda$79$lambda$59$lambda$58(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$NavHost");
        return EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getLinearOutSlowInEasing(), (int)2, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.slideInVertically((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getLinearOutSlowInEasing(), (int)2, null)), (Function1)new MainActivityKt$$ExternalSyntheticLambda166()));
    }

    static final int PomoPalApp$lambda$79$lambda$59$lambda$58$lambda$57(int n) {
        return -n / 12;
    }

    static final ExitTransition PomoPalApp$lambda$79$lambda$61$lambda$60(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$NavHost");
        return EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)350, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (float)0.0f, (int)2, null);
    }

    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77(PaddingValues paddingValues, NavHostController navHostController, TimerViewModel timerViewModel, NavGraphBuilder navGraphBuilder) {
        Intrinsics.checkNotNullParameter((Object)navGraphBuilder, (String)"$this$NavHost");
        float f = paddingValues.calculateBottomPadding-D9Ej5fM();
        NavGraphBuilderKt.composable$default((NavGraphBuilder)navGraphBuilder, (String)"signin", null, null, null, (Function1)new MainActivityKt$$ExternalSyntheticLambda83(), null, (Function1)new MainActivityKt$$ExternalSyntheticLambda84(), null, (Function4)((Function4)ComposableLambdaKt.composableLambdaInstance((int)-2027003279, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda85(navHostController))), (int)174, null);
        NavGraphBuilderKt.composable$default((NavGraphBuilder)navGraphBuilder, (String)"home", null, null, (Function1)new MainActivityKt$$ExternalSyntheticLambda86(), null, null, null, null, (Function4)((Function4)ComposableLambdaKt.composableLambdaInstance((int)852308186, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda87(timerViewModel, navHostController, f))), (int)246, null);
        NavGraphBuilderKt.composable$default((NavGraphBuilder)navGraphBuilder, (String)"history", null, null, null, null, null, null, null, (Function4)((Function4)ComposableLambdaKt.composableLambdaInstance((int)-1419159559, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda88(timerViewModel, navHostController, f))), (int)254, null);
        NavGraphBuilderKt.composable$default((NavGraphBuilder)navGraphBuilder, (String)"alarm", null, null, null, null, null, null, null, (Function4)((Function4)ComposableLambdaKt.composableLambdaInstance((int)604339992, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda89(navHostController, f))), (int)254, null);
        NavGraphBuilderKt.composable$default((NavGraphBuilder)navGraphBuilder, (String)"workout", null, null, null, null, null, null, null, (Function4)((Function4)ComposableLambdaKt.composableLambdaInstance((int)-1667127753, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda90(f))), (int)254, null);
        NavGraphBuilderKt.composable$default((NavGraphBuilder)navGraphBuilder, (String)"calibration", null, null, null, null, null, null, null, (Function4)((Function4)ComposableLambdaKt.composableLambdaInstance((int)356371798, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda91(navHostController))), (int)254, null);
        return Unit.INSTANCE;
    }

    static final ExitTransition PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$62(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$composable");
        return EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getLinearOutSlowInEasing(), (int)2, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.scaleOut-L8ZKh-E$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (float)0.94f, (long)0L, (int)4, null));
    }

    static final ExitTransition PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$63(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$composable");
        return EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getLinearOutSlowInEasing(), (int)2, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.scaleOut-L8ZKh-E$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)400, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (float)0.94f, (long)0L, (int)4, null));
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68(NavHostController object, AnimatedContentScope object2, NavBackStackEntry navBackStackEntry, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$composable");
        Intrinsics.checkNotNullParameter((Object)navBackStackEntry, (String)"it");
        ComposerKt.sourceInformation((Composer)composer, (String)"C441@22360L157,441@22298L220:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-2027003279, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:441)");
        }
        navBackStackEntry = (NavController)object;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1965004462, (String)"CC(remember):MainActivity.kt#9igjgp");
        boolean bl = composer.changedInstance(object);
        object2 = composer.rememberedValue();
        if (!bl && object2 != Composer.Companion.getEmpty()) {
            object = object2;
        } else {
            object = new MainActivityKt$$ExternalSyntheticLambda182((NavHostController)object);
            composer.updateRememberedValue(object);
        }
        object = (Function0)object;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        SignInScreenKt.SignInScreen((NavController)navBackStackEntry, (Function0)object, (Composer)composer, (int)0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66(NavHostController navHostController) {
        navHostController.navigate("home", (Function1)new MainActivityKt$$ExternalSyntheticLambda181());
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66$lambda$65(NavOptionsBuilder navOptionsBuilder) {
        Intrinsics.checkNotNullParameter((Object)navOptionsBuilder, (String)"$this$navigate");
        navOptionsBuilder.popUpTo("signin", (Function1)new MainActivityKt$$ExternalSyntheticLambda106());
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66$lambda$65$lambda$64(PopUpToBuilder popUpToBuilder) {
        Intrinsics.checkNotNullParameter((Object)popUpToBuilder, (String)"$this$popUpTo");
        popUpToBuilder.setInclusive(true);
        return Unit.INSTANCE;
    }

    static final EnterTransition PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$69(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$composable");
        return EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween((int)550, (int)80, (Easing)EasingKt.getLinearOutSlowInEasing())), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.scaleIn-L8ZKh-E$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween((int)550, (int)80, (Easing)EasingKt.getLinearOutSlowInEasing())), (float)1.04f, (long)0L, (int)4, null));
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$70(TimerViewModel timerViewModel, NavHostController navHostController, float f, AnimatedContentScope animatedContentScope, NavBackStackEntry navBackStackEntry, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)animatedContentScope, (String)"$this$composable");
        Intrinsics.checkNotNullParameter((Object)navBackStackEntry, (String)"it");
        ComposerKt.sourceInformation((Composer)composer, (String)"C454@22910L51:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)852308186, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:454)");
        }
        MainActivityKt.HomeScreen-6a0pyJM(timerViewModel, (NavController)navHostController, f, composer, 0, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$71(TimerViewModel timerViewModel, NavHostController navHostController, float f, AnimatedContentScope animatedContentScope, NavBackStackEntry navBackStackEntry, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)animatedContentScope, (String)"$this$composable");
        Intrinsics.checkNotNullParameter((Object)navBackStackEntry, (String)"it");
        ComposerKt.sourceInformation((Composer)composer, (String)"C457@23043L54:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-1419159559, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:457)");
        }
        HistoryScreenKt.HistoryScreen-6a0pyJM((TimerViewModel)timerViewModel, (NavController)((NavController)navHostController), (float)f, (Composer)composer, (int)0, (int)0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$72(NavHostController navHostController, float f, AnimatedContentScope animatedContentScope, NavBackStackEntry navBackStackEntry, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)animatedContentScope, (String)"$this$composable");
        Intrinsics.checkNotNullParameter((Object)navBackStackEntry, (String)"it");
        ComposerKt.sourceInformation((Composer)composer, (String)"C460@23177L41:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)604339992, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:460)");
        }
        AlarmScreenKt.AlarmScreen-rAjV9yQ((NavController)((NavController)navHostController), (float)f, (Composer)composer, (int)0, (int)0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$73(float f, AnimatedContentScope animatedContentScope, NavBackStackEntry navBackStackEntry, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)animatedContentScope, (String)"$this$composable");
        Intrinsics.checkNotNullParameter((Object)navBackStackEntry, (String)"it");
        ComposerKt.sourceInformation((Composer)composer, (String)"C463@23300L28:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-1667127753, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:463)");
        }
        WorkoutScreenKt.WorkoutScreen-8Feqmps((float)f, (Composer)composer, (int)0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$76(NavHostController object, AnimatedContentScope object2, NavBackStackEntry navBackStackEntry, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$composable");
        Intrinsics.checkNotNullParameter((Object)navBackStackEntry, (String)"it");
        ComposerKt.sourceInformation((Composer)composer, (String)"C466@23454L32,466@23414L73:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)356371798, (int)n, (int)-1, (String)"com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:466)");
        }
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1124083434, (String)"CC(remember):MainActivity.kt#9igjgp");
        boolean bl = composer.changedInstance(object);
        object2 = composer.rememberedValue();
        if (!bl && object2 != Composer.Companion.getEmpty()) {
            object = object2;
        } else {
            object = new MainActivityKt$$ExternalSyntheticLambda170((NavHostController)object);
            composer.updateRememberedValue(object);
        }
        object = (Function0)object;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        SquatCalibrationScreenKt.SquatCalibrationScreen((Function0)object, null, (Composer)composer, (int)0, (int)2);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$76$lambda$75$lambda$74(NavHostController navHostController) {
        navHostController.popBackStack();
        return Unit.INSTANCE;
    }

    static final Unit PomoPalApp$lambda$80(TimerViewModel timerViewModel, int n, Composer composer, int n2) {
        MainActivityKt.PomoPalApp(timerViewModel, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n | 1)));
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void PremiumJumpingTextPreview(@NotNull String string, @Nullable Composer object, int n) {
        int n2;
        Intrinsics.checkNotNullParameter((Object)string, (String)"text");
        Composer composer = object.startRestartGroup(-481056772);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(PremiumJumpingTextPreview)1560@82297L7,1561@82363L7,1562@82413L32,1563@82450L2690:MainActivity.kt#to5c3");
        int n3 = n2 = n;
        if ((n & 6) == 0) {
            n3 = composer.changed((Object)string) ? 4 : 2;
            n3 = n2 | n3;
        }
        if ((n3 & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-481056772, (int)n3, (int)-1, (String)"com.example.PremiumJumpingTextPreview (MainActivity.kt:1559)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = (Context)object;
            object = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ThemeOption themeOption = (ThemeOption)object;
            SystemProtectionStates systemProtectionStates = SystemProtectionStateKt.rememberSystemProtectionStates((Composer)composer, (int)0);
            Modifier modifier = SizeKt.height-3ABfNKs((Modifier)PaddingKt.padding-VpY3zN4$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)0.0f, (float)Dp.constructor-impl((float)12), (int)1, null), (float)Dp.constructor-impl((float)44));
            Arrangement.Horizontal horizontal = (Arrangement.Horizontal)Arrangement.INSTANCE.getCenter();
            Alignment.Vertical vertical = Alignment.Companion.getCenterVertically();
            object = composer;
            ComposerKt.sourceInformationMarkerStart((Composer)object, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            Object object2 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)horizontal, (Alignment.Vertical)vertical, (Composer)object, (int)(438 >> 3 & 0xE | 438 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)object, (int)0);
            vertical = object.getCurrentCompositionLocalMap();
            horizontal = ComposedModifierKt.materializeModifier((Composer)object, (Modifier)modifier);
            Object object3 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(object.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            object.startReusableNode();
            if (object.getInserting()) {
                object.createNode(object3);
            } else {
                object.useNode();
            }
            object3 = Updater.constructor-impl((Composer)object);
            Updater.set-impl((Composer)object3, (Object)object2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object3, (Object)vertical, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object3.getInserting() || !Intrinsics.areEqual((Object)object3.rememberedValue(), (Object)n2)) {
                object3.updateRememberedValue((Object)n2);
                object3.apply((Object)n2, (Function2)object2);
            }
            Updater.set-impl((Composer)object3, (Object)horizontal, (Function2)ComposeUiNode.Companion.getSetModifier());
            boolean bl = false;
            ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            horizontal = RowScopeInstance.INSTANCE;
            n3 = 438 >> 6 & 0x70 | 6;
            horizontal = (RowScope)horizontal;
            ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-2009759670, (String)"C:MainActivity.kt#to5c3");
            n2 = ((CharSequence)string).length() == 0 ? 1 : 0;
            if (n2 != 0) {
                object.startReplaceGroup(-2009806295);
                ComposerKt.sourceInformation((Composer)object, (String)"1574@82815L8,1576@82925L13,1572@82735L265");
                long l = ScaledSpKt.getScaledSp((int)18, (Composer)object, (int)6);
                long l2 = Color.copy-wmQWz5c$default((long)themeOption.getTextSecondary-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
                modifier = TypographyKt.getAppFontFamily((Composer)object, (int)0);
                horizontal = FontWeight.Companion.getMedium();
                TextKt.Text--4IGK_g((String)"Type task name...", null, (long)l2, (long)l, null, (FontWeight)horizontal, (FontFamily)modifier, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)object, (int)196614, (int)0, (int)130962);
                object.endReplaceGroup();
                horizontal = object;
            } else {
                object.startReplaceGroup(-2009456956);
                ComposerKt.sourceInformation((Composer)object, (String)"*1581@83104L68,1582@83209L68,1583@83314L66,1585@83443L403,1585@83414L432,1594@83892L255,1594@83863L284,1600@84193L196,1600@84164L225,1609@84533L8,1610@84576L13,1614@84774L266,1607@84423L687");
                object2 = string;
                n2 = 0;
                int n4 = 0;
                horizontal = object;
                while (n4 < object2.length()) {
                    char c = object2.charAt(n4);
                    ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1465236265, (String)"CC(remember):MainActivity.kt#9igjgp");
                    boolean bl2 = object.changed(n2);
                    object3 = object.rememberedValue();
                    if (bl2 || object3 == Composer.Companion.getEmpty()) {
                        object3 = AnimatableKt.Animatable$default((float)-30.0f, (float)0.0f, (int)2, null);
                        object.updateRememberedValue(object3);
                    }
                    Animatable animatable = (Animatable)object3;
                    ComposerKt.sourceInformationMarkerEnd((Composer)object);
                    ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1465232905, (String)"CC(remember):MainActivity.kt#9igjgp");
                    bl2 = object.changed(n2);
                    object3 = object.rememberedValue();
                    if (bl2 || object3 == Composer.Companion.getEmpty()) {
                        object3 = AnimatableKt.Animatable$default((float)0.4f, (float)0.0f, (int)2, null);
                        object.updateRememberedValue(object3);
                    }
                    Animatable animatable2 = (Animatable)object3;
                    ComposerKt.sourceInformationMarkerEnd((Composer)object);
                    ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1465229547, (String)"CC(remember):MainActivity.kt#9igjgp");
                    bl2 = object.changed(n2);
                    object3 = object.rememberedValue();
                    if (bl2 || object3 == Composer.Companion.getEmpty()) {
                        object3 = AnimatableKt.Animatable$default((float)0.0f, (float)0.0f, (int)2, null);
                        object.updateRememberedValue(object3);
                    }
                    Animatable animatable3 = (Animatable)object3;
                    ComposerKt.sourceInformationMarkerEnd((Composer)object);
                    ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1465225082, (String)"CC(remember):MainActivity.kt#9igjgp");
                    bl2 = object.changedInstance((Object)animatable);
                    object3 = object.rememberedValue();
                    if (bl2 || object3 == Composer.Companion.getEmpty()) {
                        object3 = (Function2)new PremiumJumpingTextPreview.1.1.1.1(animatable, null);
                        object.updateRememberedValue(object3);
                    }
                    object3 = (Function2)object3;
                    ComposerKt.sourceInformationMarkerEnd((Composer)object);
                    EffectsKt.LaunchedEffect((Object)n2, (Function2)object3, (Composer)object, (int)0);
                    ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1465210862, (String)"CC(remember):MainActivity.kt#9igjgp");
                    bl2 = object.changedInstance((Object)animatable2);
                    object3 = object.rememberedValue();
                    if (bl2 || object3 == Composer.Companion.getEmpty()) {
                        object3 = (Function2)new PremiumJumpingTextPreview.1.1.2.1(animatable2, null);
                        object.updateRememberedValue(object3);
                    }
                    object3 = (Function2)object3;
                    ComposerKt.sourceInformationMarkerEnd((Composer)object);
                    EffectsKt.LaunchedEffect((Object)n2, (Function2)object3, (Composer)object, (int)0);
                    ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1465201289, (String)"CC(remember):MainActivity.kt#9igjgp");
                    bl2 = object.changedInstance((Object)animatable3);
                    object3 = object.rememberedValue();
                    if (bl2 || object3 == Composer.Companion.getEmpty()) {
                        object3 = (Function2)new PremiumJumpingTextPreview.1.1.3.1(animatable3, null);
                        object.updateRememberedValue(object3);
                    }
                    object3 = (Function2)object3;
                    ComposerKt.sourceInformationMarkerEnd((Composer)object);
                    EffectsKt.LaunchedEffect((Object)n2, (Function2)object3, (Composer)object, (int)0);
                    object3 = c == ' ' ? " " : String.valueOf(c);
                    long l = ScaledSpKt.getScaledSp((int)24, (Composer)object, (int)6);
                    FontFamily fontFamily = TypographyKt.getAppFontFamily((Composer)object, (int)0);
                    FontWeight fontWeight = FontWeight.Companion.getBold();
                    long l3 = themeOption.getTextPrimary-0d7_KjU();
                    Modifier modifier2 = (Modifier)Modifier.Companion;
                    ComposerKt.sourceInformationMarkerStart((Composer)object, (int)-1465182627, (String)"CC(remember):MainActivity.kt#9igjgp");
                    bl2 = object.changedInstance((Object)animatable);
                    boolean bl3 = object.changedInstance((Object)animatable2);
                    boolean bl4 = object.changedInstance((Object)animatable3);
                    Object object4 = object.rememberedValue();
                    if (bl2 | bl3 | bl4 || object4 == Composer.Companion.getEmpty()) {
                        object4 = new MainActivityKt$$ExternalSyntheticLambda77(animatable, animatable2, animatable3);
                        object.updateRememberedValue(object4);
                    }
                    object4 = (Function1)object4;
                    ComposerKt.sourceInformationMarkerEnd((Composer)object);
                    object4 = PaddingKt.padding-VpY3zN4$default((Modifier)GraphicsLayerModifierKt.graphicsLayer((Modifier)modifier2, (Function1)object4), (float)Dp.constructor-impl((float)1.0f), (float)0.0f, (int)2, null);
                    TextKt.Text--4IGK_g((String)object3, (Modifier)object4, (long)l3, (long)l, null, (FontWeight)fontWeight, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)object, (int)196608, (int)0, (int)130960);
                    ++n4;
                    ++n2;
                }
                object.endReplaceGroup();
            }
            ComposerKt.sourceInformationMarkerEnd((Composer)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)object);
            horizontal.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)horizontal);
            ComposerKt.sourceInformationMarkerEnd((Composer)horizontal);
            ComposerKt.sourceInformationMarkerEnd((Composer)horizontal);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda78(string, n));
        }
    }

    static final Unit PremiumJumpingTextPreview$lambda$330$lambda$329$lambda$328$lambda$327(Animatable animatable, Animatable animatable2, Animatable animatable3, GraphicsLayerScope graphicsLayerScope) {
        Intrinsics.checkNotNullParameter((Object)graphicsLayerScope, (String)"$this$graphicsLayer");
        graphicsLayerScope.setTranslationY(((Number)animatable.getValue()).floatValue());
        graphicsLayerScope.setScaleX(((Number)animatable2.getValue()).floatValue());
        graphicsLayerScope.setScaleY(((Number)animatable2.getValue()).floatValue());
        graphicsLayerScope.setAlpha(((Number)animatable3.getValue()).floatValue());
        return Unit.INSTANCE;
    }

    static final Unit PremiumJumpingTextPreview$lambda$331(String string, int n, Composer composer, int n2) {
        MainActivityKt.PremiumJumpingTextPreview(string, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n | 1)));
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void SeriousFullscreenOverlay(@NotNull String string, @NotNull String string2, int n, @NotNull Function0<Unit> function0, @Nullable Composer object, int n2) {
        int n3;
        Intrinsics.checkNotNullParameter((Object)string, (String)"quote");
        Intrinsics.checkNotNullParameter((Object)string2, (String)"taskName");
        Intrinsics.checkNotNullParameter(function0, (String)"onDismiss");
        Composer composer = object.startRestartGroup(1248933701);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(SeriousFullscreenOverlay)P(1,2,3)2358@118100L7,2360@118214L7,2363@118340L80,2363@118312L108,2368@118533L1692,2368@118510L1715,2409@120256L49,2411@120351L289,2421@120682L272,2436@121130L39,2438@121219L2,2439@121273L13440,2431@120960L13753:MainActivity.kt#to5c3");
        int n4 = n3 = n2;
        if ((n2 & 6) == 0) {
            n4 = composer.changed((Object)string) ? 4 : 2;
            n4 = n3 | n4;
        }
        n3 = n4;
        if ((n2 & 0x30) == 0) {
            n3 = composer.changed((Object)string2) ? 32 : 16;
            n3 = n4 | n3;
        }
        if ((n2 & 0x180) == 0) {
            n4 = composer.changed(n) ? 256 : 128;
            n4 = n3 | n4;
        } else {
            n4 = n3;
        }
        n3 = n4;
        if ((n2 & 0xC00) == 0) {
            n3 = composer.changedInstance(function0) ? 2048 : 1024;
            n3 = n4 | n3;
        }
        if ((n3 & 0x493) == 1170 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1248933701, (int)n3, (int)-1, (String)"com.example.SeriousFullscreenOverlay (MainActivity.kt:2357)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Context context = (Context)object;
            object = context instanceof Activity ? (Activity)context : null;
            Object object2 = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalView();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object2 = composer.consume(object2);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            View view = (View)object2;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1929425141, (String)"CC(remember):MainActivity.kt#9igjgp");
            object2 = composer.rememberedValue();
            if (object2 == Composer.Companion.getEmpty()) {
                object2 = new MainActivityKt$$ExternalSyntheticLambda101();
                composer.updateRememberedValue(object2);
            }
            object2 = (Function0)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            BackHandlerKt.BackHandler((boolean)true, (Function0)object2, (Composer)composer, (int)54, (int)0);
            Unit unit = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1929432929, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance(object);
            boolean bl2 = composer.changedInstance((Object)context);
            object2 = composer.rememberedValue();
            if (!(bl | bl2) && object2 != Composer.Companion.getEmpty()) {
                object = object2;
            } else {
                object = new MainActivityKt$$ExternalSyntheticLambda102((Activity)object, context);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.DisposableEffect((Object)unit, (Function1)object, (Composer)composer, (int)6);
            object2 = InfiniteTransitionKt.rememberInfiniteTransition((String)"seriousness", (Composer)composer, (int)6, (int)0);
            object = TransitionKt.animateColor-DTcfvLk((InfiniteTransition)object2, (long)ColorKt.Color((long)4280159758L), (long)ColorKt.Color((long)0xFF0F0505L), (InfiniteRepeatableSpec)AnimationSpecKt.infiniteRepeatable-9IiC70o$default((DurationBasedAnimationSpec)((DurationBasedAnimationSpec)AnimationSpecKt.tween$default((int)4000, (int)0, (Easing)EasingKt.getLinearEasing(), (int)2, null)), (RepeatMode)RepeatMode.Reverse, (long)0L, (int)4, null), (String)"glowColor", (Composer)composer, (int)(InfiniteTransition.$stable | 0x61B0 | InfiniteRepeatableSpec.$stable << 9), (int)0);
            context = InfiniteTransitionKt.animateFloat((InfiniteTransition)object2, (float)0.98f, (float)1.02f, (InfiniteRepeatableSpec)AnimationSpecKt.infiniteRepeatable-9IiC70o$default((DurationBasedAnimationSpec)((DurationBasedAnimationSpec)AnimationSpecKt.tween$default((int)4000, (int)0, (Easing)EasingKt.getFastOutSlowInEasing(), (int)2, null)), (RepeatMode)RepeatMode.Reverse, (long)0L, (int)4, null), (String)"scaleGlow", (Composer)composer, (int)(InfiniteTransition.$stable | 0x61B0 | InfiniteRepeatableSpec.$stable << 9), (int)0);
            unit = BackgroundKt.background-bw27NRU$default((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (long)MainActivityKt.SeriousFullscreenOverlay$lambda$454((State<Color>)object), null, (int)2, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1929514380, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = InteractionSourceKt.MutableInteractionSource();
                composer.updateRememberedValue(object);
            }
            MutableInteractionSource mutableInteractionSource = (MutableInteractionSource)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1929517191, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda103();
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            BoxWithConstraintsKt.BoxWithConstraints((Modifier)ClickableKt.clickable-O2vRcR0$default((Modifier)unit, (MutableInteractionSource)mutableInteractionSource, null, (boolean)false, null, null, (Function0)object, (int)28, null), null, (boolean)false, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-51656529, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda104(n, string2, string, (State)context, (InfiniteTransition)object2, view, function0), (Composer)composer, (int)54)), (Composer)composer, (int)3072, (int)6);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda105(string, string2, n, function0, n2));
        }
    }

    static final Unit SeriousFullscreenOverlay$lambda$450$lambda$449() {
        return Unit.INSTANCE;
    }

    static final DisposableEffectResult SeriousFullscreenOverlay$lambda$453$lambda$452(Activity activity, Context object, DisposableEffectScope disposableEffectScope) {
        Intrinsics.checkNotNullParameter((Object)disposableEffectScope, (String)"$this$DisposableEffect");
        if (activity != null) {
            try {
                activity.startLockTask();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        object = (object = object.getSystemService("notification")) instanceof NotificationManager ? (NotificationManager)object : null;
        Ref.IntRef intRef = new Ref.IntRef();
        intRef.element = -1;
        if (object != null) {
            try {
                if (object.isNotificationPolicyAccessGranted()) {
                    intRef.element = object.getCurrentInterruptionFilter();
                    object.setInterruptionFilter(3);
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        activity = (DisposableEffectResult)new SeriousFullscreenOverlay$lambda$453$lambda$452$$inlined$onDispose$1(activity, (NotificationManager)object, intRef);
        return activity;
    }

    private static final long SeriousFullscreenOverlay$lambda$454(State<Color> state) {
        return ((Color)state.getValue()).unbox-impl();
    }

    private static final float SeriousFullscreenOverlay$lambda$455(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit SeriousFullscreenOverlay$lambda$458$lambda$457() {
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SeriousFullscreenOverlay$lambda$479(int n, String object, String string, State state, InfiniteTransition infiniteTransition, View view, Function0 function0, BoxWithConstraintsScope boxWithConstraintsScope, Composer composer, int n2) {
        int n3;
        Intrinsics.checkNotNullParameter((Object)boxWithConstraintsScope, (String)"$this$BoxWithConstraints");
        ComposerKt.sourceInformation((Composer)composer, (String)"C2443@121377L13330:MainActivity.kt#to5c3");
        int n4 = n3 = n2;
        if ((n2 & 6) == 0) {
            n2 = composer.changed((Object)boxWithConstraintsScope) ? 4 : 2;
            n4 = n3 | n2;
        }
        if ((n4 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-51656529, (int)n4, (int)-1, (String)"com.example.SeriousFullscreenOverlay.<anonymous> (MainActivity.kt:2440)");
            }
            n2 = Dp.compareTo-0680j_4((float)boxWithConstraintsScope.getMaxHeight-D9Ej5fM(), (float)Dp.constructor-impl((float)640)) < 0 ? 1 : 0;
            boxWithConstraintsScope = WindowInsetsPadding_androidKt.navigationBarsPadding((Modifier)WindowInsetsPadding_androidKt.statusBarsPadding((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null)));
            float f = n2 != 0 ? Dp.constructor-impl((float)16) : Dp.constructor-impl((float)24);
            Object object2 = PaddingKt.padding-3ABfNKs((Modifier)boxWithConstraintsScope, (float)f);
            if (n2 != 0) {
                composer.startReplaceGroup(1897694900);
                ComposerKt.sourceInformation((Composer)composer, (String)"2449@121653L21");
                boxWithConstraintsScope = ScrollKt.verticalScroll$default((Modifier)((Modifier)Modifier.Companion), (ScrollState)ScrollKt.rememberScrollState((int)0, (Composer)composer, (int)0, (int)1), (boolean)false, null, (boolean)false, (int)14, null);
                composer.endReplaceGroup();
            } else {
                composer.startReplaceGroup(1897696247);
                composer.endReplaceGroup();
                boxWithConstraintsScope = (Modifier)Modifier.Companion;
            }
            object2 = object2.then((Modifier)boxWithConstraintsScope);
            boxWithConstraintsScope = Arrangement.INSTANCE;
            boxWithConstraintsScope = n2 != 0 ? boxWithConstraintsScope.getTop() : (Arrangement.Vertical)boxWithConstraintsScope.getSpaceBetween();
            Alignment.Horizontal horizontal = Alignment.Companion.getCenterHorizontally();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            horizontal = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)boxWithConstraintsScope, (Alignment.Horizontal)horizontal, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n3 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            boxWithConstraintsScope = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object2);
            object2 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)object2);
            } else {
                composer.useNode();
            }
            object2 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)object2, (Object)horizontal, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object2, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            horizontal = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object2.getInserting() || !Intrinsics.areEqual((Object)object2.rememberedValue(), (Object)n3)) {
                object2.updateRememberedValue((Object)n3);
                object2.apply((Object)n3, (Function2)horizontal);
            }
            Updater.set-impl((Composer)object2, (Object)boxWithConstraintsScope, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            boxWithConstraintsScope = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-328944274, (String)"C2454@121901L2206,2508@124176L2810,2571@127029L7668:MainActivity.kt#to5c3");
            object2 = Alignment.Companion.getCenterHorizontally();
            horizontal = PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)Dp.constructor-impl((float)24), (float)0.0f, (float)0.0f, (int)13, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            object2 = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)object2, (Composer)composer, (int)(390 >> 3 & 0xE | 390 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n3 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            compositionLocalMap = composer.getCurrentCompositionLocalMap();
            horizontal = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)horizontal);
            Function0 function02 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function02);
            } else {
                composer.useNode();
            }
            function02 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function02, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n3)) {
                function02.updateRememberedValue((Object)n3);
                function02.apply((Object)n3, (Function2)object2);
            }
            Updater.set-impl((Composer)function02, (Object)horizontal, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object2 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1747910677, (String)"C2458@122066L776,2485@123201L8,2488@123358L8,2482@123082L369,2492@123469L40,2501@123934L8,2498@123795L298:MainActivity.kt#to5c3");
            compositionLocalMap = Alignment.Companion.getCenterVertically();
            object2 = (Arrangement.Horizontal)Arrangement.INSTANCE.getCenter();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            horizontal = (Modifier)Modifier.Companion;
            object2 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)object2, (Alignment.Vertical)compositionLocalMap, (Composer)composer, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n3 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            compositionLocalMap = composer.getCurrentCompositionLocalMap();
            horizontal = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)horizontal);
            function02 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function02);
            } else {
                composer.useNode();
            }
            function02 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function02, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n3)) {
                function02.updateRememberedValue((Object)n3);
                function02.apply((Object)n3, (Function2)object2);
            }
            Updater.set-impl((Composer)function02, (Object)horizontal, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object2 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)841550078, (String)"C2462@122242L217,2468@122480L39,2472@122688L8,2474@122794L8,2469@122540L284:MainActivity.kt#to5c3");
            BoxKt.Box((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)10)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)ColorKt.Color((long)0xFFFF5252L), null, (int)2, null), (Composer)composer, (int)0);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)"ULTRA FOCUS ACTIVE", null, (long)ColorKt.Color((long)4294937216L), (long)ScaledSpKt.getScaledSp((int)13, (Composer)composer, (int)6), null, (FontWeight)FontWeight.Companion.getBold(), null, (long)ScaledSpKt.getScaledSp((int)2, (Composer)composer, (int)6), null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196998, (int)0, (int)130898);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object2 = String.format("%02d:%02d", Arrays.copyOf(new Object[]{n / 60, n % 60}, 2));
            Intrinsics.checkNotNullExpressionValue((Object)object2, (String)"format(...)");
            long l = Color.Companion.getWhite-0d7_KjU();
            long l2 = ScaledSpKt.getScaledSp((int)52, (Composer)composer, (int)6);
            function02 = FontWeight.Companion.getExtraBold();
            horizontal = TypographyKt.getMonospaceFontFamily();
            long l3 = ScaledSpKt.getScaledSp((int)2, (Composer)composer, (int)6);
            compositionLocalMap = PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)Dp.constructor-impl((float)12), (int)1, null);
            TextKt.Text--4IGK_g((String)object2, (Modifier)compositionLocalMap, (long)l, (long)l2, null, (FontWeight)function02, (FontFamily)horizontal, (long)l3, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)197040, (int)0, (int)130832);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)4)), (Composer)composer, (int)6);
            object = !StringsKt.isBlank((CharSequence)((CharSequence)object)) && !Intrinsics.areEqual((Object)object, (Object)"Focus Time!") && !Intrinsics.areEqual((Object)object, (Object)"Break Time!") ? "Working on: " + (String)object : "Your study blocks generate your empire.";
            TextKt.Text--4IGK_g((String)object, null, (long)Color.copy-wmQWz5c$default((long)Color.Companion.getWhite-0d7_KjU(), (float)0.6f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (long)ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6), null, (FontWeight)FontWeight.Companion.getMedium(), null, (long)0L, null, (TextAlign)TextAlign.box-impl((int)TextAlign.Companion.getCenter-e0LSkKk()), (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196992, (int)0, (int)130514);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object2 = Alignment.Companion.getCenterHorizontally();
            horizontal = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            object = n2 != 0 ? PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)Dp.constructor-impl((float)16), (int)1, null) : ColumnScope.weight$default((ColumnScope)boxWithConstraintsScope, (Modifier)((Modifier)Modifier.Companion), (float)1.0f, (boolean)false, (int)2, null);
            horizontal = horizontal.then((Modifier)object);
            object = Arrangement.INSTANCE.getCenter();
            object = (Arrangement.Vertical)object;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            boxWithConstraintsScope = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)object, (Alignment.Horizontal)object2, (Composer)composer, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object = composer.getCurrentCompositionLocalMap();
            object2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)horizontal);
            horizontal = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)horizontal);
            } else {
                composer.useNode();
            }
            horizontal = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)horizontal, (Object)boxWithConstraintsScope, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)horizontal, (Object)object, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (horizontal.getInserting() || !Intrinsics.areEqual((Object)horizontal.rememberedValue(), (Object)n2)) {
                horizontal.updateRememberedValue((Object)n2);
                horizontal.apply((Object)n2, (Function2)object);
            }
            Updater.set-impl((Composer)horizontal, (Object)object2, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-18658709, (String)"C2518@124633L8,2520@124743L13,2516@124560L273,2527@124988L127,2532@125249L776,2525@124893L1132,2549@126043L41,2555@126355L8,2558@126535L8,2559@126581L8,2552@126145L827:MainActivity.kt#to5c3");
            l2 = ScaledSpKt.getScaledSp((int)110, (Composer)composer, (int)6);
            l3 = Color.copy-wmQWz5c$default((long)ColorKt.Color((long)0xFFFF5252L), (float)0.12f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
            object = TypographyKt.getAppFontFamily((Composer)composer, (int)0);
            boxWithConstraintsScope = (Modifier)Modifier.Companion;
            boxWithConstraintsScope = OffsetKt.offset-VpY3zN4$default((Modifier)boxWithConstraintsScope, (float)0.0f, (float)Dp.constructor-impl((float)25), (int)1, null);
            TextKt.Text--4IGK_g((String)"\u201c", (Modifier)boxWithConstraintsScope, (long)l3, (long)l2, null, null, (FontFamily)object, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)438, (int)0, (int)130992);
            boxWithConstraintsScope = PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8), (float)0.0f, (int)2, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2077619499, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda131();
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            AnimatedContentKt.AnimatedContent((Object)string, (Modifier)boxWithConstraintsScope, (Function1)object, null, (String)"fullscreenQuote", null, (Function4)((Function4)ComposableLambdaKt.rememberComposableLambda((int)632281218, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda132(state), (Composer)composer, (int)54)), (Composer)composer, (int)1597872, (int)40);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)32)), (Composer)composer, (int)6);
            l2 = ColorKt.Color((long)4294937216L);
            l = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
            state = FontWeight.Companion.getBlack();
            n2 = TextAlign.Companion.getCenter-e0LSkKk();
            l3 = ScaledSpKt.getScaledSp((int)1, (Composer)composer, (int)6);
            long l4 = ScaledSpKt.getScaledSp((int)18, (Composer)composer, (int)6);
            object = PaddingKt.padding-VpY3zN4((Modifier)BackgroundKt.background-bw27NRU((Modifier)PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16), (float)0.0f, (int)2, null), (long)Color.copy-wmQWz5c$default((long)ColorKt.Color((long)0xFFFF5252L), (float)0.15f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)12)))), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)12));
            string = TextAlign.box-impl((int)n2);
            TextKt.Text--4IGK_g((String)"\ud83d\udea8 FOCUS NOW \u2022 STOP WATCHING THE TIMER. YOUR DREAMS REQUIRE DISCIPLINE, NOT DOPAMINE SWIPES.", (Modifier)object, (long)l2, (long)l, null, (FontWeight)state, null, (long)l3, null, (TextAlign)string, (long)l4, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196998, (int)0, (int)129360);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = Alignment.Companion.getCenterHorizontally();
            string = PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)24), (int)7, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            state = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)object, (Composer)composer, (int)(390 >> 3 & 0xE | 390 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object = composer.getCurrentCompositionLocalMap();
            string = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)string);
            boxWithConstraintsScope = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)boxWithConstraintsScope);
            } else {
                composer.useNode();
            }
            boxWithConstraintsScope = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)boxWithConstraintsScope, (Object)state, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)boxWithConstraintsScope, (Object)object, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (boxWithConstraintsScope.getInserting() || !Intrinsics.areEqual((Object)boxWithConstraintsScope.rememberedValue(), (Object)n2)) {
                boxWithConstraintsScope.updateRememberedValue((Object)n2);
                boxWithConstraintsScope.apply((Object)n2, (Function2)object);
            }
            Updater.set-impl((Composer)boxWithConstraintsScope, (Object)string, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2144761114, (String)"C:MainActivity.kt#to5c3");
            n3 = SettingsManager.INSTANCE.getFocusTimeMins() * 60;
            n2 = RangesKt.coerceAtLeast((int)(n3 - n), (int)0);
            n3 = (int)((float)n3 * 0.75f);
            n = n2 >= n3 ? 1 : 0;
            f = n3 > 0 ? RangesKt.coerceIn((float)((float)n2 / (float)n3), (float)0.0f, (float)1.0f) : 1.0f;
            if (n == 0) {
                composer.startReplaceGroup(2145173940);
                ComposerKt.sourceInformation((Composer)composer, (String)"2591@127989L3129");
                n2 = RangesKt.coerceAtLeast((int)(n3 - n2), (int)0);
                n = n2 / 60;
                string = Alignment.Companion.getCenterHorizontally();
                object = PaddingKt.padding-3ABfNKs((Modifier)BorderKt.border-xT4_qwU((Modifier)BackgroundKt.background-bw27NRU((Modifier)PaddingKt.padding-VpY3zN4$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)Dp.constructor-impl((float)16), (float)0.0f, (int)2, null), (long)ColorKt.Color((long)4281144340L), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16)))), (float)Dp.constructor-impl((float)1.0f), (long)Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4293227379L), (float)0.3f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16)))), (float)Dp.constructor-impl((float)16));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
                string = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)string, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n3 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                state = composer.getCurrentCompositionLocalMap();
                object = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object);
                infiniteTransition = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode((Function0)infiniteTransition);
                } else {
                    composer.useNode();
                }
                infiniteTransition = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)infiniteTransition, (Object)string, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)infiniteTransition, (Object)state, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                string = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (infiniteTransition.getInserting() || !Intrinsics.areEqual((Object)infiniteTransition.rememberedValue(), (Object)n3)) {
                    infiniteTransition.updateRememberedValue((Object)n3);
                    infiniteTransition.apply((Object)n3, (Function2)string);
                }
                Updater.set-impl((Composer)infiniteTransition, (Object)object, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
                object = (ColumnScope)ColumnScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-983232472, (String)"C2600@128503L951,2620@129504L41,2624@129724L18,2623@129660L450,2633@130160L41,2638@130487L8,2635@130251L330,2642@130631L40,2647@130937L8,2644@130721L375:MainActivity.kt#to5c3");
                string = Alignment.Companion.getCenterVertically();
                state = (Arrangement.Horizontal)Arrangement.INSTANCE.getCenter();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
                object = (Modifier)Modifier.Companion;
                state = RowKt.rowMeasurePolicy((Arrangement.Horizontal)state, (Alignment.Vertical)string, (Composer)composer, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n3 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                string = composer.getCurrentCompositionLocalMap();
                object = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object);
                infiniteTransition = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode((Function0)infiniteTransition);
                } else {
                    composer.useNode();
                }
                infiniteTransition = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)infiniteTransition, (Object)state, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)infiniteTransition, (Object)string, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                string = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (infiniteTransition.getInserting() || !Intrinsics.areEqual((Object)infiniteTransition.rememberedValue(), (Object)n3)) {
                    infiniteTransition.updateRememberedValue((Object)n3);
                    infiniteTransition.apply((Object)n3, (Function2)string);
                }
                Updater.set-impl((Composer)infiniteTransition, (Object)object, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
                object = (RowScope)RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-991312672, (String)"C2604@128711L285,2610@129025L39,2614@129268L8,2616@129390L8,2611@129093L335:MainActivity.kt#to5c3");
                object = LockKt.getLock((Icons.Filled)Icons.Filled.INSTANCE);
                l3 = ColorKt.Color((long)0xFFFF5252L);
                string = (Modifier)Modifier.Companion;
                string = SizeKt.size-3ABfNKs((Modifier)string, (float)Dp.constructor-impl((float)16));
                IconKt.Icon-ww6aTOc((ImageVector)object, (String)"Locked", (Modifier)string, (long)l3, (Composer)composer, (int)3504, (int)0);
                SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
                TextKt.Text--4IGK_g((String)"Emergency Exit Locked", null, (long)ColorKt.Color((long)4294954450L), (long)ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6), null, (FontWeight)FontWeight.Companion.getBold(), null, (long)ScaledSpKt.getScaledSp((int)1, (Composer)composer, (int)6), null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196998, (int)0, (int)130898);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)10)), (Composer)composer, (int)6);
                l3 = ColorKt.Color((long)0xFFFF5252L);
                l2 = Color.copy-wmQWz5c$default((long)ColorKt.Color((long)0xFFFF5252L), (float)0.15f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
                string = ClipKt.clip((Modifier)SizeKt.height-3ABfNKs((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)Dp.constructor-impl((float)6)), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)3))));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-447322676, (String)"CC(remember):MainActivity.kt#9igjgp");
                boolean bl = composer.changed(f);
                object = composer.rememberedValue();
                if (bl || object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda133(f);
                    composer.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ProgressIndicatorKt.LinearProgressIndicator-GJbTh5U((Function0)object, (Modifier)string, (long)l3, (long)l2, (int)0, (float)0.0f, null, (Composer)composer, (int)3456, (int)112);
                SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)10)), (Composer)composer, (int)6);
                n3 = (int)(100.0f * f);
                TextKt.Text--4IGK_g((String)("Unlocks at 75% focus block complete: " + n3 + "% completed"), null, (long)Color.copy-wmQWz5c$default((long)Color.Companion.getWhite-0d7_KjU(), (float)0.7f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (long)ScaledSpKt.getScaledSp((int)11, (Composer)composer, (int)6), null, (FontWeight)FontWeight.Companion.getMedium(), null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196992, (int)0, (int)131026);
                SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)4)), (Composer)composer, (int)6);
                object = n > 0 ? n + "m " : "";
                TextKt.Text--4IGK_g((String)("Focus for another " + (String)object + n2 % 60 + "s"), null, (long)ColorKt.Color((long)4294937216L), (long)ScaledSpKt.getScaledSp((int)13, (Composer)composer, (int)6), null, (FontWeight)FontWeight.Companion.getExtraBold(), (FontFamily)TypographyKt.getMonospaceFontFamily(), (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196992, (int)0, (int)130962);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endReplaceGroup();
            } else {
                composer.startReplaceGroup(-2146415038);
                ComposerKt.sourceInformation((Composer)composer, (String)"2654@131253L399,2664@131674L2991");
                object = AnimationSpecKt.infiniteRepeatable-9IiC70o$default((DurationBasedAnimationSpec)((DurationBasedAnimationSpec)AnimationSpecKt.tween$default((int)1500, (int)0, (Easing)EasingKt.getLinearEasing(), (int)2, null)), (RepeatMode)RepeatMode.Reverse, (long)0L, (int)4, null);
                n2 = InfiniteTransition.$stable;
                n = InfiniteRepeatableSpec.$stable;
                object = InfiniteTransitionKt.animateFloat((InfiniteTransition)infiniteTransition, (float)0.8f, (float)1.0f, (InfiniteRepeatableSpec)object, (String)"activeButtonAlpha", (Composer)composer, (int)(n2 | 0x61B0 | n << 9), (int)0);
                string = Alignment.Companion.getCenterHorizontally();
                state = PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8), (float)0.0f, (int)2, null);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
                string = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)string, (Composer)composer, (int)(390 >> 3 & 0xE | 390 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                infiniteTransition = composer.getCurrentCompositionLocalMap();
                state = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)state);
                boxWithConstraintsScope = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode((Function0)boxWithConstraintsScope);
                } else {
                    composer.useNode();
                }
                boxWithConstraintsScope = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)boxWithConstraintsScope, (Object)string, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)boxWithConstraintsScope, (Object)infiniteTransition, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                string = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (boxWithConstraintsScope.getInserting() || !Intrinsics.areEqual((Object)boxWithConstraintsScope.rememberedValue(), (Object)n)) {
                    boxWithConstraintsScope.updateRememberedValue((Object)n);
                    boxWithConstraintsScope.apply((Object)n, (Function2)string);
                }
                Updater.set-impl((Composer)boxWithConstraintsScope, (Object)state, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
                string = (ColumnScope)ColumnScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1820717020, (String)"C2668@131877L1240,2696@133484L67,2692@133213L217,2691@133167L1476:MainActivity.kt#to5c3");
                state = Alignment.Companion.getCenterVertically();
                infiniteTransition = Arrangement.INSTANCE.getCenter();
                string = PaddingKt.padding-VpY3zN4((Modifier)BorderKt.border-xT4_qwU((Modifier)BackgroundKt.background-bw27NRU((Modifier)PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)16), (int)7, null), (long)Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4279983648L), (float)0.2f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)12)))), (float)Dp.constructor-impl((float)1.0f), (long)Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4286695300L), (float)0.4f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)12)))), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)8));
                infiniteTransition = (Arrangement.Horizontal)infiniteTransition;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
                state = RowKt.rowMeasurePolicy((Arrangement.Horizontal)infiniteTransition, (Alignment.Vertical)state, (Composer)composer, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                infiniteTransition = composer.getCurrentCompositionLocalMap();
                string = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)string);
                boxWithConstraintsScope = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode((Function0)boxWithConstraintsScope);
                } else {
                    composer.useNode();
                }
                boxWithConstraintsScope = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)boxWithConstraintsScope, (Object)state, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)boxWithConstraintsScope, (Object)infiniteTransition, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                state = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (boxWithConstraintsScope.getInserting() || !Intrinsics.areEqual((Object)boxWithConstraintsScope.rememberedValue(), (Object)n)) {
                    boxWithConstraintsScope.updateRememberedValue((Object)n);
                    boxWithConstraintsScope.apply((Object)n, (Function2)state);
                }
                Updater.set-impl((Composer)boxWithConstraintsScope, (Object)string, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
                string = (RowScope)RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)382561020, (String)"C2679@132586L8,2677@132489L208,2685@132924L8,2687@133053L8,2682@132726L365:MainActivity.kt#to5c3");
                l3 = ScaledSpKt.getScaledSp((int)16, (Composer)composer, (int)6);
                string = PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)8), (float)0.0f, (int)11, null);
                TextKt.Text--4IGK_g((String)"\ud83d\udd13", (Modifier)string, (long)0L, (long)l3, null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)54, (int)0, (int)131060);
                TextKt.Text--4IGK_g((String)"Focus standards met! Exit is now authorized.", null, (long)ColorKt.Color((long)4286695300L), (long)ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6), null, (FontWeight)FontWeight.Companion.getExtraBold(), null, (long)ScaledSpKt.getScaledSp((double)0.5, (Composer)composer, (int)6), null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196998, (int)0, (int)130898);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                string = ButtonDefaults.INSTANCE.buttonColors-ro_MJ88(Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4281236786L), (float)0.2f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), 0L, 0L, 0L, composer, ButtonDefaults.$stable << 12 | 6, 14);
                boxWithConstraintsScope = BorderStrokeKt.BorderStroke-cXLIe8U((float)Dp.constructor-impl((float)((float)1.5)), (long)Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4286695300L), (float)MainActivityKt.SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$477$lambda$472((State<Float>)object), (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null));
                object2 = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24));
                state = SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)48));
                infiniteTransition = PaddingKt.PaddingValues-YgX7TsA$default((float)Dp.constructor-impl((float)24), (float)0.0f, (int)2, null);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1998355254, (String)"CC(remember):MainActivity.kt#9igjgp");
                boolean bl = composer.changedInstance((Object)view);
                boolean bl2 = composer.changed((Object)function0);
                object = composer.rememberedValue();
                if (bl | bl2 || object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda134(view, function0);
                    composer.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                view = (Shape)object2;
                ButtonKt.Button((Function0)object, (Modifier)state, (boolean)false, (Shape)view, (ButtonColors)string, null, (BorderStroke)boxWithConstraintsScope, (PaddingValues)infiniteTransition, null, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$-801647615$app(), (Composer)composer, (int)0x30C00030, (int)292);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endReplaceGroup();
            }
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final ContentTransform SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$467$lambda$462$lambda$461(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$AnimatedContent");
        return AnimatedContentKt.togetherWith((EnterTransition)EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)800, (int)0, null, (int)6, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.scaleIn-L8ZKh-E$default(null, (float)0.0f, (long)0L, (int)7, null)), (ExitTransition)EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)500, (int)0, null, (int)6, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.scaleOut-L8ZKh-E$default(null, (float)0.0f, (long)0L, (int)7, null)));
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$467$lambda$466(State object, AnimatedContentScope object2, String string, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$AnimatedContent");
        Intrinsics.checkNotNullParameter((Object)string, (String)"targetQuote");
        ComposerKt.sourceInformation((Composer)composer, (String)"C2536@125471L8,2538@125572L13,2541@125796L8,2542@125864L121,2533@125286L721:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)632281218, (int)n, (int)-1, (String)"com.example.SeriousFullscreenOverlay.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:2533)");
        }
        if ((n = (object2 = (CharSequence)string).length() == 0 ? 1 : 0) != 0) {
            object2 = "Stay present and fully engaged.";
        }
        String string2 = (String)object2;
        long l = ColorKt.Color((long)0xFFFFEBEEL);
        long l2 = ScaledSpKt.getScaledSp((int)24, (Composer)composer, (int)6);
        string = FontWeight.Companion.getBold();
        FontFamily fontFamily = TypographyKt.getAppFontFamily((Composer)composer, (int)0);
        n = FontStyle.Companion.getItalic-_-LCdwA();
        int n2 = TextAlign.Companion.getCenter-e0LSkKk();
        long l3 = ScaledSpKt.getScaledSp((int)36, (Composer)composer, (int)6);
        Modifier modifier = (Modifier)Modifier.Companion;
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2031881563, (String)"CC(remember):MainActivity.kt#9igjgp");
        boolean bl = composer.changed(object);
        object2 = composer.rememberedValue();
        if (!bl && object2 != Composer.Companion.getEmpty()) {
            object = object2;
        } else {
            object = new MainActivityKt$$ExternalSyntheticLambda42((State)object);
            composer.updateRememberedValue(object);
        }
        object = (Function1)object;
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        object2 = GraphicsLayerModifierKt.graphicsLayer((Modifier)modifier, (Function1)object);
        modifier = FontStyle.box-impl((int)n);
        object = TextAlign.box-impl((int)n2);
        TextKt.Text--4IGK_g((String)string2, (Modifier)object2, (long)l, (long)l2, (FontStyle)modifier, (FontWeight)string, (FontFamily)fontFamily, (long)0L, null, (TextAlign)object, (long)l3, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196992, (int)0, (int)129408);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$467$lambda$466$lambda$465$lambda$464(State state, GraphicsLayerScope graphicsLayerScope) {
        Intrinsics.checkNotNullParameter((Object)graphicsLayerScope, (String)"$this$graphicsLayer");
        graphicsLayerScope.setScaleX(MainActivityKt.SeriousFullscreenOverlay$lambda$455((State<Float>)state));
        graphicsLayerScope.setScaleY(MainActivityKt.SeriousFullscreenOverlay$lambda$455((State<Float>)state));
        return Unit.INSTANCE;
    }

    static final float SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$477$lambda$471$lambda$470$lambda$469(float f) {
        return f;
    }

    private static final float SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$477$lambda$472(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    static final Unit SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$477$lambda$476$lambda$475$lambda$474(View view, Function0 function0) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        function0.invoke();
        return Unit.INSTANCE;
    }

    static final Unit SeriousFullscreenOverlay$lambda$480(String string, String string2, int n, Function0 function0, int n2, Composer composer, int n3) {
        MainActivityKt.SeriousFullscreenOverlay(string, string2, n, (Function0<Unit>)function0, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n2 | 1)));
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void SettingsOverlay(@NotNull Function0<Unit> function0, int n, @Nullable Composer object, int n2, int n3) {
        int n4;
        int n5;
        Intrinsics.checkNotNullParameter(function0, (String)"onDismiss");
        Composer composer = object.startRestartGroup(1046193317);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(SettingsOverlay)P(1)930@46406L7,931@46468L7,932@46491L86,934@46658L74,934@46641L91,937@46756L38,938@46873L25,938@46856L42,940@46931L135,940@46904L162,951@47218L73,952@47314L73,953@47415L65,954@47516L81,958@47846L108,960@48045L271,960@47964L352,967@48481L479,967@48373L587,980@49024L485,980@48996L513,992@49567L84,996@49690L103,996@49657L136,1001@49811L24,1002@49873L7,1006@50041L1268,1040@51316L26193,1003@49885L27624:MainActivity.kt#to5c3");
        int n6 = n5 = n2;
        if ((n2 & 6) == 0) {
            n6 = composer.changedInstance(function0) ? 4 : 2;
            n6 = n5 | n6;
        }
        if ((n4 = n3 & 2) != 0) {
            n6 |= 0x30;
        } else if ((n2 & 0x30) == 0) {
            n5 = composer.changed(n) ? 32 : 16;
            n6 |= n5;
        }
        if ((n6 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (n4 != 0) {
                n = 0;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1046193317, (int)n6, (int)-1, (String)"com.example.SettingsOverlay (MainActivity.kt:929)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Context context = (Context)object;
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalView();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            View view = (View)object;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369437595, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = CollectionsKt.listOf((Object[])new String[]{"Timer & Sound", "Theme & Font", "App Blocker", "System", "About"});
                composer.updateRememberedValue(object);
            }
            List list = (List)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Object[] objectArray = new Object[]{};
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369442927, (String)"CC(remember):MainActivity.kt#9igjgp");
            n5 = (n6 & 0x70) == 32 ? 1 : 0;
            int n7 = composer.changedInstance((Object)list);
            object = composer.rememberedValue();
            if ((n5 | n7) != 0 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda141(n, list);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            objectArray = (Object[])RememberSaveableKt.rememberSaveable((Object[])objectArray, null, null, (Function0)object, (Composer)composer, (int)0, (int)6);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369446027, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateListOf();
                composer.updateRememberedValue(object);
            }
            SnapshotStateList snapshotStateList = (SnapshotStateList)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369449758, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda142();
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            MutableState mutableState = (MutableState)RememberSaveableKt.rememberSaveable((Object[])new Object[0], null, null, (Function0)object, (Composer)composer, (int)0, (int)6);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369451724, (String)"CC(remember):MainActivity.kt#9igjgp");
            int n8 = composer.changedInstance((Object)list);
            n5 = (n6 & 0x70) == 32 ? 1 : 0;
            int n9 = composer.changed((Object)objectArray);
            object = composer.rememberedValue();
            if ((n8 | n5 | n9) != 0 || object == Composer.Companion.getEmpty()) {
                object = (Function2)new SettingsOverlay.1.1(n, list, (MutableIntState)objectArray, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)n, (Function2)object, (Composer)composer, (int)(n6 >> 3 & 0xE));
            if (MainActivityKt.SettingsOverlay$lambda$197((MutableState<Boolean>)mutableState)) {
                composer.startReplaceGroup(-1431738792);
                ComposerKt.sourceInformation((Composer)composer, (String)"947@47145L27,947@47118L55");
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369458464, (String)"CC(remember):MainActivity.kt#9igjgp");
                boolean bl = composer.changed((Object)mutableState);
                object = composer.rememberedValue();
                if (bl || object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda143(mutableState);
                    composer.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                BlockedAppsScreenKt.BlockedAppsScreen((Function0)object, (Composer)composer, (int)0);
                composer.endReplaceGroup();
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
                if ((object = composer.endRestartGroup()) != null) {
                    object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda144(function0, n, n2, n3));
                }
                return;
            }
            composer.startReplaceGroup(-1478459171);
            composer.endReplaceGroup();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369460846, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)Float.valueOf(SettingsManager.INSTANCE.getFocusTimeMins()), null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState2 = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369463918, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)Float.valueOf(SettingsManager.INSTANCE.getBreakTimeMins()), null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState3 = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369467142, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)SettingsManager.INSTANCE.getCompletionSound(), null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState4 = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369470390, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)Float.valueOf(SettingsManager.INSTANCE.getCompletionDurationSec()), null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState5 = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = context.getSystemService("audio");
            Intrinsics.checkNotNull((Object)object, (String)"null cannot be cast to non-null type android.media.AudioManager");
            AudioManager audioManager = (AudioManager)object;
            float f = audioManager.getStreamMaxVolume(3);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369480977, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)Float.valueOf(audioManager.getStreamVolume(3)), null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState6 = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            float f2 = MainActivityKt.SettingsOverlay$lambda$204((MutableState<Float>)mutableState2);
            float f3 = MainActivityKt.SettingsOverlay$lambda$207((MutableState<Float>)mutableState3);
            n5 = MainActivityKt.SettingsOverlay$lambda$210((MutableState<Integer>)mutableState4);
            float f4 = MainActivityKt.SettingsOverlay$lambda$213((MutableState<Float>)mutableState5);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369487508, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = (Function2)new SettingsOverlay.4.1(mutableState2, mutableState3, mutableState4, mutableState5, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object[])new Object[]{Float.valueOf(f2), Float.valueOf(f3), n5, Float.valueOf(f4)}, (Function2)object, (Composer)composer, (int)0);
            ActivityResultContract activityResultContract = (ActivityResultContract)new ActivityResultContracts.OpenDocument();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369501668, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance((Object)context);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda145(context, mutableState4);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            activityResultContract = ActivityResultRegistryKt.rememberLauncherForActivityResult((ActivityResultContract)activityResultContract, (Function1)object, (Composer)composer, (int)0);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369519050, (String)"CC(remember):MainActivity.kt#9igjgp");
            int n10 = composer.changedInstance((Object)view);
            int n11 = composer.changed((Object)objectArray);
            n6 = (n6 & 0xE) == 4 ? 1 : 0;
            object = composer.rememberedValue();
            if ((n10 | n11 | n6) != 0 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda146(snapshotStateList, view, function0, (MutableIntState)objectArray);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            BackHandlerKt.BackHandler((boolean)true, (Function0)object, (Composer)composer, (int)6, (int)0);
            LazyListState lazyListState = LazyListStateKt.rememberLazyListState((int)MainActivityKt.SettingsOverlay$lambda$192((MutableIntState)objectArray), (int)0, (Composer)composer, (int)0, (int)2);
            n6 = MainActivityKt.SettingsOverlay$lambda$192((MutableIntState)objectArray);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)369539980, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl2 = composer.changed((Object)lazyListState);
            boolean bl3 = composer.changed((Object)objectArray);
            object = composer.rememberedValue();
            if (bl2 | bl3 || object == Composer.Companion.getEmpty()) {
                object = (Function2)new SettingsOverlay.6.1(lazyListState, (MutableIntState)objectArray, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)n6, (Function2)object, (Composer)composer, (int)0);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)773894976, (String)"CC(rememberCoroutineScope)482@20332L144:Effects.kt#9igjgp");
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-954367824, (String)"CC(remember):Effects.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new CompositionScopedCoroutineScopeCanceller(EffectsKt.createCompositionCoroutineScope((CoroutineContext)((CoroutineContext)EmptyCoroutineContext.INSTANCE), (Composer)composer));
                composer.updateRememberedValue(object);
            }
            object = (CompositionScopedCoroutineScopeCanceller)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object.getCoroutineScope();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ThemeOption themeOption = (ThemeOption)object;
            object = BackgroundKt.background-bw27NRU$default((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (long)themeOption.getBackground-0d7_KjU(), null, (int)2, null);
            long l = themeOption.getBackground-0d7_KjU();
            themeOption = (Function2)ComposableLambdaKt.rememberComposableLambda((int)-1910115223, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda147(themeOption, view, function0), (Composer)composer, (int)54);
            ScaffoldKt.Scaffold-TvnljyQ((Modifier)object, (Function2)themeOption, null, null, null, (int)0, (long)l, (long)0L, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)1568336244, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda148(lazyListState, list, (MutableIntState)objectArray, view, snapshotStateList, context, mutableState, mutableState2, mutableState3, audioManager, f, mutableState6, (ManagedActivityResultLauncher)activityResultContract, mutableState4, mutableState5), (Composer)composer, (int)54)), (Composer)composer, (int)0x30000030, (int)444);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda149(function0, n, n2, n3));
        }
    }

    static final MutableIntState SettingsOverlay$lambda$191$lambda$190(int n, List list) {
        return SnapshotIntStateKt.mutableIntStateOf((int)RangesKt.coerceIn((int)n, (int)0, (int)(list.size() - 1)));
    }

    private static final int SettingsOverlay$lambda$192(MutableIntState mutableIntState) {
        return ((IntState)mutableIntState).getIntValue();
    }

    private static final void SettingsOverlay$lambda$193(MutableIntState mutableIntState, int n) {
        mutableIntState.setIntValue(n);
    }

    static final MutableState SettingsOverlay$lambda$196$lambda$195() {
        return SnapshotStateKt.mutableStateOf$default((Object)false, null, (int)2, null);
    }

    private static final boolean SettingsOverlay$lambda$197(MutableState<Boolean> mutableState) {
        return (Boolean)((State)mutableState).getValue();
    }

    private static final void SettingsOverlay$lambda$198(MutableState<Boolean> mutableState, boolean bl) {
        mutableState.setValue((Object)bl);
    }

    static final Unit SettingsOverlay$lambda$201$lambda$200(MutableState mutableState) {
        MainActivityKt.SettingsOverlay$lambda$198((MutableState<Boolean>)mutableState, false);
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$202(Function0 function0, int n, int n2, int n3, Composer composer, int n4) {
        MainActivityKt.SettingsOverlay((Function0<Unit>)function0, n, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n2 | 1)), n3);
        return Unit.INSTANCE;
    }

    private static final float SettingsOverlay$lambda$204(MutableState<Float> mutableState) {
        return ((Number)((State)mutableState).getValue()).floatValue();
    }

    private static final void SettingsOverlay$lambda$205(MutableState<Float> mutableState, float f) {
        mutableState.setValue((Object)Float.valueOf(f));
    }

    private static final float SettingsOverlay$lambda$207(MutableState<Float> mutableState) {
        return ((Number)((State)mutableState).getValue()).floatValue();
    }

    private static final void SettingsOverlay$lambda$208(MutableState<Float> mutableState, float f) {
        mutableState.setValue((Object)Float.valueOf(f));
    }

    private static final int SettingsOverlay$lambda$210(MutableState<Integer> mutableState) {
        return ((Number)((State)mutableState).getValue()).intValue();
    }

    private static final void SettingsOverlay$lambda$211(MutableState<Integer> mutableState, int n) {
        mutableState.setValue((Object)n);
    }

    private static final float SettingsOverlay$lambda$213(MutableState<Float> mutableState) {
        return ((Number)((State)mutableState).getValue()).floatValue();
    }

    private static final void SettingsOverlay$lambda$214(MutableState<Float> mutableState, float f) {
        mutableState.setValue((Object)Float.valueOf(f));
    }

    private static final float SettingsOverlay$lambda$216(MutableState<Float> mutableState) {
        return ((Number)((State)mutableState).getValue()).floatValue();
    }

    private static final void SettingsOverlay$lambda$217(MutableState<Float> mutableState, float f) {
        mutableState.setValue((Object)Float.valueOf(f));
    }

    static final Unit SettingsOverlay$lambda$220$lambda$219(Context context, MutableState mutableState, Uri uri) {
        if (uri != null) {
            try {
                context.getContentResolver().takePersistableUriPermission(uri, 1);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            SettingsManager.INSTANCE.setCustomCompletionUri(uri.toString());
            MainActivityKt.SettingsOverlay$lambda$211((MutableState<Integer>)mutableState, 3);
            SoundPlayer.INSTANCE.playCompletion(context, MainActivityKt.SettingsOverlay$lambda$210((MutableState<Integer>)mutableState));
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$222$lambda$221(SnapshotStateList object, View view, Function0 function0, MutableIntState mutableIntState) {
        if (!((Collection)object).isEmpty()) {
            object = object.removeLast();
            Intrinsics.checkNotNullExpressionValue((Object)object, (String)"removeLast(...)");
            int n = ((Number)object).intValue();
            try {
                view.performHapticFeedback(3);
            }
            catch (Exception exception) {
                // empty catch block
            }
            MainActivityKt.SettingsOverlay$lambda$193(mutableIntState, n);
        } else {
            try {
                view.performHapticFeedback(3);
            }
            catch (Exception exception) {
                // empty catch block
            }
            SoundPlayer.INSTANCE.stop();
            function0.invoke();
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$228(ThemeOption themeOption, View object, Function0 function0, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C1007@50055L1244:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1910115223, (int)n, (int)-1, (String)"com.example.SettingsOverlay.<anonymous> (MainActivity.kt:1007)");
            }
            Object object2 = PaddingKt.padding-VpY3zN4((Modifier)WindowInsetsPadding_androidKt.statusBarsPadding((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null)), (float)Dp.constructor-impl((float)24), (float)Dp.constructor-impl((float)16));
            Arrangement.Horizontal horizontal = (Arrangement.Horizontal)Arrangement.INSTANCE.getSpaceBetween();
            Alignment.Vertical vertical = Alignment.Companion.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            horizontal = RowKt.rowMeasurePolicy((Arrangement.Horizontal)horizontal, (Alignment.Vertical)vertical, (Composer)composer, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            vertical = composer.getCurrentCompositionLocalMap();
            object2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object2);
            Function0 function02 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function02);
            } else {
                composer.useNode();
            }
            function02 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function02, (Object)horizontal, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function02, (Object)vertical, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            horizontal = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                function02.updateRememberedValue((Object)n);
                function02.apply((Object)n, (Function2)horizontal);
            }
            Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object2 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1122770237, (String)"C1017@50480L17,1019@50583L8,1015@50402L261,1024@50723L308,1031@51050L235,1023@50681L604:MainActivity.kt#to5c3");
            horizontal = TypographyKt.getCursiveFontFamily((Composer)composer, (int)0);
            object2 = FontWeight.Companion.getBold();
            long l = ScaledSpKt.getScaledSp((int)32, (Composer)composer, (int)6);
            long l2 = themeOption.getTextPrimary-0d7_KjU();
            TextKt.Text--4IGK_g((String)"Settings", null, (long)l2, (long)l, null, (FontWeight)object2, (FontFamily)horizontal, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)130962);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1283134687, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance(object);
            boolean bl2 = composer.changed((Object)function0);
            object2 = composer.rememberedValue();
            if (!(bl | bl2) && object2 != Composer.Companion.getEmpty()) {
                object = object2;
            } else {
                object = new MainActivityKt$$ExternalSyntheticLambda96((View)object, function0);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            IconButtonKt.IconButton((Function0)object, null, (boolean)false, null, null, (Function2)((Function2)ComposableLambdaKt.rememberComposableLambda((int)-796569622, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda97(themeOption), (Composer)composer, (int)54)), (Composer)composer, (int)196608, (int)30);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$228$lambda$227$lambda$225$lambda$224(View view, Function0 function0) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        SoundPlayer.INSTANCE.stop();
        function0.invoke();
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$228$lambda$227$lambda$226(ThemeOption themeOption, Composer composer, int n) {
        ComposerKt.sourceInformation((Composer)composer, (String)"C1032@51072L195:MainActivity.kt#to5c3");
        if ((n & 3) == 2 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-796569622, (int)n, (int)-1, (String)"com.example.SettingsOverlay.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1032)");
            }
            ImageVector imageVector = CloseKt.getClose((Icons.Filled)Icons.Filled.INSTANCE);
            IconKt.Icon-ww6aTOc((ImageVector)imageVector, (String)"Close", null, (long)themeOption.getTextPrimary-0d7_KjU(), (Composer)composer, (int)48, (int)4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$306(LazyListState object, List object2, MutableIntState mutableIntState, View view, SnapshotStateList snapshotStateList, Context context, MutableState mutableState, MutableState mutableState2, MutableState mutableState3, AudioManager audioManager, float f, MutableState mutableState4, ManagedActivityResultLauncher managedActivityResultLauncher, MutableState mutableState5, MutableState mutableState6, PaddingValues object3, Composer composer, int n) {
        int n2;
        Intrinsics.checkNotNullParameter((Object)object3, (String)"paddingValues");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1041@51343L26164:MainActivity.kt#to5c3");
        int n3 = n2 = n;
        if ((n & 6) == 0) {
            n = composer.changed(object3) ? 4 : 2;
            n3 = n2 | n;
        }
        if ((n3 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1568336244, (int)n3, (int)-1, (String)"com.example.SettingsOverlay.<anonymous> (MainActivity.kt:1041)");
            }
            object3 = PaddingKt.padding((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (PaddingValues)object3);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            MeasurePolicy measurePolicy = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            object3 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object3);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            n2 = (0 << 3 & 0x70) << 6 & 0x380 | 6;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            measurePolicy = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)measurePolicy);
            }
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object3 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)135227390, (String)"C1046@51511L7,1052@51829L2809,1047@51564L3074,1101@54652L22849:MainActivity.kt#to5c3");
            object3 = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object3 = composer.consume((CompositionLocal)object3);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            measurePolicy = (ThemeOption)object3;
            function0 = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            object3 = Arrangement.INSTANCE.spacedBy-0680j_4(Dp.constructor-impl((float)8));
            compositionLocalMap = PaddingKt.PaddingValues-YgX7TsA((float)Dp.constructor-impl((float)24), (float)Dp.constructor-impl((float)8));
            Arrangement.Horizontal horizontal = (Arrangement.Horizontal)object3;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-826933801, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance(object2);
            boolean bl2 = composer.changed((Object)mutableIntState);
            boolean bl3 = composer.changedInstance((Object)measurePolicy);
            boolean bl4 = composer.changedInstance((Object)view);
            object3 = composer.rememberedValue();
            if (!(bl | bl2 | bl3 | bl4) && object3 != Composer.Companion.getEmpty()) {
                object2 = object3;
            } else {
                object2 = new MainActivityKt$$ExternalSyntheticLambda138((List)object2, (ThemeOption)measurePolicy, view, mutableIntState, snapshotStateList);
                composer.updateRememberedValue(object2);
            }
            object2 = (Function1)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            LazyDslKt.LazyRow((Modifier)function0, (LazyListState)object, (PaddingValues)compositionLocalMap, (boolean)false, (Arrangement.Horizontal)horizontal, null, null, (boolean)false, (Function1)object2, (Composer)composer, (int)24966, (int)232);
            snapshotStateList = SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            object = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)Alignment.Companion.getTopStart(), (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object2 = composer.getCurrentCompositionLocalMap();
            snapshotStateList = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)snapshotStateList);
            object3 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)object3);
            } else {
                composer.useNode();
            }
            object3 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)object3, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object3, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object3.getInserting() || !Intrinsics.areEqual((Object)object3.rememberedValue(), (Object)n)) {
                object3.updateRememberedValue((Object)n);
                object3.apply((Object)n, (Function2)object);
            }
            Updater.set-impl((Composer)object3, (Object)snapshotStateList, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            object = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2061787153, (String)"C1106@54872L1025,1120@55969L21492,1104@54766L22695:MainActivity.kt#to5c3");
            n = MainActivityKt.SettingsOverlay$lambda$192(mutableIntState);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1036358055, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda139();
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            AnimatedContentKt.AnimatedContent((Object)n, null, (Function1)object, null, (String)"SettingsTabTransition", null, (Function4)((Function4)ComposableLambdaKt.rememberComposableLambda((int)-335780754, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda140((ThemeOption)measurePolicy, context, view, mutableState, mutableState2, mutableState3, audioManager, f, mutableState4, managedActivityResultLauncher, mutableState5, mutableState6), (Composer)composer, (int)54)), (Composer)composer, (int)1597824, (int)42);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236(List list, ThemeOption themeOption, View view, MutableIntState mutableIntState, SnapshotStateList snapshotStateList, LazyListScope lazyListScope) {
        Intrinsics.checkNotNullParameter((Object)lazyListScope, (String)"$this$LazyRow");
        LazyListScope.items$default((LazyListScope)lazyListScope, (int)list.size(), null, null, (Function4)((Function4)ComposableLambdaKt.composableLambdaInstance((int)-1228530455, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda124(themeOption, view, mutableIntState, snapshotStateList, list))), (int)6, null);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235(ThemeOption object, View view, MutableIntState mutableIntState, SnapshotStateList snapshotStateList, List list, LazyItemScope lazyItemScope, int n, Composer composer, int n2) {
        int n3;
        Intrinsics.checkNotNullParameter((Object)lazyItemScope, (String)"$this$items");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1055@52003L301,1059@52372L296,1063@52741L266,1082@53797L406,1068@53049L1557:MainActivity.kt#to5c3");
        int n4 = n3 = n2;
        if ((n2 & 0x30) == 0) {
            n2 = composer.changed(n) ? 32 : 16;
            n4 = n3 | n2;
        }
        if ((n4 & 0x91) == 144 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1228530455, (int)n4, (int)-1, (String)"com.example.SettingsOverlay.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1054)");
            }
            n2 = MainActivityKt.SettingsOverlay$lambda$192(mutableIntState) == n ? 1 : 0;
            long l = n2 != 0 ? object.getPrimary-0d7_KjU() : object.getSurface-0d7_KjU();
            State state = SingleValueAnimationKt.animateColorAsState-euL9pac((long)l, (AnimationSpec)((AnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null)), (String)"", null, (Composer)composer, (int)432, (int)8);
            l = n2 != 0 ? Color.Companion.getWhite-0d7_KjU() : object.getTextPrimary-0d7_KjU();
            lazyItemScope = SingleValueAnimationKt.animateColorAsState-euL9pac((long)l, (AnimationSpec)((AnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null)), (String)"", null, (Composer)composer, (int)432, (int)8);
            float f = n2 != 0 ? Dp.constructor-impl((float)4) : Dp.constructor-impl((float)1.0f);
            State state2 = AnimateAsStateKt.animateDpAsState-AjpBEmI((float)f, (AnimationSpec)((AnimationSpec)AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null)), (String)"", null, (Composer)composer, (int)432, (int)8);
            Modifier modifier = (Modifier)Modifier.Companion;
            f = MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$231((State<Dp>)state2);
            state2 = (Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
            state = BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)ShadowKt.shadow-s4CzXII$default((Modifier)modifier, (float)f, (Shape)state2, (boolean)false, (long)0L, (long)object.getShadowColor-0d7_KjU(), (int)12, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16)))), (long)MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$229((State<Color>)state), null, (int)2, null);
            f = Dp.constructor-impl((float)1.0f);
            l = n2 != 0 ? object.getPrimary-0d7_KjU() : object.getCardBorder-0d7_KjU();
            state = BorderKt.border-xT4_qwU((Modifier)state, (float)f, (long)l, (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16))));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)498301407, (String)"CC(remember):MainActivity.kt#9igjgp");
            int n5 = composer.changedInstance((Object)view);
            int n6 = composer.changed((Object)mutableIntState);
            n2 = (n4 & 0x70) == 32 ? 1 : 0;
            object = composer.rememberedValue();
            if ((n5 | n6 | n2) != 0 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda156(view, n, snapshotStateList, mutableIntState);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            mutableIntState = ClickableKt.clickable-XHw0xAI$default((Modifier)state, (boolean)false, null, null, (Function0)object, (int)7, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            object = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)Alignment.Companion.getTopStart(), (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            view = composer.getCurrentCompositionLocalMap();
            mutableIntState = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)mutableIntState);
            snapshotStateList = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)snapshotStateList);
            } else {
                composer.useNode();
            }
            snapshotStateList = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)snapshotStateList, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)snapshotStateList, (Object)view, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (snapshotStateList.getInserting() || !Intrinsics.areEqual((Object)snapshotStateList.rememberedValue(), (Object)n2)) {
                snapshotStateList.updateRememberedValue((Object)n2);
                snapshotStateList.apply((Object)n2, (Function2)object);
            }
            Updater.set-impl((Composer)snapshotStateList, (Object)mutableIntState, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            object = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1982496344, (String)"C1095@54550L8,1090@54252L332:MainActivity.kt#to5c3");
            object = (String)list.get(n);
            l = MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$230((State<Color>)lazyItemScope);
            mutableIntState = PaddingKt.padding-VpY3zN4((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)8));
            view = FontWeight.Companion.getBold();
            long l2 = ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)object, (Modifier)mutableIntState, (long)l, (long)l2, null, (FontWeight)view, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196656, (int)0, (int)131024);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    private static final long SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$229(State<Color> state) {
        return ((Color)state.getValue()).unbox-impl();
    }

    private static final long SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$230(State<Color> state) {
        return ((Color)state.getValue()).unbox-impl();
    }

    private static final float SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$231(State<Dp> state) {
        return ((Dp)state.getValue()).unbox-impl();
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$233$lambda$232(View view, int n, SnapshotStateList snapshotStateList, MutableIntState mutableIntState) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (MainActivityKt.SettingsOverlay$lambda$192(mutableIntState) != n) {
            snapshotStateList.add((Object)MainActivityKt.SettingsOverlay$lambda$192(mutableIntState));
            MainActivityKt.SettingsOverlay$lambda$193(mutableIntState, n);
        }
        return Unit.INSTANCE;
    }

    static final ContentTransform SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$241$lambda$240(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$AnimatedContent");
        return AnimatedContentKt.togetherWith((EnterTransition)EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)220, (int)90, null, (int)4, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.slideInHorizontally((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.8f, (float)300.0f, null, (int)4, null)), (Function1)new MainActivityKt$$ExternalSyntheticLambda49(animatedContentTransitionScope))), (ExitTransition)EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.tween$default((int)90, (int)0, null, (int)6, null)), (float)0.0f, (int)2, null).plus(EnterExitTransitionKt.slideOutHorizontally((FiniteAnimationSpec)((FiniteAnimationSpec)AnimationSpecKt.spring$default((float)0.8f, (float)300.0f, null, (int)4, null)), (Function1)new MainActivityKt$$ExternalSyntheticLambda50(animatedContentTransitionScope))));
    }

    static final int SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$241$lambda$240$lambda$238(AnimatedContentTransitionScope animatedContentTransitionScope, int n) {
        if (((Number)animatedContentTransitionScope.getTargetState()).intValue() <= ((Number)animatedContentTransitionScope.getInitialState()).intValue()) {
            n = -n;
        }
        return n;
    }

    static final int SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$241$lambda$240$lambda$239(AnimatedContentTransitionScope animatedContentTransitionScope, int n) {
        block0: {
            if (((Number)animatedContentTransitionScope.getTargetState()).intValue() <= ((Number)animatedContentTransitionScope.getInitialState()).intValue()) break block0;
            n = -n;
        }
        return n;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303(ThemeOption themeOption, Context object, View view, MutableState mutableState, MutableState mutableState2, MutableState object2, AudioManager audioManager, float f, MutableState mutableState3, ManagedActivityResultLauncher managedActivityResultLauncher, MutableState mutableState4, MutableState mutableState5, AnimatedContentScope object3, int n, Composer composer, int n2) {
        Intrinsics.checkNotNullParameter((Object)object3, (String)"$this$AnimatedContent");
        ComposerKt.sourceInformation((Composer)composer, (String)"C:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-335780754, (int)n2, (int)-1, (String)"com.example.SettingsOverlay.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1121)");
        }
        if (n == 4) {
            composer.startReplaceGroup(-265554414);
            ComposerKt.sourceInformation((Composer)composer, (String)"1122@56074L24");
            AboutScreenKt.InteractiveAboutScreen((Composer)composer, (int)0);
            composer.endReplaceGroup();
        } else {
            boolean bl;
            composer.startReplaceGroup(-264793922);
            ComposerKt.sourceInformation((Composer)composer, (String)"1128@56364L21,1124@56152L21237");
            Modifier modifier = ScrollKt.verticalScroll$default((Modifier)PaddingKt.padding-VpY3zN4$default((Modifier)SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)Dp.constructor-impl((float)24), (float)0.0f, (int)2, null), (ScrollState)ScrollKt.rememberScrollState((int)0, (Composer)composer, (int)0, (int)1), (boolean)false, null, (boolean)false, (int)14, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            MeasurePolicy measurePolicy = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object3 = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n2)) {
                function0.updateRememberedValue((Object)n2);
                function0.apply((Object)n2, (Function2)object3);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object3 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1970642173, (String)"C1130@56443L41,1440@77321L42:MainActivity.kt#to5c3");
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
            if (n == 0) {
                composer.startReplaceGroup(-1970902450);
                ComposerKt.sourceInformation((Composer)composer, (String)"1132@56568L70,1133@56669L70,1138@56900L49,1140@57062L9243,1135@56753L9552");
                float f2 = MainActivityKt.SettingsOverlay$lambda$204((MutableState<Float>)mutableState2);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1726153938, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl = composer.changed(f2);
                object3 = composer.rememberedValue();
                if (bl || object3 == Composer.Companion.getEmpty()) {
                    object3 = SnapshotStateKt.mutableStateOf$default((Object)String.valueOf((int)MainActivityKt.SettingsOverlay$lambda$204((MutableState<Float>)mutableState2)), null, (int)2, null);
                    composer.updateRememberedValue(object3);
                }
                measurePolicy = (MutableState)object3;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                f2 = MainActivityKt.SettingsOverlay$lambda$207((MutableState<Float>)object2);
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1726150706, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl = composer.changed(f2);
                object3 = composer.rememberedValue();
                if (bl || object3 == Composer.Companion.getEmpty()) {
                    object3 = SnapshotStateKt.mutableStateOf$default((Object)String.valueOf((int)MainActivityKt.SettingsOverlay$lambda$207((MutableState<Float>)object2)), null, (int)2, null);
                    composer.updateRememberedValue(object3);
                }
                MutableState mutableState6 = (MutableState)object3;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                function0 = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
                Shape shape = (Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
                modifier = CardDefaults.INSTANCE.cardColors-ro_MJ88(themeOption.getSurface-0d7_KjU(), 0L, 0L, 0L, composer, CardDefaults.$stable << 12, 14);
                BorderStroke borderStroke = BorderStrokeKt.BorderStroke-cXLIe8U((float)Dp.constructor-impl((float)1.0f), (long)themeOption.getCardBorder-0d7_KjU());
                object3 = composer;
                CardKt.Card((Modifier)function0, (Shape)shape, (CardColors)modifier, null, (BorderStroke)borderStroke, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-597187755, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda65(themeOption, view, audioManager, f, (MutableState)measurePolicy, mutableState2, mutableState6, (MutableState)object2, mutableState3, managedActivityResultLauncher, (Context)object, mutableState4, mutableState5), (Composer)object3, (int)54)), (Composer)object3, (int)196614, (int)8);
                object3.endReplaceGroup();
            } else {
                mutableState2 = composer;
                mutableState2.startReplaceGroup(-2027283142);
                mutableState2.endReplaceGroup();
            }
            mutableState2 = composer;
            if (n == 1) {
                mutableState2.startReplaceGroup(-1961433717);
                ComposerKt.sourceInformation((Composer)mutableState2, (String)"1284@66421L21");
                ThemeFontCustomizerKt.ThemeFontCustomizer(null, (Composer)mutableState2, (int)0, (int)1);
            } else {
                mutableState2.startReplaceGroup(-2027283142);
            }
            mutableState2.endReplaceGroup();
            if (n == 2) {
                mutableState2.startReplaceGroup(-1960984124);
                ComposerKt.sourceInformation((Composer)mutableState2, (String)"1288@66544L79,1289@66683L25,1290@66777L373,1296@67236L7,1297@67287L98,1299@67452L614,1299@67419L647,1418@76056L49,1420@76254L63,1409@75565L206,1421@76336L765,1408@75529L1572");
                ComposerKt.sourceInformationMarkerStart((Composer)mutableState2, (int)-1725834697, (String)"CC(remember):MainActivity.kt#9igjgp");
                object2 = mutableState2.rememberedValue();
                if (object2 == Composer.Companion.getEmpty()) {
                    object2 = InteractionSourceKt.MutableInteractionSource();
                    mutableState2.updateRememberedValue(object2);
                }
                audioManager = (MutableInteractionSource)object2;
                ComposerKt.sourceInformationMarkerEnd((Composer)mutableState2);
                f = MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$275((State<Boolean>)PressInteractionKt.collectIsPressedAsState((InteractionSource)((InteractionSource)audioManager), (Composer)mutableState2, (int)6)) ? 0.96f : 1.0f;
                object2 = (AnimationSpec)AnimationSpecKt.spring$default((float)0.5f, (float)200.0f, null, (int)4, null);
                mutableState3 = AnimateAsStateKt.animateFloatAsState((float)f, (AnimationSpec)object2, (float)0.0f, (String)"blockedScale", null, (Composer)mutableState2, (int)3120, (int)20);
                object2 = (CompositionLocal)LocalLifecycleOwnerKt.getLocalLifecycleOwner();
                ComposerKt.sourceInformationMarkerStart((Composer)mutableState2, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
                object2 = mutableState2.consume((CompositionLocal)object2);
                ComposerKt.sourceInformationMarkerEnd((Composer)mutableState2);
                mutableState4 = (LifecycleOwner)object2;
                ComposerKt.sourceInformationMarkerStart((Composer)mutableState2, (int)-1725810902, (String)"CC(remember):MainActivity.kt#9igjgp");
                object2 = mutableState2.rememberedValue();
                if (object2 == Composer.Companion.getEmpty()) {
                    object2 = SnapshotStateKt.mutableStateOf$default((Object)AppBlockerManager.INSTANCE.getAccessibilityStatus(object), null, (int)2, null);
                    mutableState2.updateRememberedValue(object2);
                }
                managedActivityResultLauncher = object2;
                ComposerKt.sourceInformationMarkerEnd((Composer)mutableState2);
                ComposerKt.sourceInformationMarkerStart((Composer)mutableState2, (int)-1725805106, (String)"CC(remember):MainActivity.kt#9igjgp");
                boolean bl2 = mutableState2.changedInstance(object);
                bl = mutableState2.changedInstance((Object)mutableState4);
                object2 = mutableState2.rememberedValue();
                if (bl2 | bl || object2 == Composer.Companion.getEmpty()) {
                    object2 = new MainActivityKt$$ExternalSyntheticLambda66((LifecycleOwner)mutableState4, (Context)object, (MutableState)managedActivityResultLauncher);
                    mutableState2.updateRememberedValue(object2);
                }
                object2 = (Function1)object2;
                ComposerKt.sourceInformationMarkerEnd((Composer)mutableState2);
                EffectsKt.DisposableEffect((Object)mutableState4, (Function1)object2, (Composer)mutableState2, (int)0);
                if (MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$278((MutableState<AppBlockerManager.AccessibilityServiceStatus>)managedActivityResultLauncher) == AppBlockerManager.AccessibilityServiceStatus.DISABLED) {
                    mutableState2.startReplaceGroup(-1959561286);
                    ComposerKt.sourceInformation((Composer)mutableState2, (String)"1315@68403L68,1317@68644L38,1318@68705L2055,1312@68208L2552");
                    object2 = PaddingKt.padding-qDBjuR0$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)16), (int)7, null);
                    object3 = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
                    mutableState5 = CardDefaults.INSTANCE.cardColors-ro_MJ88(Color.copy-wmQWz5c$default((long)themeOption.getPrimary-0d7_KjU(), (float)0.1f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), 0L, 0L, 0L, (Composer)mutableState2, CardDefaults.$stable << 12, 14);
                    managedActivityResultLauncher = BorderStrokeKt.BorderStroke-cXLIe8U((float)Dp.constructor-impl((float)1.0f), (long)Color.copy-wmQWz5c$default((long)themeOption.getPrimary-0d7_KjU(), (float)0.3f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null));
                    mutableState4 = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl((float)0.0f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, (Composer)mutableState2, CardDefaults.$stable << 18 | 6, 62);
                    object3 = (Shape)object3;
                    CardKt.Card((Modifier)object2, (Shape)object3, (CardColors)mutableState5, (CardElevation)mutableState4, (BorderStroke)managedActivityResultLauncher, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-1282906264, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda67(themeOption, (Context)object), (Composer)mutableState2, (int)54)), (Composer)mutableState2, (int)196614, (int)0);
                    mutableState2.endReplaceGroup();
                } else if (MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$278((MutableState<AppBlockerManager.AccessibilityServiceStatus>)managedActivityResultLauncher) == AppBlockerManager.AccessibilityServiceStatus.READY) {
                    mutableState2.startReplaceGroup(-1956932703);
                    ComposerKt.sourceInformation((Composer)mutableState2, (String)"1351@71100L65,1353@71335L38,1348@70905L1041");
                    object2 = PaddingKt.padding-qDBjuR0$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)16), (int)7, null);
                    mutableState5 = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
                    mutableState4 = CardDefaults.INSTANCE.cardColors-ro_MJ88(Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4283215696L), (float)0.1f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), 0L, 0L, 0L, (Composer)mutableState2, CardDefaults.$stable << 12 | 6, 14);
                    managedActivityResultLauncher = BorderStrokeKt.BorderStroke-cXLIe8U((float)Dp.constructor-impl((float)1.0f), (long)Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4283215696L), (float)0.3f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null));
                    object = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl((float)0.0f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, (Composer)mutableState2, CardDefaults.$stable << 18 | 6, 62);
                    mutableState5 = (Shape)mutableState5;
                    CardKt.Card((Modifier)object2, (Shape)mutableState5, (CardColors)mutableState4, (CardElevation)object, (BorderStroke)managedActivityResultLauncher, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$1554836497$app(), (Composer)mutableState2, (int)221190, (int)0);
                    mutableState2.endReplaceGroup();
                } else if (MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$278((MutableState<AppBlockerManager.AccessibilityServiceStatus>)managedActivityResultLauncher) != AppBlockerManager.AccessibilityServiceStatus.STALE && MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$278((MutableState<AppBlockerManager.AccessibilityServiceStatus>)managedActivityResultLauncher) != AppBlockerManager.AccessibilityServiceStatus.UNKNOWN) {
                    mutableState2.startReplaceGroup(-2027283142);
                    mutableState2.endReplaceGroup();
                } else {
                    mutableState2.startReplaceGroup(-1955587861);
                    ComposerKt.sourceInformation((Composer)mutableState2, (String)"1366@72517L65,1368@72752L38,1369@72813L2680,1363@72322L3171");
                    bl = MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$278((MutableState<AppBlockerManager.AccessibilityServiceStatus>)managedActivityResultLauncher) == AppBlockerManager.AccessibilityServiceStatus.UNKNOWN;
                    mutableState5 = PaddingKt.padding-qDBjuR0$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)16), (int)7, null);
                    measurePolicy = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
                    object3 = CardDefaults.INSTANCE.cardColors-ro_MJ88(Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4294278144L), (float)0.1f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), 0L, 0L, 0L, (Composer)mutableState2, CardDefaults.$stable << 12 | 6, 14);
                    object2 = BorderStrokeKt.BorderStroke-cXLIe8U((float)Dp.constructor-impl((float)1.0f), (long)Color.copy-wmQWz5c$default((long)ColorKt.Color((long)4294278144L), (float)0.3f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null));
                    mutableState4 = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl((float)0.0f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, (Composer)mutableState2, CardDefaults.$stable << 18 | 6, 62);
                    measurePolicy = (Shape)measurePolicy;
                    CardKt.Card((Modifier)mutableState5, (Shape)measurePolicy, (CardColors)object3, (CardElevation)mutableState4, (BorderStroke)object2, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)1961863984, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda68(themeOption, bl, (Context)object, (MutableState)managedActivityResultLauncher), (Composer)mutableState2, (int)54)), (Composer)mutableState2, (int)221190, (int)0);
                    mutableState2.endReplaceGroup();
                }
                object2 = ScaleKt.scale((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$276((State<Float>)mutableState3));
                mutableState5 = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
                mutableState3 = CardDefaults.INSTANCE.cardColors-ro_MJ88(themeOption.getSurface-0d7_KjU(), 0L, 0L, 0L, (Composer)mutableState2, CardDefaults.$stable << 12, 14);
                managedActivityResultLauncher = BorderStrokeKt.BorderStroke-cXLIe8U((float)Dp.constructor-impl((float)1.0f), (long)themeOption.getCardBorder-0d7_KjU());
                mutableState4 = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl((float)1.0f), Dp.constructor-impl((float)0.0f), 0.0f, 0.0f, 0.0f, 0.0f, (Composer)mutableState2, CardDefaults.$stable << 18 | 0x36, 60);
                ComposerKt.sourceInformationMarkerStart((Composer)mutableState2, (int)-1725545898, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl2 = mutableState2.changedInstance((Object)view);
                bl = mutableState2.changed((Object)mutableState);
                object = mutableState2.rememberedValue();
                if (bl2 | bl || object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda69(view, mutableState);
                    mutableState2.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)mutableState2);
                view = (Shape)mutableState5;
                CardKt.Card((Function0)object, (Modifier)object2, (boolean)false, (Shape)view, (CardColors)mutableState3, (CardElevation)mutableState4, (BorderStroke)managedActivityResultLauncher, (MutableInteractionSource)audioManager, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)1405418896, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda70(themeOption), (Composer)mutableState2, (int)54)), (Composer)mutableState2, (int)0x6C00000, (int)4);
            } else {
                mutableState2.startReplaceGroup(-2027283142);
            }
            mutableState2.endReplaceGroup();
            if (n == 3) {
                mutableState2.startReplaceGroup(-1950708461);
                ComposerKt.sourceInformation((Composer)mutableState2, (String)"1438@77222L24");
                SystemProtectionScreenKt.SystemProtectionScreen((Composer)mutableState2, (int)0);
            } else {
                mutableState2.startReplaceGroup(-2027283142);
            }
            mutableState2.endReplaceGroup();
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)100)), (Composer)mutableState2, (int)6);
            ComposerKt.sourceInformationMarkerEnd((Composer)mutableState2);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endReplaceGroup();
        }
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    private static final String SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$243(MutableState<String> mutableState) {
        return (String)((State)mutableState).getValue();
    }

    private static final void SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$244(MutableState<String> mutableState, String string) {
        mutableState.setValue((Object)string);
    }

    private static final String SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$246(MutableState<String> mutableState) {
        return (String)((State)mutableState).getValue();
    }

    private static final void SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$247(MutableState<String> mutableState, String string) {
        mutableState.setValue((Object)string);
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273(ThemeOption object, View view, AudioManager object2, float f, MutableState object3, MutableState object4, MutableState object52, MutableState mutableState, MutableState object6, ManagedActivityResultLauncher managedActivityResultLauncher, Context context, MutableState mutableState2, MutableState mutableState3, ColumnScope object7, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object7, (String)"$this$Card");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1141@57080L9211:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-597187755, (int)n, (int)-1, (String)"com.example.SettingsOverlay.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1141)");
            }
            Modifier modifier = PaddingKt.padding-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            Object object8 = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(6 >> 3 & 0xE | 6 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object7 = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object8, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)object7, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object7 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)object7);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object7 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-308817168, (String)"C1142@57181L8,1142@57145L109,1143@57275L41,1145@57358L1430,1164@58919L475,1175@59490L76,1162@58809L779,1177@59609L40,1179@59691L1430,1198@61252L475,1209@61823L76,1196@61142L779,1212@61963L117,1214@62215L8,1214@62122L102,1217@62338L589,1230@63109L76,1215@62245L962,1232@63228L41,1234@63384L8,1234@63311L112,1235@63444L40,1236@63505L1682,1261@65229L41,1262@65413L8,1262@65291L161,1265@65578L501,1276@66175L76,1263@65473L800:MainActivity.kt#to5c3");
            long l = ScaledSpKt.getScaledSp((int)16, (Composer)composer, (int)6);
            object7 = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"Timer & Sound", null, (long)object.getTextPrimary-0d7_KjU(), (long)l, null, (FontWeight)object7, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131026);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
            object7 = Alignment.Companion.getCenterVertically();
            modifier = Arrangement.INSTANCE.getSpaceBetween();
            object8 = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            modifier = (Arrangement.Horizontal)modifier;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            modifier = RowKt.rowMeasurePolicy((Arrangement.Horizontal)modifier, (Alignment.Vertical)object7, (Composer)composer, (int)(438 >> 3 & 0xE | 438 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object7 = composer.getCurrentCompositionLocalMap();
            object8 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object8);
            function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)object7, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object7 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)object7);
            }
            Updater.set-impl((Composer)function0, (Object)object8, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object7 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1325487320, (String)"C1146@57626L8,1146@57522L113,1149@57801L344,1147@57693L1073:MainActivity.kt#to5c3");
            l = object.getTextPrimary-0d7_KjU();
            object7 = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"Focus Time (min):", null, (long)l, (long)ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6), null, (FontWeight)object7, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131026);
            String string = MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$243((MutableState<String>)object3);
            function0 = new KeyboardOptions(0, null, KeyboardType.Companion.getNumber-PjHm6EE(), 0, null, null, null, 123, null);
            modifier = new TextStyle(object.getTextPrimary-0d7_KjU(), 0L, FontWeight.Companion.getBold(), null, null, null, null, 0L, null, null, null, 0L, null, null, null, TextAlign.Companion.getEnd-e0LSkKk(), 0, 0L, null, null, null, 0, 0, null, 0xFF7FFA, null);
            object8 = PaddingKt.padding-VpY3zN4((Modifier)BackgroundKt.background-bw27NRU((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)60)), (long)Color.copy-wmQWz5c$default((long)object.getTextPrimary-0d7_KjU(), (float)0.05f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)8)))), (float)Dp.constructor-impl((float)8), (float)Dp.constructor-impl((float)4));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-372876313, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changed(object3);
            object7 = composer.rememberedValue();
            if (!bl && object7 != Composer.Companion.getEmpty()) {
                object3 = object7;
            } else {
                object3 = new MainActivityKt$$ExternalSyntheticLambda22((MutableState)object3, (MutableState)object4);
                composer.updateRememberedValue(object3);
            }
            object3 = (Function1)object3;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            BasicTextFieldKt.BasicTextField((String)string, (Function1)object3, (Modifier)object8, (boolean)false, (boolean)false, (TextStyle)modifier, (KeyboardOptions)function0, null, (boolean)true, (int)0, (int)0, null, null, null, null, null, (Composer)composer, (int)102236160, (int)0, (int)65176);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            float f2 = RangesKt.coerceIn((float)MainActivityKt.SettingsOverlay$lambda$204((MutableState<Float>)object4), (float)1.0f, (float)60.0f);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-841197722, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changedInstance((Object)view);
            object3 = composer.rememberedValue();
            if (bl || object3 == Composer.Companion.getEmpty()) {
                object3 = new MainActivityKt$$ExternalSyntheticLambda23(view, (MutableState)object4);
                composer.updateRememberedValue(object3);
            }
            object3 = (Function1)object3;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SliderKt.Slider((float)f2, (Function1)object3, null, (boolean)false, (ClosedFloatingPointRange)RangesKt.rangeTo((float)1.0f, (float)60.0f), (int)0, null, (SliderColors)SliderDefaults.INSTANCE.colors-q0g_0yA(ColorKt.Color((long)4294937216L), ColorKt.Color((long)4294954112L), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, composer, 54, 6, 1020), null, (Composer)composer, (int)0, (int)364);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            object3 = Alignment.Companion.getCenterVertically();
            object7 = Arrangement.INSTANCE.getSpaceBetween();
            object4 = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            object7 = (Arrangement.Horizontal)object7;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            object7 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)object7, (Alignment.Vertical)object3, (Composer)composer, (int)(438 >> 3 & 0xE | 438 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object3 = composer.getCurrentCompositionLocalMap();
            object4 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object4);
            object8 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)object8);
            } else {
                composer.useNode();
            }
            object8 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)object8, (Object)object7, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object8, (Object)object3, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object8.getInserting() || !Intrinsics.areEqual((Object)object8.rememberedValue(), (Object)n)) {
                object8.updateRememberedValue((Object)n);
                object8.apply((Object)n, (Function2)object3);
            }
            Updater.set-impl((Composer)object8, (Object)object4, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object3 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1278766529, (String)"C1180@59959L8,1180@59855L113,1183@60134L344,1181@60026L1073:MainActivity.kt#to5c3");
            l = object.getTextPrimary-0d7_KjU();
            object3 = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"Break Time (min):", null, (long)l, (long)ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6), null, (FontWeight)object3, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131026);
            object8 = MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$246((MutableState<String>)object52);
            object4 = new KeyboardOptions(0, null, KeyboardType.Companion.getNumber-PjHm6EE(), 0, null, null, null, 123, null);
            modifier = new TextStyle(object.getTextPrimary-0d7_KjU(), 0L, FontWeight.Companion.getBold(), null, null, null, null, 0L, null, null, null, 0L, null, null, null, TextAlign.Companion.getEnd-e0LSkKk(), 0, 0L, null, null, null, 0, 0, null, 0xFF7FFA, null);
            object7 = PaddingKt.padding-VpY3zN4((Modifier)BackgroundKt.background-bw27NRU((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)60)), (long)Color.copy-wmQWz5c$default((long)object.getTextPrimary-0d7_KjU(), (float)0.05f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)8)))), (float)Dp.constructor-impl((float)8), (float)Dp.constructor-impl((float)4));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1011089886, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changed(object52);
            object3 = composer.rememberedValue();
            if (bl || object3 == Composer.Companion.getEmpty()) {
                object3 = new MainActivityKt$$ExternalSyntheticLambda24((MutableState)object52, mutableState);
                composer.updateRememberedValue(object3);
            }
            object3 = (Function1)object3;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            BasicTextFieldKt.BasicTextField((String)object8, (Function1)object3, (Modifier)object7, (boolean)false, (boolean)false, (TextStyle)modifier, (KeyboardOptions)object4, null, (boolean)true, (int)0, (int)0, null, null, null, null, null, (Composer)composer, (int)102236160, (int)0, (int)65176);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            f2 = RangesKt.coerceIn((float)MainActivityKt.SettingsOverlay$lambda$207((MutableState<Float>)mutableState), (float)1.0f, (float)30.0f);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-841123066, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changedInstance((Object)view);
            object3 = composer.rememberedValue();
            if (bl || object3 == Composer.Companion.getEmpty()) {
                object3 = new MainActivityKt$$ExternalSyntheticLambda25(view, mutableState);
                composer.updateRememberedValue(object3);
            }
            object3 = (Function1)object3;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SliderKt.Slider((float)f2, (Function1)object3, null, (boolean)false, (ClosedFloatingPointRange)RangesKt.rangeTo((float)1.0f, (float)30.0f), (int)0, null, (SliderColors)SliderDefaults.INSTANCE.colors-q0g_0yA(ColorKt.Color((long)4286698746L), ColorKt.Color((long)4289979900L), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, composer, 54, 6, 1020), null, (Composer)composer, (int)0, (int)364);
            DividerKt.HorizontalDivider-9IZ8Weo((Modifier)PaddingKt.padding-VpY3zN4$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)Dp.constructor-impl((float)16), (int)1, null), (float)0.0f, (long)Color.copy-wmQWz5c$default((long)object.getTextPrimary-0d7_KjU(), (float)0.1f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (Composer)composer, (int)6, (int)2);
            l = object.getTextPrimary-0d7_KjU();
            object3 = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"Volume", null, (long)l, (long)ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6), null, (FontWeight)object3, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131026);
            f2 = MainActivityKt.SettingsOverlay$lambda$216((MutableState<Float>)object6);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-841088200, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl2 = composer.changedInstance((Object)view);
            bl = composer.changedInstance(object2);
            object3 = composer.rememberedValue();
            if (!(bl2 | bl) && object3 != Composer.Companion.getEmpty()) {
                object2 = object3;
            } else {
                object2 = new MainActivityKt$$ExternalSyntheticLambda26(view, (AudioManager)object2, (MutableState)object6);
                composer.updateRememberedValue(object2);
            }
            object3 = (Function1)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object4 = RangesKt.rangeTo((float)0.0f, (float)f);
            object2 = (int)f - 1;
            n = ((Number)object2).intValue();
            n = n > 0 ? 1 : 0;
            if (n == 0) {
                object2 = null;
            }
            n = object2 != null ? (Integer)object2 : 0;
            SliderKt.Slider((float)f2, (Function1)object3, null, (boolean)false, (ClosedFloatingPointRange)object4, (int)n, null, (SliderColors)SliderDefaults.INSTANCE.colors-q0g_0yA(ColorKt.Color((long)4291728344L), ColorKt.Color((long)4292984551L), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, composer, 54, 6, 1020), null, (Composer)composer, (int)0, (int)332);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)"Completion Sound", null, (long)object.getTextPrimary-0d7_KjU(), (long)ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6), null, (FontWeight)FontWeight.Companion.getBold(), null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131026);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            object3 = BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)12)))), (long)Color.copy-wmQWz5c$default((long)object.getTextPrimary-0d7_KjU(), (float)0.05f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), null, (int)2, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            object2 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)Alignment.Companion.getTop(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object4 = composer.getCurrentCompositionLocalMap();
            object3 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object3);
            object52 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)object52);
            } else {
                composer.useNode();
            }
            object52 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)object52, (Object)object2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object52, (Object)object4, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object52.getInserting() || !Intrinsics.areEqual((Object)object52.rememberedValue(), (Object)n)) {
                object52.updateRememberedValue((Object)n);
                object52.apply((Object)n, (Function2)object2);
            }
            Updater.set-impl((Composer)object52, (Object)object3, (Function2)ComposeUiNode.Companion.getSetModifier());
            object2 = composer;
            ComposerKt.sourceInformationMarkerStart((Composer)object2, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            mutableState = (RowScope)RowScopeInstance.INSTANCE;
            object4 = object2;
            ComposerKt.sourceInformationMarkerStart((Composer)object4, (int)288223545, (String)"C:MainActivity.kt#to5c3");
            object52 = CollectionsKt.listOf((Object[])new String[]{"Beep", "Alarm", "Ring", "Custom"});
            object4.startReplaceGroup(424942506);
            ComposerKt.sourceInformation((Composer)object4, (String)"*1244@64155L688,1239@63814L1325");
            object52 = (Iterable)object52;
            n = 0;
            for (Object object52 : object52) {
                if (n < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                object7 = (String)object52;
                object52 = ClipKt.clip((Modifier)RowScope.weight$default((RowScope)mutableState, (Modifier)((Modifier)Modifier.Companion), (float)1.0f, (boolean)false, (int)2, null), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)12))));
                l = MainActivityKt.SettingsOverlay$lambda$210((MutableState<Integer>)mutableState2) == n ? ColorKt.Color((long)4294937216L) : Color.Companion.getTransparent-0d7_KjU();
                object8 = BackgroundKt.background-bw27NRU$default((Modifier)object52, (long)l, null, (int)2, null);
                ComposerKt.sourceInformationMarkerStart((Composer)object4, (int)-1460167235, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl2 = object4.changedInstance((Object)view);
                boolean bl3 = object4.changed(n);
                boolean bl4 = object4.changedInstance((Object)managedActivityResultLauncher);
                bl = object4.changedInstance((Object)context);
                object52 = object4.rememberedValue();
                if (bl2 | bl3 | bl4 | bl || object52 == Composer.Companion.getEmpty()) {
                    object52 = new MainActivityKt$$ExternalSyntheticLambda27(view, n, managedActivityResultLauncher, context, mutableState2);
                    object4.updateRememberedValue(object52);
                }
                object52 = (Function0)object52;
                ComposerKt.sourceInformationMarkerEnd((Composer)object4);
                object52 = PaddingKt.padding-VpY3zN4$default((Modifier)ClickableKt.clickable-XHw0xAI$default((Modifier)object8, (boolean)false, null, null, (Function0)object52, (int)7, null), (float)0.0f, (float)Dp.constructor-impl((float)10), (int)1, null);
                object8 = Alignment.Companion.getCenter();
                ComposerKt.sourceInformationMarkerStart((Composer)object4, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
                modifier = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)object8, (boolean)false);
                ComposerKt.sourceInformationMarkerStart((Composer)object4, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                int n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)object4, (int)0);
                object8 = object4.getCurrentCompositionLocalMap();
                object52 = ComposedModifierKt.materializeModifier((Composer)object4, (Modifier)object52);
                function0 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)object4, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(object4.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                object4.startReusableNode();
                if (object4.getInserting()) {
                    object4.createNode(function0);
                } else {
                    object4.useNode();
                }
                function0 = Updater.constructor-impl((Composer)object4);
                Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function0, (Object)object8, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                object8 = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n2)) {
                    function0.updateRememberedValue((Object)n2);
                    function0.apply((Object)n2, (Function2)object8);
                }
                Updater.set-impl((Composer)function0, (Object)object52, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)object4, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
                object52 = (BoxScope)BoxScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)object4, (int)-969389329, (String)"C1257@65114L8,1257@65008L129:MainActivity.kt#to5c3");
                l = MainActivityKt.SettingsOverlay$lambda$210((MutableState<Integer>)mutableState2) == n ? Color.Companion.getWhite-0d7_KjU() : object.getTextPrimary-0d7_KjU();
                TextKt.Text--4IGK_g((String)object7, null, (long)l, (long)ScaledSpKt.getScaledSp((int)11, (Composer)object4, (int)6), null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)1, (int)0, null, null, (Composer)object4, (int)0, (int)3072, (int)122866);
                ComposerKt.sourceInformationMarkerEnd((Composer)object4);
                ComposerKt.sourceInformationMarkerEnd((Composer)object4);
                object4.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)object4);
                ComposerKt.sourceInformationMarkerEnd((Composer)object4);
                ComposerKt.sourceInformationMarkerEnd((Composer)object4);
                ++n;
            }
            object4.endReplaceGroup();
            ComposerKt.sourceInformationMarkerEnd((Composer)object4);
            ComposerKt.sourceInformationMarkerEnd((Composer)object2);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
            n = (int)MainActivityKt.SettingsOverlay$lambda$213((MutableState<Float>)mutableState3);
            TextKt.Text--4IGK_g((String)("Completion Sound Duration: " + n + " sec"), null, (long)object.getTextPrimary-0d7_KjU(), (long)ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6), null, (FontWeight)FontWeight.Companion.getBold(), null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196608, (int)0, (int)131026);
            f = MainActivityKt.SettingsOverlay$lambda$213((MutableState<Float>)mutableState3);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-840984608, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changedInstance((Object)view);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda28(view, mutableState3);
                composer.updateRememberedValue(object);
            }
            object = (Function1)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SliderKt.Slider((float)f, (Function1)object, null, (boolean)false, (ClosedFloatingPointRange)RangesKt.rangeTo((float)2.0f, (float)20.0f), (int)0, null, (SliderColors)SliderDefaults.INSTANCE.colors-q0g_0yA(ColorKt.Color((long)4286698746L), ColorKt.Color((long)4289979900L), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, composer, 54, 6, 1020), null, (Composer)composer, (int)0, (int)364);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$252$lambda$251$lambda$250(MutableState object, MutableState mutableState, String charSequence) {
        Intrinsics.checkNotNullParameter((Object)charSequence, (String)"newVal");
        charSequence = charSequence;
        Appendable appendable = new StringBuilder();
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            char c = charSequence.charAt(i);
            if (!Character.isDigit(c)) continue;
            appendable.append(c);
        }
        charSequence = ((StringBuilder)appendable).toString();
        MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$244((MutableState<String>)object, (String)charSequence);
        object = StringsKt.toFloatOrNull((String)charSequence);
        if (object != null) {
            MainActivityKt.SettingsOverlay$lambda$205((MutableState<Float>)mutableState, RangesKt.coerceIn((float)((Number)object).floatValue(), (float)1.0f, (float)200.0f));
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$254$lambda$253(View view, MutableState mutableState, float f) {
        if ((int)MainActivityKt.SettingsOverlay$lambda$204((MutableState<Float>)mutableState) != (int)f) {
            try {
                view.performHapticFeedback(4);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        MainActivityKt.SettingsOverlay$lambda$205((MutableState<Float>)mutableState, f);
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$259$lambda$258$lambda$257(MutableState object, MutableState mutableState, String object2) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"newVal");
        CharSequence charSequence = (CharSequence)object2;
        object2 = new StringBuilder();
        int n = charSequence.length();
        for (int i = 0; i < n; ++i) {
            char c = charSequence.charAt(i);
            if (!Character.isDigit(c)) continue;
            object2.append(c);
        }
        object2 = ((StringBuilder)object2).toString();
        MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$247((MutableState<String>)object, (String)object2);
        object = StringsKt.toFloatOrNull((String)object2);
        if (object != null) {
            MainActivityKt.SettingsOverlay$lambda$208((MutableState<Float>)mutableState, RangesKt.coerceIn((float)((Number)object).floatValue(), (float)1.0f, (float)120.0f));
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$261$lambda$260(View view, MutableState mutableState, float f) {
        if ((int)MainActivityKt.SettingsOverlay$lambda$207((MutableState<Float>)mutableState) != (int)f) {
            try {
                view.performHapticFeedback(4);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        MainActivityKt.SettingsOverlay$lambda$208((MutableState<Float>)mutableState, f);
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$263$lambda$262(View view, AudioManager audioManager, MutableState mutableState, float f) {
        if ((int)MainActivityKt.SettingsOverlay$lambda$216((MutableState<Float>)mutableState) != (int)f) {
            try {
                view.performHapticFeedback(4);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        MainActivityKt.SettingsOverlay$lambda$217((MutableState<Float>)mutableState, f);
        audioManager.setStreamVolume(3, (int)f, 0);
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$269$lambda$268$lambda$266$lambda$265(View view, int n, ManagedActivityResultLauncher managedActivityResultLauncher, Context context, MutableState mutableState) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (n == 3) {
            managedActivityResultLauncher.launch((Object)new String[]{"audio/*"});
        } else {
            MainActivityKt.SettingsOverlay$lambda$211((MutableState<Integer>)mutableState, n);
            SoundPlayer.INSTANCE.playCompletion(context, n);
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$271$lambda$270(View view, MutableState mutableState, float f) {
        if ((int)MainActivityKt.SettingsOverlay$lambda$213((MutableState<Float>)mutableState) != (int)f) {
            try {
                view.performHapticFeedback(4);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        MainActivityKt.SettingsOverlay$lambda$214((MutableState<Float>)mutableState, f);
        return Unit.INSTANCE;
    }

    private static final boolean SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$275(State<Boolean> state) {
        return (Boolean)state.getValue();
    }

    private static final float SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$276(State<Float> state) {
        return ((Number)state.getValue()).floatValue();
    }

    private static final AppBlockerManager.AccessibilityServiceStatus SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$278(MutableState<AppBlockerManager.AccessibilityServiceStatus> mutableState) {
        return (AppBlockerManager.AccessibilityServiceStatus)((State)mutableState).getValue();
    }

    private static final void SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$279(MutableState<AppBlockerManager.AccessibilityServiceStatus> mutableState, AppBlockerManager.AccessibilityServiceStatus accessibilityServiceStatus) {
        mutableState.setValue((Object)accessibilityServiceStatus);
    }

    static final DisposableEffectResult SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282(LifecycleOwner lifecycleOwner, Context object, MutableState mutableState, DisposableEffectScope disposableEffectScope) {
        Intrinsics.checkNotNullParameter((Object)disposableEffectScope, (String)"$this$DisposableEffect");
        object = new MainActivityKt$$ExternalSyntheticLambda52((Context)object, mutableState);
        lifecycleOwner.getLifecycle().addObserver((LifecycleObserver)object);
        lifecycleOwner = (DisposableEffectResult)new SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282$$inlined$onDispose$1(lifecycleOwner, (LifecycleEventObserver)object);
        return lifecycleOwner;
    }

    static final void SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282$lambda$280(Context context, MutableState mutableState, LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        Intrinsics.checkNotNullParameter((Object)lifecycleOwner, (String)"<unused var>");
        Intrinsics.checkNotNullParameter((Object)event, (String)"event");
        if (event == Lifecycle.Event.ON_RESUME || event == Lifecycle.Event.ON_START) {
            MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$279((MutableState<AppBlockerManager.AccessibilityServiceStatus>)mutableState, AppBlockerManager.INSTANCE.getAccessibilityStatus(context));
        }
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$288(ThemeOption object, Context context, ColumnScope columnScope, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)columnScope, (String)"$this$Card");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1319@68731L2007:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1282906264, (int)n, (int)-1, (String)"com.example.SettingsOverlay.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1319)");
            }
            Modifier modifier = PaddingKt.padding-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            MeasurePolicy measurePolicy = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(6 >> 3 & 0xE | 6 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            columnScope = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)columnScope, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            columnScope = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)columnScope);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            columnScope = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1614772303, (String)"C1320@68804L501,1325@69334L40,1328@69692L8,1330@69816L8,1326@69403L451,1332@69883L41,1339@70344L79,1334@70003L283,1333@69953L759:MainActivity.kt#to5c3");
            columnScope = Alignment.Companion.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            modifier = (Modifier)Modifier.Companion;
            columnScope = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)columnScope, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            measurePolicy = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)columnScope, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            columnScope = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)columnScope);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            columnScope = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1680228832, (String)"C1321@68890L149,1322@69072L39,1323@69266L8,1323@69144L131:MainActivity.kt#to5c3");
            measurePolicy = SecurityKt.getSecurity((Icons.Rounded)Icons.Rounded.INSTANCE);
            long l = object.getPrimary-0d7_KjU();
            columnScope = (Modifier)Modifier.Companion;
            IconKt.Icon-ww6aTOc((ImageVector)measurePolicy, null, (Modifier)SizeKt.size-3ABfNKs((Modifier)columnScope, (float)Dp.constructor-impl((float)20)), (long)l, (Composer)composer, (int)432, (int)0);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            columnScope = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"Focus Shield needs one small permission", null, (long)object.getPrimary-0d7_KjU(), (long)ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6), null, (FontWeight)columnScope, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131026);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            long l2 = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
            long l3 = object.getTextPrimary-0d7_KjU();
            l = ScaledSpKt.getScaledSp((int)16, (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)"To stop selected distracting apps during a focus session, PomoPal uses Android's Accessibility Service. This lets PomoPal detect when a protected app is opened and show the mindful pause screen.", null, (long)l3, (long)l2, null, null, null, (long)0L, null, null, (long)l, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)130034);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)12)), (Composer)composer, (int)6);
            columnScope = ButtonDefaults.INSTANCE.buttonColors-ro_MJ88(object.getPrimary-0d7_KjU(), Color.Companion.getWhite-0d7_KjU(), 0L, 0L, composer, ButtonDefaults.$stable << 12 | 0x30, 12);
            measurePolicy = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            modifier = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)12));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-883336679, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl = composer.changedInstance((Object)context);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda41(context);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ButtonKt.Button((Function0)object, (Modifier)measurePolicy, (boolean)false, (Shape)((Shape)modifier), (ButtonColors)columnScope, null, null, null, null, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$-413131058$app(), (Composer)composer, (int)0x30000030, (int)484);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$288$lambda$287$lambda$286$lambda$285(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
            context.startActivity(intent);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$296(ThemeOption object, boolean bl, Context context, MutableState object2, ColumnScope object3, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object3, (String)"$this$Card");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1370@72839L2632:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1961863984, (int)n, (int)-1, (String)"com.example.SettingsOverlay.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1370)");
            }
            Modifier modifier = PaddingKt.padding-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            object3 = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(6 >> 3 & 0xE | 6 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)object3);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object3 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-183398376, (String)"C1371@72912L499,1376@73440L40,1379@73726L8,1381@73850L8,1377@73509L379,1383@73917L41,1384@73987L1458:MainActivity.kt#to5c3");
            object3 = Alignment.Companion.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            modifier = (Modifier)Modifier.Companion;
            object3 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object3, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            compositionLocalMap = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)object3);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object3 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)963290970, (String)"C1372@72998L109,1373@73140L39,1374@73372L8,1374@73212L169:MainActivity.kt#to5c3");
            modifier = LockKt.getLock((Icons.Filled)Icons.Filled.INSTANCE);
            long l = ColorKt.Color((long)4294278144L);
            object3 = (Modifier)Modifier.Companion;
            IconKt.Icon-ww6aTOc((ImageVector)modifier, null, (Modifier)SizeKt.size-3ABfNKs((Modifier)object3, (float)Dp.constructor-impl((float)20)), (long)l, (Composer)composer, (int)3504, (int)0);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            object3 = bl ? "Unable to verify Focus Shield" : "Focus Shield needs attention";
            modifier = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)object3, null, (long)ColorKt.Color((long)4294278144L), (long)ScaledSpKt.getScaledSp((int)14, (Composer)composer, (int)6), null, (FontWeight)modifier, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196992, (int)0, (int)131026);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
            long l2 = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
            l = object.getTextPrimary-0d7_KjU();
            long l3 = ScaledSpKt.getScaledSp((int)16, (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)"The accessibility service appears to be interrupted. Please check its status in Android Settings to ensure blocking works.", null, (long)l, (long)l2, null, null, null, (long)0L, null, null, (long)l3, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)130034);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)12)), (Composer)composer, (int)6);
            compositionLocalMap = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            object3 = (Arrangement.Horizontal)Arrangement.INSTANCE.spacedBy-0680j_4(Dp.constructor-impl((float)8));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            modifier = RowKt.rowMeasurePolicy((Arrangement.Horizontal)object3, (Alignment.Vertical)Alignment.Companion.getTop(), (Composer)composer, (int)(54 >> 3 & 0xE | 54 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object3 = composer.getCurrentCompositionLocalMap();
            compositionLocalMap = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)compositionLocalMap);
            function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)object3);
            }
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            modifier = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1525947444, (String)"C1386@74175L95,1388@74400L61,1385@74113L528,1398@75089L76,1393@74728L299,1392@74674L741:MainActivity.kt#to5c3");
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1988885984, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changedInstance((Object)context);
            object3 = composer.rememberedValue();
            if (!bl && object3 != Composer.Companion.getEmpty()) {
                object2 = object3;
            } else {
                object2 = new MainActivityKt$$ExternalSyntheticLambda114(context, (MutableState)object2);
                composer.updateRememberedValue(object2);
            }
            object2 = (Function0)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ButtonKt.OutlinedButton((Function0)object2, (Modifier)RowScope.weight$default((RowScope)modifier, (Modifier)((Modifier)Modifier.Companion), (float)1.0f, (boolean)false, (int)2, null), (boolean)false, null, (ButtonColors)ButtonDefaults.INSTANCE.outlinedButtonColors-ro_MJ88(0L, object.getTextPrimary-0d7_KjU(), 0L, 0L, composer, ButtonDefaults.$stable << 12, 13), null, null, null, null, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$-477671949$app(), (Composer)composer, (int)0x30000000, (int)492);
            object2 = ButtonDefaults.INSTANCE.buttonColors-ro_MJ88(ColorKt.Color((long)4294278144L), Color.Companion.getWhite-0d7_KjU(), 0L, 0L, composer, ButtonDefaults.$stable << 12 | 0x36, 12);
            object3 = RowScope.weight$default((RowScope)modifier, (Modifier)((Modifier)Modifier.Companion), (float)1.0f, (boolean)false, (int)2, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1988868084, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl = composer.changedInstance((Object)context);
            object = composer.rememberedValue();
            if (bl || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda115(context);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ButtonKt.Button((Function0)object, (Modifier)object3, (boolean)false, null, (ButtonColors)object2, null, null, null, null, (Function3)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$-1764641295$app(), (Composer)composer, (int)0x30000000, (int)492);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$296$lambda$295$lambda$294$lambda$291$lambda$290(Context context, MutableState mutableState) {
        MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$279((MutableState<AppBlockerManager.AccessibilityServiceStatus>)mutableState, AppBlockerManager.INSTANCE.getAccessibilityStatus(context));
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$296$lambda$295$lambda$294$lambda$293$lambda$292(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
            context.startActivity(intent);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$298$lambda$297(View view, MutableState mutableState) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        MainActivityKt.SettingsOverlay$lambda$198((MutableState<Boolean>)mutableState, true);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$301(ThemeOption themeOption, ColumnScope columnScope, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)columnScope, (String)"$this$Card");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1422@76354L733:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1405418896, (int)n, (int)-1, (String)"com.example.SettingsOverlay.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1422)");
            }
            columnScope = PaddingKt.padding-3ABfNKs((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)Dp.constructor-impl((float)16));
            Alignment.Vertical vertical = Alignment.Companion.getCenterVertically();
            Arrangement.HorizontalOrVertical horizontalOrVertical = Arrangement.INSTANCE.getSpaceBetween();
            horizontalOrVertical = (Arrangement.Horizontal)horizontalOrVertical;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            horizontalOrVertical = RowKt.rowMeasurePolicy((Arrangement.Horizontal)horizontalOrVertical, (Alignment.Vertical)vertical, (Composer)composer, (int)(438 >> 3 & 0xE | 438 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            vertical = composer.getCurrentCompositionLocalMap();
            columnScope = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)columnScope);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)horizontalOrVertical, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)vertical, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            vertical = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)vertical);
            }
            Updater.set-impl((Composer)function0, (Object)columnScope, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            columnScope = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-641150527, (String)"C1427@76607L318,1431@76946L123:MainActivity.kt#to5c3");
            horizontalOrVertical = RowScope.weight$default((RowScope)columnScope, (Modifier)((Modifier)Modifier.Companion), (float)1.0f, (boolean)false, (int)2, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            columnScope = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            vertical = composer.getCurrentCompositionLocalMap();
            horizontalOrVertical = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)horizontalOrVertical);
            function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)columnScope, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)vertical, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            columnScope = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)columnScope);
            }
            Updater.set-impl((Composer)function0, (Object)horizontalOrVertical, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            columnScope = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-787131118, (String)"C1428@76672L83,1429@76858L8,1429@76780L123:MainActivity.kt#to5c3");
            columnScope = FontWeight.Companion.getBold();
            TextKt.Text--4IGK_g((String)"App Blocker", null, (long)themeOption.getTextPrimary-0d7_KjU(), (long)0L, null, (FontWeight)columnScope, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)131034);
            long l = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)"Select distracting apps to block during focus sessions.", null, (long)themeOption.getTextSecondary-0d7_KjU(), (long)l, null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)131058);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            IconKt.Icon-ww6aTOc((ImageVector)SettingsKt.getSettings((Icons.Filled)Icons.Filled.INSTANCE), (String)"Manage", null, (long)themeOption.getTextPrimary-0d7_KjU(), (Composer)composer, (int)48, (int)4);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit SettingsOverlay$lambda$307(Function0 function0, int n, int n2, int n3, Composer composer, int n4) {
        MainActivityKt.SettingsOverlay((Function0<Unit>)function0, n, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n2 | 1)), n3);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void SlidingTimer-KmRG4DE(int n, long l, @Nullable Composer object, int n2, int n3) {
        int n4;
        Composer composer = object.startRestartGroup(1282651204);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(SlidingTimer)P(1,0:c#ui.unit.TextUnit)1967@100266L7,1969@100358L2497:MainActivity.kt#to5c3");
        int n5 = n4 = n2;
        if ((n2 & 6) == 0) {
            n5 = composer.changed(n) ? 4 : 2;
            n5 = n4 | n5;
        }
        if ((n2 & 0x30) == 0) {
            n4 = (n3 & 2) == 0 && composer.changed(l) ? 32 : 16;
            n4 = n5 | n4;
        } else {
            n4 = n5;
        }
        if ((n4 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            composer.startDefaults();
            ComposerKt.sourceInformation((Composer)composer, (String)"1966@100194L8");
            if ((n2 & 1) != 0 && !composer.getDefaultsInvalid()) {
                composer.skipToGroupEnd();
                n5 = n4;
                if ((n3 & 2) != 0) {
                    n5 = n4 & 0xFFFFFF8F;
                }
            } else {
                n5 = n4;
                if ((n3 & 2) != 0) {
                    l = ScaledSpKt.getScaledSp((int)64, (Composer)composer, (int)6);
                    n5 = n4 & 0xFFFFFF8F;
                }
            }
            composer.endDefaults();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)1282651204, (int)n5, (int)-1, (String)"com.example.SlidingTimer (MainActivity.kt:1966)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            String string = "CC:CompositionLocal.kt#9igjgp";
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = (Context)object;
            CharSequence charSequence = String.format("%02d:%02d", Arrays.copyOf(new Object[]{n / 60, n % 60}, 2));
            Intrinsics.checkNotNullExpressionValue((Object)charSequence, (String)"format(...)");
            object = (Arrangement.Horizontal)Arrangement.INSTANCE.getCenter();
            Alignment.Vertical vertical = Alignment.Companion.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            Modifier modifier = (Modifier)Modifier.Companion;
            object = RowKt.rowMeasurePolicy((Arrangement.Horizontal)object, (Alignment.Vertical)vertical, (Composer)composer, (int)(432 >> 3 & 0xE | 432 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n4 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            vertical = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            Object object2 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)object2, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)object2, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            Object object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (object2.getInserting() || !Intrinsics.areEqual((Object)object2.rememberedValue(), (Object)n4)) {
                object2.updateRememberedValue((Object)n4);
                object2.apply((Object)n4, object3);
            }
            Updater.set-impl((Composer)object2, (Object)vertical, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            vertical = (RowScope)RowScopeInstance.INSTANCE;
            vertical = composer;
            ComposerKt.sourceInformationMarkerStart((Composer)vertical, (int)2062202432, (String)"C:MainActivity.kt#to5c3");
            vertical.startReplaceGroup(66523000);
            ComposerKt.sourceInformation((Composer)vertical, (String)"");
            charSequence = charSequence;
            boolean bl = false;
            n4 = 0;
            int n6 = 0;
            while (n6 < charSequence.length()) {
                char c = charSequence.charAt(n6);
                if (c == ':') {
                    vertical.startReplaceGroup(1115609937);
                    ComposerKt.sourceInformation((Composer)vertical, (String)"1979@100752L7,1980@100803L7,1975@100576L328");
                    object2 = FontWeight.Companion.getBlack();
                    object3 = (CompositionLocal)TypographyKt.getLocalAppFont();
                    ComposerKt.sourceInformationMarkerStart((Composer)vertical, (int)2023513938, (String)string);
                    object3 = vertical.consume((CompositionLocal)object3);
                    ComposerKt.sourceInformationMarkerEnd((Composer)vertical);
                    object3 = (FontFamily)object3;
                    Object object4 = (CompositionLocal)ThemeKt.getLocalAppTheme();
                    ComposerKt.sourceInformationMarkerStart((Composer)vertical, (int)2023513938, (String)string);
                    object4 = vertical.consume(object4);
                    ComposerKt.sourceInformationMarkerEnd((Composer)vertical);
                    long l2 = ((ThemeOption)object4).getTextPrimary-0d7_KjU();
                    object4 = PaddingKt.padding-qDBjuR0$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (float)0.0f, (float)0.0f, (float)Dp.constructor-impl((float)6), (int)7, null);
                    TextKt.Text--4IGK_g((String)":", (Modifier)object4, (long)l2, (long)l, null, (FontWeight)object2, (FontFamily)object3, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)vertical, (int)(n5 << 6 & 0x1C00 | 0x30036), (int)0, (int)130960);
                    vertical.endReplaceGroup();
                } else {
                    vertical.startReplaceGroup(1116021214);
                    ComposerKt.sourceInformation((Composer)vertical, (String)"1986@101036L1355,2003@102463L362,1984@100942L1883");
                    ComposerKt.sourceInformationMarkerStart((Composer)vertical, (int)-241090956, (String)"CC(remember):MainActivity.kt#9igjgp");
                    object2 = vertical.rememberedValue();
                    if (object2 == Composer.Companion.getEmpty()) {
                        object2 = new MainActivityKt$$ExternalSyntheticLambda167();
                        vertical.updateRememberedValue(object2);
                    }
                    object2 = (Function1)object2;
                    ComposerKt.sourceInformationMarkerEnd((Composer)vertical);
                    AnimatedContentKt.AnimatedContent((Object)Character.valueOf(c), null, (Function1)object2, null, (String)("digitAnimation_" + n4), null, (Function4)((Function4)ComposableLambdaKt.rememberComposableLambda((int)-448888324, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda168(l), (Composer)vertical, (int)54)), (Composer)vertical, (int)0x180180, (int)42);
                    vertical.endReplaceGroup();
                }
                ++n6;
                ++n4;
            }
            vertical.endReplaceGroup();
            ComposerKt.sourceInformationMarkerEnd((Composer)vertical);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda169(n, l, n2, n3));
        }
    }

    static final ContentTransform SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404(AnimatedContentTransitionScope animatedContentTransitionScope) {
        Intrinsics.checkNotNullParameter((Object)animatedContentTransitionScope, (String)"$this$AnimatedContent");
        SpringSpec springSpec = AnimationSpecKt.spring$default((float)0.5f, (float)200.0f, null, (int)4, null);
        SpringSpec springSpec2 = AnimationSpecKt.spring$default((float)1.0f, (float)1500.0f, null, (int)4, null);
        SpringSpec springSpec3 = AnimationSpecKt.spring$default((float)0.0f, (float)200.0f, null, (int)5, null);
        SpringSpec springSpec4 = AnimationSpecKt.spring$default((float)0.0f, (float)1500.0f, null, (int)5, null);
        springSpec = !(Intrinsics.compare((int)((Character)animatedContentTransitionScope.getTargetState()).charValue(), (int)((Character)animatedContentTransitionScope.getInitialState()).charValue()) < 0 || ((Character)animatedContentTransitionScope.getInitialState()).charValue() == '0' && ((Character)animatedContentTransitionScope.getTargetState()).charValue() == '9' || ((Character)animatedContentTransitionScope.getInitialState()).charValue() == '0' && ((Character)animatedContentTransitionScope.getTargetState()).charValue() == '5') ? AnimatedContentKt.togetherWith((EnterTransition)EnterExitTransitionKt.slideInVertically((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec), (Function1)new MainActivityKt$$ExternalSyntheticLambda109()).plus(EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec3), (float)0.0f, (int)2, null)), (ExitTransition)EnterExitTransitionKt.slideOutVertically((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec2), (Function1)new MainActivityKt$$ExternalSyntheticLambda110()).plus(EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec4), (float)0.0f, (int)2, null))) : AnimatedContentKt.togetherWith((EnterTransition)EnterExitTransitionKt.slideInVertically((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec), (Function1)new MainActivityKt$$ExternalSyntheticLambda107()).plus(EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec3), (float)0.0f, (int)2, null)), (ExitTransition)EnterExitTransitionKt.slideOutVertically((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec2), (Function1)new MainActivityKt$$ExternalSyntheticLambda108()).plus(EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec)((FiniteAnimationSpec)springSpec4), (float)0.0f, (int)2, null)));
        return animatedContentTransitionScope.using((ContentTransform)springSpec, AnimatedContentKt.SizeTransform$default((boolean)false, null, (int)2, null));
    }

    static final int SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$400(int n) {
        return -n;
    }

    static final int SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$401(int n) {
        return n;
    }

    static final int SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$402(int n) {
        return n;
    }

    static final int SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$403(int n) {
        return -n;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$406(long l, AnimatedContentScope animatedContentScope, char c, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)animatedContentScope, (String)"$this$AnimatedContent");
        ComposerKt.sourceInformation((Composer)composer, (String)"C2008@102711L7,2009@102766L7,2004@102500L307:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-448888324, (int)n, (int)-1, (String)"com.example.SlidingTimer.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:2004)");
        }
        animatedContentScope = FontWeight.Companion.getBlack();
        Object object = (CompositionLocal)TypographyKt.getLocalAppFont();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
        object = composer.consume(object);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        object = (FontFamily)object;
        Object object2 = (CompositionLocal)ThemeKt.getLocalAppTheme();
        ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
        object2 = composer.consume(object2);
        ComposerKt.sourceInformationMarkerEnd((Composer)composer);
        long l2 = ((ThemeOption)object2).getTextPrimary-0d7_KjU();
        TextKt.Text--4IGK_g((String)String.valueOf(c), null, (long)l2, (long)l, null, (FontWeight)animatedContentScope, (FontFamily)object, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196608, (int)0, (int)130962);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit SlidingTimer_KmRG4DE$lambda$409(int n, long l, int n2, int n3, Composer composer, int n4) {
        MainActivityKt.SlidingTimer-KmRG4DE(n, l, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n2 | 1)), n3);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void TaskItemRow(@NotNull TaskItem taskItem, boolean bl, @NotNull Function1<? super TaskItem, Unit> function1, @NotNull Function0<Unit> function0, @Nullable Modifier modifier, @Nullable Composer object, int n, int n2) {
        int n3;
        int n4;
        int n5;
        Intrinsics.checkNotNullParameter((Object)taskItem, (String)"task");
        Intrinsics.checkNotNullParameter(function1, (String)"onSelect");
        Intrinsics.checkNotNullParameter(function0, (String)"onDelete");
        Composer composer = object.startRestartGroup(-1073152619);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(TaskItemRow)P(4!1,3,2)1451@77765L7,1452@77827L7,1453@77872L7,1454@77915L7,1456@77978L160,1460@78194L109,1473@78617L43,1479@78970L208,1485@79213L38,1486@79258L2917,1471@78538L3637:MainActivity.kt#to5c3");
        int n6 = n5 = n;
        if ((n & 6) == 0) {
            n6 = composer.changed((Object)taskItem) ? 4 : 2;
            n6 = n5 | n6;
        }
        if ((n4 = n2 & 2) != 0) {
            n6 |= 0x30;
        } else if ((n & 0x30) == 0) {
            n5 = composer.changed(bl) ? 32 : 16;
            n6 |= n5;
        }
        n5 = n6;
        if ((n & 0x180) == 0) {
            n5 = composer.changedInstance(function1) ? 256 : 128;
            n5 = n6 | n5;
        }
        n6 = n5;
        if ((n & 0xC00) == 0) {
            n6 = composer.changedInstance(function0) ? 2048 : 1024;
            n6 = n5 | n6;
        }
        if ((n3 = n2 & 0x10) != 0) {
            n6 |= 0x6000;
        } else if ((n & 0x6000) == 0) {
            n5 = composer.changed((Object)modifier) ? 16384 : 8192;
            n6 |= n5;
        }
        if ((n6 & 0x2493) == 9362 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (n4 != 0) {
                bl = false;
            }
            if (n3 != 0) {
                modifier = (Modifier)Modifier.Companion;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1073152619, (int)n6, (int)-1, (String)"com.example.TaskItemRow (MainActivity.kt:1450)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = (Context)object;
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalView();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            View view = (View)object;
            object = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ThemeOption themeOption = (ThemeOption)object;
            object = (CompositionLocal)TypographyKt.getLocalAppFont();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            FontFamily fontFamily = (FontFamily)object;
            long l = bl ? Color.constructor-impl((long)ULong.constructor-impl((long)taskItem.getCategoryColor())) : themeOption.getCardBorder-0d7_KjU();
            object = SingleValueAnimationKt.animateColorAsState-euL9pac((long)l, null, (String)"borderColor", null, (Composer)composer, (int)384, (int)10);
            float f = bl ? Dp.constructor-impl((float)((float)1.5)) : Dp.constructor-impl((float)1.0f);
            State state = AnimateAsStateKt.animateDpAsState-AjpBEmI((float)f, null, (String)"borderWidth", null, (Composer)composer, (int)384, (int)10);
            l = bl ? ColorKt.compositeOver--OWjLjI((long)Color.copy-wmQWz5c$default((long)Color.constructor-impl((long)ULong.constructor-impl((long)taskItem.getCategoryColor())), (float)0.05f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), (long)themeOption.getSurface-0d7_KjU()) : themeOption.getSurface-0d7_KjU();
            RoundedCornerShape roundedCornerShape = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24));
            CardColors cardColors = CardDefaults.INSTANCE.cardColors-ro_MJ88(l, 0L, 0L, 0L, composer, CardDefaults.$stable << 12, 14);
            state = ClipKt.clip((Modifier)BorderKt.border-xT4_qwU((Modifier)ShadowKt.shadow-s4CzXII$default((Modifier)SizeKt.fillMaxWidth$default((Modifier)modifier, (float)0.0f, (int)1, null), (float)Dp.constructor-impl((float)3), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24))), (boolean)false, (long)0L, (long)themeOption.getShadowColor-0d7_KjU(), (int)12, null), (float)MainActivityKt.TaskItemRow$lambda$309((State<Dp>)state), (long)MainActivityKt.TaskItemRow$lambda$308((State<Color>)object), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24)))), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)24))));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-507468987, (String)"CC(remember):MainActivity.kt#9igjgp");
            int n7 = composer.changedInstance((Object)view);
            n4 = 0;
            n5 = (n6 & 0x380) == 256 ? 1 : 0;
            if ((n6 & 0xE) == 4) {
                n4 = 1;
            }
            object = composer.rememberedValue();
            if ((n5 | n7 | n4) != 0 || object == Composer.Companion.getEmpty()) {
                object = new MainActivityKt$$ExternalSyntheticLambda80(view, function1, taskItem);
                composer.updateRememberedValue(object);
            }
            object = (Function0)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = ClickableKt.clickable-XHw0xAI$default((Modifier)state, (boolean)false, null, null, (Function0)object, (int)7, null);
            state = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl((float)0.0f), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, composer, CardDefaults.$stable << 18 | 6, 62);
            roundedCornerShape = (Shape)roundedCornerShape;
            CardKt.Card((Modifier)object, (Shape)roundedCornerShape, (CardColors)cardColors, (CardElevation)state, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-1725603449, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda81(taskItem, bl, view, function0, themeOption, fontFamily), (Composer)composer, (int)54)), (Composer)composer, (int)196608, (int)16);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda82(taskItem, bl, function1, function0, modifier, n, n2));
        }
    }

    private static final long TaskItemRow$lambda$308(State<Color> state) {
        return ((Color)state.getValue()).unbox-impl();
    }

    private static final float TaskItemRow$lambda$309(State<Dp> state) {
        return ((Dp)state.getValue()).unbox-impl();
    }

    static final Unit TaskItemRow$lambda$311$lambda$310(View view, Function1 function1, TaskItem taskItem) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        function1.invoke((Object)taskItem);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit TaskItemRow$lambda$319(TaskItem object, boolean bl, View view, Function0 function0, ThemeOption themeOption, FontFamily fontFamily, ColumnScope object2, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$Card");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1490@79365L8,1487@79268L2901:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1725603449, (int)n, (int)-1, (String)"com.example.TaskItemRow.<anonymous> (MainActivity.kt:1487)");
            }
            Object object3 = PaddingKt.padding-3ABfNKs((Modifier)SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null), (float)ScaledDpKt.getScaledDp((int)16, (Composer)composer, (int)6));
            object2 = Alignment.Companion.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            MeasurePolicy measurePolicy = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object2, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object2 = composer.getCurrentCompositionLocalMap();
            object3 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object3);
            Function0 function02 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function02);
            } else {
                composer.useNode();
            }
            function02 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function02, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                function02.updateRememberedValue((Object)n);
                function02.apply((Object)n, (Function2)object2);
            }
            Updater.set-impl((Composer)function02, (Object)object3, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object2 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)709977964, (String)"C1495@79529L8,1493@79459L201,1499@79709L8,1499@79673L46,1500@79732L560,1535@81431L284,1531@81159L556:MainActivity.kt#to5c3");
            BoxKt.Box((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)ScaledDpKt.getScaledDp((int)16, (Composer)composer, (int)6)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)Color.constructor-impl((long)ULong.constructor-impl((long)object.getCategoryColor())), null, (int)2, null), (Composer)composer, (int)0);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)ScaledDpKt.getScaledDp((int)12, (Composer)composer, (int)6)), (Composer)composer, (int)0);
            measurePolicy = RowScope.weight$default((RowScope)object2, (Modifier)((Modifier)Modifier.Companion), (float)1.0f, (boolean)false, (int)2, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            object3 = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object2 = composer.getCurrentCompositionLocalMap();
            measurePolicy = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)measurePolicy);
            function02 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function02);
            } else {
                composer.useNode();
            }
            function02 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function02, (Object)object3, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                function02.updateRememberedValue((Object)n);
                function02.apply((Object)n, (Function2)object2);
            }
            Updater.set-impl((Composer)function02, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object2 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1621967993, (String)"C1506@80019L8,1501@79789L256,1510@80149L8,1508@80062L216:MainActivity.kt#to5c3");
            object2 = object.getName();
            object3 = FontWeight.Companion.getBold();
            long l = themeOption.getTextPrimary-0d7_KjU();
            long l2 = ScaledSpKt.getScaledSp((int)15, (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)object2, null, (long)l, (long)l2, null, (FontWeight)object3, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196608, (int)0, (int)130962);
            object2 = object.getCategoryName();
            l = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
            l2 = themeOption.getTextSecondary-0d7_KjU();
            TextKt.Text--4IGK_g((String)object2, null, (long)l2, (long)l, null, null, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)0, (int)0, (int)130994);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (object.getCompletedPomodoros() > 0) {
                composer.startReplaceGroup(710790039);
                ComposerKt.sourceInformation((Composer)composer, (String)"1516@80356L673,1528@81082L8,1528@81046L46");
                object3 = Alignment.Companion.getCenterVertically();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
                object2 = (Modifier)Modifier.Companion;
                measurePolicy = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object3, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                object3 = composer.getCurrentCompositionLocalMap();
                object2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object2);
                function02 = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode(function02);
                } else {
                    composer.useNode();
                }
                function02 = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)function02, (Object)measurePolicy, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)function02, (Object)object3, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (function02.getInserting() || !Intrinsics.areEqual((Object)function02.rememberedValue(), (Object)n)) {
                    function02.updateRememberedValue((Object)n);
                    function02.apply((Object)n, (Function2)object3);
                }
                Updater.set-impl((Composer)function02, (Object)object2, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
                object3 = (RowScope)RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)808459636, (String)"C:MainActivity.kt#to5c3");
                if (object.getCompletedPomodoros() <= 5) {
                    composer.startReplaceGroup(808482730);
                    ComposerKt.sourceInformation((Composer)composer, (String)"*1519@80577L8,1519@80552L34,1520@80615L39");
                    n = object.getCompletedPomodoros();
                    themeOption = object2;
                    for (int i = 0; i < n; ++i) {
                        TextKt.Text--4IGK_g((String)"\ud83c\udf45", null, (long)0L, (long)ScaledSpKt.getScaledSp((int)16, (Composer)composer, (int)6), null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)131062);
                        SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)2)), (Composer)composer, (int)6);
                    }
                    composer.endReplaceGroup();
                } else {
                    composer.startReplaceGroup(808726793);
                    ComposerKt.sourceInformation((Composer)composer, (String)"1523@80759L8,1523@80734L34,1524@80793L39,1525@80857L132");
                    TextKt.Text--4IGK_g((String)"\ud83c\udf45", null, (long)0L, (long)ScaledSpKt.getScaledSp((int)16, (Composer)composer, (int)6), null, null, null, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)6, (int)0, (int)131062);
                    SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)4)), (Composer)composer, (int)6);
                    n = object.getCompletedPomodoros();
                    object3 = "x" + n;
                    object2 = FontWeight.Companion.getBold();
                    TextKt.Text--4IGK_g((String)object3, null, (long)themeOption.getTextPrimary-0d7_KjU(), (long)0L, null, (FontWeight)object2, (FontFamily)fontFamily, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196608, (int)0, (int)130970);
                    composer.endReplaceGroup();
                }
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)ScaledDpKt.getScaledDp((int)16, (Composer)composer, (int)6)), (Composer)composer, (int)0);
            } else {
                composer.startReplaceGroup(631069943);
            }
            composer.endReplaceGroup();
            AnimatedVisibilityKt.AnimatedVisibility((boolean)bl, null, (EnterTransition)EnterExitTransitionKt.scaleIn-L8ZKh-E$default(null, (float)0.0f, (long)0L, (int)7, null).plus(EnterExitTransitionKt.fadeIn$default(null, (float)0.0f, (int)3, null)), (ExitTransition)EnterExitTransitionKt.scaleOut-L8ZKh-E$default(null, (float)0.0f, (long)0L, (int)7, null).plus(EnterExitTransitionKt.fadeOut$default(null, (float)0.0f, (int)3, null)), null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-941904701, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda129((TaskItem)object), (Composer)composer, (int)54)), (Composer)composer, (int)200064, (int)18);
            if (!bl) {
                composer.startReplaceGroup(712187302);
                ComposerKt.sourceInformation((Composer)composer, (String)"1545@81797L224,1545@81776L369");
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)161522187, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl = composer.changedInstance((Object)view);
                boolean bl2 = composer.changed((Object)function0);
                object = composer.rememberedValue();
                if (bl | bl2 || object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda130(view, function0);
                    composer.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                IconButtonKt.IconButton((Function0)object, null, (boolean)false, null, null, (Function2)ComposableSingletons.MainActivityKt.INSTANCE.getLambda$1519979098$app(), (Composer)composer, (int)196608, (int)30);
            } else {
                composer.startReplaceGroup(631069943);
            }
            composer.endReplaceGroup();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit TaskItemRow$lambda$319$lambda$318$lambda$315(TaskItem taskItem, AnimatedVisibilityScope animatedVisibilityScope, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)animatedVisibilityScope, (String)"$this$AnimatedVisibility");
        ComposerKt.sourceInformation((Composer)composer, (String)"C1536@81449L252:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart((int)-941904701, (int)n, (int)-1, (String)"com.example.TaskItemRow.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1536)");
        }
        animatedVisibilityScope = CheckCircleKt.getCheckCircle((Icons.Filled)Icons.Filled.INSTANCE);
        long l = Color.constructor-impl((long)ULong.constructor-impl((long)taskItem.getCategoryColor()));
        taskItem = SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)24));
        IconKt.Icon-ww6aTOc((ImageVector)animatedVisibilityScope, (String)"Selected", (Modifier)taskItem, (long)l, (Composer)composer, (int)432, (int)0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    static final Unit TaskItemRow$lambda$319$lambda$318$lambda$317$lambda$316(View view, Function0 function0) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        function0.invoke();
        return Unit.INSTANCE;
    }

    static final Unit TaskItemRow$lambda$320(TaskItem taskItem, boolean bl, Function1 function1, Function0 function0, Modifier modifier, int n, int n2, Composer composer, int n3) {
        MainActivityKt.TaskItemRow(taskItem, bl, (Function1<? super TaskItem, Unit>)function1, (Function0<Unit>)function0, modifier, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n | 1)), n2);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    public static final void TimerDisplay-egy_3UM(int n, int n2, @NotNull TimerManager.TimerState timerState, boolean bl, @NotNull String string, @Nullable TaskItem taskItem, @NotNull Context context, float f, @Nullable Composer object, int n3, int n4) {
        int n5;
        int n6;
        Intrinsics.checkNotNullParameter((Object)timerState, (String)"state");
        Intrinsics.checkNotNullParameter((Object)string, (String)"taskName");
        Intrinsics.checkNotNullParameter((Object)context, (String)"context");
        Composer composer = object.startRestartGroup(-561763422);
        ComposerKt.sourceInformation((Composer)composer, (String)"C(TimerDisplay)P(6,7,4,3,5,2,1,0:c#ui.unit.Dp)2028@103224L7,2029@103286L7,2030@103331L7,2031@103363L31,2034@103464L143,2034@103439L168,2043@103728L27,2045@103790L104,2045@103765L129,2049@103900L8932:MainActivity.kt#to5c3");
        int n7 = n6 = n3;
        if ((n3 & 6) == 0) {
            n7 = composer.changed(n) ? 4 : 2;
            n7 = n6 | n7;
        }
        n6 = n7;
        if ((n3 & 0x30) == 0) {
            n6 = composer.changed(n2) ? 32 : 16;
            n6 = n7 | n6;
        }
        n7 = n6;
        if ((n3 & 0x180) == 0) {
            n7 = composer.changed(((Enum)timerState).ordinal()) ? 256 : 128;
            n7 = n6 | n7;
        }
        n6 = n7;
        if ((n3 & 0xC00) == 0) {
            n6 = composer.changed(bl) ? 2048 : 1024;
            n6 = n7 | n6;
        }
        n7 = n6;
        if ((n3 & 0x6000) == 0) {
            n7 = composer.changed((Object)string) ? 16384 : 8192;
            n7 = n6 | n7;
        }
        if ((n5 = n4 & 0x20) != 0) {
            n6 = n7 | 0x30000;
        } else if ((0x30000 & n3) == 0) {
            n6 = composer.changed((Object)taskItem) ? 131072 : 65536;
            n6 = n7 | n6;
        } else {
            n6 = n7;
        }
        int n8 = n4 & 0x80;
        if (n8 != 0) {
            n6 |= 0xC00000;
        } else if ((n3 & 0xC00000) == 0) {
            n7 = composer.changed(f) ? 0x800000 : 0x400000;
            n6 |= n7;
        }
        if ((n6 & 0x412493) == 4269202 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (n5 != 0) {
                taskItem = null;
            }
            if (n8 != 0) {
                f = Dp.constructor-impl((float)260);
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-561763422, (int)n6, (int)-1, (String)"com.example.TimerDisplay (MainActivity.kt:2027)");
            }
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Context context2 = (Context)object;
            object = (CompositionLocal)AndroidCompositionLocals_androidKt.getLocalView();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            Object object2 = (View)object;
            object = (CompositionLocal)ThemeKt.getLocalAppTheme();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object = composer.consume((CompositionLocal)object);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ThemeOption themeOption = (ThemeOption)object;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1311246913, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = SnapshotStateKt.mutableStateOf$default((Object)"", null, (int)2, null);
                composer.updateRememberedValue(object);
            }
            MutableState mutableState = (MutableState)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1311250257, (String)"CC(remember):MainActivity.kt#9igjgp");
            n7 = (n6 & 0xE000) == 16384 ? 1 : 0;
            object = composer.rememberedValue();
            if (n7 != 0 || object == Composer.Companion.getEmpty()) {
                object = (Function2)new TimerDisplay.1.1(string, mutableState, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)string, (Function2)object, (Composer)composer, (int)(n6 >> 12 & 0xE));
            float f2 = n2 > 0 ? (float)n / (float)n2 : 0.0f;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1311258589, (String)"CC(remember):MainActivity.kt#9igjgp");
            object = composer.rememberedValue();
            if (object == Composer.Companion.getEmpty()) {
                object = AnimatableKt.Animatable$default((float)1.0f, (float)0.0f, (int)2, null);
                composer.updateRememberedValue(object);
            }
            mutableState = (Animatable)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1311260650, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl2 = composer.changedInstance((Object)mutableState);
            boolean bl3 = composer.changed(f2);
            object = composer.rememberedValue();
            if (bl2 | bl3 || object == Composer.Companion.getEmpty()) {
                object = (Function2)new TimerDisplay.2.1((Animatable)mutableState, f2, null);
                composer.updateRememberedValue(object);
            }
            object = (Function2)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            EffectsKt.LaunchedEffect((Object)Float.valueOf(f2), (Function2)object, (Composer)composer, (int)0);
            object = Alignment.Companion.getCenterHorizontally();
            Modifier modifier = SizeKt.fillMaxWidth$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            object = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)object, (Composer)composer, (int)(390 >> 3 & 0xE | 390 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n6 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            CompositionLocalMap compositionLocalMap = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)compositionLocalMap, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n6)) {
                function0.updateRememberedValue((Object)n6);
                function0.apply((Object)n6, (Function2)object);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1932005886, (String)"C2058@104350L274,2065@104721L3941,2050@104005L4657,2132@108680L113:MainActivity.kt#to5c3");
            compositionLocalMap = Alignment.Companion.getCenter();
            modifier = ClipKt.clip((Modifier)ShadowKt.shadow-s4CzXII$default((Modifier)AspectRatioKt.aspectRatio$default((Modifier)SizeKt.fillMaxWidth((Modifier)((Modifier)Modifier.Companion), (float)0.65f), (float)1.0f, (boolean)false, (int)2, null), (float)Dp.constructor-impl((float)10), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()), (boolean)false, (long)0L, (long)themeOption.getShadowColor-0d7_KjU(), (int)12, null), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-477962274, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl2 = composer.changed((Object)themeOption);
            object = composer.rememberedValue();
            if (bl2 || object == Composer.Companion.getEmpty()) {
                object = Brush.Companion.linearGradient-mHitzGk$default((Brush.Companion)Brush.Companion, (List)CollectionsKt.listOf((Object[])new Color[]{Color.box-impl((long)Color.copy-wmQWz5c$default((long)themeOption.getSurface-0d7_KjU(), (float)0.95f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null)), Color.box-impl((long)Color.copy-wmQWz5c$default((long)themeOption.getSurface-0d7_KjU(), (float)0.7f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null))}), (long)0L, (long)0L, (int)0, (int)14, null);
                composer.updateRememberedValue(object);
            }
            object = (Brush)object;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object = BorderKt.border-xT4_qwU((Modifier)BackgroundKt.background$default((Modifier)modifier, (Brush)object, null, (float)0.0f, (int)6, null), (float)Dp.constructor-impl((float)4), (long)themeOption.getCardBorder-0d7_KjU(), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape()));
            BoxWithConstraintsKt.BoxWithConstraints((Modifier)object, (Alignment)compositionLocalMap, (boolean)false, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-598479786, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda43(themeOption, bl, (Animatable)mutableState, n, timerState, (View)object2, context2), (Composer)composer, (int)54)), (Composer)composer, (int)3120, (int)4);
            object = (Modifier)Modifier.Companion;
            f2 = Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)200)) < 0 ? Dp.constructor-impl((float)16) : (Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)240)) < 0 ? Dp.constructor-impl((float)24) : Dp.constructor-impl((float)40));
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)object, (float)f2), (Composer)composer, (int)0);
            if (taskItem != null) {
                composer.startReplaceGroup(-1927417732);
                ComposerKt.sourceInformation((Composer)composer, (String)"2137@108945L68,2141@109221L220,2145@109456L1604,2135@108850L2210");
                mutableState = RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16));
                context2 = CardDefaults.INSTANCE.cardColors-ro_MJ88(Color.copy-wmQWz5c$default((long)themeOption.getSurface-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), 0L, 0L, 0L, composer, CardDefaults.$stable << 12, 14);
                compositionLocalMap = BorderKt.border-xT4_qwU((Modifier)ClipKt.clip((Modifier)((Modifier)Modifier.Companion), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16)))), (float)Dp.constructor-impl((float)1.0f), (long)themeOption.getCardBorder-0d7_KjU(), (Shape)((Shape)RoundedCornerShapeKt.RoundedCornerShape-0680j_4((float)Dp.constructor-impl((float)16))));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-477806456, (String)"CC(remember):MainActivity.kt#9igjgp");
                bl2 = composer.changedInstance(object2);
                object = composer.rememberedValue();
                if (bl2 || object == Composer.Companion.getEmpty()) {
                    object = new MainActivityKt$$ExternalSyntheticLambda44((View)object2);
                    composer.updateRememberedValue(object);
                }
                object = (Function0)object;
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                object = ClickableKt.clickable-XHw0xAI$default((Modifier)compositionLocalMap, (boolean)false, null, null, (Function0)object, (int)7, null);
                object2 = (Shape)mutableState;
                CardKt.Card((Modifier)object, (Shape)object2, (CardColors)context2, null, null, (Function3)((Function3)ComposableLambdaKt.rememberComposableLambda((int)-1496285313, (boolean)true, (Object)new MainActivityKt$$ExternalSyntheticLambda45(taskItem, themeOption), (Composer)composer, (int)54)), (Composer)composer, (int)196608, (int)24);
                composer.endReplaceGroup();
            } else {
                composer.startReplaceGroup(-1925210656);
                ComposerKt.sourceInformation((Composer)composer, (String)"2182@111090L664,2198@111767L40,2199@111820L996");
                object = Alignment.Companion.getCenterVertically();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
                object2 = (Modifier)Modifier.Companion;
                context2 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n6 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                object = composer.getCurrentCompositionLocalMap();
                object2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object2);
                mutableState = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode((Function0)mutableState);
                } else {
                    composer.useNode();
                }
                mutableState = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)mutableState, (Object)context2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)mutableState, (Object)object, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                object = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (mutableState.getInserting() || !Intrinsics.areEqual((Object)mutableState.rememberedValue(), (Object)n6)) {
                    mutableState.updateRememberedValue((Object)n6);
                    mutableState.apply((Object)n6, (Function2)object);
                }
                Updater.set-impl((Composer)mutableState, (Object)object2, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
                object = (RowScope)RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-65998821, (String)"C2183@111160L200,2189@111377L39,2192@111551L8,2194@111663L7,2190@111433L307:MainActivity.kt#to5c3");
                BoxKt.Box((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)12)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)themeOption.getPrimary-0d7_KjU(), null, (int)2, null), (Composer)composer, (int)0);
                SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)8)), (Composer)composer, (int)6);
                object = bl ? "Break Time!" : "Focus Time!";
                long l = ScaledSpKt.getScaledSp((int)16, (Composer)composer, (int)6);
                long l2 = themeOption.getTextSecondary-0d7_KjU();
                object2 = (CompositionLocal)TypographyKt.getLocalAppFont();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
                object2 = composer.consume((CompositionLocal)object2);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                context2 = (FontFamily)object2;
                object2 = FontWeight.Companion.getMedium();
                TextKt.Text--4IGK_g((String)object, null, (long)l2, (long)l, null, (FontWeight)object2, (FontFamily)context2, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196608, (int)0, (int)130962);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)4)), (Composer)composer, (int)6);
                object = Alignment.Companion.getCenterVertically();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
                context2 = (Modifier)Modifier.Companion;
                object = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
                n6 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
                object2 = composer.getCurrentCompositionLocalMap();
                context2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)context2);
                mutableState = ComposeUiNode.Companion.getConstructor();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
                if (!(composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer.startReusableNode();
                if (composer.getInserting()) {
                    composer.createNode((Function0)mutableState);
                } else {
                    composer.useNode();
                }
                mutableState = Updater.constructor-impl((Composer)composer);
                Updater.set-impl((Composer)mutableState, (Object)object, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
                Updater.set-impl((Composer)mutableState, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
                object = ComposeUiNode.Companion.getSetCompositeKeyHash();
                if (mutableState.getInserting() || !Intrinsics.areEqual((Object)mutableState.rememberedValue(), (Object)n6)) {
                    mutableState.updateRememberedValue((Object)n6);
                    mutableState.apply((Object)n6, (Function2)object);
                }
                Updater.set-impl((Composer)mutableState, (Object)context2, (Function2)ComposeUiNode.Companion.getSetModifier());
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
                object = (RowScope)RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)951792838, (String)"C2202@111974L8,2203@112030L7,2200@111890L269,2207@112194L44,2207@112239L219,2212@112475L39,2213@112531L271:MainActivity.kt#to5c3");
                l2 = ScaledSpKt.getScaledSp((int)24, (Composer)composer, (int)6);
                object = (CompositionLocal)TypographyKt.getLocalAppFont();
                ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
                object = composer.consume((CompositionLocal)object);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                object2 = (FontFamily)object;
                l = themeOption.getTextPrimary-0d7_KjU();
                object = FontWeight.Companion.getBold();
                TextKt.Text--4IGK_g((String)"Choose a task", null, (long)l, (long)l2, null, (FontWeight)object, (FontFamily)object2, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196614, (int)0, (int)130962);
                object = InfiniteTransitionKt.rememberInfiniteTransition((String)"cursor", (Composer)composer, (int)6, (int)0);
                object = InfiniteTransitionKt.animateFloat((InfiniteTransition)object, (float)1.0f, (float)0.0f, (InfiniteRepeatableSpec)AnimationSpecKt.infiniteRepeatable-9IiC70o$default((DurationBasedAnimationSpec)((DurationBasedAnimationSpec)AnimationSpecKt.tween$default((int)500, (int)0, null, (int)6, null)), (RepeatMode)RepeatMode.Reverse, (long)0L, (int)4, null), (String)"cursorAlpha", (Composer)composer, (int)(InfiniteTransition.$stable | 0x61B0 | InfiniteRepeatableSpec.$stable << 9), (int)0);
                SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)4)), (Composer)composer, (int)6);
                BoxKt.Box((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.height-3ABfNKs((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)4)), (float)Dp.constructor-impl((float)28)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)Color.copy-wmQWz5c$default((long)themeOption.getPrimary-0d7_KjU(), (float)((Number)object.getValue()).floatValue(), (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null), null, (int)2, null), (Composer)composer, (int)0);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                ComposerKt.sourceInformationMarkerEnd((Composer)composer);
                composer.endReplaceGroup();
            }
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        object = composer.endRestartGroup();
        if (object != null) {
            object.updateScope((Function2)new MainActivityKt$$ExternalSyntheticLambda46(n, n2, timerState, bl, string, taskItem, context, f, n3, n4));
        }
    }

    private static final String TimerDisplay_egy_3UM$lambda$411(MutableState<String> mutableState) {
        return (String)((State)mutableState).getValue();
    }

    private static final void TimerDisplay_egy_3UM$lambda$412(MutableState<String> mutableState, String string) {
        mutableState.setValue((Object)string);
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit TimerDisplay_egy_3UM$lambda$432$lambda$424(ThemeOption object, boolean bl, Animatable object2, int n, TimerManager.TimerState object3, View view, Context context, BoxWithConstraintsScope object4, Composer composer, int n2) {
        int n3;
        Intrinsics.checkNotNullParameter((Object)object4, (String)"$this$BoxWithConstraints");
        ComposerKt.sourceInformation((Composer)composer, (String)"C2069@104913L783,2067@104780L916,2086@105722L2930:MainActivity.kt#to5c3");
        int n4 = n3 = n2;
        if ((n2 & 6) == 0) {
            n2 = composer.changed(object4) ? 4 : 2;
            n4 = n3 | n2;
        }
        if ((n4 & 0x13) == 18 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-598479786, (int)n4, (int)-1, (String)"com.example.TimerDisplay.<anonymous>.<anonymous> (MainActivity.kt:2066)");
            }
            float f = object4.getMaxWidth-D9Ej5fM();
            object4 = SizeKt.fillMaxSize$default((Modifier)((Modifier)Modifier.Companion), (float)0.0f, (int)1, null);
            float f2 = Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)200)) < 0 ? Dp.constructor-impl((float)8) : Dp.constructor-impl((float)16);
            Modifier modifier = PaddingKt.padding-3ABfNKs((Modifier)object4, (float)f2);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)386550245, (String)"CC(remember):MainActivity.kt#9igjgp");
            boolean bl2 = composer.changedInstance(object);
            boolean bl3 = composer.changed(f);
            boolean bl4 = composer.changed(bl);
            boolean bl5 = composer.changedInstance(object2);
            object4 = composer.rememberedValue();
            if (!(bl2 | bl3 | bl4 | bl5) && object4 != Composer.Companion.getEmpty()) {
                object2 = object4;
            } else {
                object2 = new MainActivityKt$$ExternalSyntheticLambda0((ThemeOption)object, f, bl, (Animatable)object2);
                composer.updateRememberedValue(object2);
            }
            object2 = (Function1)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            CanvasKt.Canvas((Modifier)modifier, (Function1)object2, (Composer)composer, (int)0);
            object2 = Alignment.Companion.getCenterHorizontally();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            modifier = (Modifier)Modifier.Companion;
            object4 = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)object2, (Composer)composer, (int)(384 >> 3 & 0xE | 384 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n2 = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object2 = composer.getCurrentCompositionLocalMap();
            modifier = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)modifier);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object4, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n2)) {
                function0.updateRememberedValue((Object)n2);
                function0.apply((Object)n2, (Function2)object2);
            }
            Updater.set-impl((Composer)function0, (Object)modifier, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object2 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1487530860, (String)"C2091@106099L69,2092@106185L82,2102@106938L1112,2097@106678L1960:MainActivity.kt#to5c3");
            n2 = n / 60 > 99 ? 1 : 0;
            f2 = n2 != 0 ? 0.16f : 0.22f;
            float f3 = n2 != 0 ? 38.0f : 54.0f;
            MainActivityKt.SlidingTimer-KmRG4DE(n, TextUnitKt.getSp((float)RangesKt.coerceAtMost((float)(f * f2), (float)f3)), composer, 0, 0);
            object2 = (Modifier)Modifier.Companion;
            f2 = Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)200)) < 0 ? Dp.constructor-impl((float)2) : Dp.constructor-impl((float)4);
            SpacerKt.Spacer((Modifier)SizeKt.height-3ABfNKs((Modifier)object2, (float)f2), (Composer)composer, (int)0);
            long l = bl ? Color.copy-wmQWz5c$default((long)object.getSecondary-0d7_KjU(), (float)0.18f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null) : Color.copy-wmQWz5c$default((long)object.getPrimary-0d7_KjU(), (float)0.18f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
            long l2 = bl ? object.getSecondary-0d7_KjU() : object.getPrimary-0d7_KjU();
            object = bl ? "BREAK TIME" : "WORK TIME";
            n = object3 == TimerManager.TimerState.RUNNING ? 1 : 0;
            object2 = BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)((Modifier)Modifier.Companion), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)l, null, (int)2, null);
            f2 = n != 0 ? 0.5f : 1.0f;
            object4 = AlphaKt.alpha((Modifier)object2, (float)f2);
            bl4 = n == 0;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)740756248, (String)"CC(remember):MainActivity.kt#9igjgp");
            bl2 = composer.changedInstance((Object)view);
            boolean bl6 = composer.changed(((Enum)object3).ordinal());
            bl5 = composer.changedInstance((Object)context);
            bl3 = composer.changed(bl);
            object2 = composer.rememberedValue();
            if (bl2 | bl6 | bl5 | bl3 || object2 == Composer.Companion.getEmpty()) {
                object2 = new MainActivityKt$$ExternalSyntheticLambda1(view, (TimerManager.TimerState)object3, context, bl);
                composer.updateRememberedValue(object2);
            }
            object2 = (Function0)object2;
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object2 = ClickableKt.clickable-XHw0xAI$default((Modifier)object4, (boolean)bl4, null, null, (Function0)object2, (int)6, null);
            f2 = Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)200)) < 0 ? Dp.constructor-impl((float)8) : Dp.constructor-impl((float)12);
            f3 = Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)200)) < 0 ? Dp.constructor-impl((float)2) : Dp.constructor-impl((float)4);
            view = PaddingKt.padding-VpY3zN4((Modifier)object2, (float)f2, (float)f3);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)733328855, (String)"CC(Box)P(2,1,3)72@3384L130:Box.kt#2w3rfo");
            object3 = BoxKt.maybeCachedBoxMeasurePolicy((Alignment)Alignment.Companion.getTopStart(), (boolean)false);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object2 = composer.getCurrentCompositionLocalMap();
            view = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)view);
            context = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode((Function0)context);
            } else {
                composer.useNode();
            }
            context = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)context, (Object)object3, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)context, (Object)object2, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (context.getInserting() || !Intrinsics.areEqual((Object)context.rememberedValue(), (Object)n)) {
                context.updateRememberedValue((Object)n);
                context.apply((Object)n, (Function2)object2);
            }
            Updater.set-impl((Composer)context, (Object)view, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-2146769399, (String)"C73@3429L9:Box.kt#2w3rfo");
            object2 = (BoxScope)BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1152943342, (String)"C2124@108493L7,2126@108590L8,2120@108245L375:MainActivity.kt#to5c3");
            if (Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)200)) < 0) {
                composer.startReplaceGroup(1902474306);
                ComposerKt.sourceInformation((Composer)composer, (String)"2122@108362L8");
                n = 9;
            } else {
                composer.startReplaceGroup(1902474850);
                ComposerKt.sourceInformation((Composer)composer, (String)"2122@108379L8");
                n = 12;
            }
            long l3 = ScaledSpKt.getScaledSp((int)n, (Composer)composer, (int)6);
            composer.endReplaceGroup();
            object2 = FontWeight.Companion.getBold();
            object3 = (CompositionLocal)TypographyKt.getLocalAppFont();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object3 = composer.consume((CompositionLocal)object3);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object3 = (FontFamily)object3;
            l = ScaledSpKt.getScaledSp((int)1, (Composer)composer, (int)6);
            TextKt.Text--4IGK_g((String)object, null, (long)l2, (long)l3, null, (FontWeight)object2, (FontFamily)object3, (long)l, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196608, (int)0, (int)130834);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit TimerDisplay_egy_3UM$lambda$432$lambda$424$lambda$418$lambda$417(ThemeOption themeOption, float f, boolean bl, Animatable animatable, DrawScope drawScope) {
        Intrinsics.checkNotNullParameter((Object)drawScope, (String)"$this$Canvas");
        long l = Color.copy-wmQWz5c$default((long)themeOption.getCardBorder-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
        float f2 = Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)200)) < 0 ? Dp.constructor-impl((float)4) : Dp.constructor-impl((float)8);
        DrawScope.drawArc-yD3GUKo$default((DrawScope)drawScope, (long)l, (float)-90.0f, (float)360.0f, (boolean)false, (long)0L, (long)0L, (float)0.0f, (DrawStyle)((DrawStyle)new Stroke(drawScope.toPx-0680j_4(f2), 0.0f, StrokeCap.Companion.getRound-KaPHkGw(), 0, null, 26, null)), null, (int)0, (int)880, null);
        l = bl ? themeOption.getSecondary-0d7_KjU() : themeOption.getPrimary-0d7_KjU();
        f2 = ((Number)animatable.getValue()).floatValue();
        f = Dp.compareTo-0680j_4((float)f, (float)Dp.constructor-impl((float)200)) < 0 ? Dp.constructor-impl((float)4) : Dp.constructor-impl((float)8);
        DrawScope.drawArc-yD3GUKo$default((DrawScope)drawScope, (long)l, (float)-90.0f, (float)(360.0f * f2), (boolean)false, (long)0L, (long)0L, (float)0.0f, (DrawStyle)((DrawStyle)new Stroke(drawScope.toPx-0680j_4(f), 0.0f, StrokeCap.Companion.getRound-KaPHkGw(), 0, null, 26, null)), null, (int)0, (int)880, null);
        return Unit.INSTANCE;
    }

    static final Unit TimerDisplay_egy_3UM$lambda$432$lambda$424$lambda$423$lambda$421$lambda$420(View object, TimerManager.TimerState timerState, Context context, boolean bl) {
        try {
            object.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        boolean bl2 = timerState != TimerManager.TimerState.STOPPED;
        if (bl2) {
            object = new Intent(context, TimerService.class);
            object.setAction("ACTION_TOGGLE_MODE");
            context.startService((Intent)object);
        } else {
            TimerManager.INSTANCE.setBreakMode(bl ^= true);
            TimerManager.INSTANCE.updateState(TimerManager.TimerState.STOPPED);
            timerState = TimerManager.INSTANCE;
            object = TimerManager.INSTANCE;
            object = bl ? object.getBreakTimeSeconds() : object.getFocusTimeSeconds();
            timerState.updateTime(((Number)object.getValue()).intValue());
            timerState = TimerManager.INSTANCE;
            object = bl ? "Break Time!" : "Focus Time!";
            TimerManager.setTask$default((TimerManager)timerState, (int)-1, (String)object, null, (int)4, null);
        }
        return Unit.INSTANCE;
    }

    static final Unit TimerDisplay_egy_3UM$lambda$432$lambda$426$lambda$425(View view) {
        try {
            view.performHapticFeedback(3);
        }
        catch (Exception exception) {
            // empty catch block
        }
        TimerManager.setTask$default((TimerManager)TimerManager.INSTANCE, (int)-1, (String)"Focus Time!", null, (int)4, null);
        return Unit.INSTANCE;
    }

    @Composable
    @ComposableTarget(applier="androidx.compose.ui.UiComposable")
    static final Unit TimerDisplay_egy_3UM$lambda$432$lambda$429(TaskItem object, ThemeOption themeOption, ColumnScope object2, Composer composer, int n) {
        Intrinsics.checkNotNullParameter((Object)object2, (String)"$this$Card");
        ComposerKt.sourceInformation((Composer)composer, (String)"C2146@109474L1572:MainActivity.kt#to5c3");
        if ((n & 0x11) == 16 && composer.getSkipping()) {
            composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart((int)-1496285313, (int)n, (int)-1, (String)"com.example.TimerDisplay.<anonymous>.<anonymous> (MainActivity.kt:2146)");
            }
            object2 = Alignment.Companion.getCenterVertically();
            Object object3 = PaddingKt.padding-VpY3zN4((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16), (float)Dp.constructor-impl((float)12));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)693286680, (String)"CC(Row)P(2,1,3)99@5018L58,100@5081L130:Row.kt#2w3rfo");
            object2 = RowKt.rowMeasurePolicy((Arrangement.Horizontal)Arrangement.INSTANCE.getStart(), (Alignment.Vertical)object2, (Composer)composer, (int)(390 >> 3 & 0xE | 390 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            Object object4 = composer.getCurrentCompositionLocalMap();
            object3 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object3);
            Function0 function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object2, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)object4, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object2 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)object2);
            }
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-407840262, (String)"C101@5126L9:Row.kt#2w3rfo");
            object2 = (RowScope)RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1154576605, (String)"C2150@109673L242,2156@109936L40,2157@109997L668,2172@110686L40,2173@110747L281:MainActivity.kt#to5c3");
            BoxKt.Box((Modifier)BackgroundKt.background-bw27NRU$default((Modifier)ClipKt.clip((Modifier)SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)14)), (Shape)((Shape)RoundedCornerShapeKt.getCircleShape())), (long)Color.constructor-impl((long)ULong.constructor-impl((long)object.getCategoryColor())), null, (int)2, null), (Composer)composer, (int)0);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)12)), (Composer)composer, (int)6);
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-483455358, (String)"CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
            object2 = (Modifier)Modifier.Companion;
            object4 = ColumnKt.columnMeasurePolicy((Arrangement.Vertical)Arrangement.INSTANCE.getTop(), (Alignment.Horizontal)Alignment.Companion.getStart(), (Composer)composer, (int)(0 >> 3 & 0xE | 0 >> 3 & 0x70));
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-1323940314, (String)"CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
            n = ComposablesKt.getCurrentCompositeKeyHash((Composer)composer, (int)0);
            object3 = composer.getCurrentCompositionLocalMap();
            object2 = ComposedModifierKt.materializeModifier((Composer)composer, (Modifier)object2);
            function0 = ComposeUiNode.Companion.getConstructor();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-692256719, (String)"CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!(composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer.startReusableNode();
            if (composer.getInserting()) {
                composer.createNode(function0);
            } else {
                composer.useNode();
            }
            function0 = Updater.constructor-impl((Composer)composer);
            Updater.set-impl((Composer)function0, (Object)object4, (Function2)ComposeUiNode.Companion.getSetMeasurePolicy());
            Updater.set-impl((Composer)function0, (Object)object3, (Function2)ComposeUiNode.Companion.getSetResolvedCompositionLocals());
            object3 = ComposeUiNode.Companion.getSetCompositeKeyHash();
            if (function0.getInserting() || !Intrinsics.areEqual((Object)function0.rememberedValue(), (Object)n)) {
                function0.updateRememberedValue((Object)n);
                function0.apply((Object)n, (Function2)object3);
            }
            Updater.set-impl((Composer)function0, (Object)object2, (Function2)ComposeUiNode.Companion.getSetModifier());
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)-384784025, (String)"C88@4444L9:Column.kt#2w3rfo");
            object2 = (ColumnScope)ColumnScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)1272870393, (String)"C2160@110131L8,2163@110315L7,2158@110030L318,2167@110482L8,2169@110610L7,2165@110373L270:MainActivity.kt#to5c3");
            object3 = object.getName();
            long l = ScaledSpKt.getScaledSp((int)15, (Composer)composer, (int)6);
            long l2 = themeOption.getTextPrimary-0d7_KjU();
            object2 = FontWeight.Companion.getBold();
            object4 = (CompositionLocal)TypographyKt.getLocalAppFont();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object4 = composer.consume((CompositionLocal)object4);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object4 = (FontFamily)object4;
            TextKt.Text--4IGK_g((String)object3, null, (long)l2, (long)l, null, (FontWeight)object2, (FontFamily)object4, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)196608, (int)0, (int)130962);
            object = object.getCategoryName();
            l = ScaledSpKt.getScaledSp((int)12, (Composer)composer, (int)6);
            l2 = themeOption.getTextSecondary-0d7_KjU();
            object2 = (CompositionLocal)TypographyKt.getLocalAppFont();
            ComposerKt.sourceInformationMarkerStart((Composer)composer, (int)2023513938, (String)"CC:CompositionLocal.kt#9igjgp");
            object2 = composer.consume((CompositionLocal)object2);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            object2 = (FontFamily)object2;
            TextKt.Text--4IGK_g((String)object, null, (long)l2, (long)l, null, null, (FontFamily)object2, (long)0L, null, null, (long)0L, (int)0, (boolean)false, (int)0, (int)0, null, null, (Composer)composer, (int)0, (int)0, (int)130994);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            SpacerKt.Spacer((Modifier)SizeKt.width-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16)), (Composer)composer, (int)6);
            object = CloseKt.getClose((Icons.Filled)Icons.Filled.INSTANCE);
            l = Color.copy-wmQWz5c$default((long)themeOption.getTextSecondary-0d7_KjU(), (float)0.5f, (float)0.0f, (float)0.0f, (float)0.0f, (int)14, null);
            themeOption = SizeKt.size-3ABfNKs((Modifier)((Modifier)Modifier.Companion), (float)Dp.constructor-impl((float)16));
            IconKt.Icon-ww6aTOc((ImageVector)object, (String)"Deselect Task", (Modifier)themeOption, (long)l, (Composer)composer, (int)432, (int)0);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            ComposerKt.sourceInformationMarkerEnd((Composer)composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit TimerDisplay_egy_3UM$lambda$433(int n, int n2, TimerManager.TimerState timerState, boolean bl, String string, TaskItem taskItem, Context context, float f, int n3, int n4, Composer composer, int n5) {
        MainActivityKt.TimerDisplay-egy_3UM(n, n2, timerState, bl, string, taskItem, context, f, composer, RecomposeScopeImplKt.updateChangedFlags((int)(n3 | 1)), n4);
        return Unit.INSTANCE;
    }

    public static final /* synthetic */ void access$AddTaskCard$lambda$368$lambda$334(MutableState mutableState, boolean bl) {
        MainActivityKt.AddTaskCard$lambda$368$lambda$334((MutableState<Boolean>)mutableState, bl);
    }

    public static final /* synthetic */ TimerManager.TimerState access$HomeScreen_6a0pyJM$lambda$82(State state) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$82((State<? extends TimerManager.TimerState>)state);
    }

    public static final /* synthetic */ boolean access$HomeScreen_6a0pyJM$lambda$83(State state) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$83((State<Boolean>)state);
    }

    public static final /* synthetic */ int access$HomeScreen_6a0pyJM$lambda$87(State state) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$87((State<Integer>)state);
    }

    public static final /* synthetic */ void access$HomeScreen_6a0pyJM$lambda$93(MutableState mutableState, boolean bl) {
        MainActivityKt.HomeScreen_6a0pyJM$lambda$93((MutableState<Boolean>)mutableState, bl);
    }

    public static final /* synthetic */ boolean access$HomeScreen_6a0pyJM$lambda$94(State state) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$94((State<Boolean>)state);
    }

    public static final /* synthetic */ void access$HomeScreen_6a0pyJM$lambda$98(MutableState mutableState, boolean bl) {
        MainActivityKt.HomeScreen_6a0pyJM$lambda$98((MutableState<Boolean>)mutableState, bl);
    }

    public static final /* synthetic */ void access$PomoPalApp$lambda$7(MutableState mutableState, String string) {
        MainActivityKt.PomoPalApp$lambda$7((MutableState<String>)mutableState, string);
    }

    public static final /* synthetic */ int access$SettingsOverlay$lambda$192(MutableIntState mutableIntState) {
        return MainActivityKt.SettingsOverlay$lambda$192(mutableIntState);
    }

    public static final /* synthetic */ void access$SettingsOverlay$lambda$193(MutableIntState mutableIntState, int n) {
        MainActivityKt.SettingsOverlay$lambda$193(mutableIntState, n);
    }

    public static final /* synthetic */ float access$SettingsOverlay$lambda$204(MutableState mutableState) {
        return MainActivityKt.SettingsOverlay$lambda$204((MutableState<Float>)mutableState);
    }

    public static final /* synthetic */ float access$SettingsOverlay$lambda$207(MutableState mutableState) {
        return MainActivityKt.SettingsOverlay$lambda$207((MutableState<Float>)mutableState);
    }

    public static final /* synthetic */ int access$SettingsOverlay$lambda$210(MutableState mutableState) {
        return MainActivityKt.SettingsOverlay$lambda$210((MutableState<Integer>)mutableState);
    }

    public static final /* synthetic */ float access$SettingsOverlay$lambda$213(MutableState mutableState) {
        return MainActivityKt.SettingsOverlay$lambda$213((MutableState<Float>)mutableState);
    }

    public static final /* synthetic */ String access$TimerDisplay_egy_3UM$lambda$411(MutableState mutableState) {
        return MainActivityKt.TimerDisplay_egy_3UM$lambda$411((MutableState<String>)mutableState);
    }

    public static final /* synthetic */ void access$TimerDisplay_egy_3UM$lambda$412(MutableState mutableState, String string) {
        MainActivityKt.TimerDisplay_egy_3UM$lambda$412((MutableState<String>)mutableState, string);
    }
}

