<template>
    <div class="content">
        <el-container>
            <el-aside class="aside">
                <div class="operationPage">
                    <el-button type="default" class="changeButton" @click="router.push('/AcademicManagement/Practice')">
                        <a>社会实践</a>
                    </el-button>
                    <el-button type="default" class="changeButton" @click="router.push('/AcademicManagement/Reward')">
                        <a>成果奖励</a>
                    </el-button>
                    <el-button type="default" class="changeButton" @click="router.push('/AcademicManagement/Activity')">
                        <a>课外活动</a>
                    </el-button>
                    <el-button type="default" class="changeButton"
                        @click="router.push('/AcademicManagement/StudentEvaluate')">
                        <a>学生评价</a>
                    </el-button>
                </div>
                <div class="notice">
                </div>
            </el-aside>
            <el-main class="main">
                <router-view /> <!-- 再建一个vue用于滚动主页图片？直接放组件里引入过来吧 -->
            </el-main>
        </el-container>
    </div>
</template>
<script lang="ts" setup>
import router from "@/router";
import AvatarUploadTestViewVue from '@/components/AvatarUploadTestView.vue';
import service from '@/request';
import { messageWarning, messageError, messageInfo, messageSuccess } from '@/utils/message';
import { reactive, ref } from "vue";
  //变量
</script>
<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: row;
    height: 100vh;

    .aside {
        display: flex;
        flex-direction: column;
        height: 85vh;
        width: 24vw;
        padding-top: 5vh;
        padding-left: 4vw;

        /*       border-radius: 1vw;
        box-shadow: 0 0 10px 0 #b9ccee; */
        .operationPage {
            border-radius: 1vw;
            width: 18vw;
            height: 35vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            background-color: #FFFFFF;
            border-radius: 1vw;
            box-shadow: 0 0 10px 0 #b9ccee;

            .changeButton {
                display: flex;
                margin-top: 3vh;
                justify-content: center;
                margin-left: 2vw;
                width: 14vw;
                height: 5vh;
                color: #0808ad;
                background: transparent;
                /* 边框样式、颜色、宽度 */
                border: 2px solid #8e5cf1 !important;
                /* 给边框添加圆角 */
                border-radius: 6px;
                border: none;
                color: rgb(13, 50, 183);
                text-align: center;
                display: inline-block;
                font-size: 16px;
                -webkit-transition-duration: 0.4s;
                /* Safari */
                transition-duration: 0.4s;
                cursor: pointer;
                text-decoration: none;
                text-transform: uppercase;
            }

            .changeButton:hover {
                background-color: #1abaf0;
                color: white;

            }
        }

        .notice {
            border-radius: 1vw;
            box-shadow: 0 0 10px 0 #b9ccee;
            margin-top: 2vh;
            width: 18vw;
            height: 51vh;
            background-color: #FFFFFF;

        }
    }

    .main {
        padding: 0px 0px 0px 0px !important;
        margin-top: 5vh;
        margin-right: 3vw;
        height: 80vh;
        width: 60vw;
        border-radius: 1vw;
        box-shadow: 0 0 10px 0 #b9ccee;
        background-color: #FFFFFF;
    }
}

//下面是弹出框，在布局中独立，没有写入子样式中
.areaTextInput {
    font-size: 15px;
}

.dialogButtonPage {
    text-align: center;
}

.dialogButton {
    padding-left: 5px;
    padding-right: 5px;
}
</style>