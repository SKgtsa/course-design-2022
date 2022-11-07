<template>
    <div class="selfInformationPage">
        <el-container>
            <el-aside width="300px" class="aside">
                <div class="selfImage">
                    <AvatarUploadTestViewVue class="image" />
                </div>
                <div class="selfInf">
                    <el-table :data="tableData" size="mini" class="tableStyle">
                        <el-table-column prop="title" label="" width="120">
                        </el-table-column>
                        <el-table-column prop="information" label="" width="120">
                        </el-table-column>
                    </el-table>
                    <el-button type="info" class="changeButton" plain>
                        修改个人信息
                    </el-button>

                </div>
            </el-aside>
            <el-main class="main">

            </el-main>
        </el-container>
    </div>
</template>
<script lang="ts" setup>
import AvatarUploadTestViewVue from '@/components/AvatarUploadTestView.vue';
import { reactive, ref } from "vue";
const tableData = reactive([
    {
        title:"姓名",
        information:"彭帅豪",
    },
    {
        title: "性别",
        information: "男",
    },
    {
        title: "邮箱",
        information: "859455675@qq.com",
    },
    {
        title: "电话",
        information:'12345678910',
    },
    {
        title:'地址',
        information:"山东大学软件园校区"
    }
])
</script>
<style lang="scss" scoped>
.selfInformationPage {
    position: relative;

    .aside {
        position: absolute;
        background-color: aqua;
        height: 650px;

        .selfImage {
            position: absolute;
            width: 280px;
            height: 200px;
            left: 10px;
            display: flex;
            padding-top: 20px;
            justify-content: center;
            background-color: rgb(196, 255, 201);

            .image {
                display: flex;
                justify-content: center;
            }
        }

        .selfInf {
            display: flex;
            position: absolute;
            top: 230px;
            margin-top: 2px;
            width: 280px;
            height: 400px;
            left: 10px;
            background-color: brown;
            .tableStyle{
                margin-top: 40px;
                padding-top: 10px;
                padding-left: 5px;
            }
            .changeButton{
                position: absolute;
                top:340px;
                width: 220px;
                height: 35px;
                left: 25px;
                color: #FFFF99;
            }
        }
    }

    .main {
        background-color: bisque;
        height: 650px;
    }
}
</style>