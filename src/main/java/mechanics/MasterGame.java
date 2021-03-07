package mechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import asset.Board;
import asset.Rail.RailColor;
import mechanics.GameState.GlobalState;

public class MasterGame implements PlayerActionListener
{

	private static final int railCardCntPerColor = 12;
	private static final int railCardCntOnTable = 5;

	private Board board;
	private GlobalState globalState;
	private int currentPlayerId;
	private List<Player> players = new ArrayList<>();
	private List<GameStateListener> stateListeners = new LinkedList<>();

	private Map<Player, PlayerState> playerStates = new HashMap<>();
	private Map<Class<? extends PlayerStep>, Integer> currentPlayersStepsLeft = new HashMap<>();

	private LinkedList<RailCard> railCardDeck = new LinkedList<>();
	private RailCard[] railCardsOnTable = new RailCard[railCardCntOnTable];
	private List<RailCard> railCardsDisposed = new LinkedList<>();

	public MasterGame(Board board)
	{
		this.board = board;
	}

	public Board getBoard()
	{
		return board;
	}

	public void initialize()
	{
		for (int i = 0; i < railCardCntPerColor; ++i)
		{
			for (RailColor color : RailColor.values())
			{
				railCardDeck.add(new RailCard(color));
			}
		}

		Collections.shuffle(railCardDeck);

		fillRailCardsOnTable();
		initializeStepsLeft();
	}

	private void initializeStepsLeft()
	{
		currentPlayersStepsLeft.put(DrawRailCardStep.class, 2);
		currentPlayersStepsLeft.put(BuildRailStep.class, 1);
		currentPlayersStepsLeft.put(DrawNewGoalsStep.class, 1);
		currentPlayersStepsLeft.put(BuildTunnelSegmentStep.class, 1);
	}

	private boolean fillRailCardsOnTable()
	{
		for (int i = 0; i < railCardCntOnTable; ++i)
		{
			if (!drawRailCardFromDeckToPos(i)) return false;
		}
		return true;
	}

	private boolean drawRailCardFromDeckToPos(int pos)
	{
		if (railCardsOnTable[pos] == null)
		{
			if (railCardDeck.isEmpty())
			{
				if (railCardsDisposed.isEmpty())
				{
					System.out.println("Cannot draw new card from deck, not enough discarded cards.");
					return false;
				}
				else
				{
					railCardDeck.addAll(railCardsDisposed);
					railCardsDisposed.clear();
					Collections.shuffle(railCardDeck);
				}
			}

			railCardsOnTable[pos] = railCardDeck.removeFirst();
			return true;
		}
		else
		{
			System.out.println("Cannot draw new rail card to pos " + pos + " as that is already occupied");
			return false;
		}
	}

	public void disposeRailCard(RailCard railCard)
	{
		railCardsDisposed.add(railCard);
	}

	public void advance()
	{
		if (++currentPlayerId == players.size()) currentPlayerId = 0;
		notifyPlayers(new GameState(globalState, currentPlayerId));

		initializeStepsLeft();
	}

	private void notifyPlayers(GameState state)
	{
		for (GameStateListener listener : stateListeners)
			listener.onGameStateChanged(state);
	}

	@Override
	public void onPlayerTurnCompleted(Player player)
	{
		if (player.getId() == currentPlayerId)
		{
			advance();
		}
		else
		{
			System.out.println("Player " + player.getId() + " finished turn while player " + currentPlayerId + " was active. Ignoring...");
		}
	}

	@Override
	public void onPlayerStep(Player player, PlayerStep step)
	{
		if (step.playerId == currentPlayerId && player.getId() == currentPlayerId)
		{
			step.perform(this);
		}
		else
		{
			System.out.println("Player " + step.playerId + " finished turn while player " + currentPlayerId + " was active. Ignoring...");
		}
	}

	// ============================================================================================================
	// Step actions
	// ============================================================================================================

	public boolean drawRailCard(Player player, int cardId)
	{
		if (player.getId() == currentPlayerId)
		{
			if (railCardsOnTable[cardId] != null)
			{
				playerStates.get(player).addRailCard(railCardsOnTable[cardId]);
				railCardsOnTable[cardId] = null;
				return true;
			}
			else
			{
				System.out.println("" + player + " wanted to draw rail card " + cardId + " from the table, but there was nothing.");
				return false;
			}
		}
		else
		{
			System.out.println("Player " + player.getId() + " wanted to make step while " + currentPlayerId + " was active. Ignoring...");
			return false;
		}
	}
}
