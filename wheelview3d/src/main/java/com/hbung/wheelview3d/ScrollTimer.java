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

    ScrollTimer(LoopView loopview, int offset) {
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
            float itemHeight = loopView.getItemHeight();
            offset = (int) ((offset + itemHeight) % itemHeight);//滚动超过item的大小部分
            if ((float) offset > itemHeight / 2.0F) {//如果超过Item高度的一半，滚动到下一个Item去
                realTotalOffset = (int) (itemHeight - offset);
            } else {//其他情况滚动到上一个Item去
                realTotalOffset = -offset;
            }
        }
        //取实际滑动的10分之一  来进行实际的滚动
        realOffset = (int) ((float) realTotalOffset * 0.1F);

        if (realOffset == 0) {
            if (realTotalOffset < 0) {
                realOffset = -1;
            } else {
                realOffset = 1;
            }
        }
        //如果偏移量在-1 到 1  之间说明停止了
        if (Math.abs(realTotalOffset) <= 1) {
            loopView.cancelFuture();
            loopView.handler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
            return;
        } else {
            loopView.setTotalScrollY(loopView.getTotalScrollY() + realOffset);
            loopView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
            realTotalOffset = realTotalOffset - realOffset;
            return;
        }
    }
}
