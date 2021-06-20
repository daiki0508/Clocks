#include <jni.h>
#include <string>
#include <cstring>
//
// Created by oocha on 2021/06/19.
//

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_websarva_wings_android_clocks_LicenseActivity_getAESDataUrl(JNIEnv *env,
                                                                                     jobject thiz,
                                                                                     jint flag) {
    // TODO: implement getAESDataUrl()
    std::string ret_data;
    switch (flag) {
        case 0:
            ret_data = "/R8eMHuCvlUdeHqEvqrMnRFhyhHYOe6Qa76mxscPaQ1FAszd7XBAfM59VQAGHWE7WNrQG2P3J6cEfqXtGKMkOg==";
            break;
        case 1:
            ret_data = "ZHNYamFZa2NkdXNkZG1jSllkdGJ4WFlweWdCSDdqS1g=";
            break;
        case 2:
            ret_data = "9BSBvocLwR4USPPJhesJpQ==";
            break;
        default:
            ret_data = "Error";
            break;
    }
    return env->NewStringUTF(ret_data.c_str());
}