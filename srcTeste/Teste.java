public class Teste {
	
	private String version;
	
	private String description;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void getInstance() {
		System.out.println("\t > Version: "+this.version);
		System.out.println("\t > Description: "+this.description);

	}

}
