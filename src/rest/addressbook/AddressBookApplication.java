package rest.addressbook;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.wink.common.model.wadl.WADLGenerator;

/**
 * Class to return all classes for this JAX-RS application, to allow
 * JAX-RS servelet to serve.
 * @author Sean Cawood
 */
public class AddressBookApplication extends Application {
	 public Set<Class<?>> getClasses() {
	 Set<Class<?>> classes = new HashSet<Class<?>>();
	 classes.add(Book.class);
	 classes.add(Resources.class);
	 return classes;
	 }

}
