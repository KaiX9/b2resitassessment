package ibf2022.batch2.paf.server.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;
import ibf2022.batch2.paf.server.services.RestaurantService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;

@RestController
@RequestMapping(path="/api")
public class RestaurantController {

	@Autowired
	private RestaurantService resSvc;

	// TODO: Task 2 - request handler
	@GetMapping(path="/cuisines")
	public ResponseEntity<String> getAllCuisines() {

		List<String> cuisines = this.resSvc.getCuisines();
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for (String cuisine : cuisines) {
			arrBuilder.add(cuisine);
		}
		JsonArray cuisineArray = arrBuilder.build();

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(cuisineArray.toString());

	}

	// TODO: Task 3 - request handler
	@GetMapping(path="/restaurants/{cuisine}")
	public ResponseEntity<String> getRestaurantsByCuisine(@PathVariable String cuisine) {

		List<Restaurant> restaurantList = this.resSvc.getRestaurantsByCuisine(cuisine);
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for (Restaurant r : restaurantList) {
			arrBuilder.add(r.IdAndNameToJSON());
		}
		JsonArray restaurantArray = arrBuilder.build();

		System.out.println(restaurantArray);
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(restaurantArray.toString());
	}

	// TODO: Task 4 - request handler
	@GetMapping(path="/restaurant/{restaurant_id}")
	public ResponseEntity<String> getRestaurantById(@PathVariable String restaurant_id) {

		Optional<Restaurant> restaurant = this.resSvc.getRestaurantById(restaurant_id);
		if (restaurant.isEmpty()) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body("{ 'error' : 'Missing " + restaurant_id + "'" + " }");
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(restaurant.get().toJSON().toString());
	}

	// TODO: Task 5 - request handler
	@PostMapping(path="/restaurant/comment")
	public ResponseEntity<String> postComment(@RequestBody MultiValueMap<String, String> form) {

		try {
			Date d = new java.util.Date();
			long epoch = d.getTime();
			Comment c = new Comment();
			System.out.println(form);
			c.setDate(epoch);
			c.setComment(form.getFirst("comment"));
			c.setName(form.getFirst("name"));
			c.setRating(Integer.parseInt(form.getFirst("rating")));
			c.setRestaurantId(form.getFirst("restaurantId"));
			this.resSvc.postRestaurantComment(c);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Json.createObjectBuilder()
						.build().toString());
	}
}
