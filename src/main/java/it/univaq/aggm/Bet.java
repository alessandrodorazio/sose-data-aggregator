package it.univaq.aggm;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Bet")
public class Bet {
	private double localTeamQuote;
	private double visitorTeamQuote;
	
	public Bet() {
		
	}
	
	public Bet(double localTeamQuote, double visitorTeamQuote) {
		this.localTeamQuote = localTeamQuote;
		this.visitorTeamQuote = visitorTeamQuote;
	}
	
	public double getLocalTeamQuote() {
		return localTeamQuote;
	}
	public void setLocalTeamQuote(double localTeamQuote) {
		this.localTeamQuote = localTeamQuote;
	}
	public double getVisitorTeamQuote() {
		return visitorTeamQuote;
	}
	public void setVisitorTeamQuote(double visitorTeamQuote) {
		this.visitorTeamQuote = visitorTeamQuote;
	}

	
	
}