package rest.addressbook;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;

import org.apache.wink.common.model.wadl.Application;
import org.apache.wink.common.model.wadl.WADLGenerator;

/**
 * Class to allow JAX-RS servelet to serve a XML file documenting available rest interfaces.
 * @author Sean Cawood
 */
@Path("/xml")
public class Resources {
	@Context
	javax.ws.rs.core.Application app;

	@Path("resources.xml")
	@GET
	@Produces("application/vnd.sun.wadl+xml")
	public Application ok() {
		return getOptions();
	}

	@OPTIONS
	@Produces("application/vnd.sun.wadl+xml")
	public org.apache.wink.common.model.wadl.Application getOptions() {
		org.apache.wink.common.model.wadl.Application wadlAppDoc = new WADLGenerator()
				.generate("", app.getClasses());
		/*
		 * modify the wadlAppDoc JAXB model if you want to add additional
		 * information
		 */
		return wadlAppDoc;
	}
}
