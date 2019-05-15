<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-card>
        <v-container>
            <v-select
                    multiple
                    :items="Array.from(groups).sort((a, b) => a - b)"
                    label="Выберите номер группы"
                    v-model="currentGroups"
            >
                <template v-slot:prepend-item>
                    <v-list-tile
                            ripple
                            @click="toggle"
                    >
                        <v-list-tile-action>
                            <v-checkbox v-model="allSelected"/>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>Select All</v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-divider class="mt-2"></v-divider>
                </template>
            </v-select>
        </v-container>
        <v-card-title>
            Группы: {{ currentGroups.length === 0 ? 'все группы' : currentGroups.join(', ') }}
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
        >
            <template v-slot:items="props" >
                <td>{{ props.item.index + 1 }}</td>
                <td><input type="checkbox" v-model="props.item.checked"/></td>
                <td class="text-xs-left">
                    <a
                            v-bind:href="'https://login.salesforce.com/?un=' + props.item.userName + '&pw=' + props.item.password"
                            target="_blank"
                    >
                        {{ props.item.userName }}
                    </a>
                </td>
                <td class="text-xs-left">{{ props.item.fio }}</td>
                <td class="text-xs-left">{{ props.item.group }}</td>
            </template>
        </v-data-table>
        <div style="float: right;">
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
            currentGroups: [],
            allSelected: false
        }),

        created() {
            this.getUsersCreds();
        },

        methods: {
            toggle: function() {
                if (this.allSelected) {
                    this.currentGroups = Array.from(this.groups).sort((a, b) => a - b);
                } else {
                    this.currentGroups = [];
                }
            },

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
                if (this.currentGroups.length === 0) {
                    return this.users;
                }
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
                this.getResults(users);
            },

            getSelectedUsersInfo: function () {
                let users = [];
                this.getCurrentUsers().forEach((elem) => {
                    if (elem.checked) {
                        users.push(elem.userName);
                    }
                });
                this.getResults(users);
            },
            getResults: function (users) {
                if (users.length === 0) {
                    this.$root.$emit('setAlert', NO_SELECTED_USERS_MESSAGE, 'error');
                } else if (users.includes('')) {
                    this.$root.$emit('setAlert', 'Выбраны пользователи без логинов', 'error');
                } else {
                    this.$root.$emit('runCallback', this.$http.post(HTTP_USERS_INFO_URL, users.join(';')), 'getUserResults');
                }
            }
        }
    }
</script>

<style scoped>

</style>