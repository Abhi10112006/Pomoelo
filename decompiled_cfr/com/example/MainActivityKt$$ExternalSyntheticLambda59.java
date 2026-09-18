/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.runtime.State
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.service.TimerManager$TimerState
 *  kotlin.jvm.functions.Function0
 */
package com.example;

import android.content.Context;
import androidx.compose.runtime.State;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.service.TimerManager;
import kotlin.jvm.functions.Function0;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda59
implements Function0 {
    public final TimerManager.TimerState f$0;
    public final Context f$1;
    public final State f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda59(TimerManager.TimerState timerState, Context context, State state) {
        this.f$0 = timerState;
        this.f$1 = context;
        this.f$2 = state;
    }

    public final Object invoke() {
        return MainActivityKt.AppControls$lambda$398$lambda$395$lambda$394(this.f$0, this.f$1, this.f$2);
    }
}

