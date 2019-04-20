package com.zpan.sharelib.business;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.handsome.sharelib.R;
import com.zpan.sharelib.ShareBuilder;
import com.zpan.sharelib.callback.ShareCancleListener;
import com.zpan.sharelib.callback.ShareCopyListener;
import com.zpan.sharelib.callback.ShareItemClickListener;
import com.zpan.sharelib.icon.ShareIcon;
import com.zpan.sharelib.icon.ShareViewIcons;
import com.zpan.sharelib.iconfont.IconFontTextView;
import com.zpan.sharelib.sns.config.SnsMediaType;
import java.util.ArrayList;

/**
 * 分享面板视图
 *
 * @author zpan
 * @date 2019/3/21
 */
public class SharePopupWindow {

    private static final int SHARE_VIEW_MAX_ROW = 3;

    private Context mContext;
    private ShareViewIcons mShareViewIcons;
    private ShareItemClickListener mShareItemClickListener;
    private ShareCopyListener mShareCopyListener;
    private ShareCancleListener mShareCancleListener;
    private SnsMediaType mMediaType;
    private ShareLibBusiness mShareLibBusiness;

    private View mRootView;
    private RelativeLayout mShareRoot;
    private LinearLayout mShareBottom;
    private RecyclerView mShareItem;
    private TextView mShareCopy;
    private TextView mShareCancel;

    private PopupWindow mPopupWindow;

    public static ShareBuilder getShareBuilder() {
        return new ShareBuilder();
    }

    public SharePopupWindow(Context context, ShareBuilder builder) {
        this.mContext = context;
        this.mShareItemClickListener = builder.shareItemClickListener;
        this.mShareCopyListener = builder.shareCopyListener;
        this.mShareCancleListener = builder.shareCancleListener;
        this.mMediaType = builder.snsMediaType;
        this.mShareViewIcons = builder.shareViewIcons;
        this.mShareLibBusiness = builder.shareLibBusiness;

        initShow();
    }

    private void initShow() {
        initShareView();
        initPopwindow();
        initShareItem();
        initShareEvent();
    }

    @SuppressLint("InflateParams")
    private void initShareView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = inflater.inflate(R.layout.sns_share_view, null, false);
        mShareItem = mRootView.findViewById(R.id.rv_share_popwindow_item);
        mShareRoot = mRootView.findViewById(R.id.rl_share_popwindow_root);
        mShareBottom = mRootView.findViewById(R.id.ll_share_popwindow_bottom);
        mShareCopy = mRootView.findViewById(R.id.tv_share_popwindow_copy);
        mShareCancel = mRootView.findViewById(R.id.tv_share_popwindow_cancel);

        int rootBackground = ContextCompat.getColor(mContext, R.color.pub_layout_bg_color);
        mShareRoot.setBackgroundColor(rootBackground);
        mShareRoot.getBackground().mutate().setAlpha(150);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mShareRoot.setLayoutParams(params);
    }

    private void initPopwindow() {
        int popWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int popHeight = ViewGroup.LayoutParams.MATCH_PARENT;
        mPopupWindow = new PopupWindow(mRootView, popWidth, popHeight, true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation(mShareCancel, Gravity.BOTTOM, 0, 0);
    }

    private void initShareItem() {
        int size = mShareViewIcons.getShareIcons().size();
        int row;
        if (size <= SHARE_VIEW_MAX_ROW) {
            row = size;
        } else {
            row = SHARE_VIEW_MAX_ROW;
        }
        mShareItem.setLayoutManager(new GridLayoutManager(mContext, row));
        PubSnsAdapter snsAdapter = new PubSnsAdapter(mContext);
        mShareItem.setAdapter(snsAdapter);
        snsAdapter.setIcons(mShareViewIcons.getShareIcons());
        snsAdapter.notifyDataSetChanged();

        snsAdapter.setOnItemClickLitener(new PubSnsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int snsPlatform) {
                clickItem(view, (Integer) view.getTag());
                dismissPopupWindow();
            }
        });
    }

    private void initShareEvent() {
        final float[] touchY = new float[1];
        mShareRoot.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchY[0] = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (null != mShareCopyListener) {
                            if (touchY[0] < mShareCopy.getTop()) {
                                dismissPopupWindow();
                            }
                        } else {
                            if (touchY[0] < mShareBottom.getTop()) {
                                dismissPopupWindow();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        if (mShareCopyListener != null) {
            mShareCopy.setVisibility(View.VISIBLE);
            mShareCopy.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mShareCopyListener.onShareCopy();
                }
            });
        }

        mShareCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mShareCancleListener != null) {
                    mShareCancleListener.onShareCancle();
                }
                dismissPopupWindow();
            }
        });
    }

    private void clickItem(View clickView, int snsPlatform) {
        // 自定义点击处理
        if (null != mShareItemClickListener) {
            mShareItemClickListener.onItemClick(mPopupWindow, clickView, snsPlatform);
        }

        if (null != mShareLibBusiness) {
            mShareLibBusiness.shareStart(snsPlatform, mMediaType);
        }
    }

    private void dismissPopupWindow() {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
        }

        if (null != mShareLibBusiness) {
            //mShareLibBusiness.shareFinish();
        }
    }

    static class PubSnsAdapter extends RecyclerView.Adapter<PubSnsAdapter.ItemHolder> {

        private OnItemClickListener mOnItemClickListener;
        private LayoutInflater mLayoutInflater;
        private ArrayList<ShareIcon> mIcons;

        public PubSnsAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        public void setIcons(ArrayList<ShareIcon> icons) {
            this.mIcons = icons;
        }

        @Override
        public int getItemCount() {
            return mIcons != null ? mIcons.size() : 0;
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemHolder(mLayoutInflater.inflate(R.layout.sns_share_view_item, null));
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemHolder holder, @SuppressLint("RecyclerView") final int position) {
            ShareIcon shareIcon = mIcons.get(position);
            if (shareIcon != null) {
                holder.itemTvText.setText(shareIcon.iconText);
                holder.itemTvText.setTextColor(shareIcon.iconTextColor);

                if (shareIcon.useResourceIcon) {
                    holder.itemIcon.setVisibility(View.VISIBLE);
                    holder.itemIcon.setBackgroundResource(shareIcon.iconResourceId);
                } else {
                    holder.itemTvIcon.setVisibility(View.VISIBLE);
                    holder.itemTvIcon.setIcon(shareIcon.iconfont, shareIcon.iconColor, shareIcon.iconSize);
                }

                holder.itemTvIcon.setTag(shareIcon.platform);
                int backgroundResId = shareIcon.backgroundResId;
                if (backgroundResId != 0) {
                    holder.itemTvIcon.setBackgroundResource(backgroundResId);
                }

                int iconBackgroundResId = shareIcon.iconBackgroudResId;
                if (iconBackgroundResId != 0) {
                    holder.itemTvIcon.setBackgroundResource(iconBackgroundResId);
                }
            }
            if (mOnItemClickListener != null) {
                holder.itemRoot.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemTvIcon, mIcons.get(position).platform);
                    }
                });
            }
        }

        public class ItemHolder extends RecyclerView.ViewHolder {

            ImageView itemIcon;
            IconFontTextView itemTvIcon;
            TextView itemTvText;
            LinearLayout itemRoot;

            public ItemHolder(View itemView) {
                super(itemView);
                itemIcon = itemView.findViewById(R.id.iv_share_item);
                itemTvIcon = itemView.findViewById(R.id.tv_share_item);
                itemTvText = itemView.findViewById(R.id.tv_share_item_text);
                itemRoot = itemView.findViewById(R.id.ll_share_item_root);
            }
        }

        public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        public interface OnItemClickListener {
            /**
             * 条目点击事件
             *
             * @param view 控件
             * @param snsPlatform 平台类型
             */
            void onItemClick(View view, int snsPlatform);
        }
    }
}
