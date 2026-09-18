/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.runtime.DisposableEffectScope
 *  androidx.compose.runtime.MutableState
 *  androidx.lifecycle.LifecycleOwner
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import android.content.Context;
import androidx.compose.runtime.DisposableEffectScope;
import androidx.compose.runtime.MutableState;
import androidx.lifecycle.LifecycleOwner;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda66
implements Function1 {
    public final LifecycleOwner f$0;
    public final Context f$1;
    public final MutableState f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda66(LifecycleOwner lifecycleOwner, Context context, MutableState mutableState) {
        this.f$0 = lifecycleOwner;
        this.f$1 = context;
        this.f$2 = mutableState;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282(this.f$0, this.f$1, this.f$2, (DisposableEffectScope)object);
    }
}

