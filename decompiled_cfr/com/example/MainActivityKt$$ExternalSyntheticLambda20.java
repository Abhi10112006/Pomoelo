/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.foundation.layout.ColumnScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableState
 *  androidx.compose.runtime.State
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.TimerViewModel
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.State;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.TimerViewModel;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda20
implements Function3 {
    public final ThemeOption f$0;
    public final State f$1;
    public final State f$2;
    public final TimerViewModel f$3;
    public final MutableState f$4;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda20(ThemeOption themeOption, State state, State state2, TimerViewModel timerViewModel, MutableState mutableState) {
        this.f$0 = themeOption;
        this.f$1 = state;
        this.f$2 = state2;
        this.f$3 = timerViewModel;
        this.f$4 = mutableState;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$187(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (ColumnScope)object, (Composer)object2, (Integer)object3);
    }
}

