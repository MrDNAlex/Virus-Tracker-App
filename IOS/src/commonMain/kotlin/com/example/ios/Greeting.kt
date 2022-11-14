package com.example.ios

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}