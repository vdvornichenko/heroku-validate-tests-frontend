<template>
    <v-layout row justify-center>
        <v-dialog v-model="dialog" max-width="1200" transition="dialog-bottom-transition">
            <v-card v-model="isFile">
                <v-toolbar dark color="primary">
                    <v-btn icon dark @click="dialog = false; feedBackText = ''; isFeedBack = false; isFile = false">
                        <v-icon>close</v-icon>
                    </v-btn>
                    <v-toolbar-title v-if="isFile">{{ fileOwner }}</v-toolbar-title>
                    <v-toolbar-title v-if="isFeedBack">{{ 'Введите пожелания' }}</v-toolbar-title>
                </v-toolbar>
                <v-subheader v-if="isFile">{{ fileName }}</v-subheader>
                <v-subheader v-if="isFeedBack">{{ 'Оставьте свои пожелания'}}</v-subheader>
                <div v-if="isFeedBack">
                    <v-textarea
                            solo
                            name="input-7-4"
                            label="Текст отзыва"
                            v-model="feedBackText"
                    >{{ feedBackText }}</v-textarea>
                    <v-btn v-on:click="sendFeedback">Отправить</v-btn>
                </div>
                <div id="fileContent" style="padding-left:40px">
                </div>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    import {HTTP_FEEDBACK_URL} from "../Constants";
    export default {
        name: "FilesComponent",
        data: () => ({
            dialog: false,
            fileName: "",
            fileOwner: "",
            isFeedBack: false,
            feedBackText : "",
            isFile : false
        }),

        mounted() {
            this.$root.$on('setUserFile', file => {
               document.getElementById('fileContent').innerHTML = file.content;
               this.fileName = file.fileName;
               this.fileOwner = file.fileOwner;
               this.isFeedBack = false;
               this.isFile = true;
               this.dialog = true;
               this.isShowAlert = false;
            });

            this.$root.$on('setFeedBack', () => {
                document.getElementById('fileContent').innerHTML = "";
                this.isFile = false;
                this.isFeedBack = true;
                this.dialog = true;
                this.feedBackText = '';
                this.isShowAlert = false;
            });
        },

        methods: {
            sendFeedback: function () {
                if (this.feedBackText.length === 0) {
                }
                else {
                    this.$http.post(HTTP_FEEDBACK_URL, this.feedBackText).then(() => {
                        // eslint-disable-next-line no-console
                        console.log('SUCCESS');
                    }, () => {
                        // eslint-disable-next-line no-console
                        console.log('ERROR');
                    });
                    this.dialog = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>