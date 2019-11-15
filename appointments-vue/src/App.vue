<template>
    <div class="container" id="app">
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

<style lang="scss">
    @import '../node_modules/@clayui/css/lib/css/atlas.css';

    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
    }
</style>
