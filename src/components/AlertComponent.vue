<template>
    <div>
        <v-alert
                v-model="showAlert"
                dismissible
                :type="alertType"
        >
            {{ message }}
        </v-alert>
    </div>
</template>

<script>
    export default {
        name: "AlertComponent",
        data: () => ({
            showAlert : false,
            message : "",
            alertType : "error"
        }),

        mounted() {
            this.$root.$on('setAlert', (message, type) => {
               this.message = message;
               this.alertType = type;
               this.showAlert = true;
            });
        }
    }
</script>

<style scoped>

</style>