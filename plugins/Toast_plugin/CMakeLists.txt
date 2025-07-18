cmake_minimum_required(VERSION 4.0.2)

project("Toast_plugin")

add_library(Toast_plugin SHARED
    Toast_plugin.cpp
)

target_link_libraries(Toast_plugin
    log
)

set(NDK_PATH "D:/android-sdk/ndk/29.0.13599879")
include_directories(
    ${NDK_PATH}/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include
    ${NDK_PATH}/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/android
)