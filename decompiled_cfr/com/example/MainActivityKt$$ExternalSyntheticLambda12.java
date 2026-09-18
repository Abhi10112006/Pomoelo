/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.runtime.Composer
 *  androidx.compose.ui.focus.FocusManager
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function2
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import androidx.compose.runtime.Composer;
import androidx.compose.ui.focus.FocusManager;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda12
implements Function2 {
    public final FocusManager f$0;
    public final Function3 f$1;
    public final Function0 f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda12(FocusManager focusManager, Function3 function3, Function0 function0) {
        this.f$0 = focusManager;
        this.f$1 = function3;
        this.f$2 = function0;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.AddTaskCard$lambda$368(this.f$0, this.f$1, this.f$2, (Composer)object, (Integer)object2);
    }
}

