<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
<!--        <v-navigation-drawer-->
<!--                v-model="primaryDrawer.model"-->
<!--                :permanent="primaryDrawer.type === 'permanent'"-->
<!--                :temporary="primaryDrawer.type === 'temporary'"-->
<!--                :clipped="primaryDrawer.clipped"-->
<!--                :floating="primaryDrawer.floating"-->
<!--                :mini-variant="primaryDrawer.mini"-->
<!--                absolute-->
<!--                overflow-->
<!--                app-->
<!--        >-->
<!--        </v-navigation-drawer>-->
        <v-toolbar :clipped-left="primaryDrawer.clipped" app absolute>
            <v-toolbar-title>Task Checker</v-toolbar-title>
            <v-spacer></v-spacer>
            <div class="right-part">
                <v-layout row wrap>
                    <v-flex lg8 style="padding-top:15px">
                        <v-btn v-on:click="sendFeedBack" color="info" style="padding-right: 20px">Оставить пожелание</v-btn>
<!--                        <v-icon large color="blue darken-2" v-on:click="sendFeedBack">chat</v-icon>-->
                    </v-flex>
                    <v-flex lg3>
                        <br/>
                        <v-switch primary :label="dark ? 'Dark' : 'Light'" v-on:change="chooseTheme"></v-switch>
                    </v-flex>
                    <v-flex lg1>
                        <br/>
                        <v-tooltip bottom>
                            <template v-slot:activator="{ on }">
                                <span v-on="on">
                                    <v-icon color="primary" large v-on:click="logOut">logout</v-icon>
                                </span>
                            </template>
                            <span>Выход</span>
                        </v-tooltip>
                    </v-flex>
                </v-layout>
            </div>
        </v-toolbar>
    </div>
</template>

<script>
    export default {
        name: "Header",
        data: () => ({
            drawers: ['Default (no property)', 'Permanent', 'Temporary'],
            primaryDrawer: {
                model: false,
                type: 'Default (no property)',
                clipped: true,
                floating: true,
                mini: false
            },
            showDrawer: false,
            dark: true
        }),

        methods: {
            chooseTheme: function () {
                this.$emit('changeTheme');
                this.dark = !this.dark;
            },
            sendFeedBack: function () {
                this.$root.$emit('setFeedBack');
            },

            logOut: function () {
                this.$emit('logout');
                this.$root.$emit('openLoginForm');
            }
        }
    }
</script>

<style scoped>

    .right-part {
        padding-right: 30px;
    }
</style>