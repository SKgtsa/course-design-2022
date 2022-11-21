<template>
  <TEditor ref="editor" v-model="formState.content" :disabled='disabled' @getContent="getContent"/>
</template>

<script setup lang="ts">
import {  reactive } from "vue";
// 引入
import TEditor from '@/components/TEditor.vue';
const formState = reactive({contents :''})
const getContent = (v: string) => {
  formState.contents = v
}
</script>

<style scoped>

</style>
// 使用





