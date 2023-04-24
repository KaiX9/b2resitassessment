package ibf2022.batch2.paf.server.models;

import java.util.Date;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// Do not change this file
public class Comment {

	private String restaurantId;
	private String name;
	private long date = 0l;
	private String comment;
	private int rating;

	public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
	public String getRestaurantId() { return this.restaurantId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setComment(String comment) { this.comment = comment; }
	public String getComment() { return this.comment; }

	public void setDate(long date) { this.date = date; }
	public long getDate() { return this.date; }

	public void setRating(int rating) { this.rating = rating; }
	public int getRating() { return this.rating; }

	@Override
	public String toString() {
		return "Comment{restaurantId=%s, name=%s, date=%d, comment=%s, rating=%d"
				.formatted(restaurantId, name, date, comment, rating);
	}

	public static Comment createFromDoc(Document d) {
		Comment c = new Comment();
		c.setComment(d.getString("grade"));
		Date date = d.getDate("date");
		c.setDate(date.getTime());
		c.setName(d.getString("name"));
		c.setRating(d.getInteger("score"));
		return c;
	}

	public JsonObject toJSON() {
		if (name == null) {
			return Json.createObjectBuilder()
				.add("date", getDate())
				.add("comment", getComment())
				.add("rating", getRating())
				.build();
		}
		return Json.createObjectBuilder()
			.add("date", getDate())
			.add("name", getName())
			.add("comment", getComment())
			.add("rating", getRating())
			.build();
	}

}
