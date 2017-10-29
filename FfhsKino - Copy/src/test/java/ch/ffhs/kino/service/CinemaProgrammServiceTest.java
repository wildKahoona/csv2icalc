package ch.ffhs.kino.service;

import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import ch.ffhs.kino.model.Vorstellung;


public class CinemaProgrammServiceTest {

	private CinemaProgrammServiceMock testee;

	@Before
	public void init() {
		testee = new CinemaProgrammServiceMock();

	}

	@Test
	public void test() {

		List<Vorstellung> programm = testee.getProgramm();

		System.out.println(programm.toString());
		Assume.assumeNotNull(programm);

	}

}
