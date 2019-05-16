<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div v-if="showResults" style="padding-top: 50px">
        <v-text-field
                v-model="userForSearch"
                append-icon="search"
                label="Введите имя пользователя"
                single-line
                hide-details
        ></v-text-field>
        <div style="padding-top: 30px" v-if="Object.keys(userResults).filter(res => res.includes(userForSearch)).length === 0">
            No users
        </div>
        <div v-for="(value, propertyName, index) in userResults" v-bind:key="index">
            <div v-if="propertyName.includes(userForSearch)" style="padding-top: 30px">
                <v-toolbar flat>
                    <v-toolbar-title>{{ propertyName }}</v-toolbar-title>
                </v-toolbar>
                <template v-if="usersErrors[propertyName]">
                    <v-alert :value="true" color="error" icon="warning" v-for="(error, index) in usersErrors[propertyName]" v-bind:key="index">
                        {{ error }}
                    </v-alert>
                </template>
                <v-data-table
                              v-if="!usersErrors[propertyName]"
                              disable-initial-sort
                              :headers="userResultsHeaders"
                              :items="value"
                              class="elevation-1"
                >
                    <template v-slot:items="props">
                        <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">{{ props.item.index }}</td>
                        <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">
                            <v-btn
                                    outline fab small
                                    v-if="props.item.resultsList.length > 0"
                                    v-on:click="showMetadataResults(props.item)"
                            >
                                <v-icon>list</v-icon>
                            </v-btn>
                            {{ props.item.nameMetadata }}
                            <table v-if="props.item.showResultsList">
                                <tr v-for="(res, index) in props.item.resultsList" v-bind:key="index">
                                    <td :bgcolor="res.status == 'ERROR' ? errorColor : ''">{{ res.message }}</td>
                                    <!--  -->
                                          <v-btn
                                                    v-if="!res.message.includes(notFound) && props.item.nameMetadata.includes('Test')"
                                                    v-on:click="showFile(propertyName, res.message.substring(7, res.message.indexOf(' ',  8)))"
                                            >
                                                View file
                                            </v-btn>
<!--  -->
                                </tr>
                            </table>
                        </td>
                        <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">{{ props.item.status }}</td>
                        <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">{{ props.item.message }}</td>
                        <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">
                            <v-btn
                                    v-if="!props.item.message.includes(notFound) && !props.item.nameMetadata.includes('Test')"
                                    v-on:click="showFile(propertyName, props.item.nameMetadata)"
                            >
                                View file
                            </v-btn>
                        </td>
                    </template>
                    <template v-slot:footer>
                        <td :colspan="userResultsHeaders.length">
                            <strong style="float: right">
                                {{ getReport(value) }}
                            </strong>
                        </td>
                    </template>
                </v-data-table>
            </div>
        </div>
    </div>
</template>

<script>
    import { NOT_FOUND_MESSAGE } from "../Constants";
    import { HTTP_FILE_URL } from "../Constants";
    import { ERROR_COLOR } from "../Constants";
    import { ERRORS_NUMBER_MESSAGE} from "../Constants";

    export default {
        name: "Results",
        data: () => ({
            showResults : false,
            userResults : [],
            userResultsHeaders : [
                {text: '#', value: 'index', sortable: false},
                {text : "Metadata File", value : "nameMetadata"},
                {text : "Status", value : "status"},
                {text : "Message", value : "message"},
                {text : "View file", value : "file"}
            ],
            notFound: NOT_FOUND_MESSAGE,
            errorColor: ERROR_COLOR,
            usersErrors: {},
            userForSearch: ''
        }),

        mounted() {
            this.$root.$on('getUserResults', results => {
                // eslint-disable-next-line no-console
                console.log(results);
                let totalResults = {};
                for (let userName in results) {
                    let resultsOfUser = [];
                    results[userName].forEach(res => {
                        if (res.nameMetadata === null) {
                            let errors = this.usersErrors[userName];
                            if (!errors) {
                                errors = [res.message];
                            }
                            this.usersErrors[userName] = errors;
                        } else {
                            if (resultsOfUser.filter(value => value.nameMetadata === res.nameMetadata).length === 0) {
                                let fileResults = results[userName].filter(elem => elem.nameMetadata === res.nameMetadata);

                                let errors = fileResults.filter(val => val.status === 'ERROR');
                                let resultMessage = ERRORS_NUMBER_MESSAGE + errors.length;
                                if (errors.length === 1) {
                                    if (errors[0].message.includes(NOT_FOUND_MESSAGE)) {
                                        resultMessage = errors[0].message;
                                        fileResults = [];
                                    }
                                }
                                let status = errors.length === 0 ? 'SUCCESS' : 'ERROR';
                                resultsOfUser.push({
                                    index: resultsOfUser.length + 1,
                                    nameMetadata: res.nameMetadata,
                                    status: status,
                                    message: resultMessage,
                                    resultsList: fileResults,
                                    showResultsList: false
                                });
                            }
                        }
                    });
                    totalResults[userName] = resultsOfUser;
                }
                this.userResults = totalResults;
                this.showResults = true;
            });
        },

        methods: {
            showFile: function(fileOwner, fileName) {
                this.$root.$emit(
                    'runCallback',
                    this.$http.post(HTTP_FILE_URL, fileOwner + ';' + fileName),
                    'setUserFile'
                );
            },

            showMetadataResults: function(result) {
                result.showResultsList = !result.showResultsList;
                if (result.status === 'ERROR' || result.status === '') {
                    result.status = (result.status === 'ERROR') ? '' : 'ERROR';
                }
            },

            getReport: function(result) {
                let beginningTasks = result.filter(val => val.resultsList.length !== 0);
                let finishedTasks = beginningTasks.filter(el => el.status === 'SUCCESS').length;
                beginningTasks = beginningTasks.length - finishedTasks;
                let notBeginningTasks = result.length - beginningTasks - finishedTasks;

                return 'Заданий выполнено без ошибок: ' + finishedTasks +
                    ', Заданий выполнено с ошибками: ' + beginningTasks +
                    ', Заданий не выполнено: ' + notBeginningTasks;
            }
        }
    }
</script>

<style scoped>

</style>