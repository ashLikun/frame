// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hbung.wheelview3d;

import android.os.Handler;
import android.os.Message;

// Referenced classes of package com.qingchifan.view:
//            LoopView

final class MessageHandler extends Handler {
    public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    public static final int WHAT_SMOOTH_SCROLL = 2000;
    public static final int WHAT_ITEM_SELECTED = 3000;
    final LoopView loopview;

    MessageHandler(LoopView loopview) {
        super();
        this.loopview = loopview;
    }

    @Override
    public final void handleMessage(Message paramMessage) {
        if (paramMessage.what == WHAT_INVALIDATE_LOOP_VIEW)
            this.loopview.invalidate();
        while (true) {
            if (paramMessage.what == WHAT_SMOOTH_SCROLL)
                loopview.smoothScroll();
            else if (paramMessage.what == WHAT_ITEM_SELECTED)
                this.loopview.itemSelected();
            super.handleMessage(paramMessage);
            return;
        }
    }

}
