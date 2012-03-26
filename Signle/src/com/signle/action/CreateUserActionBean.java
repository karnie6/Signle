package com.signle.action;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoURI;
import com.mongodb.WriteResult;
import com.signle.model.User;

import com.google.gson.*;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import java.util.Set;

import org.apache.commons.io.IOUtils;

//todo: add version in URL
@UrlBinding("/signle/createuser")
public class CreateUserActionBean extends SignleActionBean {
	
	@DefaultHandler
	public Resolution createUser() {
		try {
			String userJson = new String(IOUtils.toByteArray(getContext().getRequest().getInputStream()), "UTF-8");
			
			Gson gson = new Gson();
			User userBean = gson.fromJson(userJson, User.class);
			//User userBean = new User();
	    // userBean.setEmail("ks3@test.com");
		//	userBean.setFirstName("kar");
			//userBean.setLastName("sen");
			//todo: validate email, first name, last name
	    	MongoURI uri = new MongoURI("mongodb://dbh74.mongolab.com:27747/beacon");		
		   DB mongoDB = uri.connectDB();
		   
		   boolean auth = mongoDB.authenticate("806_ro","townehouse".toCharArray());
		   
		   DBCollection userCollection = mongoDB.getCollection("User");
		   BasicDBObject user = new BasicDBObject();

		   user.put("firstName", userBean.getFirstName());
		   user.put("lastName", userBean.getLastName());
		    
	       user.put("emailAddress", userBean.getEmail());
	       userCollection.insert(user);
	   //   System.out.println("User Id : " + user.get("_id"));
	      // return new StreamingResolution("", writeResult.getField("_id").toString()); 
	       //writeResult.getField("_id");
	        
	       //lets find this object so we can return the system generated id
	      // BasicDBObject query = new BasicDBObject();
	      // query.put("emailAddress", userBean.getEmail());
	      // DBCursor cursor = userCollection.find(query);
	       
	       //return id
	       //cursor.get
	       //user.getString("_id")
	        
	        return new StreamingResolution("", user.get("_id").toString() );

		} catch (Exception e) {
			e.printStackTrace();
			return new ErrorResolution(400, "Unable to add user");
		}
	
	}

}
