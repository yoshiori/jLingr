package org.yoshiori.lingr.api;


/**
 * @author yoshiori
 *
 */
public class Message {

	final Room room;
	final String iconUrl;
	final String nickname;
	final String text;

	public Message(Room room, String nickname, String text, String iconUrl) {
		this.room = room;
		this.nickname = nickname;
		this.text = text;
		this.iconUrl = iconUrl;
	}

	public Room getRoom() {
		return room;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public String getNickname() {
		return nickname;
	}
	public String getText() {
		return text;
	}
}
