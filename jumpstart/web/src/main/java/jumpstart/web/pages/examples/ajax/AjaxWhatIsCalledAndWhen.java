package jumpstart.web.pages.examples.ajax;

import java.util.Date;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;

public class AjaxWhatIsCalledAndWhen {
	static final private String[] ALL_MAKES = new String[] { "Honda", "Toyota" };
	
	// Screen fields

	private String name1;

	private String name2;

	private String carMake;

	@Property
	private String[] carMakes;

	// Generally useful bits and pieces

	@Inject
	private Logger logger;

	@Inject
	private Request request;

	@InjectComponent
	private Zone time1Zone;

	@InjectComponent
	private Zone time2Zone;

	@InjectComponent
	private Zone formZone;

	@InjectComponent
	private Zone name2Zone;

	@InjectComponent
	private Zone carDisplayZone;

	// The code

	void pageLoaded() {
		logger.debug("   ");
		logger.debug("pageLoaded()");
	}

	void pageAttached() {
		logger.debug("   ");
		logger.debug("pageAttached()");
	}

	void pageDetached() {
		logger.debug("pageDetached()");
	}

	void onPassivate() {
		logger.debug("...onPassivate()");
	}

	void onActivate() {
		logger.debug("...onActivate()");
	}

	void setupRender() {
		logger.debug("...setupRender()");
		if (carMakes == null) {
			carMakes = ALL_MAKES;
		}
	}

	void beginRender() {
		logger.debug("...beginRender()");
	}

	void afterRender() {
		logger.debug("...afterRender()");
	}

	void cleanupRender() {
		logger.debug("...cleanupRender()");
	}

	Object onUpdateTime1() {
		logger.debug("...onUpdateTime1()");
		return request.isXHR() ? time1Zone.getBody() : null;
	}

	Object onAction() {
		logger.debug("...onAction()");
		return request.isXHR() ? time2Zone.getBody() : null;
	}

	void onPrepareForRender() {
		logger.debug("...onPrepareForRender()");
	}

	void onPrepare() {
		logger.debug("...onPrepare()");
	}

	void onPrepareForSubmit() {
		logger.debug("...onPrepareForSubmit()");
	}

	void onValidateFromName1() {
		logger.debug("...onValidateFromName1()");
	}

	void onSelected() {
		logger.debug("...onSelected()");
	}

	void onValidateFromFormWithZone() {
		logger.debug("...onValidateFromFormWithZone()");
	}

	void onSuccess() {
		logger.debug("...onSuccess()");
	}

	void onFailure() {
		logger.debug("...onFailure()");
	}

	Object onSubmit() {
		logger.debug("...onSubmit()");
		return request.isXHR() ? formZone.getBody() : null;
	}

	Object onName2Changed() {
		logger.debug("...onName2Changed()");
		name2 = request.getParameter("param");
		if (name2 == null) {
			name2 = "";
		}
		return request.isXHR() ? name2Zone.getBody() : null;
	}

	Object onValueChangedFromCarMake(String carMake) {
		logger.debug("...onValueChangedFromCarMake()");
		this.carMake = carMake;
		carMakes = ALL_MAKES;
		return carDisplayZone.getBody();
	}

	public String getMessage() {
		logger.debug("...getMessage()");
		return "This message is generated by getMessage().";
	}

	public Date getTime1() {
		logger.debug("...getTime1()");
		return new Date();
	}

	public Date getTime2() {
		logger.debug("...getTime2()");
		return new Date();
	}

	public String getName1() {
		logger.debug("...getName1()");
		return name1;
	}

	public void setName1(String name1) {
		logger.debug("...setName1()");
		this.name1 = name1;
	}

	public String getCarMake() {
		logger.debug("...getCarMake()");
		return carMake;
	}

	public void setCarMake(String carMake) {
		logger.debug("...setCarMake()");
		this.carMake = carMake;
	}
	
	public String getName2() {
		logger.debug("...getName2()");
		return name2;
	}
	
	public void setName2(String name2) {
		logger.debug("...setName2()");
		this.name2 = name2;
	}
}
