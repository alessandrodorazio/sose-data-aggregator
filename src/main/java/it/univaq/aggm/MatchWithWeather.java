package it.univaq.aggm;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MatchWithWeather")
public class MatchWithWeather {
	private Match match;
	private Weather weather;
	
	public MatchWithWeather() {
		
	}
	
	public MatchWithWeather(Match match, Weather weather) {
		this.match = match;
		this.weather = weather;
	}
	
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}	
	public Weather getWeather() {
		return weather;
	}
	public void setWeather(Weather weather) {
		this.weather = weather;
	}
	
	
	
}
