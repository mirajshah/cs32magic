package network;

import java.util.Arrays;
import java.util.List;

import pvpmagic.*;

public class Coder {

	/**
	 * Called by server game loop on receiving queued update to lobby.
	 * @param players list of players in lobby
	 * @param update command
	 * @throws BadProtocolException poorly formed commands and anything other than connection or DC
	 */
	public static void updateLobby(List<Player> players, String update) throws BadProtocolException {
		try {
			String[] updateArr = update.split("[\t]");
			if (updateArr[0].equals("CONNECTION")) {
				String[] clientData = Arrays.copyOfRange(updateArr, 1, updateArr.length);
				players.add(Player.fromNetInit(clientData));
			} else if (updateArr[0].equals("DISCONNECTION")) {
				Integer ID = Integer.parseInt(updateArr[1]);
				int DCindex = 0;
				int index = 0;
				for (Player player : players) {
					if (player._netID == ID) {
						DCindex = index;
						break;
					}
					index++;
				}
				players.remove(DCindex);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new BadProtocolException("ERROR: Bad protocol.");
		}
	}
	
	/**
	 * Called by server game loop to encode lobby for broadcasting
	 * @param players list of players
	 * @param map integer representing which map is being played
	 * @return lobby encoded as string
	 */
	public static String encodeLobby(List<Player> players, int map) {
		String lobby = "";
		lobby += map + "\n";
		for (Player player : players) {
			lobby += player._netID + "\t" + player.toNetInit() + "\n";
		}
		return lobby;
	}
	
	/**
	 * Called by server game loop to encode game state for broadcasting
	 * @param units list of units in the game state - AFTER update() has been called
	 * @return game state encoded as string
	 */
	public static String encodeGame(List<Unit> units) {
		String game = "";
		for (Unit u : units) {
			game += u.toNet() + "\n";
		}
		return game;
	}
	
	 //eventString is in the format netID \t timestamp \t <data>
	public void handleEvent(String[] eventString, Player p, GameData data) {
	   Vector target = Vector.fromNet(eventString[3]);
	   if (eventString[2].equals("Q")) {
	     data.startCastingSpell(p, p._spells[0], target);
	   } else if (eventString[2].equals("W")){
	     data.startCastingSpell(p, p._spells[1], target);
	   } else if (eventString[2].equals("E")){
	     data.startCastingSpell(p, p._spells[2], target);
	   } else if (eventString[2].equals("R")){
	     data.startCastingSpell(p, p._spells[2], target);
	   } else if (eventString[2].equals("A")){
	     data.startCastingSpell(p, p._spells[4], target);
	   } else if (eventString[2].equals("S")){
	     data.startCastingSpell(p, p._spells[5], target);
	   } else if (eventString[2].equals("D")){
	     data.startCastingSpell(p, p._spells[6], target);  
	   } else if (eventString[2].equals("F")){
	     data.startCastingSpell(p, p._spells[7], target);
	   } else if (eventString[2].equals("CLICK")) {
	     if (!p._isRooted) {
	       p._destination = target;
	     }
	   }
	 }
}
