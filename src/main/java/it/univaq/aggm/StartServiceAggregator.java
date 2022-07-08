package it.univaq.aggm;

import java.util.List;

import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

public class StartServiceAggregator {

	public static void main(String[] args) {
		String restAddress = "http://localhost:8086/";
		String soapAddress = "http://localhost:8096/aggregator";
		startRest(restAddress);
		startSoap(soapAddress);
	}
	
	public static void startRest(String address) {
		JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(ServiceRepository.class);
        factoryBean.setResourceProvider(new SingletonResourceProvider(new ServiceRepository()));
        factoryBean.setAddress(address);
        Server server = factoryBean.create();
        System.out.println("Server ready...");
	}
	
	public static void startSoap(String address) {
		Endpoint ep = Endpoint.create(new ServiceRepository());
		List<Handler> handlerChain = ep.getBinding().getHandlerChain();
		ep.getBinding().setHandlerChain(handlerChain);
		ep.publish(address);
		System.out.println("SOAP server ready...");
	}

}
