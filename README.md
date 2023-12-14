# LLog
An easily expandable log library in Android.
- Uses a chain of responsibility approach to transmit log messages.
- Can use method name as Tag when you don't specify a tag.

[![](https://jitpack.io/v/graveyard233/LLogUtil.svg)](https://jitpack.io/#graveyard233/LLogUtil)

## Import
In project gradle (maybe it called settings.gradle.kts) add jitpack
```kotlin
dependencyResolutionManagement {
    // ...
    repositories {
        // ...
        maven(url = "https://www.jitpack.io/")
    }
}
```

then, add dependencies in the project (Please refer to the latest version number for release)
```kotlin
implementation("com.github.graveyard233:LLogUtil:LatestVersion")
```

## Usage
You need init LLog before use it. I like init LLog by StartUp.
```kotlin
LLog.apply {
    setDebug(methodNameEnable = true)// If you don't set tag,llog will use methodName as default tag.
    addInterceptor(LogcatInterceptor())// Use Android Log to print log
    addInterceptor(LinearInterceptor())// Use channel to ensure that log printing is linear output
    addInterceptor(PackToLogInterceptor())// Just pack interceptor
    addInterceptor(
        // If you want to add write function,you need implement BaseLogWriter.
        // You can copy DefaultWriter in demo or customize it.
        WriteInInterceptor(
            logWriter = LogDefaultWriter(
                formatStrategy = LogWriteDefaultFormatStrategy(),
                diskStrategy = FileLogDiskStrategyImpl(
                    logDirectory = context.getExternalFilesDir("log")!!.absolutePath,
                    logFileStoreSizeOfMB = 2,
                    logFileMaxNumber = 4
                )
            )
        )
    )
}
```

After init, you can use it anywhere.
```kotlin
// normal message
LLog.i(tag = "TAG", msg = "Test a log")
LLog.d(msg = "something log")// LLog will use method name for default tag
// Print the exception
try {
    val a = 1 / 0
} catch (ex :Exception){
    LLog.e(msg = ex)
}
```
For more details, please refer to the demo.

## Control print and write level
LLog use `minPrintPriority` and `maxPrintPriority` control log's print.(when you use LogcatInterceptor)
LLog use `minWritePriority` and `maxWritePriority` control log's write.(when you use or implement WriteInInterceptor)

## Custom your Interceptor
You can implement `Interceptor` to custom your Interceptor and log system.
Such as:Add a report interceptor(send logs to Remote after how many logs being print, just use your imagination)
And use `addInterceptor(interceptor: Interceptor<T>)` to expand your LLog.

## Thanks
LLog referenced other excellent open-source Android log libraries and blog.

[EasyLog](https://github.com/wisdomtl/EasyLog)
[Logcat](https://github.com/liangjingkanji/LogCat)
[OTLogger](https://github.com/oi-october/OTLogger)
[xLog](https://github.com/elvishew/xLog)
[logger](https://github.com/orhanobut/logger)

[The idea of constructing a log library](https://juejin.cn/post/7244002241845166138)
[The idea of printing log output classes](https://juejin.cn/post/6844904097020116999)