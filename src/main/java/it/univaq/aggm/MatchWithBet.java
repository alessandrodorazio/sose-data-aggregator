package it.univaq.aggm;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MatchWithBet")
public class MatchWithBet {
	private Match match;
	private Bet bet;
	
	public MatchWithBet() {
		
	}
	
	public MatchWithBet(Match match, Bet bet) {
		this.match = match;
		this.bet = bet;
	}
	
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}
	public Bet getBet() {
		return bet;
	}
	public void setBet(Bet bet) {
		this.bet = bet;
	}
	
}
