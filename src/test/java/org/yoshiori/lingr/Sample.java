package org.yoshiori.lingr;

import org.yoshiori.lingr.api.Message;
import org.yoshiori.lingr.api.ObserveEvent;
import org.yoshiori.lingr.api.Presence;
import org.yoshiori.lingr.bot.LingrBot;
import org.yoshiori.lingr.bot.ObserverListener;

public class Sample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LingrBot bot = new LingrBot("testData", "testtest");
		bot.addListener(new ObserverListener() {
			public void observe(LingrBot lingrBot, ObserveEvent event) {
				for(Message message : event.getMessages()){
					System.out.println(message.getNickname() + "@" + message.getRoom().getName());
					System.out.println(message.getText());
				}
				for(Presence presence : event.getPresences()){
					System.out.println(presence.getNickname() + "@" + presence.getRoom().getName());
					System.out.println(presence.getStatus());
				}
			}
		});
		bot.start();
	}
}