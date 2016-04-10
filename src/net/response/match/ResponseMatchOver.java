package net.response.match;

//Other Imports
import metadata.NetworkCode;
import util.GamePacket;
import net.response.GameResponse;

public class ResponseMatchOver extends GameResponse{
	private short status;
	private int credits;

 public ResponseMatchOver() {
        response_id = NetworkCode.MATCH_OVER;
    }

    @Override
    public byte[] getBytes() {
        GamePacket packet = new GamePacket(response_id);
        packet.addShort16(status);
        packet.addInt32(credits);

        return packet.getBytes();
    }
    
	public void setStatus(short status) {
        this.status = status;
    }
	
	// added
	public void setCredits(int credits) {
		this.credits = credits;
	}
}
