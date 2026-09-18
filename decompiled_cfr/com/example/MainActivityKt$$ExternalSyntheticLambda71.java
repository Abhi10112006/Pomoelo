/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.foundation.text.KeyboardActionScope
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.view.View;
import androidx.compose.foundation.text.KeyboardActionScope;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda71
implements Function1 {
    public final View f$0;
    public final Function3 f$1;
    public final MutableState f$2;
    public final MutableState f$3;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda71(View view, Function3 function3, MutableState mutableState, MutableState mutableState2) {
        this.f$0 = view;
        this.f$1 = function3;
        this.f$2 = mutableState;
        this.f$3 = mutableState2;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.AddTaskCard$lambda$368$lambda$367$lambda$366$lambda$365$lambda$351$lambda$350(this.f$0, this.f$1, this.f$2, this.f$3, (KeyboardActionScope)object);
    }
}

