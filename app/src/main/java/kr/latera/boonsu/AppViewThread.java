package kr.latera.boonsu;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.SurfaceHolder;

/**
 * Created by jinwoo on 2016-08-19.
 */
public class AppViewThread extends Thread
{

	private static final int FPS = 30;
	private SurfaceHolder mHolder;
	private AppView mView;
	private boolean mRun = false;


	public AppViewThread(SurfaceHolder holder, AppView view)
	{
		mHolder = holder;
		mView = view;
	}

	public void setRunning(boolean run)
	{
		mRun = run;
	}


	@Override
	public void run()
	{
		int interval = 1000 / FPS;
		Canvas c;
		while(mRun)
		{
			c = null;
			try
			{
				c = mHolder.lockCanvas(null);
				synchronized (mHolder)
				{
					mView.doDraw(c);
					sleep(interval);
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(c != null)
				{
					mHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}
