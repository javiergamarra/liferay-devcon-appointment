package com.liferay.appointments.internal.graphql.query.v1_0;

import com.liferay.appointments.dto.v1_0.Appointment;
import com.liferay.appointments.resource.v1_0.AppointmentResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.pagination.Page;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.UriInfo;

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

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {appointments(siteId: ___){items {__}, page, pageSize, totalCount}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public AppointmentPage appointments(
			@GraphQLName("siteId") Long siteId,
			@GraphQLName("siteKey") String siteKey)
		throws Exception {

		return _applyComponentServiceObjects(
			_appointmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			appointmentResource -> new AppointmentPage(
				appointmentResource.getSiteAppointmentsPage(siteId)));
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -H 'Content-Type: text/plain; charset=utf-8' -X 'POST' 'http://localhost:8080/o/graphql' -d $'{"query": "query {appointment(appointmentId: ___){date, id, title}}"}' -u 'test@liferay.com:test'
	 */
	@GraphQLField
	public Appointment appointment(
			@GraphQLName("appointmentId") Long appointmentId)
		throws Exception {

		return _applyComponentServiceObjects(
			_appointmentResourceComponentServiceObjects,
			this::_populateResourceContext,
			appointmentResource -> appointmentResource.getAppointment(
				appointmentId));
	}

	@GraphQLName("AppointmentPage")
	public class AppointmentPage {

		public AppointmentPage(Page appointmentPage) {
			items = appointmentPage.getItems();
			page = appointmentPage.getPage();
			pageSize = appointmentPage.getPageSize();
			totalCount = appointmentPage.getTotalCount();
		}

		@GraphQLField
		protected java.util.Collection<Appointment> items;

		@GraphQLField
		protected long page;

		@GraphQLField
		protected long pageSize;

		@GraphQLField
		protected long totalCount;

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

		appointmentResource.setContextAcceptLanguage(_acceptLanguage);
		appointmentResource.setContextCompany(_company);
		appointmentResource.setContextHttpServletRequest(_httpServletRequest);
		appointmentResource.setContextHttpServletResponse(_httpServletResponse);
		appointmentResource.setContextUriInfo(_uriInfo);
		appointmentResource.setContextUser(_user);
	}

	private static ComponentServiceObjects<AppointmentResource>
		_appointmentResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private BiFunction<Object, String, Filter> _filterBiFunction;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private Company _company;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private UriInfo _uriInfo;
	private User _user;

}