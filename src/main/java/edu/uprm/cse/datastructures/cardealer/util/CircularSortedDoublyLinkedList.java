package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class CircularSortedDoublyLinkedList <E> implements SortedList<E> {

	//Node Class
	private static class Node<E>{

		private E element;
		private Node<E> next;
		private Node<E> prev;

		public Node(E element, Node<E> next, Node<E> prev ) {
			super();
			this.element = element;
			this.next = next;
			this.prev = prev;
		}

		public Node() {
			super();
		}

		public E getElement() {
			return this.element;
		}

		public void setElement(E element) {
			this.element =element;
		}

		public Node<E> getNext(){
			return this.next;
		}

		public Node<E> getPrev(){
			return this.prev;
		}

		public void setNext(Node<E> nextNode) {
			this.next = nextNode;
		}

		public void setPrev(Node<E> prevNode) {
			this.prev = prevNode;
		}

	}
	//End Node Class

	//Iterator Class
	private class CircularSortedDoublyLinkedListIterator <E> implements Iterator<E> {
		private Node<E> nextNode;

		public CircularSortedDoublyLinkedListIterator() {
			this.nextNode = (Node<E>) header.getNext();
		}

		@Override
		public boolean hasNext() {
			return nextNode != header;
		}

		@Override
		public E next() {
			if(this.hasNext()) {
				E result = this.nextNode.getElement();
				this.nextNode = this.nextNode.getNext();
				return result;
			}
			else {
				throw new NoSuchElementException();
			}
		}

	}
	//End of Iterator Class

	private Node<E> header;
	private int currentSize;
	private Comparator<E> comparator;

	public CircularSortedDoublyLinkedList(Comparator<E> c) {
		this.header = new Node<>();
		this.header.next = this.header.prev = this.header;
		this.comparator= c;
		this.currentSize = 0;
	}

	//Adds an object to the List sorted
	@Override
	public boolean add(E obj) {
		Node<E> newNode = new Node<>(obj,this.header,this.header);
		Node<E> temp = this.header.getNext();
		if(this.isEmpty()) {
			this.header.setNext(newNode);
			this.header.setPrev(newNode);
			this.currentSize++;
			return true;
		}else {
			while(temp != this.header) {
				if(this.comparator.compare(obj, temp.getElement()) < 0) {
					temp.getPrev().setNext(newNode);
					newNode.setPrev(temp.getPrev());
					newNode.setNext(temp);
					temp.setPrev(newNode);
					this.currentSize++;
					return true;
				}
				temp = temp.getNext();
			}

		}
		temp.getPrev().setNext(newNode);
		newNode.setPrev(temp.getPrev());
		newNode.setNext(temp);
		temp.setPrev(newNode);
		this.currentSize++;
		return true;
	}

	// Returns the size of the list
	@Override
	public int size() {
		return this.currentSize;
	}

	//Removes an element from the list
	@Override
	public boolean remove(E obj) {
		return this.contains(obj) ? this.remove(this.firstIndex(obj)) : false;
	}

	//Remove the element at the specified index
	@Override
	public boolean remove(int index) {
		if(index >= this.size() || index < 0) {
			throw new IndexOutOfBoundsException();
		}else {
			Node<E>temp = this.getNode(index);
			temp.getPrev().setNext(temp.getNext());
			temp.getNext().setPrev(temp.getPrev());
			temp.setElement(null);
			temp.setNext(null);
			temp.setPrev(null);
			this.currentSize--;
			return true;
		}
	}

	//removes all the same elements and returns the count of elements removed
	@Override
	public int removeAll(E obj) {
		int result = 0;
		while(this.contains(obj)) {
			this.remove(obj);
			result++;
		}
		return result;
	}
	
	//Returns the first element in the list
	@Override
	public E first() {
		return this.header.getNext().getElement();
	}
	
	//Returns the last element in the list
	@Override
	public E last() {
		return this.header.getPrev().getElement();
	}
	
	//returns the element at the specified index
	@Override
	public E get(int index) {
		return this.getNode(index).getElement();
	}

	//Erases all the list
	@Override
	public void clear() {
		while(!this.isEmpty()) {
			this.remove(0);
		}
	}

	//Check is the list is empty
	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	//Checks if the element is in the list
	@Override
	public boolean contains(E obj) {
		return this.firstIndex(obj) >= 0;
	}

	//returns the first index of the element equal to the parameter
	@Override
	public int firstIndex(E obj) {
		int index = 0;
		Node<E> temp = this.header.getNext();
		while(index!=this.size()) {
			if(temp.getElement().equals(obj)) {
				return index;
			}
			temp = temp.getNext();
			index++;
		}
		return -1;
	}

	//returns the last index of the element equal to the parameter
	@Override
	public int lastIndex(E obj) {
		int index = this.size()-1;
		Node<E> temp = this.header.getPrev();
		while(index != -1) {
			if(temp.getElement().equals(obj)) {
				return index;
			}
			temp = temp.getPrev();
			index--;
		}
		return -1;
	}
	
	//returns a new Iterator 
	@Override
	public Iterator<E> iterator() {
		return new CircularSortedDoublyLinkedListIterator<E>();
	}
	
	//returns the node at the specified index
	private Node<E> getNode(int index){
		if(index >= this.size() || index < 0) {
			throw new IndexOutOfBoundsException();
		}else {
			Node<E>temp = this.header.getNext();
			int i =0;
			while(i!=index) {
				temp = temp.getNext();
				i++;
			}
			return temp;
		}
	}
}
