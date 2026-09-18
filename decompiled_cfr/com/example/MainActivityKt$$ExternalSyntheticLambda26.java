/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.media.AudioManager
 *  android.view.View
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import android.media.AudioManager;
import android.view.View;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda26
implements Function1 {
    public final View f$0;
    public final AudioManager f$1;
    public final MutableState f$2;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda26(View view, AudioManager audioManager, MutableState mutableState) {
        this.f$0 = view;
        this.f$1 = audioManager;
        this.f$2 = mutableState;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$273$lambda$272$lambda$263$lambda$262(this.f$0, this.f$1, this.f$2, ((Float)object).floatValue());
    }
}

