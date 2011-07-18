package org.yoshiori.lingr.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.arnx.jsonic.JSON;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author yoshiori
 *
 */
public class Lingr {

	private static final Log log = LogFactory.getLog(Lingr.class);

	private final String userName;

	private final String password;

	private String session;

	private BigDecimal counter;

	private int maxObserveTime;

	/**
	 * @param name
	 * @param apiKey
	 */
	public Lingr(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	/**
	 * @return
	 */
	public boolean createSession() {
		return createSession(null);
	}

	public boolean createSession(String appkey) {
		PostMethod post = new PostMethod("http://lingr.com/api/session/create/");
		post.addParameter("user", userName);
		post.addParameter("password", password);
		if(appkey != null) {
			post.addParameter("app_key", appkey);
		}
		Map<String, Object> json = (Map<String, Object>) executeMethod(post);
		session = String.class.cast(json.get("session"));
		if (log.isDebugEnabled()) {
			log.debug("session start - session : " + session);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Room> getRooms() {
		GetMethod get = new GetMethod("http://lingr.com/api/user/get_rooms");
		get.setQueryString(new NameValuePair[] { new NameValuePair("session",
				session) });
		Map<String, Object> json = executeMethod(get);
		List<String> roomNames = (List<String>) json.get("rooms");
		List<Room> rooms = new ArrayList<Room>();
		for (String name : roomNames) {
			rooms.add(new Room(name));
		}
		return rooms;

	}

	public void subscribe(List<Room> rooms, Boolean reset) {
		PostMethod post = new PostMethod("http://lingr.com/api/room/subscribe");
		StringBuilder roomNames = new StringBuilder();
		for (Room room : rooms) {
			roomNames.append(room.getName());
			roomNames.append(",");
		}
		post.addParameter("session", session);
		post.addParameter("room", roomNames.toString());
		post.addParameter("reset", reset.toString());
		Map<String, Object> json = (Map<String, Object>) executeMethod(post);
		counter = BigDecimal.class.cast(json.get("counter"));
		if (log.isDebugEnabled()) {
			log.debug("counter : " + counter);
		}
	}

	/**
	 * @return
	 */
	public ObserveEvent observe() {
		if (log.isDebugEnabled()) {
			log.debug("observe - start");
		}
		GetMethod get = new GetMethod("http://lingr.com:8080/api/event/observe");
		get.setQueryString(new NameValuePair[] {
				new NameValuePair("session", session),
				new NameValuePair("counter", counter.toString()) });
		Map<String, Object> json = (Map<String, Object>) executeMethod(get);
		counter = BigDecimal.class.cast(json.get("counter"));
		if (log.isDebugEnabled()) {
			log.debug("observe - end");
		}
		return parseEvents(json);
	}

	/**
	 * @param message
	 * @return
	 */
	public void say(String message, Room room) {
		PostMethod post = new PostMethod("http://lingr.com/api/room/say");
		post.setParameter("session", session);
		post.setParameter("room", room.getName());
		post.setParameter("nickname", userName);
		post.setParameter("text", message);
		executeMethod(post);
	}

	@SuppressWarnings("unchecked")
	ObserveEvent parseEvents(Map<String, Object> json) {
		Set<Message> messages = new HashSet<Message>();
		Set<Presence> presences = new HashSet<Presence>();
		List<Map<String, Object>> events = (List<Map<String, Object>>) json
				.get("events");
		for (Map<String, Object> event : events) {
			// FIXME なんかダサイ
			if (event.containsKey("message")) {
				Map<String, Object> messageMap = (Map<String, Object>) event.get("message");
				messages
						.add(new Message(
								new Room(String.class.cast(messageMap.get("room"))),
								String.class.cast(messageMap.get("nickname")),
								String.class.cast(messageMap.get("text")),
								String.class.cast(messageMap.get("icon_url"))));
			} else if (event.containsKey("presence")) {
				Map<String, Object> presenceMap = (Map<String, Object>) event.get("presence");
				presences.add(new Presence(
						new Room(String.class.cast(presenceMap.get("room"))),
						String.class.cast(presenceMap.get("nickname")),
						String.class.cast(presenceMap.get("status")),
						String.class.cast(presenceMap.get("icon_url"))));
			}
		}
		return new ObserveEvent(messages, presences);
	}

	/**
	 * @param method
	 * @return
	 */
	@SuppressWarnings("unchecked")
	Map<String, Object> executeMethod(HttpMethod method) {
		try {
			method.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			HttpClient client = new HttpClient();
			if (maxObserveTime != 0) {
				client.getHttpConnectionManager().getParams().setSoTimeout(
						(maxObserveTime * 1000) + (1000 * 60));
			}
			client.executeMethod(method);
			Map<String, Object> json = (Map<String, Object>) JSON.decode(method
					.getResponseBodyAsStream());
			if (!json.get("status").equals("ok")) {
				throw new LingrException((String) json.get("detail"));
			}
			return json;
		} catch (IOException e) {
			throw new LingrException(e);
		} finally {
			method.releaseConnection();
		}
	}
}

