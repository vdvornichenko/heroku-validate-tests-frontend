<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <v-card>
      <v-card-title>
        <span class="headline">ApexPage rule creator</span>
      </v-card-title>
      <v-card-text>
        <v-container grid-list-md>
          <v-layout wrap>
            <v-flex xs12 sm6>
              <v-text-field v-model="ApexPage.name" 
                 label="ApexPage name"
                :error="($v.ApexPage.name.$dirty && !$v.ApexPage.name.required)"
                :error-messages="ApexPageErrors"
                @change="$v.ApexPage.name.$touch()"
                 ></v-text-field>
            </v-flex>

            <v-flex xs12 sm6>
              <div class="text-xs-center">
                <v-dialog v-model="dialog" persistent max-width="600px" light>
                  <template v-slot:activator="{ on }">
                    <v-btn color="primary" dark @click="AddNewRule()">Add rule</v-btn>
                  </template>

                  <v-card>
                    <v-card-title>
                      <span class="headline">Create rule search value in tag</span>
                    </v-card-title>
                    <v-card-text>
                      <v-container grid-list-md>
                        <v-layout wrap>
                          <v-flex xs12>
                            <v-combobox light
                              v-model="newRule.nameTag"
                              :items="['apex:page','button','table']"
                              label="Select a tag or enter another to search for values"
                            ></v-combobox>
                          </v-flex>

                          <v-flex xs12>
                            <v-combobox light
                              ref="inp"
                              v-model="newRule.searchStrings"
                              label="Add value and press enter"
                              multiple
                              persistent-hint
                              small-chips
                            >
                              <template v-slot:selection="{ item, parent, selected }">
                                <v-chip label small>
                                  <span class="pr-2">{{ item }}</span>
                                  <v-icon small @click="parent.selectItem(item)">close</v-icon>
                                </v-chip>
                              </template>
                            </v-combobox>
                          </v-flex>
                        </v-layout>
                      </v-container>
                    </v-card-text>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="blue darken-1" flat @click="closeDialog">Close</v-btn>
                      <v-btn color="blue darken-1" flat @click="saveDialog">Save</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </div>
            </v-flex>

            <v-flex xs12>
              <v-list>
                <v-list-group
                  no-action
                  sub-group
                  value="true"
                  v-for="(rule, index) in ApexPage.rules"
                  :key="rule.nameTag"
                >
                  <template v-slot:activator>
                    <v-list-tile>
                      <v-list-tile-title>
                        <span class="font-weight-light body-1">In tagName:  </span>
                        <span class="text-uppercase blue--text">{{ rule.nameTag }}</span>
                        <span class="font-weight-light body-1"> search strings:</span>
                      </v-list-tile-title>
                    </v-list-tile>
                    <v-btn fab icon float pre color="white" flat value="favorites" @click="edit(rule)">
                      <v-icon>edit</v-icon>
                    </v-btn>
                    <v-btn fab icon float color="white" flat value="favorites" @click="remove(index)">
                      <v-icon>delete</v-icon>
                    </v-btn>
                  </template>

                  <v-list-tile v-for="str in rule.searchStrings" :key="str">
                    <v-list-tile-title v-text="str"></v-list-tile-title>
                  </v-list-tile>
                </v-list-group>
              </v-list>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" flat @click="emitCloseRule">Close</v-btn>
        <v-btn color="blue darken-1" flat @click="emitSaveRule">Save</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>
<script>
import { required } from "vuelidate/lib/validators";
export default {
  name: "ApexPageComponent",

  data: () => ({
    dialog: false,
    mode: "new",
    modeEditTask: "new",
    ApexPage: {
      name: "",
      rules: []
    },
    newRule: {
      nameTag: "",
      searchStrings: []
    }
  }),
  computed: {
    ApexPageErrors () {
     const errors = []
      if (this.$v.ApexPage.name.$dirty && !this.$v.ApexPage.name.required) {
        errors.push('This field is required');
      }
      return errors
    },
  },
  validations: {
    ApexPage: {
      name: {
        required
      }
    }
  },
  methods: {
    closeDialog: function() {
      this.dialog = false;
    },
    remove: function(ind) {
      this.ApexPage.rules.splice(ind, 1);
    },
    edit: function(rule) {
      this.dialog = true;
      this.mode = "edit";
      this.newRule = rule;
    },
    AddNewRule: function() {
      this.dialog = true;
      this.mode = "new";
      this.newRule = {
        nameTag: "",
        searchStrings: []
      };
    },
    saveDialog: function() {
      if (this.mode == "new") {
        this.ApexPage.rules.push(this.newRule);
      }
      this.newRule = {
        nameTag: "",
        searchStrings: []
      };
      this.dialog = false;
    },
    emitCloseRule() {
      this.$root.$emit("closeRule");
    },
    emitSaveRule() {
      if (this.$v.$invalid) {
        this.$v.$touch();
        return;
      }
      if (this.modeEditTask == "new") {
        this.$root.$emit("addRule", "ApexPageRule", this.ApexPage);
      } else {
        this.$root.$emit("closeRule");
      }
    }
  }
};
</script>

<style scoped>
</style>
