<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-card>
        <input type="hidden" v-model="showTableForm"/>
        <v-card-title>
            Группы: {{ currentGroups.length === 0 ? 'все группы' : currentGroups.join(', ') }}
            <v-icon v-on:click="refresh" large style="padding-left: 50px">cached</v-icon>
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
                      :rows-per-page-items="rowsPerPage"
        >
            <template v-slot:headers="props">
                <tr>
                    <th>
                        <v-checkbox v-model="allUsersSelected" v-on:change="selectAllUsers" hide-details/>
                    </th>
                    <th v-for="header in props.headers" :key="header.text" style="text-align: left">
                        {{ header.text }}
                    </th>
                </tr>
            </template>
            <template v-slot:items="props" >
                <td>
                    <v-checkbox v-model="props.item.checked" hide-details/>
                </td>
                <td>{{ props.item.index + 1 }}</td>
                <td class="text-xs-left">
                    <v-tooltip bottom>
                        <template v-slot:activator="{ on }">
                            <span v-on="on">
                                <a
                                        v-bind:href="'https://login.salesforce.com/?un=' + props.item.userName + '&pw=' + props.item.password"
                                        target="_blank"
                                >
                                    {{ props.item.userName }}
                                </a>
                            </span>
                        </template>
                        <span>Перейти на орг</span>
                    </v-tooltip>
                </td>
                <td class="text-xs-left">{{ props.item.fio }}</td>
                <td class="text-xs-left">{{ props.item.group }}</td>
            </template>
        </v-data-table>
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
                {text : "Login(Можно перейти на орг)", value : "userName", sortable: false},
                {text : 'User name', value : 'fio', sortable: false},
                {text : "Group", value : "group", sortable: false},

            ],
            search: '',
            groups: [''],
            currentGroups: [],
            rowsPerPage: [10,25,5,{"text":"$vuetify.dataIterator.rowsPerPageAll","value":-1}],
            onlyWithCreds: true,
            allUsersSelected: false
        }),

        props : {
            showTable: Boolean
        },

        mounted() {
            this.$root.$on('selectGroups', currentGroups => {this.currentGroups = currentGroups});

            this.$root.$on('getSelectedUsersInfo', this.getSelectedUsersInfo);

            this.$root.$on('setOnlyWithCredsMod', () => {this.onlyWithCreds = !this.onlyWithCreds});

            this.$root.$on('openMainPage', () => {
                this.getUsersCreds()
            });
        },

        computed: {
            showTableForm : function () {
                if (this.showTable) {
                    this.getUsersCreds();
                }
                return this.showTable;
            }
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
                    let curGroups = this.$cookies.get('currentGroups');
                    if (curGroups) {
                        this.currentGroups = curGroups.split(',').filter(elem => groups.includes(elem));
                    }
                    this.$root.$emit('setGroups', new Set(groups), this.currentGroups);
                    this.$root.$emit('setState', false);

                }, response => {
                    this.$root.$emit('setAlert', response.body.message, 'error');
                    this.$root.$emit('setState', false);
                });
            },

            getCurrentUsers: function() {
                let users = this.onlyWithCreds ? this.users.filter(elem => elem.userName !== '' && elem.password !== '') : this.users;
                if (this.currentGroups.length === 0) {
                    return users;
                }
                return users.filter(elem => this.currentGroups.includes(elem.group));
            },

            refresh: function() {
                this.users = [];
                this.getUsersCreds();
            },

            getSelectedUsersInfo: function () {
                let users = [];
                this.getCurrentUsers().forEach((elem) => {
                    if (elem.checked) {
                        users.push(elem.userName);
                    }
                });
                return this.getResults(users);
            },
            getResults: function (users) {
                if (users.includes('')) {
                    this.$root.$emit('setAlert', 'Выбраны пользователи без логинов', 'error');
                } else if (users.length === 0) {
                    this.$root.$emit('setAlert', NO_SELECTED_USERS_MESSAGE, 'error');
                } else {
                    this.$root.$emit('runCallback', this.$http.post(HTTP_USERS_INFO_URL, users.join(';')), 'getUserResults');
                    this.$root.$emit('showUsersSearch');
                }
            },

            selectAllUsers: function () {
                let currentUsers = this.getCurrentUsers();
                if (this.allUsersSelected) {
                    this.users = this.users.map(el => {
                        if (currentUsers.includes(el)) {
                            el.checked = true;
                        }
                        return el;
                    });
                } else {
                    this.users = this.users.map(el => {
                        if (currentUsers.includes(el)) {
                            el.checked = false;
                        }
                        return el;
                    });
                }
            }
        }
    }
</script>

<style scoped>

</style>