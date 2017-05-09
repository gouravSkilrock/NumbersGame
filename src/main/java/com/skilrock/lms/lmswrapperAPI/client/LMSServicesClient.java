
package com.skilrock.lms.lmswrapperAPI.client;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class LMSServicesClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public LMSServicesClient(String thridPartyAddress) {
    	String address= "http://"+thridPartyAddress+"/services/LMSServices";
        create0();
        Endpoint LMSServicesPortTypeLocalEndpointEP = service0 .addEndpoint(new QName("http://lms.api.lms.skilrock.com", "LMSServicesPortTypeLocalEndpoint"), new QName("http://lms.api.lms.skilrock.com", "LMSServicesPortTypeLocalBinding"), "xfire.local://LMSServices");
        endpoints.put(new QName("http://lms.api.lms.skilrock.com", "LMSServicesPortTypeLocalEndpoint"), LMSServicesPortTypeLocalEndpointEP);
        Endpoint LMSServicesHttpPortEP = service0 .addEndpoint(new QName("http://lms.api.lms.skilrock.com", "LMSServicesHttpPort"), new QName("http://lms.api.lms.skilrock.com", "LMSServicesHttpBinding"), address/*"http://localhost:8081/LMSWrapper/services/LMSServices"*/);
        endpoints.put(new QName("http://lms.api.lms.skilrock.com", "LMSServicesHttpPort"), LMSServicesHttpPortEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((com.skilrock.lms.lmswrapperAPI.client.LMSServicesPortType.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://lms.api.lms.skilrock.com", "LMSServicesPortTypeLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://lms.api.lms.skilrock.com", "LMSServicesHttpBinding"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public LMSServicesPortType getLMSServicesPortTypeLocalEndpoint() {
        return ((LMSServicesPortType)(this).getEndpoint(new QName("http://lms.api.lms.skilrock.com", "LMSServicesPortTypeLocalEndpoint")));
    }

    public LMSServicesPortType getLMSServicesPortTypeLocalEndpoint(String url) {
        LMSServicesPortType var = getLMSServicesPortTypeLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public LMSServicesPortType getLMSServicesHttpPort() {
        return ((LMSServicesPortType)(this).getEndpoint(new QName("http://lms.api.lms.skilrock.com", "LMSServicesHttpPort")));
    }

    public LMSServicesPortType getLMSServicesHttpPort(String url) {
        LMSServicesPortType var = getLMSServicesHttpPort();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public static void main(String[] args) {
        

        LMSServicesClient client = new LMSServicesClient("");
        
		//create a default service endpoint
        LMSServicesPortType service = client.getLMSServicesHttpPort();
        
		//TODO: Add custom client code here
        		//
        		//service.yourServiceOperationHere();
        
		System.out.println("test client completed");
        		System.exit(0);
    }

}
