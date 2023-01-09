<template>
    <div class="container">
      <div class="header">
        <div class="titleBox">
          <img class="chineseImg" v-if="!mobile" src='https://courseback.clankalliance.cn/static/inbuild/psh/烟火一格.png' />
          <img class="englishImg" v-if="!mobile" src='https://courseback.clankalliance.cn/static/inbuild/psh/烟火一格英语.png' />
          <img class="chineseImg" style="width:60%" v-if="mobile" src='https://courseback.clankalliance.cn/static/inbuild/psh/烟火一格.png' />
          <img class="englishImg" style="width:60%" v-if="mobile" src='https://courseback.clankalliance.cn/static/inbuild/psh/烟火一格英语.png' />
        </div>
        <div class="imgBox">
        </div>
      </div>
      <div class="menuBox">
        <el-menu class="menu" default-active="/PengShuaihao" mode="horizontal" background-color="#545c64" text-color="#fff"
          active-text-color="#ffd04b" @select="handleSelect">
          <el-menu-item index="/PengShuaihao"><span @click="toMain">首页</span></el-menu-item>
          <el-menu-item index="1">
            <span @click="showLake">大明湖
          </span></el-menu-item>
          <el-menu-item index="2">
            <span @click="showHouse">洪家楼
          </span></el-menu-item>
          <el-menu-item index="3">
            <span @click="showLeave">红叶谷
          </span></el-menu-item>
          <el-menu-item index="4">
            <span @click="showBaotu">趵突泉
          </span></el-menu-item>
   <!--        <el-menu-item index="5">
            <span @click="checkInfo">关于作者
          </span></el-menu-item> -->
        </el-menu>
      </div>         <!-- :style="{'height':`${mobile? '40vh': '70vh'}`}" -->
      <div class="main" :style="{'padding-left':`${mobile?'0':'10vw'}`,'padding-right':`${mobile?'0':'10vw'}`}">
        <div class="carouselBox" :style="{
          'height':`${mobile? '50vh': '70vh'}`,
          'width':`${mobile? '100vw': '100%'}`,
          }" v-if="!isMenu">
          <el-carousel ref="carousel" v-touch:swipe.left="right" v-touch:swipe.right="left" class="carouselPage" height="100%" interval="4000">
            <el-carousel-item class="carouselItem">
              <img style="width: 100%; height: 100%" src='https://courseback.clankalliance.cn/static/inbuild/psh/横屏3.jpg' />
            </el-carousel-item>
            <el-carousel-item class="carouselItem">
              <img style="width: 100%; height: 100%" src='https://courseback.clankalliance.cn/static/inbuild/psh/横屏2.jpg' />
            </el-carousel-item>
            <el-carousel-item class="carouselItem">
              <img style="width: 100%; height: 100%" src='https://courseback.clankalliance.cn/static/inbuild/psh/雪景.jpg' />
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="content" v-if="!isMenu" :style="{'height':`${mobile? '120vh': '80vh'}`}">
          <div v-if="!mobile" class="tourDiv">
            <img  src="https://courseback.clankalliance.cn/static/inbuild/psh/导游.jpg" class="tour">
          </div>
          <el-scrollbar v-if="!mobile" class="contentSrollbar">
            <div class="cardBox" v-if="!mobile">
              <el-card class="card" @click="showBaotu">
                <img class="cardImg" src="https://courseback.clankalliance.cn/static/inbuild/psh/趵突泉.jpg">
                <span class="cardText">趵突泉</span>
              </el-card>
              <el-card class="card" @click="showHouse">
                <img class="cardImg" src="https://courseback.clankalliance.cn/static/inbuild/psh/洪家楼.jpg">
                <span class="cardText">洪家楼</span>
              </el-card>
            </div>
            <div class="cardBox" v-if="!mobile">
              <el-card class="card" @click="showLake">
                <img class="cardImg" src="https://courseback.clankalliance.cn/static/inbuild/psh/大明湖.png">
                <span class="cardText">大明湖</span>
              </el-card>
              <el-card class="card" @click="showLeave">
                <img class="cardImg" src="https://courseback.clankalliance.cn/static/inbuild/psh/红叶谷.jpeg">
                <span class="cardText">红叶谷</span>
              </el-card>
            </div>
          </el-scrollbar>
            <div v-if="mobile" style="width:100vw;height: 120vh;padding-left: 8vw;">
              <a class="mobileTitle">景致</a>
            <el-scrollbar style="height:110vh;">
              <el-card class="cardMobile" @click="showBaotu" >
                <img class="cardImgMobile" src="https://courseback.clankalliance.cn/static/inbuild/psh/趵突泉.jpg">
                <span class="cardText">趵突泉</span>
              </el-card>
              <el-card class="cardMobile" @click="showHouse">
                <img class="cardImgMobile" src="https://courseback.clankalliance.cn/static/inbuild/psh/洪家楼.jpg">
                <span class="cardText">洪家楼</span>
              </el-card>
              <el-card class="cardMobile" @click="showLake">
                <img class="cardImgMobile" src="https://courseback.clankalliance.cn/static/inbuild/psh/大明湖.png">
                <span class="cardText">大明湖</span>
              </el-card>
              <el-card class="cardMobile" @click="showLeave">
                <img class="cardImgMobile" src="https://courseback.clankalliance.cn/static/inbuild/psh/红叶谷.jpeg">
                <span class="cardText">红叶谷</span>
              </el-card>
            </el-scrollbar>
            </div>

        </div>
        <div class="carouselBox" v-if="isMenu">
          <img v-if="attractions == 'Baotu'" style="width:100%;height: 100%;" src="https://courseback.clankalliance.cn/static/inbuild/psh/趵突泉.jpg"/>
          <img v-if="attractions == 'lake'" style="width:100%;height: 100%;" src="https://courseback.clankalliance.cn/static/inbuild/psh/大明湖.png"/>
          <img v-if="attractions == 'leave'" style="width:100%;height: 100%;" src="https://courseback.clankalliance.cn/static/inbuild/psh/红叶谷.jpeg"/>
          <img v-if="attractions == 'house'" style="width:100%;height: 100%;" src="https://courseback.clankalliance.cn/static/inbuild/psh/洪家楼.jpg"/>
        </div>

        <div class="contentAttraction" v-if="isMenu">
          <!--  <div class="divider">
          </div> -->
          <el-button type="primary" @click="toMain" style="height:5vh;">
            <a>看看其他</a>
          </el-button>
          <el-scrollbar class="attractionScrollbar">
            <span class="attractionTitle">
              <h3>简介</h3>
              <el-divider />
            </span>
            <div style="padding-right:6vw;padding-left:3vw;" v-if="attractions=='Baotu'"><a class="attrIntro">{{ Baotu }}</a></div>
            <div style="padding-right:6vw;padding-left:3vw;" v-if="attractions=='lake'"><a class="attrIntro">{{ lake }}</a></div>
            <div style="padding-right:6vw;padding-left:3vw;" v-if="attractions=='house'"><a class="attrIntro">{{ house }}</a></div>
            <div style="padding-right:6vw;padding-left:3vw;" v-if="attractions=='leave'"><a class="attrIntro">{{ leave }}</a></div>
            <el-divider />
            <!-- <span class="attractionTitle"><a>详情</a> </span> -->
            <el-descriptions v-if="attractions=='Baotu'"
      class="margin-top"
      :column="2"
      title="详情"
      border
    >
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <user />
            </el-icon>
            中文名
          </div>
        </template>
        趵突泉
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <iphone />
            </el-icon>
            开放时间
          </div>
        </template>
        全年 07:00-19:00(最晚入园18:30)
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
             <el-icon>
              <user />
            </el-icon>
            英文名
          </div>
        </template>
        Baotu Spring
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <tickets />
            </el-icon>
            景点级别
          </div>
        </template>
        国家AAAAA级旅游景区
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <location />
            </el-icon>
            地理位置
          </div>
        </template>
        山东省济南市历下区趵突泉南路1号
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            门票价格
          </div>
        </template>
        成人40元；学生20元
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            气候条件
          </div>
        </template>
        温带季风气候
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            占地面积
          </div>
        </template>
        158 亩
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions v-if="attractions=='lake'"
      class="margin-top"
      :column="2"
      title="详情"
      border
    >
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <user />
            </el-icon>
            中文名
          </div>
        </template>
        大明湖
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <iphone />
            </el-icon>
            开放时间
          </div>
        </template>
        全年 6：00-22：00
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
             <el-icon>
              <user />
            </el-icon>
            英文名
          </div>
        </template>
        Daming Lake
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <tickets />
            </el-icon>
            景点级别
          </div>
        </template>
        国家AAAAA级旅游景区
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <location />
            </el-icon>
            地理位置
          </div>
        </template>
        山东省济南市历下区
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            门票价格
          </div>
        </template>
        45元
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            所属水系
          </div>
        </template>
        渤海独流入海河流小清河
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            湖面面积
          </div>
        </template>
        57.7公顷
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions v-if="attractions=='house'"
      class="margin-top"
      :column="2"
      title="详情"
      border
    >
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <user />
            </el-icon>
            中文名
          </div>
        </template>
        洪家楼天主教堂
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <iphone />
            </el-icon>
            开放时间
          </div>
        </template>
        周一至周六:9:00-16:00.周日:8:00-19:00 特殊时间除外
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
             <el-icon>
              <user />
            </el-icon>
            所属年代
          </div>
        </template>
        清
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <tickets />
            </el-icon>
            保护级别
          </div>
        </template>
        第六批全国重点文物保护单位
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <location />
            </el-icon>
            地理位置
          </div>
        </template>
        山东省济南市区东部，历城区花园路洪家楼3号
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            门票价格
          </div>
        </template>
        无
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            气候条件
          </div>
        </template>
        温带季风气候
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            占地面积
          </div>
        </template>
        约 2600 m²
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions v-if="attractions=='leave'"
      class="margin-top"
      :column="2"
      title="详情"
      border
    >
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <user />
            </el-icon>
            中文名
          </div>
        </template>
        红叶谷生态文化旅游区
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <iphone />
            </el-icon>
            开放时间
          </div>
        </template>
        全年 08:00—17:00
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
             <el-icon>
              <user />
            </el-icon>
            英文名
          </div>
        </template>
        Red Leaves Canyon
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <tickets />
            </el-icon>
            景点级别
          </div>
        </template>
        国家AAAA级旅游景区
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <location />
            </el-icon>
            地理位置
          </div>
        </template>
        山东省济南市历城区仲宫街道
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            门票价格
          </div>
        </template>
        40元/人
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            气候条件
          </div>
        </template>
        温带季风气候
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">
            <el-icon>
              <office-building />
            </el-icon>
            占地面积
          </div>
        </template>
        约 4000 亩
      </el-descriptions-item>
    </el-descriptions>
          </el-scrollbar>
        </div>
        <div class="introductionBox">
          <div class="imgIntroductionBox">
          </div>
          <div class="textIntroductionBox">
            <el-scrollbar>
              <h4 class="introTitle">欢迎来到济南！</h4>
              <p3 class="introText">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;济南，这是一座以泉水闻名的城市，所谓的"家家泉水，户户垂柳"便是说的美丽的泉城济南。
  济南素以泉水众多、风景秀丽而闻名天下，据统计有四大泉域，十大泉群，733个天然泉，在国内外城市中罕见，是举世无双的天然岩溶泉水博物馆，除“泉城”外济南也被常被称为“泉都”，因“家家泉水户户垂柳”有著名的七十二泉，一派江南风光而得名。
              </p3>
            </el-scrollbar>
          </div>
        </div>
      </div>
      <div class="footer" v-if="isMenu||!mobile">
        <h3 class="copyright">@版权所有:信步书史</h3>
      </div>
    <!--   <div class="footer" style="padding-top:0;margin-top:0" v-if="mobile">
        <h3 class="copyright">@版权所有:信步书史</h3>
      </div> -->
    </div>
  </template>
  <script lang="ts" setup>
  import { mobile } from '@/global/global';
  import router from '@/router';
import { messageSuccess } from '@/utils/message';
  import { ref } from 'vue';

  const handleSelect = (key: string, keyPath: string[]) => {
    console.log(key, keyPath)
  }

/*   const checkInfo = () =>{
    messageSuccess('感谢你发现了我!')
  } */
  let carousel = ref(null)

  const left = () => {
  carousel.value.prev();
}

  const right = () => {
  carousel.value.next();
}
  const showLake = () => {
    isMenu.value = true;
    attractions.value = 'lake';
  }
  const showBaotu = () => {
    isMenu.value = true;
    attractions.value = 'Baotu';
  }
  const showLeave = () => {
    isMenu.value = true;
    attractions.value = 'leave';
  }
  const showHouse = () => {
    isMenu.value = true;
    attractions.value = 'house';
  }
  const toMain = () => {
    isMenu.value = false;
    attractions.value = '';
  }
  let attractions = ref('');
  let isMenu = ref(false);
  const Baotu = ref("趵突泉（Baotu Spring），济南三大名胜之一，位于山东省济南市历下区，东临泉城广场，北望五龙潭，面积达158亩，位居济南七十二名泉之冠。乾隆皇帝南巡时因趵突泉水泡茶味醇甘美，曾册封趵突泉为“天下第一泉”。也是最早见于古代文献的济南名泉。趵突泉泉眼位于趵突泉公园内的泺源堂前。民国二十年（1931年）四周用石砌岸。几经变化，形成长方形泉池，长30米，宽18米，深2.2米。北临泺源堂，西傍观澜亭，东架来鹤桥，南有长廊围合。泉水从地下石灰岩溶洞中涌出，每天涌出7万立方米泉水，泉水有三个出水口，最大涌水量为16.2万立方米/天。趵突泉水一年四季恒定在18℃左右。趵突泉周边的名胜古迹有泺源堂、观澜亭、尚志堂、李清照纪念堂、李苦禅纪念馆等景点。趵突泉与千佛山、大明湖并称为济南三大名胜。2013年，以趵突泉为代表的“天下第一泉景区”成为国家AAAAA级旅游景区。2016年9月5日，趵突泉泉群省级地质公园揭牌开园。");
  const house = ref('洪家楼天主教堂（Hongjialou Cathedral）全称洪家楼耶稣圣心主教座堂，一般简称洪楼教堂。教堂位于山东省济南市区东部，历城区花园路洪家楼3号。  东邻山东大学洪家楼校区，洪楼广场北侧，以洪家楼村而得名。洪家楼天主教堂是利用光绪二十六年（1900年）《辛丑条约》的庚子赔款筹建的，由奥地利庞会襄修士设计、中国劳工承建。光绪二十七年（1901年）开工建设，历经3年多时间，于光绪三十一年（1905年）5月竣工。整个建筑坐东朝西，立面为典型的哥特式建筑风格，平面呈“十字形”，象征着天主耶稣受难的十字架。建筑面积约2600平方米，教堂大厅可容纳千人进行宗教活动。洪家楼天主教堂建成时是济南市也是华北地区规模最大的天主教堂，在中国近代宗教建筑中占有重要地位。2006年5月，被国务院列为第六批全国重点文物保护单位。')
  const lake = ref('大明湖，山东省济南市区湖泊、济南三大历史名胜之一。位于山东省济南市历下区旧城区北部，是由济南众多泉水汇流而成，湖水经泺水河注入小清河。水面面积57.7公顷   ，水深平均2~3米。大明湖景色优美秀丽，湖水水色澄碧，是国家5A级旅游景区——天下第一泉风景区的核心组成部分之一。大明湖历史悠久，纪念古人政绩、行踪的建筑以及自然景观很多，诸如历下亭、铁公祠、小沧浪、北极阁、汇波楼、南丰祠、遐园、稼轩祠等，引得历代文人前来凭吊、吟咏。唐代以后的历代诗人，都留下了著名的诗篇。解放后，于1957年正式辟建成公园，几经修缮美化，大明湖逐渐成为济南市著名的游览胜地之一。')
  const leave = ref('红叶谷生态文化旅游区（Red Leaves Canyon）位于山东省济南市历城区仲宫街道锦绣川水库南3千米，距济南市区33千米，紧靠济泰高速锦绣川出口并与京福高速相连，有省道317线直达，景区占地4000余亩。红叶谷生态文化旅游区内植物品种繁多，有黄桢、红枫、紫藤等400余种，约百万余株，是一处以生态文化为主的近郊风景旅游区。红叶谷生态文化旅游区由百合园、樱花园、蔷薇园、绚秋湖、天趣苑、兴教寺、梅园、香巴拉休闲谷、欧洲风情谷与情人谷等十大专类园区组成，相互掩映，浑然天成，青山、秀水、灵泉、幽谷，被人们称为“江北九寨沟。” 2003年8月，红叶谷生态文化旅游区获国家AAAA级旅游景区。')
  </script>
  <style lang="scss" scoped>
  .container {
    background-color: aquamarine;
    height: 230vh;
    display: flex;
    flex-direction: column;

    .header {
      background-color: #FFFFFF;
      display: flex;
      flex-direction: row;
      height: 24vh;

      .titleBox {
        padding-left: 4vw;
        padding-top: 5vh;
        width: 40%;
        display: flex;
        flex-direction: column;

        .chineseImg {
          height: 50%;
          width: 35%;

        }

        .englishImg {
          height: 30%;
          width: 35%;
        }
      }

      .imgBox {
        height: 100%;
        width: 60%;
        overflow: hidden;
        background-size: 100% 100%;
        background-image: linear-gradient(to right, #FFFFFF, transparent), url('https://courseback.clankalliance.cn/static/inbuild/psh/散步.jpg');
        background-repeat: no-repeat;
      }
    }

    .menuBox {
      background-color: rgb(170, 104, 37);
      height: 8vh;

      .menu {
        height: 100%;
        padding-left: 10%;
        padding-right: 10%;
      }
    }

    .main {
      background-color: rgb(247, 249, 235);
      height: 190vh;
 /*      padding-left: 10vw;
      padding-right: 10vw; */

      .carouselBox {
    /*     background-color: coral; */
        height: 70vh;
        width: 100%;

        .carouselPage {
          height: 100%;
          width: 100%;

          .carouselItem {
            height: 100%;
            width: 100%;
          }
        }
      }

      .introductionBox {
        display: flex;
        flex-direction: row;
        background-color: rgb(196, 214, 248);
        height: 40vh;
        width: 100%;

        .imgIntroductionBox {
          /* background-color: aqua; */
          background-image: url('https://courseback.clankalliance.cn/static/inbuild/psh/济南.jpg');
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin-left: 3vw;
          margin-top: 4vh;
          height: 30vh;
          width: 40vw;
        }

        .textIntroductionBox {
          background-color: rgb(248, 226, 226);
          border-radius: 1vw;
          margin-left: 3vw;
          margin-top: 2vh;
          margin-right: 0.4vw;
          height: 90%;
          width: 45%;

          .introTitle {
            font-size: 2vw;
            margin-top: 1.5vh;
            margin-left: 2vw;
            padding-bottom: 0 !important;
            margin-bottom: 0.4vh !important;
            font-family: 微软雅黑;
          }

          .introText {
            /*  margin-block-start: 1em; */
            padding-top: 3vh;
            font-family: 微软雅黑;
            font-size: 1.1vw;
          }
        }
      }

      .content {
        background-color: #FFFFFF;
        display: flex;
        flex-direction: row;
        height: 80vh;
        width: 100%;

        .tourDiv {
          padding-top: 8vh;
          width: 25vw;
          height: 60vh;

          .tour {
            width: 25vw;
            height: 60vh;
          }
        }

        .contentSrollbar {
          height: 80vh;
          width: 52.5vw;
          padding-left: 0vw;
        }

        .cardBox {
          padding-top: 4vh;
          display: flex;
          flex-direction: row;
          padding-left: 0vw;

          .card {
            margin-left: 5vw;
            --el-card-padding: 0 !important;
            height: 33vh;
            width: 18vw;
            border-radius: 2vh;
            cursor: pointer;
            text-align: center;
            box-shadow: 0.1vw 0.1vw 0.1vw 0.1vw rgba(0, 0, 0, 0.2);
            .cardButton {
              border-radius: 2vh;
              margin: 0 !important;
              padding: 0 !important;
              height: 33vh;
              width: 18vw;
            }

            .cardImg {
              height: 27vh;
              width: 18vw;
            }

            .cardText {

              font-size: 1.2vw;
              font-weight: 550;
            }

          }

          .card:hover {
            box-shadow: 0.2vw 0.5vw 0.3vw 0.5vw rgba(48, 55, 66, 0.15) !important;
            transition: all 0.1s ease-in-out !important;
          }
        }
        .mobileTitle{
          font-size: 8vw;
          font-weight: 550;
          padding-left: 30vw;
          color: chocolate;
        }
        .cardMobile{
          /*   margin-left: 5vw; */
            --el-card-padding: 0 !important;
            height: 38vh;
            width: 80vw;
            border-radius: 2vh;
            cursor: pointer;
            text-align: center;
            margin-top: 4vh;
            box-shadow: 0.1vw 0.1vw 0.1vw 0.1vw rgba(0, 0, 0, 0.2);
            }
            .cardMobile:hover {
            box-shadow: 0.5vw 0.5vw 0.5vw 0.5vw rgba(48, 55, 66, 0.15) !important;
            transition: all 0.1s ease-in-out !important;
          }
            .cardImgMobile {
              height: 33vh;
              width: 80vw;
            }



      }

      .contentAttraction {
        background-color: #FFFFFF;
        height: 80vh;
        width: 100%;
        display: flex;
        flex-direction: column;

        .attractionScrollbar {
          padding-left: 2vw;
          padding-right: 2vw;

          .attractionTitle {
            padding-top: 0 !important;
            font-size: 2vw;
            padding-left: 0vw;
            padding-right: 3vw;
            color: black;
            font-weight: 600;
          }

          .attrIntro {
            font-size: 1vw;
            padding-right: 15vw !important;
          }
        }

        .divider {
          padding-bottom: 0vh;
          height: 3vh;
          background-color: rgb(58, 58, 209);
          width: 100%;
        }
      }
    }

    .footer {
      background-color: #545c64;
      height: 10vh;

      .copyright {
        font-size: 1.3vw;
        padding-top: 2vh;
        padding-right: 13vw;
        float: right;
        font-family: 宋体;
        color: #faefef;
      }
    }

  }
  </style>
