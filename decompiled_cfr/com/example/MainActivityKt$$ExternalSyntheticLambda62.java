/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.TimerViewModel
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import android.content.Context;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.TimerViewModel;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda62
implements Function1 {
    public final Context f$0;
    public final TimerViewModel f$1;
    public final MutableState f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda62(Context context, TimerViewModel timerViewModel, MutableState mutableState) {
        this.f$0 = context;
        this.f$1 = timerViewModel;
        this.f$2 = mutableState;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$172$lambda$171$lambda$170$lambda$165$lambda$164(this.f$0, this.f$1, this.f$2, (String)object);
    }
}

