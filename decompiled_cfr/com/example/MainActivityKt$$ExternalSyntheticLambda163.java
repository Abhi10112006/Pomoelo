/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.foundation.layout.PaddingValues
 *  androidx.navigation.NavGraphBuilder
 *  androidx.navigation.NavHostController
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.TimerViewModel
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import androidx.compose.foundation.layout.PaddingValues;
import androidx.navigation.NavGraphBuilder;
import androidx.navigation.NavHostController;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.TimerViewModel;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda163
implements Function1 {
    public final PaddingValues f$0;
    public final NavHostController f$1;
    public final TimerViewModel f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda163(PaddingValues paddingValues, NavHostController navHostController, TimerViewModel timerViewModel) {
        this.f$0 = paddingValues;
        this.f$1 = navHostController;
        this.f$2 = timerViewModel;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.PomoPalApp$lambda$79$lambda$78$lambda$77(this.f$0, this.f$1, this.f$2, (NavGraphBuilder)object);
    }
}

