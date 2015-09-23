package rest.addressbook;

import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * Java object to represent a Entry in a phone book, with provisions for use of JAXB (library used did not support JSON marshaling).
 * @author Sean Cawood
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name="entry")
public class Entry {
	
	public final static String  JSON_ID= "id";
	public final static String  JSON_FORENAME= "forename";
	public final static String  JSON_SURNAME= "surname";
	public final static String  JSON_ADDRESS= "address";
	public final static String  JSON_PHONE= "phone";
	
	private long id;
	public String forename;
	protected String surname;
	protected String address;
	protected String phone;

	/**
	 * Null constructor used to allow use of JAXB marshalling
	 */
	public Entry() {
		forename = "";
		surname = "";
		phone = "";
		address = null;
	}
	
	/**
	 * Constructs entry object based on given JSONObject
	 * @param input JSONObject to parse
	 * @param setId Whether the id filed is set and the book id count is updated
	 */
	public Entry(JSONObject input, boolean setId) {
		forename = input.optString(JSON_FORENAME);
		surname = input.optString(JSON_SURNAME);
		phone = input.optString(JSON_PHONE);
		address = input.optString(JSON_ADDRESS);
		if (setId) {
			id = Book.nextEntryId();
		}
	}
	
	/**
	 * Sets the id value of entry based on next available id in phone book
	 */
	public void setId() {
		id = Book.nextEntryId();
	}
	
	/**
	 * Return entries forename
	 * @return forename string
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * Set entries forename
	 * @param forename Forename to set
	 */
	@XmlElement
	public void setForename(String forename) {
		this.forename = forename;
	}

	/**
	 * Get entries surname
	 * @return surname string
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Set the entries surname
	 * @param surname surname string
	 */
	@XmlElement
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Get the entries address
	 * @return address string
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Set entry address
	 * @param address Entries address
	 */
	@XmlElement
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Return entry phone number
	 * @return phone number
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set the phone number of entry
	 * @param phone Number to set
	 */
	@XmlElement
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * Get the Id of entry
	 * @return entry id
	 */
	@XmlElement
	public long getId() {
		return this.id;
	}
	
	/**
	 * Validate the entry fields are filled out as required
	 * @return if Entry values are valid
	 */
	public boolean validate() {
		return (!(forename.equals("")
				|| surname.equals("")
				|| phone.equals("")));
	}
	
	/**
	 * Return JSON object representation
	 * @return JSONOBject of this Entry
	 */
	public JSONObject asJSON() {
		JSONObject toReturn = new JSONObject();
		
		try {
			toReturn.put(JSON_ID, id);
			toReturn.put(JSON_FORENAME, forename);
			toReturn.put(JSON_SURNAME, surname);
			toReturn.put(JSON_PHONE, phone);
			if (address != null) {
				toReturn.put(JSON_ADDRESS, address);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	/**
	 * Update Entry values from given JSONObject
	 * @param updateValue JSON to parse through
	 * @return Updated entry
	 */
	public Entry update(JSONObject updateValue) {
		for (Object keyObj : updateValue.keySet()) {
			if (keyObj instanceof String && !keyObj.equals(JSON_ID)) {
				try {
					Field field = this.getClass().getDeclaredField((String) keyObj);
					field.set(this, updateValue.getString((String) keyObj));
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | JSONException e) {
					// Do nothing
				}
			}
		}
		return this;
	}
}
