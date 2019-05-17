<template>

    <v-layout row justify-center>
        <v-dialog v-model="dialog" max-width="1200" transition="dialog-bottom-transition" persistent>
            <v-card style="overflow-x: auto">
                <v-toolbar dark color="primary">
                    <v-toolbar-title>Войти</v-toolbar-title>
                </v-toolbar>
                <v-alert
                        v-model="showAlert"
                        dismissible
                        type="error"
                >
                    {{ message }}
                </v-alert>
                <v-subheader >Заполните поля с логином и паролем</v-subheader>
                <div>
                    <span>Логин</span>
                    <br>
                    <input type="text" v-model="userName" placeholder="Введите логин"><br/>
                    <span>Пароль</span>
                    <br>
                    <input type="text" v-model="password" placeholder="Введите пароль"><br/>
                    <v-btn v-on:click="login">Войти</v-btn>
                </div>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    import {HTTP_AUTHORIZATION_URL} from "../Constants";
    import { AUTH_TOKEN } from "../Constants";

    export default {
        name: "LoginComponent",
        data() {
            return {
                userName: "",
                password: "",
                showAlert: false,
                message: "",
                dialog: this.showThis
            }

        },

        props: {
          showThis: Boolean
        },

        mounted() {
            this.$root.$on('checkLogin', response => {
                let authToken = response.bodyText;
                if (authToken) {
                    this.$cookies.set(AUTH_TOKEN, authToken);
                    this.$root.$emit('openMainPage');
                    this.dialog = false;
                } else {
                    this.message = 'Неправильный логин или пароль';
                    this.showAlert = true;
                }
            });
        },

        methods: {
            login: function () {
                this.$root.$emit(
                    'runCallback',
                    this.$http.post(HTTP_AUTHORIZATION_URL, this.userName + ';' + this.password),
                    'checkLogin'
                );
            }
        }
    }
</script>

<style scoped>

</style>