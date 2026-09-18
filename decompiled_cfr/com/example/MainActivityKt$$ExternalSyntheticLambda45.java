/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.compose.foundation.layout.ColumnScope
 *  androidx.compose.runtime.Composer
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.data.TaskItem
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.runtime.Composer;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.data.TaskItem;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda45
implements Function3 {
    public final TaskItem f$0;
    public final ThemeOption f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda45(TaskItem taskItem, ThemeOption themeOption) {
        this.f$0 = taskItem;
        this.f$1 = themeOption;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.TimerDisplay_egy_3UM$lambda$432$lambda$429(this.f$0, this.f$1, (ColumnScope)object, (Composer)object2, (Integer)object3);
    }
}

