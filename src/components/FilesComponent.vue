<template>
    <v-layout row justify-center>
        <v-dialog v-model="dialog" max-width="1200" transition="dialog-bottom-transition">
            <v-card style="overflow-x: auto">
                <v-toolbar dark color="primary">
                    <v-btn icon dark @click="dialog = false">
                        <v-icon>close</v-icon>
                    </v-btn>
                    <v-toolbar-title v-if="isFile">{{ fileOwner }}</v-toolbar-title>
                    <v-toolbar-title v-if="isFeedBack">Введите пожелания</v-toolbar-title>
                </v-toolbar>
                <v-alert
                        v-model="showAlert"
                        dismissible
                        type="error"
                >
                    {{ message }}
                </v-alert>
                <v-subheader v-if="isFile">{{ fileName }}</v-subheader>
                <v-subheader v-if="isFeedBack">Оставьте свои пожелания</v-subheader>
                <div v-if="isFeedBack">
                    <v-container>
                        <v-textarea
                                solo
                                label="Текст отзыва"
                                v-model="feedBackText"
                        >{{ feedBackText }}</v-textarea>
                        <v-btn v-on:click="sendFeedback">Отправить</v-btn>
                    </v-container>
                </div>
                <pre id="fileContent">
                </pre>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    import {HTTP_FEEDBACK_URL} from "../Constants";
    import {NO_FEEDBACK_MESSAGE} from "../Constants";
    import {FEEDBACK_SUCCESS_MESSAGE} from "../Constants";
    import {FEEDBACK_ERROR_MESSAGE} from "../Constants";
    export default {
        name: "FilesComponent",
        data: () => ({
            dialog: false,
            fileName: "",
            fileOwner: "",
            isFeedBack: false,
            feedBackText : "",
            isFile : false,
            showAlert: false,
            message: "",
        }),

        mounted() {
            this.$root.$on('setUserFile', file => {
                file = file.body;
               document.getElementById('fileContent').innerHTML = file.content;
               this.fileName = file.fileName;
               this.fileOwner = file.fileOwner;
               this.isFeedBack = false;
               this.isFile = true;
               this.dialog = true;
                this.showAlert = false;
            });

            this.$root.$on('setFeedBack', () => {
                document.getElementById('fileContent').innerHTML = "";
                this.isFile = false;
                this.isFeedBack = true;
                this.dialog = true;
                this.feedBackText = '';
                this.showAlert = false;
            });

        },

        methods: {
            sendFeedback: function () {
                if (this.feedBackText.length === 0) {
                    this.message = NO_FEEDBACK_MESSAGE;
                    this.showAlert = true;
                }
                else {
                    this.$http.post(HTTP_FEEDBACK_URL, this.feedBackText).then(() => {
                        this.$root.$emit('setAlert', FEEDBACK_SUCCESS_MESSAGE, 'success');
                    }, () => {
                        this.$root.$emit('setAlert', FEEDBACK_ERROR_MESSAGE, 'error');
                    });
                    this.dialog = false;
                }
            }
        }
    }
</script>

<style scoped>

    #fileContent {
        padding-left: 40px;
    }
</style>