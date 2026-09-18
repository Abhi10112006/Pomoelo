/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.data.TaskItem
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import android.view.View;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.data.TaskItem;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda80
implements Function0 {
    public final View f$0;
    public final Function1 f$1;
    public final TaskItem f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda80(View view, Function1 function1, TaskItem taskItem) {
        this.f$0 = view;
        this.f$1 = function1;
        this.f$2 = taskItem;
    }

    public final Object invoke() {
        return MainActivityKt.TaskItemRow$lambda$311$lambda$310(this.f$0, this.f$1, this.f$2);
    }
}

