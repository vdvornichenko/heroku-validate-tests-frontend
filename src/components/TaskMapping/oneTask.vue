<template>
    <div>
        <v-card class="mx-auto">
            <v-card-title class="title font-weight-regular justify-space-between"></v-card-title>

            <v-window v-model="step">
                <v-window-item :value="1">
                    <template>
                        <v-toolbar dark flat color="secondary">
               
                            <v-toolbar-title>Task creator</v-toolbar-title>
                            <v-spacer></v-spacer>
           
							
			
           
                                <v-menu bottom right>
                                    <template v-slot:activator="{ on }">
                                        <v-btn right color="primary" dark v-on="on">add rule</v-btn>
                                    </template>
                                    <v-list>
                                        <v-list-tile
                                            v-for="(item, i) in items"
                                            :key="i"
                                            @click="createRule(item)"
                                        >
                                            <v-list-tile-title>{{ item }}</v-list-tile-title>
                                        </v-list-tile>
                                    </v-list>
                                </v-menu>
                    
                        </v-toolbar>
                        <!-- <v-layout align-center justify-center row fill-height>
                        <v-flex xs8>
	                        <v-text-field label="name Field" required v-model="Task.nameTask"></v-text-field>
                        </v-flex>
                        </v-layout> -->
                    </template>

                    <v-card-text>
                        <v-flex xs12>
                            <v-list>
                                <v-list-group
                                    no-action
                                    sub-group
                                    value="true"
                                    v-for="(value, name, ind) in Task"
                                    :key="ind"
                                    v-if="value.length > 0"
                                >
                                    <template v-slot:activator>
                                        <v-list-tile>
                                            <v-list-tile-title>
                                                <span class="font-weight-light body-1">Metadata:  </span>
                                                <span class="blue--text">{{ name.slice(0,-5) }}</span>
                                            </v-list-tile-title>
                                        </v-list-tile>
                                    </template>

                                    <v-list-tile v-for="(item, index) in value" :key="index">
                                        <v-list-tile-title>
                                            <span class="font-weight-light body-1">name:  </span>
                                            <span class="blue--text">{{ item.name }}</span>
                                        </v-list-tile-title>

                                        <v-btn
                                            fab
                                            icon
                                            float
                                            pre
                                            color="white"
                                            flat
                                            small
                                            @click="editRule(item, name, index)"
                                        >
                                            <v-icon>edit</v-icon>
                                        </v-btn>
                                        <v-btn
                                            fab
                                            icon
                                            float
                                            color="white"
                                            flat
                                            small
                                            @click="removeRule(name, index)"
                                        >
                                            <v-icon>delete</v-icon>
                                        </v-btn>
                                    </v-list-tile>
                                </v-list-group>
                            </v-list>

                            <v-layout align-end justify-end>
                                <v-btn color="primary" dark @click="cancel">Cancel</v-btn>
                                <v-btn color="primary" dark @click="createTask">SAVE TASK</v-btn>
                            </v-layout>
                        </v-flex>
                    </v-card-text>
                </v-window-item>

                <v-window-item :value="2">
                    <component :is="component" v-if="component" ref="cmp"/>
                </v-window-item>
            </v-window>
        </v-card>
    </div>
</template>
<script>
import sObject from "./sObject";
import ApexPage from "./ApexPage";
import ApexClass from "./ApexClass";
import Trigger from "./Trigger";
import Test from "./Test";
export default {
    components: {
        sObject,
        ApexPage,
        ApexClass,
        Trigger,
        Test
    },
    name: "container",
    data: () => ({
        step: 1,
        Task: {
            nameTask: "",
            sObjectTasks: [],
            apexPageTasks: [],
            apexClassTasks: [],
            triggerTasks: [],
            testTasks: []
        },
        items: [
            "sObject rule",
            "apex Page rule",
            "apex Class rule",
            "trigger rule",
            "test rule"
        ],
        component: null
    }),
    mounted() {
        this.$root.$on("addRule", (metaName, rule) => {
            console.log("emit OneTAsk " + metaName);
            this.step++;
            if (metaName == "sObjectRule") {
                this.Task.sObjectTasks.push(rule);
            } else if (metaName == "ApexPageRule") {
                this.Task.apexPageTasks.push(rule);
            } else if (metaName == "ApexClassRule") {
                this.Task.apexClassTasks.push(rule);
            } else if (metaName == "TriggerRule") {
                this.Task.triggerTasks.push(rule);
            } else if (metaName == "TestRule") {
                this.Task.testTasks.push(rule);
            }
            this.component = null;
            // var that = this;
            // setTimeout(function() {
            //     that.component = null;
            // }, 700);
        });
        this.$root.$on("closeRule", () => {
            console.log("emit close ");
            this.step++;
            var that = this;
            setTimeout(function() {
                that.component = null;
            }, 700);
        });
    },
    methods: {
        createRule: function(e) {
            this.step++;
            console.log(e);
            var that = this;
            setTimeout(function() {
                if (e == "sObject rule") {
                    that.component = "sObject";
                } else if (e == "apex Class rule") {
                    that.component = "ApexClass";
                } else if (e == "apex Page rule") {
                    that.component = "ApexPage";
                } else if (e == "trigger rule") {
                    that.component = "Trigger";
                } else if (e == "test rule") {
                    that.component = "Test";
                }
            }, 270);
        },
        removeRule: function(name, index) {
            this.Task[name].splice(index, 1);
        },
        editRule: function(rule, name, index) {
            // CREATE METHOD
            console.log(name);
            console.log(rule);
            this.step++;
            var that = this;
            if (name == "sObjectTasks") {
                this.component = "sObject";
                setTimeout(function() {
                    that.$refs.cmp.sObjectRule = rule;
                    that.$refs.cmp.modeEditTask = "edit";
                }, 200);
            } else if (name == "apexClassTasks") {
                this.component = "ApexClass";
                setTimeout(function() {
                    that.$refs.cmp.ApexClass = rule;
                    that.$refs.cmp.modeEditTask = "edit";
                }, 200);
            } else if (name == "apexPageTasks") {
                this.component = "ApexPage";
                setTimeout(function() {
                    that.$refs.cmp.ApexPage = rule;
                    that.$refs.cmp.modeEditTask = "edit";
                }, 200);
            } else if (name == "triggerTasks") {
                this.component = "Trigger";
                setTimeout(function() {
                    that.$refs.cmp.Trigger = rule;
                    that.$refs.cmp.modeEditTask = "edit";
                }, 200);
            } else if (name == "testTasks") {
                this.component = "Test";
                setTimeout(function() {
                    that.$refs.cmp.Test = rule;
                    that.$refs.cmp.modeEditTask = "edit";
                }, 200);
            }
        },
        createTask: function() {
            console.log(this.Task);
            this.$root.$emit("createTask", this.Task);
            this.Task = {
                sObjectTasks: [],
                apexPageTasks: [],
                apexClassTasks: [],
                triggerTasks: [],
                testTasks: []
            };
        },
        cancel: function() {
            this.$root.$emit("cancelTask");
            this.Task = {
                sObjectTasks: [],
                apexPageTasks: [],
                apexClassTasks: [],
                triggerTasks: [],
                testTasks: []
            };
        }
    }
};
</script>

<style scoped>
</style>

