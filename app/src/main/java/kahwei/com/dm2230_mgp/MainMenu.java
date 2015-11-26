package kahwei.com.dm2230_mgp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener
{
	private Button btn_start;
	private Button btn_help;
	private Button btn_options;
	private Button btn_quit;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// hide top navigation bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main_menu);

		// Set up event listeners for buttons
		btn_start = (Button)findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);
		btn_help = (Button)findViewById(R.id.btn_help);
		btn_help.setOnClickListener(this);
		btn_options = (Button)findViewById(R.id.btn_options);
		btn_options.setOnClickListener(this);
		btn_quit = (Button)findViewById(R.id.btn_quit);
		btn_quit.setOnClickListener(this);
	}

	// On Click Event Handler for this Activity
	public void onClick(View v)
	{
		Intent intent = new Intent();

		if (v == btn_start)
		{
			intent.setClass(this, GamePage.class);
		}
		else if (v == btn_help)
		{
			intent.setClass(this, HelpPage.class);
		}
		else if (v == btn_options)
		{
			intent.setClass(this, OptionPage.class);
		}
		else if (v == btn_quit)
		{
			// End this menu which ends the app
			finish();
		}

		try
		{
			startActivity(intent);
		}
		catch (Exception e)
		{
			// Prevent crashing for buttons that do nothing
		}
	}

	// Pause Handler
	protected void onPause()
	{
		super.onPause();
	}

	// Stop Handler
	protected void onStop()
	{
		super.onStop();
	}

	// Destroyed Handler
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
