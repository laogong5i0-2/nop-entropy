# 关于可逆计算的讨论--答圆角骑士魔理沙

在[为什么计算机科学存在图灵机和Lambda演算两种世界观，量子力学中却存在着三种世界图景？](https://www.zhihu.com/question/614938288/answer/3147722439)一文中我提出了一个启发式的观点，将图灵机、lambda演算、可逆计算与量子力学中的薛定谔图景、海森堡图景和狄拉克图景做了一个有趣的对应，指出可逆计算是第三条通向图灵完备且受到物理学启发的技术路线。这对于研究生阶段学习过多种图灵完备的计算模型的同学来说，理智和情感上都可能感到难以接受，完全无法将这些概念联系起来，甚至有人调侃的问：为什么计算机科学有五个字，量子力学却是四个字，它们之间是否存在什么对应关系？

本质上，可逆计算源于一组不同的物理直觉，所以需要跳出原有的计算机科学思维模式才可能理解。至于可逆计算的意义，首先根据它可以推导出具体的技术方案，可以在程序语言中引入可逆计算专用的语法，**可以在理论和实践层面解决组件理论所无法解决的粗粒度软件复用的问题**。第二，目前业内大量差量相关的实践都可以统一纳入到可逆计算理论的框架中来进行研究，比如Docker技术就是可逆计算理论的一个标准应用实例。

在本文中我将回答一下常见的问题，主要是解释一下可逆计算理论背后的直觉内容，以及它解决了现有的程序语言所无法解决的哪些问题。

# 1. 图灵完备的基础语言有很多，研究生基础课都要起码讲个三四种计算模型，为什么说只有图灵机和Lambda演算两种世界观？

我的文章中所表达的不是只有两种图灵完备的语言，它说的也不是只有两种风格的图灵完备的语言。实际上，图灵完备的计算模型可以说是有很多种。这就产生了一个有趣的问题：**图灵完备的计算模型到底是有限多种还是无限多种**？ 如果是有限多种，到底是哪几种，自然界凭什么只允许这几种计算模型？如果是无限多种，我们怎么去理解无限多种的计算模型？这么多的计算模型有没有更基本的模型，而其他的模型可以看作是这些基本模型的混杂？ 

很多人虽然在课堂上学习过很多图灵完备的计算模型，但是它们都是作为孤立的模型被认知的，**这些图灵完备的模型作为一个整体可以被怎样看待？这些模型之间除了图灵完备这个共性之外还有什么概念联系**？对于这个问题，你不感到好奇吗？

站在计算机科学内部很难看清楚世界的全貌。当年Lamport（图灵奖得主，Paxos算法发明人）之所以在分布式领域做出很多革命性的创新工作，与他学习过相对论有着很大的关系。**图灵机和Lambda演算除了计算能力等价之外还有什么联系**？这个问题在单纯的计算机科学中可能难以回答。但是通过与量子力学类比，我们不仅仅可以建立这两种计算模型的一种概念联系，**而且可以很自然的推导得出一种新的源于物理学直觉的通用计算模型**。

为什么量子力学中只有三种世界图景？不可以更多吗？完全可以有无限多种。但是薛定谔图景和海森堡图景提供了**两种基本的认知视角**。风吹幡动，是风动还是幡动？量子力学的基本研究内容是量子算符如何作用到量子态上驱动系统演化。因为我们只关心最终的观测结果，所以对于算子与量子态如何参与演化可以存在不同的解读。一种方式是将所有演化都归因于量子态的演化，这对应于薛定谔图景，而另一种方式是将所有演化都归因于量子算符的演化，这对应于海森堡图景。这两种图景对于最终的物理观测结果的预测是完全一致的。

$$
\langle \psi_S(t)|A_S|\psi_S(t)\rangle = \langle \psi_H|A_H(t)|\psi_H\rangle

$$

显然，这代表了两种极端化的世界观。狄拉克图景可以看作是这两种图景的一种混杂，一种折衷，但是它所研究的重点是有关于Delta的问题：在一个已知模型的基础上增加微小的扰动会产生一个新的模型，**如何充分利用已知模型的解来简化这个微扰模型的求解**？

类似于量子世界的算符和量子态，程序的世界同样有两个对偶的基本概念：函数和数据。面对新的问题，图灵机的选择是固定几个操作，但是接受无限输入，而Lambda演算则是固定常量状态，一切都通过新的函数来表达。在这个意义上，我们可以说图灵机（固定的函数+无限变化的数据）和Lambda演算（固定的数据+无限变化的函数）是两种基本的世界观。

说得更直白一些，在一对对偶的概念中，固定一侧，只变化另一侧，这对应于两个极端的世界观。而可逆计算可以看作是恰在中间的一种强调有限性和分层认知的世界观，它认为当两侧都无法无限变化时需要通过额外的Delta差量来体现未知的变化，Y=F0(X0)+Delta，所以它是第三种世界观。在这三者之间肯定存在无限多的其他选项，但它们都不是最极端最具有代表性的。

> 关于对偶性的概念，可以参见知乎的这篇文章： [怎么形象地理解对偶空间（Dual Vector Space）](https://www.zhihu.com/question/38464481/answer/2446175090)

# 2. 建新框架是为了发现新的以前没有的东东，而不是重新诠释已有的东东。可逆计算是不是马后炮？

Docker技术在数学层面可以被概括为如下公式：

>  `App = DockerBuild<DockerFile> overlay-fs BaseImage`

* DockerFile相当于是一种定义镜像内容的DSL

* Docker的Build工具相当于一种解析并执行DSL的Generator，根据DSL生成镜像切片

* 多个镜像切片通过支持差量化的文件系统OverlayFS合成到一起

很明显，Docker所体现的计算模式正是可逆计算理论所提出的软件构造公式的特例

> `App = Delta x-extends Generator<DSL>`

**可逆计算理论可以为包括Docker在内的一系列基于差量概念的技术提供统一的理论解释**，那它是不是一种马后炮？

1. 首先**我早在2007年就提出了可逆计算的思想**，而Docker是2013年才公开发布的技术。显然这只能看作是理论先于实践很多年，而且理论精确预测了实践所需要具有的技术结构。

2. 可逆计算不仅仅是解释某个孤立的技术现象，而是将它对应到抽象的数学概念上，从而可以迅速推广到更为广泛的应用领域。特别的，可逆计算指出每一个DSL都可以定义一个差量模型空间，在每一个这样的空间中都可以定义具有领域语义的差量合并运算机制和差量生成机制。Docker的做法是非常巧妙的选择了差量文件系统作为底层的差量模型空间，而Linux世界中积累了许多年的命令行程序都自动成为一个个差量生成算子，从而盘活了已有的技术资产。这种思想很容易推广到任意其他差量模型空间中，例如k8s 1.14版本之后提出的Kustomize配置工具将类似的思想应用到YAML配置文件，产生了新的配置定制方案。而类似的Delta定制方案我们早已经使用了很多年。具体参见 [从可逆计算看kustomize](https://zhuanlan.zhihu.com/p/64153956)

有些人可能认为Docker不过是一种应用层具体的技术，它与基础的计算模型没有关系，但是我想指出的是，一切由软件自动完成的操作都是某种计算，它们背后都体现了某种计算模型，Docker技术的成功之处在理论层面可以被提炼为一种抽象的计算模型，并应用到其他场景。**我们在实践中早已经习惯了不可变数据的概念，如果考虑到数据与函数之间的对偶性，那么如何去定义并实现不可变的逻辑这一概念**？

# 3. 可逆计算跟所谓的鹅卵石游戏只是计算机科学中很小的一部分，将它看作是计算模型的根基是不是一种错误？

对于我提出的可逆计算理论，最常见的一种误解是将它与硬件领域的可逆计算机概念混淆。可逆计算机的概念源于1961年IBM的物理学家Rolf Landauer 发表的一篇文章,“Irreversibility and Heat Generationin the Computing Process.” 他的研究表明，每擦除一比特信息，至少会有kT*ln2的能量以热能的形式耗散进入环境，为了最大限度的降低能量耗散，我们必须要使用可逆逻辑门来完成运算。可逆计算机的研究一直以来都是一个很小众的研究领域，它偏于硬件实现层面，与量子计算机的研究可以算作一类。

我提出的可逆计算理论，与可逆计算机的相似之处在于它们都是要实现计算的可逆化，但是可逆计算理论强调的是高层的抽象结构，而不是底层的物理实现。为了避免与可逆计算机混淆，我选择的英文是Reversible Computation，而一般可逆计算机对应的英文是Reversible Computing。

前一段时间Leo受到可逆计算机概念和深度神经网络反向传播算法的启发，提出了一个抽象的支持可逆运行的程序语言：NiLang。参见[NiLang - 可逆计算，微分万物](https://zhuanlan.zhihu.com/p/191845544)。这个可逆语言所面向的结构空间仍然是传统的计算机硬件存储结构。而我所提出的可逆计算它的重点在于可以灵活定义差量结构空间，并且明确定义结构化差量的概念，可逆差量运算完全可能只定义在某个DSL层面，**在程序整体层面不一定是可逆的**。

不可逆在物理层面意味着熵增，而可逆计算理论指出，借助于差量化结构，**即使我们不能控制系统整体的熵增，我们仍然可能控制熵增的地方**，限制它只存在于Delta差量中，从而避免偶然的需求不断侵蚀基础产品的技术架构。

可逆计算理论可以看作是对软件工程领域传统的面向对象、面向组件理论的进一步发展，它在理论层面克服了组件理论所存在的固有困难，解决了粗粒度的系统级的软件复用问题。

1. 面向对象的继承可以看作是一种偏序关系 A > B，派生类A 比基类B多，但是具体多了什么没有明确的表达。

2. 组件理论所谓的组合优于继承相当于是 A = B + C，相同的C可以多次被多次重用利用 E = D + C。

3. 从不等式到加法，进一步的发展很自然的是引入减法
   
   ```
   X = A + B + C
   Y = A + B + D = X + (-C+D) = X + Delta
   ```
   
   **引入减法类似于在数学中引入负数，相当于扩大了问题的解空间，以前不可解的一些结构复用问题现在变得可解了**。

美国卡内基大学软件工程研究所将软件工程的发展脉络整理为：不断提升复用的颗粒度。但是如果要实现真正粗粒度的、系统级的复用在理论层面存在很多困难，软件工程理论界的探索可以参见 [从可逆计算看Delta Oriented Programming](https://zhuanlan.zhihu.com/p/377740576)。

在可逆计算的视角下，软件复用的原理发生了本质性的变化：从组件复用的**相同可复用**转换到可逆计算的**相关可复用**：任意的Y和任意的X之间都可以通过Delta建立转换关系，从而形成复用，而不需要它们之间构成传统的部分-整体这样的组合关系。

所谓系统级的复用在实践层面对应于在完全不修改基础产品X的源码的情况下，可以通过增量式的模式开发产品Y。翻译到抽象层面，就对应于 建立从X到Y的一个运算关系 Y = X + Delta

由此可以看出，对于产品可扩展性的研究可以被转化为对差量形式以及差量之间运算关系的研究，这正是可逆计算理论所研究的范畴。

可逆计算理论所解决的一个非常实际的问题就是：一个银行核心应用在部署到不同银行进行深度定制开发的时候，如何能够做到完全不修改基础产品源码？也就是说，**我们能不能够像继承一个类一样继承一整个软件产品**？基础产品和定制产品可以并行进行改进，无需进行手工代码合并就可以自动继承得到基础产品所提供的新的特性、所修正的BUG补丁等。

为了更好的阐释可逆计算理论的内容，我开源了可逆计算的一个参考实现：Nop平台 https://www.gitee.com/canonical-entropy/nop-entropy。

Nop平台引入了专用于可逆差量合并的程序语法，参见[XDSL：通用的领域特定语言设计](https://zhuanlan.zhihu.com/p/612512300), 可以自动支持粗粒度的软件复用，参见[如何在不修改基础产品源码的情况下实现定制化开发](https://zhuanlan.zhihu.com/p/628770810).

## 可逆计算所依赖的物理直觉

可逆计算带来了一种有别于传统计算机科学理论的新的世界观，它的方法论来源是物理学的熵增原理以及微扰论。如果思想开放一些，至少它能给你一些不一样的启发。

在微积分中我们都学习过Taylor级数展开

$$
y = 3(x+dx)^2 + 2(x+dx) = 3x^2 + 2x + (6x+2)dx + 3dx^2 
$$

数学上我们可以把分散在系统各处的delta小量收集在一起，物理上我们可以赋予汇总后的同价小量一个明确的物理意义（例如，所有的一阶小量被聚集在一起，称为函数的导数，对应于速度、加速度、强度、力这样的物理观念），而**在软件中却从未有过这种系统化的分层级的差量分解机制**，仔细的思考一下就会发现，这真的是一件很奇怪的事情。

通过对Talyor级数的观察，我们可以得到一系列的物理直觉。

1. **直觉1：差量的独立存在隐含要求原系统存在一个明确的坐标系统**。在Taylor级数的构造中，差量是从系统各处产生并收集得来的。如果我们可以将差量独立存放并管理，最终把它和原系统进行合并的时候就需要明确知道每个局部变化原始的来源位置。 差量需要能够透明的穿越一切结构障碍直接应用到扰动实际发生的坐标处。你仔细想一下就会知道，所有具有差量特性的设计第一个要求都是系统内部存在系统化的、带有唯一性的定位机制。比如数据库领域中典型的表-行-列坐标系。

2. **直觉2，类型系统不是一个合适的坐标系**。因为类型的基本假定是多个对象具有同样的类型，它用于定位时会导致概念的模糊。典型的问题是，我们在一组按钮列表中进行定位一般依靠的是id，而不是类型。 如果学过群表示论就可以知道，同一个物理事实可以投射到不同的空间中进行观察，我们可以看到不同的差量构造规律。比如说，对于一个函数的定义，我们可以把投射到二进制空间，异或运算可以在任意函数表示之间建立差量关系。 我们也可以把函数投射到通用的行文本空间，通过diff算法建立差量关系，git的应用就建立在这种投射关系上。将函数投射到类型系统也是一种普适的方案，有些人也在类型系统上建立差量，比如Compositional Programming，https://www.bilibili.com/video/BV1Ph4y1M7aB/ 。一般的类型系统不过是定义了类（模块）-方法这样一种两级的坐标系统。需要强调的是，**在不同的差量空间中对同一个物理对象我们可以定义不同的差量形式，差量的定义和它的运算方式不是唯一的**。

3. **直觉3，基于坐标系的概念我们转向了场论的世界观**。组件的黑箱模型类似于高中阶段牛顿力学的世界观，它是完全机械化的：刚体的运动完全由它的质心坐标和尺寸形状朝向等少数几个参数来描述，刚体的内部构造无法被观测也无关紧要，刚体之间通过直接接触发生相互作用， 刚体的形状必须精确匹配才能构成一个无缝的整体。即使是在古典力学中，稍微高级一点的观点也都会转换到拉格朗日表述或者哈密尔顿表述，它的精神实质是转向场论的世界观。 所谓的场（Field），其实就是建立一个无所不在的坐标系，然后在坐标系的每一点上都可以指定一个物理量。场的自由度是无限的，但是通过坐标系它是可描述的、可定义的、可研究的，在坐标系的每一点上我们都可以精确的度量局部的变化。在场的世界观中，我们面对的核心图像是对象总是浸泡在场（无所不在的坐标系）中，而不再是孤立对象之间的两两相互作用。

4. **直觉4，同一个物理事实在不同的坐标系中都可以被表达，但是在特定的领域，存在着最适合该领域的内禀坐标系**。程序语言可以被看作是定义了一个语义空间， 它提供了一种表达物理事实的坐标系统。而一个领域特定语言DSL可以提供一个最适合表述本领域物理事实的坐标系统。特别的，当需求发生变化的时候，在DSL的表述中我们只会看到少量的局部变化，而在通用程序语言所实现的代码中，可能会发现大量的源码调整。这类似于在直角坐标系中描述圆形需要同时变化两个坐标参数，而在极坐标系中只变化角度坐标，从而起到降维的作用。

5. **直觉5， DSL模型既是在坐标系中定义的实体，同时它又构成了坐标系本身**。我们没有必要单独为了扩展点在模型中增加额外的描述信息，只需要利用DSL语言自身的结构标识即可。这类似于外微分中的活动标架法，我们在坐标系中描述运动，而运动的方向本身又构成一个自然产生的坐标系统。

6. **直觉6，同一个物理事实可以具有多种坐标表象**。坐标系转换可以成为一种有效的结构构造手段。例如
   
   ```
   Excel <==> DomainObject <==> UI
   ```
   
   无需编程，根据元模型可以自动推导得到可视化编辑界面，实现模型对象的可视化编辑。从一种形式转换到另一种形式，类似于范畴论中的函子（Functor）映射，我们所关注的不是某一个具体的对象，而是需要为形式体系中的每一个可能的对象都定义一种通用的转换策略。

7. **直觉7，全量可以看作是差量的一种特例**。A = 0 + A，因此全量和差量可以是同构的，应该用同一个schema去约束，在这种情况下，差量的差量仍然是一个普通的差量。

8. **直觉8，差量可以独立于base存在**。很多人将差量的作用看作是 base + patch，认为base是主要的，patch是次要的。但是，实际上base也可以被看作是patch的patch，base与patch之间是对偶关系，在理论上没有必要把它们区别对待。差量具有独立存在的价值，它并不需要依附于base才能够被理解。

这些直觉内容可以用维特根斯坦的一句话来概括：**语言的边界就是我们世界的边界**，可逆计算理论进一步的诠释是：**一种语言就是一种坐标系，基于坐标自然产生差量**。

可逆计算系统化的发展了领域坐标系的概念，Nop平台在具体落实可逆计算理论时采用了如下方案：

1. 采用DSL来描述所有业务。DSL采用XML语法形式，也就是一种Tree结构。文件系统+文件内的Tree构成一个细化到单个属性和函数的坐标系。

2. 所有的DSL都满足所谓的XDSL规范要求，都具有同样的差量合并运算语法x:extends, x:override, x:gen-extends，它们负责实现可逆计算所要求的计算模式

3. 有一种特殊的XDSL叫做XDef，它作为元模型用于定义所有其他DSL的语法结构，同时XDef自身也由XDef来约束。它与类型系统最本质的区别在于要求列表结构必须包含某种唯一标识属性，例如name，id等，从而确保领域坐标的存在性。

4. 使用XDSL实现的所有系统都自动具有一个特性：支持Delta差量定制。可以在完全不修改原系统源码的情况下，通过额外补充Delta差量就可以对系统中所有的逻辑和数据结构进行深度定制。 现有的程序语言因为缺少领域坐标系的概念，所以无法实现这种系统级的、粗粒度的复用。

可逆计算完全是在编译期可以实施的操作，所以它不会影响运行期性能。框架层面的技术方案参见[从张量积看低代码平台的设计](https://zhuanlan.zhihu.com/p/531474176)。

关于领域坐标系的详细定义和结合律的证明，可以参考[写给程序员的可逆计算理论辨析](https://zhuanlan.zhihu.com/p/632876361)