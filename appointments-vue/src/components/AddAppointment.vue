<template>
    <div class="sheet">

        <form>

            <h1>{{appointment.id ? 'Editing' : 'Adding'}} an appointment</h1>

            <div v-if="loading">
                <span aria-hidden="true" class="loading-animation"></span>
            </div>

            <div class="form-group form-group-sm">
                <label for="title">Title</label>
                <input class="form-control" id="title" v-model="appointment.title">
            </div>
            <div class="form-group form-group-sm">
                <label for="date">Date</label>
                <input class="form-control" id="date" type="datetime-local" v-model="appointment.date">
            </div>

            <div class="sheet-footer">
                <div class="btn-group">
                    <button @click="createAppointment()" class="btn btn-group-item btn-primary" type="button"
                            v-if="!appointment.id">
                        Create an appointment
                    </button>
                    <button @click="event => updateAppointment(appointment.id)" class="btn btn-group-item btn-primary"
                            type="button"
                            v-if="appointment.id">Update
                    </button>
                    <button @click="deleteAppointment(appointment.id)" class="btn btn-group-item btn-danger"
                            type="button"
                            v-if="appointment.id">
                        Delete
                    </button>
                    <button @click="() => this.$router.push('/')" class="btn btn-group-item btn-secondary"
                            type="button">
                        Cancel
                    </button>
                </div>
            </div>
        </form>
    </div>
</template>

<script>
    import {appointment, createAppointment, deleteAppointment, updateAppointment} from "../client/client";

    export default {
        methods: {
            createAppointment: function () {
                createAppointment(this.appointment.title, this.toISOString(this.appointment.date)).then(() =>
                    this.$router.push('/')
                ).catch(err => this.$emit('error', err));
            },
            deleteAppointment: function (id) {
                deleteAppointment(id).then(() => {
                    this.$router.push('/')
                }).catch(err => this.$emit('error', err));
            },
            loadAppointment: function () {
                this.appointment.id = this.$route.params.id;
                if (this.appointment.id) {
                    this.loading = true;
                    appointment(this.appointment.id).then(data => {
                        this.appointment = {...data, date: data.date && data.date.replace('Z', '')};
                        this.loading = false;
                    }).catch(err => this.$emit('error', err));
                }
            },
            updateAppointment: function (id) {
                updateAppointment(id, this.appointment.title, this.toISOString(this.appointment.date)).then(() => {
                        this.$router.push('/')
                    }
                ).catch(err => this.$emit('error', err));
            },
            toISOString: function (date) {
                return new Date(date).toISOString().split('.')[0] + "Z";
            }
        },
        created() {
            this.loadAppointment()
        },
        data() {
            return {
                appointment: {
                    title: '',
                    date: new Date(),
                },
                loading: false
            }
        },
        watch: {
            '$route': 'loadAppointment'
        },
    }
</script>

<style lang="scss" scoped>

</style>
