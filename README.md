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

## 📦 Возможности

- 🔌 Загрузка `.so`-плагинов в рантайме через `dlopen`
- ☕️ Вызов `OnPluginCreate(JNIEnv*, jobject)` из плагина
- 📤 Расширяемость без перекомпиляции приложения

---

## ⚙️ Как это работает

1. Приложение ищет `.so`-файлы в директории:
   storage/emulated/0/Android/data/com.all1eexxx.dynamicnativepluginloaderforandroid/files/plugins, копирует их во внутрешнее хранилище приложения.


2. Загружает их через `dlopen`
3. Ищет символ `OnPluginCreate`
4. Вызывает его с `JNIEnv*` и `Activity context`

---

## ⚙️ Как это устроено

```txt
📁 storage/emulated/0/Android/data/com.all1eexxx.dynamicnativepluginloaderforandroid/files/plugins
   ├── plugin1.so
   ├── plugin2.so
   └── ...
   ```

📌 Каждый .so должен экспортировать:
    extern "C" void OnPluginCreate(JNIEnv* env, jobject activity);

---

## ⚠️ Ограничения
Нельзя обрабатывать нажатия кнопок напрямую из C++

Используется фиксированное имя точки входа (OnPluginCreate)

Все плагины исполняются в контексте одного процесса

## 📄 Лицензия
Проект распространяется под лицензией MIT. Используй, исследуй, дополняй.

<div align="center">
⭐️ Не забудь поставить звезду, если тебе понравился проект!

</div> 