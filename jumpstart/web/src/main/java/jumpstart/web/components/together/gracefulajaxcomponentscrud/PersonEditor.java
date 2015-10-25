package jumpstart.web.components.together.gracefulajaxcomponentscrud;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.business.domain.person.iface.IPersonManagerServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.components.CustomForm;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * This component will trigger the following events on its container (which in this example is the page):
 * {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#CANCEL_CREATE} ,
 * {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#SUCCESSFUL_CREATE} (Long personId),
 * {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#FAILED_CREATE} ,
 * {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#TO_UPDATE} (Long personId),
 * {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#CANCEL_UPDATE} (Long personId),
 * {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#SUCCESSFUL_UPDATE} (Long personId),
 * {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#FAILED_UPDATE} (Long personId),
 * personId), {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#SUCCESSFUL_DELETE} (Long
 * personId), {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#FAILED_DELETE} (Long
 * personId), {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#TO_CONFIRM_DELETE} (Long
 * personId), {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#CANCEL_CONFIRM_DELETE}
 * (Long personId),
 * {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#SUCCESSFUL_CONFIRM_DELETE} (Long
 * personId), {@link jumpstart.web.components.examples.ajax.gracefulcomponentscrud.PersonEditor#FAILED_CONFIRM_DELETE}
 * (Long personId)
 */
// @Events is applied to a component solely to document what events it may
// trigger. It is not checked at runtime.
@Events({ PersonEditor.CANCEL_CREATE, PersonEditor.SUCCESSFUL_CREATE, PersonEditor.FAILED_CREATE,
		PersonEditor.TO_UPDATE, PersonEditor.CANCEL_UPDATE, PersonEditor.SUCCESSFUL_UPDATE, PersonEditor.FAILED_UPDATE,
		PersonEditor.SUCCESSFUL_DELETE, PersonEditor.FAILED_DELETE, PersonEditor.TO_CONFIRM_DELETE,
		PersonEditor.CANCEL_CONFIRM_DELETE, PersonEditor.SUCCESSFUL_CONFIRM_DELETE, PersonEditor.FAILED_CONFIRM_DELETE })
public class PersonEditor {
	public static final String CANCEL_CREATE = "cancelCreate";
	public static final String SUCCESSFUL_CREATE = "successfulCreate";
	public static final String FAILED_CREATE = "failedCreate";
	public static final String TO_UPDATE = "toUpdate";
	public static final String CANCEL_UPDATE = "cancelUpdate";
	public static final String SUCCESSFUL_UPDATE = "successfulUpdate";
	public static final String FAILED_UPDATE = "failedUpdate";
	public static final String SUCCESSFUL_DELETE = "successfulDelete";
	public static final String FAILED_DELETE = "failedDelete";
	public static final String TO_CONFIRM_DELETE = "toConfirmDelete";
	public static final String CANCEL_CONFIRM_DELETE = "cancelConfirmDelete";
	public static final String SUCCESSFUL_CONFIRM_DELETE = "successfulConfirmDelete";
	public static final String FAILED_CONFIRM_DELETE = "failedConfirmDelete";

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	public enum Mode {
		CREATE, REVIEW, UPDATE, CONFIRM_DELETE;
	}

	// Parameters

	@Parameter(required = true)
	@Property
	private Mode mode;

	@Parameter(required = true)
	@Property
	private Long personId;

	// Screen fields

	@Property
	private Person person;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String deleteMessage;

	// Work fields

	// This carries version through the redirect that follows a server-side
	// validation failure.
	@Persist(PersistenceConstants.FLASH)
	private Integer versionFlash;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@EJB
	private IPersonManagerServiceLocal personManagerService;

	@InjectComponent
	private CustomForm createForm;

	@InjectComponent
	private CustomForm updateForm;

	@InjectComponent
	private Form confirmDeleteForm;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private Request request;

	@Inject
	private Messages messages;

	// The code

	// setupRender() is called by Tapestry right before it starts rendering the
	// component.

	void setupRender() {

		if (mode == Mode.REVIEW) {
			if (personId == null) {
				person = null;
				// Handle null person in the template.
			}
			else {
				person = personFinderService.findPerson(personId);
				// Handle null person in the template.
			}
		}

	}

	// /////////////////////////////////////////////////////////////////////
	// CREATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "cancelCreate"

	boolean onCancelCreate() {
		// Return false, which means we haven't handled the event so bubble it
		// up.
		// This method is here solely as documentation, because without this
		// method the event would bubble up anyway.
		return false;
	}

	// Component "createForm" bubbles up the PREPARE event when it is rendered
	// or submitted

	void onPrepareFromCreateForm() throws Exception {
		// Instantiate a Person for the form data to overlay.
		person = new Person();
	}

	// Component "createForm" bubbles up the VALIDATE event when it is submitted

	void onValidateFromCreateForm() {

		if (createForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			createForm.recordError("Sorry, but Create is not allowed in Demo mode.");
			return;
		}

		try {
			person = personManagerService.createPerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a
			// user-friendly message.
			createForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	// Component "createForm" bubbles up SUCCESS or FAILURE when it is
	// submitted, depending on whether VALIDATE
	// records an error

	boolean onSuccessFromCreateForm() {
		// We want to tell our containing page explicitly what person we've
		// created, so we trigger new event
		// "successfulCreate" with a parameter. It will bubble up because we
		// don't have a handler method for it.
		componentResources.triggerEvent(SUCCESSFUL_CREATE, new Object[] { person.getId() }, null);
		// We don't want "success" to bubble up, so we return true to say we've
		// handled it.
		return true;
	}

	boolean onFailureFromCreateForm() {
		// Rather than letting "failure" bubble up which doesn't say what you
		// were trying to do, we trigger new event
		// "failedCreate". It will bubble up because we don't have a handler
		// method for it.
		componentResources.triggerEvent(FAILED_CREATE, null, null);
		// We don't want "failure" to bubble up, so we return true to say we've
		// handled it.
		return true;
	}

	// /////////////////////////////////////////////////////////////////////
	// REVIEW
	// /////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////
	// UPDATE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "toUpdate"

	boolean onToUpdate(Long personId) {
		// Return false, which means we haven't handled the event so bubble it
		// up.
		// This method is here solely as documentation, because without this
		// method the event would bubble up anyway.
		return false;
	}

	// Handle event "cancelUpdate"

	boolean onCancelUpdate(Long personId) {
		// Return false, which means we haven't handled the event so bubble it
		// up.
		// This method is here solely as documentation, because without this
		// method the event would bubble up anyway.
		return false;
	}

	// Component "updateForm" bubbles up the PREPARE_FOR_RENDER event during
	// form render

	void onPrepareForRenderFromUpdateForm(Long personId) {
		this.personId = personId;

		if (request.isXHR()) {

			// If the form is valid then we're not redisplaying due to error, so
			// get the person.

			if (updateForm.isValid()) {
				person = personFinderService.findPerson(this.personId);
				// Handle null person in the template.
			}

		}
		else {
			person = personFinderService.findPerson(personId);
			// Handle null person in the template.

			// If the form has errors then we're redisplaying after a redirect.
			// Form will restore your input values but it's up to us to restore
			// Hidden values.

			if (updateForm.getHasErrors()) {
				if (person != null) {
					person.setVersion(versionFlash);
				}
			}

		}

	}

	// Component "updateForm" bubbles up the PREPARE_FOR_SUBMIT event during
	// form submission

	void onPrepareForSubmitFromUpdateForm(Long personId) {
		this.personId = personId;

		// Get objects for the form fields to overlay.
		person = personFinderService.findPerson(this.personId);

		if (person == null) {
			person = new Person();
			updateForm.recordError("Person has been deleted by another process.");
		}
	}

	// Component "updateForm" bubbles up the VALIDATE event when it is submitted

	void onValidateFromUpdateForm() {

		if (updateForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		try {
			personManagerService.changePerson(person);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a
			// user-friendly message.
			updateForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	// Component "updateForm" bubbles up SUCCESS or FAILURE when it is
	// submitted, depending on whether VALIDATE
	// records an error

	boolean onSuccessFromUpdateForm() {
		// We want to tell our containing page explicitly what person we've
		// updated, so we trigger new event
		// "successfulUpdate" with a parameter. It will bubble up because we
		// don't have a handler method for it.
		componentResources.triggerEvent(SUCCESSFUL_UPDATE, new Object[] { personId }, null);
		// We don't want "success" to bubble up, so we return true to say we've
		// handled it.
		return true;
	}

	boolean onFailureFromUpdateForm() {
		if (!request.isXHR()) {
			versionFlash = person.getVersion();
		}

		// Rather than letting "failure" bubble up which doesn't say what you
		// were trying to do, we trigger new event
		// "failedUpdate". It will bubble up because we don't have a handler
		// method for it.
		componentResources.triggerEvent(FAILED_UPDATE, new Object[] { personId }, null);
		// We don't want "failure" to bubble up, so we return true to say we've
		// handled it.
		return true;
	}

	// /////////////////////////////////////////////////////////////////////
	// DELETE
	// /////////////////////////////////////////////////////////////////////

	// Handle event "delete"

	boolean onDelete(Long personId, Integer personVersion) {
		this.personId = personId;

		// If request is AJAX then the user has pressed Delete..., was presented
		// with a Confirm dialog, and OK'd it.

		if (request.isXHR()) {
			boolean successfulDelete = false;

			if (demoModeStr != null && demoModeStr.equals("true")) {
				deleteMessage = "Sorry, but Delete is not allowed in Demo mode.";
			}
			else {

				try {
					personManagerService.deletePerson(personId, personVersion);
					successfulDelete = true;
				}
				catch (Exception e) {
					// Display the cause. In a real system we would try harder
					// to get a user-friendly message.
					deleteMessage = ExceptionUtil.getRootCauseMessage(e);
				}

			}

			if (successfulDelete) {
				// Trigger new event "successfulDelete" (which in this example
				// will bubble up to the page).
				componentResources.triggerEvent(SUCCESSFUL_DELETE, new Object[] { personId }, null);
			}
			else {
				// Trigger new event "failedDelete" (which in this example will
				// bubble up to the page).
				componentResources.triggerEvent(FAILED_DELETE, new Object[] { personId }, null);
			}
		}

		// Else, (JavaScript disabled) user has pressed Delete..., but not yet
		// confirmed so go to confirmation mode.

		else {
			// Trigger new event "toConfirmDelete" (which in this example will
			// bubble up to the page).
			componentResources.triggerEvent(TO_CONFIRM_DELETE, new Object[] { personId }, null);
		}

		// We don't want "delete" to bubble up, so we return true to say we've
		// handled it.
		return true;
	}

	// /////////////////////////////////////////////////////////////////////
	// CONFIRM DELETE - used only when JavaScript is disabled.
	// /////////////////////////////////////////////////////////////////////

	// Handle event "cancelConfirmDelete"

	boolean onCancelConfirmDelete(Long personId) {
		// Return false, which means we haven't handled the event so bubble it
		// up.
		// This method is here solely as documentation, because without this
		// method the event would bubble up anyway.
		return false;
	}

	// Component "confirmDeleteForm" bubbles up the PREPARE_FOR_RENDER event
	// during form render

	void onPrepareForRenderFromConfirmDeleteForm() {
		person = personFinderService.findPerson(personId);
		// Handle null person in the template.

		// If the form has errors then we're redisplaying after a redirect.
		// Form will restore your input values but it's up to us to restore
		// Hidden values.

		if (confirmDeleteForm.getHasErrors()) {
			if (person != null) {
				person.setVersion(versionFlash);
			}
		}
	}

	// Component "confirmDeleteForm" bubbles up the PREPARE_FOR_SUBMIT event
	// during form submission

	void onPrepareForSubmitFromConfirmDeleteForm() {
		// Get objects for the form fields to overlay.
		person = personFinderService.findPerson(personId);

		if (person == null) {
			person = new Person();
			confirmDeleteForm.recordError("Person has already been deleted by another process.");
		}
	}

	// Component "confirmDeleteForm" bubbles up the VALIDATE event when it is
	// submitted

	void onValidateFromConfirmDeleteForm() {

		if (confirmDeleteForm.getHasErrors()) {
			// We get here only if a server-side validator detected an error.
			return;
		}

		if (demoModeStr != null && demoModeStr.equals("true")) {
			confirmDeleteForm.recordError("Sorry, but Delete is not allowed in Demo mode.");
		}
		else {

			try {
				personManagerService.deletePerson(personId, person.getVersion());
			}
			catch (Exception e) {
				// Display the cause. In a real system we would try harder to
				// get a user-friendly message.
				confirmDeleteForm.recordError(ExceptionUtil.getRootCauseMessage(e));
			}

		}

	}

	// Component "confirmDeleteForm" bubbles up SUCCESS or FAILURE when it is
	// submitted, depending on whether
	// VALIDATE records an error

	boolean onSuccessFromConfirmDeleteForm() {
		// We want to tell our containing page explicitly what person we've
		// deleted, so we trigger new event
		// "successfulDelete" with a parameter. It will bubble up because we
		// don't have a handler method for it.
		componentResources.triggerEvent(SUCCESSFUL_CONFIRM_DELETE, new Object[] { person.getId() }, null);
		// We don't want "success" to bubble up, so we return true to say we've
		// handled it.
		return true;
	}

	boolean onFailureFromConfirmDeleteForm() {
		versionFlash = person.getVersion();

		// Rather than letting "failure" bubble up which doesn't say what you
		// were trying to do, we trigger new event
		// "failedDelete". It will bubble up because we don't have a handler
		// method for it.
		componentResources.triggerEvent(FAILED_CONFIRM_DELETE, new Object[] { person.getId() }, null);
		// We don't want "failure" to bubble up, so we return true to say we've
		// handled it.
		return true;
	}

	// /////////////////////////////////////////////////////////////////////
	// OTHER
	// /////////////////////////////////////////////////////////////////////

	// Getters

	public boolean isModeCreate() {
		return mode == Mode.CREATE;
	}

	public boolean isModeReview() {
		return mode == Mode.REVIEW;
	}

	public boolean isModeUpdate() {
		return mode == Mode.UPDATE;
	}

	public boolean isModeConfirmDelete() {
		return mode == Mode.CONFIRM_DELETE;
	}

	public boolean isModeNull() {
		return mode == null;
	}

	public String getPersonRegion() {
		// Follow the same naming convention that the Select component uses
		return messages.get(Regions.class.getSimpleName() + "." + person.getRegion().name());
	}

	public String getDatePattern() {
		return "dd/MM/yyyy";
	}

	public Format getDateFormat() {
		return new SimpleDateFormat(getDatePattern());
	}
}
