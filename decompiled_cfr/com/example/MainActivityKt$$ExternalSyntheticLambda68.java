/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.foundation.layout.ColumnScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.content.Context;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda68
implements Function3 {
    public final ThemeOption f$0;
    public final boolean f$1;
    public final Context f$2;
    public final MutableState f$3;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda68(ThemeOption themeOption, boolean bl, Context context, MutableState mutableState) {
        this.f$0 = themeOption;
        this.f$1 = bl;
        this.f$2 = context;
        this.f$3 = mutableState;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$296(this.f$0, this.f$1, this.f$2, this.f$3, (ColumnScope)object, (Composer)object2, (Integer)object3);
    }
}

