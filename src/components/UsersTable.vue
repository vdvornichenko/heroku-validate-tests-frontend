<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-card>
        <v-container>
        <v-select
                multiple
                :items="Array.from(groups).sort((a, b) => a - b)"
                label="Выберите номер группы"
                v-model="currentGroups"
        ></v-select>
        </v-container>
        <v-card-title v-if="currentGroups.length > 0">
            Группы {{ currentGroups.join(', ') }}
            <v-spacer></v-spacer>
            <v-text-field
                    v-model="search"
                    append-icon="search"
                    label="Search"
                    single-line
                    hide-details
            ></v-text-field>
        </v-card-title>
        <v-data-table disable-initial-sort
                      :headers="usersTableHeaders"
                      :items="getCurrentUsers()"
                      class="elevation-1"
                      :search="search"
                      v-if="currentGroups.length > 0"
        >
            <template v-slot:items="props" >
                <td>{{ props.item.index + 1 }}</td>
                <td><input type="checkbox" v-model="props.item.checked"/></td>
                <td class="text-xs-left">
                    <a v-bind:href="'https://login.salesforce.com/?un=' + props.item.userName + '&pw=' + props.item.password">
                        {{ props.item.userName }}
                    </a>
                </td>
                <td class="text-xs-left">{{ props.item.fio }}</td>
                <td class="text-xs-left">{{ props.item.group }}</td>
            </template>
        </v-data-table>
        <div style="float: right;" v-if="currentGroups.length > 0">
            <v-btn color="info" v-on:click="getAllUsersInfo">Show Results for all users</v-btn>
            <v-btn color="info" v-on:click="getSelectedUsersInfo">Show Results for selected users</v-btn>
            <v-btn color="info" v-on:click="refresh">Refresh</v-btn>
        </div>
    </v-card>
</template>

<script>
    import { NO_SELECTED_USERS_MESSAGE } from "../Constants";
    import { HTTP_USER_CREDS_URL } from "../Constants";
    import { HTTP_USERS_INFO_URL } from "../Constants";
    export default {
        name: "UsersTable",
        data: () => ({
            users : [],
            usersTableHeaders : [
                {text : "#", value : "index", sortable: false, align: "left"},
                {text : "Select User", value : "checked", sortable : false},
                {text : "Login", value : "userName", sortable: false},
                {text : 'User name', value : 'fio', sortable: false},
                {text : "Group", value : "group", sortable: false},

            ],
            search: '',
            groups: [''],
            currentGroups: []
        }),
        created() {
            this.getUsersCreds();
        },

        methods: {
            getUsersCreds: function() {
                this.$root.$emit('setState', true);
                this.$http.get(HTTP_USER_CREDS_URL).then(response => {
                    let groups = [];
                    response.body.forEach((elem, i) => {
                        this.users.push({
                            index : i, userName : elem.userName,
                            password : elem.password, checked: false,
                            group: elem.group, fio: elem.fio
                        });
                        groups.push(elem.group);
                    });
                    this.groups = new Set(groups);
                    this.$root.$emit('setState', false);

                });
            },

            getCurrentUsers: function() {
                return  this.users.filter(elem => this.currentGroups.includes(elem.group));
            },

            refresh: function() {
                this.users = [];
                this.getUsersCreds();
            },

            getAllUsersInfo: function () {
                let users = [];
                this.getCurrentUsers().forEach((elem) => {
                    users.push(elem.userName);
                });
                this.getResults(users.join(";"));
            },

            getSelectedUsersInfo: function () {
                let users = [];
                this.getCurrentUsers().forEach((elem) => {
                    if (elem.checked) {
                        users.push(elem.userName);
                    }
                });
                this.getResults(users.join(";"));
            },
            getResults: function (users) {
                if (users === "") {
                    this.$root.$emit('setAlert', NO_SELECTED_USERS_MESSAGE, 'error');
                } else {
                    this.$root.$emit('runCallback', this.$http.post(HTTP_USERS_INFO_URL, users), 'getUserResults');
                }
            }
        }
    }
</script>

<style scoped>

</style>