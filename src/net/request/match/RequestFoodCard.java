
package net.request.match;

import core.match.*;

import java.io.DataInputStream;
import java.io.IOException;
import metadata.Constants;
import metadata.NetworkCode;
import net.request.GameRequest;
import net.response.match.ResponseFoodCard;
import util.DataReader;
import util.Log;


public class RequestFoodCard extends GameRequest{
    private int playerID;   // match ID number
    private int cardId;   
    private int targetCardId;  

    
    @Override
	public void parse(DataInputStream dataInput) throws IOException {
	   playerID = DataReader.readInt(dataInput);
            cardId = DataReader.readInt(dataInput);
            targetCardId = DataReader.readInt(dataInput);
	}
    
    @Override
    public void process() throws Exception {
        Log.printf("RequestFoodCard with playerId:%d,attackersPosition:%d,attackedPosition:%d", playerID,cardId,targetCardId);
        ResponseFoodCard response = new ResponseFoodCard();
        MatchManager manager = MatchManager.getInstance();
        Match match = manager.getMatchByPlayer(playerID);
        MatchAction action = new MatchAction();
        action.setActionID(NetworkCode.APPLY_FOOD); 
        action.setIntCount(2);
        action.addInt(cardId);
        action.addInt(targetCardId);
        
        
        // TODO: update log
        Log.printf("Player '%d' is attacking '%d'", 1, 2);
    
        response.setStatus((short)0);
    	
        if(!Constants.SINGLE_PLAYER){
        	match.addMatchAction(playerID, action);
        }
        client.add(response);
       
    }
}
