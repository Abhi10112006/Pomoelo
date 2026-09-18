/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  androidx.compose.runtime.MutableState
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  kotlin.jvm.functions.Function1
 */
package com.example;

import android.content.Context;
import android.net.Uri;
import androidx.compose.runtime.MutableState;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import kotlin.jvm.functions.Function1;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda145
implements Function1 {
    public final Context f$0;
    public final MutableState f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda145(Context context, MutableState mutableState) {
        this.f$0 = context;
        this.f$1 = mutableState;
    }

    public final Object invoke(Object object) {
        return MainActivityKt.SettingsOverlay$lambda$220$lambda$219(this.f$0, this.f$1, (Uri)object);
    }
}

