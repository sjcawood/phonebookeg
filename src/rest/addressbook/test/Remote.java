package rest.addressbook.test;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import rest.addressbook.Entry;


/**
 * Test the phonebook application through the REST api
 * @author Sean Cawood
 */
public class Remote {

	/**
	 * Set the URL to the book application URI
	 */
	public static final String REST_URI = "http://localhost/Address/book";
	private static JSONObject entry1;
	private static JSONObject entry2;
	private static JSONObject entry3;
	
	
	/**
	 * Set up the JSON objects used to send requests and test with
	 * @throws JSONException
	 */
	@BeforeClass
	public static void setup() throws JSONException {
		entry1 = new JSONObject();
		entry1.put(Entry.JSON_FORENAME, "BOB");
		entry1.put(Entry.JSON_SURNAME, "SMITH");
		entry1.put(Entry.JSON_PHONE, "01234 999888");
		entry1.put(Entry.JSON_ID, 0);
		
		entry2 = new JSONObject();
		entry2.put(Entry.JSON_FORENAME, "Dave");
		entry2.put(Entry.JSON_SURNAME, "Jones");
		entry2.put(Entry.JSON_ADDRESS, "123 Road Lane");
		entry2.put(Entry.JSON_PHONE, "01234 555666");
		entry2.put(Entry.JSON_ID, 1);
		
		entry3 = new JSONObject();
		entry3.put(Entry.JSON_FORENAME, "Eve");
		entry3.put(Entry.JSON_SURNAME, "Jone");
		entry3.put(Entry.JSON_ADDRESS, "123 Road Lane");
		entry3.put(Entry.JSON_PHONE, "01234 555667");
		entry3.put(Entry.JSON_ID, 2);
	}
	
	/**
	 * Test the new entry functionality
	 */
	@Test
	public void testNewEntry() {
		ClientConfig clientConf = new ClientConfig();
		RestClient client = new RestClient(clientConf);
		Resource restResource = client.resource(REST_URI);
		restResource.contentType(MediaType.APPLICATION_JSON);
		
		ClientResponse response = restResource.post(entry1.toString());
		assertEquals("Response status code as not 200", 200, response.getStatusCode());

		response = restResource.post(entry2.toString());
		assertEquals("Response status code as not 200", 200, response.getStatusCode());
	}

	/**
	 * Test the remove entry functionality
	 */
	@Test
	public void testDeleteEntry() {
		ClientConfig clientConf = new ClientConfig();
		RestClient client = new RestClient(clientConf);
		Resource restResource = client.resource(REST_URI + "/0");
		
		ClientResponse response = restResource.delete();
		assertEquals("Response status code as not 200", 200, response.getStatusCode());
	}

	/**
	 * Test the update entry functionality
	 */
	@Test
	public void testUpdateEntry() throws JSONException {
		ClientConfig clientConf = new ClientConfig();
		RestClient client = new RestClient(clientConf);
		Resource restResource = client.resource(REST_URI + "/1");
		restResource.contentType(MediaType.APPLICATION_JSON);
		
		JSONObject updateJSON = new JSONObject();
		updateJSON.put(Entry.JSON_FORENAME, "NEW");
		updateJSON.put(Entry.JSON_ID, 99);
		entry2.put(Entry.JSON_FORENAME, "NEW");
		
		ClientResponse response = restResource.put(updateJSON.toString());
		assertEquals("Response status code as not 200", 200, response.getStatusCode());
	}

	/**
	 * Test the search entries functionality
	 */
	@Test
	public void testSearch() throws JSONException {
		ClientConfig clientConf = new ClientConfig();
		RestClient client = new RestClient(clientConf);
		Resource restResourceUpdate = client.resource(REST_URI);
		restResourceUpdate.contentType(MediaType.APPLICATION_JSON);
		
		ClientResponse response = restResourceUpdate.post(entry3.toString());
		assertEquals("Response status code as not 200", 200, response.getStatusCode());
		
		Resource restResourceTest = client.resource(REST_URI + "/"+ entry2.getString(Entry.JSON_SURNAME));
		restResourceTest.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		
		
		response = restResourceTest.get();
		assertEquals("Response status code as not 200", 200, response.getStatusCode());
		String respString = response.getEntity(String.class);
		JSONArray responceJSON = new JSONArray(respString);
		JSONArray testJSON = new JSONArray().put(entry2);
		
		assertEquals("Unexpected result returned", testJSON, responceJSON);
	}

	/**
	 * Test the list entries functionality
	 */
	@Test
	public void testListAll() throws JSONException {
		ClientConfig clientConf = new ClientConfig();
		RestClient client = new RestClient(clientConf);
		Resource restResource = client.resource(REST_URI);
		restResource.contentType(MediaType.APPLICATION_JSON);
		restResource.accept(MediaType.APPLICATION_JSON);
		ClientResponse response = restResource.get();
		
		assertEquals("Response status code as not 200", 200, response.getStatusCode());
		
		String respString = response.getEntity(String.class);
		JSONArray responceJSON = new JSONArray(respString);
		JSONArray testJSON = new JSONArray().put(entry2).put(entry3);
		
		assertEquals("Unexpected result returned", testJSON, responceJSON);
	}
	
	/** 
	 * Cleanup after test, removing the added entries, to allow service to be used
	 */
	@AfterClass
	public static void cleanUp() {
		ClientConfig clientConf = new ClientConfig();
		RestClient client = new RestClient(clientConf);
		Resource restResource = client.resource(REST_URI + "/1");
		
		ClientResponse response = restResource.delete();
		assertEquals("Response status code as not 200", 200, response.getStatusCode());
		restResource = client.resource(REST_URI + "/2");
		response = restResource.delete();
		assertEquals("Response status code as not 200", 200, response.getStatusCode());
	}

}
