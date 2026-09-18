/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.runtime.Composer
 *  androidx.navigation.NavController
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.TimerViewModel
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import androidx.compose.runtime.Composer;
import androidx.navigation.NavController;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.TimerViewModel;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda21
implements Function2 {
    public final TimerViewModel f$0;
    public final NavController f$1;
    public final float f$2;
    public final int f$3;
    public final int f$4;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda21(TimerViewModel timerViewModel, NavController navController, float f, int n, int n2) {
        this.f$0 = timerViewModel;
        this.f$1 = navController;
        this.f$2 = f;
        this.f$3 = n;
        this.f$4 = n2;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$188(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (Composer)object, (Integer)object2);
    }
}

