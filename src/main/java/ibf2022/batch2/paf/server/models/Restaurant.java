package ibf2022.batch2.paf.server.models;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

// Do not change this file
public class Restaurant {

	private String restaurantId;
	private String name;
	private String address;
	private String cuisine;
	private List<Comment> comments = new LinkedList<>();

	public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
	public String getRestaurantId() { return this.restaurantId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setAddress(String address) { this.address = address; }
	public String getAddress() { return this.address; }

	public void setCuisine(String cuisine) { this.cuisine = cuisine; }
	public String getCuisine() { return this.cuisine; }

	public void setComments(List<Comment> comments) { this.comments = comments; }
	public List<Comment> getComments() { return this.comments; }
	public void addComment(Comment comment) { this.comments.add(comment); }

	@Override
	public String toString() {
		return "Restaurant{restaurantId=%s, name=%s, address=%s, cuisine=%s, comments=%s"
				.formatted(restaurantId, name, address, cuisine, comments);
	}

	public static Restaurant createIdAndNameFromDoc(Document d) {
		Restaurant res = new Restaurant();

		res.setRestaurantId(d.getString("restaurant_id"));
		res.setName(d.getString("name"));

		return res;
	}

	public static Restaurant createFromDoc(Document d) {
		Restaurant res = new Restaurant();

		res.setCuisine(d.getString("cuisine"));
		res.setName(d.getString("name"));
		res.setRestaurantId(d.getString("restaurant_id"));
		res.setAddress(d.getString("address"));
		List<Document> grades = d.getList("grades", Document.class);
		res.setComments(grades.stream()
			.map(doc -> Comment.createFromDoc(doc))
			.toList());

		return res;
	}

	public JsonObject IdAndNameToJSON() {
		return Json.createObjectBuilder()
					.add("restaurantId", getRestaurantId())
					.add("name", getName())
					.build();
	}

	public JsonObject toJSON() {
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

		for (Comment c : comments) {
			arrBuilder.add(c.toJSON());
		}
		
		return Json.createObjectBuilder()
					.add("restaurant_id", getRestaurantId())
					.add("cuisine", getCuisine())
					.add("address", getAddress())
					.add("comments", arrBuilder)
					.add("name", getName())
					.build();
	}

}
