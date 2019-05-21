<template>
	<div>
		<v-dialog v-model="dialog" persistent max-width="600px" light>
			<template v-slot:activator="{ on }">
				<!-- <v-btn color="primary" dark v-on="on">{{buttonCreate}}</v-btn> -->
				<v-btn color="primary" dark @click="createNew()">{{buttonCreate}}</v-btn>
			</template>
			<v-card>
				<v-card-title>
					<span class="headline">{{nameObj}}</span>
				</v-card-title>
				<v-card-text>
					<v-container grid-list-md>
						<v-layout wrap>
							<v-flex xs12 sm12 md12>
								<v-text-field label="name Field" required v-model="map.name"></v-text-field>
							</v-flex>
							<v-flex xs12 sm12 md12>
								<ul>
									<li v-for="(row, index) in map.keyValue" :key="row.key">
										<v-layout row>
											<v-flex grow pa-1 align-self-baseline>
												<span class="font-weight-light body-1"> key: </span>
												<span class="text-uppercase blue--text"> {{ row.key }}</span>
												<span class="font-weight-light body-1"> - value: </span>
												<span class="text-uppercase blue--text"> {{ row.value }}</span>
											</v-flex>
											<v-flex shrink pa-1 align-self-baseline>
												<v-btn color="blue darken-1" flat v-on:click="remove(index)">remove</v-btn>
											</v-flex>
										</v-layout>
									</li>
								</ul>
							</v-flex>
							<v-flex xs5>
								<v-select v-model="newKeyValue.key"  light :items="selectList" label="tag" required ></v-select>
							</v-flex>
							<v-flex xs4>
								<v-text-field label="value" v-model="newKeyValue.value" required></v-text-field>
							</v-flex>
							<v-flex xs3>
								<v-btn color="blue darken-1" flat @click="AddRule()">Add next Rule</v-btn>
							</v-flex>
						</v-layout>
					</v-container>
				</v-card-text>
				<v-card-actions>
					<v-spacer></v-spacer>
					<v-btn color="blue darken-1" flat v-on:click="Close()">Close</v-btn>
					<v-btn color="blue darken-1" flat v-on:click="Add()">{{mode}}</v-btn>
				</v-card-actions>
			</v-card>
		</v-dialog>
	</div>
</template>
<script>
export default {
	name: "mapComponent",
	props: {
		selectList: Array,
		nameObj: String,
		buttonCreate: String
	},
	data: () => ({
		dialog: false,
		mode: "new",
		map: {
			name: "",
			keyValue: []
		},
		newKeyValue: {
			key: "",
			value: ""
		}
	}),
	methods: {
		Close: function() {
			this.dialog = false;
		},
		Add: function() {
			this.dialog = false;
			if (this.mode == "new") {
				this.$emit("sendRule", this.map);
				this.map = {
					name: "",
					keyValue: []
				};
			}
		},
		createNew: function() {
			this.dialog = true;
			this.mode = "new";
			this.map = {
				name: "",
				keyValue: []
			};
			this.newKeyValue = {
				key: "",
				value: ""
			};
		},
		remove: function(ind) {
			this.map.keyValue.splice(ind, 1);
		},
		AddRule: function() {
			this.map.keyValue.push(this.newKeyValue);
			this.newKeyValue = {
				key: "",
				value: ""
			};
		}
	}
};
</script>

<style scoped>
</style>