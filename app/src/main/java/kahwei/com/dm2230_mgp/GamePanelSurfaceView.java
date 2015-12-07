package kahwei.com.dm2230_mgp;

/**
 * Created by xecli on 11/23/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import kahwei.com.dm2230_mgp.Enemy.EnemyShip;
import kahwei.com.dm2230_mgp.Object.GameObject;
import kahwei.com.dm2230_mgp.Object.Vector3;
import kahwei.com.dm2230_mgp.PowerUp.AugmentPowerUp;
import kahwei.com.dm2230_mgp.PowerUp.LifePowerUp;
import kahwei.com.dm2230_mgp.PowerUp.PowerUp;
import kahwei.com.dm2230_mgp.PowerUp.RankPowerUp;
import kahwei.com.dm2230_mgp.Weapon.Bullet;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback		// Implement this interface to receive information about changes to the surface.
{
	// Thread to control the rendering
	private GameThread m_gameThread = null;

	// RNG
	Random m_rng = new Random();

	// Vibrator to Vibrate
	Vibrator m_vibrator;

	// Bitmaps
	private Bitmap bg;
	private Bitmap scaledBG;
	private Bitmap m_lifeTexture;
	private Bitmap m_powerUpTexture[];
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

	// Bullet buffer for sending to the ship
	ArrayList<Bullet> m_bulletBuffer;

	// Power up storage
	ArrayList<PowerUp>[] m_powerUpBuffer;

	// All GameObjects stored in this list including bullets and powerups
	private ArrayList<GameObject> m_goList;
	// Bullets
	private ArrayList<Bullet> m_bulletList;
	// Powerup List
	private ArrayList<PowerUp> m_powerUpList;
	// Enemy list
	private ArrayList<EnemyShip> m_enemyList;
	private ArrayList<Bullet> m_enemyBulletList;

	// Variable for spawning enemy
	static final float S_TIME_TILL_SPAWN_ENEMY = 2.f;
	float m_enemySpawnTimer;

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

		// Load life bitmap
		m_lifeTexture = BitmapFactory.decodeResource(getResources(), R.drawable.life);
		// Generate the scaled version of this the life bitmap
		m_lifeTexture = Bitmap.createScaledBitmap(m_lifeTexture, 100, 100, true);

		// Load Power up Bitmaps
		m_powerUpTexture = new Bitmap[PowerUp.Type.values().length + Ship.PowerType.values()
				.length - 1];
		m_powerUpTexture[PowerUp.Type.PU_LIFE.ordinal()] = m_lifeTexture;
		m_powerUpTexture[PowerUp.Type.PU_RANK.ordinal()] = BitmapFactory.decodeResource
				(getResources(), R.drawable.rank);
		m_powerUpTexture[PowerUp.Type.PU_AUGMENT.ordinal() + Ship.PowerType.PT_NORMAL.ordinal()] =
				BitmapFactory.decodeResource(getResources(), R.drawable.normal_augment);
		m_powerUpTexture[PowerUp.Type.PU_AUGMENT.ordinal() + Ship.PowerType.PT_BEAM.ordinal()] =
				BitmapFactory.decodeResource(getResources(), R.drawable.beam_augment);
		m_powerUpTexture[PowerUp.Type.PU_AUGMENT.ordinal() + Ship.PowerType.PT_SPIKE.ordinal()] =
				BitmapFactory.decodeResource(getResources(), R.drawable.spike_augment);

		// Initialize the Ship
		m_ship = new Ship();
		Vector3 shipPos = new Vector3();
		shipPos.Set(screenWidth * 0.5f, screenHeight * 0.85f);
		m_ship.Init(shipPos, getResources());

		// Create the game loop thread
		m_gameThread = new GameThread(getHolder(), this);

		// Initialize the bulletbuffer
		m_bulletBuffer = new ArrayList<Bullet>();

		// Initialize game object list
		m_goList = new ArrayList<GameObject>();

		// Initialize power up buffer
		m_powerUpBuffer = new ArrayList[PowerUp.Type.values().length];
		// Initialize each buffer for each power up
		for (int buffer = 0; buffer < m_powerUpBuffer.length; ++buffer)
		{
			m_powerUpBuffer[buffer] = new ArrayList<PowerUp>();
		}
		// Add power ups to the buffer
		for (int p = 0; p < 5; ++p)
		{
			PowerUp pwup;

			pwup = new LifePowerUp();
			pwup.Reset();
			m_powerUpBuffer[PowerUp.Type.PU_LIFE.ordinal()].add(pwup);
			m_goList.add(pwup);

			pwup = new RankPowerUp();
			pwup.Reset();
			m_powerUpBuffer[PowerUp.Type.PU_RANK.ordinal()].add(pwup);
			m_goList.add(pwup);

			pwup = new AugmentPowerUp();
			pwup.Reset();
			m_powerUpBuffer[PowerUp.Type.PU_AUGMENT.ordinal()].add(pwup);
			m_goList.add(pwup);
		}

		// Initialize the bullet list
		m_bulletList = new ArrayList<Bullet>();

		// Initialize the power up list
		m_powerUpList = new ArrayList<PowerUp>();

		// TODO: Remove this testing code for lives
		PowerUp pwup = new RankPowerUp();
		pwup.Init(BitmapFactory.decodeResource(getResources(), R.drawable.rank), true, true,
				new Vector3(screenWidth * 0.8f, screenHeight * 0.3f, 0.0f),
				new Vector3(-25.0f, 25.0f, 0.0f));
		m_goList.add(pwup);
		m_powerUpList.add(pwup);

		pwup = new RankPowerUp();
		pwup.Init(BitmapFactory.decodeResource(getResources(), R.drawable.rank), true, true,
				new Vector3(screenWidth * 0.8f, screenHeight * 0.8f, 0.0f),
				new Vector3(-25.0f, 25.0f, 0.0f));
		m_goList.add(pwup);
		m_powerUpList.add(pwup);

		pwup = new LifePowerUp();
		pwup.Init(BitmapFactory.decodeResource(getResources(), R.drawable.life), true, true,
				new Vector3(screenWidth * 0.2f, screenHeight * 0.4f, 0.0f),
				new Vector3(25.0f, 50.0f, 0.0f));
		m_goList.add(pwup);
		m_powerUpList.add(pwup);

		AugmentPowerUp apwup = new AugmentPowerUp();
		apwup.Init(BitmapFactory.decodeResource(getResources(), R.drawable.spike_augment), true, true,
				new Vector3(screenWidth * 0.5f, screenHeight * 0.5f, 0.0f),
				new Vector3(25.0f, 50.0f, 0.0f),
				Ship.PowerType.PT_SPIKE);
		m_goList.add(apwup);
		m_powerUpList.add(apwup);

		apwup = new AugmentPowerUp();
		apwup.Init(BitmapFactory.decodeResource(getResources(), R.drawable.beam_augment), true, true,
				new Vector3(screenWidth * 0.8f, screenHeight * 0.1f, 0.0f),
				new Vector3(-25.0f, 50.0f, 0.0f),
				Ship.PowerType.PT_BEAM);
		m_goList.add(apwup);
		m_powerUpList.add(apwup);

		// Initialize the enemy list
		EnemyShip.CreateEnemyMesh(getResources());
		m_enemyList = new ArrayList<EnemyShip>();
		m_enemyBulletList = new ArrayList<Bullet>();

		// Data to spawn enemy
		m_enemySpawnTimer = 0.f;

		// Make the GamePanel focusable so it can handle events
		setFocusable(true);

		// Set up the Vibrator
		m_vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
	}

	//must implement inherited abstract methods
	public void surfaceCreated(SurfaceHolder holder)
	{
		// Create the thread
		if (!m_gameThread.isAlive())
		{
			m_gameThread = new GameThread(getHolder(), this);
			m_gameThread.startRun(true);
			m_gameThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// Destroy the thread
		if (m_gameThread.isAlive())
		{
			m_gameThread.startRun(false);


		}
		boolean retry = true;
		while (retry)
		{
			try {
				m_gameThread.join();
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

		// Draw game objects
		for (int go = 0; go < m_goList.size(); ++go)
		{
			GameObject gameObject = m_goList.get(go);

			if (gameObject.GetActive())
			{
				gameObject.Draw(canvas);
			}
		}

		// Draw the enemies
		for (int e = 0; e < m_enemyList.size(); ++e)
		{
			EnemyShip enemy = m_enemyList.get(e);
			if (enemy.GetRender())
			{
				enemy.Draw(canvas);
			}
		}

		// 4d) Draw the spaceships
		m_ship.Draw(canvas);

		// Draw the health
		for (int lives = 1; lives <= m_ship.GetHealth(); ++lives)
		{
			canvas.drawBitmap(m_lifeTexture, screenWidth - m_lifeTexture.getWidth() * lives, 0, null);
		}
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

				// Spawn some power ups
				//spawnPowerUp();

				// Update the Ship
				m_ship.Update(dt);

				// Spawning enemies
				if (m_enemySpawnTimer <= 0.f)
				{
					spawnEnemy();
					m_enemySpawnTimer = S_TIME_TILL_SPAWN_ENEMY;
				}
				else
				{
					m_enemySpawnTimer -= dt;
				}

				// Update the Shooting
				if (m_attemptShoot)
				{
					// Collect 5 bullets to be used
					for (int i = 0; i < 5; ++i)
					{
						m_bulletBuffer.add(fetchBullet());
					}
					m_ship.Shoot(m_bulletBuffer);
					m_bulletBuffer.clear();
				}

				// Enemy shoot
				for (int enemy = 0; enemy < m_enemyList.size(); ++enemy)
				{
					EnemyShip e = m_enemyList.get(enemy);
					m_bulletBuffer.add(fetchEnemyBullet());
					e.Shoot(m_bulletBuffer);
					m_bulletBuffer.clear();
				}

				// Update all the gameobjects
				for (int b = 0; b < m_goList.size(); ++b)
				{
					GameObject gameObject = m_goList.get(b);

					if (gameObject.GetActive())
					{
						gameObject.Update(dt);

						Vector3 goPos = gameObject.GetTransform().m_translate;
						Vector3 goScale = gameObject.GetTransform().m_scale;

						// Check for out of screen
						if (
							goPos.x < -goScale.x || goPos.x > screenWidth + goScale.x
							||
							goPos.y < -goScale.y * 2 || goPos.y > screenHeight + goScale.y
							)
						{
							gameObject.SetActive(false);
						}

						// Do collision checking
						if (m_ship.CollideWith(gameObject, dt))
						{
							if (gameObject instanceof PowerUp)
							{
								((PowerUp)gameObject).AffectShip(m_ship);
							}
							else
							{
								System.out.print("cmi");
							}
						}
					}
				}

				// Update all enemies
				for (int e = 0; e < m_enemyList.size(); ++e)
				{
					EnemyShip enemy = m_enemyList.get(e);
					if (enemy.GetActive())
					{
						enemy.Update(dt, screenWidth, screenHeight);

						// Check Collision with Bullets
						for (int bullet = 0; bullet < m_bulletList.size(); ++bullet)
						{
							Bullet b = m_bulletList.get(bullet);
							if (b.GetActive())
							{
								if (b.CollideWith(enemy, dt))
								{
									// Kill enemy
									enemy.Reset();
									// Kill bullet
									b.Reset();
									// Stop checking, no point coz it's dead
									break;
								}
							}
						}
					}
				}

				// Check for player collision with enemy bullets
				for (int eB = 0; eB < m_enemyBulletList.size(); ++eB)
				{
					Bullet bullet = m_enemyBulletList.get(eB);
					if (bullet.GetActive())
					{
						if (m_ship.CollideWith(bullet, dt))
						{
							// Vibrate
							m_vibrator.vibrate(100);
							// Hurt the player
							m_ship.Kill();
							// Destroy the bullet
							bullet.Reset();
							// Player is killed, no point checking again
							break;
						}
					}
				}

				// Kill the game if player lost
				if (!m_ship.IsAlive())
				{
					// TODO: Go to a proper losing screen
					Activity parentActivity = (Activity)getContext();
					parentActivity.finish();
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
		m_gameThread.pause();
	}

	public void Unpause()
	{
		m_gameThread.unPause();
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
			m_attemptShoot = true;
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
				b.SetActive(true);
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
			m_goList.add(bullet);
		}

		Bullet b = m_bulletList.get(m_bulletList.size()-1);
		b.SetActive(true);
		return b;
	}

	private Bullet fetchEnemyBullet()
	{
		for (Bullet b : m_enemyBulletList)
		{
			if (b.GetActive() == false)
			{
				b.SetActive(true);
				return b;
			}
		}

		// Not enough bullets
		final int BATCH_PRODUCE = 30;
		for (int b = 0; b < BATCH_PRODUCE; ++b)
		{
			Bullet bullet = new Bullet();
			bullet.Init(null, false, true);
			m_enemyBulletList.add(bullet);
			m_goList.add(bullet);
		}

		Bullet b = m_enemyBulletList.get(m_enemyBulletList.size()-1);
		b.SetActive(true);
		return b;
	}

	private EnemyShip fetchEnemy()
	{
		for (EnemyShip e : m_enemyList)
		{
			if (e.GetActive() == false)
			{
				return e;
			}
		}

		// Not enough enemies
		final int BATCH_PRODUCE = 10;
		for (int b = 0; b < BATCH_PRODUCE; ++b)
		{
			EnemyShip e = new EnemyShip();
			//e.Init(screenWidth, screenHeight, getResources());
			m_enemyList.add(e);
			m_goList.add(e);
		}

		return m_enemyList.get(m_enemyList.size() - 1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				movePlayer(event.getX(), event.getY());
				return true;
			case MotionEvent.ACTION_UP:
				m_attemptShoot = false;
				return true;
		}

		return super.onTouchEvent(event);
	}

	private boolean spawnEnemy()
	{
		EnemyShip e = fetchEnemy(); // Fetch an empty enemy
		e.Init(screenWidth, screenHeight, getResources()); // Create an enemy
		e.InitWeapon(1.f, getResources());
		return true;
	}

	private void spawnPowerUp()
	{
		// Should we spawn something this cycle?
		final int SPAWN_CHANCE = 40;
		int chance = m_rng.nextInt(100);
		if (chance > SPAWN_CHANCE)
		{
			// Don't spawn
			return;
		}

		PowerUp.Type typeToSpawn;
		System.out.println(chance);
		// Choose what to spawn
		if (chance > 0)
		{
			typeToSpawn = PowerUp.Type.PU_LIFE;
		}
		else if (chance > 70)
		{
			typeToSpawn = PowerUp.Type.PU_RANK;
		}
		else
		{
			typeToSpawn = PowerUp.Type.PU_AUGMENT;
		}

		PowerUp powerUpToSpawn = null;
		powerUpToSpawn = fetchPowerUp(typeToSpawn);

		if (powerUpToSpawn != null)
		{
			// Calculate random values
			Vector3 randPos = new Vector3(-200.0f, screenWidth * m_rng.nextFloat(), 0.0f);
			float randSpeed = 100.0f + (m_rng.nextFloat() * 100.0f);
			float xVelFactor =  randSpeed * m_rng.nextFloat();
			Vector3 randVel = new Vector3(xVelFactor, randSpeed - xVelFactor, 0.0f);

			if (typeToSpawn == PowerUp.Type.PU_AUGMENT)
			{
				// Choose the augment to spawn
				Ship.PowerType augment = Ship.PowerType.values()[m_rng.nextInt(Ship.PowerType.values().length)];
				// Use the custom spawner for AugmentPowerUp
				((AugmentPowerUp)powerUpToSpawn).Init(m_powerUpTexture[typeToSpawn.ordinal() +
								augment.ordinal()],
						true,
						true, randPos,
						randVel, augment);
			}
			else
			{
				// Spawn the power
				powerUpToSpawn.Init(m_powerUpTexture[typeToSpawn.ordinal()], true, true, randPos,
						randVel);
			}
		}
	}

	private PowerUp fetchPowerUp(PowerUp.Type type)
	{
		for (PowerUp pwup : m_powerUpBuffer[type.ordinal()])
		{
			if (pwup.GetActive() == false)
			{
				pwup.SetActive(true);
				return pwup;
			}
		}

		return null;
	}
}
