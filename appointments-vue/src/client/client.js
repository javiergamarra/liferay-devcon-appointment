const SERVER_GRAPHQL_ENDPOINT = 'http://localhost:8080/o/graphql';
const AUTHORIZATION = 'Basic dGVzdEBsaWZlcmF5LmNvbTp0ZXN0';

export const appointments = (filter = '', siteKey = 'guest') =>
    request(gql`   
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
    `);

export const appointment = appointmentId =>
    request(gql`   
        query {
            appointment(appointmentId: ${appointmentId}){
                id
                date
                title
            }
        }
    `);

export const createAppointment = (title, date, siteKey = 'guest') =>
    request(gql`   
        mutation {
            createSiteAppointment(appointment: { date: ${date}, title: ${title}}, siteKey: ${siteKey}) {
                date
                id
                title
            }
        }
    `);

export const deleteAppointment = appointmentId =>
    request(gql`   
        mutation {
            deleteAppointment(appointmentId: ${appointmentId})
        }
    `);

export const updateAppointment = (appointmentId, title, date) =>
    request(gql`   
        mutation {
            updateAppointment(appointment: { date: ${date}, title: ${title}}, appointmentId: ${appointmentId}) {
                date
                id
                title
            }
        }
    `);

function request(query) {
    return fetch(getURL(SERVER_GRAPHQL_ENDPOINT), {
            body: `{"query": "${query}"}`,
            headers: {
                Authorization: AUTHORIZATION,
                'Content-Type': 'text/plain; charset=utf-8',
            },
            method: 'POST'
        }
    ).then(response => response.json()
    ).then(json => {
        const data = json.data;

        if (!data) return Promise.reject(json.errors);

        return data[Object.keys(data)[0]];
    })
}

function getURL(path, params = {p_auth: ''}) {
    const uri = new URL(path);

    const keys = Object.keys(params);

    keys.forEach(key => uri.searchParams.set(key, params[key]));

    return uri.toString();
}

function escape(x) {
    return x !== undefined && JSON.stringify(x).replace(/[\\]/g, '\\\\')
        .replace(/[/]/g, '\\/')
        .replace(/[\b]/g, '\\b')
        .replace(/[\f]/g, '\\f')
        .replace(/[\n]/g, '\\n')
        .replace(/[\r]/g, '\\r')
        .replace(/[\t]/g, '\\t');
}

function gql(strings, ...values) {
    return strings.map((string, i) => string + (escape(values[i]) || ''))
        .join('')
        .replace(/\s+/g, ' ')
        .replace(/"/g, '\\"');
}