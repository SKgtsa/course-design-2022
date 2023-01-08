import {showGlobalRouter} from "@/global/global";
import {nextTick} from "@vue/runtime-core";

/**
 * 刷新页面
 */
export const reload = () => {
    showGlobalRouter.value = false;
    nextTick(() => {
        showGlobalRouter.value = true;
    })
}
