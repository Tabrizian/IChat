package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class UsersDatabase {

	private HashMap<String, String> userAndPass;
	private ArrayList<User> users;
	private static UsersDatabase instance = null;

	private UsersDatabase() {
		userAndPass = new HashMap<>();
		users = new ArrayList<>();
		getInitialUsers();
	}

	public static UsersDatabase getUsersDataBase() {
		if (instance == null) {
			instance = new UsersDatabase();
		}
		return instance;
	}

	public boolean addUser(User user, boolean check) {
		if (!check) {
			userAndPass.put(user.getUsername(), user.getPassword());
			users.add(user);
			return true;
		} else {
			if (!users.contains(user)) {
				userAndPass.put(user.getUsername(), user.getPassword());
				users.add(user);
				return true;
			} else {
				return false;
			}
		}
	}

	public void addUser(User user) {
		userAndPass.put(user.getUsername(), user.getPassword());
		users.add(user);
		writeUser(user);

	}

	public boolean checkValidity(String username, String password) {
		if (userAndPass.containsKey(username)
				&& userAndPass.get(username).equals(password)) {
			return true;
		}
		return false;
	}

	public void getInitialUsers() {
		File users = new File("data/users.txt");
		InputStream in = null;
		ObjectInputStream object = null;
		try {
			in = new FileInputStream(users);
			
			while (in.available() > 0) {
				User user;
				object = new ObjectInputStream(in);
				user = (User) object.readObject();
				addUser(user, false);

			}

			in.close();
			object.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(NullPointerException e){
			
		}

	}

	public void writeUser(User user) {
		OutputStream out = null;
		ObjectOutputStream object = null;
		try {
			out = new FileOutputStream(new File("data/users.txt"), true);
			object = new ObjectOutputStream(out);
			object.writeObject(user);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
				object.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public User getUser(String username){
		for (User user : users) {
			if(user.getUsername().equals(username))
				return user;
		}
		return null;
	}
}