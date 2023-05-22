import com.chatroom.Server;
import com.chatroom.ClientHandler;

public class Main {
	public static void main(String[] args) {
		Server server = Server.initialize();
		server.startServer();
	}
}
