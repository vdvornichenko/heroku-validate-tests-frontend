<template>

    <v-layout row justify-center>
        <input type="hidden" v-model="showForm">
        <v-dialog v-model="dialog" max-width="500" transition="dialog-bottom-transition" persistent>
            <v-card style="overflow-x: auto">
                <v-toolbar dark color="primary">
                    <v-toolbar-title>Войти</v-toolbar-title>
                </v-toolbar>
                <AlertComponent/>
                <v-alert
                        v-model="showAlert"
                        dismissible
                        type="error"
                >
                    {{ message }}
                </v-alert>
                <div v-on:keyup.13="setCursor">
                    <v-container>
                        <v-form ref="loginForm" >
                            <span>Логин</span>
                            <br>
                            <v-text-field
                                    id="login"
                                    v-model="userName" label="Введите логин"
                                    prepend-icon="person"
                                    :rules="loginRules"
                            />
                            <span>Пароль</span>
                            <br>
                            <v-text-field
                                    id="password"
                                    label="Введите пароль" v-model="password"
                                    :append-icon="showPassword ? 'visibility' : 'visibility_off'"
                                    :type="showPassword ? 'text' : 'password'"
                                    @click:append="showPassword = !showPassword"
                                    required
                                    prepend-icon="lock"
                                    :rules="passwordRules"
                            />
                            <div style="text-align: center">
                                <v-btn v-on:click="login" color="primary">Войти</v-btn>
                            </div>
                        </v-form>
                    </v-container>
                </div>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    import { HTTP_AUTHORIZATION_URL } from "../Constants";
    import { AUTH_TOKEN } from "../Constants";
    import { USER_SESSION } from "../Constants";
    import AlertComponent from './AlertComponent'

    export default {
        name: "LoginComponent",
        components: {
            AlertComponent
        },

        data() {
            return {
                userName: "",
                password: "",
                showAlert: false,
                message: "",
                dialog: this.showThis,
                showPassword: false,
                passwordRules: [
                    p => !(p.length < 8) || 'Пароль должен содержать от 8 символов',
                ],

                loginRules: [
                    l => !(l.length < 2) || 'Имя пользователя должно содержать хотя бы 2 символа'
                ]
            }

        },

        props: {
          showThis: Boolean
        },

        computed: {
            showForm : function () {
                this.setFormState(this.showThis);
                return this.showThis;
            }
        },

        mounted() {
            this.$root.$on('checkLogin', response => {
                let userInfo = response.body;
                if (userInfo) {
                    this.$cookies.set(AUTH_TOKEN, userInfo.authorizationToken, 60*60*24*14);
                    this.$cookies.set(USER_SESSION, userInfo.userName, 60*60*24*14);
                    this.$root.$emit('openMainPage');
                    this.dialog = false;
                    this.password = '';
                    this.userName = '';
                } else {
                    this.message = 'Неправильный логин или пароль';
                    this.showAlert = true;
                }
            });

            this.$root.$on('openLoginForm', () => {
                this.dialog = true;
            });
        },

        methods: {
            login: function () {
                if (this.$refs.loginForm.validate()) {
                    this.$root.$emit(
                        'runCallback',
                        this.$http.post(HTTP_AUTHORIZATION_URL, this.userName + ';' + this.password),
                        'checkLogin'
                    );
                }
            },

            setFormState: function(state) {
                this.dialog = state;
            },

            setCursor: function () {
                let onFocusElements = document.querySelectorAll(':focus');
                if (onFocusElements.length > 0) {
                    if (onFocusElements[0].id === 'login') {
                        document.getElementById('password').focus();
                    } else if (onFocusElements[0].id === 'password') {
                        this.login();
                    }
                } else {
                    this.login();
                }
            }
        }
    }
</script>

<style scoped>

</style>