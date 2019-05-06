<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <v-app id="sandbox" :dark="dark">
    <v-navigation-drawer
            v-model="primaryDrawer.model"
            :permanent="primaryDrawer.type === 'permanent'"
            :temporary="primaryDrawer.type === 'temporary'"
            :clipped="primaryDrawer.clipped"
            :floating="primaryDrawer.floating"
            :mini-variant="primaryDrawer.mini"
            absolute
            overflow
            app
    ></v-navigation-drawer>
    <v-toolbar :clipped-left="primaryDrawer.clipped" app absolute>
      <v-toolbar-side-icon
              v-if="primaryDrawer.type !== 'permanent'"
              @click.stop="primaryDrawer.model = !primaryDrawer.model"
      ></v-toolbar-side-icon>
      <v-toolbar-title>Task Checker</v-toolbar-title>
      <div style="float: right">
        <br/>
        <v-flex xs12 md6>
          <v-switch v-model="dark" primary label="Dark"></v-switch>
        </v-flex>
      </div>
    </v-toolbar>
    <v-container>
      <div style="height: 100%; padding-top: 50px">
        <div>
          <v-alert
                  v-model="errorAlert"
                  dismissible
                  type="error"
          >
            {{ errorMessage }}
          </v-alert>
        </div>
        <div class="text-xs-center">
          <v-dialog
                  v-model="callbackState"
                  hide-overlay
                  persistent
                  width="300"
          >
            <v-card
                    color="primary"
                    dark
            >
              <v-card-text>
                Please stand by
                <v-progress-linear
                        indeterminate
                        color="white"
                        class="mb-0"
                ></v-progress-linear>
              </v-card-text>
            </v-card>
          </v-dialog>
        </div>
        <v-data-table :dark="dark" disable-initial-sort
                      :headers="usersTableHeaders"
                      :items="users"
                      class="elevation-1"
        >
          <template v-slot:items="props" >
            <td>{{ props.item.index + 1 }}</td>
            <td><input type="checkbox" v-model="props.item.checked"/></td>
            <td class="text-xs-left">{{ props.item.userName }}</td>
            <td class="text-xs-left">{{ props.item.password }}</td>
          </template>
        </v-data-table>
        <div style="float: right;">
          <v-btn color="info" v-on:click="getAllUsersInfo">Show Results for all users</v-btn>
          <v-btn color="info" v-on:click="getSelectedUsersInfo">Show Results for selected users</v-btn>
        </div>
        <div v-if="showResults" style="padding-top: 30px">
          <div v-for="(value, propertyName, index) in userResults" v-bind:key="index" style="padding-top: 30px">
            <v-toolbar flat :dark="dark">
              <v-toolbar-title>{{ propertyName }}</v-toolbar-title>
            </v-toolbar>
            <v-data-table :dark="dark"
                          :headers="userResultsHeaders"
                          :items="value"
                          class="elevation-1"
            >
              <template v-slot:items="props">
                <td>{{ props.item.nameMetadata }}</td>
                <td class="text-xs-left">{{ props.item.status }}</td>
                <td class="text-xs-left">{{ props.item.message }}</td>
              </template>
            </v-data-table>
          </div>
        </div>
      </div>
    </v-container>
    <v-footer :inset="footer.inset" app>
      <span class="px-3">&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>

</template>

<script>
  export default {
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
      users : [],
      usersTableHeaders : [
        {text : "#", value : "index", sortable: false, align: "left"},
        {text : "Select User", value : "checked", sortable : false},
        {text : "User Name", value : "userName"},
        {text : "Password", value : "password"}
      ],
      showResults : false,
      userResults : [],
      userResultsHeaders : [
        {text : "Metadata File", value : "nameMetadata"},
        {text : "Status", value : "status"},
        {text : "Message", value : "message"}
      ],
      callbackState: false,
      errorAlert : false,
      errorMessage : "",
      dark: true,
      drawers: ['Default (no property)', 'Permanent', 'Temporary'],
      primaryDrawer: {
        model: null,
        type: 'Default (no property)',
        clipped: true,
        floating: true,
        mini: false
      },
      footer: {
        inset: false
      }
    }),

    created() {
      this.$http.get('http://localhost:8080/getUsers').then(response => {
        response.body.forEach((elem, i) => {
          this.users.push({index : i, userName : elem.userName, password : elem.password, checked: false});
        });
      });
    },

    methods: {
      getAllUsersInfo : function() {
        this.getResults("all");
      },

      getSelectedUsersInfo : function() {
        let users = [];
        this.users.forEach((elem) => {
          if (elem.checked) {
            users.push(elem.userName);
          }
        });
        this.getResults(users.join(";"));
      },

      getResults : function(users) {
        if (users === "") {
          this.errorAlert = true;
          this.errorMessage = "You must select at least 1 user"
        } else {
          this.callbackState = true;
          this.$http.post('http://localhost:8080/usersInfo', users).then(response => {
            this.userResults = response.body;
            this.showResults = true;
            this.callbackState = false;
          }, response => {
            this.errorMessage = response.body.bodyText.error;
            this.errorAlert = true;
            this.callbackState = false;
          });
        }
      }
    }
  }
</script>

<style>

</style>
