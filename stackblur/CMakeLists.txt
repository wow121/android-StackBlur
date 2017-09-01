# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.


cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")


add_library( stackblur-lib SHARED src/main/jni/com_xianrui_stackblur_NativeBlurProcess.cpp)

find_library(log-lib log)

target_link_libraries(stackblur-lib android log $\{log-lib})
