/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.animation.AnimatedVisibilityScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.State
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.TimerViewModel
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import androidx.compose.animation.AnimatedVisibilityScope;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.State;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.TimerViewModel;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda155
implements Function3 {
    public final TimerViewModel f$0;
    public final State f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda155(TimerViewModel timerViewModel, State state) {
        this.f$0 = timerViewModel;
        this.f$1 = state;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$158(this.f$0, this.f$1, (AnimatedVisibilityScope)object, (Composer)object2, (Integer)object3);
    }
}

