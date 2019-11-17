# appointments-vue

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Exercises

#### List appointments

```
query {
            appointments(filter: ${filter}, siteKey: ${siteKey}){
                items {
                    id
                    date
                    title
                }
                page
                pageSize
                totalCount
            }
        }
```

### Create appointment

```
mutation {
            createSiteAppointment(appointment: { date: ${date}, title: ${title}}, siteKey: ${siteKey}) {
                date
                id
                title
            }
        }
```

#### Get appointment 

```
query {
            appointment(appointmentId: ${appointmentId}){
                id
                date
                title
            }
        }
```

#### Update appointment

```
 mutation {
            updateAppointment(appointment: { date: ${date}, title: ${title}}, appointmentId: ${appointmentId}) {
                date
                id
                title
            }
        }
```

#### Delete appointment

```
mutation {
            deleteAppointment(appointmentId: ${appointmentId})
        }
```

#### Filter by title

```
`contains(title, '${event.target.value}')`
````

#### Filter by date

```
`date gt ${event.target.value}`
```