package com.example.task

import org.gradle.api.*
import org.gradle.api.tasks.*

/**
 * 自定义Task
 */
class MyTask extends DefaultTask{

 	def message = 'hello world from myTask'

 	@TaskAction
 	def println1(){
 		println "println1: $message"
 	}

 	@TaskAction
 	def println2(){
 		println "println2: $message"
 	}
 	
 }