package kr.latera.boonsu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FrameLayout flSurface = (FrameLayout) findViewById(R.id.fl_surface_frame);
		AppView av = new AppView(this);
		av.setEtView(
				(EditText)findViewById(R.id.et_num1),
				(EditText)findViewById(R.id.et_num2),
				(EditText)findViewById(R.id.et_num3),
				(EditText)findViewById(R.id.et_denom1),
				(EditText)findViewById(R.id.et_denom2),
				(EditText)findViewById(R.id.et_denom3)
		);
		flSurface.addView(av);
//		setContentView(new AppView(this));
	}
}
