package com.liferay.appointments.internal.resource.v1_0;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.dto.v1_0.Owner;
import com.liferay.appointments.internal.odata.AppointmentEntityModel;
import com.liferay.appointments.internal.util.AppointmentUtil;
import com.liferay.appointments.internal.util.DateEntityFieldProvider;
import com.liferay.appointments.resource.v1_0.AppointmentResource;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.SearchUtil;

import javax.ws.rs.core.MultivaluedMap;

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
public class AppointmentResourceImpl
	extends BaseAppointmentResourceImpl implements EntityModelResource {

	@Override
	public void deleteAppointment(Long appointmentId) throws Exception {
		JournalArticle journalArticle = _journalArticleService.getLatestArticle(
			appointmentId);

		_journalArticleService.deleteArticle(
			journalArticle.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getArticleResourceUuid(), new ServiceContext());
	}

	@Override
	public Appointment getAppointment(Long appointmentId) throws Exception {
		return _toAppointment(
			_journalArticleService.getLatestArticle(appointmentId));
	}

	@Override
	public Owner getAppointmentOwner(Long appointmentId) throws Exception {
		JournalArticle journalArticle = _journalArticleService.getLatestArticle(
			appointmentId);

		User user = _userService.getUserById(journalArticle.getUserId());

		Owner owner = new Owner();

		owner.setName(user.getFullName());

		return owner;
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws PortalException {

		Object siteId = multivaluedMap.getFirst("siteId");

		DDMStructure ddmStructure = _appointmentUtil.getDDMStructure(siteId);

		return new AppointmentEntityModel(
			_dateEntityFieldProvider.getDateEntityField(ddmStructure));
	}

	@Override
	public Page<Appointment> getSiteAppointmentsPage(
			Long siteId, String search, Filter filter, Pagination pagination,
			Sort[] sorts)
		throws Exception {

		DDMStructure ddmStructure = _appointmentUtil.getDDMStructure(siteId);

		return SearchUtil.search(
			booleanQuery -> {
				BooleanFilter booleanFilter =
					booleanQuery.getPreBooleanFilter();

				booleanFilter.add(
					new TermFilter(
						com.liferay.portal.kernel.search.Field.CLASS_TYPE_ID,
						String.valueOf(ddmStructure.getStructureId())),
					BooleanClauseOccur.MUST);
			},
			filter, JournalArticle.class, search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				com.liferay.portal.kernel.search.Field.ARTICLE_ID,
				com.liferay.portal.kernel.search.Field.SCOPE_GROUP_ID),
			searchContext -> {
				searchContext.setAttribute(
					com.liferay.portal.kernel.search.Field.STATUS,
					WorkflowConstants.STATUS_APPROVED);
				searchContext.setAttribute("head", Boolean.TRUE);
				searchContext.setCompanyId(contextCompany.getCompanyId());
				searchContext.setGroupIds(new long[] {siteId});
			},
			document -> _toAppointment(
				_journalArticleService.getLatestArticle(
					GetterUtil.getLong(
						document.get(
							com.liferay.portal.kernel.search.Field.
								SCOPE_GROUP_ID)),
					document.get(
						com.liferay.portal.kernel.search.Field.ARTICLE_ID),
					WorkflowConstants.STATUS_APPROVED)),
			sorts);
	}

	@Override
	public Appointment postSiteAppointment(Long siteId, Appointment appointment)
		throws Exception {

		JournalArticle journalArticle = _appointmentUtil.createJournalArticle(
			siteId, appointment.getTitle(), appointment.getDate());

		return _toAppointment(journalArticle);
	}

	@Override
	public Appointment putAppointment(
			Long appointmentId, Appointment appointment)
		throws Exception {

		JournalArticle journalArticle = _journalArticleService.getLatestArticle(
			appointmentId);

		String content = _appointmentUtil.getContent(
			appointment.getDate(), journalArticle.getDDMStructure());

		return _toAppointment(
			_appointmentUtil.updateJournalArticle(
				appointment.getTitle(), content, journalArticle));
	}

	private Appointment _toAppointment(JournalArticle journalArticle)
		throws Exception {

		Appointment appointment = new Appointment();

		appointment.setDate(_appointmentUtil.getDateField(journalArticle));

		appointment.setId(journalArticle.getResourcePrimKey());

		appointment.setTitle(
			journalArticle.getTitle(
				contextAcceptLanguage.getPreferredLocale()));

		return appointment;
	}

	@Reference
	private AppointmentUtil _appointmentUtil;

	@Reference
	private DateEntityFieldProvider _dateEntityFieldProvider;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private UserService _userService;

}