/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.foundation.lazy.LazyListScope
 *  androidx.compose.runtime.MutableIntState
 *  androidx.compose.runtime.snapshots.SnapshotStateList
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import android.view.View;
import androidx.compose.foundation.lazy.LazyListScope;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.snapshots.SnapshotStateList;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import java.util.List;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda138
implements Function1 {
    public final List f$0;
    public final ThemeOption f$1;
    public final View f$2;
    public final MutableIntState f$3;
    public final SnapshotStateList f$4;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda138(List list, ThemeOption themeOption, View view, MutableIntState mutableIntState, SnapshotStateList snapshotStateList) {
        this.f$0 = list;
        this.f$1 = themeOption;
        this.f$2 = view;
        this.f$3 = mutableIntState;
        this.f$4 = snapshotStateList;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (LazyListScope)object);
    }
}

