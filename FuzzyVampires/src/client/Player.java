package client;

import java.util.LinkedList;
import java.util.List;

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

	private float					xspeed		= .25F, yspeed = .25F;
	private double					theta;
	private boolean					left, right, up, down;

	private Vector2f				playerdirection;
	private Shape					newhitbox;
	private Shape					hitbox;

	private List<Projectile>	projectiles	= new LinkedList<Projectile>();

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
		g.setAntiAlias(true);
		g.draw(newhitbox);
		//this.drawMagnitudeAndComponents(g);
		//this.drawCenterDot(g);
		//this.drawDebugInformation(g);

		for(Projectile p: projectiles)
		{
			p.render(g);
		}
		g.setAntiAlias(false);
	}

	private void drawMagnitudeAndComponents(Graphics g)
	{
		// Magnitude Line
		g.drawLine(hitbox.getCenterX(), hitbox.getCenterY(), playerdirection.x
				+ hitbox.getCenterX(), playerdirection.y + hitbox.getCenterY());

		// Y component line
		g.drawLine(hitbox.getCenterX() + dx, hitbox.getCenterY(),
				hitbox.getCenterX() + dx, hitbox.getCenterY() + dy);

		// X component line
		g.drawLine(hitbox.getCenterX(), hitbox.getCenterY(),
				hitbox.getCenterX() + dx, hitbox.getCenterY());

		// Line labels
		g.drawString("" + dy, hitbox.getCenterX() + dx, hitbox.getCenterY()
				+ dy / 2);
		g.drawString("" + dx, hitbox.getCenterX() + dx / 2, hitbox.getCenterY());
	}

	private void drawCenterDot(Graphics g)
	{
		g.fillOval(Game.WIDTH / 2 - 2, Game.HEIGHT / 2 - 2, 4, 4);
	}

	private void drawDebugInformation(Graphics g)
	{
		g.drawString(
				"Player coords: (" + hitbox.getCenterX() + ", "
						+ hitbox.getCenterY() + ")", 25, 50);
		g.drawString("Mouse coords: " + Mouse.getX() + ", "
				+ (Game.HEIGHT - Mouse.getY()), 25, 35);

		g.drawString("Player direction: " + playerdirection.x + " "
				+ playerdirection.y, 25, 65);

		g.drawString("Theta: " + theta, 25, 80);
		g.drawString("Components: " + dy + "y " + dx + "x", 25, 95);
	}

	public void update(Input input, int delta)
	{
		playerdirection = calculateDirectionVector(input);
		Vector3f playerdirectionas3f = new Vector3f(playerdirection.x,
				playerdirection.y, 0);
		Vector3f horizon = new Vector3f(1, 0, 0);

		theta = convertTheta(playerdirectionas3f, horizon);

		transformHitbox(theta);

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

		for(Projectile p: projectiles)
		{
			p.update(input, delta);
		}

		for(int i = 0; i < projectiles.size(); i++)
		{
			if(projectiles.get(i).isDestroyed())
			{
				projectiles.remove(i);
			}
		}
		
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
		{
			projectiles.add(new Projectile(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, this
					.getWidth() / 5, this.getHeight() / 5, playerdirection));
		}
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
		{
			projectiles.add(new Projectile(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2, this
					.getWidth() / 5, this.getHeight() / 5, playerdirection));
		}
	}

	public void move(float dx, float dy)
	{
		Vector2f direction = new Vector2f(dx, dy).normalise();
		float xscale = direction.x;
		float yscale = direction.y;
		if (xscale < 0)
		{
			xscale = -xscale;
		}
		if (yscale < 0)
		{
			yscale = -yscale;
		}

		this.incrementX(dx * xscale);
		this.incrementY(dy * yscale);
	}

	public void transformHitbox(double theta)
	{
		newhitbox = hitbox.transform(Transform.createRotateTransform(
				(float) theta, hitbox.getCenterX(), hitbox.getCenterY()));
	}

	private void setDirections()
	{

		left = Keyboard.isKeyDown(Keyboard.KEY_A);
		right = Keyboard.isKeyDown(Keyboard.KEY_D);
		up = Keyboard.isKeyDown(Keyboard.KEY_W);
		down = Keyboard.isKeyDown(Keyboard.KEY_S);
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

		if (player.y < 0)
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

	public void incrementY(float dy)
	{
		this.hitbox.setY(hitbox.getY() + dy);
	}

	public float getX()
	{
		return hitbox.getX();
	}

	public void setX(float x)
	{
		this.hitbox.setX(x);
	}

	public float getY()
	{
		return hitbox.getY();
	}

	public void setY(float y)
	{
		this.hitbox.setY(y);
	}

	public float getWidth()
	{
		return hitbox.getWidth();
	}

	public float getHeight()
	{
		return hitbox.getHeight();
	}

}
