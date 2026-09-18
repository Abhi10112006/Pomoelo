/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.runtime.MutableState
 *  androidx.lifecycle.Lifecycle$Event
 *  androidx.lifecycle.LifecycleEventObserver
 *  androidx.lifecycle.LifecycleOwner
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 */
package com.example;

import android.content.Context;
import androidx.compose.runtime.MutableState;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda52
implements LifecycleEventObserver {
    public final Context f$0;
    public final MutableState f$1;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda52(Context context, MutableState mutableState) {
        this.f$0 = context;
        this.f$1 = mutableState;
    }

    public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        MainActivityKt.SettingsOverlay$lambda$306$lambda$305$lambda$304$lambda$303$lambda$302$lambda$283$lambda$282$lambda$280(this.f$0, this.f$1, lifecycleOwner, event);
    }
}

