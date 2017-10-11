package ch.ffhs.kino.service;

import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import ch.ffhs.kino.model.Event;


public class CinemaProgrammServiceTest {

	private CinemaProgrammService testee;

	@Before
	public void init() {
		testee = new CinemaProgrammService();

	}

	@Test
	public void test() {

		List<Event> programm = testee.getProgramm();

		System.out.println(programm.toString());
		Assume.assumeNotNull(programm);

	}

}
