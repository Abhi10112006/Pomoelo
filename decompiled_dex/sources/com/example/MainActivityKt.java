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
import android.view.Window;
import androidx.activity.compose.BackHandlerKt;
import androidx.activity.compose.ManagedActivityResultLauncher;
import androidx.compose.animation.AnimatedContentKt;
import androidx.compose.animation.AnimatedContentScope;
import androidx.compose.animation.AnimatedContentTransitionScope;
import androidx.compose.animation.AnimatedVisibilityKt;
import androidx.compose.animation.AnimatedVisibilityScope;
import androidx.compose.animation.ContentTransform;
import androidx.compose.animation.EnterExitTransitionKt;
import androidx.compose.animation.EnterTransition;
import androidx.compose.animation.ExitTransition;
import androidx.compose.animation.SingleValueAnimationKt;
import androidx.compose.animation.TransitionKt;
import androidx.compose.animation.core.Animatable;
import androidx.compose.animation.core.AnimateAsStateKt;
import androidx.compose.animation.core.AnimationSpec;
import androidx.compose.animation.core.AnimationSpecKt;
import androidx.compose.animation.core.Easing;
import androidx.compose.animation.core.EasingKt;
import androidx.compose.animation.core.FiniteAnimationSpec;
import androidx.compose.animation.core.InfiniteRepeatableSpec;
import androidx.compose.animation.core.InfiniteTransition;
import androidx.compose.animation.core.InfiniteTransitionKt;
import androidx.compose.animation.core.RepeatMode;
import androidx.compose.foundation.BackgroundKt;
import androidx.compose.foundation.BorderKt;
import androidx.compose.foundation.BorderStroke;
import androidx.compose.foundation.ClickableKt;
import androidx.compose.foundation.Indication;
import androidx.compose.foundation.interaction.InteractionSourceKt;
import androidx.compose.foundation.interaction.MutableInteractionSource;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.BoxKt;
import androidx.compose.foundation.layout.BoxWithConstraintsKt;
import androidx.compose.foundation.layout.BoxWithConstraintsScope;
import androidx.compose.foundation.layout.ColumnKt;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.foundation.layout.ColumnScopeInstance;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.layout.WindowInsets;
import androidx.compose.foundation.layout.WindowInsetsKt;
import androidx.compose.foundation.layout.WindowInsetsPadding_androidKt;
import androidx.compose.foundation.lazy.LazyItemScope;
import androidx.compose.foundation.lazy.LazyListScope;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.foundation.text.KeyboardActionScope;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.AddKt;
import androidx.compose.material.icons.filled.CheckCircleKt;
import androidx.compose.material.icons.filled.CloseKt;
import androidx.compose.material3.AndroidAlertDialog_androidKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonElevation;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.CardColors;
import androidx.compose.material3.CardDefaults;
import androidx.compose.material3.CardElevation;
import androidx.compose.material3.CardKt;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.ScaffoldKt;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Applier;
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.ComposableTarget;
import androidx.compose.runtime.ComposablesKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.CompositionLocalMap;
import androidx.compose.runtime.DisposableEffectResult;
import androidx.compose.runtime.DisposableEffectScope;
import androidx.compose.runtime.EffectsKt;
import androidx.compose.runtime.IntState;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.SnapshotMutationPolicy;
import androidx.compose.runtime.SnapshotStateKt;
import androidx.compose.runtime.State;
import androidx.compose.runtime.Updater;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.runtime.snapshots.SnapshotStateList;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.ComposedModifierKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.draw.ClipKt;
import androidx.compose.ui.draw.ShadowKt;
import androidx.compose.ui.focus.FocusManager;
import androidx.compose.ui.geometry.Size;
import androidx.compose.ui.graphics.AndroidPath_androidKt;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.ColorFilter;
import androidx.compose.ui.graphics.ColorKt;
import androidx.compose.ui.graphics.GraphicsLayerModifierKt;
import androidx.compose.ui.graphics.GraphicsLayerScope;
import androidx.compose.ui.graphics.Path;
import androidx.compose.ui.graphics.PathEffect;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.graphics.StrokeCap;
import androidx.compose.ui.graphics.drawscope.DrawScope;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.input.pointer.SuspendingPointerInputFilterKt;
import androidx.compose.ui.layout.MeasurePolicy;
import androidx.compose.ui.node.ComposeUiNode;
import androidx.compose.ui.platform.AndroidCompositionLocals_androidKt;
import androidx.compose.ui.platform.CompositionLocalsKt;
import androidx.compose.ui.semantics.Role;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import androidx.compose.ui.window.AndroidDialog_androidKt;
import androidx.compose.ui.window.DialogProperties;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraphBuilder;
import androidx.navigation.NavHostController;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigator;
import androidx.navigation.PopUpToBuilder;
import androidx.navigation.compose.NavGraphBuilderKt;
import androidx.navigation.compose.NavHostControllerKt;
import androidx.navigation.compose.NavHostKt;
import com.example.ComposableSingletons;
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
import com.example.ui.HistoryScreenKt;
import com.example.ui.SignInScreenKt;
import com.example.ui.SquatCalibrationScreenKt;
import com.example.ui.SquatCalibrationViewModel;
import com.example.ui.SystemProtectionStateKt;
import com.example.ui.SystemProtectionStates;
import com.example.ui.TimerViewModel;
import com.example.ui.WorkoutScreenKt;
import com.example.ui.components.AdaptiveDimensions;
import com.example.ui.components.AdaptiveSystemKt;
import com.example.ui.components.PomoUIKt;
import com.example.ui.components.ScaledSpKt;
import com.example.ui.theme.ThemeKt;
import com.example.ui.theme.ThemeOption;
import com.example.ui.theme.TypographyKt;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ULong;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000°\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0015\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0004\u001a)\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007¢\u0006\u0004\b\n\u0010\u000b\u001a%\u0010\f\u001a\u00020\u00012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001aK\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00162\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00010\u00182\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u001bH\u0007¢\u0006\u0002\u0010\u001c\u001a\u0015\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u001fH\u0007¢\u0006\u0002\u0010 \u001a;\u0010!\u001a\u00020\u00012\u001e\u0010\"\u001a\u001a\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u00010#2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007¢\u0006\u0002\u0010&\u001ab\u0010'\u001a\u00020\u00012\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010)\u001a\u00020\u00162\b\b\u0002\u0010*\u001a\u00020+2\b\b\u0002\u0010,\u001a\u00020\t2\b\b\u0002\u0010-\u001a\u00020.2\u0011\u0010/\u001a\r\u0012\u0004\u0012\u00020\u00010\u000e¢\u0006\u0002\b0H\u0007¢\u0006\u0004\b1\u00102\u001a-\u00103\u001a\u00020\u00012\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u0002072\u000e\b\u0002\u00108\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007¢\u0006\u0002\u00109\u001a!\u0010:\u001a\u00020\u00012\u0006\u0010;\u001a\u00020\u00102\b\b\u0002\u0010<\u001a\u00020=H\u0007¢\u0006\u0004\b>\u0010?\u001aU\u0010@\u001a\u00020\u00012\u0006\u0010;\u001a\u00020\u00102\u0006\u0010A\u001a\u00020\u00102\u0006\u00104\u001a\u0002052\u0006\u0010B\u001a\u00020\u00162\u0006\u0010C\u001a\u00020\u001f2\n\b\u0002\u0010D\u001a\u0004\u0018\u00010\u00142\u0006\u00106\u001a\u0002072\b\b\u0002\u0010E\u001a\u00020\tH\u0007¢\u0006\u0004\bF\u0010G\u001a\u001d\u0010H\u001a\u00020\u00012\u0006\u0010I\u001a\u00020\u001f2\u0006\u0010J\u001a\u00020\u0016H\u0007¢\u0006\u0002\u0010K\u001a3\u0010L\u001a\u00020\u00012\u0006\u0010I\u001a\u00020\u001f2\u0006\u0010C\u001a\u00020\u001f2\u0006\u0010;\u001a\u00020\u00102\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007¢\u0006\u0002\u0010M\u001a\u001d\u0010N\u001a\u00020\u00012\u0006\u0010O\u001a\u00020\u001f2\u0006\u0010P\u001a\u00020\u0010H\u0007¢\u0006\u0002\u0010Q¨\u0006R²\u0006\f\u0010S\u001a\u0004\u0018\u00010TX\u008a\u0084\u0002²\u0006\n\u0010U\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\n\u0010V\u001a\u000205X\u008a\u0084\u0002²\u0006\n\u0010W\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\n\u0010X\u001a\u00020\u0010X\u008a\u0084\u0002²\u0006\f\u0010Y\u001a\u0004\u0018\u00010\u001fX\u008a\u008e\u0002²\u0006\n\u0010Z\u001a\u00020[X\u008a\u0084\u0002²\u0006\n\u0010\\\u001a\u00020[X\u008a\u0084\u0002²\u0006\n\u0010]\u001a\u00020[X\u008a\u0084\u0002²\u0006\n\u0010^\u001a\u00020[X\u008a\u0084\u0002²\u0006\n\u0010V\u001a\u000205X\u008a\u0084\u0002²\u0006\n\u0010B\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\n\u0010_\u001a\u00020\u001fX\u008a\u0084\u0002²\u0006\n\u0010;\u001a\u00020\u0010X\u008a\u0084\u0002²\u0006\n\u0010`\u001a\u00020\u001fX\u008a\u0084\u0002²\u0006\n\u0010a\u001a\u00020\u0010X\u008a\u0084\u0002²\u0006\f\u0010b\u001a\u0004\u0018\u00010$X\u008a\u0084\u0002²\u0006\u0010\u0010c\u001a\b\u0012\u0004\u0012\u00020\u00140dX\u008a\u0084\u0002²\u0006\n\u0010U\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\n\u0010e\u001a\u00020\u0016X\u008a\u008e\u0002²\u0006\n\u0010f\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\n\u0010X\u001a\u00020\u0010X\u008a\u0084\u0002²\u0006\n\u0010g\u001a\u00020\u0016X\u008a\u008e\u0002²\u0006\n\u0010h\u001a\u00020\u0016X\u008a\u008e\u0002²\u0006\n\u0010i\u001a\u00020.X\u008a\u0084\u0002²\u0006\n\u0010j\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\n\u0010k\u001a\u00020[X\u008a\u0084\u0002²\u0006\n\u0010l\u001a\u00020\u0010X\u008a\u008e\u0002²\u0006\n\u0010m\u001a\u00020\u0016X\u008a\u008e\u0002²\u0006\n\u0010n\u001a\u00020[X\u008a\u008e\u0002²\u0006\n\u0010o\u001a\u00020[X\u008a\u008e\u0002²\u0006\n\u0010p\u001a\u00020\u0010X\u008a\u008e\u0002²\u0006\n\u0010q\u001a\u00020[X\u008a\u008e\u0002²\u0006\n\u0010r\u001a\u00020[X\u008a\u008e\u0002²\u0006\n\u0010s\u001a\u00020.X\u008a\u0084\u0002²\u0006\n\u0010t\u001a\u00020.X\u008a\u0084\u0002²\u0006\n\u0010u\u001a\u00020\tX\u008a\u0084\u0002²\u0006\n\u0010v\u001a\u00020\u001fX\u008a\u008e\u0002²\u0006\n\u0010w\u001a\u00020\u001fX\u008a\u008e\u0002²\u0006\n\u0010x\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\n\u0010y\u001a\u00020[X\u008a\u0084\u0002²\u0006\n\u0010z\u001a\u00020{X\u008a\u008e\u0002²\u0006\n\u0010|\u001a\u00020.X\u008a\u0084\u0002²\u0006\n\u0010}\u001a\u00020\tX\u008a\u0084\u0002²\u0006\n\u0010~\u001a\u00020\u0016X\u008a\u008e\u0002²\u0006\n\u0010\u007f\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u0080\u0001\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u0081\u0001\u001a\u00020\u001fX\u008a\u008e\u0002²\u0006\u0018\u0010\u0082\u0001\u001a\u000f\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020.0\u0083\u0001X\u008a\u008e\u0002²\u0006\u000b\u0010\u0084\u0001\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\n\u0010\u007f\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u0085\u0001\u001a\u00020\tX\u008a\u0084\u0002²\u0006\n\u0010B\u001a\u00020\u0016X\u008a\u0084\u0002²\u0006\u000b\u0010\u0086\u0001\u001a\u00020\u001fX\u008a\u008e\u0002²\u0006\u000b\u0010\u0087\u0001\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u0088\u0001\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u0089\u0001\u001a\u00020.X\u008a\u0084\u0002²\u0006\u000b\u0010\u008a\u0001\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u008b\u0001\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u008c\u0001\u001a\u00020\u0010X\u008a\u008e\u0002²\u0006\u000b\u0010\u008d\u0001\u001a\u00020\u0016X\u008a\u008e\u0002²\u0006\u000b\u0010\u008e\u0001\u001a\u00020\u001fX\u008a\u008e\u0002²\u0006\u000b\u0010\u008f\u0001\u001a\u00020\u0016X\u008a\u008e\u0002²\u0006\n\u0010\u007f\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u0090\u0001\u001a\u00020[X\u008a\u0084\u0002²\u0006\u000b\u0010\u0091\u0001\u001a\u00020[X\u008a\u0084\u0002"}, d2 = {"PomoPalApp", "", "viewModel", "Lcom/example/ui/TimerViewModel;", "(Lcom/example/ui/TimerViewModel;Landroidx/compose/runtime/Composer;I)V", "HomeScreen", "navController", "Landroidx/navigation/NavController;", "bottomPadding", "Landroidx/compose/ui/unit/Dp;", "HomeScreen-6a0pyJM", "(Lcom/example/ui/TimerViewModel;Landroidx/navigation/NavController;FLandroidx/compose/runtime/Composer;II)V", "SettingsOverlay", "onDismiss", "Lkotlin/Function0;", "initialTab", "", "(Lkotlin/jvm/functions/Function0;ILandroidx/compose/runtime/Composer;II)V", "TaskItemRow", "task", "Lcom/example/data/TaskItem;", "isSelected", "", "onSelect", "Lkotlin/Function1;", "onDelete", "modifier", "Landroidx/compose/ui/Modifier;", "(Lcom/example/data/TaskItem;ZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "PremiumJumpingTextPreview", "text", "", "(Ljava/lang/String;Landroidx/compose/runtime/Composer;I)V", "AddTaskCard", "onSave", "Lkotlin/Function3;", "", "onCancel", "(Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "AnimatedScaleBox", "onClick", "enabled", "shape", "Landroidx/compose/ui/graphics/Shape;", "elevation", "shadowColor", "Landroidx/compose/ui/graphics/Color;", "content", "Landroidx/compose/runtime/Composable;", "AnimatedScaleBox-gXMAzVA", "(Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;ZLandroidx/compose/ui/graphics/Shape;FJLkotlin/jvm/functions/Function2;Landroidx/compose/runtime/Composer;II)V", "AppControls", "state", "Lcom/example/service/TimerManager$TimerState;", "context", "Landroid/content/Context;", "onStartAttempt", "(Lcom/example/service/TimerManager$TimerState;Landroid/content/Context;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;II)V", "SlidingTimer", "timeRemaining", "fontSize", "Landroidx/compose/ui/unit/TextUnit;", "SlidingTimer-KmRG4DE", "(IJLandroidx/compose/runtime/Composer;II)V", "TimerDisplay", "totalTime", "isBreakMode", "taskName", "currentTask", "circleSize", "TimerDisplay-egy_3UM", "(IILcom/example/service/TimerManager$TimerState;ZLjava/lang/String;Lcom/example/data/TaskItem;Landroid/content/Context;FLandroidx/compose/runtime/Composer;II)V", "AnimatedQuoteCard", "quote", "isBreak", "(Ljava/lang/String;ZLandroidx/compose/runtime/Composer;I)V", "SeriousFullscreenOverlay", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "AlarmRingingLockScreen", "label", "targetCount", "(Ljava/lang/String;ILandroidx/compose/runtime/Composer;I)V", "app", "navBackStackEntry", "Landroidx/navigation/NavBackStackEntry;", "isAddingTask", "timerState", "isSettingsOpen", "settingsInitialTab", "updateUrl", "homeWeight", "", "historyWeight", "alarmWeight", "workoutWeight", "currentQuote", "currentTaskName", "currentTaskId", "currentTaskColor", "allTasks", "", "showAllTasksSheet", "showSettings", "showFullScreenSeriousness", "showJustInTimeSetup", "stateColor", "isSettingsPressed", "settingsRotation", "selectedTabIndex", "showBlockedApps", "localFocus", "localBreak", "localCompletion", "localCompletionDuration", "localVolume", "tabBgColor", "tabTextColor", "tabElevation", "focusInput", "breakInput", "isBlockedPressed", "blockedScale", "accessibilityStatus", "Lcom/example/service/AppBlockerManager$AccessibilityServiceStatus;", "borderColor", "borderWidth", "animateIn", "scale", "alpha", "title", "selectedCategory", "Lkotlin/Pair;", "isPressed", "currentElevation", "animatedText", "pulseAlpha", "pulseScale", "glowColor", "scaleGlow", "animatedAlpha", "currentCount", "shakeWarning", "sensorMessage", "isFinished", "animatedProgress", "waveOffset"}, k = 2, mv = {2, 2, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nMainActivity.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MainActivity.kt\ncom/example/MainActivityKt\n+ 2 CompositionLocal.kt\nandroidx/compose/runtime/CompositionLocal\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n+ 4 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 5 Effects.kt\nandroidx/compose/runtime/EffectsKt\n+ 6 Effects.kt\nandroidx/compose/runtime/EffectsKt$rememberCoroutineScope$1\n+ 7 Row.kt\nandroidx/compose/foundation/layout/RowKt\n+ 8 Layout.kt\nandroidx/compose/ui/layout/LayoutKt\n+ 9 Composables.kt\nandroidx/compose/runtime/ComposablesKt\n+ 10 Composer.kt\nandroidx/compose/runtime/Updater\n+ 11 _Strings.kt\nkotlin/text/StringsKt___StringsKt\n+ 12 Box.kt\nandroidx/compose/foundation/layout/BoxKt\n+ 13 Column.kt\nandroidx/compose/foundation/layout/ColumnKt\n+ 14 SnapshotState.kt\nandroidx/compose/runtime/SnapshotStateKt__SnapshotStateKt\n+ 15 Dp.kt\nandroidx/compose/ui/unit/Dp\n+ 16 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 17 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 18 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt\n+ 19 SnapshotIntState.kt\nandroidx/compose/runtime/SnapshotIntStateKt__SnapshotIntStateKt\n+ 20 Effects.kt\nandroidx/compose/runtime/DisposableEffectScope\n*L\n1#1,3084:1\n77#2:3085\n77#2:3092\n77#2:3093\n77#2:3108\n77#2:3115\n77#2:3116\n77#2:3147\n77#2:3148\n77#2:3174\n77#2:3175\n77#2:3281\n77#2:3282\n77#2:3283\n77#2:3284\n77#2:3285\n77#2:3299\n77#2:3300\n77#2:3385\n77#2:3386\n77#2:3388\n77#2:3397\n77#2:3455\n77#2:3456\n77#2:3516\n77#2:3556\n77#2:3557\n77#2:3571\n77#2:3572\n77#2:3573\n77#2:3691\n77#2:3734\n77#2:3746\n77#2:3747\n77#2:3761\n77#2:3762\n77#2:3787\n77#2:4127\n77#2:4128\n77#2:4986\n77#2:5933\n77#2:6008\n77#2:6403\n77#2:6404\n77#2:6405\n77#2:6596\n77#2:6597\n77#2:6692\n77#2:6777\n77#2:6778\n1225#3,6:3086\n1225#3,6:3094\n1225#3,6:3101\n1225#3,6:3109\n1225#3,6:3117\n1225#3,6:3123\n1225#3,6:3129\n1225#3,6:3135\n1225#3,6:3141\n1225#3,6:3149\n1225#3,6:3155\n1225#3,6:3161\n1225#3,6:3168\n1225#3,6:3176\n1225#3,6:3182\n1225#3,6:3188\n1225#3,6:3194\n1225#3,6:3200\n1225#3,6:3206\n1225#3,6:3212\n1225#3,6:3218\n1225#3,6:3224\n1225#3,6:3230\n1225#3,6:3236\n1225#3,6:3242\n1225#3,6:3248\n1225#3,6:3254\n1225#3,6:3260\n1225#3,3:3271\n1228#3,3:3277\n1225#3,6:3292\n1225#3,6:3337\n1225#3,6:3343\n1225#3,6:3349\n1225#3,6:3355\n1225#3,6:3361\n1225#3,6:3367\n1225#3,6:3373\n1225#3,6:3389\n1225#3,6:3398\n1225#3,6:3404\n1225#3,6:3449\n1225#3,6:3490\n1225#3,6:3498\n1225#3,6:3506\n1225#3,6:3559\n1225#3,6:3574\n1225#3,6:3580\n1225#3,6:3586\n1225#3,6:3592\n1225#3,6:3635\n1225#3,6:3646\n1225#3,6:3753\n1225#3,6:3763\n1225#3,6:3769\n1225#3,6:3775\n1225#3,6:3781\n1225#3,6:3788\n1225#3,6:3794\n1225#3,6:3800\n1225#3,6:3806\n1225#3,6:3812\n1225#3,6:3818\n1225#3,6:3938\n1225#3,6:4021\n1225#3,6:4028\n1225#3,6:4085\n1225#3,6:4111\n1225#3,6:4117\n1225#3,6:4204\n1225#3,6:4255\n1225#3,6:4306\n1225#3,6:4357\n1225#3,6:4415\n1225#3,6:4421\n1225#3,6:4428\n1225#3,6:4434\n1225#3,6:4440\n1225#3,6:4446\n1225#3,6:4452\n1225#3,6:4458\n1225#3,6:4464\n1225#3,6:4491\n1225#3,6:4569\n1225#3,6:4581\n1225#3,6:4587\n1225#3,6:4594\n1225#3,6:4645\n1225#3,6:4854\n1225#3,6:4863\n1225#3,6:4904\n1225#3,6:4918\n1225#3,6:4960\n1225#3,6:4974\n1225#3,6:4980\n1225#3,6:4995\n1225#3,6:5002\n1225#3,6:5008\n1225#3,6:5089\n1225#3,6:5096\n1225#3,6:5162\n1225#3,6:5226\n1225#3,6:5244\n1225#3,6:5380\n1225#3,6:5390\n1225#3,6:5430\n1225#3,6:5440\n1225#3,6:5447\n1225#3,6:5495\n1225#3,6:5547\n1225#3,6:5650\n1225#3,6:5778\n1225#3,6:5784\n1225#3,6:5913\n1225#3,6:5919\n1225#3,6:5927\n1225#3,6:5934\n1225#3,6:5940\n1225#3,6:5961\n1225#3,6:6011\n1225#3,6:6053\n1225#3,6:6189\n1225#3,6:6210\n1225#3,6:6216\n1225#3,6:6222\n1225#3,6:6268\n1225#3,6:6274\n1225#3,6:6321\n1225#3,6:6368\n1225#3,6:6377\n1225#3,6:6391\n1225#3,6:6397\n1225#3,6:6406\n1225#3,6:6448\n1225#3,6:6456\n1225#3,6:6604\n1225#3,6:6648\n1225#3,6:6909\n1225#3,6:6931\n1225#3,6:7093\n1225#3,6:7230\n1225#3,6:7324\n149#4:3100\n149#4:3107\n149#4:3167\n159#4:3286\n149#4:3287\n149#4:3288\n149#4:3289\n149#4:3290\n149#4:3291\n149#4:3298\n149#4:3301\n149#4:3302\n149#4:3379\n149#4:3387\n159#4:3395\n149#4:3396\n149#4:3489\n149#4:3496\n149#4:3497\n149#4:3504\n149#4:3505\n149#4:3558\n149#4:3570\n149#4:3634\n149#4:3641\n149#4:3642\n149#4:3643\n149#4:3644\n149#4:3645\n149#4:3689\n149#4:3690\n149#4:3696\n149#4:3735\n149#4:3736\n149#4:3737\n149#4:3748\n149#4:3749\n149#4:3750\n149#4:3751\n149#4:3752\n159#4:3759\n149#4:3760\n149#4:3824\n149#4:3892\n149#4:3928\n149#4:3933\n149#4:3934\n149#4:3935\n149#4:3936\n149#4:3937\n149#4:3944\n149#4:3945\n149#4:3978\n149#4:3983\n149#4:3984\n149#4:3985\n149#4:4027\n149#4:4034\n149#4:4080\n149#4:4081\n149#4:4082\n149#4:4083\n149#4:4084\n149#4:4091\n149#4:4092\n149#4:4093\n149#4:4094\n149#4:4164\n149#4:4165\n149#4:4166\n149#4:4167\n149#4:4203\n149#4:4210\n149#4:4247\n149#4:4248\n149#4:4254\n149#4:4261\n149#4:4298\n149#4:4299\n149#4:4305\n149#4:4312\n149#4:4349\n149#4:4350\n149#4:4356\n149#4:4363\n149#4:4400\n149#4:4401\n149#4:4427\n149#4:4500\n149#4:4575\n149#4:4600\n149#4:4641\n149#4:4642\n149#4:4643\n149#4:4644\n149#4:4651\n149#4:4762\n149#4:4763\n149#4:4764\n149#4:4765\n149#4:4801\n149#4:4806\n149#4:4844\n149#4:4845\n149#4:4860\n149#4:4861\n149#4:4862\n149#4:4987\n149#4:4988\n149#4:4989\n149#4:4991\n149#4:4992\n149#4:4993\n149#4:5001\n149#4:5014\n149#4:5015\n149#4:5095\n149#4:5122\n149#4:5159\n149#4:5160\n149#4:5161\n149#4:5193\n149#4:5239\n149#4:5240\n149#4:5241\n149#4:5242\n149#4:5243\n149#4:5286\n149#4:5309\n149#4:5346\n149#4:5379\n149#4:5396\n149#4:5429\n149#4:5446\n149#4:5453\n149#4:5454\n149#4:5455\n149#4:5494\n149#4:5501\n149#4:5546\n149#4:5567\n149#4:5641\n149#4:5642\n149#4:5647\n149#4:5648\n149#4:5649\n149#4:5660\n149#4:5734\n149#4:5735\n149#4:5740\n149#4:5741\n149#4:5742\n149#4:5798\n149#4:5875\n149#4:5912\n149#4:5925\n149#4:5926\n149#4:5946\n149#4:5947\n149#4:5948\n149#4:5949\n149#4:5950\n149#4:5951\n149#4:5952\n149#4:5953\n149#4:5954\n149#4:5955\n149#4:5956\n149#4:5957\n149#4:5958\n149#4:5959\n149#4:5960\n149#4:5967\n149#4:6009\n149#4:6010\n149#4:6069\n149#4:6183\n149#4:6184\n149#4:6228\n149#4:6265\n149#4:6266\n149#4:6267\n149#4:6280\n149#4:6281\n149#4:6282\n149#4:6319\n149#4:6320\n149#4:6332\n149#4:6374\n149#4:6375\n149#4:6376\n149#4:6447\n149#4:6454\n149#4:6455\n149#4:6462\n149#4:6470\n159#4:6471\n149#4:6507\n149#4:6512\n149#4:6513\n149#4:6554\n159#4:6555\n149#4:6591\n149#4:6601\n149#4:6602\n149#4:6603\n149#4:6647\n149#4:6654\n149#4:6691\n149#4:6701\n149#4:6738\n149#4:6739\n149#4:6783\n149#4:6784\n149#4:6827\n149#4:6828\n149#4:6829\n149#4:6834\n149#4:6871\n149#4:6872\n149#4:6919\n149#4:6938\n149#4:6939\n149#4:6972\n149#4:7046\n149#4:7047\n149#4:7052\n149#4:7053\n149#4:7058\n149#4:7091\n149#4:7092\n149#4:7099\n149#4:7100\n149#4:7101\n149#4:7102\n149#4:7107\n149#4:7144\n149#4:7145\n149#4:7146\n149#4:7147\n149#4:7221\n149#4:7222\n149#4:7227\n149#4:7228\n149#4:7229\n149#4:7236\n149#4:7237\n149#4:7242\n149#4:7279\n149#4:7280\n149#4:7281\n149#4:7282\n149#4:7315\n159#4:7320\n149#4:7321\n149#4:7322\n149#4:7323\n149#4:7362\n481#5:3266\n480#5,4:3267\n484#5,2:3274\n488#5:3280\n480#6:3276\n99#7,3:3303\n102#7:3334\n106#7:3384\n99#7,3:3457\n102#7:3488\n106#7:3515\n99#7:3517\n95#7,7:3518\n102#7:3553\n106#7:3569\n99#7:3652\n95#7,7:3653\n102#7:3688\n106#7:3695\n99#7:3697\n95#7,7:3698\n102#7:3733\n106#7:3741\n99#7,3:3946\n102#7:3977\n106#7:3982\n99#7:4168\n97#7,5:4169\n102#7:4202\n99#7:4211\n96#7,6:4212\n102#7:4246\n106#7:4253\n99#7:4262\n96#7,6:4263\n102#7:4297\n106#7:4304\n99#7:4313\n96#7,6:4314\n102#7:4348\n106#7:4355\n99#7:4364\n96#7,6:4365\n102#7:4399\n106#7:4406\n106#7:4410\n99#7,3:4537\n102#7:4568\n106#7:4579\n99#7,3:4693\n102#7:4724\n99#7:4725\n95#7,7:4726\n102#7:4761\n106#7:4853\n106#7:4917\n99#7,3:5194\n102#7:5225\n106#7:5235\n99#7,3:5347\n102#7:5378\n106#7:5389\n99#7,3:5397\n102#7:5428\n106#7:5439\n99#7:5456\n96#7,6:5457\n102#7:5491\n106#7:5545\n99#7:5604\n95#7,7:5605\n102#7:5640\n106#7:5646\n99#7:5697\n95#7,7:5698\n102#7:5733\n106#7:5739\n99#7:5743\n97#7,5:5744\n102#7:5777\n106#7:5793\n99#7,3:5799\n102#7:5830\n106#7:5874\n99#7:6070\n96#7,6:6071\n102#7:6105\n99#7:6146\n95#7,7:6147\n102#7:6182\n106#7:6188\n106#7:6198\n99#7:6283\n97#7,5:6284\n102#7:6317\n106#7:6331\n99#7:6333\n97#7,5:6334\n102#7:6367\n106#7:6386\n99#7:6702\n96#7,6:6703\n102#7:6737\n106#7:6788\n99#7:7009\n95#7,7:7010\n102#7:7045\n106#7:7051\n99#7:7184\n95#7,7:7185\n102#7:7220\n106#7:7226\n99#7,3:7283\n102#7:7314\n106#7:7319\n79#8,6:3306\n86#8,4:3321\n90#8,2:3331\n94#8:3383\n79#8,6:3416\n86#8,4:3431\n90#8,2:3441\n94#8:3447\n79#8,6:3460\n86#8,4:3475\n90#8,2:3485\n94#8:3514\n79#8,6:3525\n86#8,4:3540\n90#8,2:3550\n94#8:3568\n79#8,6:3605\n86#8,4:3620\n90#8,2:3630\n79#8,6:3660\n86#8,4:3675\n90#8,2:3685\n94#8:3694\n79#8,6:3705\n86#8,4:3720\n90#8,2:3730\n94#8:3740\n94#8:3744\n79#8,6:3831\n86#8,4:3846\n90#8,2:3856\n79#8,6:3863\n86#8,4:3878\n90#8,2:3888\n79#8,6:3899\n86#8,4:3914\n90#8,2:3924\n94#8:3931\n79#8,6:3949\n86#8,4:3964\n90#8,2:3974\n94#8:3981\n79#8,6:3992\n86#8,4:4007\n90#8,2:4017\n79#8,6:4043\n86#8,4:4058\n90#8,2:4068\n94#8:4074\n94#8:4078\n94#8:4097\n94#8:4101\n79#8,6:4135\n86#8,4:4150\n90#8,2:4160\n79#8,6:4174\n86#8,4:4189\n90#8,2:4199\n79#8,6:4218\n86#8,4:4233\n90#8,2:4243\n94#8:4252\n79#8,6:4269\n86#8,4:4284\n90#8,2:4294\n94#8:4303\n79#8,6:4320\n86#8,4:4335\n90#8,2:4345\n94#8:4354\n79#8,6:4371\n86#8,4:4386\n90#8,2:4396\n94#8:4405\n94#8:4409\n94#8:4413\n79#8,6:4508\n86#8,4:4523\n90#8,2:4533\n79#8,6:4540\n86#8,4:4555\n90#8,2:4565\n94#8:4578\n94#8:4603\n79#8,6:4612\n86#8,4:4627\n90#8,2:4637\n94#8:4655\n79#8,6:4664\n86#8,4:4679\n90#8,2:4689\n79#8,6:4696\n86#8,4:4711\n90#8,2:4721\n79#8,6:4733\n86#8,4:4748\n90#8,2:4758\n79#8,6:4772\n86#8,4:4787\n90#8,2:4797\n94#8:4804\n79#8,6:4815\n86#8,4:4830\n90#8,2:4840\n94#8:4848\n94#8:4852\n79#8,6:4875\n86#8,4:4890\n90#8,2:4900\n94#8:4912\n94#8:4916\n79#8,6:4931\n86#8,4:4946\n90#8,2:4956\n94#8:4968\n94#8:4972\n79#8,6:5023\n86#8,4:5038\n90#8,2:5048\n79#8,6:5060\n86#8,4:5075\n90#8,2:5085\n94#8:5104\n94#8:5108\n79#8,6:5130\n86#8,4:5145\n90#8,2:5155\n94#8:5170\n79#8,6:5197\n86#8,4:5212\n90#8,2:5222\n94#8:5234\n79#8,6:5257\n86#8,4:5272\n90#8,2:5282\n94#8:5289\n79#8,6:5317\n86#8,4:5332\n90#8,2:5342\n79#8,6:5350\n86#8,4:5365\n90#8,2:5375\n94#8:5388\n79#8,6:5400\n86#8,4:5415\n90#8,2:5425\n94#8:5438\n79#8,6:5463\n86#8,4:5478\n90#8,2:5488\n79#8,6:5508\n86#8,4:5523\n90#8,2:5533\n94#8:5539\n94#8:5544\n94#8:5555\n79#8,6:5575\n86#8,4:5590\n90#8,2:5600\n79#8,6:5612\n86#8,4:5627\n90#8,2:5637\n94#8:5645\n94#8:5658\n79#8,6:5668\n86#8,4:5683\n90#8,2:5693\n79#8,6:5705\n86#8,4:5720\n90#8,2:5730\n94#8:5738\n79#8,6:5749\n86#8,4:5764\n90#8,2:5774\n94#8:5792\n94#8:5796\n79#8,6:5802\n86#8,4:5817\n90#8,2:5827\n79#8,6:5838\n86#8,4:5853\n90#8,2:5863\n94#8:5869\n94#8:5873\n79#8,6:5883\n86#8,4:5898\n90#8,2:5908\n94#8:5970\n79#8,6:5979\n86#8,4:5994\n90#8,2:6004\n79#8,6:6024\n86#8,4:6039\n90#8,2:6049\n94#8:6061\n94#8:6065\n79#8,6:6077\n86#8,4:6092\n90#8,2:6102\n79#8,6:6113\n86#8,4:6128\n90#8,2:6138\n94#8:6144\n79#8,6:6154\n86#8,4:6169\n90#8,2:6179\n94#8:6187\n94#8:6197\n79#8,6:6236\n86#8,4:6251\n90#8,2:6261\n79#8,6:6289\n86#8,4:6304\n90#8,2:6314\n94#8:6330\n79#8,6:6339\n86#8,4:6354\n90#8,2:6364\n94#8:6385\n94#8:6389\n79#8,6:6418\n86#8,4:6433\n90#8,2:6443\n94#8:6465\n79#8,6:6478\n86#8,4:6493\n90#8,2:6503\n94#8:6510\n79#8,6:6520\n86#8,4:6535\n90#8,2:6545\n94#8:6551\n79#8,6:6562\n86#8,4:6577\n90#8,2:6587\n94#8:6594\n79#8,6:6618\n86#8,4:6633\n90#8,2:6643\n79#8,6:6662\n86#8,4:6677\n90#8,2:6687\n94#8:6695\n94#8:6699\n79#8,6:6709\n86#8,4:6724\n90#8,2:6734\n79#8,6:6748\n86#8,4:6763\n90#8,2:6773\n94#8:6781\n94#8:6787\n79#8,6:6798\n86#8,4:6813\n90#8,2:6823\n94#8:6832\n79#8,6:6842\n86#8,4:6857\n90#8,2:6867\n79#8,6:6880\n86#8,4:6895\n90#8,2:6905\n94#8:6917\n94#8:6922\n79#8,6:6943\n86#8,4:6958\n90#8,2:6968\n79#8,6:6980\n86#8,4:6995\n90#8,2:7005\n79#8,6:7017\n86#8,4:7032\n90#8,2:7042\n94#8:7050\n94#8:7056\n79#8,6:7062\n86#8,4:7077\n90#8,2:7087\n94#8:7105\n79#8,6:7115\n86#8,4:7130\n90#8,2:7140\n79#8,6:7155\n86#8,4:7170\n90#8,2:7180\n79#8,6:7192\n86#8,4:7207\n90#8,2:7217\n94#8:7225\n94#8:7240\n79#8,6:7250\n86#8,4:7265\n90#8,2:7275\n79#8,6:7286\n86#8,4:7301\n90#8,2:7311\n94#8:7318\n94#8:7332\n94#8:7336\n94#8:7340\n368#9,9:3312\n377#9:3333\n378#9,2:3381\n368#9,9:3422\n377#9:3443\n378#9,2:3445\n368#9,9:3466\n377#9:3487\n378#9,2:3512\n368#9,9:3531\n377#9:3552\n378#9,2:3566\n368#9,9:3611\n377#9:3632\n368#9,9:3666\n377#9:3687\n378#9,2:3692\n368#9,9:3711\n377#9:3732\n378#9,2:3738\n378#9,2:3742\n368#9,9:3837\n377#9:3858\n368#9,9:3869\n377#9:3890\n368#9,9:3905\n377#9:3926\n378#9,2:3929\n368#9,9:3955\n377#9:3976\n378#9,2:3979\n368#9,9:3998\n377#9:4019\n368#9,9:4049\n377#9:4070\n378#9,2:4072\n378#9,2:4076\n378#9,2:4095\n378#9,2:4099\n368#9,9:4141\n377#9:4162\n368#9,9:4180\n377#9:4201\n368#9,9:4224\n377#9:4245\n378#9,2:4250\n368#9,9:4275\n377#9:4296\n378#9,2:4301\n368#9,9:4326\n377#9:4347\n378#9,2:4352\n368#9,9:4377\n377#9:4398\n378#9,2:4403\n378#9,2:4407\n378#9,2:4411\n368#9,9:4514\n377#9:4535\n368#9,9:4546\n377#9:4567\n378#9,2:4576\n378#9,2:4601\n368#9,9:4618\n377#9:4639\n378#9,2:4653\n368#9,9:4670\n377#9:4691\n368#9,9:4702\n377#9:4723\n368#9,9:4739\n377#9:4760\n368#9,9:4778\n377#9:4799\n378#9,2:4802\n368#9,9:4821\n377#9:4842\n378#9,2:4846\n378#9,2:4850\n368#9,9:4881\n377#9:4902\n378#9,2:4910\n378#9,2:4914\n368#9,9:4937\n377#9:4958\n378#9,2:4966\n378#9,2:4970\n368#9,9:5029\n377#9:5050\n368#9,9:5066\n377#9:5087\n378#9,2:5102\n378#9,2:5106\n368#9,9:5136\n377#9:5157\n378#9,2:5168\n368#9,9:5203\n377#9:5224\n378#9,2:5232\n368#9,9:5263\n377#9:5284\n378#9,2:5287\n368#9,9:5323\n377#9:5344\n368#9,9:5356\n377#9:5377\n378#9,2:5386\n368#9,9:5406\n377#9:5427\n378#9,2:5436\n368#9,9:5469\n377#9:5490\n368#9,9:5514\n377#9:5535\n378#9,2:5537\n378#9,2:5542\n378#9,2:5553\n368#9,9:5581\n377#9:5602\n368#9,9:5618\n377#9:5639\n378#9,2:5643\n378#9,2:5656\n368#9,9:5674\n377#9:5695\n368#9,9:5711\n377#9:5732\n378#9,2:5736\n368#9,9:5755\n377#9:5776\n378#9,2:5790\n378#9,2:5794\n368#9,9:5808\n377#9:5829\n368#9,9:5844\n377#9:5865\n378#9,2:5867\n378#9,2:5871\n368#9,9:5889\n377#9:5910\n378#9,2:5968\n368#9,9:5985\n377#9:6006\n368#9,9:6030\n377#9:6051\n378#9,2:6059\n378#9,2:6063\n368#9,9:6083\n377#9:6104\n368#9,9:6119\n377#9:6140\n378#9,2:6142\n368#9,9:6160\n377#9:6181\n378#9,2:6185\n378#9,2:6195\n368#9,9:6242\n377#9:6263\n368#9,9:6295\n377#9:6316\n378#9,2:6328\n368#9,9:6345\n377#9:6366\n378#9,2:6383\n378#9,2:6387\n368#9,9:6424\n377#9:6445\n378#9,2:6463\n368#9,9:6484\n377#9:6505\n378#9,2:6508\n368#9,9:6526\n377#9:6547\n378#9,2:6549\n368#9,9:6568\n377#9:6589\n378#9,2:6592\n368#9,9:6624\n377#9:6645\n368#9,9:6668\n377#9:6689\n378#9,2:6693\n378#9,2:6697\n368#9,9:6715\n377#9:6736\n368#9,9:6754\n377#9:6775\n378#9,2:6779\n378#9,2:6785\n368#9,9:6804\n377#9:6825\n378#9,2:6830\n368#9,9:6848\n377#9:6869\n368#9,9:6886\n377#9:6907\n378#9,2:6915\n378#9,2:6920\n368#9,9:6949\n377#9:6970\n368#9,9:6986\n377#9:7007\n368#9,9:7023\n377#9:7044\n378#9,2:7048\n378#9,2:7054\n368#9,9:7068\n377#9:7089\n378#9,2:7103\n368#9,9:7121\n377#9:7142\n368#9,9:7161\n377#9:7182\n368#9,9:7198\n377#9:7219\n378#9,2:7223\n378#9,2:7238\n368#9,9:7256\n377#9:7277\n368#9,9:7292\n377#9:7313\n378#9,2:7316\n378#9,2:7330\n378#9,2:7334\n378#9,2:7338\n4034#10,6:3325\n4034#10,6:3435\n4034#10,6:3479\n4034#10,6:3544\n4034#10,6:3624\n4034#10,6:3679\n4034#10,6:3724\n4034#10,6:3850\n4034#10,6:3882\n4034#10,6:3918\n4034#10,6:3968\n4034#10,6:4011\n4034#10,6:4062\n4034#10,6:4154\n4034#10,6:4193\n4034#10,6:4237\n4034#10,6:4288\n4034#10,6:4339\n4034#10,6:4390\n4034#10,6:4527\n4034#10,6:4559\n4034#10,6:4631\n4034#10,6:4683\n4034#10,6:4715\n4034#10,6:4752\n4034#10,6:4791\n4034#10,6:4834\n4034#10,6:4894\n4034#10,6:4950\n4034#10,6:5042\n4034#10,6:5079\n4034#10,6:5149\n4034#10,6:5216\n4034#10,6:5276\n4034#10,6:5336\n4034#10,6:5369\n4034#10,6:5419\n4034#10,6:5482\n4034#10,6:5527\n4034#10,6:5594\n4034#10,6:5631\n4034#10,6:5687\n4034#10,6:5724\n4034#10,6:5768\n4034#10,6:5821\n4034#10,6:5857\n4034#10,6:5902\n4034#10,6:5998\n4034#10,6:6043\n4034#10,6:6096\n4034#10,6:6132\n4034#10,6:6173\n4034#10,6:6255\n4034#10,6:6308\n4034#10,6:6358\n4034#10,6:6437\n4034#10,6:6497\n4034#10,6:6539\n4034#10,6:6581\n4034#10,6:6637\n4034#10,6:6681\n4034#10,6:6728\n4034#10,6:6767\n4034#10,6:6817\n4034#10,6:6861\n4034#10,6:6899\n4034#10,6:6962\n4034#10,6:6999\n4034#10,6:7036\n4034#10,6:7081\n4034#10,6:7134\n4034#10,6:7174\n4034#10,6:7211\n4034#10,6:7269\n4034#10,6:7305\n1188#11,2:3335\n1190#11:3380\n1188#11,2:3554\n1190#11:3565\n434#11:5297\n507#11,5:5298\n434#11:5303\n507#11,5:5304\n71#12:3410\n69#12,5:3411\n74#12:3444\n78#12:3448\n71#12:3825\n69#12,5:3826\n74#12:3859\n71#12:3893\n69#12,5:3894\n74#12:3927\n78#12:3932\n71#12:3986\n69#12,5:3987\n74#12:4020\n78#12:4079\n78#12:4102\n71#12:4129\n69#12,5:4130\n74#12:4163\n78#12:4414\n71#12:4766\n69#12,5:4767\n74#12:4800\n78#12:4805\n71#12:4869\n69#12,5:4870\n74#12:4903\n78#12:4913\n71#12:4924\n68#12,6:4925\n74#12:4959\n78#12:4969\n71#12:5016\n68#12,6:5017\n74#12:5051\n78#12:5109\n71#12:5250\n68#12,6:5251\n74#12:5285\n78#12:5290\n71#12:5502\n69#12,5:5503\n74#12:5536\n78#12:5540\n71#12:6017\n68#12,6:6018\n74#12:6052\n78#12:6062\n71#12:6412\n69#12,5:6413\n74#12:6446\n78#12:6466\n71#12:6472\n69#12,5:6473\n74#12:6506\n78#12:6511\n71#12:6514\n69#12,5:6515\n74#12:6548\n78#12:6552\n71#12:6556\n69#12,5:6557\n74#12:6590\n78#12:6595\n71#12:6655\n68#12,6:6656\n74#12:6690\n78#12:6696\n71#12:6835\n68#12,6:6836\n74#12:6870\n78#12:6923\n86#13:3598\n83#13,6:3599\n89#13:3633\n93#13:3745\n86#13,3:3860\n89#13:3891\n86#13:4035\n82#13,7:4036\n89#13:4071\n93#13:4075\n93#13:4098\n86#13:4502\n84#13,5:4503\n89#13:4536\n93#13:4604\n86#13:4605\n83#13,6:4606\n89#13:4640\n93#13:4656\n86#13:4657\n83#13,6:4658\n89#13:4692\n86#13:4807\n82#13,7:4808\n89#13:4843\n93#13:4849\n93#13:4973\n86#13:5052\n82#13,7:5053\n89#13:5088\n93#13:5105\n86#13:5123\n83#13,6:5124\n89#13:5158\n93#13:5171\n86#13:5310\n83#13,6:5311\n89#13:5345\n93#13:5556\n86#13:5568\n83#13,6:5569\n89#13:5603\n93#13:5659\n86#13:5661\n83#13,6:5662\n89#13:5696\n93#13:5797\n86#13:5831\n83#13,6:5832\n89#13:5866\n93#13:5870\n86#13:5876\n83#13,6:5877\n89#13:5911\n93#13:5971\n86#13:5972\n83#13,6:5973\n89#13:6007\n93#13:6066\n86#13:6106\n83#13,6:6107\n89#13:6141\n93#13:6145\n86#13:6229\n83#13,6:6230\n89#13:6264\n93#13:6390\n86#13:6610\n82#13,7:6611\n89#13:6646\n93#13:6700\n86#13:6740\n82#13,7:6741\n89#13:6776\n93#13:6782\n86#13:6791\n83#13,6:6792\n89#13:6826\n93#13:6833\n86#13:6873\n83#13,6:6874\n89#13:6908\n93#13:6918\n86#13,3:6940\n89#13:6971\n86#13:6973\n83#13,6:6974\n89#13:7008\n93#13:7057\n86#13,3:7059\n89#13:7090\n93#13:7106\n86#13:7108\n83#13,6:7109\n89#13:7143\n86#13:7148\n83#13,6:7149\n89#13:7183\n93#13:7241\n86#13:7243\n83#13,6:7244\n89#13:7278\n93#13:7333\n93#13:7337\n93#13:7341\n81#14:4103\n81#14:4104\n81#14:4105\n81#14:4106\n81#14:4107\n81#14:4108\n107#14,2:4109\n81#14:4123\n81#14:4124\n81#14:4125\n81#14:4126\n81#14:4470\n81#14:4471\n81#14:4472\n81#14:4473\n81#14:4474\n81#14:4475\n81#14:4476\n81#14:4477\n81#14:4478\n81#14:4479\n107#14,2:4480\n81#14:4482\n81#14:4483\n81#14:4484\n107#14,2:4485\n81#14:4487\n107#14,2:4488\n81#14:4490\n81#14:4497\n81#14:4498\n81#14:5175\n107#14,2:5176\n81#14:5178\n107#14,2:5179\n81#14:5181\n107#14,2:5182\n81#14:5184\n107#14,2:5185\n81#14:5187\n107#14,2:5188\n81#14:5190\n107#14,2:5191\n81#14:5236\n81#14:5237\n81#14:5238\n81#14:5291\n107#14,2:5292\n81#14:5294\n107#14,2:5295\n81#14:5557\n81#14:5558\n81#14:5559\n107#14,2:5560\n81#14:6067\n81#14:6068\n81#14:6199\n107#14,2:6200\n81#14:6202\n81#14:6203\n81#14:6204\n107#14,2:6205\n81#14:6207\n107#14,2:6208\n81#14:6467\n81#14:6468\n81#14:6469\n81#14:6553\n81#14:6598\n107#14,2:6599\n81#14:6789\n81#14:6790\n81#14:6929\n81#14:6930\n81#14:6937\n81#14:7342\n107#14,2:7343\n81#14:7345\n107#14,2:7346\n81#14:7348\n107#14,2:7349\n81#14:7351\n107#14,2:7352\n81#14:7359\n81#14:7360\n81#14:7361\n84#15:4249\n84#15:4300\n84#15:4351\n84#15:4402\n51#15:4501\n51#15:4652\n84#15:4990\n84#15:4994\n1#16:4499\n1869#17:4580\n1870#17:4593\n1878#17,2:5492\n1880#17:5541\n1869#17:6318\n1870#17:6327\n143#18,12:5110\n78#19:5172\n111#19,2:5173\n64#20,5:5562\n64#20,5:6924\n64#20,5:7354\n*S KotlinDebug\n*F\n+ 1 MainActivity.kt\ncom/example/MainActivityKt\n*L\n173#1:3085\n183#1:3092\n185#1:3093\n483#1:3108\n496#1:3115\n497#1:3116\n520#1:3147\n521#1:3148\n931#1:3174\n932#1:3175\n1003#1:3281\n1452#1:3282\n1453#1:3283\n1454#1:3284\n1455#1:3285\n1561#1:3299\n1562#1:3300\n1630#1:3385\n1631#1:3386\n1809#1:3388\n1822#1:3397\n1858#1:3455\n1859#1:3456\n1968#1:3516\n1980#1:3556\n1981#1:3557\n2029#1:3571\n2030#1:3572\n2031#1:3573\n2195#1:3691\n2204#1:3734\n2228#1:3746\n2229#1:3747\n2359#1:3761\n2361#1:3762\n2729#1:3787\n244#1:4127\n246#1:4128\n543#1:4986\n1297#1:5933\n1047#1:6008\n1658#1:6403\n1659#1:6404\n1660#1:6405\n2009#1:6596\n2010#1:6597\n2125#1:6692\n2164#1:6777\n2170#1:6778\n182#1:3086,6\n187#1:3094,6\n196#1:3101,6\n484#1:3109,6\n498#1:3117,6\n502#1:3123,6\n503#1:3129,6\n507#1:3135,6\n514#1:3141,6\n824#1:3149,6\n878#1:3155,6\n884#1:3161,6\n890#1:3168,6\n933#1:3176,6\n935#1:3182,6\n938#1:3188,6\n939#1:3194,6\n941#1:3200,6\n948#1:3206,6\n952#1:3212,6\n953#1:3218,6\n954#1:3224,6\n955#1:3230,6\n959#1:3236,6\n961#1:3242,6\n968#1:3248,6\n981#1:3254,6\n997#1:3260,6\n1002#1:3271,3\n1002#1:3277,3\n1480#1:3292,6\n1582#1:3337,6\n1583#1:3343,6\n1584#1:3349,6\n1586#1:3355,6\n1595#1:3361,6\n1601#1:3367,6\n1615#1:3373,6\n1810#1:3389,6\n1826#1:3398,6\n1843#1:3404,6\n1857#1:3449,6\n1869#1:3490,6\n1893#1:3498,6\n1933#1:3506,6\n1987#1:3559,6\n2032#1:3574,6\n2035#1:3580,6\n2044#1:3586,6\n2046#1:3592,6\n2059#1:3635,6\n2142#1:3646,6\n2266#1:3753,6\n2364#1:3763,6\n2369#1:3769,6\n2437#1:3775,6\n2439#1:3781,6\n2730#1:3788,6\n2731#1:3794,6\n2732#1:3800,6\n2733#1:3806,6\n2736#1:3812,6\n2741#1:3818,6\n2820#1:3938,6\n2935#1:4021,6\n2944#1:4028,6\n3001#1:4085,6\n200#1:4111,6\n212#1:4117,6\n268#1:4204,6\n303#1:4255,6\n338#1:4306,6\n373#1:4357,6\n235#1:4415,6\n238#1:4421,6\n442#1:4428,6\n467#1:4434,6\n409#1:4440,6\n416#1:4446,6\n419#1:4452,6\n426#1:4458,6\n429#1:4464,6\n582#1:4491,6\n730#1:4569,6\n747#1:4581,6\n748#1:4587,6\n754#1:4594,6\n773#1:4645,6\n629#1:4854,6\n647#1:4863,6\n659#1:4904,6\n683#1:4918,6\n705#1:4960,6\n809#1:4974,6\n818#1:4980,6\n558#1:4995,6\n814#1:5002,6\n815#1:5008,6\n835#1:5089,6\n861#1:5096,6\n911#1:5162,6\n1025#1:5226,6\n1083#1:5244,6\n1150#1:5380,6\n1165#1:5390,6\n1184#1:5430,6\n1199#1:5440,6\n1218#1:5447,6\n1245#1:5495,6\n1266#1:5547,6\n1335#1:5650,6\n1387#1:5778,6\n1394#1:5784,6\n1133#1:5913,6\n1134#1:5919,6\n1289#1:5927,6\n1298#1:5934,6\n1300#1:5940,6\n1410#1:5961,6\n1053#1:6011,6\n1107#1:6053,6\n1546#1:6189,6\n1688#1:6210,6\n1696#1:6216,6\n1701#1:6222,6\n1737#1:6268,6\n1721#1:6274,6\n1760#1:6321,6\n1772#1:6368,6\n1784#1:6377,6\n1640#1:6391,6\n1641#1:6397,6\n1666#1:6406,6\n1676#1:6448,6\n1683#1:6456,6\n2070#1:6604,6\n2103#1:6648,6\n2302#1:6909,6\n2543#1:6931,6\n2528#1:7093,6\n2625#1:7230,6\n2693#1:7324,6\n222#1:3100\n482#1:3107\n892#1:3167\n1462#1:3286\n1462#1:3287\n1473#1:3288\n1477#1:3289\n1478#1:3290\n1479#1:3291\n1486#1:3298\n1567#1:3301\n1568#1:3302\n1621#1:3379\n1805#1:3387\n1818#1:3395\n1818#1:3396\n1867#1:3489\n1886#1:3496\n1891#1:3497\n1925#1:3504\n1931#1:3505\n1982#1:3558\n2027#1:3570\n2056#1:3634\n2065#1:3641\n2133#1:3642\n2137#1:3643\n2140#1:3644\n2141#1:3645\n2186#1:3689\n2190#1:3690\n2199#1:3696\n2213#1:3735\n2216#1:3736\n2217#1:3737\n2255#1:3748\n2257#1:3749\n2260#1:3750\n2262#1:3751\n2263#1:3752\n2271#1:3759\n2273#1:3760\n2774#1:3824\n2786#1:3892\n2795#1:3928\n2799#1:3933\n2810#1:3934\n2818#1:3935\n2850#1:3936\n2853#1:3937\n2878#1:3944\n2879#1:3945\n2883#1:3978\n2906#1:3983\n2917#1:3984\n2922#1:3985\n2938#1:4027\n2947#1:4034\n2972#1:4080\n2979#1:4081\n2982#1:4082\n2999#1:4083\n3000#1:4084\n3024#1:4091\n3032#1:4092\n3035#1:4093\n3077#1:4094\n253#1:4164\n254#1:4165\n256#1:4166\n258#1:4167\n266#1:4203\n274#1:4210\n281#1:4247\n284#1:4248\n301#1:4254\n309#1:4261\n316#1:4298\n319#1:4299\n336#1:4305\n344#1:4312\n351#1:4349\n354#1:4350\n371#1:4356\n379#1:4363\n386#1:4400\n389#1:4401\n242#1:4427\n716#1:4500\n730#1:4575\n755#1:4600\n771#1:4641\n778#1:4642\n779#1:4643\n780#1:4644\n792#1:4651\n596#1:4762\n597#1:4763\n598#1:4764\n600#1:4765\n605#1:4801\n609#1:4806\n617#1:4844\n624#1:4845\n639#1:4860\n640#1:4861\n643#1:4862\n546#1:4987\n547#1:4988\n548#1:4989\n552#1:4991\n553#1:4992\n554#1:4993\n562#1:5001\n828#1:5014\n830#1:5015\n859#1:5095\n897#1:5122\n905#1:5159\n908#1:5160\n910#1:5161\n1012#1:5193\n1065#1:5239\n1073#1:5240\n1076#1:5241\n1079#1:5242\n1081#1:5243\n1094#1:5286\n1142#1:5309\n1144#1:5346\n1159#1:5379\n1178#1:5396\n1193#1:5429\n1213#1:5446\n1233#1:5453\n1236#1:5454\n1237#1:5455\n1243#1:5494\n1256#1:5501\n1262#1:5546\n1320#1:5567\n1322#1:5641\n1323#1:5642\n1326#1:5647\n1333#1:5648\n1342#1:5649\n1371#1:5660\n1373#1:5734\n1374#1:5735\n1377#1:5740\n1384#1:5741\n1385#1:5742\n1424#1:5798\n1128#1:5875\n1131#1:5912\n1138#1:5925\n1140#1:5926\n1314#1:5946\n1315#1:5947\n1317#1:5948\n1318#1:5949\n1350#1:5950\n1351#1:5951\n1353#1:5952\n1354#1:5953\n1365#1:5954\n1366#1:5955\n1368#1:5956\n1369#1:5957\n1417#1:5958\n1420#1:5959\n1421#1:5960\n1441#1:5967\n1051#1:6009\n1052#1:6010\n1541#1:6069\n1521#1:6183\n1525#1:6184\n1704#1:6228\n1713#1:6265\n1717#1:6266\n1724#1:6267\n1746#1:6280\n1748#1:6281\n1750#1:6282\n1756#1:6319\n1759#1:6320\n1769#1:6332\n1781#1:6374\n1789#1:6375\n1790#1:6376\n1672#1:6447\n1681#1:6454\n1682#1:6455\n1686#1:6462\n1876#1:6470\n1879#1:6471\n1882#1:6507\n1904#1:6512\n1911#1:6513\n1949#1:6554\n1952#1:6555\n1959#1:6591\n2076#1:6601\n2083#1:6602\n2070#1:6603\n2093#1:6647\n2119#1:6654\n2123#1:6691\n2149#1:6701\n2153#1:6738\n2157#1:6739\n2173#1:6783\n2178#1:6784\n2323#1:6827\n2325#1:6828\n2329#1:6829\n2279#1:6834\n2290#1:6871\n2296#1:6872\n2346#1:6919\n2442#1:6938\n2449#1:6939\n2457#1:6972\n2465#1:7046\n2469#1:7047\n2490#1:7052\n2493#1:7053\n2513#1:7058\n2522#1:7091\n2531#1:7092\n2550#1:7099\n2562#1:7100\n2565#1:7101\n2567#1:7102\n2574#1:7107\n2596#1:7144\n2597#1:7145\n2598#1:7146\n2599#1:7147\n2609#1:7221\n2611#1:7222\n2621#1:7227\n2630#1:7228\n2631#1:7229\n2634#1:7236\n2643#1:7237\n2667#1:7242\n2673#1:7279\n2674#1:7280\n2675#1:7281\n2676#1:7282\n2681#1:7315\n2698#1:7320\n2699#1:7321\n2700#1:7322\n2701#1:7323\n3020#1:7362\n1002#1:3266\n1002#1:3267,4\n1002#1:3274,2\n1002#1:3280\n1002#1:3276\n1564#1:3303,3\n1564#1:3334\n1564#1:3384\n1860#1:3457,3\n1860#1:3488\n1860#1:3515\n1970#1:3517\n1970#1:3518,7\n1970#1:3553\n1970#1:3569\n2183#1:3652\n2183#1:3653,7\n2183#1:3688\n2183#1:3695\n2200#1:3697\n2200#1:3698,7\n2200#1:3733\n2200#1:3741\n2876#1:3946,3\n2876#1:3977\n2876#1:3982\n251#1:4168\n251#1:4169,5\n251#1:4202\n264#1:4211\n264#1:4212,6\n264#1:4246\n264#1:4253\n299#1:4262\n299#1:4263,6\n299#1:4297\n299#1:4304\n334#1:4313\n334#1:4314,6\n334#1:4348\n334#1:4355\n369#1:4364\n369#1:4365,6\n369#1:4399\n369#1:4406\n251#1:4410\n718#1:4537,3\n718#1:4568\n718#1:4579\n588#1:4693,3\n588#1:4724\n593#1:4725\n593#1:4726,7\n593#1:4761\n593#1:4853\n588#1:4917\n1008#1:5194,3\n1008#1:5225\n1008#1:5235\n1146#1:5347,3\n1146#1:5378\n1146#1:5389\n1180#1:5397,3\n1180#1:5428\n1180#1:5439\n1237#1:5456\n1237#1:5457,6\n1237#1:5491\n1237#1:5545\n1321#1:5604\n1321#1:5605,7\n1321#1:5640\n1321#1:5646\n1372#1:5697\n1372#1:5698,7\n1372#1:5733\n1372#1:5739\n1385#1:5743\n1385#1:5744,5\n1385#1:5777\n1385#1:5793\n1423#1:5799,3\n1423#1:5830\n1423#1:5874\n1488#1:6070\n1488#1:6071,6\n1488#1:6105\n1517#1:6146\n1517#1:6147,7\n1517#1:6182\n1517#1:6188\n1488#1:6198\n1749#1:6283\n1749#1:6284,5\n1749#1:6317\n1749#1:6331\n1770#1:6333\n1770#1:6334,5\n1770#1:6367\n1770#1:6386\n2147#1:6702\n2147#1:6703,6\n2147#1:6737\n2147#1:6788\n2459#1:7009\n2459#1:7010,7\n2459#1:7045\n2459#1:7051\n2601#1:7184\n2601#1:7185,7\n2601#1:7220\n2601#1:7226\n2669#1:7283,3\n2669#1:7314\n2669#1:7319\n1564#1:3306,6\n1564#1:3321,4\n1564#1:3331,2\n1564#1:3383\n1824#1:3416,6\n1824#1:3431,4\n1824#1:3441,2\n1824#1:3447\n1860#1:3460,6\n1860#1:3475,4\n1860#1:3485,2\n1860#1:3514\n1970#1:3525,6\n1970#1:3540,4\n1970#1:3550,2\n1970#1:3568\n2050#1:3605,6\n2050#1:3620,4\n2050#1:3630,2\n2183#1:3660,6\n2183#1:3675,4\n2183#1:3685,2\n2183#1:3694\n2200#1:3705,6\n2200#1:3720,4\n2200#1:3730,2\n2200#1:3740\n2050#1:3744\n2770#1:3831,6\n2770#1:3846,4\n2770#1:3856,2\n2777#1:3863,6\n2777#1:3878,4\n2777#1:3888,2\n2784#1:3899,6\n2784#1:3914,4\n2784#1:3924,2\n2784#1:3931\n2876#1:3949,6\n2876#1:3964,4\n2876#1:3974,2\n2876#1:3981\n2920#1:3992,6\n2920#1:4007,4\n2920#1:4017,2\n2951#1:4043,6\n2951#1:4058,4\n2951#1:4068,2\n2951#1:4074\n2920#1:4078\n2777#1:4097\n2770#1:4101\n247#1:4135,6\n247#1:4150,4\n247#1:4160,2\n251#1:4174,6\n251#1:4189,4\n251#1:4199,2\n264#1:4218,6\n264#1:4233,4\n264#1:4243,2\n264#1:4252\n299#1:4269,6\n299#1:4284,4\n299#1:4294,2\n299#1:4303\n334#1:4320,6\n334#1:4335,4\n334#1:4345,2\n334#1:4354\n369#1:4371,6\n369#1:4386,4\n369#1:4396,2\n369#1:4405\n251#1:4409\n247#1:4413\n714#1:4508,6\n714#1:4523,4\n714#1:4533,2\n718#1:4540,6\n718#1:4555,4\n718#1:4565,2\n718#1:4578\n714#1:4603\n762#1:4612,6\n762#1:4627,4\n762#1:4637,2\n762#1:4655\n564#1:4664,6\n564#1:4679,4\n564#1:4689,2\n588#1:4696,6\n588#1:4711,4\n588#1:4721,2\n593#1:4733,6\n593#1:4748,4\n593#1:4758,2\n594#1:4772,6\n594#1:4787,4\n594#1:4797,2\n594#1:4804\n610#1:4815,6\n610#1:4830,4\n610#1:4840,2\n610#1:4848\n593#1:4852\n637#1:4875,6\n637#1:4890,4\n637#1:4900,2\n637#1:4912\n588#1:4916\n698#1:4931,6\n698#1:4946,4\n698#1:4956,2\n698#1:4968\n564#1:4972\n825#1:5023,6\n825#1:5038,4\n825#1:5048,2\n832#1:5060,6\n832#1:5075,4\n832#1:5085,2\n832#1:5104\n825#1:5108\n894#1:5130,6\n894#1:5145,4\n894#1:5155,2\n894#1:5170\n1008#1:5197,6\n1008#1:5212,4\n1008#1:5222,2\n1008#1:5234\n1069#1:5257,6\n1069#1:5272,4\n1069#1:5282,2\n1069#1:5289\n1142#1:5317,6\n1142#1:5332,4\n1142#1:5342,2\n1146#1:5350,6\n1146#1:5365,4\n1146#1:5375,2\n1146#1:5388\n1180#1:5400,6\n1180#1:5415,4\n1180#1:5425,2\n1180#1:5438\n1237#1:5463,6\n1237#1:5478,4\n1237#1:5488,2\n1240#1:5508,6\n1240#1:5523,4\n1240#1:5533,2\n1240#1:5539\n1237#1:5544\n1142#1:5555\n1320#1:5575,6\n1320#1:5590,4\n1320#1:5600,2\n1321#1:5612,6\n1321#1:5627,4\n1321#1:5637,2\n1321#1:5645\n1320#1:5658\n1371#1:5668,6\n1371#1:5683,4\n1371#1:5693,2\n1372#1:5705,6\n1372#1:5720,4\n1372#1:5730,2\n1372#1:5738\n1385#1:5749,6\n1385#1:5764,4\n1385#1:5774,2\n1385#1:5792\n1371#1:5796\n1423#1:5802,6\n1423#1:5817,4\n1423#1:5827,2\n1428#1:5838,6\n1428#1:5853,4\n1428#1:5863,2\n1428#1:5869\n1423#1:5873\n1125#1:5883,6\n1125#1:5898,4\n1125#1:5908,2\n1125#1:5970\n1042#1:5979,6\n1042#1:5994,4\n1042#1:6004,2\n1102#1:6024,6\n1102#1:6039,4\n1102#1:6049,2\n1102#1:6061\n1042#1:6065\n1488#1:6077,6\n1488#1:6092,4\n1488#1:6102,2\n1501#1:6113,6\n1501#1:6128,4\n1501#1:6138,2\n1501#1:6144\n1517#1:6154,6\n1517#1:6169,4\n1517#1:6179,2\n1517#1:6187\n1488#1:6197\n1698#1:6236,6\n1698#1:6251,4\n1698#1:6261,2\n1749#1:6289,6\n1749#1:6304,4\n1749#1:6314,2\n1749#1:6330\n1770#1:6339,6\n1770#1:6354,4\n1770#1:6364,2\n1770#1:6385\n1698#1:6389\n1662#1:6418,6\n1662#1:6433,4\n1662#1:6443,2\n1662#1:6465\n1874#1:6478,6\n1874#1:6493,4\n1874#1:6503,2\n1874#1:6510\n1902#1:6520,6\n1902#1:6535,4\n1902#1:6545,2\n1902#1:6551\n1947#1:6562,6\n1947#1:6577,4\n1947#1:6587,2\n1947#1:6594\n2087#1:6618,6\n2087#1:6633,4\n2087#1:6643,2\n2098#1:6662,6\n2098#1:6677,4\n2098#1:6687,2\n2098#1:6695\n2087#1:6699\n2147#1:6709,6\n2147#1:6724,4\n2147#1:6734,2\n2158#1:6748,6\n2158#1:6763,4\n2158#1:6773,2\n2158#1:6781\n2147#1:6787\n2310#1:6798,6\n2310#1:6813,4\n2310#1:6823,2\n2310#1:6832\n2276#1:6842,6\n2276#1:6857,4\n2276#1:6867,2\n2293#1:6880,6\n2293#1:6895,4\n2293#1:6905,2\n2293#1:6917\n2276#1:6922\n2444#1:6943,6\n2444#1:6958,4\n2444#1:6968,2\n2455#1:6980,6\n2455#1:6995,4\n2455#1:7005,2\n2459#1:7017,6\n2459#1:7032,4\n2459#1:7042,2\n2459#1:7050\n2455#1:7056\n2509#1:7062,6\n2509#1:7077,4\n2509#1:7087,2\n2509#1:7105\n2572#1:7115,6\n2572#1:7130,4\n2572#1:7140,2\n2592#1:7155,6\n2592#1:7170,4\n2592#1:7180,2\n2601#1:7192,6\n2601#1:7207,4\n2601#1:7217,2\n2601#1:7225\n2592#1:7240\n2665#1:7250,6\n2665#1:7265,4\n2665#1:7275,2\n2669#1:7286,6\n2669#1:7301,4\n2669#1:7311,2\n2669#1:7318\n2665#1:7332\n2572#1:7336\n2444#1:7340\n1564#1:3312,9\n1564#1:3333\n1564#1:3381,2\n1824#1:3422,9\n1824#1:3443\n1824#1:3445,2\n1860#1:3466,9\n1860#1:3487\n1860#1:3512,2\n1970#1:3531,9\n1970#1:3552\n1970#1:3566,2\n2050#1:3611,9\n2050#1:3632\n2183#1:3666,9\n2183#1:3687\n2183#1:3692,2\n2200#1:3711,9\n2200#1:3732\n2200#1:3738,2\n2050#1:3742,2\n2770#1:3837,9\n2770#1:3858\n2777#1:3869,9\n2777#1:3890\n2784#1:3905,9\n2784#1:3926\n2784#1:3929,2\n2876#1:3955,9\n2876#1:3976\n2876#1:3979,2\n2920#1:3998,9\n2920#1:4019\n2951#1:4049,9\n2951#1:4070\n2951#1:4072,2\n2920#1:4076,2\n2777#1:4095,2\n2770#1:4099,2\n247#1:4141,9\n247#1:4162\n251#1:4180,9\n251#1:4201\n264#1:4224,9\n264#1:4245\n264#1:4250,2\n299#1:4275,9\n299#1:4296\n299#1:4301,2\n334#1:4326,9\n334#1:4347\n334#1:4352,2\n369#1:4377,9\n369#1:4398\n369#1:4403,2\n251#1:4407,2\n247#1:4411,2\n714#1:4514,9\n714#1:4535\n718#1:4546,9\n718#1:4567\n718#1:4576,2\n714#1:4601,2\n762#1:4618,9\n762#1:4639\n762#1:4653,2\n564#1:4670,9\n564#1:4691\n588#1:4702,9\n588#1:4723\n593#1:4739,9\n593#1:4760\n594#1:4778,9\n594#1:4799\n594#1:4802,2\n610#1:4821,9\n610#1:4842\n610#1:4846,2\n593#1:4850,2\n637#1:4881,9\n637#1:4902\n637#1:4910,2\n588#1:4914,2\n698#1:4937,9\n698#1:4958\n698#1:4966,2\n564#1:4970,2\n825#1:5029,9\n825#1:5050\n832#1:5066,9\n832#1:5087\n832#1:5102,2\n825#1:5106,2\n894#1:5136,9\n894#1:5157\n894#1:5168,2\n1008#1:5203,9\n1008#1:5224\n1008#1:5232,2\n1069#1:5263,9\n1069#1:5284\n1069#1:5287,2\n1142#1:5323,9\n1142#1:5344\n1146#1:5356,9\n1146#1:5377\n1146#1:5386,2\n1180#1:5406,9\n1180#1:5427\n1180#1:5436,2\n1237#1:5469,9\n1237#1:5490\n1240#1:5514,9\n1240#1:5535\n1240#1:5537,2\n1237#1:5542,2\n1142#1:5553,2\n1320#1:5581,9\n1320#1:5602\n1321#1:5618,9\n1321#1:5639\n1321#1:5643,2\n1320#1:5656,2\n1371#1:5674,9\n1371#1:5695\n1372#1:5711,9\n1372#1:5732\n1372#1:5736,2\n1385#1:5755,9\n1385#1:5776\n1385#1:5790,2\n1371#1:5794,2\n1423#1:5808,9\n1423#1:5829\n1428#1:5844,9\n1428#1:5865\n1428#1:5867,2\n1423#1:5871,2\n1125#1:5889,9\n1125#1:5910\n1125#1:5968,2\n1042#1:5985,9\n1042#1:6006\n1102#1:6030,9\n1102#1:6051\n1102#1:6059,2\n1042#1:6063,2\n1488#1:6083,9\n1488#1:6104\n1501#1:6119,9\n1501#1:6140\n1501#1:6142,2\n1517#1:6160,9\n1517#1:6181\n1517#1:6185,2\n1488#1:6195,2\n1698#1:6242,9\n1698#1:6263\n1749#1:6295,9\n1749#1:6316\n1749#1:6328,2\n1770#1:6345,9\n1770#1:6366\n1770#1:6383,2\n1698#1:6387,2\n1662#1:6424,9\n1662#1:6445\n1662#1:6463,2\n1874#1:6484,9\n1874#1:6505\n1874#1:6508,2\n1902#1:6526,9\n1902#1:6547\n1902#1:6549,2\n1947#1:6568,9\n1947#1:6589\n1947#1:6592,2\n2087#1:6624,9\n2087#1:6645\n2098#1:6668,9\n2098#1:6689\n2098#1:6693,2\n2087#1:6697,2\n2147#1:6715,9\n2147#1:6736\n2158#1:6754,9\n2158#1:6775\n2158#1:6779,2\n2147#1:6785,2\n2310#1:6804,9\n2310#1:6825\n2310#1:6830,2\n2276#1:6848,9\n2276#1:6869\n2293#1:6886,9\n2293#1:6907\n2293#1:6915,2\n2276#1:6920,2\n2444#1:6949,9\n2444#1:6970\n2455#1:6986,9\n2455#1:7007\n2459#1:7023,9\n2459#1:7044\n2459#1:7048,2\n2455#1:7054,2\n2509#1:7068,9\n2509#1:7089\n2509#1:7103,2\n2572#1:7121,9\n2572#1:7142\n2592#1:7161,9\n2592#1:7182\n2601#1:7198,9\n2601#1:7219\n2601#1:7223,2\n2592#1:7238,2\n2665#1:7256,9\n2665#1:7277\n2669#1:7292,9\n2669#1:7313\n2669#1:7316,2\n2665#1:7330,2\n2572#1:7334,2\n2444#1:7338,2\n1564#1:3325,6\n1824#1:3435,6\n1860#1:3479,6\n1970#1:3544,6\n2050#1:3624,6\n2183#1:3679,6\n2200#1:3724,6\n2770#1:3850,6\n2777#1:3882,6\n2784#1:3918,6\n2876#1:3968,6\n2920#1:4011,6\n2951#1:4062,6\n247#1:4154,6\n251#1:4193,6\n264#1:4237,6\n299#1:4288,6\n334#1:4339,6\n369#1:4390,6\n714#1:4527,6\n718#1:4559,6\n762#1:4631,6\n564#1:4683,6\n588#1:4715,6\n593#1:4752,6\n594#1:4791,6\n610#1:4834,6\n637#1:4894,6\n698#1:4950,6\n825#1:5042,6\n832#1:5079,6\n894#1:5149,6\n1008#1:5216,6\n1069#1:5276,6\n1142#1:5336,6\n1146#1:5369,6\n1180#1:5419,6\n1237#1:5482,6\n1240#1:5527,6\n1320#1:5594,6\n1321#1:5631,6\n1371#1:5687,6\n1372#1:5724,6\n1385#1:5768,6\n1423#1:5821,6\n1428#1:5857,6\n1125#1:5902,6\n1042#1:5998,6\n1102#1:6043,6\n1488#1:6096,6\n1501#1:6132,6\n1517#1:6173,6\n1698#1:6255,6\n1749#1:6308,6\n1770#1:6358,6\n1662#1:6437,6\n1874#1:6497,6\n1902#1:6539,6\n1947#1:6581,6\n2087#1:6637,6\n2098#1:6681,6\n2147#1:6728,6\n2158#1:6767,6\n2310#1:6817,6\n2276#1:6861,6\n2293#1:6899,6\n2444#1:6962,6\n2455#1:6999,6\n2459#1:7036,6\n2509#1:7081,6\n2572#1:7134,6\n2592#1:7174,6\n2601#1:7211,6\n2665#1:7269,6\n2669#1:7305,6\n1581#1:3335,2\n1581#1:3380\n1974#1:3554,2\n1974#1:3565\n1151#1:5297\n1151#1:5298,5\n1185#1:5303\n1185#1:5304,5\n1824#1:3410\n1824#1:3411,5\n1824#1:3444\n1824#1:3448\n2770#1:3825\n2770#1:3826,5\n2770#1:3859\n2784#1:3893\n2784#1:3894,5\n2784#1:3927\n2784#1:3932\n2920#1:3986\n2920#1:3987,5\n2920#1:4020\n2920#1:4079\n2770#1:4102\n247#1:4129\n247#1:4130,5\n247#1:4163\n247#1:4414\n594#1:4766\n594#1:4767,5\n594#1:4800\n594#1:4805\n637#1:4869\n637#1:4870,5\n637#1:4903\n637#1:4913\n698#1:4924\n698#1:4925,6\n698#1:4959\n698#1:4969\n825#1:5016\n825#1:5017,6\n825#1:5051\n825#1:5109\n1069#1:5250\n1069#1:5251,6\n1069#1:5285\n1069#1:5290\n1240#1:5502\n1240#1:5503,5\n1240#1:5536\n1240#1:5540\n1102#1:6017\n1102#1:6018,6\n1102#1:6052\n1102#1:6062\n1662#1:6412\n1662#1:6413,5\n1662#1:6446\n1662#1:6466\n1874#1:6472\n1874#1:6473,5\n1874#1:6506\n1874#1:6511\n1902#1:6514\n1902#1:6515,5\n1902#1:6548\n1902#1:6552\n1947#1:6556\n1947#1:6557,5\n1947#1:6590\n1947#1:6595\n2098#1:6655\n2098#1:6656,6\n2098#1:6690\n2098#1:6696\n2276#1:6835\n2276#1:6836,6\n2276#1:6870\n2276#1:6923\n2050#1:3598\n2050#1:3599,6\n2050#1:3633\n2050#1:3745\n2777#1:3860,3\n2777#1:3891\n2951#1:4035\n2951#1:4036,7\n2951#1:4071\n2951#1:4075\n2777#1:4098\n714#1:4502\n714#1:4503,5\n714#1:4536\n714#1:4604\n762#1:4605\n762#1:4606,6\n762#1:4640\n762#1:4656\n564#1:4657\n564#1:4658,6\n564#1:4692\n610#1:4807\n610#1:4808,7\n610#1:4843\n610#1:4849\n564#1:4973\n832#1:5052\n832#1:5053,7\n832#1:5088\n832#1:5105\n894#1:5123\n894#1:5124,6\n894#1:5158\n894#1:5171\n1142#1:5310\n1142#1:5311,6\n1142#1:5345\n1142#1:5556\n1320#1:5568\n1320#1:5569,6\n1320#1:5603\n1320#1:5659\n1371#1:5661\n1371#1:5662,6\n1371#1:5696\n1371#1:5797\n1428#1:5831\n1428#1:5832,6\n1428#1:5866\n1428#1:5870\n1125#1:5876\n1125#1:5877,6\n1125#1:5911\n1125#1:5971\n1042#1:5972\n1042#1:5973,6\n1042#1:6007\n1042#1:6066\n1501#1:6106\n1501#1:6107,6\n1501#1:6141\n1501#1:6145\n1698#1:6229\n1698#1:6230,6\n1698#1:6264\n1698#1:6390\n2087#1:6610\n2087#1:6611,7\n2087#1:6646\n2087#1:6700\n2158#1:6740\n2158#1:6741,7\n2158#1:6776\n2158#1:6782\n2310#1:6791\n2310#1:6792,6\n2310#1:6826\n2310#1:6833\n2293#1:6873\n2293#1:6874,6\n2293#1:6908\n2293#1:6918\n2444#1:6940,3\n2444#1:6971\n2455#1:6973\n2455#1:6974,6\n2455#1:7008\n2455#1:7057\n2509#1:7059,3\n2509#1:7090\n2509#1:7106\n2572#1:7108\n2572#1:7109,6\n2572#1:7143\n2592#1:7148\n2592#1:7149,6\n2592#1:7183\n2592#1:7241\n2665#1:7243\n2665#1:7244,6\n2665#1:7278\n2665#1:7333\n2572#1:7337\n2444#1:7341\n176#1:4103\n178#1:4104\n179#1:4105\n180#1:4106\n181#1:4107\n182#1:4108\n182#1:4109,2\n262#1:4123\n297#1:4124\n332#1:4125\n367#1:4126\n487#1:4470\n488#1:4471\n489#1:4472\n490#1:4473\n491#1:4474\n492#1:4475\n493#1:4476\n494#1:4477\n495#1:4478\n498#1:4479\n498#1:4480,2\n500#1:4482\n501#1:4483\n502#1:4484\n502#1:4485,2\n503#1:4487\n503#1:4488,2\n523#1:4490\n630#1:4497\n631#1:4498\n939#1:5175\n939#1:5176,2\n952#1:5178\n952#1:5179,2\n953#1:5181\n953#1:5182,2\n954#1:5184\n954#1:5185,2\n955#1:5187\n955#1:5188,2\n959#1:5190\n959#1:5191,2\n1056#1:5236\n1060#1:5237\n1064#1:5238\n1133#1:5291\n1133#1:5292,2\n1134#1:5294\n1134#1:5295,2\n1290#1:5557\n1291#1:5558\n1298#1:5559\n1298#1:5560,2\n1457#1:6067\n1461#1:6068\n1640#1:6199\n1640#1:6200,2\n1644#1:6202\n1652#1:6203\n1688#1:6204\n1688#1:6205,2\n1696#1:6207\n1696#1:6208,2\n1811#1:6467\n1812#1:6468\n1817#1:6469\n1927#1:6553\n2032#1:6598\n2032#1:6599,2\n2235#1:6789\n2244#1:6790\n2412#1:6929\n2422#1:6930\n2655#1:6937\n2730#1:7342\n2730#1:7343,2\n2731#1:7345\n2731#1:7346,2\n2732#1:7348\n2732#1:7349,2\n2733#1:7351\n2733#1:7352,2\n2866#1:7359\n2927#1:7360\n2986#1:7361\n284#1:4249\n319#1:4300\n354#1:4351\n389#1:4402\n716#1:4501\n792#1:4652\n545#1:4990\n551#1:4994\n743#1:4580\n743#1:4593\n1239#1:5492,2\n1239#1:5541\n1753#1:6318\n1753#1:6327\n912#1:5110,12\n935#1:5172\n935#1:5173,2\n1307#1:5562,5\n2391#1:6924,5\n2764#1:7354,5\n*E\n"})
/* loaded from: /app/applet/app/build/intermediates/project_dex_archive/debug/dexBuilderDebug/out/com/example/MainActivityKt.dex */
public final class MainActivityKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AddTaskCard$lambda$369(Function3 function3, Function0 function0, int i, Composer composer, int i2) {
        AddTaskCard(function3, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AlarmRingingLockScreen$lambda$519(String str, int i, int i2, Composer composer, int i3) {
        AlarmRingingLockScreen(str, i, composer, RecomposeScopeImplKt.updateChangedFlags(i2 | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AnimatedQuoteCard$lambda$448(String str, boolean z, int i, Composer composer, int i2) {
        AnimatedQuoteCard(str, z, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AnimatedScaleBox_gXMAzVA$lambda$379(Function0 function0, Modifier modifier, boolean z, Shape shape, float f, long j, Function2 function2, int i, int i2, Composer composer, int i3) {
        m0AnimatedScaleBoxgXMAzVA(function0, modifier, z, shape, f, j, function2, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AppControls$lambda$399(TimerManager.TimerState timerState, Context context, Function0 function0, int i, int i2, Composer composer, int i3) {
        AppControls(timerState, context, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$188(TimerViewModel timerViewModel, NavController navController, float f, int i, int i2, Composer composer, int i3) {
        m1HomeScreen6a0pyJM(timerViewModel, navController, f, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$80(TimerViewModel timerViewModel, int i, Composer composer, int i2) {
        PomoPalApp(timerViewModel, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PremiumJumpingTextPreview$lambda$331(String str, int i, Composer composer, int i2) {
        PremiumJumpingTextPreview(str, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SeriousFullscreenOverlay$lambda$480(String str, String str2, int i, Function0 function0, int i2, Composer composer, int i3) {
        SeriousFullscreenOverlay(str, str2, i, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i2 | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$202(Function0 function0, int i, int i2, int i3, Composer composer, int i4) {
        SettingsOverlay(function0, i, composer, RecomposeScopeImplKt.updateChangedFlags(i2 | 1), i3);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$307(Function0 function0, int i, int i2, int i3, Composer composer, int i4) {
        SettingsOverlay(function0, i, composer, RecomposeScopeImplKt.updateChangedFlags(i2 | 1), i3);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SlidingTimer_KmRG4DE$lambda$409(int i, long j, int i2, int i3, Composer composer, int i4) {
        m2SlidingTimerKmRG4DE(i, j, composer, RecomposeScopeImplKt.updateChangedFlags(i2 | 1), i3);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TaskItemRow$lambda$320(TaskItem taskItem, boolean z, Function1 function1, Function0 function0, Modifier modifier, int i, int i2, Composer composer, int i3) {
        TaskItemRow(taskItem, z, function1, function0, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TimerDisplay_egy_3UM$lambda$433(int i, int i2, TimerManager.TimerState timerState, boolean z, String str, TaskItem taskItem, Context context, float f, int i3, int i4, Composer composer, int i5) {
        m3TimerDisplayegy_3UM(i, i2, timerState, z, str, taskItem, context, f, composer, RecomposeScopeImplKt.updateChangedFlags(i3 | 1), i4);
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final void PomoPalApp(@NotNull final TimerViewModel viewModel, @Nullable Composer $composer, final int $changed) {
        Object obj;
        int $dirty;
        Object obj2;
        View view;
        int i;
        Object obj3;
        NavDestination destination;
        String route;
        Intrinsics.checkNotNullParameter(viewModel, "viewModel");
        Composer $composer2 = $composer.startRestartGroup(-1210837019);
        ComposerKt.sourceInformation($composer2, "C(PomoPalApp)172@7182L7,174@7318L23,175@7385L30,177@7537L16,178@7597L16,179@7665L16,180@7734L16,181@7772L42,182@7869L7,184@7936L7,185@7986L32,186@8044L122,186@8023L143,228@9793L10745,403@20545L2972,225@9676L13841:MainActivity.kt#to5c3");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= $composer2.changedInstance(viewModel) ? 4 : 2;
        }
        if (($dirty2 & 3) == 2 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1210837019, $dirty2, -1, "com.example.PomoPalApp (MainActivity.kt:171)");
            }
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume = $composer2.consume(AndroidCompositionLocals_androidKt.getLocalContext());
            ComposerKt.sourceInformationMarkerEnd($composer2);
            final Context context = (Context) consume;
            final String startDest = SettingsManager.INSTANCE.getUserName() == null ? "signin" : "home";
            final NavController rememberNavController = NavHostControllerKt.rememberNavController(new Navigator[0], $composer2, 0);
            final State navBackStackEntry$delegate = NavHostControllerKt.currentBackStackEntryAsState(rememberNavController, $composer2, 0);
            NavBackStackEntry PomoPalApp$lambda$0 = PomoPalApp$lambda$0(navBackStackEntry$delegate);
            final String currentRoute = (PomoPalApp$lambda$0 == null || (destination = PomoPalApp$lambda$0.getDestination()) == null || (route = destination.getRoute()) == null) ? startDest : route;
            final State isAddingTask$delegate = SnapshotStateKt.collectAsState(viewModel.isAddingTask(), (CoroutineContext) null, $composer2, 0, 1);
            final State timerState$delegate = SnapshotStateKt.collectAsState(viewModel.getTimerState(), (CoroutineContext) null, $composer2, 0, 1);
            final State isSettingsOpen$delegate = SnapshotStateKt.collectAsState(viewModel.isSettingsOpen(), (CoroutineContext) null, $composer2, 0, 1);
            SnapshotStateKt.collectAsState(viewModel.getSettingsTab(), (CoroutineContext) null, $composer2, 0, 1);
            ComposerKt.sourceInformationMarkerStart($composer2, 321784591, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue = $composer2.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = SnapshotStateKt.mutableStateOf$default((Object) null, (SnapshotMutationPolicy) null, 2, (Object) null);
                $composer2.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            final MutableState updateUrl$delegate = (MutableState) obj;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume2 = $composer2.consume(AndroidCompositionLocals_androidKt.getLocalView());
            ComposerKt.sourceInformationMarkerEnd($composer2);
            final View view2 = (View) consume2;
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume3 = $composer2.consume(ThemeKt.getLocalAppTheme());
            ComposerKt.sourceInformationMarkerEnd($composer2);
            final ThemeOption currentTheme = (ThemeOption) consume3;
            SystemProtectionStateKt.rememberSystemProtectionStates($composer2, 0);
            Unit unit = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart($composer2, 321793375, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue2 = $composer2.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                $dirty = $dirty2;
                obj2 = (Function2) new PomoPalApp.1.1(updateUrl$delegate, (Continuation) null);
                $composer2.updateRememberedValue(obj2);
            } else {
                $dirty = $dirty2;
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer2);
            EffectsKt.LaunchedEffect(unit, (Function2) obj2, $composer2, 6);
            if (PomoPalApp$lambda$6(updateUrl$delegate) != null) {
                $composer2.startReplaceGroup(1385851770);
                ComposerKt.sourceInformation($composer2, "195@8249L20,198@8581L574,210@9185L374,196@8291L92,197@8404L147,194@8205L1459");
                long j = Color.Companion.getTransparent-0d7_KjU();
                Shape shape = RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24));
                ComposerKt.sourceInformationMarkerStart($composer2, 321799833, "CC(remember):MainActivity.kt#9igjgp");
                Object rememberedValue3 = $composer2.rememberedValue();
                if (rememberedValue3 == Composer.Companion.getEmpty()) {
                    obj3 = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda172
                        public final Object invoke() {
                            return MainActivityKt.PomoPalApp$lambda$10$lambda$9(updateUrl$delegate);
                        }
                    };
                    $composer2.updateRememberedValue(obj3);
                } else {
                    obj3 = rememberedValue3;
                }
                ComposerKt.sourceInformationMarkerEnd($composer2);
                view = view2;
                i = 1377675613;
                AndroidAlertDialog_androidKt.AlertDialog-Oix01E0((Function0) obj3, ComposableLambdaKt.rememberComposableLambda(-675414984, true, new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda173
                    public final Object invoke(Object obj4, Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$13(view2, context, updateUrl$delegate, (Composer) obj4, ((Integer) obj5).intValue());
                    }
                }, $composer2, 54), (Modifier) null, ComposableLambdaKt.rememberComposableLambda(1222081782, true, new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda174
                    public final Object invoke(Object obj4, Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$17(view2, updateUrl$delegate, currentTheme, (Composer) obj4, ((Integer) obj5).intValue());
                    }
                }, $composer2, 54), (Function2) null, ComposableLambdaKt.rememberComposableLambda(-1175388748, true, new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda175
                    public final Object invoke(Object obj4, Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$18(currentTheme, (Composer) obj4, ((Integer) obj5).intValue());
                    }
                }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(1920843283, true, new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda176
                    public final Object invoke(Object obj4, Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$19(currentTheme, (Composer) obj4, ((Integer) obj5).intValue());
                    }
                }, $composer2, 54), shape, j, 0L, 0L, 0L, 0.0f, (DialogProperties) null, $composer2, 102435894, 0, 15892);
                $composer2 = $composer2;
            } else {
                view = view2;
                i = 1377675613;
                $composer2.startReplaceGroup(1377675613);
            }
            $composer2.endReplaceGroup();
            final View view3 = view;
            ScaffoldKt.Scaffold-TvnljyQ(SizeKt.fillMaxSize$default(Modifier.Companion, 0.0f, 1, (Object) null), (Function2) null, ComposableLambdaKt.rememberComposableLambda(1378613994, true, new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda177
                public final Object invoke(Object obj4, Object obj5) {
                    return MainActivityKt.PomoPalApp$lambda$51(currentRoute, startDest, navBackStackEntry$delegate, isAddingTask$delegate, timerState$delegate, isSettingsOpen$delegate, view3, rememberNavController, (Composer) obj4, ((Integer) obj5).intValue());
                }
            }, $composer2, 54), (Function2) null, (Function2) null, 0, Color.Companion.getTransparent-0d7_KjU(), 0L, (WindowInsets) null, ComposableLambdaKt.rememberComposableLambda(238820404, true, new Function3() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda178
                public final Object invoke(Object obj4, Object obj5, Object obj6) {
                    return MainActivityKt.PomoPalApp$lambda$79(rememberNavController, startDest, viewModel, (PaddingValues) obj4, (Composer) obj5, ((Integer) obj6).intValue());
                }
            }, $composer2, 54), $composer2, 806879622, 442);
            if (AlarmState.INSTANCE.isAlarmRinging()) {
                $composer2.startReplaceGroup(1401068151);
                ComposerKt.sourceInformation($composer2, "472@23584L182");
                AlarmRingingLockScreen(AlarmState.INSTANCE.getActiveAlarmLabel(), AlarmState.INSTANCE.getActiveAlarmSquatTarget(), $composer2, 0);
            } else {
                $composer2.startReplaceGroup(i);
            }
            $composer2.endReplaceGroup();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda179
                public final Object invoke(Object obj4, Object obj5) {
                    return MainActivityKt.PomoPalApp$lambda$80(viewModel, $changed, (Composer) obj4, ((Integer) obj5).intValue());
                }
            });
        }
    }

    private static final NavBackStackEntry PomoPalApp$lambda$0(State<NavBackStackEntry> state) {
        return (NavBackStackEntry) state.getValue();
    }

    private static final boolean PomoPalApp$lambda$1(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final TimerManager.TimerState PomoPalApp$lambda$2(State<? extends TimerManager.TimerState> state) {
        return (TimerManager.TimerState) state.getValue();
    }

    private static final boolean PomoPalApp$lambda$3(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final String PomoPalApp$lambda$6(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$10$lambda$9(MutableState $updateUrl$delegate) {
        $updateUrl$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$18(ThemeOption $currentTheme, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C196@8293L88:MainActivity.kt#to5c3");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1175388748, $changed, -1, "com.example.PomoPalApp.<anonymous> (MainActivity.kt:196)");
            }
            TextKt.Text--4IGK_g("Update Available", (Modifier) null, $currentTheme.getTextPrimary-0d7_KjU(), 0L, (FontStyle) null, FontWeight.Companion.getBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 196614, 0, 131034);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$19(ThemeOption $currentTheme, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C197@8406L143:MainActivity.kt#to5c3");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1920843283, $changed, -1, "com.example.PomoPalApp.<anonymous> (MainActivity.kt:197)");
            }
            TextKt.Text--4IGK_g("A new version of PomoPal is available! Please download it to enjoy the latest features and bug fixes.", (Modifier) null, $currentTheme.getTextPrimary-0d7_KjU(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 6, 0, 131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$13(final View $view, final Context $context, final MutableState $updateUrl$delegate, Composer $composer, int $changed) {
        Function0 function0;
        ComposerKt.sourceInformation($composer, "C199@8620L406,199@8599L542:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-675414984, $changed, -1, "com.example.PomoPalApp.<anonymous> (MainActivity.kt:199)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, -533736274, "CC(remember):MainActivity.kt#9igjgp");
            boolean changedInstance = $composer.changedInstance($view) | $composer.changedInstance($context);
            Object rememberedValue = $composer.rememberedValue();
            if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
                function0 = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda150
                    public final Object invoke() {
                        return MainActivityKt.PomoPalApp$lambda$13$lambda$12$lambda$11($view, $context, $updateUrl$delegate);
                    }
                };
                $composer.updateRememberedValue(function0);
            } else {
                function0 = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.TextButton(function0, (Modifier) null, false, (Shape) null, (ButtonColors) null, (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableSingletons.MainActivityKt.INSTANCE.getLambda$1933997979$app(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$13$lambda$12$lambda$11(View $view, Context $context, MutableState $updateUrl$delegate) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(PomoPalApp$lambda$6($updateUrl$delegate)));
        $context.startActivity(intent);
        $updateUrl$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$17(final View $view, final MutableState $updateUrl$delegate, final ThemeOption $currentTheme, Composer $composer, int $changed) {
        Function0 function0;
        ComposerKt.sourceInformation($composer, "C211@9224L230,216@9456L89,211@9203L342:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1222081782, $changed, -1, "com.example.PomoPalApp.<anonymous> (MainActivity.kt:211)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, 1894522172, "CC(remember):MainActivity.kt#9igjgp");
            boolean changedInstance = $composer.changedInstance($view);
            Object rememberedValue = $composer.rememberedValue();
            if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
                function0 = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda112
                    public final Object invoke() {
                        return MainActivityKt.PomoPalApp$lambda$17$lambda$15$lambda$14($view, $updateUrl$delegate);
                    }
                };
                $composer.updateRememberedValue(function0);
            } else {
                function0 = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.TextButton(function0, (Modifier) null, false, (Shape) null, (ButtonColors) null, (ButtonElevation) null, (BorderStroke) null, (PaddingValues) null, (MutableInteractionSource) null, ComposableLambdaKt.rememberComposableLambda(-463472551, true, new Function3() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda113
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return MainActivityKt.PomoPalApp$lambda$17$lambda$16($currentTheme, (RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$17$lambda$15$lambda$14(View $view, MutableState $updateUrl$delegate) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        $updateUrl$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$17$lambda$16(ThemeOption $currentTheme, RowScope $this$TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C217@9478L49:MainActivity.kt#to5c3");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-463472551, $changed, -1, "com.example.PomoPalApp.<anonymous>.<anonymous> (MainActivity.kt:217)");
            }
            TextKt.Text--4IGK_g("Later", (Modifier) null, $currentTheme.getTextSecondary-0d7_KjU(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 6, 0, 131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x005f, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual(r24, "signin") != false) goto L47;
     */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0170  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit PomoPalApp$lambda$51(final java.lang.String r23, java.lang.String r24, androidx.compose.runtime.State r25, androidx.compose.runtime.State r26, androidx.compose.runtime.State r27, androidx.compose.runtime.State r28, final android.view.View r29, final androidx.navigation.NavHostController r30, androidx.compose.runtime.Composer r31, int r32) {
        /*
            Method dump skipped, instructions count: 374
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.PomoPalApp$lambda$51(java.lang.String, java.lang.String, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, android.view.View, androidx.navigation.NavHostController, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int PomoPalApp$lambda$51$lambda$21$lambda$20(int it) {
        return it * 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int PomoPalApp$lambda$51$lambda$23$lambda$22(int it) {
        return it * 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x07af  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x07bb  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x07c1  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x07f4  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x080a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0863  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0868  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x08a8  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x091b  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0949  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x094c  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x097c  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0981  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x09aa  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x09b5  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x09dd  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x09ea A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0a91  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0a9d  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0aa3  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0ad6  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0aec A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0b40  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0b45  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0b85  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0bf5  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0c23  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x0c26  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0c56  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0c5b  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0c84  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0c8f  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x0cb6  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0cc3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0d60  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0d6c  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0d72  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0da3  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0e05  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0e0a  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0e4a  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0eb8  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0f10  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0275  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0327  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0361  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0366  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0396  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03a1  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x03cb  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x03d9  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0485  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0491  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0497  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x04e0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0535  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x053a  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x057f  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0627  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0661  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0664  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0697  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x069c  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x06c9  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x06d4  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x06fc  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0709 A[ADDED_TO_REGION] */
    /* JADX WARN: Type inference failed for: r3v53 */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit PomoPalApp$lambda$51$lambda$50(java.lang.String r118, final android.view.View r119, final androidx.navigation.NavHostController r120, androidx.compose.animation.AnimatedVisibilityScope r121, androidx.compose.runtime.Composer r122, int r123) {
        /*
            Method dump skipped, instructions count: 3862
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.PomoPalApp$lambda$51$lambda$50(java.lang.String, android.view.View, androidx.navigation.NavHostController, androidx.compose.animation.AnimatedVisibilityScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    private static final float PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$24(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$28$lambda$27(View $view, boolean $isHome, NavHostController $navController) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        if (!$isHome) {
            $navController.navigate("home", new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda47
                public final Object invoke(Object obj) {
                    return MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$28$lambda$27$lambda$26((NavOptionsBuilder) obj);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$28$lambda$27$lambda$26(NavOptionsBuilder $this$navigate) {
        Intrinsics.checkNotNullParameter($this$navigate, "$this$navigate");
        NavOptionsBuilder.popUpTo$default($this$navigate, 0, (Function1) null, 2, (Object) null);
        return Unit.INSTANCE;
    }

    private static final float PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$30(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$34$lambda$33(View $view, boolean $isHistory, NavHostController $navController) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        if (!$isHistory) {
            $navController.navigate("history", new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda48
                public final Object invoke(Object obj) {
                    return MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$34$lambda$33$lambda$32((NavOptionsBuilder) obj);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$34$lambda$33$lambda$32(NavOptionsBuilder $this$navigate) {
        Intrinsics.checkNotNullParameter($this$navigate, "$this$navigate");
        NavOptionsBuilder.popUpTo$default($this$navigate, 0, (Function1) null, 2, (Object) null);
        return Unit.INSTANCE;
    }

    private static final float PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$36(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$40$lambda$39(View $view, boolean $isAlarm, NavHostController $navController) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        if (!$isAlarm) {
            $navController.navigate("alarm", new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda171
                public final Object invoke(Object obj) {
                    return MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$40$lambda$39$lambda$38((NavOptionsBuilder) obj);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$40$lambda$39$lambda$38(NavOptionsBuilder $this$navigate) {
        Intrinsics.checkNotNullParameter($this$navigate, "$this$navigate");
        NavOptionsBuilder.popUpTo$default($this$navigate, 0, (Function1) null, 2, (Object) null);
        return Unit.INSTANCE;
    }

    private static final float PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$42(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$46$lambda$45(View $view, boolean $isWorkout, NavHostController $navController) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        if (!$isWorkout) {
            $navController.navigate("workout", new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda79
                public final Object invoke(Object obj) {
                    return MainActivityKt.PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$46$lambda$45$lambda$44((NavOptionsBuilder) obj);
                }
            });
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$51$lambda$50$lambda$49$lambda$48$lambda$46$lambda$45$lambda$44(NavOptionsBuilder $this$navigate) {
        Intrinsics.checkNotNullParameter($this$navigate, "$this$navigate");
        NavOptionsBuilder.popUpTo$default($this$navigate, 0, (Function1) null, 2, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$79(final NavHostController $navController, String $startDest, final TimerViewModel $viewModel, final PaddingValues paddingValues, Composer $composer, int $changed) {
        Object obj;
        Object obj2;
        Object obj3;
        Object obj4;
        Function1 function1;
        Intrinsics.checkNotNullParameter(paddingValues, "paddingValues");
        ComposerKt.sourceInformation($composer, "C408@20743L283,415@21057L97,418@21189L283,425@21506L97,428@21614L1897,404@20572L2939:MainActivity.kt#to5c3");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(paddingValues) ? 4 : 2;
        }
        if (($dirty & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(238820404, $dirty, -1, "com.example.PomoPalApp.<anonymous> (MainActivity.kt:404)");
            }
            Modifier fillMaxSize$default = SizeKt.fillMaxSize$default(Modifier.Companion, 0.0f, 1, (Object) null);
            ComposerKt.sourceInformationMarkerStart($composer, 1873148175, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda159
                    public final Object invoke(Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$79$lambda$54$lambda$53((AnimatedContentTransitionScope) obj5);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            Function1 function12 = (Function1) obj;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, 1873158037, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue2 = $composer.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                obj2 = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda160
                    public final Object invoke(Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$79$lambda$56$lambda$55((AnimatedContentTransitionScope) obj5);
                    }
                };
                $composer.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            Function1 function13 = (Function1) obj2;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, 1873162447, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue3 = $composer.rememberedValue();
            if (rememberedValue3 == Composer.Companion.getEmpty()) {
                obj3 = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda161
                    public final Object invoke(Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$79$lambda$59$lambda$58((AnimatedContentTransitionScope) obj5);
                    }
                };
                $composer.updateRememberedValue(obj3);
            } else {
                obj3 = rememberedValue3;
            }
            Function1 function14 = (Function1) obj3;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, 1873172405, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue4 = $composer.rememberedValue();
            if (rememberedValue4 == Composer.Companion.getEmpty()) {
                obj4 = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda162
                    public final Object invoke(Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$79$lambda$61$lambda$60((AnimatedContentTransitionScope) obj5);
                    }
                };
                $composer.updateRememberedValue(obj4);
            } else {
                obj4 = rememberedValue4;
            }
            Function1 function15 = (Function1) obj4;
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerStart($composer, 1873177661, "CC(remember):MainActivity.kt#9igjgp");
            boolean changedInstance = (($dirty & 14) == 4) | $composer.changedInstance($navController) | $composer.changedInstance($viewModel);
            Object rememberedValue5 = $composer.rememberedValue();
            if (changedInstance || rememberedValue5 == Composer.Companion.getEmpty()) {
                function1 = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda163
                    public final Object invoke(Object obj5) {
                        return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77(paddingValues, $navController, $viewModel, (NavGraphBuilder) obj5);
                    }
                };
                $composer.updateRememberedValue(function1);
            } else {
                function1 = rememberedValue5;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            NavHostKt.NavHost($navController, $startDest, fillMaxSize$default, (Alignment) null, (String) null, function12, function13, function14, function15, (Function1) null, function1, $composer, 115016064, 0, 536);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final EnterTransition PomoPalApp$lambda$79$lambda$54$lambda$53(AnimatedContentTransitionScope $this$NavHost) {
        Intrinsics.checkNotNullParameter($this$NavHost, "$this$NavHost");
        return EnterExitTransitionKt.fadeIn$default(AnimationSpecKt.tween$default(400, 0, EasingKt.getLinearOutSlowInEasing(), 2, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.slideInVertically(AnimationSpecKt.tween$default(400, 0, EasingKt.getLinearOutSlowInEasing(), 2, (Object) null), new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda92
            public final Object invoke(Object obj) {
                return Integer.valueOf(MainActivityKt.PomoPalApp$lambda$79$lambda$54$lambda$53$lambda$52(((Integer) obj).intValue()));
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int PomoPalApp$lambda$79$lambda$54$lambda$53$lambda$52(int it) {
        return it / 12;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ExitTransition PomoPalApp$lambda$79$lambda$56$lambda$55(AnimatedContentTransitionScope $this$NavHost) {
        Intrinsics.checkNotNullParameter($this$NavHost, "$this$NavHost");
        return EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.tween$default(350, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), 0.0f, 2, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final EnterTransition PomoPalApp$lambda$79$lambda$59$lambda$58(AnimatedContentTransitionScope $this$NavHost) {
        Intrinsics.checkNotNullParameter($this$NavHost, "$this$NavHost");
        return EnterExitTransitionKt.fadeIn$default(AnimationSpecKt.tween$default(400, 0, EasingKt.getLinearOutSlowInEasing(), 2, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.slideInVertically(AnimationSpecKt.tween$default(400, 0, EasingKt.getLinearOutSlowInEasing(), 2, (Object) null), new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda166
            public final Object invoke(Object obj) {
                return Integer.valueOf(MainActivityKt.PomoPalApp$lambda$79$lambda$59$lambda$58$lambda$57(((Integer) obj).intValue()));
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int PomoPalApp$lambda$79$lambda$59$lambda$58$lambda$57(int it) {
        return (-it) / 12;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ExitTransition PomoPalApp$lambda$79$lambda$61$lambda$60(AnimatedContentTransitionScope $this$NavHost) {
        Intrinsics.checkNotNullParameter($this$NavHost, "$this$NavHost");
        return EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.tween$default(350, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), 0.0f, 2, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77(PaddingValues $paddingValues, final NavHostController $navController, final TimerViewModel $viewModel, NavGraphBuilder $this$NavHost) {
        Intrinsics.checkNotNullParameter($this$NavHost, "$this$NavHost");
        final float bottomPadding = $paddingValues.calculateBottomPadding-D9Ej5fM();
        NavGraphBuilderKt.composable$default($this$NavHost, "signin", (List) null, (List) null, (Function1) null, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda83
            public final Object invoke(Object obj) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$62((AnimatedContentTransitionScope) obj);
            }
        }, (Function1) null, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda84
            public final Object invoke(Object obj) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$63((AnimatedContentTransitionScope) obj);
            }
        }, (Function1) null, ComposableLambdaKt.composableLambdaInstance(-2027003279, true, new Function4() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda85
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68($navController, (AnimatedContentScope) obj, (NavBackStackEntry) obj2, (Composer) obj3, ((Integer) obj4).intValue());
            }
        }), 174, (Object) null);
        NavGraphBuilderKt.composable$default($this$NavHost, "home", (List) null, (List) null, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda86
            public final Object invoke(Object obj) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$69((AnimatedContentTransitionScope) obj);
            }
        }, (Function1) null, (Function1) null, (Function1) null, (Function1) null, ComposableLambdaKt.composableLambdaInstance(852308186, true, new Function4() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda87
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$70($viewModel, $navController, bottomPadding, (AnimatedContentScope) obj, (NavBackStackEntry) obj2, (Composer) obj3, ((Integer) obj4).intValue());
            }
        }), 246, (Object) null);
        NavGraphBuilderKt.composable$default($this$NavHost, "history", (List) null, (List) null, (Function1) null, (Function1) null, (Function1) null, (Function1) null, (Function1) null, ComposableLambdaKt.composableLambdaInstance(-1419159559, true, new Function4() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda88
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$71($viewModel, $navController, bottomPadding, (AnimatedContentScope) obj, (NavBackStackEntry) obj2, (Composer) obj3, ((Integer) obj4).intValue());
            }
        }), 254, (Object) null);
        NavGraphBuilderKt.composable$default($this$NavHost, "alarm", (List) null, (List) null, (Function1) null, (Function1) null, (Function1) null, (Function1) null, (Function1) null, ComposableLambdaKt.composableLambdaInstance(604339992, true, new Function4() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda89
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$72($navController, bottomPadding, (AnimatedContentScope) obj, (NavBackStackEntry) obj2, (Composer) obj3, ((Integer) obj4).intValue());
            }
        }), 254, (Object) null);
        NavGraphBuilderKt.composable$default($this$NavHost, "workout", (List) null, (List) null, (Function1) null, (Function1) null, (Function1) null, (Function1) null, (Function1) null, ComposableLambdaKt.composableLambdaInstance(-1667127753, true, new Function4() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda90
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$73(bottomPadding, (AnimatedContentScope) obj, (NavBackStackEntry) obj2, (Composer) obj3, ((Integer) obj4).intValue());
            }
        }), 254, (Object) null);
        NavGraphBuilderKt.composable$default($this$NavHost, "calibration", (List) null, (List) null, (Function1) null, (Function1) null, (Function1) null, (Function1) null, (Function1) null, ComposableLambdaKt.composableLambdaInstance(356371798, true, new Function4() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda91
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$76($navController, (AnimatedContentScope) obj, (NavBackStackEntry) obj2, (Composer) obj3, ((Integer) obj4).intValue());
            }
        }), 254, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ExitTransition PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$62(AnimatedContentTransitionScope $this$composable) {
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        return EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.tween$default(400, 0, EasingKt.getLinearOutSlowInEasing(), 2, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.scaleOut-L8ZKh-E$default(AnimationSpecKt.tween$default(400, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), 0.94f, 0L, 4, (Object) null));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ExitTransition PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$63(AnimatedContentTransitionScope $this$composable) {
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        return EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.tween$default(400, 0, EasingKt.getLinearOutSlowInEasing(), 2, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.scaleOut-L8ZKh-E$default(AnimationSpecKt.tween$default(400, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), 0.94f, 0L, 4, (Object) null));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68(final NavHostController $navController, AnimatedContentScope $this$composable, NavBackStackEntry it, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        Intrinsics.checkNotNullParameter(it, "it");
        ComposerKt.sourceInformation($composer, "C441@22360L157,441@22298L220:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(-2027003279, $changed, -1, "com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:441)");
        }
        NavController navController = (NavController) $navController;
        ComposerKt.sourceInformationMarkerStart($composer, 1965004462, "CC(remember):MainActivity.kt#9igjgp");
        boolean changedInstance = $composer.changedInstance($navController);
        Object rememberedValue = $composer.rememberedValue();
        if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
            obj = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda182
                public final Object invoke() {
                    return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66($navController);
                }
            };
            $composer.updateRememberedValue(obj);
        } else {
            obj = rememberedValue;
        }
        ComposerKt.sourceInformationMarkerEnd($composer);
        SignInScreenKt.SignInScreen(navController, (Function0) obj, $composer, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66(NavHostController $navController) {
        $navController.navigate("home", new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda181
            public final Object invoke(Object obj) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66$lambda$65((NavOptionsBuilder) obj);
            }
        });
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66$lambda$65(NavOptionsBuilder $this$navigate) {
        Intrinsics.checkNotNullParameter($this$navigate, "$this$navigate");
        $this$navigate.popUpTo("signin", new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda106
            public final Object invoke(Object obj) {
                return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66$lambda$65$lambda$64((PopUpToBuilder) obj);
            }
        });
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$68$lambda$67$lambda$66$lambda$65$lambda$64(PopUpToBuilder $this$popUpTo) {
        Intrinsics.checkNotNullParameter($this$popUpTo, "$this$popUpTo");
        $this$popUpTo.setInclusive(true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final EnterTransition PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$69(AnimatedContentTransitionScope $this$composable) {
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        return EnterExitTransitionKt.fadeIn$default(AnimationSpecKt.tween(550, 80, EasingKt.getLinearOutSlowInEasing()), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.scaleIn-L8ZKh-E$default(AnimationSpecKt.tween(550, 80, EasingKt.getLinearOutSlowInEasing()), 1.04f, 0L, 4, (Object) null));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$70(TimerViewModel $viewModel, NavHostController $navController, float $bottomPadding, AnimatedContentScope $this$composable, NavBackStackEntry it, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        Intrinsics.checkNotNullParameter(it, "it");
        ComposerKt.sourceInformation($composer, "C454@22910L51:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(852308186, $changed, -1, "com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:454)");
        }
        m1HomeScreen6a0pyJM($viewModel, (NavController) $navController, $bottomPadding, $composer, 0, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$71(TimerViewModel $viewModel, NavHostController $navController, float $bottomPadding, AnimatedContentScope $this$composable, NavBackStackEntry it, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        Intrinsics.checkNotNullParameter(it, "it");
        ComposerKt.sourceInformation($composer, "C457@23043L54:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(-1419159559, $changed, -1, "com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:457)");
        }
        HistoryScreenKt.HistoryScreen-6a0pyJM($viewModel, (NavController) $navController, $bottomPadding, $composer, 0, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$72(NavHostController $navController, float $bottomPadding, AnimatedContentScope $this$composable, NavBackStackEntry it, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        Intrinsics.checkNotNullParameter(it, "it");
        ComposerKt.sourceInformation($composer, "C460@23177L41:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(604339992, $changed, -1, "com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:460)");
        }
        AlarmScreenKt.AlarmScreen-rAjV9yQ((NavController) $navController, $bottomPadding, $composer, 0, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$73(float $bottomPadding, AnimatedContentScope $this$composable, NavBackStackEntry it, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        Intrinsics.checkNotNullParameter(it, "it");
        ComposerKt.sourceInformation($composer, "C463@23300L28:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(-1667127753, $changed, -1, "com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:463)");
        }
        WorkoutScreenKt.WorkoutScreen-8Feqmps($bottomPadding, $composer, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$76(final NavHostController $navController, AnimatedContentScope $this$composable, NavBackStackEntry it, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$composable, "$this$composable");
        Intrinsics.checkNotNullParameter(it, "it");
        ComposerKt.sourceInformation($composer, "C466@23454L32,466@23414L73:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(356371798, $changed, -1, "com.example.PomoPalApp.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:466)");
        }
        ComposerKt.sourceInformationMarkerStart($composer, -1124083434, "CC(remember):MainActivity.kt#9igjgp");
        boolean changedInstance = $composer.changedInstance($navController);
        Object rememberedValue = $composer.rememberedValue();
        if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
            obj = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda170
                public final Object invoke() {
                    return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$76$lambda$75$lambda$74($navController);
                }
            };
            $composer.updateRememberedValue(obj);
        } else {
            obj = rememberedValue;
        }
        ComposerKt.sourceInformationMarkerEnd($composer);
        SquatCalibrationScreenKt.SquatCalibrationScreen((Function0) obj, (SquatCalibrationViewModel) null, $composer, 0, 2);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$76$lambda$75$lambda$74(NavHostController $navController) {
        $navController.popBackStack();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x04b0  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x04bf  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0555  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x056a  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x026f  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0296  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x02a5  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0302  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x03c9  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0429  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0440  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /* renamed from: HomeScreen-6a0pyJM  reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void m1HomeScreen6a0pyJM(@org.jetbrains.annotations.NotNull final com.example.ui.TimerViewModel r50, @org.jetbrains.annotations.NotNull final androidx.navigation.NavController r51, float r52, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r53, final int r54, final int r55) {
        /*
            Method dump skipped, instructions count: 1414
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.m1HomeScreen6a0pyJM(com.example.ui.TimerViewModel, androidx.navigation.NavController, float, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final TimerManager.TimerState HomeScreen_6a0pyJM$lambda$82(State<? extends TimerManager.TimerState> state) {
        return (TimerManager.TimerState) state.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean HomeScreen_6a0pyJM$lambda$83(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final String HomeScreen_6a0pyJM$lambda$84(State<String> state) {
        return (String) state.getValue();
    }

    private static final int HomeScreen_6a0pyJM$lambda$85(State<Integer> state) {
        return ((Number) state.getValue()).intValue();
    }

    private static final String HomeScreen_6a0pyJM$lambda$86(State<String> state) {
        return (String) state.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int HomeScreen_6a0pyJM$lambda$87(State<Integer> state) {
        return ((Number) state.getValue()).intValue();
    }

    private static final Long HomeScreen_6a0pyJM$lambda$88(State<Long> state) {
        return (Long) state.getValue();
    }

    private static final List<TaskItem> HomeScreen_6a0pyJM$lambda$89(State<? extends List<TaskItem>> state) {
        return (List) state.getValue();
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$90(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$92(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void HomeScreen_6a0pyJM$lambda$93(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean HomeScreen_6a0pyJM$lambda$94(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final int HomeScreen_6a0pyJM$lambda$95(State<Integer> state) {
        return ((Number) state.getValue()).intValue();
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$97(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void HomeScreen_6a0pyJM$lambda$98(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$100(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void HomeScreen_6a0pyJM$lambda$101(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final long HomeScreen_6a0pyJM$lambda$104(State<Color> state) {
        return ((Color) state.getValue()).unbox-impl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit HomeScreen_6a0pyJM$lambda$160(final ThemeOption $currentTheme, final SystemProtectionStates $protectionStates, final State $isBreakMode$delegate, final State $timerState$delegate, final FocusManager $focusManager, final Context $context, final View $view, final MutableState $showJustInTimeSetup$delegate, final TimerViewModel $viewModel, final FontFamily $currentFont, final String $userName, final State $timeRemaining$delegate, final State $currentTaskName$delegate, final State $allTasks$delegate, final State $currentTaskId$delegate, final float $bottomPadding, final MutableState $showAllTasksSheet$delegate, final State $currentQuote$delegate, final MutableState $showFullScreenSeriousness$delegate, final State $showSettings$delegate, final State $settingsInitialTab$delegate, BoxWithConstraintsScope $this$BoxWithConstraints, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$BoxWithConstraints, "$this$BoxWithConstraints");
        ComposerKt.sourceInformation($composer, "C541@26643L14699,537@26492L14850:MainActivity.kt#to5c3");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed($this$BoxWithConstraints) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (($dirty2 & 19) == 18 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-583363852, $dirty2, -1, "com.example.HomeScreen.<anonymous> (MainActivity.kt:533)");
            }
            boolean z = false;
            if (!Intrinsics.areEqual($currentTheme.getId(), "premium") || HomeScreen_6a0pyJM$lambda$83($isBreakMode$delegate)) {
                $composer.startReplaceGroup(-1580841586);
            } else {
                $composer.startReplaceGroup(-1554773407);
                ComposerKt.sourceInformation($composer, "534@26315L27");
                PomoUIKt.PremiumBackgroundOverlays($composer, 0);
            }
            $composer.endReplaceGroup();
            if ($protectionStates.getNeedsAttention() && HomeScreen_6a0pyJM$lambda$82($timerState$delegate) == TimerManager.TimerState.STOPPED) {
                z = true;
            }
            final boolean hasBanner = z;
            AdaptiveSystemKt.ProvideAdaptiveDimensions-if577FI($this$BoxWithConstraints.getMaxWidth-D9Ej5fM(), $this$BoxWithConstraints.getMaxHeight-D9Ej5fM(), hasBanner, ComposableLambdaKt.rememberComposableLambda(-1821730338, true, new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda53
                public final Object invoke(Object obj, Object obj2) {
                    return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159($focusManager, $currentTheme, $protectionStates, $context, $view, $timerState$delegate, $showJustInTimeSetup$delegate, $viewModel, $currentFont, $userName, $timeRemaining$delegate, $isBreakMode$delegate, $currentTaskName$delegate, $allTasks$delegate, $currentTaskId$delegate, $bottomPadding, hasBanner, $showAllTasksSheet$delegate, $currentQuote$delegate, $showFullScreenSeriousness$delegate, $showSettings$delegate, $settingsInitialTab$delegate, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), $composer, 3072);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159(FocusManager $focusManager, final ThemeOption $currentTheme, final SystemProtectionStates $protectionStates, final Context $context, final View $view, final State $timerState$delegate, final MutableState $showJustInTimeSetup$delegate, final TimerViewModel $viewModel, final FontFamily $currentFont, final String $userName, final State $timeRemaining$delegate, final State $isBreakMode$delegate, final State $currentTaskName$delegate, final State $allTasks$delegate, final State $currentTaskId$delegate, final float $bottomPadding, final boolean $hasBanner, final MutableState $showAllTasksSheet$delegate, final State $currentQuote$delegate, final MutableState $showFullScreenSeriousness$delegate, State $showSettings$delegate, final State $settingsInitialTab$delegate, Composer $composer, int $changed) {
        float f;
        float f2;
        Object obj;
        Object obj2;
        Object obj3;
        ComposerKt.sourceInformation($composer, "C542@26732L7,557@27296L88,562@27510L12589,556@27221L12878,803@40512L230,799@40109L633,813@40862L6,814@41037L6,816@41210L122,811@40751L581:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1821730338, $changed, -1, "com.example.HomeScreen.<anonymous>.<anonymous> (MainActivity.kt:542)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume = $composer.consume(AdaptiveSystemKt.getLocalAdaptiveDimensions());
            ComposerKt.sourceInformationMarkerEnd($composer);
            final AdaptiveDimensions adaptiveDimensions = (AdaptiveDimensions) consume;
            if (!adaptiveDimensions.isVeryCompact()) {
                if (adaptiveDimensions.isCompact()) {
                    f = Dp.constructor-impl(220);
                } else {
                    f = Dp.constructor-impl(260);
                }
            } else {
                f = Dp.constructor-impl(180);
            }
            final float circleSize = Dp.constructor-impl(f * adaptiveDimensions.getScale());
            if (!adaptiveDimensions.isVeryCompact()) {
                if (adaptiveDimensions.isCompact()) {
                    f2 = Dp.constructor-impl(16);
                } else {
                    f2 = Dp.constructor-impl(24);
                }
            } else {
                f2 = Dp.constructor-impl(12);
            }
            final float verticalSpacing = Dp.constructor-impl(f2 * adaptiveDimensions.getScale());
            Modifier fillMaxSize$default = SizeKt.fillMaxSize$default(Modifier.Companion, 0.0f, 1, (Object) null);
            Unit unit = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart($composer, 1663822934, "CC(remember):MainActivity.kt#9igjgp");
            boolean changedInstance = $composer.changedInstance($focusManager);
            Object rememberedValue = $composer.rememberedValue();
            if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
                obj = (Function2) new HomeScreen.4.1.1.1($focusManager, (Continuation) null);
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ScaffoldKt.Scaffold-TvnljyQ(WindowInsetsPadding_androidKt.imePadding(SuspendingPointerInputFilterKt.pointerInput(fillMaxSize$default, unit, (Function2) obj)), (Function2) null, (Function2) null, (Function2) null, (Function2) null, 0, Color.Companion.getTransparent-0d7_KjU(), 0L, WindowInsetsKt.WindowInsets-a9UjIt4$default(Dp.constructor-impl(0), 0.0f, 0.0f, 0.0f, 14, (Object) null), ComposableLambdaKt.rememberComposableLambda(1114272045, true, new Function3() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda151
                public final Object invoke(Object obj4, Object obj5, Object obj6) {
                    return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148($protectionStates, verticalSpacing, $context, circleSize, $view, $timerState$delegate, $showJustInTimeSetup$delegate, $currentTheme, $viewModel, $currentFont, $userName, $timeRemaining$delegate, $isBreakMode$delegate, $currentTaskName$delegate, $allTasks$delegate, $currentTaskId$delegate, $bottomPadding, adaptiveDimensions, $hasBanner, $showAllTasksSheet$delegate, $currentQuote$delegate, $showFullScreenSeriousness$delegate, (PaddingValues) obj4, (Composer) obj5, ((Integer) obj6).intValue());
                }
            }, $composer, 54), $composer, 806879232, 190);
            AnimatedVisibilityKt.AnimatedVisibility(HomeScreen_6a0pyJM$lambda$97($showFullScreenSeriousness$delegate), (Modifier) null, EnterExitTransitionKt.fadeIn$default(AnimationSpecKt.tween$default(500, 0, (Easing) null, 6, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.scaleIn-L8ZKh-E$default(AnimationSpecKt.spring$default(0.5f, 200.0f, (Object) null, 4, (Object) null), 0.95f, 0L, 4, (Object) null)), EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.tween$default(400, 0, (Easing) null, 6, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.scaleOut-L8ZKh-E$default(AnimationSpecKt.spring$default(0.0f, 1500.0f, (Object) null, 5, (Object) null), 0.95f, 0L, 4, (Object) null)), (String) null, ComposableLambdaKt.rememberComposableLambda(-1686702922, true, new Function3() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda152
                public final Object invoke(Object obj4, Object obj5, Object obj6) {
                    return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$151($currentQuote$delegate, $currentTaskName$delegate, $timeRemaining$delegate, $showFullScreenSeriousness$delegate, (AnimatedVisibilityScope) obj4, (Composer) obj5, ((Integer) obj6).intValue());
                }
            }, $composer, 54), $composer, 196608, 18);
            boolean HomeScreen_6a0pyJM$lambda$94 = HomeScreen_6a0pyJM$lambda$94($showSettings$delegate);
            FiniteAnimationSpec spring$default = AnimationSpecKt.spring$default(1.0f, 200.0f, (Object) null, 4, (Object) null);
            ComposerKt.sourceInformationMarkerStart($composer, 1664256964, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue2 = $composer.rememberedValue();
            if (rememberedValue2 == Composer.Companion.getEmpty()) {
                obj2 = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda153
                    public final Object invoke(Object obj4) {
                        return Integer.valueOf(MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$153$lambda$152(((Integer) obj4).intValue()));
                    }
                };
                $composer.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            EnterTransition plus = EnterExitTransitionKt.slideInVertically(spring$default, (Function1) obj2).plus(EnterExitTransitionKt.fadeIn$default((FiniteAnimationSpec) null, 0.0f, 3, (Object) null));
            FiniteAnimationSpec spring$default2 = AnimationSpecKt.spring$default(0.0f, 1500.0f, (Object) null, 5, (Object) null);
            ComposerKt.sourceInformationMarkerStart($composer, 1664262564, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue3 = $composer.rememberedValue();
            if (rememberedValue3 == Composer.Companion.getEmpty()) {
                obj3 = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda154
                    public final Object invoke(Object obj4) {
                        return Integer.valueOf(MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$155$lambda$154(((Integer) obj4).intValue()));
                    }
                };
                $composer.updateRememberedValue(obj3);
            } else {
                obj3 = rememberedValue3;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            AnimatedVisibilityKt.AnimatedVisibility(HomeScreen_6a0pyJM$lambda$94, BackgroundKt.background-bw27NRU$default(SizeKt.fillMaxSize$default(Modifier.Companion, 0.0f, 1, (Object) null), $currentTheme.getBackground-0d7_KjU(), (Shape) null, 2, (Object) null), plus, EnterExitTransitionKt.slideOutVertically(spring$default2, (Function1) obj3).plus(EnterExitTransitionKt.fadeOut$default((FiniteAnimationSpec) null, 0.0f, 3, (Object) null)), (String) null, ComposableLambdaKt.rememberComposableLambda(382343725, true, new Function3() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda155
                public final Object invoke(Object obj4, Object obj5, Object obj6) {
                    return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$158($viewModel, $settingsInitialTab$delegate, (AnimatedVisibilityScope) obj4, (Composer) obj5, ((Integer) obj6).intValue());
                }
            }, $composer, 54), $composer, 196608, 16);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x084b  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0856  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x086f  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0874  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0922  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x092f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x09b1  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x09bd  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x09c3  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x09f6  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0a0c  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0a7c  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0a8c  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0afb  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0b02  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0b27  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0b9a  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0c33  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0c3f  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0c43  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0c71  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0c87  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0cd8  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0cdb  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0cfc  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0d09  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0d98  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0b48 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0276  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0282  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0288  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x039c  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x03a8  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x03ae  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x03e1  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x03f7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0529  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0535  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x053b  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x056e  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0584 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x06a7  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x06b3  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x06b9  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x06ec  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0702 A[ADDED_TO_REGION] */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148(final com.example.ui.SystemProtectionStates r144, float r145, final android.content.Context r146, float r147, final android.view.View r148, final androidx.compose.runtime.State r149, final androidx.compose.runtime.MutableState r150, final com.example.ui.theme.ThemeOption r151, final com.example.ui.TimerViewModel r152, androidx.compose.ui.text.font.FontFamily r153, java.lang.String r154, androidx.compose.runtime.State r155, final androidx.compose.runtime.State r156, androidx.compose.runtime.State r157, final androidx.compose.runtime.State r158, final androidx.compose.runtime.State r159, final float r160, final com.example.ui.components.AdaptiveDimensions r161, final boolean r162, final androidx.compose.runtime.MutableState r163, final androidx.compose.runtime.State r164, final androidx.compose.runtime.MutableState r165, androidx.compose.foundation.layout.PaddingValues r166, androidx.compose.runtime.Composer r167, int r168) {
        /*
            Method dump skipped, instructions count: 3486
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148(com.example.ui.SystemProtectionStates, float, android.content.Context, float, android.view.View, androidx.compose.runtime.State, androidx.compose.runtime.MutableState, com.example.ui.theme.ThemeOption, com.example.ui.TimerViewModel, androidx.compose.ui.text.font.FontFamily, java.lang.String, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, float, com.example.ui.components.AdaptiveDimensions, boolean, androidx.compose.runtime.MutableState, androidx.compose.runtime.State, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.PaddingValues, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$108(SystemProtectionStates $protectionStates, final MutableState $showJustInTimeSetup$delegate, AnimatedVisibilityScope $this$AnimatedVisibility, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$AnimatedVisibility, "$this$AnimatedVisibility");
        ComposerKt.sourceInformation($composer, "C581@28457L74,579@28352L197:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(1831168415, $changed, -1, "com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:579)");
        }
        ComposerKt.sourceInformationMarkerStart($composer, -1183740055, "CC(remember):MainActivity.kt#9igjgp");
        Object rememberedValue = $composer.rememberedValue();
        if (rememberedValue == Composer.Companion.getEmpty()) {
            obj = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda111
                public final Object invoke() {
                    return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$108$lambda$107$lambda$106($showJustInTimeSetup$delegate);
                }
            };
            $composer.updateRememberedValue(obj);
        } else {
            obj = rememberedValue;
        }
        ComposerKt.sourceInformationMarkerEnd($composer);
        SystemProtectionStateKt.SystemProtectionBanner($protectionStates, (Function0) obj, $composer, 48);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$108$lambda$107$lambda$106(MutableState $showJustInTimeSetup$delegate) {
        HomeScreen_6a0pyJM$lambda$101($showJustInTimeSetup$delegate, true);
        return Unit.INSTANCE;
    }

    private static final boolean HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$113(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final float HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$114(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$116$lambda$115(View $view, TimerViewModel $viewModel) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        $viewModel.setSettingsOpen(true, 0);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$119$lambda$118$lambda$117(State $settingsRotation$delegate, GraphicsLayerScope $this$graphicsLayer) {
        Intrinsics.checkNotNullParameter($this$graphicsLayer, "$this$graphicsLayer");
        $this$graphicsLayer.setRotationZ(HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$120$lambda$114($settingsRotation$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$124$lambda$123(SystemProtectionStates $protectionStates, View $view, Context $context, MutableState $showJustInTimeSetup$delegate) {
        if ($protectionStates.getNeedsAttention()) {
            try {
                $view.performHapticFeedback(0);
            } catch (Exception e) {
            }
            HomeScreen_6a0pyJM$lambda$101($showJustInTimeSetup$delegate, true);
        } else {
            Intent intent = new Intent($context, TimerService.class);
            intent.setAction("ACTION_START");
            $context.startService(intent);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ContentTransform HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$128$lambda$127(AnimatedContentTransitionScope $this$AnimatedContent) {
        Intrinsics.checkNotNullParameter($this$AnimatedContent, "$this$AnimatedContent");
        return AnimatedContentKt.togetherWith(EnterExitTransitionKt.fadeIn$default(AnimationSpecKt.spring$default(0.0f, 200.0f, (Object) null, 5, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.slideInVertically(AnimationSpecKt.spring$default(0.5f, 200.0f, (Object) null, 4, (Object) null), new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda2
            public final Object invoke(Object obj) {
                return Integer.valueOf(MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$128$lambda$127$lambda$125(((Integer) obj).intValue()));
            }
        })), EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.spring$default(0.0f, 1500.0f, (Object) null, 5, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.slideOutVertically(AnimationSpecKt.spring$default(0.0f, 1500.0f, (Object) null, 5, (Object) null), new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda3
            public final Object invoke(Object obj) {
                return Integer.valueOf(MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$128$lambda$127$lambda$126(((Integer) obj).intValue()));
            }
        })));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$128$lambda$127$lambda$125(int it) {
        return 100;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$128$lambda$127$lambda$126(int it) {
        return -100;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0212  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0255  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x026b  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0315  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0323 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0391  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0397  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0442  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0450  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x048e  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0517  */
    /* JADX WARN: Type inference failed for: r5v54 */
    /* JADX WARN: Type inference failed for: r5v83 */
    /* JADX WARN: Type inference failed for: r5v89 */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145(float r79, com.example.ui.components.AdaptiveDimensions r80, boolean r81, final com.example.ui.theme.ThemeOption r82, final com.example.ui.TimerViewModel r83, androidx.compose.runtime.State r84, androidx.compose.runtime.State r85, final androidx.compose.runtime.MutableState r86, final android.view.View r87, androidx.compose.runtime.State r88, androidx.compose.runtime.State r89, androidx.compose.runtime.State r90, final androidx.compose.runtime.MutableState r91, androidx.compose.animation.AnimatedContentScope r92, boolean r93, androidx.compose.runtime.Composer r94, int r95) {
        /*
            Method dump skipped, instructions count: 1992
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145(float, com.example.ui.components.AdaptiveDimensions, boolean, com.example.ui.theme.ThemeOption, com.example.ui.TimerViewModel, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.MutableState, android.view.View, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.MutableState, androidx.compose.animation.AnimatedContentScope, boolean, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$132$lambda$130$lambda$129(TimerViewModel $viewModel) {
        $viewModel.setAddingTask(true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$132$lambda$131(ThemeOption $currentTheme, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C730@36189L88:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1563276960, $changed, -1, "com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:730)");
            }
            IconKt.Icon-ww6aTOc(AddKt.getAdd(Icons.Filled.INSTANCE), "Add Task", (Modifier) null, $currentTheme.getTextPrimary-0d7_KjU(), $composer, 48, 4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$137$lambda$134$lambda$133(TimerViewModel $viewModel, TaskItem $task, TaskItem it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $viewModel.setTask($task.getId(), $task.getName(), Long.valueOf($task.getCategoryColor()));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$137$lambda$136$lambda$135(TimerViewModel $viewModel, TaskItem $task) {
        $viewModel.deleteTask($task);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$139$lambda$138(MutableState $showAllTasksSheet$delegate) {
        HomeScreen_6a0pyJM$lambda$93($showAllTasksSheet$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$141$lambda$140(ThemeOption $currentTheme, RowScope $this$TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C756@37714L90:MainActivity.kt#to5c3");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1572464585, $changed, -1, "com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:756)");
            }
            TextKt.Text--4IGK_g("View all tasks →", (Modifier) null, $currentTheme.getTextSecondary-0d7_KjU(), 0L, (FontStyle) null, FontWeight.Companion.getBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 196614, 0, 131034);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$146$lambda$145$lambda$144$lambda$143$lambda$142(View $view, MutableState $showFullScreenSeriousness$delegate) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        HomeScreen_6a0pyJM$lambda$98($showFullScreenSeriousness$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$151(State $currentQuote$delegate, State $currentTaskName$delegate, State $timeRemaining$delegate, final MutableState $showFullScreenSeriousness$delegate, AnimatedVisibilityScope $this$AnimatedVisibility, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$AnimatedVisibility, "$this$AnimatedVisibility");
        ComposerKt.sourceInformation($composer, "C808@40689L37,804@40522L214:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(-1686702922, $changed, -1, "com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:804)");
        }
        String HomeScreen_6a0pyJM$lambda$84 = HomeScreen_6a0pyJM$lambda$84($currentQuote$delegate);
        String HomeScreen_6a0pyJM$lambda$86 = HomeScreen_6a0pyJM$lambda$86($currentTaskName$delegate);
        int HomeScreen_6a0pyJM$lambda$85 = HomeScreen_6a0pyJM$lambda$85($timeRemaining$delegate);
        ComposerKt.sourceInformationMarkerStart($composer, -1718831749, "CC(remember):MainActivity.kt#9igjgp");
        Object rememberedValue = $composer.rememberedValue();
        if (rememberedValue == Composer.Companion.getEmpty()) {
            obj = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda40
                public final Object invoke() {
                    return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$151$lambda$150$lambda$149($showFullScreenSeriousness$delegate);
                }
            };
            $composer.updateRememberedValue(obj);
        } else {
            obj = rememberedValue;
        }
        ComposerKt.sourceInformationMarkerEnd($composer);
        SeriousFullscreenOverlay(HomeScreen_6a0pyJM$lambda$84, HomeScreen_6a0pyJM$lambda$86, HomeScreen_6a0pyJM$lambda$85, (Function0) obj, $composer, 3072);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$151$lambda$150$lambda$149(MutableState $showFullScreenSeriousness$delegate) {
        HomeScreen_6a0pyJM$lambda$98($showFullScreenSeriousness$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$153$lambda$152(int it) {
        return it;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$155$lambda$154(int it) {
        return it;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$158(final TimerViewModel $viewModel, State $settingsInitialTab$delegate, AnimatedVisibilityScope $this$AnimatedVisibility, Composer $composer, int $changed) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$AnimatedVisibility, "$this$AnimatedVisibility");
        ComposerKt.sourceInformation($composer, "C817@41252L36,817@41224L98:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(382343725, $changed, -1, "com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:817)");
        }
        ComposerKt.sourceInformationMarkerStart($composer, -1934866351, "CC(remember):MainActivity.kt#9igjgp");
        boolean changedInstance = $composer.changedInstance($viewModel);
        Object rememberedValue = $composer.rememberedValue();
        if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
            obj = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda51
                public final Object invoke() {
                    return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$158$lambda$157$lambda$156($viewModel);
                }
            };
            $composer.updateRememberedValue(obj);
        } else {
            obj = rememberedValue;
        }
        ComposerKt.sourceInformationMarkerEnd($composer);
        SettingsOverlay((Function0) obj, HomeScreen_6a0pyJM$lambda$95($settingsInitialTab$delegate), $composer, 0, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$158$lambda$157$lambda$156(TimerViewModel $viewModel) {
        TimerViewModel.setSettingsOpen$default($viewModel, false, 0, 2, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$162$lambda$161(MutableState $showJustInTimeSetup$delegate) {
        HomeScreen_6a0pyJM$lambda$101($showJustInTimeSetup$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01d7  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x02a2  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x02b2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0307 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x038a  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit HomeScreen_6a0pyJM$lambda$172(final com.example.ui.theme.ThemeOption r65, com.example.ui.SystemProtectionStates r66, final android.content.Context r67, final com.example.ui.TimerViewModel r68, final androidx.compose.runtime.MutableState r69, androidx.compose.runtime.Composer r70, int r71) {
        /*
            Method dump skipped, instructions count: 912
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.HomeScreen_6a0pyJM$lambda$172(com.example.ui.theme.ThemeOption, com.example.ui.SystemProtectionStates, android.content.Context, com.example.ui.TimerViewModel, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$172$lambda$171$lambda$170$lambda$165$lambda$164(Context $context, TimerViewModel $viewModel, MutableState $showJustInTimeSetup$delegate, String actionId) {
        Intrinsics.checkNotNullParameter(actionId, "actionId");
        try {
            switch (actionId.hashCode()) {
                case 67834:
                    if (!actionId.equals("DND")) {
                        break;
                    } else {
                        $context.startActivity(new Intent("android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS"));
                        break;
                    }
                case 93629640:
                    if (actionId.equals("NOTIFICATIONS")) {
                        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
                        intent.putExtra("android.provider.extra.APP_PACKAGE", $context.getPackageName());
                        $context.startActivity(intent);
                        break;
                    }
                    break;
                case 386742765:
                    if (!actionId.equals("BATTERY")) {
                        break;
                    } else {
                        $context.startActivity(new Intent("android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS"));
                        break;
                    }
            }
        } catch (Exception e) {
            $viewModel.setSettingsOpen(true, 3);
        }
        HomeScreen_6a0pyJM$lambda$101($showJustInTimeSetup$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$172$lambda$171$lambda$170$lambda$168$lambda$167(Context $context, MutableState $showJustInTimeSetup$delegate) {
        HomeScreen_6a0pyJM$lambda$101($showJustInTimeSetup$delegate, false);
        Intent intent = new Intent($context, TimerService.class);
        intent.setAction("ACTION_START");
        $context.startService(intent);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit HomeScreen_6a0pyJM$lambda$172$lambda$171$lambda$170$lambda$169(ThemeOption $currentTheme, RowScope $this$TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C868@44149L71:MainActivity.kt#to5c3");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1953007553, $changed, -1, "com.example.HomeScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:868)");
            }
            TextKt.Text--4IGK_g("Continue without protection", (Modifier) null, $currentTheme.getTextSecondary-0d7_KjU(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 6, 0, 131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$175$lambda$174(final TimerViewModel $viewModel, final String name, String cat, final long color) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(cat, "cat");
        $viewModel.saveTask(name, cat, color, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda36
            public final Object invoke(Object obj) {
                return MainActivityKt.HomeScreen_6a0pyJM$lambda$175$lambda$174$lambda$173($viewModel, name, color, ((Integer) obj).intValue());
            }
        });
        $viewModel.setAddingTask(false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$175$lambda$174$lambda$173(TimerViewModel $viewModel, String $name, long $color, int newId) {
        $viewModel.setTask(newId, $name, Long.valueOf($color));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$177$lambda$176(TimerViewModel $viewModel) {
        $viewModel.setAddingTask(false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$179$lambda$178(MutableState $showAllTasksSheet$delegate) {
        HomeScreen_6a0pyJM$lambda$93($showAllTasksSheet$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0278  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit HomeScreen_6a0pyJM$lambda$187(com.example.ui.theme.ThemeOption r57, final androidx.compose.runtime.State r58, final androidx.compose.runtime.State r59, final com.example.ui.TimerViewModel r60, final androidx.compose.runtime.MutableState r61, androidx.compose.foundation.layout.ColumnScope r62, androidx.compose.runtime.Composer r63, int r64) {
        /*
            Method dump skipped, instructions count: 638
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.HomeScreen_6a0pyJM$lambda$187(com.example.ui.theme.ThemeOption, androidx.compose.runtime.State, androidx.compose.runtime.State, com.example.ui.TimerViewModel, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184(State $allTasks$delegate, TimerViewModel $viewModel, State $currentTaskId$delegate, MutableState $showAllTasksSheet$delegate, LazyListScope $this$LazyColumn) {
        Intrinsics.checkNotNullParameter($this$LazyColumn, "$this$LazyColumn");
        List HomeScreen_6a0pyJM$lambda$89 = HomeScreen_6a0pyJM$lambda$89($allTasks$delegate);
        $this$LazyColumn.items(HomeScreen_6a0pyJM$lambda$89.size(), new MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$.inlined.items.default.2(new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda4
            public final Object invoke(Object obj) {
                return MainActivityKt.HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$lambda$180((TaskItem) obj);
            }
        }, HomeScreen_6a0pyJM$lambda$89), new MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$.inlined.items.default.3(MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$.inlined.items.default.1.INSTANCE, HomeScreen_6a0pyJM$lambda$89), ComposableLambdaKt.composableLambdaInstance(-632812321, true, new MainActivityKt$HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$.inlined.items.default.4(HomeScreen_6a0pyJM$lambda$89, $viewModel, $currentTaskId$delegate, $showAllTasksSheet$delegate)));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Object HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184$lambda$180(TaskItem it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Integer.valueOf(it.getId());
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x04a3  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x04a6  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x04b2  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x050e  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x051d  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0566  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0583  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0647  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void SettingsOverlay(@org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function0<kotlin.Unit> r42, int r43, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r44, final int r45, final int r46) {
        /*
            Method dump skipped, instructions count: 1627
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay(kotlin.jvm.functions.Function0, int, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int SettingsOverlay$lambda$192(MutableIntState $selectedTabIndex$delegate) {
        return ((IntState) $selectedTabIndex$delegate).getIntValue();
    }

    private static final boolean SettingsOverlay$lambda$197(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void SettingsOverlay$lambda$198(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$201$lambda$200(MutableState $showBlockedApps$delegate) {
        SettingsOverlay$lambda$198($showBlockedApps$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final float SettingsOverlay$lambda$204(MutableState<Float> mutableState) {
        return ((Number) ((State) mutableState).getValue()).floatValue();
    }

    private static final void SettingsOverlay$lambda$205(MutableState<Float> mutableState, float f) {
        mutableState.setValue(Float.valueOf(f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final float SettingsOverlay$lambda$207(MutableState<Float> mutableState) {
        return ((Number) ((State) mutableState).getValue()).floatValue();
    }

    private static final void SettingsOverlay$lambda$208(MutableState<Float> mutableState, float f) {
        mutableState.setValue(Float.valueOf(f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int SettingsOverlay$lambda$210(MutableState<Integer> mutableState) {
        return ((Number) ((State) mutableState).getValue()).intValue();
    }

    private static final void SettingsOverlay$lambda$211(MutableState<Integer> mutableState, int i) {
        mutableState.setValue(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final float SettingsOverlay$lambda$213(MutableState<Float> mutableState) {
        return ((Number) ((State) mutableState).getValue()).floatValue();
    }

    private static final void SettingsOverlay$lambda$214(MutableState<Float> mutableState, float f) {
        mutableState.setValue(Float.valueOf(f));
    }

    private static final float SettingsOverlay$lambda$216(MutableState<Float> mutableState) {
        return ((Number) ((State) mutableState).getValue()).floatValue();
    }

    private static final void SettingsOverlay$lambda$217(MutableState<Float> mutableState, float f) {
        mutableState.setValue(Float.valueOf(f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$220$lambda$219(Context $context, MutableState $localCompletion$delegate, Uri uri) {
        if (uri != null) {
            try {
                $context.getContentResolver().takePersistableUriPermission(uri, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SettingsManager.INSTANCE.setCustomCompletionUri(uri.toString());
            SettingsOverlay$lambda$211($localCompletion$delegate, 3);
            SoundPlayer.INSTANCE.playCompletion($context, SettingsOverlay$lambda$210($localCompletion$delegate));
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$222$lambda$221(SnapshotStateList $tabBackStack, View $view, Function0 $onDismiss, MutableIntState $selectedTabIndex$delegate) {
        if (!((Collection) $tabBackStack).isEmpty()) {
            Object removeLast = $tabBackStack.removeLast();
            Intrinsics.checkNotNullExpressionValue(removeLast, "removeLast(...)");
            int prevTab = ((Number) removeLast).intValue();
            try {
                $view.performHapticFeedback(3);
            } catch (Exception e) {
            }
            $selectedTabIndex$delegate.setIntValue(prevTab);
        } else {
            try {
                $view.performHapticFeedback(3);
            } catch (Exception e2) {
            }
            SoundPlayer.INSTANCE.stop();
            $onDismiss.invoke();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:36:0x022a  */
    /* JADX WARN: Type inference failed for: r2v31 */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsOverlay$lambda$228(final com.example.ui.theme.ThemeOption r53, final android.view.View r54, final kotlin.jvm.functions.Function0 r55, androidx.compose.runtime.Composer r56, int r57) {
        /*
            Method dump skipped, instructions count: 560
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay$lambda$228(com.example.ui.theme.ThemeOption, android.view.View, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$228$lambda$227$lambda$225$lambda$224(View $view, Function0 $onDismiss) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        SoundPlayer.INSTANCE.stop();
        $onDismiss.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit SettingsOverlay$lambda$228$lambda$227$lambda$226(ThemeOption $currentTheme, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C1032@51072L195:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-796569622, $changed, -1, "com.example.SettingsOverlay.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1032)");
            }
            IconKt.Icon-ww6aTOc(CloseKt.getClose(Icons.Filled.INSTANCE), "Close", (Modifier) null, $currentTheme.getTextPrimary-0d7_KjU(), $composer, 48, 4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:42:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x02ac  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x02dd  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x02f3  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0368  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0375  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x03f5  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsOverlay$lambda$306(androidx.compose.foundation.lazy.LazyListState r63, final java.util.List r64, final androidx.compose.runtime.MutableIntState r65, final android.view.View r66, final androidx.compose.runtime.snapshots.SnapshotStateList r67, final android.content.Context r68, final androidx.compose.runtime.MutableState r69, final androidx.compose.runtime.MutableState r70, final androidx.compose.runtime.MutableState r71, final android.media.AudioManager r72, final float r73, final androidx.compose.runtime.MutableState r74, final androidx.activity.compose.ManagedActivityResultLauncher r75, final androidx.compose.runtime.MutableState r76, final androidx.compose.runtime.MutableState r77, androidx.compose.foundation.layout.PaddingValues r78, androidx.compose.runtime.Composer r79, int r80) {
        /*
            Method dump skipped, instructions count: 1019
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay$lambda$306(androidx.compose.foundation.lazy.LazyListState, java.util.List, androidx.compose.runtime.MutableIntState, android.view.View, androidx.compose.runtime.snapshots.SnapshotStateList, android.content.Context, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, android.media.AudioManager, float, androidx.compose.runtime.MutableState, androidx.activity.compose.ManagedActivityResultLauncher, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.PaddingValues, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236(final List $tabs, final ThemeOption $currentTheme, final View $view, final MutableIntState $selectedTabIndex$delegate, final SnapshotStateList $tabBackStack, LazyListScope $this$LazyRow) {
        Intrinsics.checkNotNullParameter($this$LazyRow, "$this$LazyRow");
        LazyListScope.items$default($this$LazyRow, $tabs.size(), (Function1) null, (Function1) null, ComposableLambdaKt.composableLambdaInstance(-1228530455, true, new Function4() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda124
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235($currentTheme, $view, $selectedTabIndex$delegate, $tabBackStack, $tabs, (LazyItemScope) obj, ((Integer) obj2).intValue(), (Composer) obj3, ((Integer) obj4).intValue());
            }
        }), 6, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:66:0x031d  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235(com.example.ui.theme.ThemeOption r60, final android.view.View r61, final androidx.compose.runtime.MutableIntState r62, final androidx.compose.runtime.snapshots.SnapshotStateList r63, java.util.List r64, androidx.compose.foundation.lazy.LazyItemScope r65, final int r66, androidx.compose.runtime.Composer r67, int r68) {
        /*
            Method dump skipped, instructions count: 803
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235(com.example.ui.theme.ThemeOption, android.view.View, androidx.compose.runtime.MutableIntState, androidx.compose.runtime.snapshots.SnapshotStateList, java.util.List, androidx.compose.foundation.lazy.LazyItemScope, int, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    private static final long SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$229(State<Color> state) {
        return ((Color) state.getValue()).unbox-impl();
    }

    private static final long SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$230(State<Color> state) {
        return ((Color) state.getValue()).unbox-impl();
    }

    private static final float SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$231(State<Dp> state) {
        return ((Dp) state.getValue()).unbox-impl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235$lambda$233$lambda$232(View $view, int $index, SnapshotStateList $tabBackStack, MutableIntState $selectedTabIndex$delegate) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        if (SettingsOverlay$lambda$192($selectedTabIndex$delegate) != $index) {
            $tabBackStack.add(Integer.valueOf(SettingsOverlay$lambda$192($selectedTabIndex$delegate)));
            $selectedTabIndex$delegate.setIntValue($index);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ContentTransform SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$241$lambda$240(final AnimatedContentTransitionScope $this$AnimatedContent) {
        Intrinsics.checkNotNullParameter($this$AnimatedContent, "$this$AnimatedContent");
        return AnimatedContentKt.togetherWith(EnterExitTransitionKt.fadeIn$default(AnimationSpecKt.tween$default(220, 90, (Easing) null, 4, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.slideInHorizontally(AnimationSpecKt.spring$default(0.8f, 300.0f, (Object) null, 4, (Object) null), new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda49
            public final Object invoke(Object obj) {
                return Integer.valueOf(MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$241$lambda$240$lambda$238($this$AnimatedContent, ((Integer) obj).intValue()));
            }
        })), EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.tween$default(90, 0, (Easing) null, 6, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.slideOutHorizontally(AnimationSpecKt.spring$default(0.8f, 300.0f, (Object) null, 4, (Object) null), new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda50
            public final Object invoke(Object obj) {
                return Integer.valueOf(MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$241$lambda$240$lambda$239($this$AnimatedContent, ((Integer) obj).intValue()));
            }
        })));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$241$lambda$240$lambda$238(AnimatedContentTransitionScope $this_AnimatedContent, int fullWidth) {
        return ((Number) $this_AnimatedContent.getTargetState()).intValue() > ((Number) $this_AnimatedContent.getInitialState()).intValue() ? fullWidth : -fullWidth;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$241$lambda$240$lambda$239(AnimatedContentTransitionScope $this_AnimatedContent, int fullWidth) {
        return ((Number) $this_AnimatedContent.getTargetState()).intValue() > ((Number) $this_AnimatedContent.getInitialState()).intValue() ? -fullWidth : fullWidth;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x02cb  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x02f4  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0305  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x030f  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x07a4  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x07b4  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x07c4  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303(com.example.ui.theme.ThemeOption r64, android.content.Context r65, android.view.View r66, final androidx.compose.runtime.MutableState r67, final androidx.compose.runtime.MutableState r68, final androidx.compose.runtime.MutableState r69, final android.media.AudioManager r70, final float r71, final androidx.compose.runtime.MutableState r72, final androidx.activity.compose.ManagedActivityResultLauncher r73, final androidx.compose.runtime.MutableState r74, final androidx.compose.runtime.MutableState r75, androidx.compose.animation.AnimatedContentScope r76, int r77, androidx.compose.runtime.Composer r78, int r79) {
        /*
            Method dump skipped, instructions count: 2056
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303(com.example.ui.theme.ThemeOption, android.content.Context, android.view.View, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, android.media.AudioManager, float, androidx.compose.runtime.MutableState, androidx.activity.compose.ManagedActivityResultLauncher, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.animation.AnimatedContentScope, int, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    private static final String SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$243(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final String SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$246(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x08c6  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x08c9  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x08ce  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0a17  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0a23  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0a29  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0a5c  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0a72 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0af6  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0c24  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0c30  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0c36  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0c69  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0c7f  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0cd9  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0ce0  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0dde  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0e6d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x03d5  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03e3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x045f  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0561  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x056d  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0573  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x05a4  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x05ba A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x06fb  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0708  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0782  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0879  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x088a  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x08c0  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x08c2  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273(com.example.ui.theme.ThemeOption r113, final android.view.View r114, final android.media.AudioManager r115, float r116, final androidx.compose.runtime.MutableState r117, final androidx.compose.runtime.MutableState r118, final androidx.compose.runtime.MutableState r119, final androidx.compose.runtime.MutableState r120, final androidx.compose.runtime.MutableState r121, final androidx.activity.compose.ManagedActivityResultLauncher r122, final android.content.Context r123, final androidx.compose.runtime.MutableState r124, final androidx.compose.runtime.MutableState r125, androidx.compose.foundation.layout.ColumnScope r126, androidx.compose.runtime.Composer r127, int r128) {
        /*
            Method dump skipped, instructions count: 3699
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273(com.example.ui.theme.ThemeOption, android.view.View, android.media.AudioManager, float, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.activity.compose.ManagedActivityResultLauncher, android.content.Context, androidx.compose.runtime.MutableState, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$252$lambda$251$lambda$250(MutableState $focusInput$delegate, MutableState $localFocus$delegate, String newVal) {
        Intrinsics.checkNotNullParameter(newVal, "newVal");
        String str = newVal;
        Appendable sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (Character.isDigit(charAt)) {
                sb.append(charAt);
            }
        }
        String sb2 = ((StringBuilder) sb).toString();
        $focusInput$delegate.setValue(sb2);
        Float floatOrNull = StringsKt.toFloatOrNull(sb2);
        if (floatOrNull != null) {
            SettingsOverlay$lambda$205($localFocus$delegate, RangesKt.coerceIn(floatOrNull.floatValue(), 1.0f, 200.0f));
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$254$lambda$253(View $view, MutableState $localFocus$delegate, float it) {
        int oldVal = (int) SettingsOverlay$lambda$204($localFocus$delegate);
        int newVal = (int) it;
        if (oldVal != newVal) {
            try {
                $view.performHapticFeedback(4);
            } catch (Exception e) {
            }
        }
        SettingsOverlay$lambda$205($localFocus$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$259$lambda$258$lambda$257(MutableState $breakInput$delegate, MutableState $localBreak$delegate, String newVal) {
        Intrinsics.checkNotNullParameter(newVal, "newVal");
        String str = newVal;
        Appendable sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (Character.isDigit(charAt)) {
                sb.append(charAt);
            }
        }
        String sb2 = ((StringBuilder) sb).toString();
        $breakInput$delegate.setValue(sb2);
        Float floatOrNull = StringsKt.toFloatOrNull(sb2);
        if (floatOrNull != null) {
            SettingsOverlay$lambda$208($localBreak$delegate, RangesKt.coerceIn(floatOrNull.floatValue(), 1.0f, 120.0f));
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$261$lambda$260(View $view, MutableState $localBreak$delegate, float it) {
        int oldVal = (int) SettingsOverlay$lambda$207($localBreak$delegate);
        int newVal = (int) it;
        if (oldVal != newVal) {
            try {
                $view.performHapticFeedback(4);
            } catch (Exception e) {
            }
        }
        SettingsOverlay$lambda$208($localBreak$delegate, it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$263$lambda$262(View $view, AudioManager $audioManager, MutableState $localVolume$delegate, float it) {
        int oldVal = (int) SettingsOverlay$lambda$216($localVolume$delegate);
        int newVal = (int) it;
        if (oldVal != newVal) {
            try {
                $view.performHapticFeedback(4);
            } catch (Exception e) {
            }
        }
        SettingsOverlay$lambda$217($localVolume$delegate, it);
        $audioManager.setStreamVolume(3, (int) it, 0);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$269$lambda$268$lambda$266$lambda$265(View $view, int $index, ManagedActivityResultLauncher $completionLauncher, Context $context, MutableState $localCompletion$delegate) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        if ($index == 3) {
            $completionLauncher.launch(new String[]{"audio/*"});
        } else {
            SettingsOverlay$lambda$211($localCompletion$delegate, $index);
            SoundPlayer.INSTANCE.playCompletion($context, $index);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$271$lambda$270(View $view, MutableState $localCompletionDuration$delegate, float it) {
        int oldVal = (int) SettingsOverlay$lambda$213($localCompletionDuration$delegate);
        int newVal = (int) it;
        if (oldVal != newVal) {
            try {
                $view.performHapticFeedback(4);
            } catch (Exception e) {
            }
        }
        SettingsOverlay$lambda$214($localCompletionDuration$delegate, it);
        return Unit.INSTANCE;
    }

    private static final boolean SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$275(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final float SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$276(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    private static final AppBlockerManager.AccessibilityServiceStatus SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$278(MutableState<AppBlockerManager.AccessibilityServiceStatus> mutableState) {
        return (AppBlockerManager.AccessibilityServiceStatus) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final DisposableEffectResult SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282(LifecycleOwner $lifecycleOwner, final Context $context, final MutableState $accessibilityStatus$delegate, DisposableEffectScope $this$DisposableEffect) {
        Intrinsics.checkNotNullParameter($this$DisposableEffect, "$this$DisposableEffect");
        LifecycleObserver lifecycleObserver = new LifecycleEventObserver() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda52
            public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282$lambda$280($context, $accessibilityStatus$delegate, lifecycleOwner, event);
            }
        };
        $lifecycleOwner.getLifecycle().addObserver(lifecycleObserver);
        return new MainActivityKt$SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282$.inlined.onDispose.1($lifecycleOwner, lifecycleObserver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final void SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282$lambda$280(Context $context, MutableState $accessibilityStatus$delegate, LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        Intrinsics.checkNotNullParameter(lifecycleOwner, "<unused var>");
        Intrinsics.checkNotNullParameter(event, "event");
        if (event != Lifecycle.Event.ON_RESUME && event != Lifecycle.Event.ON_START) {
            return;
        }
        $accessibilityStatus$delegate.setValue(AppBlockerManager.INSTANCE.getAccessibilityStatus($context));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x03af  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x040e  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$288(com.example.ui.theme.ThemeOption r86, final android.content.Context r87, androidx.compose.foundation.layout.ColumnScope r88, androidx.compose.runtime.Composer r89, int r90) {
        /*
            Method dump skipped, instructions count: 1044
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$288(com.example.ui.theme.ThemeOption, android.content.Context, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$288$lambda$287$lambda$286$lambda$285(Context $context) {
        try {
            $context.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
        } catch (Exception e) {
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01cc  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x02bc  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x02bf  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03fc  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0408  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x043f  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0455 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x04d0  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0576  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0584  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x05f0  */
    /* JADX WARN: Type inference failed for: r1v47 */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$296(com.example.ui.theme.ThemeOption r89, boolean r90, final android.content.Context r91, final androidx.compose.runtime.MutableState r92, androidx.compose.foundation.layout.ColumnScope r93, androidx.compose.runtime.Composer r94, int r95) {
        /*
            Method dump skipped, instructions count: 1526
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$296(com.example.ui.theme.ThemeOption, boolean, android.content.Context, androidx.compose.runtime.MutableState, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$296$lambda$295$lambda$294$lambda$291$lambda$290(Context $context, MutableState $accessibilityStatus$delegate) {
        $accessibilityStatus$delegate.setValue(AppBlockerManager.INSTANCE.getAccessibilityStatus($context));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$296$lambda$295$lambda$294$lambda$293$lambda$292(Context $context) {
        try {
            $context.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
        } catch (Exception e) {
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$298$lambda$297(View $view, MutableState $showBlockedApps$delegate) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        SettingsOverlay$lambda$198($showBlockedApps$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x021d  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0233  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0316  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$301(com.example.ui.theme.ThemeOption r72, androidx.compose.foundation.layout.ColumnScope r73, androidx.compose.runtime.Composer r74, int r75) {
        /*
            Method dump skipped, instructions count: 796
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$301(com.example.ui.theme.ThemeOption, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final void TaskItemRow(@NotNull final TaskItem task, boolean isSelected, @NotNull final Function1<? super TaskItem, Unit> function1, @NotNull final Function0<Unit> function0, @Nullable Modifier modifier, @Nullable Composer $composer, final int $changed, final int i) {
        boolean z;
        Modifier modifier2;
        Function0 function02;
        Composer $composer2;
        final boolean isSelected2;
        final Modifier modifier3;
        Intrinsics.checkNotNullParameter(task, "task");
        Intrinsics.checkNotNullParameter(function1, "onSelect");
        Intrinsics.checkNotNullParameter(function0, "onDelete");
        Composer $composer3 = $composer.startRestartGroup(-1073152619);
        ComposerKt.sourceInformation($composer3, "C(TaskItemRow)P(4!1,3,2)1451@77765L7,1452@77827L7,1453@77872L7,1454@77915L7,1456@77978L160,1460@78194L109,1473@78617L43,1479@78970L208,1485@79213L38,1486@79258L2917,1471@78538L3637:MainActivity.kt#to5c3");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(task) ? 4 : 2;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            $dirty |= 48;
            z = isSelected;
        } else if (($changed & 48) == 0) {
            z = isSelected;
            $dirty |= $composer3.changed(z) ? 32 : 16;
        } else {
            z = isSelected;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 2048 : 1024;
        }
        int i3 = i & 16;
        if (i3 != 0) {
            $dirty |= 24576;
            modifier2 = modifier;
        } else if (($changed & 24576) == 0) {
            modifier2 = modifier;
            $dirty |= $composer3.changed(modifier2) ? 16384 : 8192;
        } else {
            modifier2 = modifier;
        }
        if (($dirty & 9363) == 9362 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            isSelected2 = z;
            modifier3 = modifier2;
            $composer2 = $composer3;
        } else {
            boolean isSelected3 = i2 != 0 ? false : z;
            Modifier modifier4 = i3 != 0 ? (Modifier) Modifier.Companion : modifier2;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1073152619, $dirty, -1, "com.example.TaskItemRow (MainActivity.kt:1450)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume = $composer3.consume(AndroidCompositionLocals_androidKt.getLocalContext());
            ComposerKt.sourceInformationMarkerEnd($composer3);
            Context context = (Context) consume;
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume2 = $composer3.consume(AndroidCompositionLocals_androidKt.getLocalView());
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final View view = (View) consume2;
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume3 = $composer3.consume(ThemeKt.getLocalAppTheme());
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final ThemeOption currentTheme = (ThemeOption) consume3;
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume4 = $composer3.consume(TypographyKt.getLocalAppFont());
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final FontFamily currentFont = (FontFamily) consume4;
            State borderColor$delegate = SingleValueAnimationKt.animateColorAsState-euL9pac(isSelected3 ? Color.constructor-impl(ULong.constructor-impl(task.getCategoryColor())) : currentTheme.getCardBorder-0d7_KjU(), (AnimationSpec) null, "borderColor", (Function1) null, $composer3, 384, 10);
            State borderWidth$delegate = AnimateAsStateKt.animateDpAsState-AjpBEmI(isSelected3 ? Dp.constructor-impl((float) 1.5d) : Dp.constructor-impl(1), (AnimationSpec) null, "borderWidth", (Function1) null, $composer3, 384, 10);
            long containerColor = isSelected3 ? ColorKt.compositeOver--OWjLjI(Color.copy-wmQWz5c$default(Color.constructor-impl(ULong.constructor-impl(task.getCategoryColor())), 0.05f, 0.0f, 0.0f, 0.0f, 14, (Object) null), currentTheme.getSurface-0d7_KjU()) : currentTheme.getSurface-0d7_KjU();
            Shape shape = RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24));
            CardColors cardColors = CardDefaults.INSTANCE.cardColors-ro_MJ88(containerColor, 0L, 0L, 0L, $composer3, CardDefaults.$stable << 12, 14);
            Modifier clip = ClipKt.clip(BorderKt.border-xT4_qwU(ShadowKt.shadow-s4CzXII$default(SizeKt.fillMaxWidth$default(modifier4, 0.0f, 1, (Object) null), Dp.constructor-impl(3), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24)), false, 0L, currentTheme.getShadowColor-0d7_KjU(), 12, (Object) null), TaskItemRow$lambda$309(borderWidth$delegate), TaskItemRow$lambda$308(borderColor$delegate), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24))), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(24)));
            ComposerKt.sourceInformationMarkerStart($composer3, -507468987, "CC(remember):MainActivity.kt#9igjgp");
            boolean changedInstance = (($dirty & 896) == 256) | $composer3.changedInstance(view) | (($dirty & 14) == 4);
            Object rememberedValue = $composer3.rememberedValue();
            if (changedInstance || rememberedValue == Composer.Companion.getEmpty()) {
                function02 = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda80
                    public final Object invoke() {
                        return MainActivityKt.TaskItemRow$lambda$311$lambda$310(view, function1, task);
                    }
                };
                $composer3.updateRememberedValue(function02);
            } else {
                function02 = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer2 = $composer3;
            isSelected2 = isSelected3;
            Modifier modifier5 = modifier4;
            CardKt.Card(ClickableKt.clickable-XHw0xAI$default(clip, false, (String) null, (Role) null, function02, 7, (Object) null), shape, cardColors, CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl(0), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, $composer3, (CardDefaults.$stable << 18) | 6, 62), (BorderStroke) null, ComposableLambdaKt.rememberComposableLambda(-1725603449, true, new Function3() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda81
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return MainActivityKt.TaskItemRow$lambda$319(task, isSelected2, view, function0, currentTheme, currentFont, (ColumnScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), $composer2, 196608, 16);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            modifier3 = modifier5;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda82
                public final Object invoke(Object obj, Object obj2) {
                    return MainActivityKt.TaskItemRow$lambda$320(task, isSelected2, function1, function0, modifier3, $changed, i, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    private static final long TaskItemRow$lambda$308(State<Color> state) {
        return ((Color) state.getValue()).unbox-impl();
    }

    private static final float TaskItemRow$lambda$309(State<Dp> state) {
        return ((Dp) state.getValue()).unbox-impl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TaskItemRow$lambda$311$lambda$310(View $view, Function1 $onSelect, TaskItem $task) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        $onSelect.invoke($task);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0223  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0235  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x04de  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x05aa  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0606  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0662  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0688  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit TaskItemRow$lambda$319(final com.example.data.TaskItem r108, boolean r109, final android.view.View r110, final kotlin.jvm.functions.Function0 r111, com.example.ui.theme.ThemeOption r112, androidx.compose.ui.text.font.FontFamily r113, androidx.compose.foundation.layout.ColumnScope r114, androidx.compose.runtime.Composer r115, int r116) {
        /*
            Method dump skipped, instructions count: 1678
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.TaskItemRow$lambda$319(com.example.data.TaskItem, boolean, android.view.View, kotlin.jvm.functions.Function0, com.example.ui.theme.ThemeOption, androidx.compose.ui.text.font.FontFamily, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit TaskItemRow$lambda$319$lambda$318$lambda$315(TaskItem $task, AnimatedVisibilityScope $this$AnimatedVisibility, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$AnimatedVisibility, "$this$AnimatedVisibility");
        ComposerKt.sourceInformation($composer, "C1536@81449L252:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(-941904701, $changed, -1, "com.example.TaskItemRow.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1536)");
        }
        IconKt.Icon-ww6aTOc(CheckCircleKt.getCheckCircle(Icons.Filled.INSTANCE), "Selected", SizeKt.size-3ABfNKs(Modifier.Companion, Dp.constructor-impl(24)), Color.constructor-impl(ULong.constructor-impl($task.getCategoryColor())), $composer, 432, 0);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TaskItemRow$lambda$319$lambda$318$lambda$317$lambda$316(View $view, Function0 $onDelete) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        $onDelete.invoke();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x046d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0501  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01cb  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0315  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0326  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x035d  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x036b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x03a4  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x03b2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x03eb  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03f9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x041e  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0421  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0460  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void PremiumJumpingTextPreview(@org.jetbrains.annotations.NotNull final java.lang.String r71, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r72, final int r73) {
        /*
            Method dump skipped, instructions count: 1299
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.PremiumJumpingTextPreview(java.lang.String, androidx.compose.runtime.Composer, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PremiumJumpingTextPreview$lambda$330$lambda$329$lambda$328$lambda$327(Animatable $animatedOffset, Animatable $animatedScale, Animatable $animatedAlpha, GraphicsLayerScope $this$graphicsLayer) {
        Intrinsics.checkNotNullParameter($this$graphicsLayer, "$this$graphicsLayer");
        $this$graphicsLayer.setTranslationY(((Number) $animatedOffset.getValue()).floatValue());
        $this$graphicsLayer.setScaleX(((Number) $animatedScale.getValue()).floatValue());
        $this$graphicsLayer.setScaleY(((Number) $animatedScale.getValue()).floatValue());
        $this$graphicsLayer.setAlpha(((Number) $animatedAlpha.getValue()).floatValue());
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final void AddTaskCard(@NotNull final Function3<? super String, ? super String, ? super Long, Unit> function3, @NotNull final Function0<Unit> function0, @Nullable Composer $composer, final int $changed) {
        final Function0 onCancel;
        Intrinsics.checkNotNullParameter(function3, "onSave");
        Intrinsics.checkNotNullParameter(function0, "onCancel");
        Composer $composer2 = $composer.startRestartGroup(361970074);
        ComposerKt.sourceInformation($composer2, "C(AddTaskCard)P(1)1629@85296L7,1630@85374L7,1638@85767L7660,1631@85413L8014:MainActivity.kt#to5c3");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changedInstance(function3) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer2.changedInstance(function0) ? 32 : 16;
        }
        if (($dirty & 19) == 18 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
            onCancel = function0;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(361970074, $dirty, -1, "com.example.AddTaskCard (MainActivity.kt:1628)");
            }
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume = $composer2.consume(AndroidCompositionLocals_androidKt.getLocalContext());
            ComposerKt.sourceInformationMarkerEnd($composer2);
            Context context = (Context) consume;
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume2 = $composer2.consume(CompositionLocalsKt.getLocalFocusManager());
            ComposerKt.sourceInformationMarkerEnd($composer2);
            final FocusManager focusManager = (FocusManager) consume2;
            onCancel = function0;
            AndroidDialog_androidKt.Dialog(onCancel, new DialogProperties(true, false, false), ComposableLambdaKt.rememberComposableLambda(1331493425, true, new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda12
                public final Object invoke(Object obj, Object obj2) {
                    return MainActivityKt.AddTaskCard$lambda$368(focusManager, function3, function0, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer2, 54), $composer2, (($dirty >> 3) & 14) | 432, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda13
                public final Object invoke(Object obj, Object obj2) {
                    return MainActivityKt.AddTaskCard$lambda$369(function3, onCancel, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:59:0x037c  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x038b  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0420  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit AddTaskCard$lambda$368(final androidx.compose.ui.focus.FocusManager r52, final kotlin.jvm.functions.Function3 r53, final kotlin.jvm.functions.Function0 r54, androidx.compose.runtime.Composer r55, int r56) {
        /*
            Method dump skipped, instructions count: 1062
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.AddTaskCard$lambda$368(androidx.compose.ui.focus.FocusManager, kotlin.jvm.functions.Function3, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    private static final boolean AddTaskCard$lambda$368$lambda$333(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void AddTaskCard$lambda$368$lambda$334(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final float AddTaskCard$lambda$368$lambda$336(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    private static final float AddTaskCard$lambda$368$lambda$337(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AddTaskCard$lambda$368$lambda$367$lambda$340$lambda$339(State $scale$delegate, State $alpha$delegate, GraphicsLayerScope $this$graphicsLayer) {
        Intrinsics.checkNotNullParameter($this$graphicsLayer, "$this$graphicsLayer");
        $this$graphicsLayer.setScaleX(AddTaskCard$lambda$368$lambda$336($scale$delegate));
        $this$graphicsLayer.setScaleY(AddTaskCard$lambda$368$lambda$336($scale$delegate));
        $this$graphicsLayer.setAlpha(AddTaskCard$lambda$368$lambda$337($alpha$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0819  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0887  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0897 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x092c  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x093a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x09a5  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x03c2  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x041d  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x055b  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0567  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x056d  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x05a0  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x05b6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0623  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x07c0  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x07cc  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x07d2  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0803  */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r4v40 */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit AddTaskCard$lambda$368$lambda$367$lambda$366(androidx.compose.ui.focus.FocusManager r137, final com.example.ui.theme.ThemeOption r138, final android.view.View r139, final kotlin.jvm.functions.Function3 r140, final kotlin.jvm.functions.Function0 r141, final androidx.compose.ui.text.font.FontFamily r142, androidx.compose.foundation.layout.ColumnScope r143, androidx.compose.runtime.Composer r144, int r145) {
        /*
            Method dump skipped, instructions count: 2475
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366(androidx.compose.ui.focus.FocusManager, com.example.ui.theme.ThemeOption, android.view.View, kotlin.jvm.functions.Function3, kotlin.jvm.functions.Function0, androidx.compose.ui.text.font.FontFamily, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    private static final String AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final Pair<String, Color> AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346(MutableState<Pair<String, Color>> mutableState) {
        return (Pair) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$353$lambda$352(MutableState $title$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $title$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$351$lambda$350(View $view, Function3 $onSave, MutableState $title$delegate, MutableState $selectedCategory$delegate, KeyboardActionScope $this$KeyboardActions) {
        Intrinsics.checkNotNullParameter($this$KeyboardActions, "$this$KeyboardActions");
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        String AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343 = AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343($title$delegate);
        if (AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343.length() == 0) {
            AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343 = "Do nothing";
        }
        $onSave.invoke(AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343, AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346($selectedCategory$delegate).getFirst(), Long.valueOf(((Color) AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346($selectedCategory$delegate).getSecond()).unbox-impl()));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$357$lambda$356$lambda$355$lambda$354(View $view, Pair $cat, MutableState $selectedCategory$delegate) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        $selectedCategory$delegate.setValue($cat);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$364$lambda$359$lambda$358(View $view, Function0 $onCancel) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        $onCancel.invoke();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$364$lambda$360(ThemeOption $currentTheme, FontFamily $currentFont, RowScope $this$TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C1778@92585L108:MainActivity.kt#to5c3");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1385847927, $changed, -1, "com.example.AddTaskCard.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:1778)");
            }
            TextKt.Text--4IGK_g("Cancel", (Modifier) null, $currentTheme.getTextSecondary-0d7_KjU(), 0L, (FontStyle) null, FontWeight.Companion.getMedium(), $currentFont, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 196614, 0, 130970);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$364$lambda$363$lambda$362(Function3 $onSave, MutableState $title$delegate, MutableState $selectedCategory$delegate) {
        String AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343 = AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343($title$delegate);
        if (AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343.length() == 0) {
            AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343 = "Focus Task";
        }
        $onSave.invoke(AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$343, AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346($selectedCategory$delegate).getFirst(), Long.valueOf(((Color) AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$346($selectedCategory$delegate).getSecond()).unbox-impl()));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:143:0x0398  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x03a4  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x03aa  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x03db  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x03f1  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x0467  */
    @androidx.compose.runtime.Composable
    @androidx.compose.runtime.ComposableInferredTarget(scheme = "[androidx.compose.ui.UiComposable[androidx.compose.ui.UiComposable]]")
    /* renamed from: AnimatedScaleBox-gXMAzVA  reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void m0AnimatedScaleBoxgXMAzVA(@org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function0<kotlin.Unit> r35, @org.jetbrains.annotations.Nullable androidx.compose.ui.Modifier r36, boolean r37, @org.jetbrains.annotations.Nullable androidx.compose.ui.graphics.Shape r38, float r39, long r40, @org.jetbrains.annotations.NotNull final kotlin.jvm.functions.Function2<? super androidx.compose.runtime.Composer, ? super java.lang.Integer, kotlin.Unit> r42, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r43, final int r44, final int r45) {
        /*
            Method dump skipped, instructions count: 1157
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.m0AnimatedScaleBoxgXMAzVA(kotlin.jvm.functions.Function0, androidx.compose.ui.Modifier, boolean, androidx.compose.ui.graphics.Shape, float, long, kotlin.jvm.functions.Function2, androidx.compose.runtime.Composer, int, int):void");
    }

    private static final boolean AnimatedScaleBox_gXMAzVA$lambda$371(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final float AnimatedScaleBox_gXMAzVA$lambda$372(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    private static final float AnimatedScaleBox_gXMAzVA$lambda$373(State<Dp> state) {
        return ((Dp) state.getValue()).unbox-impl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AnimatedScaleBox_gXMAzVA$lambda$375$lambda$374(boolean $enabled, State $scale$delegate, GraphicsLayerScope $this$graphicsLayer) {
        Intrinsics.checkNotNullParameter($this$graphicsLayer, "$this$graphicsLayer");
        $this$graphicsLayer.setScaleX(AnimatedScaleBox_gXMAzVA$lambda$372($scale$delegate));
        $this$graphicsLayer.setScaleY(AnimatedScaleBox_gXMAzVA$lambda$372($scale$delegate));
        $this$graphicsLayer.setAlpha($enabled ? 1.0f : 0.5f);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AnimatedScaleBox_gXMAzVA$lambda$377$lambda$376(View $view, Function0 $onClick) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        $onClick.invoke();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:62:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x02a3  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x02cc  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x02ce  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x02ea  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x034e  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0381  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0384  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x039b  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x03a8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0403  */
    /* JADX WARN: Type inference failed for: r2v21 */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void AppControls(@org.jetbrains.annotations.NotNull final com.example.service.TimerManager.TimerState r58, @org.jetbrains.annotations.NotNull final android.content.Context r59, @org.jetbrains.annotations.Nullable kotlin.jvm.functions.Function0<kotlin.Unit> r60, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r61, final int r62, final int r63) {
        /*
            Method dump skipped, instructions count: 1052
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.AppControls(com.example.service.TimerManager$TimerState, android.content.Context, kotlin.jvm.functions.Function0, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AppControls$lambda$398$lambda$384$lambda$383(Context $context) {
        Intent intent = new Intent($context, TimerService.class);
        intent.setAction("ACTION_STOP");
        $context.startService(intent);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x019a  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit AppControls$lambda$398$lambda$386(com.example.ui.theme.ThemeOption r32, androidx.compose.runtime.Composer r33, int r34) {
        /*
            Method dump skipped, instructions count: 416
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.AppControls$lambda$398$lambda$386(com.example.ui.theme.ThemeOption, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AppControls$lambda$398$lambda$389$lambda$388(boolean $isRunning, Context $context, Function0 $onStartAttempt) {
        if ($isRunning) {
            Intent intent = new Intent($context, TimerService.class);
            intent.setAction("ACTION_PAUSE");
            $context.startService(intent);
        } else {
            $onStartAttempt.invoke();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01c2  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit AppControls$lambda$398$lambda$391(com.example.ui.theme.ThemeOption r34, boolean r35, androidx.compose.runtime.Composer r36, int r37) {
        /*
            Method dump skipped, instructions count: 456
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.AppControls$lambda$398$lambda$391(com.example.ui.theme.ThemeOption, boolean, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    private static final boolean AppControls$lambda$398$lambda$392(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AppControls$lambda$398$lambda$395$lambda$394(TimerManager.TimerState $state, Context $context, State $isBreakMode$delegate) {
        boolean isServiceActive = $state != TimerManager.TimerState.STOPPED;
        if (isServiceActive) {
            Intent intent = new Intent($context, TimerService.class);
            intent.setAction("ACTION_TOGGLE_MODE");
            $context.startService(intent);
        } else {
            boolean newIsBreak = true ^ AppControls$lambda$398$lambda$392($isBreakMode$delegate);
            TimerManager.INSTANCE.setBreakMode(newIsBreak);
            TimerManager.INSTANCE.updateState(TimerManager.TimerState.STOPPED);
            TimerManager timerManager = TimerManager.INSTANCE;
            TimerManager timerManager2 = TimerManager.INSTANCE;
            timerManager.updateTime(((Number) (newIsBreak ? timerManager2.getBreakTimeSeconds() : timerManager2.getFocusTimeSeconds()).getValue()).intValue());
            TimerManager.setTask$default(TimerManager.INSTANCE, -1, newIsBreak ? "Break Time!" : "Focus Time!", (Long) null, 4, (Object) null);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x019e  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit AppControls$lambda$398$lambda$397(com.example.ui.theme.ThemeOption r32, androidx.compose.runtime.Composer r33, int r34) {
        /*
            Method dump skipped, instructions count: 420
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.AppControls$lambda$398$lambda$397(com.example.ui.theme.ThemeOption, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0148  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x03bb  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0390 A[EDGE_INSN: B:76:0x0390->B:68:0x0390 ?: BREAK  , SYNTHETIC] */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /* renamed from: SlidingTimer-KmRG4DE  reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void m2SlidingTimerKmRG4DE(final int r60, long r61, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r63, final int r64, final int r65) {
        /*
            Method dump skipped, instructions count: 980
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.m2SlidingTimerKmRG4DE(int, long, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ContentTransform SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404(AnimatedContentTransitionScope $this$AnimatedContent) {
        ContentTransform contentTransform;
        Intrinsics.checkNotNullParameter($this$AnimatedContent, "$this$AnimatedContent");
        FiniteAnimationSpec spring$default = AnimationSpecKt.spring$default(0.5f, 200.0f, (Object) null, 4, (Object) null);
        FiniteAnimationSpec spring$default2 = AnimationSpecKt.spring$default(1.0f, 1500.0f, (Object) null, 4, (Object) null);
        FiniteAnimationSpec spring$default3 = AnimationSpecKt.spring$default(0.0f, 200.0f, (Object) null, 5, (Object) null);
        FiniteAnimationSpec spring$default4 = AnimationSpecKt.spring$default(0.0f, 1500.0f, (Object) null, 5, (Object) null);
        if (Intrinsics.compare(((Character) $this$AnimatedContent.getTargetState()).charValue(), ((Character) $this$AnimatedContent.getInitialState()).charValue()) < 0 || ((((Character) $this$AnimatedContent.getInitialState()).charValue() == '0' && ((Character) $this$AnimatedContent.getTargetState()).charValue() == '9') || (((Character) $this$AnimatedContent.getInitialState()).charValue() == '0' && ((Character) $this$AnimatedContent.getTargetState()).charValue() == '5'))) {
            contentTransform = AnimatedContentKt.togetherWith(EnterExitTransitionKt.slideInVertically(spring$default, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda107
                public final Object invoke(Object obj) {
                    return Integer.valueOf(MainActivityKt.SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$400(((Integer) obj).intValue()));
                }
            }).plus(EnterExitTransitionKt.fadeIn$default(spring$default3, 0.0f, 2, (Object) null)), EnterExitTransitionKt.slideOutVertically(spring$default2, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda108
                public final Object invoke(Object obj) {
                    return Integer.valueOf(MainActivityKt.SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$401(((Integer) obj).intValue()));
                }
            }).plus(EnterExitTransitionKt.fadeOut$default(spring$default4, 0.0f, 2, (Object) null)));
        } else {
            contentTransform = AnimatedContentKt.togetherWith(EnterExitTransitionKt.slideInVertically(spring$default, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda109
                public final Object invoke(Object obj) {
                    return Integer.valueOf(MainActivityKt.SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$402(((Integer) obj).intValue()));
                }
            }).plus(EnterExitTransitionKt.fadeIn$default(spring$default3, 0.0f, 2, (Object) null)), EnterExitTransitionKt.slideOutVertically(spring$default2, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda110
                public final Object invoke(Object obj) {
                    return Integer.valueOf(MainActivityKt.SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$403(((Integer) obj).intValue()));
                }
            }).plus(EnterExitTransitionKt.fadeOut$default(spring$default4, 0.0f, 2, (Object) null)));
        }
        return $this$AnimatedContent.using(contentTransform, AnimatedContentKt.SizeTransform$default(false, (Function2) null, 2, (Object) null));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$400(int height) {
        return -height;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$401(int height) {
        return height;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$402(int height) {
        return height;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$405$lambda$404$lambda$403(int height) {
        return -height;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit SlidingTimer_KmRG4DE$lambda$408$lambda$407$lambda$406(long $fontSize, AnimatedContentScope $this$AnimatedContent, char targetDigit, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$AnimatedContent, "$this$AnimatedContent");
        ComposerKt.sourceInformation($composer, "C2008@102711L7,2009@102766L7,2004@102500L307:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(-448888324, $changed, -1, "com.example.SlidingTimer.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:2004)");
        }
        String valueOf = String.valueOf(targetDigit);
        FontWeight black = FontWeight.Companion.getBlack();
        ComposerKt.sourceInformationMarkerStart($composer, 2023513938, "CC:CompositionLocal.kt#9igjgp");
        Object consume = $composer.consume(TypographyKt.getLocalAppFont());
        ComposerKt.sourceInformationMarkerEnd($composer);
        ComposerKt.sourceInformationMarkerStart($composer, 2023513938, "CC:CompositionLocal.kt#9igjgp");
        Object consume2 = $composer.consume(ThemeKt.getLocalAppTheme());
        ComposerKt.sourceInformationMarkerEnd($composer);
        TextKt.Text--4IGK_g(valueOf, (Modifier) null, ((ThemeOption) consume2).getTextPrimary-0d7_KjU(), $fontSize, (FontStyle) null, black, (FontFamily) consume, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 196608, 0, 130962);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x02e1  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x02f1  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x03de  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x04ba  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x04c3  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x04ed  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x05cd  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0738  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x073b  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x082b  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0837  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x083d  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x0a0c  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /* renamed from: TimerDisplay-egy_3UM  reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void m3TimerDisplayegy_3UM(final int r90, final int r91, @org.jetbrains.annotations.NotNull final com.example.service.TimerManager.TimerState r92, final boolean r93, @org.jetbrains.annotations.NotNull final java.lang.String r94, @org.jetbrains.annotations.Nullable com.example.data.TaskItem r95, @org.jetbrains.annotations.NotNull final android.content.Context r96, float r97, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r98, final int r99, final int r100) {
        /*
            Method dump skipped, instructions count: 2608
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.m3TimerDisplayegy_3UM(int, int, com.example.service.TimerManager$TimerState, boolean, java.lang.String, com.example.data.TaskItem, android.content.Context, float, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String TimerDisplay_egy_3UM$lambda$411(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x03c3  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x03cf  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x03d5  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x041c  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x047b  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x048b  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0533  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01ed  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0202  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0232  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x023c  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x027f  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0284  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x028a  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x028d  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0297  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0299  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02b8  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02bb  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x02c6  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0334  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x033d  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0354  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x035c  */
    /* JADX WARN: Type inference failed for: r0v37 */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit TimerDisplay_egy_3UM$lambda$432$lambda$424(final com.example.ui.theme.ThemeOption r88, final boolean r89, final androidx.compose.animation.core.Animatable r90, int r91, final com.example.service.TimerManager.TimerState r92, final android.view.View r93, final android.content.Context r94, androidx.compose.foundation.layout.BoxWithConstraintsScope r95, androidx.compose.runtime.Composer r96, int r97) {
        /*
            Method dump skipped, instructions count: 1337
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.TimerDisplay_egy_3UM$lambda$432$lambda$424(com.example.ui.theme.ThemeOption, boolean, androidx.compose.animation.core.Animatable, int, com.example.service.TimerManager$TimerState, android.view.View, android.content.Context, androidx.compose.foundation.layout.BoxWithConstraintsScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TimerDisplay_egy_3UM$lambda$432$lambda$424$lambda$418$lambda$417(ThemeOption $currentTheme, float $dynamicCircleSize, boolean $isBreakMode, Animatable $animatedProgress, DrawScope $this$Canvas) {
        Intrinsics.checkNotNullParameter($this$Canvas, "$this$Canvas");
        DrawScope.drawArc-yD3GUKo$default($this$Canvas, Color.copy-wmQWz5c$default($currentTheme.getCardBorder-0d7_KjU(), 0.5f, 0.0f, 0.0f, 0.0f, 14, (Object) null), -90.0f, 360.0f, false, 0L, 0L, 0.0f, new Stroke($this$Canvas.toPx-0680j_4(Dp.compareTo-0680j_4($dynamicCircleSize, Dp.constructor-impl((float) 200)) < 0 ? Dp.constructor-impl(4) : Dp.constructor-impl(8)), 0.0f, StrokeCap.Companion.getRound-KaPHkGw(), 0, (PathEffect) null, 26, (DefaultConstructorMarker) null), (ColorFilter) null, 0, 880, (Object) null);
        DrawScope.drawArc-yD3GUKo$default($this$Canvas, $isBreakMode ? $currentTheme.getSecondary-0d7_KjU() : $currentTheme.getPrimary-0d7_KjU(), -90.0f, 360.0f * ((Number) $animatedProgress.getValue()).floatValue(), false, 0L, 0L, 0.0f, new Stroke($this$Canvas.toPx-0680j_4(Dp.compareTo-0680j_4($dynamicCircleSize, Dp.constructor-impl((float) 200)) < 0 ? Dp.constructor-impl(4) : Dp.constructor-impl(8)), 0.0f, StrokeCap.Companion.getRound-KaPHkGw(), 0, (PathEffect) null, 26, (DefaultConstructorMarker) null), (ColorFilter) null, 0, 880, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TimerDisplay_egy_3UM$lambda$432$lambda$424$lambda$423$lambda$421$lambda$420(View $view, TimerManager.TimerState $state, Context $context, boolean $isBreakMode) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        boolean isServiceActive = $state != TimerManager.TimerState.STOPPED;
        if (isServiceActive) {
            Intent intent = new Intent($context, TimerService.class);
            intent.setAction("ACTION_TOGGLE_MODE");
            $context.startService(intent);
        } else {
            boolean newIsBreak = !$isBreakMode;
            TimerManager.INSTANCE.setBreakMode(newIsBreak);
            TimerManager.INSTANCE.updateState(TimerManager.TimerState.STOPPED);
            TimerManager timerManager = TimerManager.INSTANCE;
            TimerManager timerManager2 = TimerManager.INSTANCE;
            timerManager.updateTime(((Number) (newIsBreak ? timerManager2.getBreakTimeSeconds() : timerManager2.getFocusTimeSeconds()).getValue()).intValue());
            TimerManager.setTask$default(TimerManager.INSTANCE, -1, newIsBreak ? "Break Time!" : "Focus Time!", (Long) null, 4, (Object) null);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit TimerDisplay_egy_3UM$lambda$432$lambda$426$lambda$425(View $view) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        TimerManager.setTask$default(TimerManager.INSTANCE, -1, "Focus Time!", (Long) null, 4, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0261  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x03ee  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit TimerDisplay_egy_3UM$lambda$432$lambda$429(com.example.data.TaskItem r73, com.example.ui.theme.ThemeOption r74, androidx.compose.foundation.layout.ColumnScope r75, androidx.compose.runtime.Composer r76, int r77) {
        /*
            Method dump skipped, instructions count: 1012
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.TimerDisplay_egy_3UM$lambda$432$lambda$429(com.example.data.TaskItem, com.example.ui.theme.ThemeOption, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final void AnimatedQuoteCard(@NotNull final String quote, final boolean isBreak, @Nullable Composer $composer, final int $changed) {
        FontFamily currentFont;
        Object obj;
        Intrinsics.checkNotNullParameter(quote, "quote");
        Composer $composer2 = $composer.startRestartGroup(-1284705171);
        ComposerKt.sourceInformation($composer2, "C(AnimatedQuoteCard)P(1)2227@112942L7,2228@112985L7,2233@113266L43,2234@113351L266,2243@113659L268,2255@114012L69,2256@114116L38,2265@114462L87,2274@114735L3164,2253@113933L3966:MainActivity.kt#to5c3");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changed(quote) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer2.changed(isBreak) ? 32 : 16;
        }
        if (($dirty & 19) == 18 && $composer2.getSkipping()) {
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1284705171, $dirty, -1, "com.example.AnimatedQuoteCard (MainActivity.kt:2226)");
            }
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume = $composer2.consume(ThemeKt.getLocalAppTheme());
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ThemeOption currentTheme = (ThemeOption) consume;
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume2 = $composer2.consume(TypographyKt.getLocalAppFont());
            ComposerKt.sourceInformationMarkerEnd($composer2);
            FontFamily currentFont2 = (FontFamily) consume2;
            final long quoteColor = isBreak ? currentTheme.getSecondary-0d7_KjU() : currentTheme.getTextPrimary-0d7_KjU();
            final long accentColor = isBreak ? currentTheme.getSecondary-0d7_KjU() : currentTheme.getPrimary-0d7_KjU();
            InfiniteTransition infiniteTransition = InfiniteTransitionKt.rememberInfiniteTransition("pulse", $composer2, 6, 0);
            State pulseAlpha$delegate = InfiniteTransitionKt.animateFloat(infiniteTransition, 0.4f, 0.7f, AnimationSpecKt.infiniteRepeatable-9IiC70o$default(AnimationSpecKt.tween$default(4000, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), RepeatMode.Reverse, 0L, 4, (Object) null), "alpha", $composer2, (InfiniteRepeatableSpec.$stable << 9) | InfiniteTransition.$stable | 25008, 0);
            final State pulseScale$delegate = InfiniteTransitionKt.animateFloat(infiniteTransition, 0.98f, 1.02f, AnimationSpecKt.infiniteRepeatable-9IiC70o$default(AnimationSpecKt.tween$default(4000, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), RepeatMode.Reverse, 0L, 4, (Object) null), "scale", $composer2, InfiniteTransition.$stable | 25008 | (InfiniteRepeatableSpec.$stable << 9), 0);
            Shape shape = RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(28));
            CardColors cardColors = CardDefaults.INSTANCE.cardColors-ro_MJ88(Color.copy-wmQWz5c$default(currentTheme.getSurface-0d7_KjU(), 0.85f, 0.0f, 0.0f, 0.0f, 14, (Object) null), 0L, 0L, 0L, $composer2, CardDefaults.$stable << 12, 14);
            CardElevation cardElevation = CardDefaults.INSTANCE.cardElevation-aqJV_2Y(Dp.constructor-impl(0), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, $composer2, (CardDefaults.$stable << 18) | 6, 62);
            Modifier modifier = ShadowKt.shadow-s4CzXII$default(PaddingKt.padding-qDBjuR0$default(SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null), 0.0f, Dp.constructor-impl(12), 0.0f, Dp.constructor-impl(24), 5, (Object) null), Dp.constructor-impl(6), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(28)), false, 0L, currentTheme.getShadowColor-0d7_KjU(), 12, (Object) null);
            ComposerKt.sourceInformationMarkerStart($composer2, -1946003260, "CC(remember):MainActivity.kt#9igjgp");
            boolean changed = $composer2.changed(pulseScale$delegate);
            Object rememberedValue = $composer2.rememberedValue();
            if (changed || rememberedValue == Composer.Companion.getEmpty()) {
                currentFont = currentFont2;
                obj = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda98
                    public final Object invoke(Object obj2) {
                        return MainActivityKt.AnimatedQuoteCard$lambda$437$lambda$436(pulseScale$delegate, (GraphicsLayerScope) obj2);
                    }
                };
                $composer2.updateRememberedValue(obj);
            } else {
                currentFont = currentFont2;
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer2);
            Shape shape2 = shape;
            final FontFamily currentFont3 = currentFont;
            CardKt.Card(BorderKt.border-xT4_qwU(GraphicsLayerModifierKt.graphicsLayer(modifier, (Function1) obj), Dp.constructor-impl((float) 1.5d), Color.copy-wmQWz5c$default(accentColor, AnimatedQuoteCard$lambda$434(pulseAlpha$delegate), 0.0f, 0.0f, 0.0f, 14, (Object) null), RoundedCornerShapeKt.RoundedCornerShape-0680j_4(Dp.constructor-impl(28))), shape2, cardColors, cardElevation, (BorderStroke) null, ComposableLambdaKt.rememberComposableLambda(-878029765, true, new Function3() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda99
                public final Object invoke(Object obj2, Object obj3, Object obj4) {
                    return MainActivityKt.AnimatedQuoteCard$lambda$447(accentColor, currentFont3, quote, quoteColor, (ColumnScope) obj2, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer2, 54), $composer2, 196608, 16);
            $composer2 = $composer2;
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda100
                public final Object invoke(Object obj2, Object obj3) {
                    return MainActivityKt.AnimatedQuoteCard$lambda$448(quote, isBreak, $changed, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    private static final float AnimatedQuoteCard$lambda$434(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    private static final float AnimatedQuoteCard$lambda$435(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AnimatedQuoteCard$lambda$437$lambda$436(State $pulseScale$delegate, GraphicsLayerScope $this$graphicsLayer) {
        Intrinsics.checkNotNullParameter($this$graphicsLayer, "$this$graphicsLayer");
        $this$graphicsLayer.setScaleX(AnimatedQuoteCard$lambda$435($pulseScale$delegate));
        $this$graphicsLayer.setScaleY(AnimatedQuoteCard$lambda$435($pulseScale$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0259  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0331  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x040c  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit AnimatedQuoteCard$lambda$447(final long r70, final androidx.compose.ui.text.font.FontFamily r72, java.lang.String r73, final long r74, androidx.compose.foundation.layout.ColumnScope r76, androidx.compose.runtime.Composer r77, int r78) {
        /*
            Method dump skipped, instructions count: 1042
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.AnimatedQuoteCard$lambda$447(long, androidx.compose.ui.text.font.FontFamily, java.lang.String, long, androidx.compose.foundation.layout.ColumnScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ContentTransform AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$441$lambda$440(AnimatedContentTransitionScope $this$AnimatedContent) {
        Intrinsics.checkNotNullParameter($this$AnimatedContent, "$this$AnimatedContent");
        return AnimatedContentKt.togetherWith(EnterExitTransitionKt.fadeIn$default(AnimationSpecKt.tween$default(600, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.slideInVertically(AnimationSpecKt.tween$default(600, 0, (Easing) null, 6, (Object) null), new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda164
            public final Object invoke(Object obj) {
                return Integer.valueOf(MainActivityKt.AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$441$lambda$440$lambda$438(((Integer) obj).intValue()));
            }
        })), EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.tween$default(400, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.slideOutVertically(AnimationSpecKt.tween$default(400, 0, (Easing) null, 6, (Object) null), new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda165
            public final Object invoke(Object obj) {
                return Integer.valueOf(MainActivityKt.AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$441$lambda$440$lambda$439(((Integer) obj).intValue()));
            }
        })));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$441$lambda$440$lambda$438(int it) {
        return 20;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final int AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$441$lambda$440$lambda$439(int it) {
        return -20;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit AnimatedQuoteCard$lambda$447$lambda$446$lambda$445$lambda$444(long $quoteColor, FontFamily $currentFont, long $accentColor, AnimatedContentScope $this$AnimatedContent, String targetQuote, Composer $composer, int $changed) {
        Function0 function0;
        Intrinsics.checkNotNullParameter($this$AnimatedContent, "$this$AnimatedContent");
        Intrinsics.checkNotNullParameter(targetQuote, "targetQuote");
        ComposerKt.sourceInformation($composer, "C2309@116169L1249:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(-431559979, $changed, -1, "com.example.AnimatedQuoteCard.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:2309)");
        }
        Alignment.Horizontal centerHorizontally = Alignment.Companion.getCenterHorizontally();
        Modifier fillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.Companion, 0.0f, 1, (Object) null);
        ComposerKt.sourceInformationMarkerStart($composer, -483455358, "CC(Column)P(2,3,1)86@4330L61,87@4396L133:Column.kt#2w3rfo");
        MeasurePolicy columnMeasurePolicy = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), centerHorizontally, $composer, ((390 >> 3) & 14) | ((390 >> 3) & 112));
        ComposerKt.sourceInformationMarkerStart($composer, -1323940314, "CC(Layout)P(!1,2)79@3208L23,82@3359L411:Layout.kt#80mrfh");
        int currentCompositeKeyHash = ComposablesKt.getCurrentCompositeKeyHash($composer, 0);
        CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
        Modifier materializeModifier = ComposedModifierKt.materializeModifier($composer, fillMaxWidth$default);
        Function0 constructor = ComposeUiNode.Companion.getConstructor();
        int i = ((((390 << 3) & 112) << 6) & 896) | 6;
        ComposerKt.sourceInformationMarkerStart($composer, -692256719, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
        if (!($composer.getApplier() instanceof Applier)) {
            ComposablesKt.invalidApplier();
        }
        $composer.startReusableNode();
        if ($composer.getInserting()) {
            function0 = constructor;
            $composer.createNode(function0);
        } else {
            function0 = constructor;
            $composer.useNode();
        }
        Composer composer = Updater.constructor-impl($composer);
        Updater.set-impl(composer, columnMeasurePolicy, ComposeUiNode.Companion.getSetMeasurePolicy());
        Updater.set-impl(composer, currentCompositionLocalMap, ComposeUiNode.Companion.getSetResolvedCompositionLocals());
        Function2 setCompositeKeyHash = ComposeUiNode.Companion.getSetCompositeKeyHash();
        if (composer.getInserting() || !Intrinsics.areEqual(composer.rememberedValue(), Integer.valueOf(currentCompositeKeyHash))) {
            composer.updateRememberedValue(Integer.valueOf(currentCompositeKeyHash));
            composer.apply(Integer.valueOf(currentCompositeKeyHash), setCompositeKeyHash);
        }
        Updater.set-impl(composer, materializeModifier, ComposeUiNode.Companion.getSetModifier());
        int i2 = (i >> 6) & 14;
        ComposerKt.sourceInformationMarkerStart($composer, -384784025, "C88@4444L9:Column.kt#2w3rfo");
        ColumnScope columnScope = ColumnScopeInstance.INSTANCE;
        int i3 = ((390 >> 6) & 112) | 6;
        ComposerKt.sourceInformationMarkerStart($composer, -1103330632, "C2315@116502L8,2321@116895L8,2313@116360L645,2324@117030L41,2326@117147L249:MainActivity.kt#to5c3");
        String str = targetQuote;
        if (str.length() == 0) {
            str = "Stay present and fully engaged.";
        }
        long scaledSp = ScaledSpKt.getScaledSp(18, $composer, 6);
        int i4 = FontStyle.Companion.getItalic-_-LCdwA();
        TextKt.Text--4IGK_g(str, PaddingKt.padding-VpY3zN4$default(Modifier.Companion, Dp.constructor-impl(4), 0.0f, 2, (Object) null), $quoteColor, scaledSp, FontStyle.box-impl(i4), FontWeight.Companion.getMedium(), $currentFont, 0L, (TextDecoration) null, TextAlign.box-impl(TextAlign.Companion.getCenter-e0LSkKk()), ScaledSpKt.getScaledSp(28, $composer, 6), 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 196656, 0, 129408);
        SpacerKt.Spacer(SizeKt.height-3ABfNKs(Modifier.Companion, Dp.constructor-impl(16)), $composer, 6);
        BoxKt.Box(BackgroundKt.background-bw27NRU$default(ClipKt.clip(SizeKt.size-3ABfNKs(Modifier.Companion, Dp.constructor-impl(6)), RoundedCornerShapeKt.getCircleShape()), Color.copy-wmQWz5c$default($accentColor, 0.6f, 0.0f, 0.0f, 0.0f, 14, (Object) null), (Shape) null, 2, (Object) null), $composer, 0);
        ComposerKt.sourceInformationMarkerEnd($composer);
        ComposerKt.sourceInformationMarkerEnd($composer);
        $composer.endNode();
        ComposerKt.sourceInformationMarkerEnd($composer);
        ComposerKt.sourceInformationMarkerEnd($composer);
        ComposerKt.sourceInformationMarkerEnd($composer);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final void SeriousFullscreenOverlay(@NotNull final String quote, @NotNull final String taskName, final int timeRemaining, @NotNull final Function0<Unit> function0, @Nullable Composer $composer, final int $changed) {
        int i;
        Object obj;
        Object obj2;
        Composer $composer2;
        Object obj3;
        Object obj4;
        Intrinsics.checkNotNullParameter(quote, "quote");
        Intrinsics.checkNotNullParameter(taskName, "taskName");
        Intrinsics.checkNotNullParameter(function0, "onDismiss");
        Composer $composer3 = $composer.startRestartGroup(1248933701);
        ComposerKt.sourceInformation($composer3, "C(SeriousFullscreenOverlay)P(1,2,3)2358@118100L7,2360@118214L7,2363@118340L80,2363@118312L108,2368@118533L1692,2368@118510L1715,2409@120256L49,2411@120351L289,2421@120682L272,2436@121130L39,2438@121219L2,2439@121273L13440,2431@120960L13753:MainActivity.kt#to5c3");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(quote) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changed(taskName) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            i = timeRemaining;
            $dirty |= $composer3.changed(i) ? 256 : 128;
        } else {
            i = timeRemaining;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(function0) ? 2048 : 1024;
        }
        if (($dirty & 1171) == 1170 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1248933701, $dirty, -1, "com.example.SeriousFullscreenOverlay (MainActivity.kt:2357)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume = $composer3.consume(AndroidCompositionLocals_androidKt.getLocalContext());
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final Context context = (Context) consume;
            final Activity activity = context instanceof Activity ? (Activity) context : null;
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC:CompositionLocal.kt#9igjgp");
            Object consume2 = $composer3.consume(AndroidCompositionLocals_androidKt.getLocalView());
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final View view = (View) consume2;
            ComposerKt.sourceInformationMarkerStart($composer3, 1929425141, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue = $composer3.rememberedValue();
            if (rememberedValue == Composer.Companion.getEmpty()) {
                obj = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda101
                    public final Object invoke() {
                        Unit unit;
                        unit = Unit.INSTANCE;
                        return unit;
                    }
                };
                $composer3.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            BackHandlerKt.BackHandler(true, (Function0) obj, $composer3, 54, 0);
            Unit unit = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart($composer3, 1929432929, "CC(remember):MainActivity.kt#9igjgp");
            boolean changedInstance = $composer3.changedInstance(activity) | $composer3.changedInstance(context);
            Object rememberedValue2 = $composer3.rememberedValue();
            if (changedInstance || rememberedValue2 == Composer.Companion.getEmpty()) {
                obj2 = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda102
                    public final Object invoke(Object obj5) {
                        return MainActivityKt.SeriousFullscreenOverlay$lambda$453$lambda$452(activity, context, (DisposableEffectScope) obj5);
                    }
                };
                $composer3.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            EffectsKt.DisposableEffect(unit, (Function1) obj2, $composer3, 6);
            final InfiniteTransition infiniteTransition = InfiniteTransitionKt.rememberInfiniteTransition("seriousness", $composer3, 6, 0);
            State glowColor$delegate = TransitionKt.animateColor-DTcfvLk(infiniteTransition, ColorKt.Color(4280159758L), ColorKt.Color(4279174405L), AnimationSpecKt.infiniteRepeatable-9IiC70o$default(AnimationSpecKt.tween$default(4000, 0, EasingKt.getLinearEasing(), 2, (Object) null), RepeatMode.Reverse, 0L, 4, (Object) null), "glowColor", $composer3, InfiniteTransition.$stable | 25008 | (InfiniteRepeatableSpec.$stable << 9), 0);
            final State scaleGlow$delegate = InfiniteTransitionKt.animateFloat(infiniteTransition, 0.98f, 1.02f, AnimationSpecKt.infiniteRepeatable-9IiC70o$default(AnimationSpecKt.tween$default(4000, 0, EasingKt.getFastOutSlowInEasing(), 2, (Object) null), RepeatMode.Reverse, 0L, 4, (Object) null), "scaleGlow", $composer3, InfiniteTransition.$stable | 25008 | (InfiniteRepeatableSpec.$stable << 9), 0);
            $composer2 = $composer3;
            Modifier modifier = BackgroundKt.background-bw27NRU$default(SizeKt.fillMaxSize$default(Modifier.Companion, 0.0f, 1, (Object) null), SeriousFullscreenOverlay$lambda$454(glowColor$delegate), (Shape) null, 2, (Object) null);
            ComposerKt.sourceInformationMarkerStart($composer2, 1929514380, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue3 = $composer2.rememberedValue();
            if (rememberedValue3 == Composer.Companion.getEmpty()) {
                obj3 = InteractionSourceKt.MutableInteractionSource();
                $composer2.updateRememberedValue(obj3);
            } else {
                obj3 = rememberedValue3;
            }
            MutableInteractionSource mutableInteractionSource = (MutableInteractionSource) obj3;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1929517191, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue4 = $composer2.rememberedValue();
            if (rememberedValue4 == Composer.Companion.getEmpty()) {
                obj4 = new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda103
                    public final Object invoke() {
                        Unit unit2;
                        unit2 = Unit.INSTANCE;
                        return unit2;
                    }
                };
                $composer2.updateRememberedValue(obj4);
            } else {
                obj4 = rememberedValue4;
            }
            ComposerKt.sourceInformationMarkerEnd($composer2);
            final int i2 = i;
            BoxWithConstraintsKt.BoxWithConstraints(ClickableKt.clickable-O2vRcR0$default(modifier, mutableInteractionSource, (Indication) null, false, (String) null, (Role) null, (Function0) obj4, 28, (Object) null), (Alignment) null, false, ComposableLambdaKt.rememberComposableLambda(-51656529, true, new Function3() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda104
                public final Object invoke(Object obj5, Object obj6, Object obj7) {
                    return MainActivityKt.SeriousFullscreenOverlay$lambda$479(i2, taskName, quote, scaleGlow$delegate, infiniteTransition, view, function0, (BoxWithConstraintsScope) obj5, (Composer) obj6, ((Integer) obj7).intValue());
                }
            }, $composer2, 54), $composer2, 3072, 6);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda105
                public final Object invoke(Object obj5, Object obj6) {
                    return MainActivityKt.SeriousFullscreenOverlay$lambda$480(quote, taskName, timeRemaining, function0, $changed, (Composer) obj5, ((Integer) obj6).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final DisposableEffectResult SeriousFullscreenOverlay$lambda$453$lambda$452(Activity $activity, Context $context, DisposableEffectScope $this$DisposableEffect) {
        Intrinsics.checkNotNullParameter($this$DisposableEffect, "$this$DisposableEffect");
        if ($activity != null) {
            try {
                $activity.startLockTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Object systemService = $context.getSystemService("notification");
        NotificationManager notificationManager = systemService instanceof NotificationManager ? (NotificationManager) systemService : null;
        Ref.IntRef originalInterruptionFilter = new Ref.IntRef();
        originalInterruptionFilter.element = -1;
        if (notificationManager != null) {
            try {
                if (notificationManager.isNotificationPolicyAccessGranted()) {
                    originalInterruptionFilter.element = notificationManager.getCurrentInterruptionFilter();
                    notificationManager.setInterruptionFilter(3);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return new MainActivityKt$SeriousFullscreenOverlay$lambda$453$lambda$452$.inlined.onDispose.1($activity, notificationManager, originalInterruptionFilter);
    }

    private static final long SeriousFullscreenOverlay$lambda$454(State<Color> state) {
        return ((Color) state.getValue()).unbox-impl();
    }

    private static final float SeriousFullscreenOverlay$lambda$455(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:100:0x06fd  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0713  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x07f9  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0806  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x098d  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0999  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x099f  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x09d2  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x09e8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0a52  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0a55  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0a5b  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0a6f  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0a7a  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0ca4  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0cb0  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0cb6  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x0e53  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0f11  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0f25  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0fa3  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x1229  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x1235  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x123b  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x126c  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x1282  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x1407  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x1495  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0283  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0289  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0398  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x03a4  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x03aa  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x03dd  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x03f3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x057f  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0626  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x063a  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x06b8  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x06c4  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x06ca  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit SeriousFullscreenOverlay$lambda$479(int r150, java.lang.String r151, java.lang.String r152, final androidx.compose.runtime.State r153, androidx.compose.animation.core.InfiniteTransition r154, final android.view.View r155, final kotlin.jvm.functions.Function0 r156, androidx.compose.foundation.layout.BoxWithConstraintsScope r157, androidx.compose.runtime.Composer r158, int r159) {
        /*
            Method dump skipped, instructions count: 5275
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.SeriousFullscreenOverlay$lambda$479(int, java.lang.String, java.lang.String, androidx.compose.runtime.State, androidx.compose.animation.core.InfiniteTransition, android.view.View, kotlin.jvm.functions.Function0, androidx.compose.foundation.layout.BoxWithConstraintsScope, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final ContentTransform SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$467$lambda$462$lambda$461(AnimatedContentTransitionScope $this$AnimatedContent) {
        Intrinsics.checkNotNullParameter($this$AnimatedContent, "$this$AnimatedContent");
        return AnimatedContentKt.togetherWith(EnterExitTransitionKt.fadeIn$default(AnimationSpecKt.tween$default(800, 0, (Easing) null, 6, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.scaleIn-L8ZKh-E$default((FiniteAnimationSpec) null, 0.0f, 0L, 7, (Object) null)), EnterExitTransitionKt.fadeOut$default(AnimationSpecKt.tween$default(500, 0, (Easing) null, 6, (Object) null), 0.0f, 2, (Object) null).plus(EnterExitTransitionKt.scaleOut-L8ZKh-E$default((FiniteAnimationSpec) null, 0.0f, 0L, 7, (Object) null)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @Composable
    public static final Unit SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$467$lambda$466(final State $scaleGlow$delegate, AnimatedContentScope $this$AnimatedContent, String targetQuote, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter($this$AnimatedContent, "$this$AnimatedContent");
        Intrinsics.checkNotNullParameter(targetQuote, "targetQuote");
        ComposerKt.sourceInformation($composer, "C2536@125471L8,2538@125572L13,2541@125796L8,2542@125864L121,2533@125286L721:MainActivity.kt#to5c3");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(632281218, $changed, -1, "com.example.SeriousFullscreenOverlay.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:2533)");
        }
        String str = targetQuote;
        if (str.length() == 0) {
            str = "Stay present and fully engaged.";
        }
        String str2 = str;
        long Color = ColorKt.Color(4294962158L);
        long scaledSp = ScaledSpKt.getScaledSp(24, $composer, 6);
        FontWeight bold = FontWeight.Companion.getBold();
        FontFamily appFontFamily = TypographyKt.getAppFontFamily($composer, 0);
        int i = FontStyle.Companion.getItalic-_-LCdwA();
        int i2 = TextAlign.Companion.getCenter-e0LSkKk();
        long scaledSp2 = ScaledSpKt.getScaledSp(36, $composer, 6);
        Modifier modifier = Modifier.Companion;
        ComposerKt.sourceInformationMarkerStart($composer, 2031881563, "CC(remember):MainActivity.kt#9igjgp");
        boolean changed = $composer.changed($scaleGlow$delegate);
        Object rememberedValue = $composer.rememberedValue();
        if (changed || rememberedValue == Composer.Companion.getEmpty()) {
            rememberedValue = new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda42
                public final Object invoke(Object obj) {
                    return MainActivityKt.SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$467$lambda$466$lambda$465$lambda$464($scaleGlow$delegate, (GraphicsLayerScope) obj);
                }
            };
            $composer.updateRememberedValue(rememberedValue);
        }
        ComposerKt.sourceInformationMarkerEnd($composer);
        TextKt.Text--4IGK_g(str2, GraphicsLayerModifierKt.graphicsLayer(modifier, (Function1) rememberedValue), Color, scaledSp, FontStyle.box-impl(i), bold, appFontFamily, 0L, (TextDecoration) null, TextAlign.box-impl(i2), scaledSp2, 0, false, 0, 0, (Function1) null, (TextStyle) null, $composer, 196992, 0, 129408);
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$467$lambda$466$lambda$465$lambda$464(State $scaleGlow$delegate, GraphicsLayerScope $this$graphicsLayer) {
        Intrinsics.checkNotNullParameter($this$graphicsLayer, "$this$graphicsLayer");
        $this$graphicsLayer.setScaleX(SeriousFullscreenOverlay$lambda$455($scaleGlow$delegate));
        $this$graphicsLayer.setScaleY(SeriousFullscreenOverlay$lambda$455($scaleGlow$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final float SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$477$lambda$471$lambda$470$lambda$469(float $progressToExit) {
        return $progressToExit;
    }

    private static final float SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$477$lambda$472(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit SeriousFullscreenOverlay$lambda$479$lambda$478$lambda$477$lambda$476$lambda$475$lambda$474(View $view, Function0 $onDismiss) {
        try {
            $view.performHapticFeedback(3);
        } catch (Exception e) {
        }
        $onDismiss.invoke();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x0727  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0ade  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0aea  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0af0  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0b8b  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0b98  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0bdf  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0bf0  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0c57  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0c64  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0d20  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0d2c  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0d32  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0d63  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0d79 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0f8e  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x0fdc  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0fe6  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x10a6  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x111d  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x115d  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0377  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0383  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0387  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x03cc  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0421  */
    @androidx.compose.runtime.ComposableTarget(applier = "androidx.compose.ui.UiComposable")
    @androidx.compose.runtime.Composable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void AlarmRingingLockScreen(@org.jetbrains.annotations.NotNull java.lang.String r157, int r158, @org.jetbrains.annotations.Nullable androidx.compose.runtime.Composer r159, final int r160) {
        /*
            Method dump skipped, instructions count: 4468
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivityKt.AlarmRingingLockScreen(java.lang.String, int, androidx.compose.runtime.Composer, int):void");
    }

    private static final int AlarmRingingLockScreen$lambda$482(MutableState<Integer> mutableState) {
        return ((Number) ((State) mutableState).getValue()).intValue();
    }

    private static final void AlarmRingingLockScreen$lambda$483(MutableState<Integer> mutableState, int i) {
        mutableState.setValue(Integer.valueOf(i));
    }

    private static final boolean AlarmRingingLockScreen$lambda$485(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void AlarmRingingLockScreen$lambda$486(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final String AlarmRingingLockScreen$lambda$488(MutableState<String> mutableState) {
        return (String) ((State) mutableState).getValue();
    }

    private static final boolean AlarmRingingLockScreen$lambda$491(MutableState<Boolean> mutableState) {
        return ((Boolean) ((State) mutableState).getValue()).booleanValue();
    }

    private static final void AlarmRingingLockScreen$lambda$492(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final DisposableEffectResult AlarmRingingLockScreen$lambda$500$lambda$499(final Context $context, int $targetCount, final MutableState $isFinished$delegate, final MutableState $currentCount$delegate, final MutableState $sensorMessage$delegate, final MutableState $shakeWarning$delegate, DisposableEffectScope $this$DisposableEffect) {
        Intrinsics.checkNotNullParameter($this$DisposableEffect, "$this$DisposableEffect");
        SquatSensorService sensorService = new SquatSensorService($context);
        SquatSensorService.startTracking$default(sensorService, $targetCount, new Function0() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda93
            public final Object invoke() {
                return MainActivityKt.AlarmRingingLockScreen$lambda$500$lambda$499$lambda$495($context, $isFinished$delegate);
            }
        }, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda94
            public final Object invoke(Object obj) {
                return MainActivityKt.AlarmRingingLockScreen$lambda$500$lambda$499$lambda$496($currentCount$delegate, $sensorMessage$delegate, ((Integer) obj).intValue());
            }
        }, new Function1() { // from class: com.example.MainActivityKt$$ExternalSyntheticLambda95
            public final Object invoke(Object obj) {
                return MainActivityKt.AlarmRingingLockScreen$lambda$500$lambda$499$lambda$497($shakeWarning$delegate, $sensorMessage$delegate, ((Boolean) obj).booleanValue());
            }
        }, (Function2) null, 16, (Object) null);
        return new MainActivityKt$AlarmRingingLockScreen$lambda$500$lambda$499$.inlined.onDispose.1(sensorService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AlarmRingingLockScreen$lambda$500$lambda$499$lambda$495(Context $context, MutableState $isFinished$delegate) {
        SoundPlayer.INSTANCE.stopContinuousAlarm();
        AlarmReceiver.Companion.dismissNotification($context);
        AlarmRingingLockScreen$lambda$492($isFinished$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AlarmRingingLockScreen$lambda$500$lambda$499$lambda$496(MutableState $currentCount$delegate, MutableState $sensorMessage$delegate, int count) {
        AlarmRingingLockScreen$lambda$483($currentCount$delegate, count);
        $sensorMessage$delegate.setValue("Awesome! Perfect squat registered.");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AlarmRingingLockScreen$lambda$500$lambda$499$lambda$497(MutableState $shakeWarning$delegate, MutableState $sensorMessage$delegate, boolean isShaking) {
        AlarmRingingLockScreen$lambda$486($shakeWarning$delegate, isShaking);
        if (isShaking) {
            $sensorMessage$delegate.setValue("Locked: Please stop shaking the phone!");
        } else {
            $sensorMessage$delegate.setValue("Standing stable. Keep performing smooth physical squats.");
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AlarmRingingLockScreen$lambda$518$lambda$517$lambda$504$lambda$503(Context $context) {
        Window window;
        try {
            Object systemService = $context.getSystemService("vibrator");
            Vibrator view = systemService instanceof Vibrator ? (Vibrator) systemService : null;
            if (view != null) {
                view.vibrate(100L);
            }
        } catch (Exception e) {
        }
        AlarmState.INSTANCE.setAlarmRinging(false);
        AlarmReceiver.Companion.dismissNotification($context);
        Activity activity = $context instanceof Activity ? (Activity) $context : null;
        try {
            Intent launchIntent = new Intent($context, MainActivity.class);
            launchIntent.addFlags(268468224);
            $context.startActivity(launchIntent);
        } catch (Exception e2) {
            Log.e("SquatDebug", "Failed to launch main activity", e2);
        }
        if (Build.VERSION.SDK_INT >= 27) {
            if (activity != null) {
                activity.setShowWhenLocked(false);
            }
            if (activity != null) {
                activity.setTurnScreenOn(false);
            }
        }
        if (activity != null && (window = activity.getWindow()) != null) {
            window.clearFlags(6815872);
        }
        if (activity != null) {
            activity.finish();
        }
        return Unit.INSTANCE;
    }

    private static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$505(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$513$lambda$507(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$513$lambda$509$lambda$508() {
        return 1.0f;
    }

    private static final float AlarmRingingLockScreen$lambda$518$lambda$517$lambda$514(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit AlarmRingingLockScreen$lambda$518$lambda$517$lambda$516$lambda$515(MutableState $shakeWarning$delegate, State $waveOffset$delegate, DrawScope $this$Canvas) {
        long Color;
        int i;
        Object obj;
        float f;
        Intrinsics.checkNotNullParameter($this$Canvas, "$this$Canvas");
        Path path = AndroidPath_androidKt.Path();
        float width = Size.getWidth-impl($this$Canvas.getSize-NH-jbRc());
        float height = Size.getHeight-impl($this$Canvas.getSize-NH-jbRc());
        float midY = height / 2.0f;
        float amplitude = AlarmRingingLockScreen$lambda$485($shakeWarning$delegate) ? 16.0f : 6.0f;
        path.moveTo(0.0f, midY);
        int x = 0;
        int progressionLastElement = ProgressionUtilKt.getProgressionLastElement(0, (int) width, 6);
        if (0 <= progressionLastElement) {
            while (true) {
                float relativeX = x / width;
                float sineVal = (float) Math.sin((relativeX * 3.5f * 2.0f * 3.14159f) + AlarmRingingLockScreen$lambda$518$lambda$517$lambda$514($waveOffset$delegate));
                float y = (sineVal * amplitude) + midY;
                path.lineTo(x, y);
                if (x == progressionLastElement) {
                    break;
                }
                x += 6;
            }
        }
        if (AlarmRingingLockScreen$lambda$485($shakeWarning$delegate)) {
            Color = ColorKt.Color(4294922834L);
            i = 14;
            obj = null;
            f = 0.8f;
        } else {
            Color = ColorKt.Color(4278249078L);
            i = 14;
            obj = null;
            f = 0.5f;
        }
        DrawScope.drawPath-LG529CI$default($this$Canvas, path, Color.copy-wmQWz5c$default(Color, f, 0.0f, 0.0f, 0.0f, i, obj), 0.0f, new Stroke($this$Canvas.toPx-0680j_4(Dp.constructor-impl(2)), 0.0f, StrokeCap.Companion.getRound-KaPHkGw(), 0, (PathEffect) null, 26, (DefaultConstructorMarker) null), (ColorFilter) null, 0, 52, (Object) null);
        return Unit.INSTANCE;
    }
}
