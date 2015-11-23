package kahwei.com.dm2230_mgp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SplashPage extends Activity
{
	protected Boolean _active = true;
	protected Integer _splashTime = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// hide top navigation bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.splash_page);

		//thread for displaying the Splash Screen
		Thread splashTread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Integer waited = 0;
					while(_active && (waited < _splashTime))
					{
						sleep(200);
						if(_active)
						{
							waited += 200;
						}
					}
				}
				catch(InterruptedException e)
				{
					//do nothing
				}
				finally
				{
					finish();

					// Create a new intent to go to the MainMenu
					Intent intent = new Intent(SplashPage.this, MainMenu.class);
					// Start the intent
					startActivity(intent);
				}
			}
		};

		// Start the thread
		splashTread.start();
	}

	public Boolean OnTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			_active = false;
		}

		return true;
	}
}
