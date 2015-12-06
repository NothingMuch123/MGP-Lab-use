package kahwei.com.dm2230_mgp.Menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import kahwei.com.dm2230_mgp.R;


public class HelpPage extends Activity implements View.OnClickListener
{
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // hide top navigation bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.help_page);

        // Set up event listeners for buttons
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }

    // On Click Event Handler for this Activity
    public void onClick(View v)
    {
        Intent intent = new Intent();

        if (v == btn_back)
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
