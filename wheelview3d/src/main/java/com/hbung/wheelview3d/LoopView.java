package com.hbung.wheelview3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class LoopView extends View {
    private int scrollDelay = 20;
    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;//可控制任务，通过线程池的方法执行一个任务后会返回Future对象
    protected Handler handler;
    protected LoopListener loopListener;
    private GestureDetector gestureDetector;
    private int selectedItem;
    private int finalSelectedItem = 0;
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener;
    private Paint mPaint;  //全局画笔
    private List<LoopViewData> arrayList;
    private int textSize;
    private int maxTextWidth;
    private int maxTextHeight;
    private int colorGray;
    private int colorBlack;
    private int colorGrayLight;
    private float lineSpacingMultiplier;//行间距
    private float lineWidth;
    private boolean isLoop = true;
    private int firstLineY;//第一条线的Y
    private int secondLineY;//第二条线的Y
    private int preCurrentIndex;//当前位置
    private int initPosition = -1;//初始化位置
    private int showItemCount = 7;//一页显示item的个数
    private int halfCircumference;//一半圆周长
    private int radius;//圆半径
    private float previousY = 0;//上一次y坐标
    private int totalScrollY = 0;//总滑动y长度

    public LoopView(Context context) {
        super(context);
        initLoopView(context);
    }

    public LoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context);
    }

    public LoopView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initLoopView(context);
    }

    private void initLoopView(Context context) {
        colorGray = 0xffafafaf;
        colorBlack = 0xff313131;
        colorGrayLight = 0xffc5c5c5;
        lineSpacingMultiplier = 2.5F;
        lineWidth = dip2px(context, 1f);
        simpleOnGestureListener = new LoopViewGestureListener(this);
        handler = new MessageHandler(this);
        setTextSize(16F);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        gestureDetector = new GestureDetector(context, simpleOnGestureListener);
        gestureDetector.setIsLongpressEnabled(false);
    }

    static int getSelectedItem(LoopView loopview) {
        return loopview.selectedItem;
    }


    private void initData() {
        if (arrayList == null) {
            return;
        }
        measureTextWidthHeight();
        halfCircumference = (int) (maxTextHeight * lineSpacingMultiplier * (showItemCount - 1));
        radius = (int) (halfCircumference / Math.PI);
        firstLineY = (int) ((radius * 2 - lineSpacingMultiplier * maxTextHeight) / 2.0F);
        secondLineY = (int) ((radius * 2 + lineSpacingMultiplier * maxTextHeight) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (arrayList.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }
        preCurrentIndex = initPosition;
    }

    //测量文字大小
    private void measureTextWidthHeight() {
        switchPaintToSelectText();
        Rect rect = new Rect();
        for (int i = 0; i < arrayList.size(); i++) {
            String s1 = arrayList.get(i).getTitle();
            mPaint.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
            mPaint.getTextBounds("\u661F\u671F", 0, 2, rect); // 星期
            int textHeight = rect.height();
            if (textHeight > maxTextHeight) {
                maxTextHeight = textHeight;
            }
        }
    }


    protected void smoothScroll() {
        int offset = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
        cancelFuture();
        mFuture = mExecutor.scheduleWithFixedDelay(new ScrollTimer(this, offset), 0, scrollDelay, TimeUnit.MILLISECONDS);
    }

    //取消滚动任务
    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    public final void setNotLoop() {
        isLoop = false;
    }

    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (getContext().getResources().getDisplayMetrics().density * size);
        }
    }

    public final void setInitPosition(int initPosition) {
        this.initPosition = initPosition;
        finalSelectedItem = initPosition;

        totalScrollY = 0;
    }

    public void setFinalSelectedItem(int finalSelectedItem) {
        this.finalSelectedItem = finalSelectedItem;
    }

    public int getFinalSelectedItem() {
        return finalSelectedItem;
    }

    public final void setListener(LoopListener LoopListener) {
        loopListener = LoopListener;
    }

    public final void setArrayList(List arraylist) {
        this.arrayList = arraylist;
        initData();
        invalidate();
    }

    public List<LoopViewData> getArrayList() {
        return arrayList;
    }

    public final int getSelectedItem() {
        return finalSelectedItem;
    }

    public LoopViewData getSelectedItemData() {
        if (finalSelectedItem >= 0 && finalSelectedItem < arrayList.size()) {
            return arrayList.get(finalSelectedItem);
        }
        return null;
    }

    protected final void smoothScroll(float velocityY) {
        cancelFuture();
        mFuture = mExecutor.scheduleWithFixedDelay(new LoopTimerTask(this, velocityY), 0, scrollDelay, TimeUnit.MILLISECONDS);
    }


    protected final void itemSelected() {
        postDelayed(new LoopRunnable(this), 200L);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (arrayList == null) {
            super.onDraw(canvas);
            return;
        }
        computeCurrentIndex();
        drawLine(canvas);
        drawText(canvas);
        super.onDraw(canvas);
    }

    private void drawLine(Canvas canvas) {
        switchPaintToLine();
        canvas.drawLine(0.0F, firstLineY, getMeasuredWidth(), firstLineY, mPaint);
        canvas.drawLine(0.0F, secondLineY, getMeasuredWidth(), secondLineY, mPaint);
    }

    private void drawText(Canvas canvas) {
        LoopViewData showDatas[] = getShowLoopData();
        int startX = getMeasuredWidth() / 2;//开始绘制的x坐标
        //滑动 超过item的大小
        int itemHeightOffset = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
        int index = 0;
        while (index < showItemCount) {
            canvas.save();
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            // 弧长 L = itemHeight * index - itemHeightOffset
            // 求弧度 α = L / r  (弧长/半径) [0,π]
            double radian = ((itemHeight * index - itemHeightOffset)) / radius;
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            // angle [-90°,90°]   弧度/π = 角度
            float degrees = (float) (90 - Math.toDegrees(radian));//item第一项,从90度开始，逐渐递减到 -90度
            if (degrees >= 90F || degrees <= -90F) {
                canvas.restore();
            } else {
                //根据弧度计算平移y的大小
                int translateY = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight / 2D));
                //根据translateY来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                //画第一条线顶部和底部的选中文字
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    canvas.save();
                    //画第一条线以上的选中的item，其实是未选择画笔画的
                    canvas.clipRect(0, 0, getMeasuredWidth(), firstLineY - translateY);
                    switchPaintToNoSelectText();
                    canvas.drawText(showDatas[index].getTitle(), startX, maxTextHeight, mPaint);
                    canvas.restore();
                    canvas.save();
                    //画第一条线以下的选中文字
                    canvas.clipRect(0, firstLineY - translateY, getMeasuredWidth(), (int) (itemHeight));
                    switchPaintToSelectText();
                    canvas.drawText(showDatas[index].getTitle(), startX, maxTextHeight, mPaint);
                    canvas.restore();
                }
                //画第二条线顶部和底部的选中文字
                else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    canvas.save();
                    //画第二条线以上的选中的item
                    canvas.clipRect(0, 0, getMeasuredWidth(), secondLineY - translateY);
                    switchPaintToSelectText();
                    canvas.drawText(showDatas[index].getTitle(), startX, maxTextHeight, mPaint);
                    canvas.restore();
                    canvas.save();
                    //画第一条线以下的选中文字  其实是未选择画笔画的
                    canvas.clipRect(0, secondLineY - translateY, getMeasuredWidth(), (int) (itemHeight));
                    switchPaintToNoSelectText();
                    canvas.drawText(showDatas[index].getTitle(), startX, maxTextHeight, mPaint);
                    canvas.restore();
                }
                //画中间选中的文字
                else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    canvas.clipRect(0, 0, getMeasuredWidth(), (int) (itemHeight));
                    switchPaintToSelectText();
                    canvas.drawText(showDatas[index].getTitle(), startX, maxTextHeight, mPaint);
                    selectedItem = arrayList.indexOf(showDatas[index]);
                } else {
                    //没有选中的文本
                    canvas.clipRect(0, 0, getMeasuredWidth(), (int) (itemHeight));
                    switchPaintToNoSelectText();
                    canvas.drawText(showDatas[index].getTitle(), startX, maxTextHeight, mPaint);
                }
                canvas.restore();
            }
            index++;
        }
    }

    //计算滑动到的item位置
    private void computeCurrentIndex() {
        if (getItemCount() == 0) {
            preCurrentIndex = 0;
        } else {
            //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
            int change = (int) (totalScrollY / (lineSpacingMultiplier * maxTextHeight));
            preCurrentIndex = initPosition + change % arrayList.size();
        }
        //preCurrentIndex 边界限制
        if (!isLoop) {
            preCurrentIndex = Math.max(preCurrentIndex, 0);
            preCurrentIndex = Math.min(getItemCount() - 1, preCurrentIndex);
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = getItemCount() + preCurrentIndex;
            }
            if (preCurrentIndex > getItemCount() - 1) {
                preCurrentIndex = preCurrentIndex - getItemCount();
            }
        }
    }

    //更具preCurrentIndex值计算应该显示的item
    private LoopViewData[] getShowLoopData() {
        LoopViewData[] visibles = new LoopViewData[showItemCount];
        // 设置数组中每个元素的值
        int counter = 0;
        while (counter < showItemCount) {
            //索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值
            int index = preCurrentIndex - (showItemCount / 2 - counter);
            //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，
            // 如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
            if (isLoop) {
                //index是相对循环的position
                if (index < 0) {
                    index = index + getItemCount() * (Math.abs(index) / getItemCount() + 1);
                }
                index = index % getItemCount();
                visibles[counter] = getItemData(index);
            } else if (index < 0) {
                visibles[counter] = new LoopViewData(-1, "");
            } else if (index > getItemCount() - 1) {
                visibles[counter] = new LoopViewData(-1, "");
            } else {
                visibles[counter] = getItemData(index);
            }
            counter++;
        }
        return visibles;
    }


    //设置画笔为绘制选中文本
    private void switchPaintToSelectText() {
        mPaint.setColor(colorBlack);
        mPaint.setTextScaleX(1.05F);
        mPaint.setTextSize(textSize);
    }

    //设置画笔为未选中文本
    private void switchPaintToNoSelectText() {
        mPaint.setColor(colorGray);
        mPaint.setTextScaleX(1F);
        mPaint.setTextSize(textSize);
    }

    //设置画笔为绘制线
    private void switchPaintToLine() {
        mPaint.setColor(colorGrayLight);
        mPaint.setTextScaleX(1F);
        mPaint.setTextSize(textSize);
        mPaint.setStrokeWidth(lineWidth);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //l=C/PI   园直径
        int measuredHeight = (int) ((halfCircumference * 2) / Math.PI);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionevent) {
        switch (motionevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousY = motionevent.getRawY();//按下的坐标
                break;
            case MotionEvent.ACTION_MOVE:
                //偏移量
                float dy = previousY - motionevent.getRawY();
                previousY = motionevent.getRawY();//更新上一次坐标
                totalScrollY = (int) ((float) totalScrollY + dy);
                if (!isLoop) {
                    int initPositionCircleLength = (int) (initPosition * (lineSpacingMultiplier * maxTextHeight));
                    int initPositionStartY = -1 * initPositionCircleLength;
                    if (totalScrollY < initPositionStartY) {
                        totalScrollY = initPositionStartY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                if (!gestureDetector.onTouchEvent(motionevent) && motionevent.getAction() == MotionEvent.ACTION_UP) {
                    smoothScroll();
                }
                return true;
        }

        if (!isLoop) {
            int circleLength = (int) ((float) (arrayList.size() - 1 - initPosition) * (lineSpacingMultiplier * maxTextHeight));
            if (totalScrollY >= circleLength) {
                totalScrollY = circleLength;
            }
        }
        invalidate();

        if (!gestureDetector.onTouchEvent(motionevent) && motionevent.getAction() == MotionEvent.ACTION_UP) {
            smoothScroll();
        }
        return true;
    }

    int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public float getLineSpacingMultiplier() {
        return lineSpacingMultiplier;
    }

    public int getTextWidth() {
        return maxTextWidth;
    }

    public int getTextHeight() {
        return maxTextHeight;
    }

    protected int getTotalScrollY() {
        return totalScrollY;
    }

    protected void setTotalScrollY(int totalScrollY) {
        this.totalScrollY = totalScrollY;
    }


    public boolean isLoop() {
        return isLoop;
    }

    protected int getInitPosition() {
        return initPosition;
    }

    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    public LoopViewData getItemData(int item) {
        return arrayList.get(item);
    }
}
