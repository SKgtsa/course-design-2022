<template>
  <div class="mainPage" style="scroll-behavior: smooth">
    <el-backtop :right="30" :bottom="80" />
    <el-affix :offset="0">
      <el-header class="el-header">
        <img class="logo" src="https://courseback.clankalliance.cn/static/inbuild/jmz/在哲logo.png">
        <el-button link class="linkButton">
          <a href="#photography" style="color: white">摄影作品</a>
        </el-button>
<!--        <el-button link class="linkButton" style="margin-right: 1.5vh">-->
<!--          <a href="#afterClass" style="color: white;scroll-behavior: smooth">课余生活</a>-->
<!--        </el-button>-->
        <el-button link class="linkButton">
          <a href="#studentUnion" style="color: white">学生工作</a>
        </el-button>
        <el-button link class="linkButton" >
          <a href="#selfPre" style="color: white;">关于我</a>
        </el-button>
      </el-header>
    </el-affix>
    <div  class="carouselBack">
      <el-carousel :interval="4000" indicator-position="outside" pause-on-hover arrow="never" type="card"
                   class="carousel">
        <el-carousel-item class="carouselPages">
          <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/校园.jpg">
        </el-carousel-item>
        <el-carousel-item class="carouselPages">
          <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/夜景.jpg">
        </el-carousel-item>
        <el-carousel-item class="carouselPages">
          <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/落.jpg">
        </el-carousel-item>
        <el-carousel-item class="carouselPages">
          <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/环卫工人.jpg">
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="selfPre" id="selfPre">
      <div style="float: top;width: 100%;text-align: center;padding-top: 20vh">
        <a style="font-size: 6vh;font-family: 微软雅黑,serif;font-weight: 700;color: black" >关于我</a>
      </div>
      <div style="float: left;padding-left: 30vh;padding-top: 6vh">
        <span style="font-size: 5vh;font-family: 微软雅黑,serif;font-weight: 700;color: black">
          姜木子 :
        </span>
        <p style="font-size: 4vh;width: 90vh;line-height: 6vh;font-family: 微软雅黑,serif;font-weight: 700;color: gray;word-wrap: break-word">
          山东大学软件学院软件工程专业大二学生，现任软件学院学生会权益中心部长。
          爱好摄影、滑板、羽毛球。曾获软件学院优秀学生干部，优秀共青团员，山东大学数学建模竞赛三等奖，宿舍文化节一等奖等荣誉。
        </p>
      </div>
      <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/证件照.jpg" class="selfPhoto">
      <el-divider/>
    </div>
    <div class="studentUnion" id="studentUnion">
      <div style="float: left;height: 100vh">
        <div style="padding-top: 20vh;padding-left: 10vh">
          <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/权益菌.jpg" style="height: 20vh;float: left;margin-right: 5vh">
          <p style="font-size: 5vh;font-family: 微软雅黑,serif;font-weight: 700;color: gray;padding-left: 6vh;float: left">
            软件学院 权益中心
          </p>
        </div>
        <div style="padding-left: 10vh;padding-top: 20vh;margin-right: 3vh">
          <p style="font-size: 3vh;width: 90vh;line-height: 5vh;font-family: 微软雅黑,serif;font-weight: 700;color: gray;word-wrap: break-word;">
            “传心声，诠心意”，我们坚持从同学中来，到同学中去的工作方法，推行校园提案制度，完善“横向沟通，纵向联动”的工作机制，
            依托“软院权益菌”QQ公众平台广泛听取同学心声。部门内部设置答疑组，食堂物业组，宣传组。答疑组主要工作是通过“软院权益菌”为同学们进行答疑，
            、报销方式及时间、图书馆打印方法等各种生活问题；食堂物业组的工作是听取同学们对食堂以及校区办的意见和建议，并向相关部门进行反馈，
            与负责人进行沟通交流，并在必要时开展相关座谈会，尽最大可能满足同学们提出的合理需求。宣传组，便是将座谈会反馈情况、工作总结等内容，
            写成宣传稿或做成精美的推文面向同学反馈提案进度及工作成效。
          </p>
        </div>
      </div>
      <el-divider direction="vertical" style="height: 100vh;float: left;"/>
      <div style="float: left;height: 100vh;width: 90vh">
        <el-carousel :interval="4000"  pause-on-hover arrow="never"
                     class="carousel2">
          <el-carousel-item class="carouselPages">
            <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/权益中心合照.jpg">
          </el-carousel-item>
          <el-carousel-item class="carouselPages">
            <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/权益中心合照2.jpg">
          </el-carousel-item>
          <el-carousel-item class="carouselPages">
            <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/蛋糕.jpg">
          </el-carousel-item>
          <el-carousel-item class="carouselPages">
            <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/圣诞节.jpg">
          </el-carousel-item>
          <el-carousel-item class="carouselPages">
            <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/座谈会1.jpg">
          </el-carousel-item>
          <el-carousel-item class="carouselPages">
            <img class="carouselPhoto" src="https://courseback.clankalliance.cn/static/inbuild/jmz/座谈会2.jpg">
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <div class="photography" id="photography">
      <div style="height: 30vh;"></div>
      <div class="box">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/校园.jpg" alt="" class="img1">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/书架.jpg" alt="" class="img2">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/夜景.jpg" alt="" class="img3">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/夜景2.jpg" alt="" class="img4">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/夜景3.jpg" alt="" class="img5">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/景深.jpg" alt="" class="img6">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/校门.jpg" alt="" class="img7">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/环卫工人.jpg" alt="" class="img8">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/落日.jpg" alt="" class="img9">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/窗.jpg" alt="" class="img10">
      </div>
    </div>
    <el-footer class="footer">
      <div class="bottomCell">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/电话.png" class="bottomImage">
        <div style="float: right;margin-top:1vh;margin-left: 1vh">
          <a style="font-family: 'Microsoft YaHei',serif;">TEL:</a><br>
          <a style="font-family: 'Microsoft YaHei',serif">188****1226</a>
        </div>
      </div>
      <div class="bottomCell">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/邮箱.png" class="bottomImage">
        <div style="float: right;margin-top:1vh;margin-left: 1vh">
          <a style="font-family: 'Microsoft YaHei',serif">eMail:</a><br>
          <a style="font-family: 'Microsoft YaHei',serif">zaizhe@163.com</a>
        </div>
      </div>
      <div class="bottomCell">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/微信.png" class="bottomImage">
        <div style="float: right;margin-top:1vh;margin-left: 1vh">
          <a style="font-family: 'Microsoft YaHei',serif">WeChat:</a><br>
          <a style="font-family: 'Microsoft YaHei',serif">jmz03128</a>
        </div>
      </div>
      <div class="bottomCell">
        <img src="https://courseback.clankalliance.cn/static/inbuild/jmz/qq.png" class="bottomImage">
        <div style="float: right;margin-top:1vh;margin-left: 1vh">
          <a style="font-family: 'Microsoft YaHei',serif">QQ:</a><br>
          <a style="font-family: 'Microsoft YaHei',serif">3112948409</a>
        </div>
      </div>
      <div style="padding-top: 20vh;text-align: center;font-size:2.5vh;font-family: 微软雅黑">Copyright © 2023 Muzi Jiang. All rights reserved.</div>
    </el-footer>
  </div>

</template>

<script lang="ts" setup>
</script>

<style  lang="scss" scoped>
.box{
  width: 120vh;
  height: 56vh;
  margin: 0 auto;
  position: relative;
}
.box>img{
  width:26vh;
  border: 1vh solid #ddd;
  padding: 1vh;
  background-color: #fff;
  position: absolute;
  transition: all .5s;
}
.img1{
  top: 0;
  left: 47vh;
  transform: rotate(5deg);
}
.img2{
  top: 0;
  right: 16vh;
  transform: rotate(-15deg);
}
.img3{
  top: 0;
  right: -4vh;
  transform: rotate(10deg);
}
.img4{
  top:30vh;
  right:46vh;
  transform: rotate(-10deg);
}
.img5{
  top: 32vh;
  left: 0;
  transform: rotate(10deg);
}
.img6{
  top: 0;
  left: 0;
  transform: rotate(-10deg);
}
.img7{
  top: 33vh;
  right: -3vh;
  transform: rotate(15deg);
}
.img8{
  /*width: 180px;*/
  bottom: 1vh;
  left: 28vh;
  transform: rotate(20deg);
}
.img9{
  bottom: 0vh;
  left: 69vh;
  transform: rotate(15deg);
}
.img10{
  top: 7vh;
  left: 23vh;
  transform: rotate(-10deg);
}
.box>img:hover{
  box-shadow: 10px 10px 10px rgba(0,0,0,.1);
  transform: scale(2.8);//放大
  z-index: 666;	//调整堆叠顺序，取值为0-999，
}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
.mainPage {
  background-color: white;
  scroll-behavior: smooth;
}
.bottomImage{
  height: 8vh;
}
.selfPhoto{
  height: 40vh;
  float: right;
  padding-top: 15vh;
  padding-right: 30vh;
}
.bottomCell{
  padding-top: 6vh;
  float: left;
  padding-left: 18vh;
}
.footer{
  height: 30vh;
  background-color: gray;
}
.logo {
  padding-top: 1.5vh;
  height: 9vh;
  width: auto;
  object-fit: cover;
  float: left;
  padding-left: 8vh;
}

.linkButton {
  float: right;
  height: 8vh;
  margin-top: 2vh;
  width: auto;
  font-size: 3vh;
  font-family: "Microsoft YaHei", serif;
  color: white;
  font-weight: bold;
  padding-right: 1vh;
  /*border-bottom:1vh solid white;*/

}
.linkButton:hover{
  color: #99CCCC;
  font-size: 4.5vh;
}
.carousel {
  height: 88vh;
  width: 100%;
}
.carousel2{
  height: 88vh;
  padding-top: 12vh;
}
.carouselBack{
  background-color: #949AA6 ;
}
.selfPre{
  height: 100vh;
  width: 100%;
  background-color:  #F2EFC2;
}
.studentUnion{
  height: 100vh;
  width: 100%;
  background-color: #ffffff;
}
.afterClass{
  height: 100vh;
  width: 100%;
  background-color:  #33669977;
}
.photography{
  height: 100vh;
  width: 100%;
  background-color:#D9C7B8;
}
.carouselPages {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 88vh;
}

.carouselPhoto {
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
}

.el-header {
  background-color: #5D6C8C;
  height: 12vh;
}
</style>
