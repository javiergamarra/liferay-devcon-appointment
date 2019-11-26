package com.liferay.appointments.internal.resource.v1_0;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.internal.util.AppointmentUtil;
import com.liferay.appointments.resource.v1_0.AppointmentResource;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.vulcan.pagination.Page;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/appointment.properties",
	scope = ServiceScope.PROTOTYPE, service = AppointmentResource.class
)
public class AppointmentResourceImpl extends BaseAppointmentResourceImpl {

  @Override
  public Page<Appointment> getSiteAppointmentsPage(Long siteId) throws Exception {
    List<Appointment> appointments = new ArrayList<>(10);

    for (int i = 0; i < 10; i++) {
      Appointment appointment = new Appointment();

      appointment.setTitle("Title" + i);
      appointment.setDate(new Date());

      appointments.add(appointment);
    }

    return Page.of(appointments);
  }

  @Override
  public Appointment postSiteAppointment(Long siteId, Appointment appointment) throws Exception {

    JournalArticle journalArticle = _appointmentUtil.createJournalArticle(siteId, appointment.getTitle(),
        appointment.getDate());

    return _toAppointment(journalArticle);
  }

  private Appointment _toAppointment(JournalArticle journalArticle) throws Exception {
    Appointment appointment = new Appointment();

    appointment.setId(journalArticle.getResourcePrimKey());
    appointment.setTitle(journalArticle.getTitle());
    appointment.setDate(_appointmentUtil.getDateField(journalArticle));

    return appointment;
  }

  @Reference
  private AppointmentUtil _appointmentUtil;

}
