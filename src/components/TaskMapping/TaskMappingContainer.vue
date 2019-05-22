<template >
	<v-app id="taskMp" dark>
		<Header @changeTheme="!!dark"/>

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
												<!-- <v-list>
								
								<v-list-group
									sub-group
									v-for="(row, index) in Tasks"
									:key="index"
									:prepend-icon="row.action"
									no-action
								>
									<template v-slot:activator>
										<v-list-tile>
											<v-list-tile-content>
												<v-list-tile-title>
													<span class="font-weight-light body-1">Task Number:</span>
													{{ index + 1}}
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
														small
														 @click="editTask(row)"
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
														@click="removeTask(index)"
													><v-icon>delete</v-icon>
                                      		  		</v-btn>
									</template>
									<v-list-tile v-for="(subItem, name, subIndex) in row" :key="subIndex">
										<v-list-tile-content>
											<v-list-tile-title>
												<span class="font-weight-light body-1">{{subItem}}</span>
											</v-list-tile-title>
										</v-list-tile-content>
									</v-list-tile>
								</v-list-group>
							
												</v-list>-->

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
														<v-btn color="primary" dark @click="getTasks">TEST BUTTON</v-btn>
														<v-btn color="primary" dark @click="saveTasks" v-if="Tasks.length>0">SAVE TASKS</v-btn>
													</v-layout>
													<!-- <v-btn  color="primary" dark @click="cancel">Cancel</v-btn>
													<v-btn  color="primary" dark @click="createTask">CREATE TASK</v-btn>-->
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
			console.log("emit createTask ");
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
			this.$http
				.get("http://localhost:8080/getTaskMapping")
				.then(response => {
					console.log(response.body);
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
			console.log(" createTask ");
			this.step++;
			this.mode = "new";
			var that = this;
			setTimeout(function() {
				that.component = "oneTask";
			}, 250);
		},
		saveTasks: function() {
			console.log(" saveTasks ");
			const tasks = JSON.stringify(this.Tasks);
			console.log(tasks);
			var url = window.location.href.includes("localhost")
				? "http://localhost:8080/saveTaskMapping"
				: "https://task-validation-lc.herokuapp.com/saveTaskMapping";
			this.$http.post(url, tasks).then(
				() => {
					// eslint-disable-next-line no-console
					console.log("SUCCESS");
				},
				() => {
					// eslint-disable-next-line no-console
					console.log("ERROR");
				}
			);
			// this.$http.get(HTTP_USER_CREDS_URL).then(response => {
			// 	let groups = [];
			// 	response.body.forEach((elem, i) => {
			// 		// this.users.push({
			// 		// 	index: i,
			// 		// 	userName: elem.userName,
			// 		// 	password: elem.password,
			// 		// 	checked: false,
			// 		// 	group: elem.group,
			// 		// 	fio: elem.fio
			// 		// });
			// 		// groups.push(elem.group);
			// 	});
			// 	this.groups = new Set(groups);
			// 	this.$root.$emit("setState", false);
			// });
		},
		getTasks: function() {
			var url = window.location.href.includes("localhost")
				? "http://localhost:8080/getTaskMapping"
				: "https://task-validation-lc.herokuapp.com/getTaskMapping";
			this.$http.get(url).then(response => {
				console.log(response.body);
			});
		}
	}
};
</script>

<style scoped>
</style>