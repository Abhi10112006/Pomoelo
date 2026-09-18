/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.foundation.layout.RowScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.ui.text.font.FontFamily
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import androidx.compose.foundation.layout.RowScope;
import androidx.compose.runtime.Composer;
import androidx.compose.ui.text.font.FontFamily;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda75
implements Function3 {
    public final ThemeOption f$0;
    public final FontFamily f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda75(ThemeOption themeOption, FontFamily fontFamily) {
        this.f$0 = themeOption;
        this.f$1 = fontFamily;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$364$lambda$360(this.f$0, this.f$1, (RowScope)object, (Composer)object2, (Integer)object3);
    }
}

