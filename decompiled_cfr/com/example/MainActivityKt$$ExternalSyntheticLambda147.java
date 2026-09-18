/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.runtime.Composer
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import android.view.View;
import androidx.compose.runtime.Composer;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda147
implements Function2 {
    public final ThemeOption f$0;
    public final View f$1;
    public final Function0 f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda147(ThemeOption themeOption, View view, Function0 function0) {
        this.f$0 = themeOption;
        this.f$1 = view;
        this.f$2 = function0;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.SettingsOverlay$lambda$228(this.f$0, this.f$1, this.f$2, (Composer)object, (Integer)object2);
    }
}

