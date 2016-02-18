package com.cloud.util;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.BaseActivity;
import com.cloud.R;

/**
 * 提示布局，如加载中、无网络等通用性提示
 */
public class LoadingView
{

	private BaseActivity mActivity;
	private View mLoadingView;
	private ImageView mLoadingImgView;
	private TextView mTipTextView;
	private Boolean isShowing = false;

	public LoadingView(Context context)
	{
		this(context, true);
	}

	public LoadingView(Context context, boolean isAddToActivity)
	{
		if (context instanceof BaseActivity)
		{
			mActivity = (BaseActivity) context;

			mLoadingView = LayoutInflater.from(mActivity).inflate(R.layout.layout_loading, null);
			mLoadingView.setVisibility(View.GONE);

			mLoadingImgView = (ImageView) mLoadingView.findViewById(R.id.image);
			mTipTextView = (TextView) mLoadingView.findViewById(R.id.text);

			if (isAddToActivity)
			{
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				mActivity.addContentView(mLoadingView, params);
			}

		}
	}

	public Boolean isShowing()
	{
		return isShowing;
	}

	public void setTipInfo(int textResId, int drawableResId)
	{
		mLoadingView.setVisibility(View.VISIBLE);
		mLoadingImgView.setVisibility(View.GONE);
		mTipTextView.setVisibility(View.VISIBLE);

		mTipTextView.setText(textResId);
		mTipTextView.setCompoundDrawablesWithIntrinsicBounds(0, drawableResId, 0, 0);
	}

	public void hideTipInfo()
	{
		mLoadingView.setVisibility(View.GONE);
		mTipTextView.setVisibility(View.GONE);
	}

	public void showLoading()
	{
		isShowing = true;
		mLoadingView.setVisibility(View.VISIBLE);
		mLoadingImgView.setVisibility(View.VISIBLE);
		mTipTextView.setVisibility(View.GONE);

		if (mLoadingImgView.getBackground() instanceof AnimationDrawable)
		{
			AnimationDrawable anim = (AnimationDrawable) mLoadingImgView.getBackground(); // 获取ImageView背景,此时已被编译成AnimationDrawable
			anim.start(); // 开始动画
			return;
		}

		Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_loading_anim);
		mLoadingImgView.startAnimation(animation);
	}

	public void hideLoding()
	{
		isShowing = false;
		mLoadingView.setVisibility(View.GONE);
		mLoadingImgView.setVisibility(View.GONE);

		if (mLoadingImgView.getBackground() instanceof AnimationDrawable)
		{
			AnimationDrawable anim = (AnimationDrawable) mLoadingImgView.getBackground(); // 获取ImageView背景,此时已被编译成AnimationDrawable
			if (anim.isRunning())
			{ // 如果正在运行,就停止
				anim.stop();
			}
			return;
		}

		mLoadingImgView.clearAnimation();
	}

	public void setLoadingBackgroud(int backgroundResId)
	{
		mLoadingView.findViewById(R.id.layout_loading_layout).setBackgroundResource(backgroundResId);
	}

	public View getLodingView()
	{
		return mLoadingView;
	}

}
