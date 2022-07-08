package it.univaq.aggm;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class DataAggregator {
	
	

	public static ArrayList<CompleteMatch> getMatchesWithAllData() throws IOException, SAXException, ParserConfigurationException {
		ArrayList<CompleteMatch> result = new ArrayList<CompleteMatch>();
		// create arrays with all matches with weather and all matches with bet
		ArrayList<MatchWithWeather> arrayMatchWeather = new ArrayList<MatchWithWeather>();
		ArrayList<MatchWithBet> arrayMatchBet = new ArrayList<MatchWithBet>();
		// call football-bet prosumer
		Document docFootballBet = getFootballBetProsumerDocument();
		NodeList list = docFootballBet.getElementsByTagName("MatchWithBet"); // get all matches with bet
		for (int temp = 0; temp < list.getLength(); temp++) { // iterate over them
			 Node node = list.item(temp);
			 if (node.getNodeType() == Node.ELEMENT_NODE) {
				 Element element = (Element) node;
				 Element bet = (Element) element.getElementsByTagName("bet").item(0);				 
				 Element match = (Element) element.getElementsByTagName("match").item(0);
				 Element localTeam = (Element) match.getElementsByTagName("localTeam").item(0);
				 Element visitorTeam = (Element) match.getElementsByTagName("visitorTeam").item(0);
				 MatchWithBet mb = new MatchWithBet();
				 mb.setMatch(new Match(getTeamFromElement(localTeam), getTeamFromElement(visitorTeam)));
				 mb.setBet(getBetFromElement(bet));
				 arrayMatchBet.add(mb);
			 }
		}
		
		Document docFootballWeather = getFootballWeatherProsumerDocument();
		list = docFootballWeather.getElementsByTagName("MatchWithWeather"); // get all matches with weather
		for (int temp = 0; temp < list.getLength(); temp++) { // iterate over them
			 Node node = list.item(temp);
			 if (node.getNodeType() == Node.ELEMENT_NODE) {
				 Element element = (Element) node;
				 Element match = (Element) element.getElementsByTagName("match").item(0);
				 Element localTeam = (Element) match.getElementsByTagName("localTeam").item(0);
				 Element visitorTeam = (Element) match.getElementsByTagName("visitorTeam").item(0);
				 int localScore = Integer.parseInt(match.getElementsByTagName("localTeamScore").item(0).getTextContent());
				 int visitorScore = Integer.parseInt(match.getElementsByTagName("visitorTeamScore").item(0).getTextContent());
				 Element weather = (Element) element.getElementsByTagName("weather").item(0);
				 String coordinates = match.getElementsByTagName("coordinates").item(0).getTextContent();
				 MatchWithWeather mw = new MatchWithWeather(); // create match with weather 
				 mw.setMatch(new Match(getTeamFromElement(localTeam), localScore, getTeamFromElement(visitorTeam), visitorScore,coordinates));
				 mw.setWeather(getWeatherFromElement(weather));
				 arrayMatchWeather.add(mw);
				 
			 }
		}
		
		arrayMatchBet.forEach((mb) -> { // loop over matches with bets and matches with weather
			arrayMatchWeather.forEach((mw) -> {
				// merge them into a complete match if they are referred to same match
				if(mb.getMatch().getLocalTeam().getId() == mw.getMatch().getLocalTeam().getId() && mb.getMatch().getVisitorTeam().getId() == mw.getMatch().getVisitorTeam().getId() ) {
					CompleteMatch cm = new CompleteMatch();
					cm.setBet(mb.getBet());
					cm.setMatch(mw.getMatch());
					cm.setWeather(mw.getWeather());
					result.add(cm);
				}
			});
		});
		return result;
	}
	
	// get XML document from football-bet prosumer
	private static Document getFootballBetProsumerDocument() throws IOException, SAXException, ParserConfigurationException {
		OkHttpClient client = new OkHttpClient();
		String url = "http://localhost:8084/matches-with-bets";
		Request request = new Request.Builder().url(url).get().build();
		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(data)));
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	private static Document getFootballWeatherProsumerDocument() throws IOException, SAXException, ParserConfigurationException {
		String url = "http://localhost:8083/football-weather/matches-with-weather";
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).get().build();
		Response response = client.newCall(request).execute();
		String data = response.body().string();
		System.out.println(data);
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(data)));
		doc.getDocumentElement().normalize();
		return doc;
	}

	// create team object from XML element
	private static Team getTeamFromElement(Element element) {
		int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
		String name = element.getElementsByTagName("name").item(0).getTextContent();
		return new Team(id, name);
	}
	
	// create bet object from XML element
	private static Bet getBetFromElement(Element element) {
		double localTeamQuote = Double.parseDouble(element.getElementsByTagName("localTeamQuote").item(0).getTextContent());
		double visitorTeamQuote = Double.parseDouble(element.getElementsByTagName("visitorTeamQuote").item(0).getTextContent());
		double tieQuote = Double.parseDouble(element.getElementsByTagName("tieQuote").item(0).getTextContent());
		return new Bet(localTeamQuote, visitorTeamQuote, tieQuote);
	}
	
	// create weather object from XML element
	private static Weather getWeatherFromElement(Element element) {
		double temperature = Double.parseDouble(element.getElementsByTagName("temperature").item(0).getTextContent());
		String name = element.getElementsByTagName("name").item(0).getTextContent();
		String description = element.getElementsByTagName("description").item(0).getTextContent();
		return new Weather(name, temperature, description);
	}
}
