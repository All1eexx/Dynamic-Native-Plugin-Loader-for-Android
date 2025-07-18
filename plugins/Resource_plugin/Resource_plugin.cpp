#include "Resource_plugin.hpp"
#include <jni.h>
#include <android/log.h>
#include <fstream>
#include <sstream>
#include <string>
#include <cstring>

#define TAG "ResourcePlugin"

std::string getPluginDir(JNIEnv *env, jobject context)
{
    jclass contextClass = env->GetObjectClass(context);
    jmethodID getFilesDir = env->GetMethodID(contextClass, "getFilesDir", "()Ljava/io/File;");
    jobject fileObject = env->CallObjectMethod(context, getFilesDir);

    jclass fileClass = env->GetObjectClass(fileObject);
    jmethodID getAbsolutePath = env->GetMethodID(fileClass, "getAbsolutePath", "()Ljava/lang/String;");
    jstring pathString = (jstring)env->CallObjectMethod(fileObject, getAbsolutePath);

    const char *cpath = env->GetStringUTFChars(pathString, nullptr);
    std::string result = std::string(cpath);
    env->ReleaseStringUTFChars(pathString, cpath);
    return result;
}

std::string readJsonFile(const std::string &path)
{
    std::ifstream file(path);
    if (!file.is_open())
        return "";

    std::stringstream buffer;
    buffer << file.rdbuf();
    return buffer.str();
}

// Примитивный парсинг JSON (только для "ключ": "значение")
std::string extractJsonValue(const std::string &json, const std::string &key)
{
    size_t keyPos = json.find("\"" + key + "\"");
    if (keyPos == std::string::npos)
        return "";

    size_t colon = json.find(":", keyPos);
    if (colon == std::string::npos)
        return "";

    size_t quoteStart = json.find("\"", colon);
    size_t quoteEnd = json.find("\"", quoteStart + 1);

    if (quoteStart == std::string::npos || quoteEnd == std::string::npos)
        return "";

    return json.substr(quoteStart + 1, quoteEnd - quoteStart - 1);
}

extern "C" void OnPluginCreate(JNIEnv *env, jobject context)
{
    __android_log_print(ANDROID_LOG_INFO, TAG, "OnPluginCreate вызван");

    std::string basePath = getPluginDir(env, context);
    std::string resPath = basePath + "/plugins/res/data.json";

    __android_log_print(ANDROID_LOG_INFO, TAG, "Чтение JSON: %s", resPath.c_str());

    std::string jsonContent = readJsonFile(resPath);
    if (jsonContent.empty())
    {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Не удалось прочитать JSON");
        return;
    }

    std::string message = extractJsonValue(jsonContent, "message");

    if (!message.empty())
    {
        __android_log_print(ANDROID_LOG_INFO, TAG, "Сообщение из JSON: %s", message.c_str());

        // Показать Toast
        jclass toastClass = env->FindClass("android/widget/Toast");
        jmethodID makeText = env->GetStaticMethodID(toastClass, "makeText",
                                                    "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;");
        jmethodID show = env->GetMethodID(toastClass, "show", "()V");

        jstring jMessage = env->NewStringUTF(message.c_str());
        jobject toast = env->CallStaticObjectMethod(toastClass, makeText, context, jMessage, 0);
        env->CallVoidMethod(toast, show);
    }
    else
    {
        __android_log_print(ANDROID_LOG_WARN, TAG, "Поле 'message' не найдено в JSON");
    }
}
