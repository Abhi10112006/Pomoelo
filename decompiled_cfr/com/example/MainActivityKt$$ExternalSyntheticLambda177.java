/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.State
 *  androidx.navigation.NavHostController
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import android.view.View;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.State;
import androidx.navigation.NavHostController;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda177
implements Function2 {
    public final String f$0;
    public final String f$1;
    public final State f$2;
    public final State f$3;
    public final State f$4;
    public final State f$5;
    public final View f$6;
    public final NavHostController f$7;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda177(String string, String string2, State state, State state2, State state3, State state4, View view, NavHostController navHostController) {
        this.f$0 = string;
        this.f$1 = string2;
        this.f$2 = state;
        this.f$3 = state2;
        this.f$4 = state3;
        this.f$5 = state4;
        this.f$6 = view;
        this.f$7 = navHostController;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.PomoPalApp$lambda$51(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, (Composer)object, (Integer)object2);
    }
}

