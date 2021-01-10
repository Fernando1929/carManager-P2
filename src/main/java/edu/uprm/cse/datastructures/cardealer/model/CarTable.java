package edu.uprm.cse.datastructures.cardealer.model;

import edu.uprm.cse.datastructures.cardealer.util.HashTableOA;

public class CarTable {
	
  private static HashTableOA<Long ,Car> hashT = new HashTableOA<Long,Car>(new KeyComparator(),new CarComparator());

  private CarTable(){}
  
  //returns the instance of the list
  public static HashTableOA<Long ,Car> getInstance(){
    return hashT;
  }
  
  //creates a new instance of the list
  public static void resetCars() {
	  hashT = new HashTableOA <Long,Car>(new KeyComparator(),new CarComparator());
  }
  
}                       