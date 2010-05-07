/**
 *
 */
package org.yoshiori.lingr.api;

import java.util.Set;

/**
 * @author yoshiori
 *
 */
public class ObserveEvent {

	private final Set<Message> messages;

	private final Set<Presence> presences;

	/**
	 * @return the messages
	 */
	public Set<Message> getMessages() {
		return messages;
	}

	public Set<Presence> getPresences() {
		return presences;
	}

	/**
	 * @param messages
	 * @param occupants
	 */
	public ObserveEvent(Set<Message> messages, Set<Presence> presences) {
		this.messages = messages;
		this.presences = presences;
	}

}
