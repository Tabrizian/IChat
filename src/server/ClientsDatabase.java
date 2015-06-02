package server;

import java.util.ArrayList;
import java.util.HashMap;

import com.User;

public class ClientsDatabase {
	
	private ArrayList<Handler> handlers;
	private HashMap<User, Handler> userAndHandlers;
	private static ClientsDatabase instance = null;

	private ClientsDatabase() {
		handlers = new ArrayList<>();
		userAndHandlers = new HashMap<>();
		instance = this;
	}
	
	public static ClientsDatabase getClientsDatabase(){
		if(instance == null)
			new ClientsDatabase();
		return instance;
	}
	
	public void add(Handler handler,User username){
		handlers.add(handler);
		userAndHandlers.put(username, handler);
	}
	
	public void refresh(){
		for (Handler handler : handlers) {
			if(!handler.isRunning())
				handlers.remove(handler);
		}
	}
	public Handler getHandler(User user){
		return userAndHandlers.get(user);
	}
	
	
}
