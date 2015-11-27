package kahwei.com.dm2230_mgp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

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
	private LinearLayout menu_pause;
	private GamePanelSurfaceView game_surface;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// hide top navigation bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.game_page);
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
		menu_pause = (LinearLayout)findViewById(R.id.PauseMenu);
		game_surface = (GamePanelSurfaceView)findViewById(R.id.game_view);
	}

	public void onClick(View v)
	{
		// Main Menu Section
		if (v == btn_pause)
		{
			game_surface.Pause();
			menu_pause.setVisibility(View.VISIBLE);
		}
		else if (v == btn_resume)
		{
			game_surface.Unpause();
			menu_pause.setVisibility(View.GONE);
		}
		else if (v == btn_options)
		{
			// TODO
		}
		else if (v == btn_reset)
		{
			// Send flag to game for resetting
			game_surface.Unpause();
			menu_pause.setVisibility(View.GONE);
		}
		else if (v == btn_quit)
		{
			game_surface.Unpause();
			finish();
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
