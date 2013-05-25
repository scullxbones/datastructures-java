package net.brianscully.ds;

import static org.fest.assertions.api.Assertions.*;

import net.brianscully.ds.Option.Either;

import org.junit.Test;

public class OptionTest {

	Option<String> none = Option.none();
	Option<String> some = Option.some("TEST");
	
	@Test
	public void correctlyBehavesForNone() {
		assertThat(none.asSet()).isEmpty();
		assertThat(none.either()).isEqualTo(Either.None);
		assertThat(none.isEmpty()).isTrue();
		assertThat(none.isNone()).isTrue();
		assertThat(none.isSome()).isFalse();
	}

	@Test
	public void correctlyBehavesForSome() {
		assertThat(some.asSet()).containsOnly("TEST");
		assertThat(some.either()).isEqualTo(Either.Some);
		assertThat(some.isEmpty()).isFalse();
		assertThat(some.isNone()).isFalse();
		assertThat(some.isSome()).isTrue();
	}

}
