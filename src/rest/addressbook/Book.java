package rest.addressbook;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * Rest application and interface to represent a simple JSON phonebook application
 * Paths available :
 * /book        - GET    - Returns all entries
 * /book        - POST   - Adds a new entry based on given JSON
 * /book/{name} - GET    - Returns entries with given surnames
 * /book/{id}   - PUT    - Updates fields of given entry with passed data
 * /book/{id}   - DELETE - Removes the given entry from the addressbook
 * 
 * Entry JSON should be in the following form -
 *  {
 *  	surname ; ""
 *      forename: ""
 *      address: ""
 *      phone: ""
 *  }
 *  
 *  Returned JSON will also include a id field with entry id number
 * @author Sean
 */
@Path("/book")
public class Book{

	private static HashMap<Long, Entry> idEntryMap = new HashMap<Long, Entry>();
	private static long nextId = 0L;

	/**
	 * Handles adding a new entry, based on the passed in JSON object
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public String newEntry(String toAdd) {
		JSONObject entryJSON;
		try {
			entryJSON = new JSONObject(toAdd);
			Entry newEntry = new Entry(entryJSON, true);
			idEntryMap.put(newEntry.getId(), newEntry);
		} catch (JSONException e) {
			throw new WebApplicationException(400);
		}
		return "";
	}
	
	/**
	 * Handles removing a entry, based on the passed in id
	 */
	@DELETE
	@Path("/{entryId}")
	public String deleteEntry(@PathParam("entryId") long id) {
		if (idEntryMap.containsKey(id)) {
			idEntryMap.remove(id);
		} else {
			throw new WebApplicationException(404);
		}
		return "";
	}
	
	/**
	 * Handles removing a entry, based on the passed in id, returning updated entry json 
	 */
	@PUT
	@Path("/{entryId}")
	@Consumes("application/json")
	@Produces("application/json")
	public String updateEntry(String updateValue, @PathParam("entryId") long id) {
		try {
			JSONObject updateJSON = new JSONObject(updateValue);
			Entry selectedEntry = idEntryMap.get(id);
			if (selectedEntry == null) {
				throw new WebApplicationException(404);
			} else {
				return selectedEntry.update(updateJSON).asJSON().toString(true);
			}
		} catch (JSONException e) {
			throw new WebApplicationException(400);
		}
	}
	
	/**
	 * Handles searching for an entry by surname, returning found entries as a json array string
	 */
	@GET
	@Path("/{surname}")
	@Produces("application/json")
	public String getBySurname(@PathParam("surname") String surname) {
		JSONArray selectiveArray = new JSONArray();
		try {
			for (Entry e : idEntryMap.values()) {
				if (e.getSurname().equals(surname)) {
					selectiveArray.add(e.asJSON());
				}
			}
			
			return selectiveArray.toString(1);
		} catch (JSONException e) {
			throw new WebApplicationException(400);
		}
	}
	
	/**
	 * Handles listing all entries, returning a json array string of all entries
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String listAll() {
		JSONArray allArray = new JSONArray();
		try {
			for (Entry e : idEntryMap.values()) {
				allArray.add(e.asJSON());
			}
			
			return allArray.toString(1);
		} catch (JSONException e) {
			throw new WebApplicationException(400);
		}
	}
	
	/**
	 * Returns the next entry id available for the book
	 * @return next id number available
	 */
	protected static long nextEntryId() {
		nextId++;
		return nextId - 1;
	}
	
	/**
	 * Clears the contents of the books and resets the next available id
	 */
	public void empty() {
		idEntryMap.clear();
		nextId = 0;
	}
}
