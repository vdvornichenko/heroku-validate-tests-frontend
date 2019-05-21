<template>
	<div>
		<v-card>
			<v-card-title>
				<span class="headline">Trigger rule creator</span>
			</v-card-title>
			<v-card-text>
				<v-container grid-list-md>
					<v-layout wrap>
						<v-flex xs12 sm4>
							<v-text-field label="Trigger name" required v-model="Trigger.name"></v-text-field>
						</v-flex>
						<v-flex xs12 sm4>
							<v-text-field label="Helper method name" required v-model="Trigger.helperMethod"></v-text-field>
						</v-flex>
						<v-flex xs12 sm4>
							<v-text-field label="Trigger for object" required v-model="Trigger.objName"></v-text-field>
						</v-flex>
						<v-flex xs12>
							<v-combobox
								v-model="Trigger.trigerEvents"
								:items="events"
								label="I use chips"
								multiple
								chips
							></v-combobox>
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
		Trigger: {
			name: "",
			objName: "",
			trigerEvents: [],
			helperMethod: ""
		},
		modeEditTask: "new",
		events: [
			"before insert",
			"before update",
			"before delete",
			"after update",
			"after insert",
			"after delete",
			"after undelete"
		]
	}),
	methods: {
        emitCloseRule() {
            this.$root.$emit("closeRule");
        },
		emitSaveRule() {
			console.log("emit TriggerRule");
            if(this.modeEditTask == "new") {
                this.$root.$emit("addRule", "TriggerRule", this.Trigger);
            } else {
                this.$root.$emit("closeRule");
            }
		}
	}
};
</script>

<style scoped>
</style>
