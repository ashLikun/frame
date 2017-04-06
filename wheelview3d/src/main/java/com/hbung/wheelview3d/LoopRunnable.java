// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hbung.wheelview3d;

import com.hbung.wheelview3d.listener.LoopListener;

final class LoopRunnable implements Runnable {

    final LoopView loopView;

    LoopRunnable(LoopView loopview) {
        super();
        loopView = loopview;

    }

    @Override
    public final void run() {
        LoopListener listener = loopView.loopListener;
        int selectedItem = loopView.getSelectedItem();
        if (selectedItem >= 0 && selectedItem < loopView.getItemCount() && listener != null) {
            listener.onItemSelect(selectedItem, loopView.getItemData(selectedItem));
        }
    }
}
