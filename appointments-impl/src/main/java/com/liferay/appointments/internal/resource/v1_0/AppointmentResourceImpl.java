package com.liferay.appointments.internal.resource.v1_0;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.internal.odata.AppointmentEntityModel;
import com.liferay.appointments.resource.v1_0.AppointmentResource;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMStructureService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.util.JournalConverter;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.LocalDateTimeUtil;
import com.liferay.portal.vulcan.util.SearchUtil;

import java.text.SimpleDateFormat;

import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return new AppointmentEntityModel();
	}

	@Override
	public Page<Appointment> getSiteAppointmentsPage(
			Long siteId, String search, Filter filter, Pagination pagination,
			Sort[] sorts)
		throws Exception {

		DDMStructure ddmStructure = _ddmStructureService.getStructure(
			siteId, _portal.getClassNameId(JournalArticle.class),
			"BASIC-WEB-CONTENT", true);

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

		JournalArticle journalArticle = _createJournalArticle(
			siteId, appointment);

		return _toAppointment(journalArticle);
	}

	@Override
	public Appointment putAppointment(
			Long appointmentId, Appointment appointment)
		throws Exception {

		JournalArticle journalArticle = _journalArticleService.getLatestArticle(
			appointmentId);

		String content = _getContent(
			appointment, journalArticle.getDDMStructure());

		return _toAppointment(
			_updateJournalArticle(
				appointment.getTitle(), content, journalArticle));
	}

	private JournalArticle _createJournalArticle(
			long siteId, Appointment appointment)
		throws Exception {

		DDMStructure ddmStructure = _ddmStructureService.getStructure(
			siteId, _portal.getClassNameId(JournalArticle.class),
			"BASIC-WEB-CONTENT", true);

		String content = _getContent(appointment, ddmStructure);

		LocalDateTime localDateTime = LocalDateTimeUtil.toLocalDateTime(
			new Date());

		HashMap<Locale, String> titleMap = new HashMap<>();

		titleMap.put(
			contextAcceptLanguage.getPreferredLocale(), appointment.getTitle());

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setScopeGroupId(siteId);

		return _journalArticleService.addArticle(
			siteId, 0, 0, 0, null, true, titleMap, null, content,
			ddmStructure.getStructureKey(), null, null,
			localDateTime.getMonthValue() - 1, localDateTime.getDayOfMonth(),
			localDateTime.getYear(), localDateTime.getHour(),
			localDateTime.getMinute(), 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0, true,
			true, null, serviceContext);
	}

	private String _getContent(
			Appointment appointment, DDMStructure ddmStructure)
		throws Exception {

		SimpleDateFormat simpleDateFormat = _getDateFormat();

		DDMForm ddmForm = ddmStructure.getDDMForm();

		LocalizedValue localizedValue = new LocalizedValue();

		localizedValue.addString(
			contextAcceptLanguage.getPreferredLocale(),
			simpleDateFormat.format(appointment.getDate()));

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("content");
		ddmFormFieldValue.setValue(localizedValue);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		ddmFormValues.setAvailableLocales(ddmForm.getAvailableLocales());
		ddmFormValues.setDDMFormFieldValues(Arrays.asList(ddmFormFieldValue));
		ddmFormValues.setDefaultLocale(ddmForm.getDefaultLocale());

		Fields fields = _ddm.getFields(
			ddmStructure.getStructureId(), ddmFormValues);

		return _journalConverter.getContent(ddmStructure, fields);
	}

	private SimpleDateFormat _getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	private Appointment _toAppointment(JournalArticle journalArticle)
		throws Exception {

		Appointment appointment = new Appointment();

		Fields fields = _journalConverter.getDDMFields(
			journalArticle.getDDMStructure(), journalArticle.getContent());

		appointment.setId(journalArticle.getResourcePrimKey());

		appointment.setTitle(
			journalArticle.getTitle(
				contextAcceptLanguage.getPreferredLocale()));

		SimpleDateFormat simpleDateFormat = _getDateFormat();

		appointment.setDate(
			simpleDateFormat.parse(
				(String)fields.get(
					"content"
				).getValue()));

		return appointment;
	}

	private JournalArticle _updateJournalArticle(
			String title, String content, JournalArticle journalArticle)
		throws PortalException {

		HashMap<Locale, String> titleMap = new HashMap<>();

		LocalDateTime localDateTime = LocalDateTimeUtil.toLocalDateTime(
			journalArticle.getDisplayDate());

		titleMap.put(contextAcceptLanguage.getPreferredLocale(), title);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setScopeGroupId(journalArticle.getGroupId());

		return _journalArticleService.updateArticle(
			journalArticle.getGroupId(), journalArticle.getFolderId(),
			journalArticle.getArticleId(), journalArticle.getVersion(),
			titleMap, null, journalArticle.getFriendlyURLMap(), content,
			journalArticle.getDDMStructureKey(), null,
			journalArticle.getLayoutUuid(), localDateTime.getMonthValue() - 1,
			localDateTime.getDayOfMonth(), localDateTime.getYear(),
			localDateTime.getHour(), localDateTime.getMinute(), 0, 0, 0, 0, 0,
			true, 0, 0, 0, 0, 0, true, true, false, null, null, null, null,
			serviceContext);
	}

	@Reference
	private DDM _ddm;

	@Reference
	private DDMStructureService _ddmStructureService;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private JournalConverter _journalConverter;

	@Reference
	private Portal _portal;

}