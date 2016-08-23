package kr.latera.boonsu.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by jinwoo on 2016-08-23.
 */
public class Pie
{

	private static final int DURATION_1 = 1000;
	private static final int DURATION_2 = 500;

	private RectF r;
	private Paint p;
	private Paint p_bg;
	private Paint p_outline;
	private Paint p_proportion;
	private float degStart;
	private float degSweep;
	private int denom;
	private int num;
	private long lastDrawnTime;
	private long animStartTime_1;
	private long animStartTime_2;
	private boolean isBlinking_1 = false;
	private boolean isBlinking_2 = false;
	private int rep_cnt_1 = 30;
	private int rep_cnt_2 = 60;
	private int rep_val_1 = 0;
	private int rep_val_2 = 0;


	public Pie(RectF r, int color_bg, int color_num,float degStart, float degSweep)
	{
		this.r = r;
		p = new Paint();
		p.setStyle(Paint.Style.FILL);
		p.setColor(color_bg);
		p.setAntiAlias(true);
		p_outline = new Paint();
		p_outline.setStyle(Paint.Style.STROKE);
		p_outline.setColor(Color.BLACK);
		p_outline.setStrokeWidth(4);
		p_outline.setAntiAlias(true);
		p_bg = new Paint();
		p_bg.setStyle(Paint.Style.STROKE);
		p_bg.setColor(Color.BLACK);
		p_bg.setStrokeWidth(2);
		p_bg.setAntiAlias(true);
		p_proportion = new Paint();
		p_proportion.setStyle(Paint.Style.FILL);
		p_proportion.setColor(color_num);
		p_proportion.setAntiAlias(true);
		this.degStart = degStart;
		this.degSweep = degSweep;
	}

	public void setDenom(int denom)
	{
		this.denom = denom;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public void setFraction(int denom, int num)
	{
		int prev_den = this.denom;
		int prev_num = this.num;
		this.denom = denom;
		this.num = num;
		if(prev_den != this.denom)
		{
			// denominator has changed
			blinkOutline();
		}
		if(prev_num != this.num)
		{
			// numerator has changed
			blinkProportion();
		}
	}

	public void draw(Canvas c)
	{
		if(isBlinking_1) {updateOutlineAlpha();}
		if(isBlinking_2) {updateProportionAlpha();}
		c.drawArc(r, 0, 360, true, p_bg);
		c.drawArc(r, 0, 360, true, p);
		if(denom != 0)
		{
			float prop = 360f / denom; // angle of each part of pie

			c.drawArc(r, 270, prop * num, true, p_proportion);

			for(int i = 0 ; i < denom ; i ++)
			{
				c.drawArc(r, (270 + (prop * i)) % 360, prop, true, p_outline);
			}
		}
		lastDrawnTime = System.currentTimeMillis();
	}

	public void blinkOutline()
	{
		rep_val_1 = 0;
		isBlinking_1 = true;
		animStartTime_1 = System.currentTimeMillis();
	}

	public void blinkProportion()
	{
		rep_val_2 = 0;
		isBlinking_2 = true;
		animStartTime_2 = System.currentTimeMillis();
	}

	private  void updateOutlineAlpha()
	{
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - animStartTime_1;
		if(elapsedTime > DURATION_1) {
			isBlinking_1 = false; onEnd_1(); return; }
		float dpm = (1f * 255 * elapsedTime) / DURATION_1;
		p_outline.setAlpha((int)dpm);
	}

	private  void updateProportionAlpha()
	{
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - animStartTime_2;
		if(elapsedTime > DURATION_2) {
			isBlinking_2 = false; onEnd_2(); return; }
		float dpm = (1f * 255 * elapsedTime) / DURATION_2;
		p_proportion.setAlpha((int)dpm);
	}

	private void onEnd_1()
	{
		rep_val_1++;
		if(rep_cnt_1 == rep_val_1){return;}
		else
		{
			isBlinking_1 = true;
			animStartTime_1 = System.currentTimeMillis();
		}
	}

	private void onEnd_2()
	{
		rep_val_2++;
		if(rep_cnt_2 == rep_val_2){return;}
		else
		{
			isBlinking_2 = true;
			animStartTime_2 = System.currentTimeMillis();
		}
	}



}
