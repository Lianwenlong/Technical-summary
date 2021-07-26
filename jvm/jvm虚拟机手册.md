# JVM 知识

## 1. 内存结构

### 1.1 内存结构概览

![内存结构简图](../jvm/image/内存结构简图.png)

  <center style="font-size:20px;color:#1E90FF">图1.内存结构简图</center> 

![内存结详细图](../jvm/image/内存结构详细图.png)

  <center style="font-size:20px;color:#1E90FF">图2.内存结构详细图</center> 

### 1.2 类加载子系统

![类加载子系统](../jvm/image/类加载子系统.png)

  <center style="font-size:20px;color:#1E90FF">图3.类加载子系统</center>

负责从文件系统或者网络中加载Class文件,class文件在文件开头有特定的文件标识

类加载器(Class Loader)只负责class文件的加载,至于它是否可以运行,由执行引擎(Execution Engine)决定。

加载的**类信息**存放于一块称为**方法区**的内存空间。 除了类信息之外，方法区中还会存放**运行时常量池信息**，可能还包括字符串字面量和数字常量（这部分常量信息是class文件中常量池部分的内存映射）

- 加载

  1. 通过一个类的全限定名获取定义此类的二进制字节流
  2. 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构
  3. **在内存中生成一个代表这个类的java.lang.Class对象**，作为方法区这个类的各种数据的访问入口

- 链接

  1. 验证(Verify):

     主要包含四种验证, **文件格式验证，元数据验证，字节码验证，符号引用验证**。

     目的在于确保class文件的字节流中包含信息符合当前虚拟机要求，保证被加载类的正确性，不会危害虚拟机自身安全。

  2. 准备(Prepare):

     为**类变量**分配内存并设置该类变量的**默认初始值，即零值**。

     ```java
     public class Demo{
         private static int a = 1; // 此阶段(prepare): a = 0 
         // Initialization阶段才赋值 a = 1
     }
     ```

     ***这里不包含用final修饰的static，因为final修饰此时是个常量，final在编译的时候就会分配了，准备阶段会显示初始化***

     这里不会为实例变量分配初始化值，类变量会分配在方法区中，而实例变量是会随着对象一起分配到java堆中。

  3. 解析(Resolve):

     **将常量池内的符号引用转换为直接引用的过程。**

     **事实上，解析操作往往会伴随着jvm在执行完初始化之后再执行。**

     符号引用就是一组符号来描述所引用的目标。符号引用的字面量形式明确定义在《java虚拟机规范》的class文件格式中。直接引用就是直接指向目标的指针、相对偏移量或者一个间接定位到目标的句柄。

     解析动作主要针对类或者接口、字段、类方法、接口方法、方法类型等。对常量池中的CONSTANT_ClASS_INFO、CONSTANT_FIELDREF_INFO、CONSTANT_METHODREF_INFO等。

- 初始化(Initialization)

  初始化阶段就是之心类构造器方法<clinit>()的过程。

  **此方法不需要定义，是javac编译器自动收集类中的所有类变量的赋值动作和静态代码块中的语句合并而来的。**

  构造方法中指令按语句在源文件中出现的顺序执行。

  ```java
  	public class Demo {
          private static int num = 1;
          static {
              num = 2;
              number = 20;
          }
          private static int number = 10; // 链接(linking)的prepare阶段： number = 0; initial阶段: 先20再10, 最终 number=10; 
      }
  ```
  
  <clinit>() 不同于类的构造器。（关联：构造器是虚拟机视角下的<init>())
  
  若该类具有父类，JVM会保证字类的<clinit>()执行之前，父类的<clinit>()已经执行完毕。
  
  **虚拟机必须保证一个类的<clinit>() 方法在多线程下被同步加锁**。
  
  

## 2. 垃圾回收

## 3. 字节码与类的加载

## 4. 性能监控与调优
