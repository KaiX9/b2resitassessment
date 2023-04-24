package ibf2022.batch2.paf.server.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;
import ibf2022.batch2.paf.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository resRepo;

	// TODO: Task 2
	// Do not change the method's signature
	public List<String> getCuisines() {

		List<String> cuisines = this.resRepo.getCuisines();
		for (int i = 0; i < cuisines.size(); i++) {
			String cuisine = cuisines.get(i);
			cuisine = cuisine.replaceAll("/", "_");
			cuisines.set(i, cuisine);
		}
		Collections.sort(cuisines);
		return cuisines;
	}

	// TODO: Task 3 
	// Do not change the method's signature
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {

		cuisine = cuisine.replaceAll("_", "/");
		List<Restaurant> restaurantList = this.resRepo.getRestaurantsByCuisine(cuisine);
		
		return restaurantList;
	}

	// TODO: Task 4 
	// Do not change the method's signature
	public Optional<Restaurant> getRestaurantById(String id) {

		try {
		this.resRepo.getRestaurantById(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return Optional.empty();
		}
		return this.resRepo.getRestaurantById(id);
	}

	// TODO: Task 5 
	// Do not change the method's signature
	public void postRestaurantComment(Comment comment) {

		this.resRepo.insertRestaurantComment(comment);

	}
}
