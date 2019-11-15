<template>
    <div class="sheet">
        <form>

            <h1>{{appointment.id ? 'Editing' : 'Adding'}} an appointment</h1>

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
                    <button @click="createAppointment()" class="btn btn-group-item btn-primary" v-if="!appointment.id">
                        Create an
                        appointment
                    </button>
                    <button @click="event => updateAppointment(appointment.id)" class="btn btn-group-item btn-primary"
                            v-if="appointment.id">Update
                    </button>
                    <button @click="deleteAppointment(appointment.id)" class="btn btn-group-item btn-danger"
                            v-if="appointment.id">
                        Delete
                    </button>
                    <button @click="() => this.$router.push('/')" class="btn btn-group-item btn-secondary">
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
                )
            },
            deleteAppointment: function (id) {
                deleteAppointment(id).then(() => {
                    this.$router.push('/')
                })
            },
            loadAppointment: function (id) {
                appointment(id).then(data => {
                    data.date = data.date && data.date.replace('Z', '');
                    return this.appointment = data;
                });
            },
            updateAppointment: function (id) {
                updateAppointment(id, this.appointment.title, this.toISOString(this.appointment.date)).then(() => {
                        this.$router.push('/')
                    }
                )
            },
            toISOString: function (date) {
                return new Date(date).toISOString().split('.')[0] + "Z";
            }
        },
        data() {
            return {
                appointment: {
                    title: '',
                    date: new Date(),
                },
            }
        },
        mounted() {
            if (this.$route.params.id) {
                this.loadAppointment(this.$route.params.id)
            }
        },
        props: {}
    }
</script>

<style lang="scss" scoped>

</style>
