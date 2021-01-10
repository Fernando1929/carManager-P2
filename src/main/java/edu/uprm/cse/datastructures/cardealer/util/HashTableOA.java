package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;

public class HashTableOA<K, V> implements Map<K, V> {

	public static class MapEntry<K,V>{

		private K key;
		private V value;

		public MapEntry(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return this.key;
		}

		public V getValue() {
			return this.value;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public void setValue(V value) {
			this.value = value;
		}
	}

	private Object[] slots;
	private int currentSize;
	private static int DEFAULT_CAPACITY = 10;
	private Comparator<K> keyComp;
	private Comparator<V> valueComp;

	public HashTableOA(int initialCapacity, Comparator<K> keyComparator , Comparator<V> valueComparator) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException("Initial capacity must me at least 1.");
		}

		this.slots = new Object[initialCapacity];
		this.currentSize = 0;
		this.valueComp = valueComparator;
		this.keyComp = keyComparator;
	}

	public HashTableOA(Comparator<K> keyc ,Comparator<V> valc) {
		this(DEFAULT_CAPACITY,keyc,valc);
	}

	/*Returns the number of elements in the HashTable.
	 */
	@Override
	public int size() {
		return this.currentSize;
	}

	/*Returns the state of the HashTable empty or not.
	 */
	@Override
	public boolean isEmpty() {
		return this.currentSize == 0;
	}

	/*Adds the MapEntry with the specified key and values and if it exist one 
	 *with the same key change it's value and return the old value.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) {
		if(key == null ) {
			throw new IllegalArgumentException("The key of the value cannot be null.");
		}

		MapEntry<K,V> target = this.getMapEntry(key);

		if((this.currentSize / this.slots.length) >= 0.70)
			this.reAllocate();

		if(target == null) {
			target = (MapEntry<K,V>)this.slots[this.hashFunction(key)];

			if(target == null) {
				this.slots[this.hashFunction(key)] = new MapEntry<K,V>(key,value);
				this.currentSize++;
				return null;
			}

			for(int i = 0; i<this.slots.length;++i ) {
				target = (MapEntry<K,V>)this.slots[(this.hashFunction2(key) + i) % this.slots.length];
				if(target == null) {
					this.slots[(this.hashFunction2(key) + i) % this.slots.length] = new MapEntry<K,V>(key,value);
					this.currentSize++;
					return null;
				}
			}
		}

		V result = target.getValue();
		target.setValue(value);
		return result;
	}

	/*First hash function
	 */
	private int hashFunction(K key) {
		return Math.abs(key.hashCode()) % this.slots.length;
	}

	/*Second hash function
	 */
	private int hashFunction2(K key) {
		return  Math.abs((int)(Math.pow(key.hashCode(),2) + 1) % this.slots.length);
	}

	/*Return the value of a MapEntry with the specified key.
	 */
	@Override
	public V get(K key) {
		if(key == null ) {
			throw new IllegalArgumentException("The key cannot be null.");
		}
		return this.getMapEntry(key) == null? null : this.getMapEntry(key).getValue();
	}

	/*Removes MapEntry with the specified key/
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V remove(K key) {
		if(key == null ) {
			throw new IllegalArgumentException("The key cannot be null.");
		}

		MapEntry<K,V> target = this.getMapEntry(key);
		if(target != null) {
			V result = target.getValue();
			for(int i = 0;i < this.slots.length;++i) {
				target = (MapEntry<K,V>)this.slots[i];
				if(target != null && target.getValue().equals(result)) {
					this.slots[i] = null;
					this.currentSize--;
					return result;
				}
			}
		}
		return null;
	}

	/*Verifies if the HashTable contains a specified key.
	 */
	@Override
	public boolean containsKey(K key) {
		return this.get(key) != null;
	}

	/*Returns a SortedList with all the keys in the HashTable.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SortedList<K> getKeys() {
		SortedList<K> sKeys = new CircularSortedDoublyLinkedList<K>(this.keyComp);
		for(Object o :this.slots) {
			if(o != null) {
				MapEntry<K,V> r = (MapEntry<K, V>)o;
				sKeys.add(r.getKey());
			}
		}
		return sKeys; 
	}

	/*Returns a SortedList with all the values in the HashTable.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SortedList<V> getValues() {
		SortedList<V> sValues = new CircularSortedDoublyLinkedList<V>(this.valueComp);
		for(Object o :this.slots) {
			if(o!=null) {
				MapEntry<K,V> r = (MapEntry<K, V>) o;
				sValues.add(r.getValue());
			}
		}
		return sValues; 
	}

	/*Returns the MapEntry of the specified key.
	 */
	@SuppressWarnings("unchecked")
	private MapEntry<K,V> getMapEntry(K key){
		MapEntry<K,V> temp = (MapEntry<K,V>) this.slots[this.hashFunction(key)];//check hash function one
		if(temp != null && temp.getKey().equals(key)) {
			return temp;
		}
		for(int i = 0; i<this.slots.length;++i ) {
			temp = (MapEntry<K,V>)this.slots[(this.hashFunction2(key) + i) % this.slots.length];
			if(temp != null && temp.getKey().equals(key)) {
				return temp;
			}
		}
		return null;
	}

	/*Expands the HashTable.
	 */
	@SuppressWarnings("unchecked")
	private void reAllocate() {
		HashTableOA<K, V> temp  = new HashTableOA<>(this.slots.length*2, this.keyComp,this.valueComp);
		for(int i = 0; i<this.slots.length;++i) {
			if(((MapEntry<K,V>)this.slots[i]) != null) {
				temp.put(((MapEntry<K,V>)this.slots[i]).getKey(), ((MapEntry<K,V>)this.slots[i]).getValue());
			}
		}
		this.slots = temp.slots;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Other way to do this
		//		Object[] tempSlots = this.slots;
		//		Object[] newSlots = new Object[this.slots.length*2];
		//		this.currentSize = 0;
		//		this.slots = newSlots;
		//		for(int i = 0; i<tempSlots.length;++i) {
		//			if(((MapEntry<K,V>)tempSlots[i]) != null) {
		//				this.put(((MapEntry<K,V>)tempSlots[i]).getKey(), ((MapEntry<K,V>)tempSlots[i]).getValue());
		//			}
		//		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

}
