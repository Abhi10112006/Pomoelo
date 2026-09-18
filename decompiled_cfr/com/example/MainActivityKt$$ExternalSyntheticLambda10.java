/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.runtime.MutableState
 *  androidx.compose.runtime.State
 *  androidx.compose.ui.graphics.drawscope.DrawScope
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.State;
import androidx.compose.ui.graphics.drawscope.DrawScope;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda10
implements Function1 {
    public final MutableState f$0;
    public final State f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda10(MutableState mutableState, State state) {
        this.f$0 = mutableState;
        this.f$1 = state;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.AlarmRingingLockScreen$lambda$518$lambda$517$lambda$516$lambda$515(this.f$0, this.f$1, (DrawScope)object);
    }
}

