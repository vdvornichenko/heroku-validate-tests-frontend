<template>
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

            <v-list>
              <v-list-tile
                v-for="(item, i) in pages"
                :key="i"
              >
                     <v-list-tile-title dark >
                       
                        <router-link :to="item" style="text-decoration: none; color:black">
                            {{ item.title }}
                        </router-link>
                    </v-list-tile-title>
       
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
                name:"",
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
        float: right;
        padding-right: 30px;
    }
</style>