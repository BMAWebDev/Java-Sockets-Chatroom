import com.chatroom.Server;

public class Main {
	public static void main(String[] args) {
		Server server = Server.initialize();
		server.startServer();
	}
}
