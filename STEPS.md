# Building an SPA Using the New Headless APIs and REST Builder

## Configuring CORS

* Go to system settings -> security tools -> Portal Cross-Origin Resource sharing (CORS) 

* Add url pattern /o/appointments/*

* Save

## rest-config.yaml

```yaml
apiDir: "../appointments-api/src/main/java"
apiPackagePath: "com.liferay.appointments"
application:
    baseURI: "/appointments"
    className: "AppointmentsApplication"
    name: "Liferay.Appointments"
author: "Javier Gamarra"
```

## First endpoint

### rest-openapi.yaml

```yaml
components:
    schemas:
        Appointment:
            description: Represents an appointment.
            properties:
                date:
                    format: date-time
                    type: string
                id:
                    format: int64
                    type: integer
                title:
                    type: string
            required:
                - title
            type: object
info:
    description: ""
    license:
        name: "Apache 2.0"
        url: "http://www.apache.org/licenses/LICENSE-2.0.html"
    title: "Appointments"
    version: v1.0
openapi: 3.0.1
paths:
	"/sites/{siteId}/appointments":
        get:
            parameters:
                - in: path
                  name: siteId
                  required: true
                  schema:
                      format: int64
                      type: integer
            responses:
                200:
                    content:
                        application/json:
                            schema:
                                items:
                                    $ref: "#/components/schemas/Appointment"
                                type: array
                        application/xml:
                            schema:
                                items:
                                    $ref: "#/components/schemas/Appointment"
                                type: array
                    description: "Get appointments"
            tags: ["Appointment"]
```

### AppointmentResourceImpl.java

```java
@Override
public Page<Appointment> getAppointmentsPage() throws Exception {

       List<Appointment> appointments = new ArrayList<>(10);

       for (int i = 0; i < 10; i++) {
               Appointment appointment = new Appointment();

               appointment.setTitle("Title" + i);
               appointment.setDate(new Date());

               appointments.add(appointment);
       }

       return Page.of(appointments);
}
```

## checking existing APIs
`jaxrs:check`


## Create appointment

### rest-openapi.yaml

```yaml
post:
	description: ""
	parameters:
	    - in: path
	      name: siteId
	      required: true
	      schema:
	          format: int64
	          type: integer
	requestBody:
	    content:
	        application/json:
	            schema:
	                $ref: "#/components/schemas/Appointment"
	        application/xml:
	            schema:
	                $ref: "#/components/schemas/Appointment"
	responses:
	    200:
	        content:
	            application/json:
	                schema:
	                    $ref: "#/components/schemas/Appointment"
	            application/xml:
	                schema:
	                    $ref: "#/components/schemas/Appointment"
	        description: ""
	tags: ["Appointment"]
```

### AppointmentResourceImpl.java

```java
@Override
public Appointment postSiteAppointment(Long siteId, Appointment appointment)
	throws Exception {

	JournalArticle journalArticle = _appointmentUtil.createJournalArticle(
		siteId, appointment.getTitle(), appointment.getDate());

	return _toAppointment(journalArticle);
}
```

## Appointment list*

### AppointmentResourceImpl.java

```java
List<Appointment> appointments = new ArrayList<>();

List<JournalArticle> articles =
   _journalArticleService.getArticles(
           siteId, 0, contextAcceptLanguage.getPreferredLocale());
	
for (JournalArticle article : articles) {
   appointments.add(_toAppointment(article));
}            
```

## Get appointment

### rest-openapi.yaml
```yaml
paths:
    "/appointments/{appointmentId}":
		get:
		    parameters:
		        - in: path
		          name: appointmentId
		          required: true
		          schema:
		              format: int64
		              type: integer
		    responses:
		        200:
		            content:
		                application/json:
		                    schema:
		                        $ref: "#/components/schemas/Appointment"
		                application/xml:
		                    schema:
		                        $ref: "#/components/schemas/Appointment"
		            description: "Get appointment"
		    tags: ["Appointment"]
```

### AppointmentResourceImpl.java

```java
@Override
public Appointment getAppointment(Long appointmentId) throws Exception {
	return _toAppointment(
		_journalArticleService.getLatestArticle(appointmentId));
}
```

## Delete appointment

### rest-openapi.yaml
```yaml
delete:
    description:
    operationId: deleteAppointment
    parameters:
        - in: path
          name: appointmentId
          required: true
          schema:
              format: int64
              type: integer
    responses:
        204:
            content:
                application/json: {}
    tags: ["Appointment"]
```

### AppointmentResourceImpl.java

```java
@Override
public void deleteAppointment(Long appointmentId) throws Exception {
	JournalArticle journalArticle = _journalArticleService.getLatestArticle(
		appointmentId);

	_journalArticleService.deleteArticle(
		journalArticle.getGroupId(), journalArticle.getArticleId(),
		journalArticle.getArticleResourceUuid(), new ServiceContext());
}
```

## Update appointment

### rest-openapi.yaml
```yaml
put:
    description: ""
    operationId: putAppointment
    parameters:
        - in: path
          name: appointmentId
          required: true
          schema:
              format: int64
              type: integer
    requestBody:
        content:
            application/json:
                schema:
                    $ref: "#/components/schemas/Appointment"
            application/xml:
                schema:
                    $ref: "#/components/schemas/Appointment"
    responses:
        200:
            content:
                application/json:
                    schema:
                        $ref: "#/components/schemas/Appointment"
                application/xml:
                    schema:
                        $ref: "#/components/schemas/Appointment"
            description: ""
    tags: ["Appointment"]
```

### AppointmentResourceImpl.java

```java
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
```

## Pagination, filtering, sorting, search..

### rest-openapi.yaml
```yaml
- in: query
  name: filter
  schema:
      type: string
- in: query
  name: page
  schema:
      type: integer
- in: query
  name: pageSize
  schema:
      type: integer
- in: query
  name: search
  schema:
      type: string
- in: query
  name: sort
  schema:
      type: string
```

### AppointmentResourceImpl.java

```java
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
```

## Error handling 

```
@Component(
	property = {
		"osgi.jaxrs.application.select=(osgi.jaxrs.name=Liferay.Appointments)",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.Appointments.AppointmentTitleExceptionMapper"
	},
	service = ExceptionMapper.class
)
public class AppointmentTitleExceptionMapper
	implements ExceptionMapper<ArticleTitleException> {

	@Override
	public Response toResponse(ArticleTitleException articleTitleException) {
		return Response.status(
			400
		).entity(
			"Appointment title is mandatory"
		).type(
			MediaType.TEXT_PLAIN
		).build();
	}

}
```

## Required

### rest-openapi.yaml
```
required:
    - title
```