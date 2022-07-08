package it.univaq.aggm;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Bet")
public class Bet {
	private double localTeamQuote;
	private double visitorTeamQuote;
	private double tieQuote;
	
	public Bet() {
		
	}
	
	public Bet(double localTeamQuote, double visitorTeamQuote, double tieQuote) {
		this.localTeamQuote = localTeamQuote;
		this.visitorTeamQuote = visitorTeamQuote;
		this.setTieQuote(tieQuote);
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

	public double getTieQuote() {
		return tieQuote;
	}

	public void setTieQuote(double tieQuote) {
		this.tieQuote = tieQuote;
	}

	
	
}