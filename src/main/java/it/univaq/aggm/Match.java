package it.univaq.aggm;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Match")
public class Match {
	private Team localTeam;
	private Team visitorTeam;
	private int localScore;
	private int visitorScore;
	private String coordinates;
	
	public Match() {
		
	}
	
	public Match(Team localTeam, Team visitorTeam) {
		this.localTeam = localTeam;
		this.visitorTeam = visitorTeam;
	}
	
	public Match(Team localTeam, int localScore, Team visitorTeam, int visitorScore) {
		this.localTeam = localTeam;
		this.localScore = localScore;
		this.visitorTeam = visitorTeam;
		this.visitorScore = visitorScore;
	}
	
	public Match(Team localTeam, int localScore, Team visitorTeam, int visitorScore, String coordinates) {
		this.localTeam = localTeam;
		this.localScore = localScore;
		this.visitorTeam = visitorTeam;
		this.visitorScore = visitorScore;
		this.coordinates = coordinates;
	}
	
	public void setLocalTeam(Team localTeam) {
		this.localTeam = localTeam;
	}
	
	public Team getLocalTeam() {
		return this.localTeam;
	}
	
	public void setVisitorTeam(Team visitorTeam) {
		this.visitorTeam = visitorTeam;
	}
	
	public Team getVisitorTeam() {
		return this.visitorTeam;
	}
	
	public void setLocalScore(int score) {
		this.localScore = score;
	}
	
	public int getLocalScore() {
		return this.localScore;
	}
	
	public void setVisitorScore(int score) {
		this.visitorScore = score;
	}
	
	public int getVisitorScore() {
		return this.visitorScore;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	
}
