/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import android.view.View;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda174
implements Function2 {
    public final View f$0;
    public final MutableState f$1;
    public final ThemeOption f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda174(View view, MutableState mutableState, ThemeOption themeOption) {
        this.f$0 = view;
        this.f$1 = mutableState;
        this.f$2 = themeOption;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.PomoPalApp$lambda$17(this.f$0, this.f$1, this.f$2, (Composer)object, (Integer)object2);
    }
}

