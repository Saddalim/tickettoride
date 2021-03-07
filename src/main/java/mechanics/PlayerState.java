package mechanics;

import java.util.LinkedList;
import java.util.List;

import asset.Rail.RailColor;

public class PlayerState
{

	private List<RailCard> railCards = new LinkedList<>();

	public void addRailCard(RailCard railCard)
	{
		railCards.add(railCard);
	}

	public int getRailCardCount(RailColor color)
	{
		return (int) railCards.stream().filter(c -> c.color == color).count();
	}
}
