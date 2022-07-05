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
	
	private static Team getTeamFromElement(Element element) {
		int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
		String name = element.getElementsByTagName("name").item(0).getTextContent();
		Team team = new Team();
		team.setId(id);
		team.setName(name);
		return team;
	}
	
	private static Bet getBetFromElement(Element element) {
		Bet bet = new Bet();
		double localTeamQuote = Double.parseDouble(element.getElementsByTagName("localTeamQuote").item(0).getTextContent());
		double visitorTeamQuote = Double.parseDouble(element.getElementsByTagName("visitorTeamQuote").item(0).getTextContent());
		bet.setLocalTeamQuote(localTeamQuote);
		bet.setVisitorTeamQuote(visitorTeamQuote);
		return bet;
	}
	
	private static Weather getWeatherFromElement(Element element) {
		double temperature = Double.parseDouble(element.getElementsByTagName("temperature").item(0).getTextContent());
		String name = element.getElementsByTagName("name").item(0).getTextContent();
		String description = element.getElementsByTagName("description").item(0).getTextContent();

		Weather w = new Weather();
		w.setName(name);
		w.setTemperature(temperature);
		w.setDescription(description);
		return w;
	}

	public static ArrayList<CompleteMatch> getMatchesWithAllData() throws IOException, SAXException, ParserConfigurationException {
		ArrayList<CompleteMatch> result = new ArrayList<CompleteMatch>();
		ArrayList<MatchWithWeather> arrayMatchWeather = new ArrayList<MatchWithWeather>();
		ArrayList<MatchWithBet> arrayMatchBet = new ArrayList<MatchWithBet>();
		// call football-bet
		Document docFootballBet = getFootballBetProsumerDocument();
		NodeList list = docFootballBet.getElementsByTagName("MatchWithBet");
		for (int temp = 0; temp < list.getLength(); temp++) {
			 Node node = list.item(temp);
			 if (node.getNodeType() == Node.ELEMENT_NODE) {
				 Element element = (Element) node;
				 Element bet = (Element) element.getElementsByTagName("bet").item(0);
				 Bet b = getBetFromElement(bet);
				 //System.out.println("LOCAL TEAM QUOTE " + b.getLocalTeamQuote());
				 //System.out.println("VISITOR TEAM QUOTE " + b.getVisitorTeamQuote());
				 Element match = (Element) element.getElementsByTagName("match").item(0);
				 Element localTeam = (Element) match.getElementsByTagName("localTeam").item(0);
				 Team local = getTeamFromElement(localTeam);
				 //System.out.println("TEAM ID " + local.getId());
				 //System.out.println("TEAM NAME " + local.getName());
				 Element visitorTeam = (Element) match.getElementsByTagName("visitorTeam").item(0);
				 Team visitor = getTeamFromElement(visitorTeam);
				 //System.out.println("VISITOR ID " + visitor.getId());
				 //System.out.println("VISITOR NAME " + visitor.getName());
				 Match m = new Match();
				 m.setLocalTeam(local);
				 m.setVisitorTeam(visitor);
				 MatchWithBet mb = new MatchWithBet();
				 mb.setMatch(m);
				 mb.setBet(b);
				 arrayMatchBet.add(mb);
				
			 }
		}
		
		Document docFootballWeather = getFootballWeatherProsumerDocument();
		list = docFootballWeather.getElementsByTagName("MatchWithWeather");
		for (int temp = 0; temp < list.getLength(); temp++) {
			 Node node = list.item(temp);
			 if (node.getNodeType() == Node.ELEMENT_NODE) {
				 Element element = (Element) node;
				 Element match = (Element) element.getElementsByTagName("match").item(0);
				 Element localTeam = (Element) match.getElementsByTagName("localTeam").item(0);
				 Team local = getTeamFromElement(localTeam);
				 //System.out.println("TEAM ID " + local.getId());
				 //System.out.println("TEAM NAME " + local.getName());
				 Element visitorTeam = (Element) match.getElementsByTagName("visitorTeam").item(0);
				 Team visitor = getTeamFromElement(visitorTeam);
				 //System.out.println("VISITOR ID " + visitor.getId());
				 //System.out.println("VISITOR NAME " + visitor.getName());
				 Element weather = (Element) element.getElementsByTagName("weather").item(0);
				 Weather w = getWeatherFromElement(weather);
				 //System.out.println("CITY " + w.getName());
				 //System.out.println("TEMPERATURE " + w.getTemperature());
				 //System.out.println("DESCRIPTION " + w.getDescription());
				 int localScore = Integer.parseInt(match.getElementsByTagName("localTeamScore").item(0).getTextContent());
				 int visitorScore = Integer.parseInt(match.getElementsByTagName("visitorTeamScore").item(0).getTextContent());
				 
				 Match m = new Match();
				 m.setLocalTeam(local);
				 m.setLocalScore(localScore);
				 m.setVisitorTeam(visitor);
				 m.setVisitorScore(visitorScore);
				 MatchWithWeather mw = new MatchWithWeather();
				 mw.setMatch(m);
				 mw.setWeather(w);
				 arrayMatchWeather.add(mw);
				 
			 }
		}
		
		arrayMatchBet.forEach((mb) -> {
			arrayMatchWeather.forEach((mw) -> {
				if(mb.getMatch().getLocalTeam().getId() == mw.getMatch().getLocalTeam().getId() && mb.getMatch().getVisitorTeam().getId() == mw.getMatch().getVisitorTeam().getId() ) {
					CompleteMatch cm = new CompleteMatch();
					cm.setBet(mb.getBet());
					cm.setMatch(mw.getMatch());
					cm.setWeather(mw.getWeather());
					result.add(cm);
				}
			});
		});
		
		// call football-weather
		// do a cycle to get a unique CompleteMatch array
		// return result array
		return result;
	}
	
	private static Document getFootballBetProsumerDocument() throws IOException, SAXException, ParserConfigurationException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
			.url("http://localhost:8084/matches-with-bets/get")
			.get()
			.build();
		Response response = client.newCall(request).execute();
		String data = response.body().string();
		System.out.println(data.toString());
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(data)));
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	public static void callFootballBetProsumer() throws IOException, SAXException, ParserConfigurationException {
		Document doc = getFootballBetProsumerDocument();
		NodeList list = doc.getElementsByTagName("MatchWithBet");
		for (int temp = 0; temp < list.getLength(); temp++) {
			 Node node = list.item(temp);
			 if (node.getNodeType() == Node.ELEMENT_NODE) {
				 Element element = (Element) node;
				 Element match = (Element) element.getElementsByTagName("Match").item(0);
				 Element localTeam = (Element) match.getElementsByTagName("localTeam").item(0);
				 Team local = getTeamFromElement(localTeam);
				 Element visitorTeam = (Element) match.getElementsByTagName("visitorTeam").item(0);
				 Team visitor = getTeamFromElement(visitorTeam);
				 Element bet = (Element) match.getElementsByTagName("Bet").item(0);
				 Bet b = getBetFromElement(bet);
			 }
		}
	}
	
	private static Document getFootballWeatherProsumerDocument() throws IOException, SAXException, ParserConfigurationException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
			.url("http://localhost:8083/cities/matches-with-weather")
			.get()
			.build();
		Response response = client.newCall(request).execute();
		String data = response.body().string();
		System.out.println(data.toString());
		System.out.println("DATAAA");
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(data)));
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	public static void callFootballWeatherProsumer() throws IOException, SAXException, ParserConfigurationException {
		Document doc = getFootballWeatherProsumerDocument();
	}
}
