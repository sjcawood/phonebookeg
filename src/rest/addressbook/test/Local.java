package rest.addressbook.test;

import static org.junit.Assert.*;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rest.addressbook.Book;
import rest.addressbook.Entry;

/**
 * Test the phonebook application through direct Java calls
 * @author Sean Cawood
 */
public class Local {

	private Book phoneBook;
	private JSONObject entry1;
	private JSONObject entry2;
	private JSONObject entry3;
	
	/**
	 * Test the phonebook application through the REST api
	 * @author Sean Cawood
	 */
	@Before
	public void setup() throws JSONException {
		phoneBook = new Book();
		entry1 = new JSONObject();
		entry1.put(Entry.JSON_FORENAME, "BOB");
		entry1.put(Entry.JSON_SURNAME, "SMITH");
		entry1.put(Entry.JSON_PHONE, "01234 999888");
		entry1.put(Entry.JSON_ID, 0);
		phoneBook.newEntry(entry1.toString());
		
		entry2 = new JSONObject();
		entry2.put(Entry.JSON_FORENAME, "Dave");
		entry2.put(Entry.JSON_SURNAME, "Jones");
		entry2.put(Entry.JSON_ADDRESS, "123 Road Lane");
		entry2.put(Entry.JSON_PHONE, "01234 555666");
		entry2.put(Entry.JSON_ID, 1);
		phoneBook.newEntry(entry2.toString());
		
		entry3 = new JSONObject();
		entry3.put(Entry.JSON_FORENAME, "Eve");
		entry3.put(Entry.JSON_SURNAME, "Jones");
		entry3.put(Entry.JSON_ADDRESS, "123 Road Lane");
		entry3.put(Entry.JSON_PHONE, "01234 555667");
		entry3.put(Entry.JSON_ID, 2);
	}
	
	/**
	 * Cleanup the phonebook
	 */
	@After
	public void tearDown() {
		phoneBook.empty();
	}
	
	/**
	 * Test the new entry functionality
	 */
	@Test
	public void testNewEntry() throws JSONException {
		JSONArray expectedResultArray = new JSONArray();
		expectedResultArray.add(entry1);
		expectedResultArray.add(entry2);
		
		JSONArray actualArray = new JSONArray(phoneBook.listAll()); 
		
		assertEquals("Expected result differs from actual",
				expectedResultArray,
				actualArray);
		
		phoneBook.newEntry(entry3.toString());
		expectedResultArray.add(entry3);
		
		actualArray = new JSONArray(phoneBook.listAll());
		
		assertEquals("Expected result differs from actual",
				expectedResultArray,
				actualArray);
	}

	/**
	 * Test the remove entry functionality
	 */
	@Test
	public void testDeleteEntry() throws JSONException {
		phoneBook.deleteEntry(0);
		
		JSONArray expectedResultArray = new JSONArray();
		expectedResultArray.add(entry2);
		
		JSONArray actualArray = new JSONArray(phoneBook.listAll()); 
		assertEquals("Expected result differs from actual",
				expectedResultArray,
				actualArray);
	}

	/**
	 * Test the update entry functionality
	 */
	@Test
	public void testUpdateEntry() throws JSONException {
		
		JSONObject tempUpdate = new JSONObject();
		tempUpdate.put(Entry.JSON_ADDRESS, "NEW ADDRESS");
		
		phoneBook.updateEntry(tempUpdate.toString(), 0);
		
		JSONArray expectedResultArray = new JSONArray();
		expectedResultArray.add(entry1);
		expectedResultArray.add(entry2);
		
		JSONArray actualArray = new JSONArray(phoneBook.listAll()); 
		
		assertFalse("Expected result should differ from actual", expectedResultArray.equals(actualArray));
		
		expectedResultArray = new JSONArray();
		entry1.put(Entry.JSON_ADDRESS, "NEW ADDRESS");
		expectedResultArray.add(entry1);
		expectedResultArray.add(entry2);
		
		assertEquals("Expected result differs from actual",
				expectedResultArray,
				actualArray);
	}

	/**
	 * Test the search entries functionality
	 */
	@Test
	public void testSearch() throws JSONException {
		JSONArray actualArray = new JSONArray(phoneBook.getBySurname(entry1.getString(Entry.JSON_SURNAME)));
		
		JSONArray expectedResultArray = new JSONArray();
		expectedResultArray.add(entry1);
		
		assertEquals("Expected result differs from actual",
				expectedResultArray,
				actualArray);
		
		JSONArray noneArray = new JSONArray(phoneBook.getBySurname("NONE"));
		
		assertTrue("Expected result should be empty", noneArray.size() == 0);
	}

	/**
	 * Test the list entries functionality
	 */
	@Test
	public void testListAll() throws JSONException {
		JSONArray expectedResultArray = new JSONArray();
		expectedResultArray.add(entry1);
		expectedResultArray.add(entry2);
		
		JSONArray actualArray = new JSONArray(phoneBook.listAll()); 
		
		assertEquals("Expected result differs from actual",
				expectedResultArray,
				actualArray);
	}

}
