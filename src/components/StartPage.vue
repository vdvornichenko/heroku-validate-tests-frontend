<template>
  <v-app id="sandbox" :dark="dark">
    <div v-if="showTable">
      <fixed-header :threshold="100">
        <div class="navbar" style="z-index: 20">
          <Header @changeTheme="dark=!dark" @logout="logout"/>
        </div>
      </fixed-header>
      <v-layout row wrap>
        <v-flex lg2>
          <div style="padding-top:50px; position:sticky; top:0">
            <LeftSidebar/>
          </div>
        </v-flex>
        <v-flex lg8>
          <v-card class="content-card">
          <v-container>
              <div class="content-card-container">
                <AlertComponent />
                <UsersTable :showTable="showTable"/>
                <Results ref="results"/>
                <FilesCmp />
              </div>
          </v-container>
          </v-card>
        </v-flex>
        <v-flex lg2/>
      </v-layout>
    </div>
    <CallbackSpinner/>
    <login-component :showThis="showLoginForm"/>
    <div id="footer"></div>
  </v-app>
</template>

<script>
  import UsersTable from './UsersTable'
  import Results from './Results'
  import Header from './Header'
  import AlertComponent from './AlertComponent'
  import CallbackSpinner from './CallbackSpinner'
  import FilesCmp from './FilesComponent'
  import FixedHeader from 'vue-fixed-header'
  import LeftSidebar from './LeftSidebar'
  import LoginComponent from './LoginComponent'
  import { AUTH_TOKEN } from "../Constants";
  import { HTTP_CHECK_SESSION_URL } from "../Constants";
  import { USER_SESSION } from "../Constants";

  export default {
    components: {
      UsersTable,
      Results,
      Header,
      AlertComponent,
      CallbackSpinner,
      FilesCmp,
      FixedHeader,
      LeftSidebar,
      LoginComponent
    },
    data: () => ({
      ecosystem: [
        {
          text: 'vuetify-loader',
          href: 'https://github.com/vuetifyjs/vuetify-loader'
        },
        {
          text: 'github',
          href: 'https://github.com/vuetifyjs/vuetify'
        },
        {
          text: 'awesome-vuetify',
          href: 'https://github.com/vuetifyjs/awesome-vuetify'
        }
      ],
      importantLinks: [
        {
          text: 'Documentation',
          href: 'https://vuetifyjs.com'
        },
        {
          text: 'Chat',
          href: 'https://community.vuetifyjs.com'
        },
        {
          text: 'Made with Vuetify',
          href: 'https://madewithvuetifyjs.com'
        },
        {
          text: 'Twitter',
          href: 'https://twitter.com/vuetifyjs'
        },
        {
          text: 'Articles',
          href: 'https://medium.com/vuetify'
        }
      ],
      whatsNext: [
        {
          text: 'Explore components',
          href: 'https://vuetifyjs.com/components/api-explorer'
        },
        {
          text: 'Select a layout',
          href: 'https://vuetifyjs.com/layout/pre-defined'
        },
        {
          text: 'Frequently Asked Questions',
          href: 'https://vuetifyjs.com/getting-started/frequently-asked-questions'
        }

      ],
      dark: true,
      showLoginForm: false,
      showTable : false
    }),

    created() {
      this.showTable = true;
      // if (!this.$cookies.isKey(AUTH_TOKEN)) {
      //   this.showLoginForm = true;
      // } else {
      //   this.$root.$emit('setState', true);
      //   this.$http.post(
      //           HTTP_CHECK_SESSION_URL,
      //           this.$cookies.get(USER_SESSION) + ';' + this.$cookies.get(AUTH_TOKEN)
      //   ).then(response => {
      //     if (response.body) {
      //       this.showTable = true;
      //     } else {
      //       this.showLoginForm = true;
      //     }
      //     this.$root.$emit('setState', false);
      //   }, () => {
      //     this.showLoginForm = true;
      //     this.$root.$emit('setState', false);
      //   });
      //
      // }
    },

    mounted() {
      this.$root.$on('openMainPage', () => {
        this.showTable = true;
      });

      this.$root.$on('getUserResults', ()=> {
        this.$vuetify.goTo('#resultTables');
      });
    },

    methods: {
      logout: function () {
        this.showLoginForm = true;
        this.showTable = false;
        this.$cookies.remove('currentGroups');
        this.$cookies.remove(AUTH_TOKEN);
        this.$cookies.remove(USER_SESSION);

      }
    }
  }
</script>

<style>

  .content-card {
    height: 100%;
    padding-top: 40px;
  }

  .content-card-container {
    min-height: 100%;
    padding-top: 50px;
    position: relative
  }

  .navbar.vue-fixed-header--isFixed {
    position: fixed;
    left: 0;
    top: 0;
    width: 100vw;
  }
</style>
