<template>
    <div>
        <!-- <v-card> -->
        <v-divider></v-divider>
        <v-card-title>
            <span class="headline">SObject rule creator</span>
        </v-card-title>
        <v-card-text>
            <v-container grid-list-md>
                <v-layout wrap>
                    <v-flex xs12 sm6>
                        <v-text-field label="Object name" v-model="sObjectRule.name" required></v-text-field>
                    </v-flex>
                    <v-flex xs12 sm6>
                        <v-text-field label="Label object" v-model="sObjectRule.label"></v-text-field>
                    </v-flex>

                    <v-flex xs6 class="headline font-weight-medium">Fields</v-flex>
                    <v-flex xs6>
                        <!--  -->
                        <!--  -->

                        <cmpMapa
                            @sendRule="addToFieldsRule"
                            ref="fields"
                            v-bind:nameObj="'for Field'"
                            v-bind:mode="'new'"
                            v-bind:buttonCreate="'Create rule for Field'"
                            v-bind:selectList="['label','required','type','precision','scale','length']"
                        />
                    </v-flex>
                    <v-divider></v-divider>
                    <v-flex xs12>
                        <v-list>
                            <v-list-group
                                sub-group
                                v-for="(map, index) in sObjectRule.fieldsRule"
                                :key="map.name"
                                :prepend-icon="map.action"
                                no-action
                            >
                                <template v-slot:activator>
                                    <v-list-tile>
                                        <v-list-tile-content>
                                            <v-list-tile-title>
                                                <span
                                                    class="font-weight-light body-1"
                                                >Rules for fieldName:</span>
                                                {{ map.name }}
                                            </v-list-tile-title>
                                        </v-list-tile-content>
                                    </v-list-tile>
                                    <!-- <BUTTON> -->
                                    <v-btn
                                        fab
                                        icon
                                        float
                                        pre
                                        color="white"
                                        flat
                                        value="favorites"
                                        @click="editField(map)"
                                    >
                                        <v-icon>edit</v-icon>
                                    </v-btn>
                                    <v-btn
                                        fab
                                        icon
                                        float
                                        color="white"
                                        flat
                                        value="favorites"
                                        @click="removeField(index)"
                                    >
                                        <v-icon>delete</v-icon>
                                    </v-btn>
                                </template>
                                <v-list-tile v-for="subItem in map.keyValue" :key="subItem.name">
                                    <v-list-tile-content>
                                        <v-list-tile-title>
                                            <span class="font-weight-light body-1">For tagName: </span>
                                            <span
                                                class="text-uppercase blue--text"
                                            >{{ subItem.key }}</span>
                                            <span class="font-weight-light body-1"> value must be: </span>
                                            <span
                                                class="text-uppercase red--text"
                                            >{{ subItem.value }}</span>
                                        </v-list-tile-title>
                                    </v-list-tile-content>
                                </v-list-tile>
                            </v-list-group>
                        </v-list>
                    </v-flex>
                </v-layout>
            </v-container>

            <v-container grid-list-md>
                <v-layout wrap>
                    <v-flex xs6 class="headline font-weight-medium">Validations Rule</v-flex>
                    <v-flex xs6>
                        <!--  -->
                        <!--  -->

                        <cmpMapa
                            @sendRule="addToValidationRule"
                            ref="validation"
                            v-bind:nameObj="'for Validation Rule'"
                            v-bind:mode="'new'"
                            v-bind:buttonCreate="'Create rule for Validation'"
                            v-bind:selectList="['active','errorConditionFormula','errorMessage']"
                        />
                    </v-flex>
                    <v-divider></v-divider>
                    <v-flex xs12>
                        <v-list>
                            <v-list-group
                                sub-group
                                v-for="(map, index) in sObjectRule.validationRule"
                                :key="map.name"
                                :prepend-icon="map.action"
                                no-action
                            >
                                <template v-slot:activator>
                                    <v-list-tile>
                                        <v-list-tile-content>
                                            <v-list-tile-title>
                                                <span
                                                    class="font-weight-light body-1"
                                                >Rules for Validation Name:</span>
                                                {{ map.name }}
                                            </v-list-tile-title>
                                        </v-list-tile-content>
                                    </v-list-tile>
                                    <v-btn
                                        fab
                                        icon
                                        float
                                        pre
                                        color="white"
                                        flat
                                        value="favorites"
                                        @click="editValidation(map)"
                                    >
                                        <v-icon>edit</v-icon>
                                    </v-btn>
                                    <v-btn
                                        fab
                                        icon
                                        float
                                        color="white"
                                        flat
                                        value="favorites"
                                        @click="removeValidation(index)"
                                    >
                                        <v-icon>delete</v-icon>
                                    </v-btn>
                                </template>

                                <v-list-tile v-for="subItem in map.keyValue" :key="subItem.name">
                                    <v-list-tile-content>
                                        <v-list-tile-title>
                                            <span class="font-weight-light body-1"> In tagName: </span>
                                            <span
                                                class="text-uppercase blue--text"
                                            >{{ subItem.key }}</span>
                                            <span class="font-weight-light body-1"> search string: </span>
                                            <span
                                                class="text-uppercase red--text"
                                            >{{ subItem.value }}</span>
                                        </v-list-tile-title>
                                    </v-list-tile-content>
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
        <!-- </v-card> -->
    </div>
</template>

<script>
import cmpMapa from "./cmpMapa";
export default {
    components: {
        cmpMapa
    },
    name: "AlertComponent",
    data: () => ({
        sObjectRule: {
            name: "",
            label: "",
            fieldsRule: [],
            validationRule: []
        },
        modeEditTask: "new"
    }),
    mounted() {
        //  this.$root.$on("sendData", keyValue => {
        //     this.sObjectRule.fieldsRule.push(keyValue);
        //   });
    },

    methods: {
        editField: function(map) {
            this.$refs.fields.dialog = true;
            this.$refs.fields.mode = "save";
            this.$refs.fields.map = map;
        },
        editValidation: function(map) {
            this.$refs.validation.dialog = true;
            this.$refs.validation.mode = "save";
            this.$refs.validation.map = map;
        },
        removeField: function(ind) {
            this.sObjectRule.fieldsRule.splice(ind, 1);
        },
        removeValidation: function(ind) {
            this.sObjectRule.validationRule.splice(ind, 1);
        },
        addToFieldsRule(keyValue) {
            this.sObjectRule.fieldsRule.push(keyValue);
        },
        addToValidationRule(keyValue) {
            this.sObjectRule.validationRule.push(keyValue);
        },
        emitCloseRule() {
            this.$root.$emit("closeRule");
        },
        emitSaveRule() {
            console.log("emit sObjectRule");
            if(this.modeEditTask == "new") {
                this.$root.$emit("addRule", "sObjectRule", this.sObjectRule);
            } else {
                 this.$root.$emit("closeRule");
            }
        }
    }
};
</script>

<style scoped>
</style>