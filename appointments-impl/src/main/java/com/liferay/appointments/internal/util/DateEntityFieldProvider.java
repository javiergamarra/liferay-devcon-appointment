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

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.odata.entity.DateEntityField;
import com.liferay.portal.odata.entity.EntityField;

import java.text.DateFormat;
import java.text.ParseException;

import java.util.Date;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Víctor Galán
 */
@Component(service = DateEntityFieldProvider.class)
public class DateEntityFieldProvider {

	public EntityField getDateEntityField(DDMStructure ddmStructure) {
		return new DateEntityField(
			"date",
			locale -> _toFilterableOrSortableFieldName(
				ddmStructure.getStructureId(), "date", locale, "String"),
			locale -> _toFilterableOrSortableFieldName(
				ddmStructure.getStructureId(), "date", locale, "String"),
			this::_toFieldValue);
	}

	private String _toFieldValue(Object fieldValue) {
		DateFormat indexDateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			PropsUtil.get(PropsKeys.INDEX_DATE_FORMAT_PATTERN));

		try {
			Date date = indexDateFormat.parse(String.valueOf(fieldValue));

			DateFormat searchDateFormat =
				DateFormatFactoryUtil.getSimpleDateFormat("yyyy-MM-dd");

			return searchDateFormat.format(date);
		}
		catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	private String _toFilterableOrSortableFieldName(
		long ddmStructureId, String fieldName, Locale locale, String type) {

		return Field.getSortableFieldName(
			StringBundler.concat(
				_ddmIndexer.encodeName(ddmStructureId, fieldName, locale),
				StringPool.UNDERLINE, type));
	}

	@Reference
	private DDMIndexer _ddmIndexer;

}