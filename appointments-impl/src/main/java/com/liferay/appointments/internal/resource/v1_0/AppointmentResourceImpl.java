package com.liferay.appointments.internal.resource.v1_0;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.resource.v1_0.AppointmentResource;

import com.liferay.portal.vulcan.pagination.Page;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/appointment.properties",
	scope = ServiceScope.PROTOTYPE, service = AppointmentResource.class
)
public class AppointmentResourceImpl extends BaseAppointmentResourceImpl {

	@Override
	public Page<Appointment> getAppointmentsPage() throws Exception {

		List<Appointment> appointments = new ArrayList<>(10);

		for (int i = 0; i < 10; i++) {
			Appointment appointment = new Appointment();

			appointment.setTitle("Title" + i);
			appointment.setDate(new Date());

			appointments.add(appointment);
		}

		return Page.of(appointments);
	}
}