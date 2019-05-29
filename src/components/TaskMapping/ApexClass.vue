<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
	<div>
		<v-card>
			<v-card-title>
				<span class="headline">ApexClass rule creator</span>
			</v-card-title>
			<v-card-text>
				<v-container grid-list-md>
					<v-layout wrap>
						<v-flex xs12 sm6>
							<v-text-field label="ApexClass name" required v-model="ApexClass.name"></v-text-field>
						</v-flex>

						<v-flex xs12 sm6>
							<div class="text-xs-center">
								<v-dialog v-model="dialog" persistent max-width="600px" light>
									<template v-slot:activator="{ on }">
										<v-btn color="primary" dark @click="AddNewRule()">Add rule method execute</v-btn>
									</template>

									<v-card>
										<v-card-title>
											<span class="headline">Create execute code</span>
										</v-card-title>
										<v-card-text>
											<v-container grid-list-md>
												<v-layout wrap>
													<v-flex xs12 sm6>
														<v-text-field
															readonly
															label="Class name"
															required
															v-model="newMethodForExecute.nameClass"
														></v-text-field>
													</v-flex>
													<v-flex xs12 sm6>
														<v-text-field
															label="Enter Method name"
															required
															v-model="newMethodForExecute.nameMethod"
														></v-text-field>
													</v-flex>
													<v-flex xs12>
														<v-textarea
															v-model="newMethodForExecute.stringExecute"
															auto-grow
															name="input-7-1"
															label="Enter executed code"
														></v-textarea>
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
							<v-combobox
								ref="inp"
								v-model="ApexClass.methodForSearch"
								label="Add method for search"
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

						<v-flex xs12>
					
								<v-expansion-panel popout>
									<v-expansion-panel-content
										v-for="(method, index) in ApexClass.methodsForExecute"
										:key="index"
									>
										<template v-slot:header>
											<p class="text-lg-right">
												<span class="font-weight-light body-1">Check execute in apexclass:</span>
												<span class="text-uppercase blue--text">{{ method.nameClass }}</span>
												<span class="font-weight-light body-1">method:</span>
												<span class="text-uppercase blue--text">{{ method.nameMethod }}</span>
											</p>
											<p class="text-xs-right">
												<v-btn
													fab
													icon
													float
													pre
													color="white"
													flat
													value="favorites"
													@click="editExecuteRule(method)"
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
													@click="removeExecuteRule(index)"
												>
													<v-icon>delete</v-icon>
												</v-btn>
											</p>
										</template>
										<v-card>
											<v-card-text>
												<v-textarea
													full-width
													auto-grow
													name="input-7-4"
													readonly
													:value="method.stringExecute"
												></v-textarea>
											</v-card-text>
										</v-card>
									</v-expansion-panel-content>
								</v-expansion-panel>
					
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
export default {
	name: "ApexClassComponent",
	data: () => ({
		dialog: false,
		mode: "new",
		modeEditTask: "new",
		ApexClass: {
			name: "",
			methodForSearch: [],
			methodsForExecute: []
		},
		newMethodForExecute: {
			nameClass: "",
			nameMethod: "",
			stringExecute: ""
		}
	}),
	methods: {
		closeDialog: function() {
			this.dialog = false;
		},
		removeExecuteRule: function(ind) {
			this.ApexClass.methodsForExecute.splice(ind, 1);
		},
		editExecuteRule: function(method) {
			this.dialog = true;
			this.mode = "edit";
			this.newMethodForExecute = method;
		},
		AddNewRule: function() {
			this.dialog = true;
			this.mode = "new";
			this.newMethodForExecute = {
				nameClass: this.ApexClass.name,
				nameMethod: "",
				searchStrings: []
			};
		},
		saveDialog: function() {
			if (this.mode == "new") {
				this.ApexClass.methodsForExecute.push(this.newMethodForExecute);
			}
			this.dialog = false;
		},
		emitCloseRule() {
			this.$root.$emit("closeRule");
		},
		emitSaveRule() {
			console.log("emit ApexClassRule");
            if(this.modeEditTask == "new") {
                this.$root.$emit("addRule", "ApexClassRule", this.ApexClass);
            } else {
                 this.$root.$emit("closeRule");
            }
		}
	}
};
</script>

<style scoped>
</style>
