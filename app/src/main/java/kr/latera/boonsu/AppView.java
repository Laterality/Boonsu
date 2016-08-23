package kr.latera.boonsu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;

import kr.latera.boonsu.objects.Button;
import kr.latera.boonsu.objects.Pie;

/**
 * Created by jinwoo on 2016-08-19.
 */
public class AppView extends SurfaceView implements SurfaceHolder.Callback, TextWatcher
{

	private static final int BTN_SIZE = 48; // dp unit
	private static final int PIE_SIZE = 128; // dp unit


	private AppViewThread mThread;



	private int scrW;
	private int scrH;
	int b_size;
	int p_size;
	private boolean isMoving = false;

	private EditText etDenom1;
	private EditText etDenom2;
	private EditText etDenom3;

	private EditText etNum1;
	private EditText etNum2;
	private EditText etNum3;

	private Button btnPlus;
	private Button btnMinus;
	private Button btnMultiply;
	private Button btnDivision;
	private Button btnEqual;
	private Button t_btnDown;

	private Pie p1;
	private Pie p2;
	private Pie p3;

	private Paint p_white;
	private Paint p_black;

	private Rect rPlus;
	private Rect rMinus;
	private Rect rMultiply;
	private Rect rDivision;
	private Rect rEqual;
	private Rect rMoving;
	private Rect rOpIn1;
	private Rect rOpIn2;
	private RectF rPie1;
	private RectF rPie2;
	private RectF rPie3;

	private Bitmap bmPlus;
	private Bitmap bmMinus;
	private Bitmap bmMultiply;
	private Bitmap bmDivision;
	private Bitmap bmEqual;
	private Bitmap bmMoving;

	private int mX;
	private int mY;
	private int mDX;
	private int mDY;

	private long lastDrawnTime = -1;

	private int num1;
	private int num2;
	private int num3;
	private int denom1;
	private int denom2;
	private int denom3;


	public AppView(Context context)
	{
		super(context);
		init(context);
	}

	public void setEtView(EditText num1, EditText num2, EditText num3, EditText denom1, EditText denom2, EditText denom3)
	{
		etNum1 = num1;
		etNum2 = num2;
		etNum3 = num3;
		etDenom1 = denom1;
		etDenom2 = denom2;
		etDenom3 = denom3;

		// add textWatcher for react with change of values
		etNum1.addTextChangedListener(this);
		etNum2.addTextChangedListener(this);
		etNum3.addTextChangedListener(this);
		etDenom1.addTextChangedListener(this);
		etDenom2.addTextChangedListener(this);
		etDenom3.addTextChangedListener(this);
	}

	private void init(Context context)
	{
		getHolder().addCallback(this);
		mThread = new AppViewThread(getHolder(), this);

		p_white = new Paint();
		p_black  = new Paint();

		b_size = dpToPx(BTN_SIZE);
		p_size = dpToPx(PIE_SIZE);


		rPlus = new Rect();
		rMinus = new Rect();
		rMultiply = new Rect();
		rDivision = new Rect();
		rEqual = new Rect();
		rMoving = new Rect();
		rPie1 = new RectF();
		rPie2 = new RectF();
		rPie3 = new RectF();

		// load resources
		Resources r = context.getResources();
		bmPlus = BitmapFactory.decodeResource(r, R.drawable.add);
		bmMinus = BitmapFactory.decodeResource(r, R.drawable.minus);
		bmMultiply = BitmapFactory.decodeResource(r, R.drawable.unchecked);
		bmDivision = BitmapFactory.decodeResource(r, R.drawable.division);
		bmEqual = BitmapFactory.decodeResource(r, R.drawable.equal);

		btnPlus = new Button(bmPlus, rPlus);
		btnMinus = new Button(bmMinus, rMinus);
		btnMultiply = new Button(bmMultiply, rMultiply);
		btnDivision = new Button(bmDivision, rDivision);
		btnEqual = new Button(bmEqual, rEqual);

		p1 = new Pie(rPie1, Color.parseColor("#93F71C"), Color.parseColor("#F71CE5"), 0, 180);
		p2 = new Pie(rPie2, Color.parseColor("#1CF793"), Color.parseColor("#F7831C"), 0, 270);
		p3 = new Pie(rPie3, Color.parseColor("#0260E1"), Color.parseColor("#E1023C"), 0, 60);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		rPlus.set(scrW - (b_size *5), 0, scrW - (b_size *4), b_size);
		rMinus.set(scrW - (b_size *4), 0, scrW - (b_size *3), b_size);
		rMultiply.set(scrW - (b_size *3), 0, scrW - (b_size *2), b_size);
		rDivision.set(scrW - (b_size *2), 0, scrW - b_size, b_size);
		rEqual.set(scrW - b_size, 0, scrW, b_size);

		int pie_top = (scrH / 2) + dpToPx(16);
		int pie_gap = dpToPx(16);

		rPie1.set(dpToPx(16), pie_top, dpToPx(16) + p_size, pie_top + p_size);
		rPie2.set(rPie1.left + p_size + pie_gap, pie_top, rPie1.left + (p_size * 2) + pie_gap, pie_top + p_size);
		rPie3.set(rPie1.left + (p_size * 2) + (pie_gap * 2), pie_top, rPie1.left + (p_size * 3) + (pie_gap * 2), pie_top + p_size);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	public void doDraw(Canvas c)
	{
		if(c == null) return;
		scrW = getWidth();
		scrH = getHeight();

		p_white.setColor(Color.WHITE);
		p_white.setStyle(Paint.Style.FILL);
		p_black.setColor(Color.BLACK);
		p_black.setTextSize(32);
		p_black.setStyle(Paint.Style.FILL);
		p_black.setAntiAlias(true);




		c.drawColor(Color.WHITE); // clear screen

		// draw buttons
		btnPlus.draw(c);
		btnMinus.draw(c);
		btnMultiply.draw(c);
		btnDivision.draw(c);
		btnEqual.draw(c);


		if(isMoving &&
				mX > 0 &&
				mY > 0)
		{
			rMoving.set(mX - mDX, mY - mDY, mX - mDX + b_size, mY - mDY + b_size);
			c.drawBitmap(bmMoving, null, rMoving, null);
		} // draw moving operator img

		writeLog(c);

		// draw pies
		p1.draw(c);
		p2.draw(c);
		p3.draw(c);

		lastDrawnTime = System.currentTimeMillis();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		switch (e.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				onDown((int)e.getX(), (int)e.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				onMove((int)e.getX(), (int)e.getY());
				break;
			case MotionEvent.ACTION_UP:
				onUp((int)e.getX(), (int)e.getY());
				break;
		}

		return true;
	}

	public void onDown(int x, int y)
	{
		
		// Handling down action for buttons
		if(rPlus.contains(x, y))
		{
			isMoving = true;
			mDX = x - rPlus.left;
			mDY = y - rPlus.top;
			bmMoving = bmPlus;
			t_btnDown = btnPlus;
		}
		else if(rMinus.contains(x, y))
		{
			isMoving = true;
			mDX = x - rMinus.left;
			mDY = y - rMinus.top;
			bmMoving = bmMinus;
			t_btnDown = btnMinus;
		}
		else if(rMultiply.contains(x, y))
		{
			isMoving = true;
			mDX = x - rMultiply.left;
			mDY = y - rMultiply.top;
			bmMoving = bmMultiply;
			t_btnDown = btnMultiply;
		}
		else if(rDivision.contains(x, y))
		{
			isMoving = true;
			mDX = x - rDivision.left;
			mDY = y - rDivision.top;
			bmMoving = bmDivision;
			t_btnDown = btnDivision;
		}
		else if(rEqual.contains(x, y))
		{
			isMoving = true;
			mDX = x - rEqual.left;
			mDY = y - rEqual.top;
			bmMoving = bmEqual;
			t_btnDown = btnEqual;
		}
	}

	public void onMove(int x, int y)
	{
		mX = x;
		mY = y;
	}

	public void onUp(int x, int y)
	{
		isMoving = false;
		mX = -1;
		mY = -1;
		bmMoving = null;
		if(t_btnDown != null)
		{
			if(t_btnDown.contains(x, y))
			{
				t_btnDown.animateBlink();
			}
		}
		t_btnDown = null;
	}

	private void checkValues()
	{
		String s = etNum1.getText().toString();
		num1 = Integer.valueOf(s.isEmpty() ? "0" : s);
		s = etNum2.getText().toString();
		num2 = Integer.valueOf(s.isEmpty() ? "0" : s);
		s = etNum3.getText().toString();
		num3 = Integer.valueOf(s.isEmpty() ? "0" : s);
		s = etDenom1.getText().toString();
		denom1 = Integer.valueOf(s.isEmpty() ? "0" : s);
		s = etDenom2.getText().toString();
		denom2 = Integer.valueOf(s.isEmpty() ? "0" : s);
		s = etDenom3.getText().toString();
		denom3 = Integer.valueOf(s.isEmpty() ? "0" : s);

		p1.setFraction(denom1, num1);
		p2.setFraction(denom2, num2);
		p3.setFraction(denom3, num3);
	}

	private void writeLog(Canvas c)
	{
		int pd = dpToPx(32);
		String l = "moving : " + (isMoving ? "true" : "false") + "\n" +
				"numerators : " + num1 + ", " + num2 + ", " + num3 + "\n" +
				"denominators : " + denom1 + ", " + denom2 + ", " + denom3;

		c.drawText(l, pd, pd, p_black);
	}

	// Utility

	private int dpToPx(int dp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}


	// Overrides

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder)
	{
		mThread.setRunning(true);
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
	{

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder)
	{
		boolean retry = true;
		mThread.setRunning(false);

		while(retry)
		{
			try
			{
				mThread.join();
				retry = false;
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{
		checkValues();
	}

	@Override
	public void afterTextChanged(Editable editable)
	{

	}
}
