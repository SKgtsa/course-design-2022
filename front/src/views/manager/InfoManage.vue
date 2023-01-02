<template>
    <div class="content">
        <div class="mainCard">
            <div class="header">

                <div class="leftPanel">
                    <a class="title">信息管理</a>
                    <div class="search">
                        <span class="searchSpan">
                            <el-input v-model="search" type="text" class="searchTerm" maxlength="15"
                                placeholder="输入学工号" />
                            <el-button type="submit" class="searchButton">
                                <el-icon class="icon">
                                    <Search />
                                </el-icon>
                            </el-button>
                            <el-button type="danger" class="delectButton">
                                <a class="delectText">删除用户</a>
                            </el-button>
                        </span>
                    </div>
                </div>
                <div class="rightPanel">
                    <div class="imgBox">
                        <el-image class="img" :src="url" fit="cover">
                        </el-image>
                    </div>
                </div>
            </div>
            <div class="main">
                <div class="menu">
                    <span class="menuButtonSpan">
                        <el-button class="menuButton">
                            <a>个人信息</a>
                        </el-button>
                    </span>
                    <span class="menuButtonSpan">
                        <el-button class="menuButton">
                            <a>社会实践</a>
                        </el-button>
                    </span>
                    <span class="menuButtonSpan">
                        <el-button class="menuButton">
                            <a>成果奖励</a>
                        </el-button>
                    </span>
                    <span class="menuButtonSpan">
                        <el-button class="menuButton">
                            <a>课外活动</a>
                        </el-button>
                    </span>
                </div>
                <div>

                </div>
                <div>

                </div>
            </div>
        </div>
    </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';


let search = ref();
let url = ref('https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg');


const handleOpen = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
}
</script>
<style lang="scss" scoped>
.content {
    height: 100vh;
    width: 90vw;

    .mainCard {
        display: flex;
        flex-direction: column;
        background-color: #FFFFFF;
        margin-top: 4vh;
        margin-left: 9vw;
        width: 80vw;
        height: 95vh;
        border-radius: 1vw;
        box-shadow: 0 0 1.25vh 0 #b9ccee;

        .header {
            height: 35vh;
            width: 100%;
            background-color: rgb(232, 245, 247);
            display: flex;
            flex-direction: row;



            .leftPanel {
                width: 50vw;
                height: 30vh;
                display: flex;
                flex-direction: column;

                .title {
                    padding-top: 5vh;
                    padding-left: 5vw;
                    font-size: 2.5vw;
                    color: #1a91f3;
                    font-weight: 600;
                }

                .search {
                    display: flex;

                    .searchSpan {
                        display: flex;
                        flex-direction: row;
                        width: 45vw;
                        padding-top: 5vh;
                        padding-left: 5vw;

                        .searchTerm {

                            width: 40vw;
                            border: 0.2vw solid #00B4CC;
                            border-right: none;
                            height: 6vh;
                            border-top-left-radius: 0.2vw;
                            border-bottom-left-radius: 0.2vw;
                            outline: none;
                            color: #9DBFAF;
                            font-size: 1.2vw;
                        }

                        .searchTerm:focus {
                            color: #00B4CC;
                        }

                        .searchButton {
                            width: 5vw;
                            height: 6vh;
                            border: 0.2vw solid #00B4CC;
                            background: #00B4CC;
                            text-align: center;
                            color: #fff;
                            border-radius: 0 0.2vw 0.2vw 0;
                            cursor: pointer;
                            font-size: 1.2vw;
                        }

                        .delectButton {
                            height: 6vh;
                            border: 0.1vw solid;
                        }
                    }
                }
            }

            .rightPanel {
                width: 30vw;
                height: 30vh;

                /*         .nameBox {
                    .name {
                        font-size: 1.6vw;
                    }
                } */
                .imgBox {
                    padding-left: 8vw;
                    padding-top: 3vh;
                    width: 15vw;
                    height: 25vh;

                    .img {
                        height: 100%;
                        width: 100%;
                    }
                }

            }
        }

        .main {
            height: 65vh;
            width: 80vw;
            background-color: bisque;

            .menu {
                width: 15vw;
                height: 65vh;
                background-color: rgb(255, 255, 255);

                .menuButtonSpan {
                    width: 15vw;
                }

                .menuButton {
                    width: 15vw;
                    height: 6vh;
                    font-size: 1vw;
                    font-weight: 550;
                    line-height: 4vh;
                }
            }
        }




        /*Resize the wrap to see the search bar change!*/
    }
}
</style>