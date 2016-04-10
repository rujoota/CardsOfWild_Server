package net.response.match;

import net.response.GameResponse;
import metadata.NetworkCode;
import util.GamePacket;


public class ResponseFoodCard extends GameResponse{
    private short status;

    public ResponseFoodCard() {
        response_id = NetworkCode.APPLY_FOOD;
    }

    @Override
    public byte[] getBytes() {
        GamePacket packet = new GamePacket(response_id);
        packet.addShort16(status);

        return packet.getBytes();
    }

    public void setStatus(short status){
    	this.status = status;
    }
}
