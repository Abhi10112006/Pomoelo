/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioManager
 *  android.view.View
 *  androidx.activity.compose.ManagedActivityResultLauncher
 *  androidx.compose.foundation.layout.ColumnScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function3
 */
package com.example;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import androidx.activity.compose.ManagedActivityResultLauncher;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function3;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda65
implements Function3 {
    public final ThemeOption f$0;
    public final View f$1;
    public final Context f$10;
    public final MutableState f$11;
    public final MutableState f$12;
    public final AudioManager f$2;
    public final float f$3;
    public final MutableState f$4;
    public final MutableState f$5;
    public final MutableState f$6;
    public final MutableState f$7;
    public final MutableState f$8;
    public final ManagedActivityResultLauncher f$9;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda65(ThemeOption themeOption, View view, AudioManager audioManager, float f, MutableState mutableState, MutableState mutableState2, MutableState mutableState3, MutableState mutableState4, MutableState mutableState5, ManagedActivityResultLauncher managedActivityResultLauncher, Context context, MutableState mutableState6, MutableState mutableState7) {
        this.f$0 = themeOption;
        this.f$1 = view;
        this.f$2 = audioManager;
        this.f$3 = f;
        this.f$4 = mutableState;
        this.f$5 = mutableState2;
        this.f$6 = mutableState3;
        this.f$7 = mutableState4;
        this.f$8 = mutableState5;
        this.f$9 = managedActivityResultLauncher;
        this.f$10 = context;
        this.f$11 = mutableState6;
        this.f$12 = mutableState7;
    }

    public final Object invoke(Object object, Object object2, Object object3) {
        return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, (ColumnScope)object, (Composer)object2, (Integer)object3);
    }
}

