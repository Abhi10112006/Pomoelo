/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.animation.AnimatedVisibilityScope
 *  androidx.compose.runtime.Composer
 *  androidx.navigation.NavHostController
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.view.View;
import androidx.compose.animation.AnimatedVisibilityScope;
import androidx.compose.runtime.Composer;
import androidx.navigation.NavHostController;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda137
implements Function3 {
    public final String f$0;
    public final View f$1;
    public final NavHostController f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda137(String string, View view, NavHostController navHostController) {
        this.f$0 = string;
        this.f$1 = view;
        this.f$2 = navHostController;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.PomoPalApp$lambda$51$lambda$50(this.f$0, this.f$1, this.f$2, (AnimatedVisibilityScope)object, (Composer)object2, (Integer)object3);
    }
}

