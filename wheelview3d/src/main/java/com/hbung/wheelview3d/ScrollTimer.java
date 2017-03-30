// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hbung.wheelview3d;

import java.util.TimerTask;

final class ScrollTimer extends TimerTask {

    int realTotalOffset;
    int realOffset;
    int offset;
    final LoopView loopView;

    ScrollTimer(LoopView loopview,int offset) {
        super();
        this.loopView = loopview;
        this.offset = offset;
        realTotalOffset = Integer.MAX_VALUE;

    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public final void run() {
        if (realTotalOffset == Integer.MAX_VALUE) {
            float itemHeight = loopView.getLineSpacingMultiplier() * loopView.getTextHeight();
            offset = (int) ((offset + itemHeight) % itemHeight);
            if ((float) offset > itemHeight / 2.0F) {
                realTotalOffset = (int) (itemHeight - (float) offset);
            } else {
                realTotalOffset = -offset;
            }
        }
        realOffset = (int) ((float) realTotalOffset * 0.1F);

        if (realOffset == 0) {
            if (realTotalOffset < 0) {
                realOffset = -1;
            } else {
                realOffset = 1;
            }
        }
        if (Math.abs(realTotalOffset) <= 0) {
            loopView.cancelFuture();
            loopView.handler.sendEmptyMessage(3000);
            return;
        } else {
            loopView.setTotalScrollY(loopView.getTotalScrollY() + realOffset);
            loopView.handler.sendEmptyMessage(1000);
            realTotalOffset = realTotalOffset - realOffset;
            return;
        }
    }
}
