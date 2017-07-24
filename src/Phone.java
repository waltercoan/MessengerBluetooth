import java.io.Serializable;
import java.util.Date;


public class Phone implements Serializable{
	private String address;
	private String name;
	private String email;
	private Date lastVisit;
	private int numberVisit;
	private boolean ignore;
	private String secretCode;
	private int port = -1;
	
	public Phone() {}
	
	public Phone(String address) {
		this.address = address;
		lastVisit = new Date();
		numberVisit = 0;
	}
	public void addNumberVisit(){
		numberVisit++;
		lastVisit = new Date();
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getLastVisit() {
		return lastVisit;
	}
	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}
	public int getNumberVisit() {
		return numberVisit;
	}
	public void setNumberVisit(int numberVisit) {
		this.numberVisit = numberVisit;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public String getSecretCode() {
		return secretCode;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
}
