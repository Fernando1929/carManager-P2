package edu.uprm.cse.datastructures.cardealer.model;

import java.util.Comparator;

public class KeyComparator implements Comparator<Long>{

	@Override
	public int compare(Long o1, Long o2) {
		if(o1>o2) {
			return 1;
		}
		else if(o1<o2) {
			return -1;
		}
		return 0;
	}

}
