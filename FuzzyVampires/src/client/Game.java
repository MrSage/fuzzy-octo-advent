package client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public static final String	GAME_NAME		= "Magus 0.3 Alpha Build";
	public static final int		STATE_MAINMENU	= 1;
	public static final int		STATE_INGAME	= 0;
	public static final int		WIDTH 			= 800;
	public static final int 	HEIGHT			= 600;
	public static final int 	TARGETFPS		= 60;
	
	public static final boolean fullscreen		= false;
	public static final boolean showfps			= true;
	

	public Game(String name)
	{
		super(name);
		this.addState(new StateMainMenu(STATE_MAINMENU));
		this.addState(new StateInGame(STATE_INGAME));
	}

	public static void main(String[] args)
	{
		AppGameContainer agc;

		try
		{
			agc = new AppGameContainer(new Game(GAME_NAME));
			agc.setDisplayMode(WIDTH, HEIGHT, fullscreen);
			agc.setShowFPS(showfps);
			agc.setTargetFrameRate(TARGETFPS);
			agc.start();
		} catch (SlickException se)
		{
			se.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException
	{
		this.getState(STATE_MAINMENU).init(gc, this);
		this.getState(STATE_INGAME).init(gc, this);

		this.enterState(STATE_INGAME);
	}
}
