/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.animation.AnimatedContentScope
 *  androidx.compose.runtime.Composer
 *  androidx.navigation.NavBackStackEntry
 *  androidx.navigation.NavHostController
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.TimerViewModel
 *  kotlin.jvm.functions.Function4
 */
package com.example;

import androidx.compose.animation.AnimatedContentScope;
import androidx.compose.runtime.Composer;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavHostController;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.TimerViewModel;
import kotlin.jvm.functions.Function4;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda88
implements Function4 {
    public final TimerViewModel f$0;
    public final NavHostController f$1;
    public final float f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda88(TimerViewModel timerViewModel, NavHostController navHostController, float f) {
        this.f$0 = timerViewModel;
        this.f$1 = navHostController;
        this.f$2 = f;
    }

    public final Object invoke(Object object, Object object2, Object object3, Object object4) {
        return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77$lambda$71(this.f$0, this.f$1, this.f$2, (AnimatedContentScope)object, (NavBackStackEntry)object2, (Composer)object3, (Integer)object4);
    }
}

