package jumpstart.web.pages.together.easycrud.person;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.pages.together.easycrud.Persons;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;

public class PersonUpdate {

	// The activation context

	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

	// Work fields

	// This carries version through the redirect that follows a server-side
	// validation failure.
	@Persist(PersistenceConstants.FLASH)
	private Integer versionFlash;

	// Other pages

	@InjectPage
	private Persons indexPage;

	// Generally useful bits and pieces

	@InjectComponent
	private BeanEditForm personForm;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	// The code

	// onPassivate() is called by Tapestry to get the activation context to put
	// in the URL.

	Long onPassivate() {
		return personId;
	}

	// onActivate() is called by Tapestry to pass in the activation context from
	// the URL.

	void onActivate(Long personId) {
		this.personId = personId;
	}

	// setupRender() is called by Tapestry right before it starts rendering the
	// page.

	void setupRender() {

		// We're doing this here instead of in onPrepareForRender() because
		// person is used outside the form.

		person = personFinderService.findPerson(personId);
		// Handle null person in the template.

	}

	// PersonForm bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRender() {

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore
		// Hidden values.

		if (personForm.getHasErrors()) {
			if (person != null) {
				person.setVersion(versionFlash);
			}
		}
	}

	// PersonForm bubbles up the PREPARE_FOR_SUBMIT event during form
	// submission.

	void onPrepareForSubmit() {
		// Get objects for the form fields to overlay.
		person = personFinderService.findPerson(personId);

		if (person == null) {
			person = new Person();
			// Unfortunately this form error message will never be displayed
			// because we can't do <t:if test="user>
			// INSIDE the BeanEditForm.
			personForm
					.recordError("Person has been deleted by another process.");
		}
	}

	// PersonForm bubbles up the VALIDATE event when it is submitted

	void onValidateFromPersonForm() {

		if (personForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			personManagerService.changePerson(person);
		} catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a
			// user-friendly message.
			personForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	// PersonForm bubbles up SUCCESS or FAILURE when it is submitted, depending
	// on whether VALIDATE records an error

	Object onSuccess() {
		return indexPage;
	}

	void onFailure() {
		versionFlash = person.getVersion();
	}
}
