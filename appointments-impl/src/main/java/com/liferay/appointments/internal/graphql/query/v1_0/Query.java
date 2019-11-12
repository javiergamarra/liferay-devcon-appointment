package com.liferay.appointments.internal.graphql.query.v1_0;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.resource.v1_0.AppointmentResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLInvokeDetached;
import graphql.annotations.annotationTypes.GraphQLName;

import java.util.Collection;

import javax.annotation.Generated;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class Query {

	public static void setAppointmentResourceComponentServiceObjects(
		ComponentServiceObjects<AppointmentResource>
			appointmentResourceComponentServiceObjects) {

		_appointmentResourceComponentServiceObjects =
			appointmentResourceComponentServiceObjects;
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Appointment getAppointment(
			@GraphQLName("appointmentId") Long appointmentId)
		throws Exception {

		return _applyComponentServiceObjects(
			_appointmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			appointmentResource -> appointmentResource.getAppointment(
				appointmentId));
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public Collection<Appointment> getSiteAppointmentsPage(
			@GraphQLName("siteId") Long siteId,
			@GraphQLName("search") String search,
			@GraphQLName("filter") Filter filter,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page, @GraphQLName("sorts") Sort[] sorts)
		throws Exception {

		return _applyComponentServiceObjects(
			_appointmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			appointmentResource -> {
				Page paginationPage =
					appointmentResource.getSiteAppointmentsPage(
						siteId, search, filter, Pagination.of(pageSize, page),
						sorts);

				return paginationPage.getItems();
			});
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