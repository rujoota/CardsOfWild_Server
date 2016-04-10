package net.request.match;

import java.io.DataInputStream;
import java.io.IOException;

import core.GameServer;
import net.request.GameRequest;
import net.response.match.ResponseReturnLobby;

public class RequestReturnLobby extends GameRequest {

	@Override
	public void parse(DataInputStream dataInput) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void process() throws Exception {
		// TODO Auto-generated method stub
		ResponseReturnLobby response = new ResponseReturnLobby();
		short status = 0;
		response.setStatus(status);
		//client.add(response);
	
		
		/*
		 * Leaving client connected. TODO: Lobby
		// ha: clean up player info from server 
        GameServer server = GameServer.getInstance();
        
        server.removeActiveClient(client.getID());
        server.removeActivePlayer(client.getPlayer().getID());
    	*/
	
	}
	
	

}
