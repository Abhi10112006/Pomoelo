/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.animation.core.InfiniteTransition
 *  androidx.compose.foundation.layout.BoxWithConstraintsScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.State
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.view.View;
import androidx.compose.animation.core.InfiniteTransition;
import androidx.compose.foundation.layout.BoxWithConstraintsScope;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.State;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda104
implements Function3 {
    public final int f$0;
    public final String f$1;
    public final String f$2;
    public final State f$3;
    public final InfiniteTransition f$4;
    public final View f$5;
    public final Function0 f$6;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda104(int n, String string, String string2, State state, InfiniteTransition infiniteTransition, View view, Function0 function0) {
        this.f$0 = n;
        this.f$1 = string;
        this.f$2 = string2;
        this.f$3 = state;
        this.f$4 = infiniteTransition;
        this.f$5 = view;
        this.f$6 = function0;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.SeriousFullscreenOverlay$lambda$479(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, (BoxWithConstraintsScope)object, (Composer)object2, (Integer)object3);
    }
}

