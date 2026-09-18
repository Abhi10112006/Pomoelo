/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.foundation.lazy.LazyListScope
 *  androidx.compose.runtime.MutableState
 *  androidx.compose.runtime.State
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.TimerViewModel
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import androidx.compose.foundation.lazy.LazyListScope;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.State;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.TimerViewModel;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda180
implements Function1 {
    public final State f$0;
    public final TimerViewModel f$1;
    public final State f$2;
    public final MutableState f$3;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda180(State state, TimerViewModel timerViewModel, State state2, MutableState mutableState) {
        this.f$0 = state;
        this.f$1 = timerViewModel;
        this.f$2 = state2;
        this.f$3 = mutableState;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$187$lambda$186$lambda$185$lambda$184(this.f$0, this.f$1, this.f$2, this.f$3, (LazyListScope)object);
    }
}

