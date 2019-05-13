<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-card>
        <v-card-title>
            All users
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
                      :items="users"
                      class="elevation-1"
                      :search="search"
        >
            <template v-slot:items="props" >
                <td>{{ props.item.index + 1 }}</td>
                <td><input type="checkbox" v-model="props.item.checked"/></td>
                <td class="text-xs-left">
                    <a v-bind:href="'https://login.salesforce.com/?un=' + props.item.userName + '&pw=' + props.item.password">
                        {{ props.item.userName }}
                    </a>
                </td>
                <td class="text-xs-left">{{ props.item.password }}</td>
            </template>
        </v-data-table>
        <div style="float: right;">
            <v-btn color="info" v-on:click="getAllUsersInfo">Show Results for all users</v-btn>
            <v-btn color="info" v-on:click="getSelectedUsersInfo">Show Results for selected users</v-btn>
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
            {text : "User Name", value : "userName"},
            {text : "Password", value : "password"}
            ],
            search: ''
        }),
        created() {
            // eslint-disable-next-line no-console
            console.log(HTTP_USER_CREDS_URL);
            this.$http.get(HTTP_USER_CREDS_URL).then(response => {
                response.body.forEach((elem, i) => {
                    this.users.push({index : i, userName : elem.userName, password : elem.password, checked: false});
                });
            });

        },

        methods: {
            getAllUsersInfo: function () {
                this.getResults("all");
            },

            getSelectedUsersInfo: function () {
                let users = [];
                this.users.forEach((elem) => {
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
                    this.$root.$emit('setState', true);
                    this.$http.post(HTTP_USERS_INFO_URL, users).then(response => {
                        this.$root.$emit('getUserResults', response.body);
                        this.$root.$emit('setState', false);
                    }, response => {
                        this.$root.$emit('setAlert', response.body.bodyText.error, 'error');
                        this.$root.$emit('setState', false);
                    });
                }
            }
        }
    }
</script>

<style scoped>

</style>