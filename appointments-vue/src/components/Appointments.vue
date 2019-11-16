<template>
    <div class="sheet">

        <h1>
            <span @click="() => this.loadAppointments('')">Appointments</span>
        </h1>

        <h3 class="autofit-row sheet-subtitle">
			<span class="autofit-col autofit-col-expand">
				<span class="heading-text">Filter appointments</span>
			</span>
        </h3>

        <div class="form-group form-group-autofit">
            <div class="input-group input-group-item">
                <div class="input-group-item input-group-item-shrink ">
                    <label class="input-group-text" for="searchByTitle">Title</label>
                </div>
                <div class="input-group-item input-group-prepend">
                    <input @keyup="filterByTitle" class="form-control" id="searchByTitle">
                </div>
            </div>

            <div class="input-group input-group-item">
                <div class="input-group-item input-group-item-shrink">
                    <label class="input-group-text" for="searchByDate">Date</label>
                </div>
                <div class="input-group-item input-group-prepend">
                    <input @change="filterByDate" class="form-control" id="searchByDate" type="date">
                </div>
            </div>
        </div>

        <h3 class="autofit-row sheet-subtitle">
			<span class="autofit-col autofit-col-expand">
				<span class="heading-text">List of appointments</span>
			</span>
            <span class="autofit-col">
				<span class="heading-end">
					<button class="btn btn-secondary btn-sm" type="button">
                        <router-link to="/add">Add</router-link>
					</button>
				</span>
			</span>
        </h3>

        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><p class="table-list-title">Id</p></th>
                    <th class="table-cell-expand">Title</th>
                    <th>Date</th>
                    <th></th>
                </tr>
                </thead>
                <tr v-bind:key="appointment.id"
                    v-for="appointment in appointments">
                    <td>{{appointment.id}}</td>
                    <td class="table-cell-expand">{{appointment.title}}</td>
                    <td>{{appointment.date}}</td>
                    <td>
                        <router-link :to="'/add/' + appointment.id">Edit</router-link>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</template>

<script>
    import {appointments} from "../client/rest-client";

    export default {
        methods: {
            loadAppointments: function (filter) {
                appointments(filter).then(data => {
                    this.appointments = data.items;
                }).catch(err => {
                    this.$emit('error', err);
                });
            },
            filterByTitle: function (event) {
                this.loadAppointments(`contains(title, '${event.target.value}')`);
            },
            filterByDate: function(event) {
                this.loadAppointments(`date gt ${event.target.value}`);
            },
        },
        data() {
            return {
                appointments: [],
            }
        },
        async mounted() {
            this.loadAppointments();
        },
        props: {}
    }
</script>

<style lang="scss" scoped>

</style>
