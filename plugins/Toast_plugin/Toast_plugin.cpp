#include "Toast_plugin.hpp"
#include <jni.h>
#include <android/log.h>

#define TAG "Toast_plugin"

extern "C" void OnPluginCreate(JNIEnv *env, jobject context)
{
    __android_log_print(ANDROID_LOG_INFO, TAG, "OnPluginCreate вызван");

    jclass toastClass = env->FindClass("android/widget/Toast");
    if (!toastClass)
    {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Toast класс не найден");
        return;
    }

    jmethodID makeText = env->GetStaticMethodID(toastClass, "makeText",
                                                "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;");
    if (!makeText)
    {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Метод makeText не найден");
        return;
    }

    jstring text = env->NewStringUTF("Привет из плагина!");
    jobject toast = env->CallStaticObjectMethod(toastClass, makeText, context, text, 0);

    jmethodID show = env->GetMethodID(toastClass, "show", "()V");
    if (show && toast)
    {
        env->CallVoidMethod(toast, show);
        __android_log_print(ANDROID_LOG_INFO, TAG, "Toast показан");
    }
    else
    {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Toast не удалось показать");
    }
}
