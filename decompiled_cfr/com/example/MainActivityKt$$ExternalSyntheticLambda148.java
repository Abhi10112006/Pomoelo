/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioManager
 *  android.view.View
 *  androidx.activity.compose.ManagedActivityResultLauncher
 *  androidx.compose.foundation.layout.PaddingValues
 *  androidx.compose.foundation.lazy.LazyListState
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableIntState
 *  androidx.compose.runtime.MutableState
 *  androidx.compose.runtime.snapshots.SnapshotStateList
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import androidx.activity.compose.ManagedActivityResultLauncher;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.lazy.LazyListState;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.snapshots.SnapshotStateList;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import java.util.List;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda148
implements Function3 {
    public final LazyListState f$0;
    public final List f$1;
    public final float f$10;
    public final MutableState f$11;
    public final ManagedActivityResultLauncher f$12;
    public final MutableState f$13;
    public final MutableState f$14;
    public final MutableIntState f$2;
    public final View f$3;
    public final SnapshotStateList f$4;
    public final Context f$5;
    public final MutableState f$6;
    public final MutableState f$7;
    public final MutableState f$8;
    public final AudioManager f$9;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda148(LazyListState lazyListState, List list, MutableIntState mutableIntState, View view, SnapshotStateList snapshotStateList, Context context, MutableState mutableState, MutableState mutableState2, MutableState mutableState3, AudioManager audioManager, float f, MutableState mutableState4, ManagedActivityResultLauncher managedActivityResultLauncher, MutableState mutableState5, MutableState mutableState6) {
        this.f$0 = lazyListState;
        this.f$1 = list;
        this.f$2 = mutableIntState;
        this.f$3 = view;
        this.f$4 = snapshotStateList;
        this.f$5 = context;
        this.f$6 = mutableState;
        this.f$7 = mutableState2;
        this.f$8 = mutableState3;
        this.f$9 = audioManager;
        this.f$10 = f;
        this.f$11 = mutableState4;
        this.f$12 = managedActivityResultLauncher;
        this.f$13 = mutableState5;
        this.f$14 = mutableState6;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.SettingsOverlay$lambda$306(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, this.f$13, this.f$14, (PaddingValues)object, (Composer)object2, (Integer)object3);
    }
}

