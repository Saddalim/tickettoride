package mechanics;

public class GameState
{
	public enum GlobalState
	{
		Initializing, InProgress, LastTurn, Finished
	}

	public GlobalState globalState;
	public int activePlayerId;

	public GameState(GlobalState globalState, int activePlayerId)
	{
		this.globalState = globalState;
		this.activePlayerId = activePlayerId;
	}
}
