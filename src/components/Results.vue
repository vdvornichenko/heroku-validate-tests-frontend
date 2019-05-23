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
                            <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">
                                {{ props.item.index + 1 }}
                            </td>
                            <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">
                                {{ props.item.taskName }}
                            </td>
                            <td :bgcolor="props.item.status == 'ERROR' ? errorColor : ''">
                                {{ props.item.status }}
                            </td>
                            <td>
                                <v-layout
                                        v-for="(taskResult, index) in props.item.taskResults"
                                        :key="index" row wrap
                                        v-bind:style="{ backgroundColor: (taskResult.status == 'ERROR') ? errorColor : '' }"
                                        style="border: none; height: 100%; width: 100%"
                                >
                                    <v-flex lg6>
                                        <v-layout row wrap>
                                            <v-flex lg3>
                                                <v-menu offset-y v-if="taskResult.resultsList.length > 0">
                                                    <template v-slot:activator="{ on }">
                                                        <v-btn
                                                                v-on="on"
                                                                flat
                                                                fab small
                                                        >
                                                            <v-icon v-if="!taskResult.showResultsList">list</v-icon>
                                                            <v-icon v-if="taskResult.showResultsList">arrow_upward</v-icon>
                                                        </v-btn>
                                                    </template>
                                                    <v-list>
                                                        <v-list-tile
                                                                v-for="(resultMessage, ind) in taskResult.resultsList" :key="ind"
                                                                v-bind:style="{ backgroundColor: (resultMessage.status == 'ERROR') ? errorColor : '' }"
                                                        >
                                                            <v-list-tile-title>{{ resultMessage.message }}</v-list-tile-title>
                                                        </v-list-tile>
                                                    </v-list>
                                                </v-menu>
                                            </v-flex>
                                            <v-flex lg9>
                                                <br/>
                                                {{ taskResult.nameMetadata }}
                                            </v-flex>
                                        </v-layout>
                                    </v-flex>
                                    <v-flex lg4 :color="(taskResult.status == 'ERROR') ? errorColor : '' ">
                                        <div :style="getBlockColor()">
                                            <br/>
                                            {{ taskResult.message }}
                                        </div>
                                    </v-flex>
                                    <v-flex lg2>
                                        <div :style="'background-color:' + (taskResult.status == 'ERROR') ? errorColor : '' ">
                                            <v-btn
                                                    v-if="taskResult.resultsList.length > 0 && !taskResult.nameMetadata.includes('Test')"
                                                    v-on:click="showFile(propertyName, taskResult.nameMetadata)"
                                            >
                                                View file
                                            </v-btn>
                                        </div>
                                    </v-flex>
                                </v-layout>
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
                {text: "Status", value: "status", sortable: false},
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

            showMetadataResults: function (result) {
                result.showResultsList = !result.showResultsList;
                if (result.status === 'ERROR' || result.status === '') {
                    result.status = (result.status === 'ERROR') ? '' : 'ERROR';
                }
            },

            getReport: function (result) {
                let res = [];
                for (let task in result) {
                    res = res.concat(result[task].taskResults);
                }
                let beginningTasks = res.filter(val => val.resultsList.length !== 0);
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

    .results {
        padding-top: 50px;
    }

    .results-table {
        padding-top: 30px;
    }
</style>