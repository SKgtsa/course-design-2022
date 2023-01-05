import router from "@/router";
import {setManager} from "@/global/global";

export const identityJump = (identity: number) => {
    switch (identity){
        case 0:
            setManager(false)
            router.push('/Student/Main').then()
            break;
        case 1:
            setManager(false)
            router.push('/Teacher/Main').then()
            break;
        case 2:
            setManager(true)
            router.push('/Manager/Main').then()
            break;
    }
}
