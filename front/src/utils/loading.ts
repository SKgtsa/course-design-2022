import { ElLoading } from 'element-plus';

let loadingCount = 0;
let loading;

const startLoading = () => {
  loading = ElLoading.service({
    lock: true,
    text: '拼命加载中...',//可以自定义文字
    spinner:'el-icon-loading',//自定义加载图标类名
    background: 'rgba(50, 50, 50, 0.3)'//遮罩层背景色
  });
};

const endLoading = () => {
  loading.close();
};

export const showLoading = () => {
  if (loadingCount === 0) {
    startLoading();
  }
  loadingCount += 1;
};

export const hideLoading = () => {
  if (loadingCount <= 0) {
    return;
  }
  loadingCount -= 1;
  if (loadingCount === 0) {
    endLoading();
  }
};
