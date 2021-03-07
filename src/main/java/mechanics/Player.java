package mechanics;

public abstract class Player implements PlayerStateListener
{

	public enum PlayerColor
	{
		Red, Green, Blue, Yellow, White, Black
	}

	protected int id;
	protected String name;
	protected PlayerColor color;
	protected MasterGame game;

	protected PlayerState state = new PlayerState();

	protected Player(int id, MasterGame game)
	{
		this.id = id;
		this.game = game;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public PlayerColor getColor()
	{
		return color;
	}

	public void setColor(PlayerColor color)
	{
		this.color = color;
	}

	public MasterGame getGame()
	{
		return game;
	}

	public void setGame(MasterGame game)
	{
		this.game = game;
	}

	public abstract void subscribeToStateChange(PlayerActionListener listener);

	@Override
	public void onPlayerStateChanged(PlayerState state)
	{
		this.state = state;
	}
}
