
  <!--  查看个人成绩，班级和专业内排名，点击查看排名？
  放到一张大表里，学生只能查看分数 -->
<template>
  <div class="mainArea">
    <div class="leftWindow">
      <div class="panel">
      </div>
    </div>
    <div class="rightWindow">
      <div class="mainCard">
      </div>
    </div>
 


  </div>
</template>

<script lang="ts" setup>

</script>

<style lang="scss" scoped>
.mainArea {
  display: flex;
  flex-direction: row;
  /* flex-direction: row; */
  width: 100%;
  height: 100vh;

  .leftWindow {
    display: flex;
    width: 15vw;

    .panel {
      display: flex;
      width: 10vw;
      height: 80%;
    }
  }

  .rightWindow {
/*     width: 80vw; */
    padding-left: 2vw;
    padding-top: 1vh;
    display: flex;


    .mainCard {
      //HomePage中是逆序排列
      background-color: #FFFFFF;
      margin-top: 4vh;
      margin-left: 2vw;
      border-radius: 2vw;
      width: 74vw;
      height: 80vh;
      border-radius: 2vw;
      box-shadow: 0 0 10px 0 #b9ccee;
    }
  }
}
</style>
  