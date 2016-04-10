package net.request.match;

//Java Imports
import java.io.IOException;
import java.io.DataInputStream;


//Other Imports
import core.match.*;
import metadata.Constants;
import metadata.NetworkCode;
import net.response.match.ResponseMatchOver;
import net.request.GameRequest;
import util.DataReader;
import util.Log;

// for testing
import tests.CardWinsTests;
import model.WinsLosses;
import db.CardWinsDAO;
import db.PlayerDAO;

public class RequestMatchOver extends GameRequest {
	private int playerID;
	private int wonGame;
	private int wonAmount = 100;
	private int lossAmount = 25;
	
	@Override
    public void parse(DataInputStream dataInput) throws IOException {
    	playerID = DataReader.readInt(dataInput);
    	wonGame = DataReader.readInt(dataInput);
    }
	
	@Override
    public void process() throws Exception {
        ResponseMatchOver response = new ResponseMatchOver();
        MatchAction action = new MatchAction();
        
        // ADDED
        MatchPlayer winner, loser;
        int winnerCredits, loserCredits;
        
        action.setActionID(NetworkCode.MATCH_OVER);
        action.addInt(wonGame);
        
        MatchManager manager = MatchManager.getInstance();
        Match match = manager.getMatchByPlayer(playerID);

        
        //testing
        CardWinsTests test = new CardWinsTests();
        WinsLosses wins = test.getPlayersWinsLosses(playerID);
        test.showWinsLosses(wins);
        
        // Update player's win table and add credits 
        if (wonGame == 1) {
        	CardWinsDAO.playerWon(playerID, true);

        } else {
        	CardWinsDAO.playerWon(playerID, false);

        }

        
        /*testing
        Log.printf("%d wins; credits: %d",winner.getID(), winnerCredits);
        Log.printf("%d loses; credits: %d",loser.getID(), loserCredits);
        
        PlayerDAO.updateCredits(winner.getID(), winnerCredits);
        PlayerDAO.updateCredits(loser.getID(), loserCredits);
       	*/
        Log.printf("End of match");
        
                
        // TODO:
        response.setStatus((short)0);
    	
        client.add(response);
        if (!Constants.SINGLE_PLAYER){
        	match.addMatchAction(playerID, action);
        }
    }
	
}
