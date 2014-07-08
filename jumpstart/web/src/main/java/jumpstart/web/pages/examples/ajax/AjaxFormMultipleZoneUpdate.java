package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

public class AjaxFormMultipleZoneUpdate {

	// Screen fields

	@Property
	private String firstName;

	@Property
	private String lastName;

	@Property
	private Date birthday;

	// Generally useful bits and pieces

	@InjectComponent
	private Form ajaxForm;

	@InjectComponent("firstName")
	private TextField firstNameField;

	@InjectComponent("lastName")
	private TextField lastNameField;

	@InjectComponent("birthday")
	private DateField birthdayField;

	@InjectComponent
	private Zone formZone;

	@InjectComponent
	private Zone outZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	// The code

	void setupRender() {
		if (firstName == null && lastName == null && birthday == null) {
			firstName = "Humpty";
			lastName = "Dumpty";
			birthday = new Date(0);
		}
	}

	void onValidateFromAjaxForm() {

		if (firstName == null || firstName.trim().equals("")) {
			ajaxForm.recordError(firstNameField, "First Name is required.");
		}
		if (lastName == null || lastName.trim().equals("")) {
			ajaxForm.recordError(lastNameField, "Last Name is required.");
		}
		if (birthday == null) {
			ajaxForm.recordError(birthdayField, "Birthday is required.");
		} else {
			Date now = new Date();
			if (birthday.after(now)) {
				ajaxForm.recordError(birthdayField,
						"Birthday must be in the past.");
			}
		}
	}

	void onSuccess() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone).addRender(outZone);
		}
	}

	void onFailure() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone);
		}
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public Date getTime() {
		return new Date();
	}

}
