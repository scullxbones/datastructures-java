package net.brianscully.ds;

import java.util.Collections;
import java.util.Set;

public final class Option<T> {

	public enum Either { Some, None }
	
	private final T initialValue;

	private Option(T initialValue) {
		this.initialValue = initialValue;
	}
	
	public T get() {
		if (initialValue == null)
			throw new NullPointerException();
		return initialValue;
	}
	
	public Set<T> asSet() {
		if (initialValue == null)
			 return Collections.emptySet();
		return Collections.singleton(initialValue);
	}
	
	public Either either() {
		if (initialValue == null)
			return Either.None;
		return Either.Some;
	}
	
	public boolean isEmpty() {
		return initialValue == null;
	}
	
	public boolean isNone() {
		return initialValue == null;
	}
	
	public boolean isSome() {
		return initialValue != null;
	}
	
	public static <E> Option<E> some(E value) {
		return new Option<E>(value);
	}
	
	public static <E> Option<E> none() {
		return new Option<E>(null);
	}

	public static <E> Option<E> noneOf(Class<E> clazz) {
		return new Option<E>(null);
	}

}
