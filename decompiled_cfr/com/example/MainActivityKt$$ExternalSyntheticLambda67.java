/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.foundation.layout.ColumnScope
 *  androidx.compose.runtime.Composer
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.content.Context;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.runtime.Composer;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda67
implements Function3 {
    public final ThemeOption f$0;
    public final Context f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda67(ThemeOption themeOption, Context context) {
        this.f$0 = themeOption;
        this.f$1 = context;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$288(this.f$0, this.f$1, (ColumnScope)object, (Composer)object2, (Integer)object3);
    }
}

