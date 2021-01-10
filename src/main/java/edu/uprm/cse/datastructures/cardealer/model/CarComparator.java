package edu.uprm.cse.datastructures.cardealer.model;

import java.util.Comparator;

public class CarComparator implements Comparator<Car>  {

	//compares the strings of the brand,model and option of two cars
	@Override
	public int compare(Car car1, Car car2) {
		int brandr = car1.getCarBrand().toUpperCase().compareTo(car2.getCarBrand().toUpperCase());
		int modelr = car1.getCarModel().toUpperCase().compareTo(car2.getCarModel().toUpperCase());
		int optionr = car1.getCarModelOption().toUpperCase().compareTo(car2.getCarModelOption().toUpperCase());

		if(brandr > 0 ) {
			return 1;
		}else if(brandr < 0) {
			return -1;
		}else{
			if(modelr > 0) {
				return 1;
			}else if(modelr < 0) {
				return -1;
			}else {
				if(optionr > 0) {
					return 1;
				}else if(optionr < 0) {
					return -1;
				}
			}
		}
		return 0;
	}
}

