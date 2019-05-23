<template >
	<v-app id="taskMp" dark>
		<Header @changeTheme="!!dark"/>
    <v-snackbar
      v-model="snackbar.snackbar"
      :color="snackbar.color"
      multi-line
      top
      :timeout="snackbar.timeout"
    >
       SUCCESS SAVE
      <v-btn
        dark
        flat
        @click="snackbar = false"
      >
        Close
      </v-btn>
    </v-snackbar>
		<v-layout row wrap>
			<v-flex lg2>
				<div style="padding-top:50px; position:sticky; top:0"></div>
			</v-flex>
			<v-flex lg8>
				<v-card class="content-card">
					<v-container>
						<div class="content-card-container">
							<v-card class="mx-auto" :dark="dark">
								<v-window v-model="step">
									<v-window-item :value="0">
										<template>
											<v-toolbar flat color="secondary">
												<v-toolbar-title>TASK MAPPING</v-toolbar-title>

												<v-spacer></v-spacer>
												<v-btn color="primary" @click="createTask">CREATE TASK</v-btn>
											</v-toolbar>
										</template>
										<v-card-text>
											<v-flex xs12>
												<v-flex xs12>
													<v-expansion-panel>
														<v-expansion-panel-content v-for="(tasksArr, index) in Tasks" :key="index" draggable>
															<template v-slot:actions>
																<v-btn fab icon float pre color="white" flat small @click="editTask(tasksArr)">
																	<v-icon>edit</v-icon>
																</v-btn>
																<v-btn fab icon float color="white" flat small @click="removeTask(index)">
																	<v-icon>delete</v-icon>
																</v-btn>
															</template>
															<template v-slot:header>
																<span class="font-weight-light body-1">Task Number: {{ index + 1}}</span>
															</template>
															<v-card>
																<span class="font-weight-light body-1"></span>

																<v-card>
																	<v-card-title
																		primary-title
																		v-for="(tasksMeta, name, subIndex) in tasksArr"
																		:key="subIndex"
																		v-if="tasksMeta.length > 0"
																	>
																		<div>
																			<span class>{{ name.slice(0,-5) }} :</span>
																		</div>

																		<v-card-text v-for="(task, indTask) in tasksMeta" :key="indTask">
																			<span class="font-weight-light body-1">
																				Name:
																				<span class="blue--text">{{task.name}}</span>
																			</span>
																		</v-card-text>
																	</v-card-title>
																</v-card>
															</v-card>
														</v-expansion-panel-content>
													</v-expansion-panel>
												</v-flex>

												<v-layout align-end justify-end>
													<v-layout align-end justify-end>
														<v-btn color="primary" dark @click="saveTasks" >SAVE TASKS</v-btn>
													</v-layout>
												</v-layout>
											</v-flex>
										</v-card-text>
									</v-window-item>

									<v-window-item :value="1">
										<component :is="component" v-if="component" ref="task"/>
									</v-window-item>
								</v-window>
							</v-card>
						</div>
					</v-container>
				</v-card>
			</v-flex>
			<v-flex lg2/>
		</v-layout>
	</v-app>
</template>
<script>
import oneTask from "./oneTask";
import Header from "../Header";
export default {
  components: {
    oneTask,
    Header
  },
  name: "container",
  data: () => ({
    snackbar:{
      snackbar: false,
      timeout: 3000,
      color: "success",
    },
    dark: true,
    step: 0,
    mode: "new",
    Tasks: [],
    newTask: {
      sObjectTasks: [],
      apexClassTasks: [],
      apexPageTasks: [],
      triggerTasks: [],
      testTasks: []
    },
    component: null
  }),
  created() {
    this.getTaskMapping();
  },
  mounted() {
    this.$root.$on("createTask", task => {
      this.step++;
      if (this.mode == "new") {
        this.Tasks.push(task);
      }
      var that = this;
      setTimeout(function() {
        that.component = null;
        that.mode = "new";
      }, 500);
    });
    this.$root.$on("cancelTask", () => {
      this.step++;
      this.mode = "new";
      this.component = null;
    });
  },
  methods: {
    getTaskMapping: function(index) {
      var url = window.location.href.includes("localhost")
        ? "http://localhost:8080/getTaskMapping"
        : "https://task-validation-lc.herokuapp.com/getTaskMapping";
      this.$http.get(url).then(response => {
        if (response.body != null) {
          this.Tasks = response.body;
        }
      });
    },
    editTask: function(task) {
      this.step++;
      var that = this;
      this.mode = "edit";
      this.component = "oneTask";
      setTimeout(function() {
        that.$refs.task.Task = task;
      }, 200);
    },
    removeTask: function(index) {
      this.Tasks.splice(index, 1);
    },
    createTask: function() {
      this.step++;
      this.mode = "new";
      var that = this;
      setTimeout(function() {
        that.component = "oneTask";
      }, 250);
    },
    saveTasks: function() {
      const tasks = JSON.stringify(this.Tasks);
      var url = window.location.href.includes("localhost")
        ? "http://localhost:8080/saveTaskMapping"
        : "https://task-validation-lc.herokuapp.com/saveTaskMapping";
      this.$http.post(url, tasks).then(
        () => {
          this.snackbar.color = "success";
          this.snackbar.snackbar = true;
        },
        e => {
          this.snackbar.color = "error";
          this.snackbar.snackbar = true;
          console.log("ERROR" + e);
        }
      );
    }
  }
};
</script>

<style scoped>
</style>