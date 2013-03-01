package client;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Projectile {

	private float		width, height;
	private float		startx, starty, distance;
	private float		decaytime	= 3 * 1000;

	private boolean		destroy;

	private Vector2f	direction;
	private Shape		hitbox;

	public Projectile(float x, float y, float width, float height,
			Vector2f direction)
	{
		this.startx = x - width / 2;
		this.starty = y - height / 2;
		this.width = width;
		this.height = height;

		distance = direction.lengthSquared();

		this.direction = direction.copy().normalise();

		this.hitbox = new Rectangle(startx, starty, width, height);

		hitbox = hitbox
				.transform(Transform.createRotateTransform(
						(float) direction.getTheta(), hitbox.getCenterX(),
						hitbox.getCenterY()));
	}

	public void render(Graphics g)
	{
		//g.setColor(new Color(1F, 1F, 1F, decaytime / 3000F));
		g.draw(hitbox);
	}

	public void update(Input input, int delta)
	{
		//this.decaytime -= delta;

		if (decaytime <= 0)
		{
			//this.destroy();
		} else
		{
			move(direction.x * delta, direction.y * delta);
		}
	}

	private void move(float dx, float dy)
	{
		if (keepMoving())
		{
			this.hitbox.setX(hitbox.getX() + dx);
			this.hitbox.setY(hitbox.getY() + dy);
		}
	}

	private boolean keepMoving()
	{
		float x = this.hitbox.getCenterX() - startx;
		float y = this.hitbox.getCenterY() - starty;

		return (x * x + y * y < distance);
	}

	public void destroy()
	{
		destroy = true;
	}

	public boolean isDestroyed()
	{
		return destroy;
	}
}
