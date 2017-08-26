package com.hbung.charbar;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CharBar extends View {

    public ArrayList<String> letter = new ArrayList<String>();
//    {"最近", "A", "B", "C", "D", "E", "F", "G", "H",
//            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
//            "V", "W", "X", "Y", "Z"};

    private OnTouchLetterChangedListener onTouchLetterChangedListener;
    private int choose = -1;// 选中
    private Paint paint = new Paint();
    private Paint paintSelect = new Paint();
    // 用于显示dialog
    private TextView textView;
    private int textColor;
    private int selectTextColor;
    private int letterHeight;

    public CharBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    public CharBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharBar(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CharBar);
        textColor = a.getColor(R.styleable.CharBar_cb_textColor, Color.BLACK);
        selectTextColor = a.getColor(R.styleable.CharBar_cb_selectTextColor, Color.BLACK);
        a.recycle();
        if (letter.size() == 0) {
            letter.add("#");
        }
        setPaint();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        setPaint();
    }

    public void setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
        setPaint();
    }

    private void setPaint() {
        // 设置粗体
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        // 设置抗锯齿
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        // 设置粗体
        paintSelect.setTypeface(Typeface.DEFAULT_BOLD);
        // 设置抗锯齿
        paintSelect.setAntiAlias(true);
        paintSelect.setColor(selectTextColor);
        paintSelect.setFakeBoldText(true);
    }

    public void addAllChar(List<String> c) {
        letter.clear();
        for (String cc : c) {
            if (!letter.contains(cc)) {
                letter.add(cc);
            }
        }
        if (!letter.contains("#"))
            letter.add("#");
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setPaintTextSize(height - getPaddingTop() - getPaddingBottom());
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            width = (int) paint.measureText(letter.get(0)) + params.leftMargin + params.rightMargin + getPaddingLeft() + getPaddingRight();
        }
        setMeasuredDimension(width, height);
    }

    Paint.FontMetrics fontMetrics = null;
    Paint.FontMetrics fontMetricsSelect = null;

    private void setPaintTextSize(int height) {
        // 获取组件可绘制的大小
        // 获取每一个字母的高度
        letterHeight = (height) / letter.size();
        if (letterHeight >= 20) {
            paint.setTextSize(sp2px(getContext(), 12));
            paintSelect.setTextSize(sp2px(getContext(), 12 + 5));
        } else {
            paintSelect.setTextSize(sp2px(getContext(),
                    letterHeight / 2 + 2 + 5));
            paint.setTextSize(sp2px(getContext(),
                    letterHeight / 2 + 2));
        }
        fontMetrics = paint.getFontMetrics();
        fontMetricsSelect = paintSelect.getFontMetrics();
    }


    public void setHintView(TextView tt) {
        this.textView = tt;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取组件的大小
        int widht = getWidth();
        int height = getHeight();

        // 遍历每一个字母
        for (int i = 0; i < letter.size(); i++) {
            float si = ((choose == i) ? paintSelect : paint).measureText(letter.get(i));
            float xPx = (float) (widht / 2 + (getPaddingLeft() - getPaddingRight()) / 2 - si / 2.0);
            float yPx = getPaddingTop() + letterHeight * i + letterHeight / 2 - ((choose == i) ? fontMetricsSelect : fontMetrics).top / 2;
            canvas.drawText(letter.get(i), xPx, yPx, (choose == i) ? paintSelect : paint);
            // canvas.drawRect(0, letterHeight * i, widht, letterHeight * (i + 1), paint2);
        }
    }


    // 这个方法用来分发TouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        float y = event.getY();
        final int oldChoose = choose;
        // 获取组件可绘制的大小
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        final int click = (int) (y / height * letter.size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        if (click == 0) {
            int aaa = getHeight() - getPaddingTop() - getPaddingBottom();
        }
        if (0 >= y) {
            choose = -1;//
            if (textView != null) {
                textView.setVisibility(View.INVISIBLE);
            }
            invalidate();
            return true;
        }
        switch (action) {
            // 当抬起的时候
            case MotionEvent.ACTION_UP:
                choose = -1;//
                if (textView != null) {
                    textView.setVisibility(View.INVISIBLE);
                }
                invalidate();
                break;
            default:
                // 其他情况
                // 如果点击的不是当前的字母， 就把这个字母发给调用者（回调）
                if (oldChoose != click) {
                    if (click >= 0 && click < letter.size()) {
                        if (onTouchLetterChangedListener != null) {
                            onTouchLetterChangedListener.onTouchLetter(letter.get(click));
                        }
                        if (textView != null) {
                            textView.setText(letter.get(click));
                            textView.setVisibility(View.VISIBLE);
                        }
                        choose = click;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    boolean isUpFinish = true;

    public void setCurrent(int c) {
        // 其他情况
        // 如果点击的不是当前的字母， 就把这个字母发给调用者（回调）
        if (choose != c) {
            if (c >= 0 && c < letter.size()) {
                if (onTouchLetterChangedListener != null) {
                    onTouchLetterChangedListener.onTouchLetter(letter.get(c));
                }
                if (textView != null && c != 0) {
                    textView.setText(letter.get(c));
                    textView.setVisibility(View.VISIBLE);

                }
                choose = c;
                invalidate();
                if (isUpFinish) {
                    isUpFinish = false;
                    postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            choose = -1;
                            if (textView != null) {
                                textView.setVisibility(View.INVISIBLE);
                            }
                            invalidate();
                            isUpFinish = true;
                        }
                    }, 1000);
                }
            }
        }
    }

    // 向外公开的方法
    public void setOnTouchLetterChangedListener(
            OnTouchLetterChangedListener onTouchLetterChangedListener) {
        this.onTouchLetterChangedListener = onTouchLetterChangedListener;
    }

    // 接口
    public interface OnTouchLetterChangedListener {
        public void onTouchLetter(String s);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * fontScale + 0.5f);
    }
}
