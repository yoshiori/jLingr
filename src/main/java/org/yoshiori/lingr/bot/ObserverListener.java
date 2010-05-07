package org.yoshiori.lingr.bot;

import org.yoshiori.lingr.api.ObserveEvent;

/**
 * @author yoshiori
 * 
 */
public interface ObserverListener {

	/**
	 * @param messages
	 * @param lingr
	 */
	public void observe(LingrBot lingrBot, ObserveEvent event);

}
