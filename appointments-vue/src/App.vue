<template>
    <div id="app">

        <div class="alert alert-dismissible alert-danger" role="alert"
             v-if="errorMsg">
            <strong class="lead">Error:</strong> {{errorMsg}}
            <button @click="() => errorMsg = ''" aria-label="Close" class="close" data-dismiss="alert" type="button">
                x
            </button>
        </div>

        <router-view @error="error"/>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                errorMsg: '',
            }
        },
        methods: {
            error: function (errorObj) {
                this.errorMsg = errorObj[0].message;
            }
        }
    }
</script>

<style lang="scss">
    @import "../node_modules/@clayui/css/lib/css/atlas.css";

    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #2c3e50;
    }
</style>
