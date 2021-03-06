package jumpstart.web.pages.examples.navigation;

public class ReturnTypesPageObject {

	private String parameter;

	// set() is public so that other pages can use it to set up this page.
	
	public void set(String parameter) {
		this.parameter = parameter;
	}
	
	// onPassivate() is called by Tapestry to get the activation context to put in the URL.
	
	String onPassivate() {
		return parameter;
	}

	// onActivate() is called by Tapestry to pass in the activation context from the URL.

	void onActivate(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}
}
