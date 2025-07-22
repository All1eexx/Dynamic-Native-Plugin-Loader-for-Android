<div align="center">


# 🧩 Dynamic Native Plugin Loader for Android (.so)

![Native Plugin System](https://img.shields.io/badge/Native_Plugin_Loader-3ddc84?style=for-the-badge&logo=android&logoColor=white&color=121212&labelColor=3ddc84)

</div>

<p align="center">
  <a href="#"><img alt="License" src="https://img.shields.io/badge/LICENSE-MIT-blueviolet?style=flat-square&logo=opensourceinitiative&labelColor=282c34"></a>
  <a href="#"><img alt="Platform" src="https://img.shields.io/badge/Platform-Android-3ddc84?style=flat-square&logo=android&logoColor=white&labelColor=282c34"></a>
  <a href="#"><img alt="Language" src="https://img.shields.io/badge/C++-Native-00599C?style=flat-square&logo=c%2B%2B&logoColor=white&labelColor=282c34"></a>
  <a href="#"><img alt="Min API" src="https://img.shields.io/badge/API-21+-00B0FF?style=flat-square&logo=android-studio&logoColor=white&labelColor=282c34"></a>
  <a href="#"><img alt="JNI" src="https://img.shields.io/badge/Interface-JNI-orange?style=flat-square&logo=java&logoColor=white&labelColor=282c34"></a>
</p>

<p align="center">
  <a href="#"><img alt="Release" src="https://img.shields.io/github/v/release/All1eexx/Dynamic-Native-Plugin-Loader-for-Android?include_prereleases&style=flat-square&color=FF6D00&logo=github&logoColor=white&labelColor=282c34"></a>
  <a href="#"><img alt="Downloads" src="https://img.shields.io/github/downloads/All1eexx/Dynamic-Native-Plugin-Loader-for-Android/total?style=flat-square&color=4CAF50&logo=download&labelColor=282c34"></a>
  <a href="#"><img alt="Last commit" src="https://img.shields.io/github/last-commit/All1eexx/Dynamic-Native-Plugin-Loader-for-Android?style=flat-square&color=slateblue&logo=git&labelColor=282c34"></a>
  <a href="#"><img alt="Stars" src="https://img.shields.io/github/stars/All1eexx/Dynamic-Native-Plugin-Loader-for-Android?style=flat-square&color=FFD700&logo=star&labelColor=282c34"></a>
</p>

---

## 📦 Features
- 🔌 Runtime loading of .so plugins via dlopen
- ☕️ Calls OnPluginCreate(JNIEnv*, jobject) from plugins
- ✅ Enables Android UI creation from C++
- 🧠 Uses JNI for Android UI interaction
- 🖼️ Sample GUI plugin: TextView and Button from C++
- 📤 Extensibility without recompiling the main app
- 🗃️ Working with any files in the plugins folder (e.g., .json, .xml)
- 🔐 Permissions can be requested

---

## ⚙️ How It Works
1. The app searches for .so files in:
storage/emulated/0/Android/data/com.all1eexxx.dynamicnativepluginloaderforandroid/files/plugins
and copies them to internal storage.
2. Loads them via dlopen
3. Looks for the OnPluginCreate symbol
4. Calls it with JNIEnv* and Activity context

---

## ⚙️ Architecture

```txt
📁 storage/emulated/0/Android/data/com.all1eexxx.dynamicnativepluginloaderforandroid/files/plugins
   ├── plugin1.so
   ├── plugin2.so
   └── ...
   ```

📌 Each .so must export:
    extern "C" void OnPluginCreate(JNIEnv* env, jobject activity);

---

## 🔔 Sample Plugins

- [**Toast_plugin**](https://github.com/All1eexx/Dynamic-Native-Plugin-Loader-for-Android/tree/1.0.0.1/plugins/Toast_plugin)  
  Displays toast notifications.
- [**GUI_plugin**](https://github.com/All1eexx/Dynamic-Native-Plugin-Loader-for-Android/tree/1.0.0.2/plugins/GUI_plugin)  
  Creates text and button UI (without click handling).
- [**Resource_plugin**](https://github.com/All1eexx/Dynamic-Native-Plugin-Loader-for-Android/tree/1.1.0.1/plugins/Resource_plugin)  
  Plugin support for any file type.
- [**RequestNotificationPermission_plugin**](https://github.com/All1eexx/Dynamic-Native-Plugin-Loader-for-Android/tree/1.1.2.1/plugins/RequestNotificationPermission_plugin)  
  that plugin requests permission to send messages

---

## ⚠️ Limitations
1. Button click handling cannot be done directly from C++
2. Fixed entry point name (OnPluginCreate)
3. All plugins run in the same process context

## 📄 License
MIT License. Use, explore, contribute.

<div align="center">
⭐️ Don't forget to star if you like the project!

</div> 