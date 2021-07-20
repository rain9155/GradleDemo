package com.example.plugin


import org.gradle.api.*
import org.gradle.api.model.*
import javax.inject.Inject

/**
 * 自定义插件
 */
class MyPlugin implements Plugin<Project>{

	@Override
	void apply(Project project){

		//通过project的ExtensionContainer的create方法创建一个名为outerExt的扩展，扩展对应的类为OuterExt
		OuterExt outerExt = project.extensions.create('outerExt', OuterExt.class, "")

		project.task('showExt'){
			doLast{
				println "outerExt = ${outerExt}, innerExt = ${outerExt.innerExt}"
			}
		}

		//通过project的ObjectFactory的domainObjectContainer方法创建OuterExt的Container实例
		NamedDomainObjectContainer<OuterExt> outerExtContainer = project.objects.domainObjectContainer(OuterExt.class)

		//然后再通过project的ExtensionContainer的add方法添加名称和OuterExt的Container实例的映射
		project.extensions.add('outerExts', outerExtContainer)

		project.task('showExts'){
			doLast{
				outerExtContainer.each{ext ->
					println "${ext.name}: outerExt = ${ext}, innerExt = ${ext.innerExt}"
				}
			}
		}
	}

	/**
	 * 插件扩展对应的类
	 */
	static abstract class OuterExt{

		String message

		//嵌套类
		InnerExt innerExt

		//定义一个使用@Inject注解的、抽象的获取ObjectFactory实例的get方法
		@Inject
		abstract ObjectFactory getObjectFactory()

		//NamedDomainObjectContainer要求它的元素必须要有一个只可读的、名为name的常量字符串
		private final String name

		//只可读的name表示name要私有的，并且提供一个get方法，name的值在构造函数中注入
		String getName(){
			return this.name
		}

		//通过@Inject注解带有String类型参数的构造
		@Inject
		OuterExt(String name){
			//在构造中为name赋值
			this.name = name
			//通过ObjectFactory的newInstance方法创建嵌套类innerExt实例
			this.innerExt = getObjectFactory().newInstance(InnerExt.class)
		}

		//定义一个方法，方法名为可以随意起，方法的参数类型为Action，泛型类型为嵌套类InnerExt
		void inner(Action<InnerExt> action){
			//调用Action的execute方法，传入InnerExt实例
			action.execute(innerExt)
		}

		@Override
		String toString(){
			return "[message = ${message}]"
		}

		/**
		 * 嵌套类
		 */
		static class InnerExt{

			String message

			@Override
			String toString(){
				return "[message = ${message}]"
			}
		}
	}
}