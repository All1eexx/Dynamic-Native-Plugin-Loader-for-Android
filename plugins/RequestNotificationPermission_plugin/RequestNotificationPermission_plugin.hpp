#ifndef REQUEST_NOTIFICATION_PERMISSION_PLUGIN_HPP
#define REQUEST_NOTIFICATION_PERMISSION_PLUGIN_HPP

#include <jni.h>

extern "C"
{
  void OnPluginCreate(JNIEnv *env, jobject context);
}

#endif
