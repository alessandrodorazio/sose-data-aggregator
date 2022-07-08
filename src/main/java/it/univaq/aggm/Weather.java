package it.univaq.aggm;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Weather")
public class Weather {
	private String name;
	private double temperature;
	private String description;
	
	public Weather() {
		
	}
	
	public Weather(String name, double temperature, String description) {
		this.name = name;
		this.temperature = temperature;
		this.description = description;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTemperature(double temp) {
		this.temperature = temp;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getTemperature() {
		return this.temperature;
	}
	
	public String getDescription() {
		return this.description;
	}
}
