<template>
    <v-footer :inset="footer.inset" app>
        <span class="px-3">&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
</template>

<script>
    export default {
        name: "FooterCmp",
        data: () => ({
            footer: {
                inset: false
            }
        })
    }
</script>

<style scoped>

</style>