/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  androidx.compose.runtime.Composer
 *  com.android.tools.r8.annotations.SynthesizedClassV2
 *  com.example.data.TaskItem
 *  com.example.service.TimerManager$TimerState
 *  kotlin.jvm.functions.Function2
 */
package com.example;

import android.content.Context;
import androidx.compose.runtime.Composer;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import com.example.MainActivityKt;
import com.example.data.TaskItem;
import com.example.service.TimerManager;
import kotlin.jvm.functions.Function2;

@SynthesizedClassV2(apiLevel=-2, kind=19, versionHash="7bbc1d7254613ffaa026de47d975f73968361ef3e5d7b818ce0a83a33039c6b8")
public final class MainActivityKt$$ExternalSyntheticLambda46
implements Function2 {
    public final int f$0;
    public final int f$1;
    public final TimerManager.TimerState f$2;
    public final boolean f$3;
    public final String f$4;
    public final TaskItem f$5;
    public final Context f$6;
    public final float f$7;
    public final int f$8;
    public final int f$9;

    public /* synthetic */ MainActivityKt$$ExternalSyntheticLambda46(int n, int n2, TimerManager.TimerState timerState, boolean bl, String string, TaskItem taskItem, Context context, float f, int n3, int n4) {
        this.f$0 = n;
        this.f$1 = n2;
        this.f$2 = timerState;
        this.f$3 = bl;
        this.f$4 = string;
        this.f$5 = taskItem;
        this.f$6 = context;
        this.f$7 = f;
        this.f$8 = n3;
        this.f$9 = n4;
    }

    public final Object invoke(Object object, Object object2) {
        return MainActivityKt.TimerDisplay_egy_3UM$lambda$433(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, (Composer)object, (Integer)object2);
    }
}

