<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-container>
        <v-select
                multiple
                :items="Array.from(groups).sort((a, b) => a - b)"
                label="Выберите номер группы"
                v-model="currentGroups"
                v-on:change="changeGroup"
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
        <div style="text-align: center">
            <v-checkbox v-on:change="setOnlyWithCredsMod" label="Креды должны быть заполнены">
            </v-checkbox>
            Проверить<br/>
            <v-btn color="info" v-on:click="getAllUsersInfo" style="width: 100%">Всех</v-btn>
            <v-btn color="info" v-on:click="getSelectedUsersInfo" style="width: 100%">Выбранных</v-btn>
        </div>
        <v-text-field
                v-if="usersAreShowed"
                v-model="userForSearch"
                append-icon="search"
                label="Введите имя пользователя"
                single-line
                hide-details
                v-on:keyup="searchUserByName"
        ></v-text-field>
<!--        <v-btn color="info" v-on:click="refresh">Refresh</v-btn>-->
    </v-container>
</template>

<script>
    export default {
        name: "LeftSidebar",

        data() {
            return {
                groups: [],
                currentGroups: [],
                allSelected: false,
                usersAreShowed: false,
                userForSearch: ""
            }
        },

        mounted() {
            this.$root.$on('setGroups', (groups, currentGroups) => {
                this.groups = groups;
                this.currentGroups = currentGroups;
            });

            this.$root.$on('showUsersSearch', () => {this.usersAreShowed = true});
        },

        methods: {
            toggle: function() {
                if (this.allSelected) {
                    this.currentGroups = Array.from(this.groups).sort((a, b) => a - b);
                } else {
                    this.currentGroups = [];
                }
                this.$root.$emit('selectGroups', this.currentGroups);
            },

            changeGroup: function () {
                this.$root.$emit('selectGroups', this.currentGroups);

                this.$cookies.set('currentGroups', this.currentGroups);
                // eslint-disable-next-line no-console
                console.log(this.$cookies.get('currentGroups'));
            },

            getAllUsersInfo: function () {
                this.$root.$emit('getAllUsersInfo');
            },

            getSelectedUsersInfo: function () {
                this.$root.$emit('getSelectedUsersInfo');
            },

            searchUserByName: function () {
                this.$root.$emit('searchUser', this.userForSearch);
            },

            setOnlyWithCredsMod: function () {
                this.$root.$emit('setOnlyWithCredsMod');
            }
        }
    }
</script>

<style scoped>

</style>