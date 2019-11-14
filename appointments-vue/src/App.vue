<template>
    <div id="app">
        <Appointments/>
    </div>
</template>

<script>
    import Appointments from './components/Appointments.vue'

    export default {
        name: 'app',
        components: {
            Appointments: Appointments
        }
    }
</script>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        margin-top: 60px;
    }
</style>
