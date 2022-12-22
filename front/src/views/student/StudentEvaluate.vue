<template>
    <!-- 需要给至少五位同学打上标签，然后还要能查看别人给自己的评价，就是两个div界面用if切换吧 -->
    <div class="content">
        <div class="titleBox">
            <a class="title">学生评价</a>
        </div>
        <div class="panel">
            <el-button class="opButton">
                <a>查看对我的评价</a>
            </el-button>
            <el-button class="opButton">
                <a>给别人评价</a>
            </el-button>
        </div>
        <div v-if="showType == 'check'" class="checkEvaluation">
            <el-card class="box-card">
                <template #header>
                    <div class="card-header">
                        <span>Card name</span>
                        <el-button class="button" text>Operation button</el-button>
                    </div>
                </template>
                <div v-for="o in 4" :key="o" class="text item">{{ 'List item ' + o }}</div>
            </el-card>
        </div>
        <div v-if="showType == 'evaluate'" class="evaluateOthers">
        </div>
    </div>
</template>
<script lang="ts" setup>
import { reactive } from '@vue/reactivity';
import { ref } from 'vue';
let showType = ref('check');
let tableData = reactive([]);
let currentPage = ref(1);
let pageSize = ref(7);
</script>
<style lang="scss" scoped>
.content {
    height: 100%;
    display: flex;
    flex-direction: column;

    .titleBox {
        height: 8vh;
        width: 100%;
        display: flex;
        flex-direction: row;

        .title {
            display: flex;
            width: 15vw;
            margin-top: 30px;
            font-family: LiSu;
            font-size: 6vh;
            font-weight: 580;
            line-height: 1vh;
            color: #2986f0;
        }

    }

    .panel {
        padding-top: 3vh;
        padding-right: 3vw;
        height: 15vh;
        display: flex;
        flex-direction: row;

        .opButton {
            display: flex;
            height: 5vh;
            width: 10vw;
/*             color: #fc0707;
 */            /* 清除背景色 */
            background: transparent;
            /* 边框样式、颜色、宽度 */
            border: 2px solid #8e5cf1!important;
            /* 给边框添加圆角 */
            border-radius: 15px;
            border: none;
            color: rgb(205, 7, 255);
            text-align: center;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            -webkit-transition-duration: 0.4s;
            /* Safari */
            transition-duration: 0.4s;
            cursor: pointer;
            text-decoration: none;
            text-transform: uppercase;
        }

         .opButton:hover {
            background-color: rgb(142, 133, 249);
            color: white;
        } 
    }

    .checkEvaluation {}

    .evaluateOthers {}
}
</style>