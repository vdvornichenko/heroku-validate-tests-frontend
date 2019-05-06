<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div v-if="showResults" style="padding-top: 30px">
        <div v-for="(value, propertyName, index) in userResults" v-bind:key="index" style="padding-top: 30px">
            <v-toolbar flat>
                <v-toolbar-title>{{ propertyName }}</v-toolbar-title>
            </v-toolbar>
            <v-data-table
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
</template>

<script>

    export default {
        name: "Results",
        data: () => ({
            showResults : false,
            userResults : [],
            userResultsHeaders : [
                {text : "Metadata File", value : "nameMetadata"},
                {text : "Status", value : "status"},
                {text : "Message", value : "message"}
            ]

        }),

        mounted() {
            this.$root.$on('getUserResults', results => {
                this.userResults = results;
                this.showResults = true;
            });
        }
    }
</script>

<style scoped>

</style>