package kahwei.com.dm2230_mgp.Menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;

import kahwei.com.dm2230_mgp.R;

public class MainMenu extends Activity implements OnClickListener, SeekBar.OnSeekBarChangeListener
{
	// Main Menu
	private Button btn_start;
	private Button btn_help;
	private Button btn_options;
	private Button btn_quit;

	// Options Pop Up
	private LinearLayout layout_options;
	private Button btn_back; // Back button
	private RadioButton btn_fastGraphics, btn_fancyGraphics; // Graphical radio buttons
	private SeekBar slider_musicVol; // Music volume slider
	private SeekBar slider_sfxVol; // SFX volume slider
	final Integer VOLUME_MAX = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// hide top navigation bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main_menu);

		/*
		 * Main Menu
		 */
		// Set up event listeners for buttons
		btn_start = (Button)findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);
		btn_help = (Button)findViewById(R.id.btn_help);
		btn_help.setOnClickListener(this);
		btn_options = (Button)findViewById(R.id.btn_options);
		btn_options.setOnClickListener(this);
		btn_quit = (Button)findViewById(R.id.btn_quit);
		btn_quit.setOnClickListener(this);

		/*
		 * Options Pop Up
		 */
		// Get a handle to the Options Layout
		layout_options = (LinearLayout)findViewById((R.id.OptionsMenu));
		// Set up event listeners for buttons
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_fastGraphics = (RadioButton) findViewById(R.id.btn_fast);
		btn_fastGraphics.setOnClickListener(this);
		btn_fancyGraphics = (RadioButton) findViewById(R.id.btn_fancy);
		btn_fancyGraphics.setOnClickListener(this);
		btn_fancyGraphics.setChecked(true);

		// Set up event listeners for sliders
		slider_musicVol = (SeekBar)findViewById(R.id.slider_musicVol);
		slider_musicVol.setOnClickListener(this);
		slider_sfxVol = (SeekBar)findViewById(R.id.slider_sfxVol);
		slider_sfxVol.setOnClickListener(this);

		// Assign seekbar default values and max value
		slider_musicVol.setMax(VOLUME_MAX);
		slider_musicVol.setProgress(VOLUME_MAX);
		slider_sfxVol.setMax(VOLUME_MAX);
		slider_sfxVol.setProgress(VOLUME_MAX);
	}

	// On Click Event Handler for this Activity
	public void onClick(View v)
	{
		Intent intent = new Intent();

		// Main Menu Section
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
			layout_options.setVisibility(View.VISIBLE);
		}
		else if (v == btn_quit)
		{
			// End this menu which ends the app
			finish();
		}
		// Options menu Section
		else if (v == btn_back)
		{
			// Hide this menu
			layout_options.setVisibility(View.GONE);
		}
		else if (v == btn_fancyGraphics)
		{
			// Set fancy graphics
		}
		else if (v == btn_fastGraphics)
		{
			// Set fast graphics
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser)
		{
			seekBar.setProgress(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

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
