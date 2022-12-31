import router from "@/router";

export const identityJump = (identity: number) => {
    switch (identity){
        case 0:
            router.push('/Student/Main').then()
            break;
        case 1:
            router.push('/Teacher/Main').then()
            break;
        case 2:
            router.push('/Manager/Main').then()
            break;
    }
}
