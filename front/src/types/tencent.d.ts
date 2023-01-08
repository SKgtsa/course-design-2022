interface Tencent {
    TencentCaptcha(CaptchaAppId: String, callback: Function, options?: Object);
}

declare module "tencent" {
    export = tencent;
}
declare var tencent: Tencent;
