// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hbung.wheelview3d;

// Referenced classes of package com.qingchifan.view:
//            LoopView, LoopListener

final class LoopRunnable implements Runnable {

    final LoopView loopView;

    LoopRunnable(LoopView loopview) {
        super();
        loopView = loopview;

    }

    @Override
    public final void run() {
        LoopListener listener = loopView.loopListener;
        int selectedItem = LoopView.getSelectedItem(loopView);
        loopView.setFinalSelectedItem(selectedItem);
        if (selectedItem >= 0 && selectedItem < loopView.arrayList.size() && listener != null) {
            listener.onItemSelect(selectedItem, loopView.arrayList.get(selectedItem));
        }
    }
}
