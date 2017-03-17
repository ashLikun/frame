package com.hbung.loadingandretrymanager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbung.flatbutton.FlatButton;


/**
 * Created by Administrator on 2016/7/28.
 */

public class MyOnLoadingAndRetryListener extends OnLoadingAndRetryListener {
    private Context context;
    private OnRetryClickListion clickListion;


    public MyOnLoadingAndRetryListener(Context context, OnRetryClickListion clickListion) {
        this.context = context;
        this.clickListion = clickListion;
    }

    @Override
    public void setRetryEvent(View retryView, final ContextData data) {
        TextView title = (TextView) retryView.findViewById(R.id.title);
        if (title != null) {
            if (data == null || TextUtils.isEmpty(data.getTitle())) {
                title.setText(context.getResources().getString(R.string.http_request_failure));
            } else if (data != null && !TextUtils.isEmpty(data.getTitle())) {
                title.setText(data.getTitle() + (data.getErrCode() != 0 ? "" : ("\n(错误码:" + data.getErrCode() + ")")));
            }
        }
        ImageView img = (ImageView) retryView.findViewById(R.id.image);
        if (img != null) {
            if (data == null || data.getResId() <= 0) {
                img.setImageResource(R.drawable.material_service_error);
            } else if (data != null && data.getResId() > 0) {
                img.setImageResource(data.getResId());
            }
        }

        View butt = retryView.findViewById(R.id.reSet);
        if (butt != null) {
            if (!TextUtils.isEmpty(data.getButtonText())) {
                ((FlatButton) butt).setText(data.getButtonText());
            }
            butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListion != null) {
                        clickListion.onRetryClick(data);
                    }
                }
            });
        }


    }

    @Override
    public void setLoadingEvent(View loadingView, ContextData data) {
        TextView title = (TextView) loadingView.findViewById(R.id.content);
        if (title != null) {
            if (data == null || TextUtils.isEmpty(data.getTitle())) {
                title.setText(context.getResources().getString(R.string.loadding));
            } else if (data != null && !TextUtils.isEmpty(data.getTitle())) {
                title.setText(data.getTitle());
            }
        }
    }

    @Override
    public void setEmptyEvent(View emptyView, final ContextData data) {
        TextView title = (TextView) emptyView.findViewById(R.id.title);
        if (title != null) {
            if (data == null || TextUtils.isEmpty(data.getTitle())) {
                title.setText(context.getResources().getString(R.string.no_data));
            } else if (data != null && !TextUtils.isEmpty(data.getTitle())) {
                title.setText(data.getTitle());
            }
        }
        ImageView img = (ImageView) emptyView.findViewById(R.id.image);
        if (img != null) {
            if (data == null || data.getResId() <= 0) {
                img.setImageResource(R.drawable.material_service_error);
            } else if (data != null && data.getResId() > 0) {
                img.setImageResource(data.getResId());
            }
        }
        View butt = emptyView.findViewById(R.id.reSet);
        if (butt != null) {
            if (!TextUtils.isEmpty(data.getButtonText())) {
                ((FlatButton) butt).setText(data.getButtonText());
            }
            butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListion != null) {
                        clickListion.onEmptyClick(data);
                    }
                }
            });
        }

    }
}
