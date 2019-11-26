const SERVER_GRAPHQL_ENDPOINT = 'http://localhost:8080/o/graphql';
const AUTHORIZATION = 'Basic dGVzdEBsaWZlcmF5LmNvbTp0ZXN0';
const SITE_KEY = 'Guest';

// eslint-disable-next-line no-unused-vars
export const appointments = (filter = '', siteKey = SITE_KEY) =>
    request(gql`   
        //FILLME!
    `);

// eslint-disable-next-line no-unused-vars
export const appointment = appointmentId =>
    request(gql`   
        //FILLME!
    `);

// eslint-disable-next-line no-unused-vars
export const createAppointment = (title, date, siteKey = SITE_KEY) =>
    request(gql`   
        //FILLME!
    `);

// eslint-disable-next-line no-unused-vars
export const deleteAppointment = appointmentId =>
    request(gql`   
        //FILLME!
    `);

// eslint-disable-next-line no-unused-vars
export const updateAppointment = (appointmentId, title, date) =>
    request(gql`   
        //FIXME!
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
        if (json.errors) return Promise.reject(json.errors);

        const data = json.data;

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