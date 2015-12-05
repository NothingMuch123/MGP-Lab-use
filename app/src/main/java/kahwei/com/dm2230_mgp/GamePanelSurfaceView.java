package kahwei.com.dm2230_mgp;

/**
 * Created by xecli on 11/23/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.ListIterator;

import kahwei.com.dm2230_mgp.Object.Transform;
import kahwei.com.dm2230_mgp.Object.Vector3;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
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
	// 4b) Variable as an index to keep track of the spaceship images
	private Integer shipIndex = 0;
	private boolean m_attemptShoot = false;

	// Ship
	Ship m_ship;

	// Bullets
	private ArrayList<Bullet> m_bulletList;

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
		init(context);
	}

	public GamePanelSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public GamePanelSurfaceView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public void init(Context context)
	{
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

		// Initialize the Ship
		m_ship = new Ship();
		m_ship.Init(screenWidth * 0.5f, screenHeight * 0.85f, getResources());

		// Create the game loop thread
		myThread = new GameThread(getHolder(), this);

		// Initialize the bullet list
		m_bulletList = new ArrayList<Bullet>();

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
		canvas.drawBitmap(scaledBG, bgX, bgY - screenHeight, null);

		// Draw the bullets
		for (int b = 0; b < m_bulletList.size(); ++b)
		{
			Bullet bullet = m_bulletList.get(b);

			if (bullet.GetActive())
			{
				bullet.Draw(canvas);
			}
		}

		// 4d) Draw the spaceships
		m_ship.Draw(canvas);
	}


	//Update method to update the game play
	public void update(float dt, float fps)
	{
		FPS = fps;

		switch (GameState)
		{
			case 0:
			{
				// 3) Update the background to allow panning effect
				bgY += dt * BG_SCROLL_SPEED;

				// Once the first one has left the screen, reset it back
				if (bgY > screenHeight)
				{
					bgY = 0;
				}

				// Update the Ship
				m_ship.Update(dt);

				// Update the Shooting
				if (m_attemptShoot)
				{
					m_ship.Shoot(fetchBullet());
				}

				// Update all the bullets
				for (int b = 0; b < m_bulletList.size(); ++b)
				{
					Bullet bullet = m_bulletList.get(b);

					if (bullet.GetActive())
					{
						bullet.Update(dt);

						Vector3 bulletPos = bullet.GetTransform().GetTranslate();

						if (
							bulletPos.x < 0.0f || bulletPos.x > screenWidth
							||
							bulletPos.y < 0.0f || bulletPos.y > screenHeight
							)
						{
							bullet.SetActive(false);
						}
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

	public void Pause()
	{
		myThread.pause();
	}

	public void Unpause()
	{
		myThread.unPause();
	}

	private void movePlayer(float xPos, float yPos)
	{
		final float HIT_AREA_SCALER = 2;
		float touchHitAreaStartX = m_ship.GetPositionX() - m_ship.GetScaleX() * HIT_AREA_SCALER;
		float touchHitAreaStartY = m_ship.GetPositionY() - m_ship.GetScaleY()* HIT_AREA_SCALER;
		float touchHitAreaEndX = m_ship.GetPositionX() + m_ship.GetScaleX()* HIT_AREA_SCALER;
		float touchHitAreaEndY = m_ship.GetPositionY() + m_ship.GetScaleY()* HIT_AREA_SCALER;

		if (
				xPos >=  touchHitAreaStartX && xPos <= touchHitAreaEndX
               	&&
				yPos >= touchHitAreaStartY && yPos <= touchHitAreaEndY
			)
		{
			m_ship.SetPositionX(xPos);
			m_ship.SetPositionY(yPos - m_ship.GetScaleY());
		}
	}

	private Bullet fetchBullet()
	{
		for (Bullet b : m_bulletList)
		{
			if (b.GetActive() == false)
			{
				return b;
			}
		}

		// Not enough bullets
		final int BATCH_PRODUCE = 30;
		for (int b = 0; b < BATCH_PRODUCE; ++b)
		{
			Bullet bullet = new Bullet();
			bullet.Init(null, false, true);
			m_bulletList.add(bullet);
		}

		return m_bulletList.get(m_bulletList.size()-1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				movePlayer(event.getX(), event.getY());
				m_attemptShoot = true;
				return true;
			case MotionEvent.ACTION_UP:
				m_attemptShoot = false;
				return true;
		}

		return super.onTouchEvent(event);
	}
}
