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
                   <v-menu bottom left absolute style="z-index:100">
            <template v-slot:activator="{ on }">
              <v-btn
                dark
                icon
                v-on="on"
              >
                <v-icon>more_vert</v-icon>
              </v-btn>
            </template>

            <v-list style="background-color:white">
              <v-list-tile
                v-for="(item, i) in pages"
                :key="i"
              >
                <router-link :to="item" style="text-decoration: none; color:black">
                     <v-list-tile-title  white >
                     
                            {{ item.title }}
                        
                    </v-list-tile-title>
       </router-link>
              </v-list-tile>
            </v-list>
          </v-menu>
            <v-toolbar-title>
                <router-link to="/" class="routerlink">
                    Task Checker
                </router-link>  
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <div class="right-part">
                <v-layout row wrap>
                    <v-flex lg9 style="padding-top:15px">
                        <v-btn v-on:click="sendFeedBack" color="info" style="padding-right: 20px">Оставить пожелание</v-btn>
<!--                        <v-icon large color="blue darken-2" v-on:click="sendFeedBack">chat</v-icon>-->
                    </v-flex>
                    <v-flex lg3>
                        <br/>
                        <v-switch primary :label="dark ? 'Dark' : 'Light'" v-on:change="chooseTheme"></v-switch>
                    </v-flex>
<!--                    <v-flex lg1>-->
<!--                        <br/>-->
<!--                        <v-tooltip bottom>-->
<!--                            <template v-slot:activator="{ on }">-->
<!--                                <span v-on="on">-->
<!--                                    <v-icon color="primary" large v-on:click="logOut">logout</v-icon>-->
<!--                                </span>-->
<!--                            </template>-->
<!--                            <span>Выход</span>-->
<!--                        </v-tooltip>-->
<!--                    </v-flex>-->
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
            pages: [{
                name:"Home",
                title:"Home"},
                {
                name:"taskCreator",
                title:"Create task Mapping"
                }
            ],
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
    .routerlink{
        color: white;
        text-decoration: none;
    }
    .right-part {
        padding-right: 30px;
    }
</style>