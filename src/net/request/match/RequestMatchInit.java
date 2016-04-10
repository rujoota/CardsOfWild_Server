package net.request.match;

import core.GameServer;
import java.io.DataInputStream;
import java.io.IOException;

import core.match.MatchManager;
import util.DataReader;
import util.Log;
import net.request.GameRequest;
import net.response.match.ResponseMatchInit;
import core.match.*;
import db.AccountDAO;
import db.PlayerDAO;

public class RequestMatchInit extends GameRequest {

    private int playerID;
    private int matchID;

    @Override
    public void parse(DataInputStream dataInput) throws IOException {
        playerID = DataReader.readInt(dataInput);
        matchID = DataReader.readInt(dataInput);
    }

    @Override
    public void process() throws Exception {
        ResponseMatchInit response = new ResponseMatchInit();
        MatchManager manager = MatchManager.getInstance();
        short status;

        Log.printf("matchID = %d", matchID);

		// Assume player is in DB otherwise 
        //Match match = manager.createMatch(playerID1, playerID2);
        //if(GameServer.getInstance().getActivePlayers().size()==0) // if no players are playing
        //{
            client.joinPlayer(playerID);
        //}
        Match match = manager.matchPlayerTo(this.matchID, playerID);
        if (match != null) {
            // TODO: add response success constant
            status = 0;
            matchID = match.getMatchID();
        } else {
            // status !=0 means failure
            status = 1;
            Log.printf("Failed to create Match");
        }
        Log.printf("Initializing match for players '%d' and '%d' in match %d",
                playerID, this.matchID, matchID);

        response.setStatus(status);
        response.setMatchID(matchID);
        client.add(response);
    }
}
