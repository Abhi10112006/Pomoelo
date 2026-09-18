/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.runtime.DisposableEffectScope
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import android.content.Context;
import androidx.compose.runtime.DisposableEffectScope;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda6
implements Function1 {
    public final Context f$0;
    public final int f$1;
    public final MutableState f$2;
    public final MutableState f$3;
    public final MutableState f$4;
    public final MutableState f$5;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda6(Context context, int n, MutableState mutableState, MutableState mutableState2, MutableState mutableState3, MutableState mutableState4) {
        this.f$0 = context;
        this.f$1 = n;
        this.f$2 = mutableState;
        this.f$3 = mutableState2;
        this.f$4 = mutableState3;
        this.f$5 = mutableState4;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.AlarmRingingLockScreen$lambda$500$lambda$499(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, (DisposableEffectScope)object);
    }
}

