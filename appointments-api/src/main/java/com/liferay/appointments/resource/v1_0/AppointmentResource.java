package com.liferay.appointments.resource.v1_0;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.pagination.Page;

import javax.annotation.Generated;

/**
 * To access this resource, run:
 *
 *     curl -u your@email.com:yourpassword -D - http://localhost:8080/o/appointments/v1.0
 *
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public interface AppointmentResource {

	public Page<Appointment> getSiteAppointmentsPage(Long siteId)
		throws Exception;

	public Appointment postSiteAppointment(Long siteId, Appointment appointment)
		throws Exception;

	public Appointment getAppointment(Long appointmentId) throws Exception;

	public void setContextCompany(Company contextCompany);

}