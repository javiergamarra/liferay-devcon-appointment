<template>
    <div>

        <div class="sheet">

            <div class="input-group input-group-sm" style="width: 50%;float: right">
                <div class="input-group-item input-group-item-shrink ">
                    <div class="input-group-text">Search</div>
                </div>
                <div class="input-group-item input-group-prepend">
                    <input @keyup="search" class="form-control">
                </div>
            </div>

            <h3 class="autofit-row sheet-subtitle">
			<span class="autofit-col autofit-col-expand">
				<span class="heading-text">List of appointments</span>
			</span>
                <span class="autofit-col">
				<span class="heading-end">
					<button class="btn btn-secondary btn-sm" type="button">
						Add
					</button>
				</span>
			</span>
            </h3>

            <div class="table-responsive">
                <table class="table table-autofit table-hover table-list">
                    <thead>
                    <tr>
                        <th><p class="table-list-title">Id</p></th>
                        <th>Title</th>
                        <th>Date</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tr @click="loadAppointment(appointment.id)" v-bind:key="appointment.id"
                        v-for="appointment in appointments">
                        <td>{{appointment.id}}</td>
                        <td>{{appointment.title}}</td>
                        <td>{{appointment.date}}</td>
                        <td>Edit</td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="sheet">
            <form>
                <div class="form-group form-group-sm">
                    <label for="title">Title</label>
                    <input class="form-control" id="title" v-model="title">
                </div>
                <div class="form-group form-group-sm">
                    <label for="date">Date</label>
                    <input class="form-control" id="date" type="datetime-local" v-model="date">
                </div>
                <div class="sheet-footer">
                    <button @click="createAppointment()" class="btn-primary">Create an appointment</button>
                </div>
            </form>
        </div>

        <h1>Selected appointment</h1>
        <p>{{appointment.title}}</p>
        <div class="btn-group">
            <button @click="deleteAppointment(appointment.id)" class="btn-group-item btn-primary">Delete</button>
            <button @click="event => updateAppointment(appointment.id)" class="btn-group-item btn-primary">Update
            </button>
        </div>
    </div>

</template>

<script>
    // eslint-disable-next-line
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
