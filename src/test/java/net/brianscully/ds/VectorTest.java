package net.brianscully.ds;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class VectorTest {

	Vector<String> underTest = new Vector<String>();
	
	@Test
	public void correctlyActsOnEmptyVector() {
		assertThat(underTest).hasSize(0);
		assertThat(underTest.isEmpty()).isTrue();
		assertThat(underTest.contains("")).isFalse();
		assertThat(underTest).doesNotContain("");
		assertThat(underTest.toArray()).hasSize(0);
		String[] strings = new String[1];
		assertThat(underTest.toArray(strings)).hasSize(1);
		
		try {
			underTest.get(0);
			fail("Should have thrown IndexOutOfBoundsException");
		}
		catch(IndexOutOfBoundsException e) {
			// success
		}
	}

	@Test
	public void correctlyPopulatesTwoElementVector() {
		
		underTest.add("TEST");
		underTest.add("TEST");
		
		assertThat(underTest).hasSize(2);
		assertThat(underTest.isEmpty()).isFalse();
		assertThat(underTest.contains("")).isFalse();
		assertThat(underTest.contains("TEST")).isTrue();
		assertThat(underTest).doesNotContain("");
		assertThat(underTest.toArray()).hasSize(2);
		String[] strings = new String[2];
		assertThat(underTest.toArray(strings)).hasSize(2);
		
		assertThat(underTest.indexOf("TEST")).isEqualTo(0);
		assertThat(underTest.lastIndexOf("TEST")).isEqualTo(1);
		
		assertThat(underTest.get(0)).isEqualTo("TEST");
		assertThat(underTest.remove(0)).isEqualTo("TEST");
		assertThat(underTest).hasSize(1);
		
		underTest.clear();
		assertThat(underTest).hasSize(0);
		assertThat(underTest.isEmpty()).isTrue();
	}
	
	@Test
	public void correctlyPopulatesBulkOperations() {
		
		List<String> fixture = Arrays.asList("TEST1","TEST2","TEST3");
		List<String> fixture2 = Arrays.asList("TEST1","TEST2");
		
		underTest.addAll(fixture);

		Iterator<String> itr = underTest.iterator();
		assertThat(itr.hasNext()).isTrue();
		
		assertThat(underTest).hasSize(3);
		assertThat(underTest.get(1)).isEqualTo("TEST2");
		assertThat(underTest.contains("TEST3")).isTrue();
		assertThat(underTest).containsExactly("TEST1","TEST2","TEST3");

		assertThat(underTest.removeAll(fixture2)).isTrue();
		assertThat(underTest).hasSize(1);
		assertThat(underTest).doesNotContain("TEST2");
		
		underTest.clear();
		
		underTest.addAll(fixture);
		
		assertThat(underTest.retainAll(fixture2)).isTrue();
		assertThat(underTest).hasSize(2);
		assertThat(underTest).containsExactly("TEST1","TEST2");
	}
	
	@Test
	public void correctlyPopulatesIndexedCalls() {
		List<String> fixture = Arrays.asList("TEST1","TEST2");
		underTest.addAll(fixture);
		underTest.add(0,"TEST0");
		assertThat(underTest).hasSize(3);
		assertThat(underTest).containsExactly("TEST0","TEST1","TEST2");
		
		underTest.set(1, "TEST1A");
		assertThat(underTest).containsExactly("TEST0","TEST1A","TEST2");
		
		List<String> subList = underTest.subList(1, 2);
		assertThat(subList).containsExactly("TEST1A","TEST2");
		
		underTest.addAll(1,fixture);
		assertThat(underTest).containsExactly("TEST0","TEST1","TEST2","TEST1A","TEST2");
	}

}
