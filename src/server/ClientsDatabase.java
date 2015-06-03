package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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

	public static ClientsDatabase getClientsDatabase() {
		if (instance == null)
			new ClientsDatabase();
		return instance;
	}

	public void add(Handler handler, User username) {
		handlers.add(handler);
		userAndHandlers.put(username, handler);
	}

	public void refresh() {
		Iterator<Handler> iter = handlers.iterator();
		while (iter.hasNext()) {
			Handler handler = iter.next();

			if (!handler.isRunning())
				iter.remove();
		}
	}

	public Handler getHandler(User user) {
		return userAndHandlers.get(user);
	}
	
	public boolean isAvailable(String username){
		return userAndHandlers.containsKey(UsersDatabase.getUsersDataBase().getUser(username));	
	}
	public boolean isAvailable(User user){
		return userAndHandlers.containsKey(user);
	}
	
	public void remove(Handler handler){
		handlers.remove(handler);
		Set<User> keySet = userAndHandlers.keySet();
		Iterator<User> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			User user = (User) iterator.next();
			if(userAndHandlers.get(user).equals(handler)){
				iterator.remove();
			}
		}
	}

}
