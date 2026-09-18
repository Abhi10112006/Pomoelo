/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.runtime.Composer
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import androidx.compose.runtime.Composer;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda58
implements Function2 {
    public final ThemeOption f$0;
    public final boolean f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda58(ThemeOption themeOption, boolean bl) {
        this.f$0 = themeOption;
        this.f$1 = bl;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.AppControls$lambda$398$lambda$391(this.f$0, this.f$1, (Composer)object, (Integer)object2);
    }
}

