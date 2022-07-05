package it.univaq.aggm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

@Path("aggregator")
@Produces("text/xml")
@WebService(endpointInterface="it.univaq.aggm.ServiceRepositoryInterface")
public class ServiceRepository implements ServiceRepositoryInterface {
	
	@GET
	@Path("/get-complete-matches")
	public ArrayList<CompleteMatch> matchesWithWeather() throws IOException, ParserConfigurationException, SAXException {
		return DataAggregator.getMatchesWithAllData();
	}

}
