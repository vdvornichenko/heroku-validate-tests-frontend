<template>
    <div class="text-xs-center">
        <v-dialog
                v-model="callbackState"
                hide-overlay
                persistent
                width="300"
        >
            <v-card
                    color="primary"
                    dark
            >
                <v-card-text>
                    Please stand by
                    <v-progress-linear
                            indeterminate
                            color="white"
                            class="mb-0"
                    ></v-progress-linear>
                </v-card-text>
                <div style="text-align: center">
                    <v-btn v-on:click="callbackState = false" color="error">Cancel</v-btn>
                </div>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
    export default {
        name: "CallbackSpinner",
        data: () => ({
            callbackState: false
        }),

        mounted() {
            this.$root.$on('setState', state => {
                this.callbackState = state;
            });

            this.$root.$on('runCallback', (callback, eventName) => {
                this.callbackState = true;
               callback.then(response => {
                   if (this.callbackState) {
                       this.$root.$emit(eventName, response.body);
                       this.callbackState = false;
                   }
               }, response => {
                   this.$root.$emit('setAlert', response.body.message, 'error');
                   this.$root.$emit('setState', false);
               });
            })
        }
    }
</script>

<style scoped>

</style>