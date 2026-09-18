/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.runtime.Composer
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.service.TimerManager$TimerState
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import android.content.Context;
import androidx.compose.runtime.Composer;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.service.TimerManager;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda61
implements Function2 {
    public final TimerManager.TimerState f$0;
    public final Context f$1;
    public final Function0 f$2;
    public final int f$3;
    public final int f$4;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda61(TimerManager.TimerState timerState, Context context, Function0 function0, int n, int n2) {
        this.f$0 = timerState;
        this.f$1 = context;
        this.f$2 = function0;
        this.f$3 = n;
        this.f$4 = n2;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.AppControls$lambda$399(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (Composer)object, (Integer)object2);
    }
}

