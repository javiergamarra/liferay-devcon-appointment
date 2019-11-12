package com.liferay.appointments.internal.graphql.mutation.v1_0;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.resource.v1_0.AppointmentResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLInvokeDetached;
import graphql.annotations.annotationTypes.GraphQLName;

import javax.annotation.Generated;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setAppointmentResourceComponentServiceObjects(
		ComponentServiceObjects<AppointmentResource>
			appointmentResourceComponentServiceObjects) {

		_appointmentResourceComponentServiceObjects =
			appointmentResourceComponentServiceObjects;
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Appointment postSiteAppointment(
			@GraphQLName("siteId") Long siteId,
			@GraphQLName("appointment") Appointment appointment)
		throws Exception {

		return _applyComponentServiceObjects(
			_appointmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			appointmentResource -> appointmentResource.postSiteAppointment(
				siteId, appointment));
	}

	@GraphQLInvokeDetached
	public void deleteAppointment(
			@GraphQLName("appointmentId") Long appointmentId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_appointmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			appointmentResource -> appointmentResource.deleteAppointment(
				appointmentId));
	}

	@GraphQLInvokeDetached
	public Appointment putAppointment(
			@GraphQLName("appointmentId") Long appointmentId,
			@GraphQLName("appointment") Appointment appointment)
		throws Exception {

		return _applyComponentServiceObjects(
			_appointmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			appointmentResource -> appointmentResource.putAppointment(
				appointmentId, appointment));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(
			AppointmentResource appointmentResource)
		throws Exception {

		appointmentResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));
	}

	private static ComponentServiceObjects<AppointmentResource>
		_appointmentResourceComponentServiceObjects;

}