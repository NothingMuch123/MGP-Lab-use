package kahwei.com.dm2230_mgp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by xecli on 11/30/2015.
 */
public class SpriteAnimation
{
	public Bitmap getBitmap()
	{
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}

	public Rect getSourceRect()
	{
		return sourceRect;
	}

	public void setSourceRect(Rect sourceRect)
	{
		this.sourceRect = sourceRect;
	}

	public Integer getFrame()
	{
		return frame;
	}

	public void setFrame(Integer frame)
	{
		this.frame = frame;
	}

	public Integer getCurrentFrame()
	{
		return currentFrame;
	}

	public void setCurrentFrame(Integer currentFrame)
	{
		this.currentFrame = currentFrame;
	}

	public long getFrameTicker()
	{
		return frameTicker;
	}

	public void setFrameTicker(long frameTicker)
	{
		this.frameTicker = frameTicker;
	}

	public Integer getFramePeriod()
	{
		return framePeriod;
	}

	public void setFramePeriod(Integer framePeriod)
	{
		this.framePeriod = framePeriod;
	}

	public Integer getSpriteWidth()
	{
		return spriteWidth;
	}

	public void setSpriteWidth(Integer spriteWidth)
	{
		this.spriteWidth = spriteWidth;
	}

	public Integer getSpriteHeight()
	{
		return spriteHeight;
	}

	public void setSpriteHeight(Integer spriteHeight)
	{
		this.spriteHeight = spriteHeight;
	}

	public Integer getX()
	{
		return x;
	}

	public void setX(Integer x)
	{
		this.x = x;
	}

	public Integer getY()
	{
		return y;
	}

	public void setY(Integer y)
	{
		this.y = y;
	}

	private Bitmap bitmap;                // The animation sequence
	private Rect sourceRect;            // The rectangle to be drawn from the animation bitmap
	private Integer frame;                // Number of frames in animation
	private Integer currentFrame;        // The current frae
	private long frameTicker;            // The time of the last frame update
	private Integer framePeriod;        // Milliseconds between each frame (1000/fps)

	private Integer spriteWidth;        // The width of the sprite to calculate the cut out
	// rectangle
	private Integer spriteHeight;        // The height of the sprite

	private Integer x;                    // The X coordinate of the object (top left of image)
	private Integer y;                    // The Y coordinate of the object (top left of image_

	public SpriteAnimation(Bitmap bitmap, int x, int y, int fps, int frameCount)
	{
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;

		currentFrame = 0;

		frame = frameCount;

		spriteWidth = bitmap.getWidth() / frameCount;
		spriteHeight = bitmap.getHeight();

		sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

		framePeriod = 1000 / fps;
		frameTicker = 01;
	}

	public void update(long gameTime)	// gameTime = system time from the thread which is running
	{
		if (gameTime > frameTicker + framePeriod)
		{
			frameTicker = gameTime;
			currentFrame++;

			if (currentFrame >= frame)
			{
				currentFrame = 0;
			}
		}

		// define the rectangle to cut out sprite
		this.sourceRect.left = currentFrame * spriteWidth;
		this.sourceRect.right = this.sourceRect.left + spriteWidth;
	}

	public void draw(Canvas canvas)
	{
		// Image of each frame is define dby sourceRect
		// destRect is the area defined for the image of each frame to be drawn
		Rect destRect = new Rect(getX(), getY(), getX() + spriteWidth, getY() + spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}
}