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

import com.liferay.dynamic.data.mapping.io.DDMFormDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Víctor Galán
 */
@Component(immediate = true)
public class AppointmentStructureCreator {

	@Activate
	public void activate() throws PortalException {
		Group group = _groupLocalService.fetchGroup(
			_portal.getDefaultCompanyId(), "Guest");

		if (group == null) {
			_groupLocalService.fetchGroup(
				_portal.getDefaultCompanyId(), "guest");
		}

		DDMStructure ddmStructure = _ddmStructureLocalService.fetchStructure(
			group.getGroupId(), _portal.getClassNameId(JournalArticle.class),
			"appointment-structure");

		if (ddmStructure != null) {
			return;
		}

		String read = StringUtil.read(
			AppointmentStructureCreator.class, "structure.json");

		DDMForm ddmForm = _deserialize(read);

		HashMap<Locale, String> name = new HashMap<>();
		name.put(LocaleUtil.getDefault(), "appointment");

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setScopeGroupId(group.getGroupId());

		_ddmStructureLocalService.addStructure(
			getAdminUser(
				_portal.getDefaultCompanyId()
			).getUserId(),
			group.getGroupId(), 0, _portal.getClassNameId(JournalArticle.class),
			"appointment-structure", name, null, ddmForm,
			DDMUtil.getDefaultDDMFormLayout(ddmForm),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT,
			serviceContext);

		System.out.println("Appointment structure created");
	}

	public User getAdminUser(long companyId) throws PortalException {
		Role role = _roleLocalService.getRole(
			companyId, RoleConstants.ADMINISTRATOR);

		List<User> users = _userLocalService.getRoleUsers(
			role.getRoleId(), 0, 1);

		if (!users.isEmpty()) {
			return users.get(0);
		}

		return null;
	}

	private DDMForm _deserialize(String content) {
		DDMFormDeserializerDeserializeRequest.Builder builder =
			DDMFormDeserializerDeserializeRequest.Builder.newBuilder(content);

		DDMFormDeserializerDeserializeResponse
			ddmFormDeserializerDeserializeResponse =
				_jsonDDMFormDeserializer.deserialize(builder.build());

		return ddmFormDeserializerDeserializeResponse.getDDMForm();
	}

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference(target = "(ddm.form.deserializer.type=json)")
	private DDMFormDeserializer _jsonDDMFormDeserializer;

	@Reference
	private Portal _portal;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}