import { createStore } from 'vuex'


export default createStore({
    state: {
        avatarURL:  'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg',
        baseURL: 'http://localhost:5174',
        defaultAvatar: 'http://localhost:5174/static/file/BFD9E6FC-7AAB-4821-B769-12DB9779F90F.jpg',
    },
    getters: {
        avatarURL(state) {
            return state.avatarURL
        },
        baseURL(state) {
            return state.baseURL
        },
        defaultAvatar(state) {
            return state.defaultAvatar
        },
    },
    mutations: {
        // increase(state, { payload = 10 }) {
        //     state.num += payload
        // },
        // setCarList(state, { payload }) {
        //     return state.carList = payload
        // },
        // delList(state, index) {
        //     state.carList.splice(index, 1);
        // },
        // updateUserInfo(state, obj) {
        //     state.userInfo = obj
        //     console.log("存储成功",state)
        // },

    },
    actions: {
        // getActionList(context) {
        //     // 模拟异步请求
        //     setTimeout(() => {
        //         context.commit("setCarList", { payload: [{ carName: "极氪001", price: 28, range: 620 },] })
        //     }, 1000)
        // },
        // delCarList(context,index) {
        //     setTimeout(() => {
        //         //触发mutations中的方法 变更状态
        //         context.commit("delList", index);
        //     }, 1000)
        // }
    },
    modules: {
    }
});
