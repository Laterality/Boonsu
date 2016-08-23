package kr.latera.boonsu.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by jinwoo on 2016-08-21.
 */
public class Button
{
	private static final String TAG = "ButtonObj";
	private static final int duration = 3000;
	private Bitmap bm;
	private Rect r;
	private Paint p;
	private boolean anim = false;
	private long lastDrawnTime = 0;
	private long animStartTime = 0;

	public Button(Bitmap img, Rect r)
	{
		this.bm = img;
		this.r = r;
		p = new Paint();
		p.setStyle(Paint.Style.STROKE);
	}

	public void draw(Canvas c)
	{
		lastDrawnTime = System.currentTimeMillis();
		updateAlphaAnimation();
		c.drawBitmap(bm, null , r, anim ? p : null);
		c.drawRect(r, p);
	}

	public void animateBlink()
	{
		anim = true;
		animStartTime = System.currentTimeMillis();
	}

	public boolean contains(int x, int y)
	{
		return r.contains(x, y);
	}

	private  void updateAlphaAnimation()
	{
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - animStartTime;
		if(elapsedTime > duration) {anim = false; return;}
		float dpm = (1f * 255 * elapsedTime) / duration ;
//		Log.d(TAG, "dpm : " + dpm);
		p.setAlpha((int)dpm);
	}
}
