/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.foundation.layout.ColumnScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.ui.text.font.FontFamily
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.data.TaskItem
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.view.View;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.runtime.Composer;
import androidx.compose.ui.text.font.FontFamily;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.data.TaskItem;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda81
implements Function3 {
    public final TaskItem f$0;
    public final boolean f$1;
    public final View f$2;
    public final Function0 f$3;
    public final ThemeOption f$4;
    public final FontFamily f$5;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda81(TaskItem taskItem, boolean bl, View view, Function0 function0, ThemeOption themeOption, FontFamily fontFamily) {
        this.f$0 = taskItem;
        this.f$1 = bl;
        this.f$2 = view;
        this.f$3 = function0;
        this.f$4 = themeOption;
        this.f$5 = fontFamily;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.TaskItemRow$lambda$319(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, (ColumnScope)object, (Composer)object2, (Integer)object3);
    }
}

