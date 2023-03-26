package org.starloco.locos.entity.npc;

import org.starloco.locos.area.map.GameMap;
import org.starloco.locos.client.BasePlayer;
import org.starloco.locos.common.PathFinding;
import org.starloco.locos.common.SocketManager;
import org.starloco.locos.game.world.World;

import java.util.ArrayList;

public class NpcMovable extends Npc {

    private final static ArrayList<NpcMovable> movables = new ArrayList<>();

	private GameMap map;
	private short position = 0;
	private String[] path;

    public NpcMovable(int id, int cellid, byte orientation, int mapid, int template) {
		super(id, cellid, orientation, template);
		this.map = World.world.getMap(mapid);
		this.path = getTemplate().legacy.getPath().split(";");
        NpcMovable.movables.add(this);
	}	
	
	private void move() {
		if(this.position >= this.path.length) return;
		char dir = this.path[this.position].charAt(0);
        short nbr;

        if(dir == 'E') {
            nbr = Short.parseShort(this.path[this.position].substring(1));

            for(BasePlayer player : this.map.getPlayers())
                player.send("eUK" + this.getId() + "|" + nbr);
        } else {
            nbr = Short.parseShort(String.valueOf(this.path[this.position].charAt(1)));

            int oldCell = this.getCellId(), cell = this.getCellId();

            for (short i = 0; i <= nbr; i++) {
                cell = PathFinding.getCaseIDFromDirrection(cell, NpcMovable.getDirByChar(dir), this.map);
                if (!this.map.getCase(cell).isWalkable(true)) break;
                oldCell = cell;
            }

            String path;
            try {
                path = PathFinding.getShortestStringPathBetween(this.map, this.getCellId(), oldCell, 25);
            } catch (Exception e) {
				e.printStackTrace();
                return;
            }

            if (path == null)
                return;

            for (BasePlayer player : this.map.getPlayers())
            	if(player != null)
                	SocketManager.GAME_SEND_GA_PACKET(player.getGameClient(), "0", "1", String.valueOf(this.getId()), path);

            this.setCellId(oldCell);
        }

		this.position++;
		
		if(this.position == this.path.length) {
			String templatePath = getTemplate().legacy.getPath();
			this.path = (NpcMovable.getPath(this.path).equals(templatePath) ? NpcMovable.inverseOfPath(templatePath).split(";") : templatePath.split(";"));
			this.position = 0;
		}		
	}	
	
	private static String inverseOfPath(String arg) {
		String[] split = arg.split(";");
		String var = "";
		
		for(int i = split.length - 1; i >= 0; i--) {
			String loc0 = split[i];
			
			if(loc0.contains("R"))
				continue;
			
			switch(loc0.charAt(0)) {
				case 'H': loc0 = loc0.replace("H", "B"); break;
				case 'B': loc0 = loc0.replace("B", "H"); break;
				case 'G': loc0 = loc0.replace("G", "D"); break;
				case 'D': loc0 = loc0.replace("D", "G"); break;
			}
			
			var += (var.isEmpty() ? "" : ";") + loc0;			
		}
		return var;
	}	
	
	private static String getPath(String[] path) {
		String original = "";
		
		for(String arg : path)
			original += (original.isEmpty() ? "" : ";") + arg;
		
		return original;
	}
	
	private static char getDirByChar(char letter) {
		switch(letter) {
		case 'H': return 'f';
		case 'B': return 'b';
		case 'G': return 'd';
		case 'D': return 'h';
		default : return '?';
		}
	}

    public static void moveAll() {
        NpcMovable.movables.forEach(NpcMovable::move);
    }
}