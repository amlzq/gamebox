#!/bin/sh
gradle build
adb install build/outputs/apk/app-debug.apk
