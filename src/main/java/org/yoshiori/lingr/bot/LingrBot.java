package org.yoshiori.lingr.bot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yoshiori.lingr.api.Lingr;
import org.yoshiori.lingr.api.ObserveEvent;
import org.yoshiori.lingr.api.Room;


/**
 * @author yoshiori
 *
 */
public class LingrBot {

	private static final Log log = LogFactory.getLog(LingrBot.class);

	final Lingr lingr;

	boolean isAlive = true;

	private Set<ObserverListener> observerListeners = new HashSet<ObserverListener>();

	private Executor executor = Executors.newCachedThreadPool();


	public LingrBot(String userName, String password) {
		lingr = new Lingr(userName, password);
	}
	public void start() {
		new Thread() {
			/*
			 * (non-Javadoc)
			 *
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				while (isAlive) {
					try {
						runBot();
					} catch (Exception e) {
						if (log.isDebugEnabled()) {
							log.debug(e);
						}
					}
					try {
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	void runBot() {
		lingr.createSession();
		List<Room> rooms = lingr.getRooms();
		lingr.subscribe(rooms, true);
		while(isAlive){
			try {
				final ObserveEvent event = lingr.observe();
				if (event.getMessages().size() > 0
						|| event.getPresences().size() > 0) {
					for (final ObserverListener listener : observerListeners) {
						executor.execute(new Thread() {

							@Override
							public void run() {
								listener.observe(LingrBot.this, event);
							}
						});
					}
				}
			} catch (Exception e) {
				log.debug(e);
			}
		}
	}
//
	/**
	 * @param message
	 */
	public void say(String message,Room room) {
		lingr.say(message,room);
	}

	/**
	 *
	 */
	public void stop() {
		isAlive = false;
	}

	/**
	 * @param listener
	 */
	public void addListener(ObserverListener listener) {
		observerListeners.add(listener);
	}
}
