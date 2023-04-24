package ibf2022.batch2.paf.server.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	// TODO: Task 2 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	// db.restaurant.distinct("cuisine")
	public List<String> getCuisines() {

		Query query = new Query();
		query.fields().include("cuisine");
		List<String> cuisines = mongoTemplate.findDistinct(query, "cuisine",
			 "restaurant", String.class);
		
		return cuisines;
	}

	// TODO: Task 3 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	// db.restaurant.aggregate([
	//   {$match: {cuisine: <input cuisine>}},
	//   {$project: {restaurant_id: 1, name: 1}},
	//   {$sort: {name: 1}}
	// ])
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {

		MatchOperation matchCuisine = Aggregation.match
			(Criteria.where("cuisine").is(cuisine));
		ProjectionOperation projectFields = Aggregation.project
			("restaurant_id", "name");
		SortOperation sortName = Aggregation.sort
			(Sort.Direction.ASC, "name");
		Aggregation pipeline = Aggregation.newAggregation
			(matchCuisine, projectFields, sortName);
		AggregationResults<Document> aRe = mongoTemplate.aggregate
			(pipeline, "restaurant", Document.class);
		List<Document> aReList = aRe.getMappedResults();
		List<Restaurant> restaurantList = new ArrayList<>();
		for (Document d : aReList) {
			Restaurant res = Restaurant.createIdAndNameFromDoc(d);
			restaurantList.add(res);
		}
		return restaurantList;
	}
	
	// TODO: Task 4 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	// db.restaurant.aggregate([
	//   {$match: {restaurant_id: <input restaurantId>}},
	//   {$project: {
	//     restaurant_id: 1,
	//     name: 1,
	//     cuisine: 1,
	//     address: {$concat: ["$address.building", ", ", "$address.street",
	// 	 		", ", "$address.zipcode", ", ", "$borough"]},
	//     grades: 1
	//   }}
	// ])
	public Optional<Restaurant> getRestaurantById(String id) {
		
		MatchOperation matchId = Aggregation.match
			(Criteria.where("restaurant_id").is(id));
		ProjectionOperation projectOps = Aggregation.project
			("restaurant_id", "name", "cuisine", "grades")
			.andExpression("concat(address.building,', ',address.street,', ',address.zipcode,', ',borough)")
			.as("address");
		Aggregation pipeline = Aggregation.newAggregation
			(matchId, projectOps);
		
		AggregationResults<Document> aRe = mongoTemplate.aggregate
			(pipeline, "restaurant", Document.class);
		if (!aRe.iterator().hasNext()) {
			return Optional.empty();
		}
		Document doc = aRe.iterator().next();
		Restaurant res = Restaurant.createFromDoc(doc);

		return Optional.of(res);
	}

	// TODO: Task 5 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	// // db.comments.insert([
	//     {
	//         restaurant_id: <input restaurant_id>,
	//         name: <input restaurant name>,
	//         date: <input current date and time>,
	//         comment: <input comment>,
	//         rating: <input rating>
	//     }
	// ])
	public void insertRestaurantComment(Comment comment) {

		Document doc = new Document()
        	.append("restaurant_id", comment.getRestaurantId())
			.append("name", comment.getName())
			.append("date", comment.getDate())
			.append("comment", comment.getComment())
			.append("rating", comment.getRating());
		
		Query query = new Query(Criteria.where("restaurant_id")
			.is(comment.getRestaurantId()));
		Update update = new Update()
			.push("grades", new Document()
			.append("name", comment.getName())
			.append("date", new Date())
			.append("grade", comment.getComment())
			.append("score", comment.getRating()));

    	mongoTemplate.insert(doc, "comments");
		mongoTemplate.updateFirst(query, update, Document.class,
			 "restaurant");

	}
}