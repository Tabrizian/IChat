package server;

import java.util.ArrayList;
import java.util.HashMap;

import client.Client;

public class ClientsDatabase {
	
	private ArrayList<Handler> handlers;
	private static ClientsDatabase instance = null;

	private ClientsDatabase() {
		handlers = new ArrayList<>();
		instance = this;
	}
	
	public static ClientsDatabase getClientsDatabase(){
		if(instance == null)
			new ClientsDatabase();
		return instance;
	}
	
	public void add(Handler handler){
		handlers.add(handler);
	}
	
	public void refresh(){
		for (Handler handler : handlers) {
			if(!handler.isRunning())
				handlers.remove(handler);
		}
	}
	
	
}
