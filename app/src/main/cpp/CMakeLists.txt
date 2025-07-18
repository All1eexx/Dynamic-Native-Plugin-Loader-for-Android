cmake_minimum_required(VERSION 4.0.2)

project("plugin_loader")

add_library(plugin_loader SHARED
        plugin_loader.cpp
)

target_link_libraries(plugin_loader
        android
        log
        dl
)
