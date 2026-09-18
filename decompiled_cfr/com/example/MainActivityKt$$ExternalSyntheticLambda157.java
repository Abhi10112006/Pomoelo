/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.runtime.State
 *  androidx.compose.ui.graphics.GraphicsLayerScope
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import androidx.compose.runtime.State;
import androidx.compose.ui.graphics.GraphicsLayerScope;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda157
implements Function1 {
    public final State f$0;
    public final State f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda157(State state, State state2) {
        this.f$0 = state;
        this.f$1 = state2;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$340$lambda$339(this.f$0, this.f$1, (GraphicsLayerScope)object);
    }
}

