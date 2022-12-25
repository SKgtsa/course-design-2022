<template>
    <div class="content">
        <el-container>
            <el-aside class="aside">
                <div class="operationPage">
                    <el-menu router default-active="/AcademicManagement/AMHome" class="menu">
                        <el-menu-item index="/AcademicManagement/AMHome">
                            <el-icon>
                                <location />
                            </el-icon>
                            <span>学工首页</span>
                        </el-menu-item>
                        <el-sub-menu index="1">
                            <template #title>
                                <el-icon>
                                    <Bicycle />
                                </el-icon>
                                <span>精彩生活</span>
                            </template>
                            <el-menu-item-group>
                                <el-menu-item index="/AcademicManagement/Practice">社会实践</el-menu-item>
                                <el-menu-item index="/AcademicManagement/Activity">课外活动</el-menu-item>
                                <el-menu-item index="/AcademicManagement/Reward">成果奖励</el-menu-item>
                            </el-menu-item-group>
                        </el-sub-menu>
                        <el-menu-item index="/AcademicManagement/StudentEvaluate">
                            <el-icon>
                                <location />
                            </el-icon>
                            <span>学生评价</span>
                        </el-menu-item>
                    </el-menu>
                </div>
                <div class="notice">
                    <el-carousel height="100%" direction="vertical" class="carouselPage" :autoplay="false">
                        <el-carousel-item class="carouselItem">
                            <img class="img" src='../../assets/images/小确幸.jpg' />
                        </el-carousel-item>
                        <el-carousel-item class="carouselItem">
                            <img class="img" src='../../assets/images/学代会.jpg' />
                        </el-carousel-item>
                        <el-carousel-item class="carouselItem">
                            <img class="img" src='../../assets/images/支教.jpg' />
                        </el-carousel-item>
                        <el-carousel-item class="carouselItem">
                            <img class="img" src='../../assets/images/志愿.jpg' />
                        </el-carousel-item>
                        <el-carousel-item class="carouselItem">
                            <img class="img" src='../../assets/images/座谈会.jpg' />
                        </el-carousel-item>
                    </el-carousel>
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
import AvatarUploadTestViewVue from '@/components/photoUploadView.vue';
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
            height: 50vh;
            display: flex;
            flex-direction: column;
            /*             justify-content: center; */
            background-color: #FFFFFF;
            border-radius: 1vw;
            box-shadow: 0 0 10px 0 #b9ccee;

            .menu {
                padding-top: 0px !important;
                padding-bottom: 0px !important;
            }

            .changeButton {
                display: flex;
                margin-top: 2vh;
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

            /*  background-color: #FFFFFF;
 */
            .carouselPage {
                height: 100%;
                width: 100%;

                .carouselItem {
                    height: 100%;
                    width: 100%;

                    .img {
                        height: 100%;
                        width: 100%;
                    }
                }
            }
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