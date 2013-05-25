package net.brianscully.ds;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

public class BinarySearchTreeTest {

	private BinarySearchTree<String> underTest = new BinarySearchTree<>();
	
	@Before
	public void setUp() {
		underTest.add("TEST0");
		underTest.add("TEST1");
		underTest.add("TEST2");
	}

	@Test
	public void correctlyReportsSizeAfterInsert() {
		assertThat(underTest.size()).isEqualTo(3);
	}

	@Test
	public void correctlyReportsContains() {
		assertThat(underTest.contains("TEST1")).isTrue();
		assertThat(underTest.contains("TEST11")).isFalse();
	}

	@Test
	public void correctlyRemovesNode() {
		underTest.remove("TEST1");
		underTest.add("TEST11");
		assertThat(underTest.contains("TEST1")).isFalse();
		assertThat(underTest.contains("TEST11")).isTrue();
	}

}
