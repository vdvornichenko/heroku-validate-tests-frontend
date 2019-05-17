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
            <v-toolbar-title>Task Checker</v-toolbar-title>
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

    .right-part {
        float: right;
        padding-right: 30px;
    }
</style>