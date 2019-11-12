package com.liferay.appointments.internal.resource.v1_0;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.resource.v1_0.AppointmentResource;

import com.liferay.portal.vulcan.pagination.Page;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/appointment.properties",
	scope = ServiceScope.PROTOTYPE, service = AppointmentResource.class
)
public class AppointmentResourceImpl extends BaseAppointmentResourceImpl {

	@Override
	public Page<Appointment> getAppointmentsPage() {
		Appointment title1 = new Appointment() {{
			setTitle("title1");
			setDate(new Date());
		}};
		Appointment title2 = new Appointment() {{
			setTitle("title2");
			setDate(new Date());
		}};
		Appointment title3 = new Appointment() {{
			setTitle("title3");
			setDate(new Date());
		}};
		Appointment title4 = new Appointment() {{
			setTitle("title4");
			setDate(new Date());
		}};

		List<Appointment> appointments =
			Arrays.asList(title1, title2, title3, title4);

		return Page.of(appointments);
	}
}