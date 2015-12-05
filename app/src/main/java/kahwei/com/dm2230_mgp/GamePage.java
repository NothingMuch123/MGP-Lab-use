package kahwei.com.dm2230_mgp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by xecli on 11/23/2015.
 */
public class GamePage extends Activity implements View.OnClickListener
{
	private Button btn_pause;
	private Button btn_quit;
	private Button btn_resume;
	private Button btn_reset;
	private Button btn_options;
	private Button btn_confirmYes;
	private Button btn_confirmNo;
	private LinearLayout menu_confirm;
	private LinearLayout menu_pause;
	private TextView confirm_message;
	private GamePanelSurfaceView game_surface;
	// Confirmation Handling
	private boolean confirmIsQuit;

	// Options Pop Up
	private LinearLayout menu_option;
	private Button btn_option_back; // Back button
	private RadioButton btn_fastGraphics, btn_fancyGraphics; // Graphical radio buttons
	private SeekBar slider_musicVol; // Music volume slider
	private SeekBar slider_sfxVol; // SFX volume slider
	final Integer VOLUME_MAX = 100;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// hide top navigation bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Set the game page as the game view
		setContentView(R.layout.game_page);

		// Pause Menu
		menu_pause = (LinearLayout)findViewById(R.id.PauseMenu);
		btn_pause = (Button)findViewById(R.id.pause_button);
		btn_pause.setOnClickListener(this);
		btn_resume = (Button)findViewById(R.id.btn_resume);
		btn_resume.setOnClickListener(this);
		btn_quit = (Button)findViewById(R.id.btn_quit);
		btn_quit.setOnClickListener(this);
		btn_reset = (Button)findViewById(R.id.btn_reset);
		btn_reset.setOnClickListener(this);
		btn_options = (Button)findViewById(R.id.btn_options);
		btn_options.setOnClickListener(this);

		// Confirmation Menu
		menu_confirm = (LinearLayout)findViewById(R.id.ConfirmMenu);
		btn_confirmYes = (Button)findViewById(R.id.btn_confirmYes);
		btn_confirmYes.setOnClickListener(this);
		btn_confirmNo = (Button)findViewById(R.id.btn_confirmNo);
		btn_confirmNo.setOnClickListener(this);
		confirm_message = (TextView)findViewById(R.id.confirmMessage);

		// Option Menu
		// Get a handle to the Options Menu
		menu_option = (LinearLayout)findViewById(R.id.OptionsMenu);
		// Set up event listeners for buttons
		btn_option_back = (Button) findViewById(R.id.btn_option_back);
		btn_option_back.setOnClickListener(this);
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

		// The game itself
		game_surface = (GamePanelSurfaceView)findViewById(R.id.game_view);
	}

	private void pauseGame()
	{
		game_surface.Pause();
		menu_pause.setVisibility(View.VISIBLE);
	}

	public void onClick(View v)
	{
		// Main Menu Section
		if (v == btn_pause)
		{
			pauseGame();
		}
		else if (v == btn_resume)
		{
			game_surface.Unpause();
			menu_pause.setVisibility(View.GONE);
		}
		else if (v == btn_options)
		{
			menu_option.setVisibility(View.VISIBLE);
		}
		else if (v == btn_reset)
		{
			confirmIsQuit = false;
			confirm_message.setText("Reset Mission?");
			menu_confirm.setVisibility(View.VISIBLE);
		}
		else if (v == btn_quit)
		{
			confirmIsQuit = true;
			confirm_message.setText("Exit to Main Menu?");
			menu_confirm.setVisibility(View.VISIBLE);
		}
		// Confirm Menus
		else if (v == btn_confirmYes)
		{
			if (confirmIsQuit)
			{
				game_surface.Unpause();
				finish();
			}
			else
			{
				// TODO: Send flag to game for resetting
				game_surface.Unpause();
				menu_pause.setVisibility(View.GONE);
				menu_confirm.setVisibility(View.GONE);
			}
		}
		else if (v == btn_confirmNo)
		{
			// Close this menu
			menu_confirm.setVisibility(View.GONE);
		}
		// Options Menu
		else if (v == btn_option_back)
		{
			// Close this menu
			menu_option.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed()
	{
		// Ensure that the game was unpaused if returned to menu without unpausing, preventing a freeze
		game_surface.Unpause();
		super.onBackPressed();
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
	protected void onDestroy() {
		super.onDestroy();
	}
}
