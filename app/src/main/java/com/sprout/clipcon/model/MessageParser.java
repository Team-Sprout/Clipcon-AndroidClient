package com.sprout.clipcon.model;

import org.json.JSONException;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class MessageParser {

	/**
	 * @param message
	 * 			Message object received from server
	 * @return user 
	 * 			User object converted from message
	 */
	/*public static User getUserAndGroupByMessage(Message message) {
		User user = new User(message.get(Message.NAME));
		Group group = new Group(message.get(Message.GROUP_PK));

		// group.setName(name);

		List<String> userStringList = new ArrayList<String>();
		JSONArray tmpArray = message.getJson().getJSONArray(Message.LIST);
		Iterator<?> it = tmpArray.iterator();
		while (it.hasNext()) {
			String tmpString = (String) it.next();
			userStringList.add(tmpString);
		}

		List<User> userList = new ArrayList<User>();
		for (String userName : userStringList) {
			userList.add(new User(userName));
		}

		group.setUserList(userList);
		user.setGroup(group);

		return user;
	}*/

	/**
	 * @param m
	 * 			Message object received from server
	 * @return Contents 
	 * 			Contents object converted from message
	 */
	public static Contents getContentsbyMessage(Message m) throws JSONException {

		if (m.get("contentsType").equals(Contents.TYPE_IMAGE)) {
			String imageString = m.get("imageString");
		}
		return new Contents(m.get("contentsType"), m.getLong("contentsSize"), m.get("contentsPKName"), m.get("uploadUserName"), m.get("uploadTime"), m.get("contentsValue"));
	}

	/**
	 * @param imageString
	 * 			The String that transformed the Image received from the server
	 * @return InputStream
	 * 			An InputStream for creating Javafx Image objects
	 */
	/*public static InputStream testDecodeMethod(String imageString) {
		byte[] imageByte = Base64.decodeBase64(imageString);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		return bis;
	}*/
}
