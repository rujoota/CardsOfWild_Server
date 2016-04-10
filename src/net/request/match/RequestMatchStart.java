package net.request.match;
import java.io.DataInputStream;
import java.io.IOException;

import core.match.MatchManager;
import util.DataReader;
import util.Log;
import net.request.GameRequest;
import net.response.match.ResponseMatchStart;
import core.match.*;
import model.Session;
import model.Player;
import db.SessionDAO;

public class RequestMatchStart extends GameRequest {
	private String sessionID;
	
	@Override
	public void parse(DataInputStream dataInput) throws IOException {
		sessionID = DataReader.readString(dataInput).trim();
	}

	@Override
	public void process() throws Exception {
		ResponseMatchStart response = new ResponseMatchStart();
		MatchManager manager = MatchManager.getInstance();
		Match match;
		short status;
		int matchID = 0;
		int playerID1 = 1, playerID2 = 2;
		
		Player player = SessionDAO.sessionPlayer(sessionID);
		playerID1 = player.getID();
		Session session = new Session(sessionID, playerID1);
	
		matchID = manager.getMatchIDByPlayer(playerID1);
		if (matchID != 0) {
			match = manager.getMatch(matchID);
		} else {
		    match = manager.createMatch(playerID1, playerID2);
		}
		if(match != null){
			// TODO: add response success constant
			status = 0;
			matchID = match.getMatchID();
		} else {
			// status !=0 means failure
			status = 1;
			Log.printf("Failed to create Match");
		}
		//Log.printf("Initializing match for players '%d' and '%d' in match %d", 
		//		playerID1, playerID2, matchID);
		
		// This does not currently return the sessionID to the client
		// SessionID is available from the Match, though
		response.setStatus(status);
		response.setMatchID(matchID);
		client.add(response);
	}
}
