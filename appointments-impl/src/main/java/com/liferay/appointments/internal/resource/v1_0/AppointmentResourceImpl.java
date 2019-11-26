package com.liferay.appointments.internal.resource.v1_0;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.internal.util.AppointmentUtil;
import com.liferay.appointments.resource.v1_0.AppointmentResource;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.service.ServiceContext;
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
    List<Appointment> appointments = new ArrayList<>();

    List<JournalArticle> articles = _journalArticleService.getArticles(siteId, 0,
        contextAcceptLanguage.getPreferredLocale());

    for (JournalArticle article : articles) {
      appointments.add(_toAppointment(article));
    }

    return Page.of(appointments);
  }

  @Override
  public Appointment getAppointment(Long appointmentId) throws Exception {
    return _toAppointment(_journalArticleService.getLatestArticle(appointmentId));
  }

  @Override
  public void deleteAppointment(Long appointmentId) throws Exception {
    JournalArticle journalArticle = _journalArticleService.getLatestArticle(appointmentId);

    _journalArticleService.deleteArticle(journalArticle.getGroupId(), journalArticle.getArticleId(),
        journalArticle.getArticleResourceUuid(), new ServiceContext());
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
  private JournalArticleService _journalArticleService;

  @Reference
  private AppointmentUtil _appointmentUtil;

}
