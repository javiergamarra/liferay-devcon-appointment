import Vue from 'vue'
import VueRouter from 'vue-router'
import Appointments from "../components/Appointments";
import AddAppointment from "../components/AddAppointment";

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'appointments',
        component: Appointments
    },
    {
        path: '/add/',
        name: 'Add Appointment',
        component: AddAppointment
    },
    {
        path: '/add/:id',
        name: 'Edit Appointment',
        component: AddAppointment
    }
];

const router = new VueRouter({
    routes
});

export default router
