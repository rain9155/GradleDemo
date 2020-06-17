package com.example.plugin

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.model.*

/**
 * 自定义插件
 */
class MyPlugin implements Plugin<Project>{

	@Override
	void apply(Project project){
		/**
		 * 通过project的ExtensionContainer的create方法创建OuterExt实例
		 * 这样就可以在apply plugin的脚本中使用Gradle DSL为这个plugin配置属性值
		 */
		def outerExt = project.extensions.create('outerExt', OuterExt.class)

		project.task('showExt'){
			doLast{
				println "outerExt = $outerExt, innerExt = ${outerExt.innerExt}"
			}
		}
	}

	/**
	 * 插件的扩展属性
	 */
	static class OuterExt{
		def name
		def message
		//嵌套类
	    private InnerExt innerExt

	    public InnerExt getInnerExt(){
	    	return innerExt
	    }

	    @javax.inject.Inject
	    public OuterExt(ObjectFactory objectFactory){
	    	//通过objectFactory创建嵌套类实例
	    	innerExt = objectFactory.newInstance(InnerExt.class)
	    }

	    //定义嵌套Dsl的名字
	    void inner(Action<InnerExt> action){
	     	action.execute(innerExt)
	    }

		@Override
		String toString(){
			return "[name = $name, message = $message]"
		}

		static class InnerExt{
			def name
			def message

			@Override
			String toString(){
				return "[name = $name, message = $message]"
			}
		}
	}
}