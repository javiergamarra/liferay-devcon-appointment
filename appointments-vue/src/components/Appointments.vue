<template>
    <div>

        Search
        <input @keyup="search">

        <input v-model="title">
        <input type="datetime-local" v-model="date">
        <button @click="createAppointment()">Create an appointment</button>

        <ul>
            <li @click="loadAppointment(appointment.id)" v-bind:key="appointment.id"
                v-for="appointment in appointments">
                {{appointment.id}}
                {{appointment.title}}
                {{appointment.date}}
            </li>
        </ul>

        <h1>Selected appointment</h1>
        <p>{{appointment.title}}</p>
        <button @click="deleteAppointment(appointment.id)">Delete</button>
        <button @click="event => updateAppointment(appointment.id)">Update</button>
    </div>

</template>

<script>
    // eslint-disable-next-line
    //FIXME use clay and add style
    //TODO page, search?
    //TODO show errors
    import {appointment, appointments, createAppointment, deleteAppointment, updateAppointment} from "../client/client";

    export default {
        methods: {
            createAppointment: function () {
                createAppointment(this.title, this.toISOString(this.date)).then(data =>
                    this.appointments.push(data)
                )
            },
            deleteAppointment: function (id) {
                deleteAppointment(id).then(() => {
                    this.appointments = this.appointments.filter(app => app.id !== id);
                    this.appointment = {};
                })
            },
            loadAppointment: function (id) {
                appointment(id).then(data => this.appointment = data);
            },
            loadAppointments: function (filter) {
                appointments(filter).then(data => this.appointments = data.items);
            },
            search: function (event) {
                this.loadAppointments(`contains(title, '${event.target.value}')`);
            },
            updateAppointment: function (id) {
                updateAppointment(id, this.title, this.toISOString(this.date)).then(data => {
                        const index = this.appointments.findIndex(app => app.id === id);
                        this.appointments[index] = data;
                        this.appointments = [...this.appointments];
                    }
                )
            },
            toISOString: function (date) {
                return new Date(date).toISOString().split('.')[0] + "Z";
            }
        },
        data() {
            return {
                appointment: {},
                appointments: [],
                title: '',
                date: new Date(),
            }
        },
        async mounted() {
            this.loadAppointments();
        },
        props: {}
    }
</script>

<style scoped>

</style>
