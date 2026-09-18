/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.View
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.service.TimerManager$TimerState
 *  kotlin.jvm.functions.Function0
 */
package com.example;

import android.content.Context;
import android.view.View;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.service.TimerManager;
import kotlin.jvm.functions.Function0;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda1
implements Function0 {
    public final View f$0;
    public final TimerManager.TimerState f$1;
    public final Context f$2;
    public final boolean f$3;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda1(View view, TimerManager.TimerState timerState, Context context, boolean bl) {
        this.f$0 = view;
        this.f$1 = timerState;
        this.f$2 = context;
        this.f$3 = bl;
    }

    public final Object invoke() {
        return MainActivityKt.TimerDisplay_egy_3UM$lambda$432$lambda$424$lambda$423$lambda$421$lambda$420(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}

