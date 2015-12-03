package kahwei.com.dm2230_mgp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
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
		btn_confirmYes = (Button)findViewById(R.id.btn_confirmYes);
		btn_confirmYes.setOnClickListener(this);
		btn_confirmNo = (Button)findViewById(R.id.btn_confirmNo);
		btn_confirmNo.setOnClickListener(this);
		menu_pause = (LinearLayout)findViewById(R.id.PauseMenu);
		menu_confirm = (LinearLayout)findViewById(R.id.ConfirmMenu);
		confirm_message = (TextView)findViewById(R.id.confirmMessage);
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
			// TODO
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
