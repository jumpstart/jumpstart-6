package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

public class Mixins {
	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

	// Generally useful bits and pieces

	@InjectComponent("names")
	private Form form;

	@InjectComponent("firstName")
	private TextField firstNameField;

	@InjectComponent("lastName")
	private TextField lastNameField;

	// The code

	void onValidateFromNames() {
		if (firstName == null || firstName.trim().equals("")) {
			form.recordError(firstNameField, "First Name is required.");
		}
		if (lastName == null || lastName.trim().equals("")) {
			form.recordError(lastNameField, "Last Name is required.");
		}
	}

	Object onSuccess() {
		// In a real application we would go to the next page.
		return null;
	}
}
