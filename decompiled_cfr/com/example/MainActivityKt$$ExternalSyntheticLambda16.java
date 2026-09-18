/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.SystemProtectionStates
 *  com.example.ui.TimerViewModel
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import android.content.Context;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.SystemProtectionStates;
import com.example.ui.TimerViewModel;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda16
implements Function2 {
    public final ThemeOption f$0;
    public final SystemProtectionStates f$1;
    public final Context f$2;
    public final TimerViewModel f$3;
    public final MutableState f$4;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda16(ThemeOption themeOption, SystemProtectionStates systemProtectionStates, Context context, TimerViewModel timerViewModel, MutableState mutableState) {
        this.f$0 = themeOption;
        this.f$1 = systemProtectionStates;
        this.f$2 = context;
        this.f$3 = timerViewModel;
        this.f$4 = mutableState;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$172(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (Composer)object, (Integer)object2);
    }
}

