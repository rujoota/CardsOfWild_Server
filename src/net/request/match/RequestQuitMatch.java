package net.request.match;

// Java Imports
import java.io.IOException;
import java.io.DataInputStream;
// Other Imports


import core.match.*;
import metadata.Constants;
import metadata.NetworkCode;
import net.response.match.ResponseQuitMatch;
import net.request.GameRequest;
import util.DataReader;
import util.Log;

// added
import core.ClientHandler;
import core.GameServer;

/**
 *  The RequestQuitMatch class sent if game ends without conclusion 
*/

public class RequestQuitMatch extends GameRequest {
	private int playerID;   // changed from match ID number
	
    @Override
    public void parse(DataInputStream dataInput) throws IOException {
    	playerID = DataReader.readInt(dataInput);
    }
    
    @Override
    public void process() throws Exception {
        ResponseQuitMatch response = new ResponseQuitMatch();
        MatchAction action = new MatchAction();
        action.setActionID(NetworkCode.QUIT_MATCH);
        action.setIntCount(0);
        action.setStringCount(0);
        
        MatchManager manager = MatchManager.getInstance();
        Match match = manager.getMatchByPlayer(playerID);
        
        Log.printf("Client quit unexpectedly. End of match");

        response.setStatus((short)0);
        
        if (!Constants.SINGLE_PLAYER){
        	match.addMatchAction(playerID, action);
        	// Set player inactive so no more actions are
        	// received from other player
        	match.setPlayerHasDisconnected(playerID);
 
        }
       
        client.add(response);
    
    }
    
    

}
