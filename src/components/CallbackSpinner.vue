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
        }
    }
</script>

<style scoped>

</style>