<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div v-if="showResults" style="padding-top: 30px">
        <div v-for="(value, propertyName, index) in userResults" v-bind:key="index" style="padding-top: 30px">
            <v-toolbar flat>
                <v-toolbar-title>{{ propertyName }}</v-toolbar-title>
            </v-toolbar>
            <v-data-table
                          :headers="userResultsHeaders"
                          :items="value"
                          class="elevation-1"
            >
                <template v-slot:items="props">
                    <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">{{ props.item.nameMetadata }}</td>
                    <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">{{ props.item.status }}</td>
                    <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">{{ props.item.message }}</td>
                    <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">
                        <v-btn
                                v-if="!props.item.message.includes(notFound)"
                                v-on:click="showFile(propertyName, props.item.nameMetadata)"
                        >
                            View file
                        </v-btn>
                    </td>
                </template>
            </v-data-table>
        </div>
    </div>
</template>

<script>
    import { NOT_FOUND_MESSAGE } from "../Constants";
    import { HTTP_FILE_URL } from "../Constants";
    import { ERROR_COLOR } from "../Constants";

    export default {
        name: "Results",
        data: () => ({
            showResults : false,
            userResults : [],
            userResultsHeaders : [
                {text : "Metadata File", value : "nameMetadata"},
                {text : "Status", value : "status"},
                {text : "Message", value : "message"},
                {text : "View file", value : "file"}
            ],
            notFound: NOT_FOUND_MESSAGE,
            errorColor: ERROR_COLOR
        }),

        mounted() {
            this.$root.$on('getUserResults', results => {
                this.userResults = results;
                this.showResults = true;
            });
        },

        methods: {
            showFile: function(fileOwner, fileName) {
                this.$root.$emit('setState', true);
                this.$http.post(HTTP_FILE_URL, fileOwner + ';' + fileName).then(response => {
                    this.$root.$emit('setUserFile', response.body);
                    this.$root.$emit('setState', false);
                }, response => {
                    this.$root.$emit('setAlert', response.body.bodyText.error, 'error');
                    this.$root.$emit('setState', false);
                });
            }
        }
    }
</script>

<style scoped>

</style>