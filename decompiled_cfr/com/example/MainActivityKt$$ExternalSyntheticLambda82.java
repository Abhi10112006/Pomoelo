/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.runtime.Composer
 *  androidx.compose.ui.Modifier
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.data.TaskItem
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import androidx.compose.runtime.Composer;
import androidx.compose.ui.Modifier;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.data.TaskItem;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda82
implements Function2 {
    public final TaskItem f$0;
    public final boolean f$1;
    public final Function1 f$2;
    public final Function0 f$3;
    public final Modifier f$4;
    public final int f$5;
    public final int f$6;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda82(TaskItem taskItem, boolean bl, Function1 function1, Function0 function0, Modifier modifier, int n, int n2) {
        this.f$0 = taskItem;
        this.f$1 = bl;
        this.f$2 = function1;
        this.f$3 = function0;
        this.f$4 = modifier;
        this.f$5 = n;
        this.f$6 = n2;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.TaskItemRow$lambda$320(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, (Composer)object, (Integer)object2);
    }
}

