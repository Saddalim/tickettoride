package mechanics;

public interface PlayerActionListener
{

	void onPlayerStep(Player player, PlayerStep step);

	void onPlayerTurnCompleted(Player player);

}
