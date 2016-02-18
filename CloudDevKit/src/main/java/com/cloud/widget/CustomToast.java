
package com.cloud.widget;

import android.content.Context;
import android.widget.Toast;

public class CustomToast
{

	private static Toast mToast;


	public static void makeText(Context mContext, String text, int duration)
	{
		Context def = mContext.getApplicationContext();
		mContext = null;

		if (mToast != null)
		{
			mToast.setText(text);
		}
		else
		{
			mToast = Toast.makeText(def, text, duration);
		}

		mToast.show();
	}

	public static void makeText(Context mContext, int resId, int duration)
	{
		Context def = mContext.getApplicationContext();
		mContext = null;
		makeText(def, def.getResources().getString(resId), duration);
	}

}
