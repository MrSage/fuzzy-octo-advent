package client;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

public class Player {

	private float		xspeed	= .25F, yspeed = .25F;
	private double		theta;
	private boolean		left, right, up, down;

	private Vector2f	playerdirection;
	private Shape		newhitbox;
	private Shape		hitbox;

	public Player()
	{
		this.hitbox = new Rectangle(50, 50, 25, 25);

		this.setX(Game.WIDTH / 2 - hitbox.getWidth() / 2);
		this.setY(Game.HEIGHT / 2 - hitbox.getHeight() / 2);

		playerdirection = new Vector2f(0, 1);
	}

	public void render(Graphics g)
	{
		g.setColor(Color.white);

		//g.draw(hitbox);
		g.draw(newhitbox);
		g.drawLine(hitbox.getCenterX(), hitbox.getCenterY(), playerdirection.x
				+ hitbox.getCenterX(), playerdirection.y + hitbox.getCenterY());

		g.drawLine(hitbox.getCenterX() + dx, hitbox.getCenterY(),
				hitbox.getCenterX() + dx, hitbox.getCenterY() + dy);
		g.drawLine(hitbox.getCenterX(), hitbox.getCenterY(),
				hitbox.getCenterX() + dx, hitbox.getCenterY());

		g.drawString("" + dy, hitbox.getCenterX() + dx, hitbox.getCenterY()
				+ dy / 2);
		g.drawString("" + dx, hitbox.getCenterX() + dx / 2, hitbox.getCenterY());

		g.fillOval(Game.WIDTH / 2 - 2, Game.HEIGHT / 2 - 2, 4, 4);
		g.drawString(
				"Player coords: (" + hitbox.getCenterX() + ", "
						+ hitbox.getCenterY() + ")", 25, 50);
		g.drawString("Mouse coords: " + Mouse.getX() + ", "
				+ (Game.HEIGHT - Mouse.getY()), 25, 35);

		g.drawString("Player direction: " + playerdirection.x + " "
				+ playerdirection.y, 25, 65);

		g.drawString("Theta: " + theta + " " + theta * 180 / Math.PI, 25, 80);
		g.drawString("Components: " + dy + "y " + dx + "x", 25, 95);
	}

	public void update(Input input, int delta)
	{
		playerdirection = calculateDirectionVector(input);

		Vector3f playerdirectionas3f = new Vector3f(playerdirection.x,
				playerdirection.y, 0);
		Vector3f horizon = new Vector3f(1, 0, 0);

		theta = convertTheta(playerdirectionas3f, horizon);

		newhitbox = hitbox.transform(Transform.createRotateTransform(
				(float) theta, hitbox.getCenterX(), hitbox.getCenterY()));

		setDirections();

		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN))
		{
			this.setX(Game.WIDTH / 2 - hitbox.getWidth() / 2);
			this.setY(Game.HEIGHT / 2 - hitbox.getHeight() / 2);
		}

		float dx = 0, dy = 0;

		if (left)
		{
			dx += -xspeed;
		}
		if (right)
		{
			dx += xspeed;
		}
		if (up)
		{
			dy += -yspeed;
		}
		if (down)
		{
			dy += yspeed;
		}
		move(dx * delta, dy * delta);
	}

	public void move(float dx, float dy)
	{
		this.incrementX(dx);
		this.incrementY(dy);
	}

	private void setDirections()
	{

		left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		up = Keyboard.isKeyDown(Keyboard.KEY_UP);
		down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
	}

	float	dx;
	float	dy;

	private Vector2f calculateDirectionVector(Input input)
	{
		float mousex = input.getMouseX();
		float mousey = input.getMouseY();

		dx = mousex - (hitbox.getCenterX());
		dy = mousey - (hitbox.getCenterY());

		return new Vector2f(dx, dy);
	}

	private double convertTheta(Vector3f player, Vector3f horizon)
	{
		double angle = Vector3f.angle(player, horizon);

		if(player.y < 0)
		{
			angle = -angle;
		}
		return angle;
	}

	public void onKey(int keycode)
	{

	}

	public void incrementX(float dx)
	{
		this.hitbox.setX(hitbox.getX() + dx);
	}

	public void setX(float x)
	{
		this.hitbox.setX(x);
	}

	public float getX()
	{
		return hitbox.getX();
	}

	public void incrementY(float dy)
	{
		this.hitbox.setY(hitbox.getY() + dy);
	}

	public void setY(float y)
	{
		this.hitbox.setY(y);
	}

	public float getY()
	{
		return hitbox.getY();
	}
}
