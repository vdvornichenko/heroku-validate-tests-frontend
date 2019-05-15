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
<!--        ></v-navigation-drawer>-->
        <v-toolbar :clipped-left="primaryDrawer.clipped" app absolute fixed>
<!--            <v-toolbar-side-icon-->
<!--                    v-if="primaryDrawer.type !== 'permanent'"-->
<!--                    @click.stop="primaryDrawer.model = !primaryDrawer.model"-->
<!--            ></v-toolbar-side-icon>-->
            <v-toolbar-title>Task Checker</v-toolbar-title>
            <v-spacer></v-spacer>
            <div style="float: right">
                <br/>
                <v-flex xs12 md6>
                    <v-switch primary label="Dark" v-on:change="chooseTheme"></v-switch>
                </v-flex>
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
            showDrawer: false
        }),

        methods: {
            chooseTheme: function () {
                this.$emit('changeTheme');
            }
        }
    }
</script>

<style scoped>

</style>