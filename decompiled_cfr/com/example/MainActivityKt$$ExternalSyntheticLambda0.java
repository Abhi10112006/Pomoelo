/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.animation.core.Animatable
 *  androidx.compose.ui.graphics.drawscope.DrawScope
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import androidx.compose.animation.core.Animatable;
import androidx.compose.ui.graphics.drawscope.DrawScope;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda0
implements Function1 {
    public final ThemeOption f$0;
    public final float f$1;
    public final boolean f$2;
    public final Animatable f$3;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda0(ThemeOption themeOption, float f, boolean bl, Animatable animatable) {
        this.f$0 = themeOption;
        this.f$1 = f;
        this.f$2 = bl;
        this.f$3 = animatable;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.TimerDisplay_egy_3UM$lambda$432$lambda$424$lambda$418$lambda$417(this.f$0, this.f$1, this.f$2, this.f$3, (DrawScope)object);
    }
}

