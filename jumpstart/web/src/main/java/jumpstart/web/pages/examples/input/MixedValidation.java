package jumpstart.web.pages.examples.input;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;

public class MixedValidation {

	// Screen fields

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String firstName;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String lastName;

	// Generally useful bits and pieces

	@InjectComponent("inputs")
	private Form form;

	@InjectComponent("firstName")
	private TextField firstNameField;

	@InjectComponent("lastName")
	private TextField lastNameField;

	// The code

	void onValidateFromInputs() {

		// Error if the names don't contain letters only

		if (firstName != null) {
			if (!firstName.matches("[A-Za-z]+")) {
				form.recordError(firstNameField,
						"First Name must contain letters only");
			}
		}

		if (lastName != null) {
			if (!lastName.matches("[A-Za-z]+")) {
				form.recordError(lastNameField,
						"Last Name must contain letters only");
			}
		}
	}

}
