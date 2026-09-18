/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.media.AudioManager
 *  android.view.View
 *  androidx.activity.compose.ManagedActivityResultLauncher
 *  androidx.compose.animation.AnimatedContentScope
 *  androidx.compose.runtime.Composer
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.ui.theme.ThemeOption
 *  kotlin.jvm.functions.Function4
 */
package com.example;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import androidx.activity.compose.ManagedActivityResultLauncher;
import androidx.compose.animation.AnimatedContentScope;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.ui.theme.ThemeOption;
import kotlin.jvm.functions.Function4;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda140
implements Function4 {
    public final ThemeOption f$0;
    public final Context f$1;
    public final MutableState f$10;
    public final MutableState f$11;
    public final View f$2;
    public final MutableState f$3;
    public final MutableState f$4;
    public final MutableState f$5;
    public final AudioManager f$6;
    public final float f$7;
    public final MutableState f$8;
    public final ManagedActivityResultLauncher f$9;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda140(ThemeOption themeOption, Context context, View view, MutableState mutableState, MutableState mutableState2, MutableState mutableState3, AudioManager audioManager, float f, MutableState mutableState4, ManagedActivityResultLauncher managedActivityResultLauncher, MutableState mutableState5, MutableState mutableState6) {
        this.f$0 = themeOption;
        this.f$1 = context;
        this.f$2 = view;
        this.f$3 = mutableState;
        this.f$4 = mutableState2;
        this.f$5 = mutableState3;
        this.f$6 = audioManager;
        this.f$7 = f;
        this.f$8 = mutableState4;
        this.f$9 = managedActivityResultLauncher;
        this.f$10 = mutableState5;
        this.f$11 = mutableState6;
    }

    public final Object invoke(Object object, Object object2, Object object3, Object object4) {
        return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, (AnimatedContentScope)object, (Integer)object2, (Composer)object3, (Integer)object4);
    }
}

