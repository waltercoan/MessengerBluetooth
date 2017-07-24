import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class PhoneVendorDiscover {
	public static PhoneVendorDiscover instance;
	public Hashtable<String, String> macs = new Hashtable<String, String>();
	public static PhoneVendorDiscover getInstance(String file){
		if(instance == null){
			instance = new PhoneVendorDiscover(file);
		}
		return instance;
	}
	public static PhoneVendorDiscover getInstance(){
		if(instance == null){
			instance = new PhoneVendorDiscover();
		}
		return instance;
	}
	private PhoneVendorDiscover(){
	}
	private PhoneVendorDiscover(String file) {
		loadFile(file);
	}
	public String getPort(String mac){
		try{
			for(String key:macs.keySet()){
				if(key.equalsIgnoreCase(mac.substring(0, 6))){
					return macs.get(key);
				}
			}
			return null;
		}catch(Exception e){
			return null;
		}
	}
	private void loadFile(String file){
		String mac = null;
		String port = null;
		try {
			//InputStream input = ClassLoader.getSystemResourceAsStream("resource/macs.xml");
			FileInputStream input = new FileInputStream(new File(file));
			XMLInputFactory xmli = XMLInputFactory.newInstance();
			XMLStreamReader xmlr = xmli.createXMLStreamReader(input);

			while(xmlr.hasNext()){
				if(xmlr.getEventType() == XMLStreamReader.START_ELEMENT){
					//Le os valores dentro da TAH PARA	
					if(xmlr.getLocalName().equals("Port"))
						port = xmlr.getElementText();
					if(xmlr.getLocalName().equals("Mac")){
						mac = xmlr.getElementText();
						macs.put(mac, port);
					}
				}
				xmlr.next();
			}
			input.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
