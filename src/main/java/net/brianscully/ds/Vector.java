package net.brianscully.ds;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Vector<E> implements List<E> {
	
	@SuppressWarnings("unchecked")
	private E[] backingArray = (E[]) new Object[0];
	private int lastIndex = 0;
	
	private class InternalIterator implements ListIterator<E> {
		
		private final int frozenLastIndex;
		private int index = 0;
		
		public InternalIterator(int lastIndex, Option<Integer> startingIndex) {
			frozenLastIndex = lastIndex;
			if (startingIndex.isSome())
				index = startingIndex.get();
		}

		public boolean hasNext() {
			checkComodification();
			return index < lastIndex;
		}

		public E next() {
			checkComodification();
			E value = backingArray[index];
			index++;
			return value;
		}

		public void remove() {
			checkComodification();
			throw new UnsupportedOperationException("Remove is an unsupported operation on Vector");
		}

		private void checkComodification() {
			if (lastIndex != frozenLastIndex)
				throw new ConcurrentModificationException();
		}

		public boolean hasPrevious() {
			checkComodification();
			return index > 0;
		}

		public E previous() {
			checkComodification();
			index--;
			return backingArray[index];
		}

		public int nextIndex() {
			checkComodification();
			return index+1;
		}

		public int previousIndex() {
			checkComodification();
			return index-1;
		}

		public void set(E e) {
			checkComodification();
			throw new UnsupportedOperationException();
		}

		public void add(E e) {
			checkComodification();
			throw new UnsupportedOperationException();
		}
	}

	public int size() {
		return lastIndex;
	}

	public boolean isEmpty() {
		return lastIndex == 0;
	}

	public boolean contains(Object o) {
		return indexOf(o) > -1;
	}

	public Iterator<E> iterator() {
		Option<Integer> none = none();
		return new InternalIterator(lastIndex,none);
	}

	private Option<Integer> none() {
		Option<Integer> none = Option.none();
		return none;
	}

	public Object[] toArray() {
		Object[] result = new Object[lastIndex];
		System.arraycopy(backingArray, 0, result, 0, result.length);
		return result;
	}

	public <T> T[] toArray(T[] a) {
		int copyLen = backingArray.length > a.length ? a.length : backingArray.length;
		System.arraycopy(backingArray, 0, a, 0, copyLen);
		return a;
	}

	public boolean add(E e) {
		resizeArrayIfNeeded(1);
		backingArray[lastIndex] = e;
		lastIndex++;
		return true;
	}

	private void resizeArrayIfNeeded(int addedEntries) {
		if (backingArray.length < (lastIndex+addedEntries)) {
			int newLength = Math.max(backingArray.length + addedEntries, backingArray.length * 2);
			@SuppressWarnings("unchecked")
			E[] resizedArray = (E[]) new Object[newLength];
			System.arraycopy(backingArray, 0, resizedArray, 0, backingArray.length);
			backingArray = resizedArray;
		}
	}

	public boolean remove(Object o) {
		return remove(indexOf(o)) != null;
	}

	public boolean containsAll(Collection<?> c) {
		for(Iterator<?> itr = c.iterator(); itr.hasNext(); ) {
			Object test = itr.next();
			if (!contains(test))
				return false;
		}
		return true;
	}

	public boolean addAll(Collection<? extends E> c) {
		resizeArrayIfNeeded(c.size());
		for(Iterator<? extends E> itr = c.iterator(); itr.hasNext(); ) {
			backingArray[lastIndex] = itr.next();
			lastIndex++;
		}
		return true;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		resizeArrayIfNeeded(c.size());
		int offset = c.size();
		for(int i=backingArray.length-1; i>index+offset-1; i--) {
			backingArray[i] = backingArray[i-offset];
		}
		Iterator<? extends E> itr = c.iterator();
		for(int i=index; i<index+offset; i++) {
			backingArray[i] = itr.next();
			lastIndex++;
		}
		return true;
	}

	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for(Iterator<?> itr = c.iterator(); itr.hasNext(); ) {
			Object next = itr.next();
			if (remove(next)) changed = true;
		}
		return changed;
	}

	public boolean retainAll(Collection<?> c) {
		if (c.containsAll(this)) return false;
		for(int i=0; i<backingArray.length; i++) {
			if (!c.contains(backingArray[i]))
				remove(i);
		}
		return true;
	}

	public void clear() {
		for(int i=0; i<backingArray.length-1; i++) {
			backingArray[i] = null;
		}
		lastIndex = 0;
	}

	public E get(int index) {
		checkWithinBounds(index);
		return backingArray[index];
	}

	public E set(int index, E element) {
		checkWithinBounds(index);
		E current = backingArray[index];
		backingArray[index] = element;
		return current;
	}

	public void add(int index, E element) {
		checkWithinBounds(index);
		resizeArrayIfNeeded(1);
		for(int i=backingArray.length-1; i>index; i--) {
			backingArray[i] = backingArray[i-1];
		}
		lastIndex++;
		backingArray[index] = element;
	}

	private void checkWithinBounds(int index) {
		if (index < 0 || index >= lastIndex)
			throw new IndexOutOfBoundsException();
	}

	public E remove(int index) {
		checkWithinBounds(index);
		E result = backingArray[index];
		for(int i=index+1; i<backingArray.length; i++) {
			backingArray[i-1] = backingArray[i];
		}
		lastIndex--;
		return result;
	}

	public int indexOf(Object o) {
		for (int i=0; i<backingArray.length; i++) {
			if ((backingArray[i] == null && o == null) || backingArray[i].equals(o))
				return i;
		}
		return -1;
	}

	public int lastIndexOf(Object o) {
		for (int i=backingArray.length-1; i>0; i--) {
			if ((backingArray[i] == null && o == null) || backingArray[i].equals(o))
				return i;
		}
		return -1;
	}

	public ListIterator<E> listIterator() {
		return new InternalIterator(lastIndex,none());
	}

	public ListIterator<E> listIterator(int index) {
		return new InternalIterator(lastIndex,Option.some(index));
	}

	public List<E> subList(int fromIndex, int toIndex) {
		checkWithinBounds(fromIndex);
		checkWithinBounds(toIndex);
		Vector<E> result = new Vector<E>();
		for(int i=fromIndex; i<toIndex+1; i++) {
			result.add(backingArray[i]);
		}
		return result;
	}

	
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		for(int i=0; i<lastIndex; i++) {
			builder.append(toStringIndex(i));
			if (i < lastIndex-1)
				builder.append(',');
		}
		return builder.append("]").toString();
	}

	private String toStringIndex(int index) {
		E value = backingArray[index];
		return value == null ? "null" : value.toString();
	}
}
