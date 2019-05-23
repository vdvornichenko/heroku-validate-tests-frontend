<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div id="resultTables">
        <div v-if="showResults" class="results">
            <div class="results-table"
                 v-if="Object.keys(userResults).filter(res => res.includes(userForSearch)).length === 0">
                No users
            </div>
            <div v-for="(value, propertyName, index) in userResults" v-bind:key="index">
                <div v-if="propertyName.includes(userForSearch)" class="results-table">
                    <v-toolbar flat>
                        <v-toolbar-title>{{ propertyName }}</v-toolbar-title>
                        <v-spacer></v-spacer>
                        <div class="text-xs-center" v-if="usersLoginHistories[propertyName]">
                            <v-menu offset-y>
                                <template v-slot:activator="{ on }">
                                    <v-btn
                                            color="primary"
                                            dark
                                            v-on="on"
                                    >
                                        История входов
                                    </v-btn>
                                </template>
                                <v-list>
                                    <v-list-tile
                                            v-for="(item, index) in usersLoginHistories[propertyName]"
                                            :key="index"
                                    >
                                        <v-list-tile-title>{{ item }}</v-list-tile-title>
                                    </v-list-tile>
                                </v-list>
                            </v-menu>
                        </div>
                        <div v-if="!usersLoginHistories[propertyName]">No login history</div>
                    </v-toolbar>
                    <template v-if="usersErrors[propertyName]">
                        <v-alert :value="true" color="error" icon="warning"
                                 v-for="(error, index) in usersErrors[propertyName]" v-bind:key="index">
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
                            <tr>
                                <td>
                                    {{ props.item.index + 1 }}
                                </td>
                                <td>
                                    {{ props.item.taskName }}
                                </td>
                                <td
                                        :class="props.item.status == 'ERROR' ? 'font-weight-medium error--text' : 'success--text'"
                                >
                                    {{ props.item.status }}
                                </td>
                                <td>
                                    <div
                                            style="width: 100%"
                                            v-for="(taskResult, index) in props.item.taskResults"
                                            :key="index"
                                    >
                                        <v-layout row wrap>
                                            <v-flex lg9>
                                                <v-list expand dense
                                                        :class="taskResult.status == 'ERROR' ? 'primary error' : ''"
                                                        v-if="taskResult.resultsList.length > 0"
                                                >
                                                    <v-list-group
                                                                no-action
                                                                sub-group
                                                                fab
                                                    >
                                                        <template v-slot:activator >
                                                            <v-list-tile >
                                                                <v-list-tile-title>
                                                                    {{ taskResult.nameMetadata }} - {{ taskResult.message }}
                                                                </v-list-tile-title>
                                                            </v-list-tile>
                                                        </template>
                                                        <v-list-tile
                                                                v-for="(resultMessage, ind) in taskResult.resultsList" :key="ind"
                                                                :class="resultMessage.status == 'ERROR' ? 'primary error' : 'primary secondary'"
                                                        >
                                                            <v-list-tile-content >
                                                                <span class="white--text font-weight-light">&nbsp;&nbsp;&nbsp;{{ resultMessage.message }}</span>
                                                            </v-list-tile-content>
                                                        </v-list-tile>
                                                    </v-list-group>
                                                </v-list>
                                                <v-list v-if="taskResult.resultsList.length == 0" class="primary error" dense>
                                                    <v-list-tile>
                                                        <v-list-tile-title>
                                                            {{ taskResult.nameMetadata }} - {{ taskResult.message }}
                                                        </v-list-tile-title>
                                                    </v-list-tile>
                                                </v-list>
                                            </v-flex>
                                            <v-flex lg3>
                                                <v-btn
                                                        v-on:click="showFile(propertyName, taskResult.nameMetadata)"
                                                        style="float:right"
                                                        v-if="taskResult.resultsList.length != 0"
                                                >
                                                    View file
                                                </v-btn>
                                            </v-flex>
                                        </v-layout>
                                    </div>
                                </td>
                            </tr>
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
    </div>
</template>

<script>
    import {NOT_FOUND_MESSAGE} from "../Constants";
    import {HTTP_FILE_URL} from "../Constants";
    import {ERROR_COLOR} from "../Constants";
    import {ERRORS_NUMBER_MESSAGE} from "../Constants";

    export default {
        name: "Results",
        data: () => ({
            showResults: false,
            userResults: [],
            userResultsHeaders: [
                {text: '#', value: 'index', sortable: false},
                {text: 'Task Name', value: 'taskName', sortable: false},
                {text: 'Status', value: 'status', sortable: false},
                {text: "Metadata Files", value: "taskResults", sortable: false}
            ],
            notFound: NOT_FOUND_MESSAGE,
            errorColor: ERROR_COLOR,
            usersErrors: {},
            userForSearch: '',
            usersLoginHistories: {}
        }),

        mounted() {
            this.$root.$on('getUserResults', results => {
                results = results.body;
                // eslint-disable-next-line no-console
                console.log(results);
                let totalResults = {};
                for (let userName in results) {
                    let resultsOfUser = [];
                    this.usersErrors[userName] = results[userName].errors;
                    this.usersLoginHistories[userName] = results[userName].loginHistoryList;
                    if (results[userName].results) {
                        let userResults = results[userName].results;
                        for (let task in userResults) {
                            let taskResults = [];
                            userResults[task].forEach(taskResult => {
                                if (taskResults.filter(val => val.nameMetadata === taskResult.nameMetadata).length === 0) {
                                    let fileResults = userResults[task].filter(elem => elem.nameMetadata === taskResult.nameMetadata);
                                    let errors = fileResults.filter(val => val.status === 'ERROR');
                                    let resultMessage = ERRORS_NUMBER_MESSAGE + errors.length;
                                    if (errors.length === 1) {
                                        if (errors[0].message.includes(NOT_FOUND_MESSAGE)) {
                                            resultMessage = errors[0].message;
                                            fileResults = [];
                                        }
                                    }
                                    let status = errors.length === 0 ? 'SUCCESS' : 'ERROR';
                                    taskResults.push({
                                        nameMetadata: taskResult.nameMetadata,
                                        status: status,
                                        message: resultMessage,
                                        resultsList: fileResults,
                                        showResultsList: false
                                    });
                                }
                            });
                            let taskStatus = taskResults.filter(res => res.status === 'ERROR').length === 0 ? 'SUCCESS' : 'ERROR';
                            resultsOfUser.push({taskName: task, taskResults: taskResults, status: taskStatus, index: resultsOfUser.length});
                        }
                    }
                    totalResults[userName] = resultsOfUser;
                }
                this.userResults = totalResults;
                this.showResults = true;
            });

            this.$root.$on('searchUser', user => {this.userForSearch = user});
        },

        methods: {
            getBlockColor: function (taskResult) {
                return 'background-color:' + (taskResult.status == 'ERROR') ? this.errorColor : '';
            },

            showFile: function (fileOwner, fileName) {
                this.$root.$emit(
                    'runCallback',
                    this.$http.post(HTTP_FILE_URL, fileOwner + ';' + fileName),
                    'setUserFile'
                );
            },

            getReport: function (result) {
                if (result) {
                    let errorTasks = result.filter(res => res.status === 'ERROR');
                    let notBeginningTasks = errorTasks.filter(res => {
                       let notFoundNumber = 0;
                       res.taskResults.forEach(el => {
                           if (el.resultsList.length === 0) {
                               notFoundNumber ++;
                           }
                       });
                       if (notFoundNumber == res.taskResults.length) {
                           return true;
                       }
                       return false;
                    }).length;
                    let beginningTasks = errorTasks.length - notBeginningTasks;
                    let finishedTasks = result.length - beginningTasks - notBeginningTasks;
                    return 'Заданий выполнено без ошибок: ' + finishedTasks +
                        ', Заданий выполнено с ошибками: ' + beginningTasks +
                        ', Заданий не выполнено: ' + notBeginningTasks;
                } else {
                    return '';
                }
            }
        }
    }
</script>

<style scoped>

    .results {
        padding-top: 50px;
    }

    .results-table {
        padding-top: 30px;
    }

    .elevation-1 .v-table tbody tr:hover{
        background-color: inherit
    }
</style>