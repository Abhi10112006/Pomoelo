/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  androidx.compose.foundation.lazy.LazyItemScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableIntState
 *  androidx.compose.runtime.snapshots.SnapshotStateList
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function4
 */
package com.example;

import android.view.View;
import androidx.compose.foundation.lazy.LazyItemScope;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.snapshots.SnapshotStateList;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import java.util.List;
import kotlin.jvm.functions.Function4;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda124
implements Function4 {
    public final ThemeOption f$0;
    public final View f$1;
    public final MutableIntState f$2;
    public final SnapshotStateList f$3;
    public final List f$4;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda124(ThemeOption themeOption, View view, MutableIntState mutableIntState, SnapshotStateList snapshotStateList, List list) {
        this.f$0 = themeOption;
        this.f$1 = view;
        this.f$2 = mutableIntState;
        this.f$3 = snapshotStateList;
        this.f$4 = list;
    }

    public final Object invoke(Object object, Object object2, Object object3, Object object4) {
        return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$237$lambda$236$lambda$235(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (LazyItemScope)object, (Integer)object2, (Composer)object3, (Integer)object4);
    }
}

