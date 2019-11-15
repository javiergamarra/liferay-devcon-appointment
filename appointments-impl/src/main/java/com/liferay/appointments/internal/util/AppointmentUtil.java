/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.appointments.internal.util;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.util.JournalConverter;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.vulcan.util.LocalDateTimeUtil;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Víctor Galán
 */
@Component(service = AppointmentUtil.class)
public class AppointmentUtil {

	public JournalArticle createJournalArticle(
			long siteId, String title, Date date)
		throws Exception {

		DDMStructure ddmStructure = getDDMStructure(siteId);

		String content = getContent(date, ddmStructure);

		LocalDateTime localDateTime = LocalDateTimeUtil.toLocalDateTime(
			new Date());

		HashMap<Locale, String> titleMap = new HashMap<>();

		titleMap.put(LocaleUtil.getDefault(), title);

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

	public String getContent(Date date, DDMStructure ddmStructure)
		throws Exception {

		if (date == null) {
			date = new Date();
		}

		String dateString = DateUtil.getDate(
			date, "yyyy-MM-dd", LocaleUtil.getDefault(),
			TimeZone.getTimeZone("UTC"));

		DDMForm ddmForm = ddmStructure.getDDMForm();

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("date");
		ddmFormFieldValue.setValue(new UnlocalizedValue(dateString));

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		ddmFormValues.setAvailableLocales(ddmForm.getAvailableLocales());
		ddmFormValues.setDDMFormFieldValues(
			Collections.singletonList(ddmFormFieldValue));
		ddmFormValues.setDefaultLocale(ddmForm.getDefaultLocale());

		Fields fields = _ddm.getFields(
			ddmStructure.getStructureId(), ddmFormValues);

		return _journalConverter.getContent(ddmStructure, fields);
	}

	public Date getDateField(JournalArticle journalArticle) throws Exception {
		Fields fields = _journalConverter.getDDMFields(
			journalArticle.getDDMStructure(), journalArticle.getContent());

		return DateUtil.parseDate(
			"yyyy-MM-dd",
			(String)fields.get(
				"date"
			).getValue(),
			LocaleUtil.getDefault());
	}

	public DDMStructure getDDMStructure(Object siteId) throws PortalException {
		long groupId = 0;

		if (siteId instanceof String) {
			groupId = GroupLocalServiceUtil.fetchGroup(
				_portal.getDefaultCompanyId(), (String)siteId
			).getGroupId();
		}
		else {
			groupId = (long)siteId;
		}

		return _ddmStructureLocalService.fetchStructure(
			groupId, _portal.getClassNameId(JournalArticle.class),
			"appointment-structure");
	}

	public JournalArticle updateJournalArticle(
			String title, String content, JournalArticle journalArticle)
		throws PortalException {

		HashMap<Locale, String> titleMap = new HashMap<>();

		LocalDateTime localDateTime = LocalDateTimeUtil.toLocalDateTime(
			journalArticle.getDisplayDate());

		titleMap.put(LocaleUtil.getDefault(), title);

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
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private JournalConverter _journalConverter;

	@Reference
	private Portal _portal;

}