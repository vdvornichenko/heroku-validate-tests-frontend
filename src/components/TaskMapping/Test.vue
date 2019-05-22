<template>
	<div>
		<v-card>
			<v-card-title>
				<span class="headline">Test rule creator</span>
			</v-card-title>
			<v-card-text>
				<v-container grid-list-md>
					<v-layout wrap>
						<v-flex xs12 sm6>
							<v-text-field label="Test class name" required v-model="Test.name"></v-text-field>
						</v-flex>
						<v-flex xs12 sm6>
							<v-text-field label="Name tested class" required v-model="Test.testingClass"></v-text-field>
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
		Test: {
			name: "",
			testingClass: ""
		},
		modeEditTask: "new",
	}),
	methods: {
        emitCloseRule() {
            this.$root.$emit("closeRule");
        },

		emitSaveRule() {
			console.log("emit TestRule");
            if(this.modeEditTask == "new") {
              	this.$root.$emit("addRule", "TestRule", this.Test);
            } else {
                this.$root.$emit("closeRule");
            }
		}
	}
};
</script>

<style scoped>
</style>
