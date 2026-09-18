/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.animation.AnimatedVisibilityScope
 *  androidx.compose.runtime.Composer
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.data.TaskItem
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import androidx.compose.animation.AnimatedVisibilityScope;
import androidx.compose.runtime.Composer;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.data.TaskItem;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda129
implements Function3 {
    public final TaskItem f$0;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda129(TaskItem taskItem) {
        this.f$0 = taskItem;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.TaskItemRow$lambda$319$lambda$318$lambda$315(this.f$0, (AnimatedVisibilityScope)object, (Composer)object2, (Integer)object3);
    }
}

