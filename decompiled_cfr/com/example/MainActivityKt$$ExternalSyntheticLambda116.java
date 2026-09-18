/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.animation.AnimatedVisibilityScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.SystemProtectionStates
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import androidx.compose.animation.AnimatedVisibilityScope;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.SystemProtectionStates;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda116
implements Function3 {
    public final SystemProtectionStates f$0;
    public final MutableState f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda116(SystemProtectionStates systemProtectionStates, MutableState mutableState) {
        this.f$0 = systemProtectionStates;
        this.f$1 = mutableState;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.HomeScreen_6a0pyJM$lambda$160$lambda$159$lambda$148$lambda$147$lambda$108(this.f$0, this.f$1, (AnimatedVisibilityScope)object, (Composer)object2, (Integer)object3);
    }
}

