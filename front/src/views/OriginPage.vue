<template>
</template>
<script setup lang="ts">
import service from '@/request';
import router from '@/router';
import { showLoading,hideLoading } from '@/utils/loading';
import { messageWarning, messageError, messageInfo, messageSuccess } from '@/utils/message';
let checkInf = async () => {
    showLoading();
    await service.post('/api/user/myInfo', { token: localStorage.getItem('token') }).then(res => {
        let data = res.data;
        if (data.success) {
            if(data.character==0){
                hideLoading();
                router.push('/Main')
            }
        } else {
            hideLoading();
            router.push('/Login')
        }
    })
        .catch(function (error) {
            hideLoading();
            messageError("服务器开小差了呢");
            console.log(error)
        })
}
checkInf();
</script>
<style lang="scss" scoped>
</style>