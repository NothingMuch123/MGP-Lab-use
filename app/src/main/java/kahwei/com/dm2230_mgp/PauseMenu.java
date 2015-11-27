package kahwei.com.dm2230_mgp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class PauseMenu extends Activity implements View.OnClickListener
{
    private Button btn_resume, btn_reset, btn_options, btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide top navigation bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.pause_menu);

        // Set up event listeners for buttons
        btn_resume = (Button)findViewById(R.id.btn_resume);
        btn_resume.setOnClickListener(this);
        btn_reset = (Button)findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);
        btn_options = (Button)findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);
        btn_exit = (Button)findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
    }

    // On Click Event Handler for this Activity
    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_options)
        {
            // Pop up options menu
        }
        else if (v == btn_resume)
        {
            finish();
        }
        else if (v == btn_reset)
        {
            // Send flag to game for resetting
        }
        else if (v == btn_exit)
        {
            // Send flag to game to finish
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
