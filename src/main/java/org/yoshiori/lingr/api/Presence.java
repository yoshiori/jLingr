package org.yoshiori.lingr.api;

public class Presence {
	final Room room;
	final String nickname;
	final String status;
	final String iconUrl;

	public Presence(Room room, String nickname, String status, String iconUrl) {
		this.room = room;
		this.nickname = nickname;
		this.status = status;
		this.iconUrl = iconUrl;
	}

	public Room getRoom() {
		return room;
	}

	public String getNickname() {
		return nickname;
	}

	public String getStatus() {
		return status;
	}

	public String getIconUrl() {
		return iconUrl;
	}
}
