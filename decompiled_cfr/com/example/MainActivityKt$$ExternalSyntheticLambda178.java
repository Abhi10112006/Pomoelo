/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.foundation.layout.PaddingValues
 *  androidx.compose.runtime.Composer
 *  androidx.navigation.NavHostController
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.TimerViewModel
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.runtime.Composer;
import androidx.navigation.NavHostController;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.TimerViewModel;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda178
implements Function3 {
    public final NavHostController f$0;
    public final String f$1;
    public final TimerViewModel f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda178(NavHostController navHostController, String string, TimerViewModel timerViewModel) {
        this.f$0 = navHostController;
        this.f$1 = string;
        this.f$2 = timerViewModel;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.PomoPalApp$lambda$79(this.f$0, this.f$1, this.f$2, (PaddingValues)object, (Composer)object2, (Integer)object3);
    }
}

