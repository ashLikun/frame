// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hbung.wheelview3d;

import java.util.TimerTask;

final class LoopTimerTask extends TimerTask {

    float a;
    final float velocityY;
    final LoopView loopView;

    LoopTimerTask(LoopView loopview, float velocityY) {
        super();
        loopView = loopview;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            loopView.cancelFuture();
            loopView.handler.sendEmptyMessage(2000);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        LoopView loopview = loopView;
        loopview.setTotalScrollY(loopview.getTotalScrollY() - i);
        if (!loopView.isLoop()) {
            float itemHeight = loopView.getLineSpacingMultiplier() * loopView.getTextHeight();
            if (loopView.getTotalScrollY() <= (int) ((float) (-loopView.getInitPosition()) * itemHeight)) {
                a = 40F;
                loopView.setTotalScrollY((int) ((float) (-loopView.getInitPosition()) * itemHeight));
            } else if (loopView.getTotalScrollY() >= (int) ((float) (loopView.getItemCount() - 1 - loopView.getInitPosition()) * itemHeight)) {
                loopView.setTotalScrollY((int) ((float) (loopView.getItemCount() - 1 - loopView.getInitPosition()) * itemHeight));
                a = -40F;
            }
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        loopView.handler.sendEmptyMessage(1000);
    }
}
