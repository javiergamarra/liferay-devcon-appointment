const SERVER_REST_ENDPOINT = 'http://localhost:8080/o/appointments/v1.0';
const AUTHORIZATION = 'Basic dGVzdEBsaWZlcmF5LmNvbTp0ZXN0';
const SITE_KEY = 'Guest';

export const appointments = (filter = '', siteKey = SITE_KEY) =>
    request(`${SERVER_REST_ENDPOINT}/sites/${siteKey}/appointments`, 'GET', {filter});

export const appointment = appointmentId =>
    request(`${SERVER_REST_ENDPOINT}/appointments/${appointmentId}`);

export const createAppointment = (title, date, siteKey = SITE_KEY) =>
    request(`${SERVER_REST_ENDPOINT}/sites/${siteKey}/appointments`, 'POST', {}, {title, date});

export const deleteAppointment = appointmentId =>
    request(`${SERVER_REST_ENDPOINT}/appointments/${appointmentId}`, 'DELETE');

export const updateAppointment = (appointmentId, title, date) =>
    request(`${SERVER_REST_ENDPOINT}/appointments/${appointmentId}`, 'PUT', {}, {title, date});

function request(url, method, params, body) {
    return fetch(getURL(url, params), {
            body: JSON.stringify(body),
            headers: {
                Authorization: AUTHORIZATION,
                'Content-Type': 'application/json'
            },
            method: method || 'GET'
        }
    ).then(response => {
        if (response.status === 204) {
            return true;
        }

        return response.json();
    });
}

function getURL(path, params = {}) {
    const uri = new URL(path);

    const keys = Object.keys(params);

    keys.forEach(key => uri.searchParams.set(key, params[key]));

    return uri.toString();
}