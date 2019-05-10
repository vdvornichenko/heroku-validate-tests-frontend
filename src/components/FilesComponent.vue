<template>
    <v-layout row justify-center>
        <v-dialog v-model="dialog" max-width="1200" transition="dialog-bottom-transition">
            <v-card>
                <v-toolbar dark color="primary">
                    <v-btn icon dark @click="dialog = false">
                        <v-icon>close</v-icon>
                    </v-btn>
                    <v-toolbar-title>{{ fileOwner }}</v-toolbar-title>
                </v-toolbar>
                <v-subheader>{{ fileName }}</v-subheader>
                <div id="fileContent" style="padding-left:40px">
                </div>
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    export default {
        name: "FilesComponent",
        data: () => ({
           dialog: false,
           fileName: "",
           fileOwner: ""
        }),

        mounted() {
            this.$root.$on('setUserFile', file => {
               document.getElementById('fileContent').innerHTML = file.content;
               this.fileName = file.fileName;
               this.fileOwner = file.fileOwner;
               this.dialog = true;
            });
        }
    }
</script>

<style scoped>

</style>