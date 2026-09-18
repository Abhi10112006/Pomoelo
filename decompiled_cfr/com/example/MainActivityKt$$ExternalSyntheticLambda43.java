/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.View
 *  androidx.compose.animation.core.Animatable
 *  androidx.compose.foundation.layout.BoxWithConstraintsScope
 *  androidx.compose.runtime.Composer
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.service.TimerManager$TimerState
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.content.Context;
import android.view.View;
import androidx.compose.animation.core.Animatable;
import androidx.compose.foundation.layout.BoxWithConstraintsScope;
import androidx.compose.runtime.Composer;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.service.TimerManager;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda43
implements Function3 {
    public final ThemeOption f$0;
    public final boolean f$1;
    public final Animatable f$2;
    public final int f$3;
    public final TimerManager.TimerState f$4;
    public final View f$5;
    public final Context f$6;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda43(ThemeOption themeOption, boolean bl, Animatable animatable, int n, TimerManager.TimerState timerState, View view, Context context) {
        this.f$0 = themeOption;
        this.f$1 = bl;
        this.f$2 = animatable;
        this.f$3 = n;
        this.f$4 = timerState;
        this.f$5 = view;
        this.f$6 = context;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.TimerDisplay_egy_3UM$lambda$432$lambda$424(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, (BoxWithConstraintsScope)object, (Composer)object2, (Integer)object3);
    }
}

