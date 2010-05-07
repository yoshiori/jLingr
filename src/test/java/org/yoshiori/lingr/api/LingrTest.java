package org.yoshiori.lingr.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class LingrTest {

	static String USER = "testData";
	static String PASSWORD = "testtest";

	Lingr lingr = null;

	@Before
	public void startUp(){
		lingr = new Lingr(USER, PASSWORD);
	}

	@Test
	public void sessionが作成出来る事() throws Exception {
		assertThat( lingr.createSession(), is(true));
	}

	@Test
	public void roomsが取得出来る事() throws Exception {
		lingr.createSession();
		assertThat( lingr.getRooms(), is(notNullValue()));
		assertThat( lingr.getRooms().size(), is(1));
		assertThat( lingr.getRooms().get(0).getName(), is("yoshiori_test"));
	}

	@Test
	public void say出来る事() throws Exception {
		lingr.createSession();
		lingr.subscribe(lingr.getRooms(), true);
		long time = System.currentTimeMillis();
		lingr.say(String.valueOf(time), new Room("yoshiori_test"));
	}


}
