#include <jni.h>
#include <string>
#include <dlfcn.h>
#include <android/log.h>

#define TAG "PluginLoader"

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_all1eexxx_dynamicnativepluginloaderforandroid_SoPluginLoader_loadPlugin(
        JNIEnv *env, jclass, jobject context, jstring path) {

    const char *cpath = env->GetStringUTFChars(path, nullptr);
    __android_log_print(ANDROID_LOG_INFO, TAG, "Loading plugin: %s", cpath);

    void *handle = dlopen(cpath, RTLD_NOW);
    if (!handle) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "dlopen failed: %s", dlerror());
        env->ReleaseStringUTFChars(path, cpath);
        return JNI_FALSE;
    }

    typedef void (*PluginEntry)(JNIEnv*, jobject);
    auto onCreate = (PluginEntry) dlsym(handle, "OnPluginCreate");

    if (onCreate) {
        __android_log_print(ANDROID_LOG_INFO, TAG, "Found OnPluginCreate entry point in %s. Calling it now.", cpath);
        onCreate(env, context);
        dlclose(handle);
        env->ReleaseStringUTFChars(path, cpath);
        return JNI_TRUE;
    } else {
        __android_log_print(ANDROID_LOG_WARN, TAG, "OnPluginCreate not found in %s", cpath);
        dlclose(handle);
        env->ReleaseStringUTFChars(path, cpath);
        return JNI_FALSE;
    }
}