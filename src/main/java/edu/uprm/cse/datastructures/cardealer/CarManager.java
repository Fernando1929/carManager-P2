package edu.uprm.cse.datastructures.cardealer;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uprm.cse.datastructures.cardealer.model.Car;
import edu.uprm.cse.datastructures.cardealer.model.CarTable;
import edu.uprm.cse.datastructures.cardealer.util.HashTableOA;
import edu.uprm.cse.datastructures.cardealer.util.SortedList;

@Path("/cars")
public class CarManager {

	private final HashTableOA<Long,Car> hashT = CarTable.getInstance();

	/*Returns an array of cars.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Car[] getAllCars() {
		SortedList<Car> sObjects = hashT.getValues();
		Car [] result = new Car[sObjects.size()];
		for(int i = 0; i < sObjects.size();++i) {
			result[i] = sObjects.get(i);
		}
 		return result;
	}

	/*Returns the car with the specific id or throws Not found exception.
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Car get(@PathParam("id")long id) {
		Car target = hashT.get(id);
		if(target == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return target;
	}

	/*Adds a new car verifying that there isn't a car with the same id and
	 *returns a created response or Bad request if there is a car with the same id.
	 */
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCar(Car car) {
		if(!(hashT.containsKey(car.getCarId()))){
			hashT.put(car.getCarId(), car);
			return Response.status(Response.Status.CREATED).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	/*Removes the specified car and add the updated version of the car and
	 *returns OK or Not found response.
	 */
	//
	@PUT
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCar(Car car) {
		if(hashT.containsKey(car.getCarId())) {
			hashT.put(car.getCarId(), car);
			return Response.status(Response.Status.OK).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	/*Removes the car with the specified id and
	 *returns OK or Not found response.
	 */
	@DELETE
	@Path("/{id}/delete")
	public Response deleteCar(@PathParam("id") long id) {
		if(hashT.remove(id) != null) {
			return Response.status(Response.Status.OK).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
