package kahwei.com.dm2230_mgp;

/**
 * Created by xecli on 11/23/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	// Implement this interface to receive information about changes to the surface.

	private GameThread myThread = null; // Thread to control the rendering

	// 1a) Variables used for background rendering
	private Bitmap bg;
	private Bitmap scaledBG;
	// 1b) Define Screen width and Screen height as integer
	private Integer screenWidth;
	private Integer screenHeight;
	// 1c) Variables for defining background start and end point
	private short bgX = 0;
	private short bgY = 0;
	private final float BG_SCROLL_SPEED = 500;
	// 4a) bitmap array to stores 4 images of the spaceship
	final short NUM_SHIP_SPRITES = 4;
	private Bitmap[] spaceship = new Bitmap[NUM_SHIP_SPRITES];
	private final float SHIP_ANIM_SPEED = 200;
	private float shipAnim = 0.0f;
	// 4b) Variable as an index to keep track of the spaceship images
	private Integer shipIndex = 0;

	// Variables for FPS
	public float FPS;
	float deltaTime;
	long dt;

	// Variable for Game State check
	private short GameState = 0;

	//constructor for this GamePanelSurfaceView class
	public GamePanelSurfaceView (Context context)
	{
		// Context is the current state of the application/object
		super(context);

		// Adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// 1d) Set information to get screen size
		// -- Get the display info from the current context
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		// -- Store this info in the member variables
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		// 1e)load the image when this class is being instantiated
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene);
		// -- Load scaled version of the background
		scaledBG = Bitmap.createScaledBitmap(bg, screenWidth, screenHeight, true);

		// 4c) Load the images of the spaceships
		spaceship[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1);
		spaceship[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2);
		spaceship[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3);
		spaceship[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4);

		// Create the game loop thread
		myThread = new GameThread(getHolder(), this);

		// Make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	//must implement inherited abstract methods
	public void surfaceCreated(SurfaceHolder holder)
	{
		// Create the thread
		if (!myThread.isAlive())
		{
			myThread = new GameThread(getHolder(), this);
			myThread.startRun(true);
			myThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// Destroy the thread
		if (myThread.isAlive())
		{
			myThread.startRun(false);


		}
		boolean retry = true;
		while (retry)
		{
			try {
				myThread.join();
				retry = false;
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	// Not advised to touch as it is linked to GameThread
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	public void RenderGameplay(Canvas canvas)
	{
		// 2) Re-draw 2nd image after the 1st image ends
		// -- If there is no canvas to draw on, then don't do anything
		if (canvas == null)
		{
			return;
		}

		canvas.drawBitmap(scaledBG, bgX, bgY, null);
		canvas.drawBitmap(scaledBG, bgX + screenWidth, bgY, null);

		// 4d) Draw the spaceships
		canvas.drawBitmap(spaceship[shipIndex], screenWidth * 0.1f, screenHeight * 0.5f, null);

		// Bonus) To print FPS on the screen

	}


	//Update method to update the game play
	public void update(float dt, float fps)
	{
		FPS = fps;

		switch (GameState) {
			case 0:
			{
				// 3) Update the background to allow panning effect
				bgX -= dt * BG_SCROLL_SPEED;

				// Once the first one has left the screen, reset it back
				if (bgX < -screenWidth)
				{
					bgX = 0;
				}

				// 4e) Update the spaceship images / shipIndex so that the animation will occur.
				shipAnim += dt;
				if (shipAnim > SHIP_ANIM_SPEED)
				{
					shipAnim = 0.0f;
					shipIndex++;
					if (shipIndex >= NUM_SHIP_SPRITES)
					{
						shipIndex = 0;
					}
				}
			}
			break;
		}
	}

	// Rendering is done on Canvas
	public void doDraw(Canvas canvas)
	{
		switch (GameState)
		{
			case 0:
				RenderGameplay(canvas);
				break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		// 5) In event of touch on screen, the spaceship will relocate to the point of touch


		return super.onTouchEvent(event);
	}
}
