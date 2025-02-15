# 1. 代码生成器是低码吗？

传统的代码生成器只是一次性脚手架，不支持持续的增量化改进，所以它不是低码的。典型的，一旦生成代码之后，所有客户化定制工作都需要修改生成代码，从而脱离原有
的模型约束，当我们对模型进行了增量调整之后，重新出码会覆盖所有代码，丢失手工修改内容。

原则上生成代码与基于JSON格式进行解释并没有本质上的差别。例如在形式上，我们可以这样看待amis框架的运行过程：
````
result = renderAmis(json, data)
````

renderAmis作为一个解释器接受amis的json文件以及外部传入的data数据，从而在运行时产生最终的结果。

我们知道，多参数函数可以通过curry化转化为单参数函数，所以这一过程实际上等价于
````
result = (renderAmis(json))(data)
````
即 renderAmis与json先结合，产生一个函数，然后再把这个函数作用到data上。

如果renderAmis(json)的信息在编译期已经能够确定，那么完全可以在编译期经过编译优化得到一个编译后的组件。
````
component = renderAmis(json) // 在编译期执行
result = component(data)   // 在运行期执行
````

在数学上，所谓的编译过程可以看作是将一个原先运行期的函数拆分成了两个部分：编译期的函数生成器 + 运行期的函数。这一过程，可以被看作是将函数提升为了泛函或者物理上我们常说的算子。

理论上说，生成代码并不是做不到**“持续的可视化编译”**，只要通过即时编译技术实现动态的差量编译（差量化的产生式编程），所有解释模型能做到的事情，多阶段编译技术都能够做到。

如何实现增量化的代码生成，可以参见我的文章
[可逆计算]!https://zhuanlan.zhihu.com/p/64004026

[可逆计算的技术实现]!https://zhuanlan.zhihu.com/p/163852896


当然，目前业内并没有公开的开箱即用的现成解决方案，基于代码生成有很多工程问题需要去解决。


Nop平台在元编程阶段的大量结构转换也可以看作是一种编译过程，所以整体可以看作是一种多阶段编译，并不只是生成通用语言源码这一个编译阶段

出码有可能出的是DSL代码，而并不一定是通用语言代码，例如Nop平台在前端目前出的是amis代码，在前台交由amis引擎负责解释运行。
本质上代码只是信息的一种载体。业务信息一旦得到精确的表达，应该可以使用多种载体去承载，特别是应该可以逆向抽取得到原始编程意图


# 2. 低码自己的核心领域模型如何定义？
一些低代码平台在固化的少数场景中显得非常强大，可以非常快速的以固化的模式实现某些业务。但是我们认为强大的本质在于内在演化的基因，而不是堆砌的功能。
恐龙很强大，却在环境变迁中被无情的淘汰。现有的低代码产品主要还是采用穷举法，内置大量的功能特性，但是技术环境不断变迁，它能否快速适应?不要说别的，能否迁移到最新的基础技术平台上就是一个很明显的挑战。大量的功能都是采用过去流行的某种技术构建，很可能会绑定在某个技术框架版本上。

低代码的能力来源于建模，而强大的低代码应该是支持立刻新建模型并自由扩展已有模型的，这要求基础架构至少是具有元模型定义。

Nop平台自身的所有模型都统一使用xdef元模型来定义所有的模型，而xdef元模型由xdef元模型自身来约束，即元元模型仍然是xdef。平台的核心实际上是通用的Tree结构变换、加载机制，而DSL不过是具有语义的某种Tree。

具体来说，我们的做法是定义xdef元模型之后，立刻自动推导得到设计器，并在IDEA中通过一个统一的插件根据元模型自动实现语法提示，支持DSL断点调试等。所以本质上是一种面向语言的编程范式：解决业务领域的问题之前，先制定一个面向特定领域的DSL，然后再用DSL来开发业务，而平台的作用是将开发并扩展已有DSL的成本降到与开发一个函数的成本类似。

低代码如果要面向广泛的编程领域，而不仅仅局限在少数CRUD场景，它的核心领域模型就不应该是少数面向业务的固化模型，而是一种快速创建新的领域模型的元模型的能力。


# 3. DSL需要图灵完备吗？

DSL可以图灵完备，也可以不要求图灵完备，但是涉及到领域知识的子部分，它应该支持反向信息抽取，这个子部分原则上是描述式而且不是图灵完备的。一个语言相当于是一个坐标系，一个通用语言相当于是一个万能坐标系，所有信息都采用这个坐标系表达，但是DSL相当于是某种局部坐标系，多个局部坐标系粘结在一起构成整体坐标系（可以类比微分流形的概念）。所以实际解决问题的时候，我们是通过DSL森林来解决问题。**图灵机能够实现图灵完备的根本原因在于图灵机可以被看作是一种虚拟机**，它可以模拟所有其他的自动计算机器，而如果我们不断提升虚拟机的抽象层次，就会得到可以直接"运行"所谓领域特定语言(DSL)的虚拟机，但是因为DSL关注的重点是特定领域概念，它必然无法以最便利的方式表达所有通用计算逻辑（否则它就成为了通用语言），必然会导致某种信息溢出，成为所谓的Delta项。

## 4. 可视化编辑对于低代码来说是不是必不可少的？

单就低代码产品来说，可视化编辑可能是必不可缺的功能，因为这是一个非常重要的卖点。但是对于广义的低代码而言，可视化并不应该是必须的选择。可视化实际上是意味着同一份信息同时具有两种表示：文本表示和可视化表示，而且这两种表示之间是可以进行可逆转换的。声明式并不一定可以从展现结果反向推导到源码，可以反向的只能是声明式的一个可逆子集。

从广义的角度上说，真正的发展方向是可逆的表达方式：同一份信息具有多种表达方式，而且它们之间可以相互转换，最终使得信息能够摆脱单一形式的约束，实现无阻碍的自由流动（第一次工业革命源于人们发现能量在不同形式之间可以相互转化）。典型的，AMIS框架向下兼容这一优势与可视化并无关系，它只是说AMIS的信息表达方式在某种程度上是自我完备的，它的语义仅依赖于其自身的结构定义，从而它可以具有多种解释的可能性，在不同的时期、不同的技术环境中我们可以为AMIS的逻辑表示提供不同的执行层面的表示而已。

传统上面向代码编程时，我们并不是很关注描述信息的完备性，很多信息存在于文档中、程序员的头脑中，或者散在于外部图灵完备的语言、框架中，从而难以通过简易的手段对信息进行反向抽取，更无法为它提供逻辑等价的不同表达形式。

进一步的论述可以参见我的文章
[从可逆计算看LowCode]!https://zhuanlan.zhihu.com/p/344845973

## 5. 低代码是不是存在着先天的局限性？
低代码是不是只适用于特定领域？依赖低代码构建的程序是不是对特定的领域模型具有强依赖，从而本质上丧失了灵活性？

低代码的力量在于它内置了很多领域相关的假设，从而可以减少不必要的重复表达（通过引擎内部的自动推理减少了手工编写的各类关联和转换代码）。但是一旦领域假设局部假设被突破，会不会导致整体程序结构被摧毁？

例如，明明是一个字段级别的需求问题，但是经过低代码封装后，我们如何进行字段级别的定制？如何才能定制修改封装后的组件？还是说要对整个页面进行特殊处理，甚或是废弃整个框架从头来过？

回顾一下物理学中的成功经验，我们总是以递进式的方式来解决问题，零阶模型、一阶模型、二阶模型...。当越来越多的特殊化的信息进入我们的认知之后，我们并不会直接推翻此前基于少量通用信息所建立的模型，而是可以不断补充一个高阶模型来对差异化部分进行描述。

可逆计算在理论层面提供了一个完整的解决方案，它指出本质上低代码并不会受到这些局限性的限制。

## 6. 图形化的逻辑表达
图形化的方式确实不适合表达细节逻辑，因为它的信息密度很低。图形的好处是可以充分利用人脑并行模式识别的能力，迅速发现二维或者三维的某种组织模式，但总的来说，人类的文化不是围绕着图形化表达方式建立的，除了数学符号之外的图形化表达方式一般缺乏文化背景，导致传达的信息量很有限。

当我们用代码来表达逻辑时，它潜在利用的信息很多：
1. 函数调用、算术运算、算符结合律等复用了我们花费多年学习的数学知识
2. 程序语法要素和书写形式类似于我们习惯的自然语言
3. 代码文本的顺序潜在的表达了代码运行时的执行顺序
4. 通过括号、缩进、函数调用等表达的嵌套结构，与程序运行时的堆栈变化存在直接对应关系

相比于流程图，与现代程序的运行时更接近的其实还是直接支持堆栈概念的某种Tree结构。通过Tree的嵌套，我们可以隐式的表达goto到某个状态，然后必然会返回到前一状态这一基本运行时结构。游戏领域常见的behavior tree可以看作是通过Tree结构表达逻辑的一种范例。

https://opsive.com/support/documentation/behavior-designer/what-is-a-behavior-tree/

## 7. 低代码的后端存储
单从存储的需求上说，后端采用K-V格式按照键值对保存所有数据是最灵活的。因为底层的数据库在逻辑层面就是这么干的！实际上分布式数据库TiDB可以看作是在分布式的TiKV上所建立的一种封装层。

当我们把数据按照KV格式保存之后，我们就拥有了一种灵活性：重新实现并定制所有数据库机制的灵活性。我们在KV存储基础上所干的所有工作只是在实现一个简易版的数据库而已。

理想的情况下，我们可以通过一个ORM引擎来屏蔽对底层存储结构的依赖，即无论它是直接按照数据库表存储，或者预留多个通用类型列，抑或采用KV键值对形式存储到纵表中，我们在业务层面都察觉不到这些变化。ORM引擎在概念层面所提供的是一种面向业务的虚拟数据库。



