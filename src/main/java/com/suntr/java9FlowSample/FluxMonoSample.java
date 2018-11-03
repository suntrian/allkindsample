package com.suntr.java9FlowSample;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class FluxMonoSample {


    /**
     * 第一种方式是通过 Flux 类中的静态方法。
     *
     * just()：可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
     * fromArray()，fromIterable()和 fromStream()：可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
     * empty()：创建一个不包含任何元素，只发布结束消息的序列。
     * error(Throwable error)：创建一个只包含错误消息的序列。
     * never()：创建一个不包含任何消息通知的序列。
     * range(int start, int count)：创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
     * interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的 Long
     * 对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
     * intervalMillis(long period)和 intervalMillis(long delay, long period)：与 interval()方法的作用相同，只不过该方法通过毫秒数来指定时间间隔和延迟时间。
     */
    public static void simpleStaticFlux() throws InterruptedException {
        Flux.just("Hello","World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1,2,3,4,5}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1,10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
        Flux.interval(Duration.of(1000,ChronoUnit.MILLIS)).subscribe(System.out::println);
        Thread.sleep(100000);
    }


    /**
     * generate()方法通过同步和逐一的方式来产生 Flux 序列。序列的产生是通过调用所提供的 SynchronousSink 对象的 next()，complete()和 error(Throwable)
     * 方法来完成的。逐一生成的含义是在具体的生成逻辑中，next()方法只能最多被调用一次。在有些情况下，序列的生成可能是有状态的，需要用到某些状态对象。此时可以使用 generate()方法的另外一种形式 generate
     * (Callable<S> stateSupplier, BiFunction<S,SynchronousSink<T>,S> generator)，其中 stateSupplier
     * 用来提供初始的状态对象。在进行序列生成时，状态对象会作为 generator 使用的第一个参数传入，可以在对应的逻辑中对该状态对象进行修改以供下一次生成时使用。
     *
     * 在代码清单 2中，第一个序列的生成逻辑中通过 next()方法产生一个简单的值，然后通过 complete()方法来结束该序列。如果不调用 complete()
     * 方法，所产生的是一个无限序列。第二个序列的生成逻辑中的状态对象是一个 ArrayList 对象。实际产生的值是一个随机数。产生的随机数被添加到 ArrayList 中。当产生了 10 个数时，通过 complete()
     * 方法来结束序列。
     */
    public static void generateFlux() {
        Flux.generate(sink -> {
            sink.next("hello");
            sink.complete();
        }).subscribe(System.out::println);
        Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int v = random.nextInt(100);
            list.add(v);
            sink.next(v);
            if (list.size()==10){
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);
    }

    /**
     * create()方法与 generate()方法的不同之处在于所使用的是 FluxSink 对象。FluxSink 支持同步和异步的消息产生，并且可以在一次调用中产生多个元素。在代码清单 3 中，在一次调用中就产生了全部的 10 个元素。
     */
    public static void createFlux() {
        Flux.create(fluxSink -> {
            for (int i = 0; i < 10; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
        }).subscribe(System.out::println);
    }


    /**
     * Mono 的创建方式与之前介绍的 Flux 比较相似。Mono 类中也包含了一些与 Flux 类中相同的静态方法。这些方法包括 just()，empty()，error()和 never()等。除了这些方法之外，Mono
     * 还有一些独有的静态方法。
     *
     * fromCallable()、fromCompletionStage()、fromFuture()、fromRunnable()和 fromSupplier()：分别从
     * Callable、CompletionStage、CompletableFuture、Runnable 和 Supplier 中创建 Mono。
     * delay(Duration duration)和 delayMillis(long duration)：创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
     * ignoreElements(Publisher<T> source)：创建一个 Mono 序列，忽略作为源的 Publisher 中的所有元素，只产生结束消息。
     * justOrEmpty(Optional<? extends T> data)和 justOrEmpty(T data)：从一个 Optional 对象或可能为 null 的对象中创建 Mono。只有 Optional
     * 对象中包含值或对象不为 null 时，Mono 序列才产生对应的元素。
     * 还可以通过 create()方法来使用 MonoSink 来创建 Mono。代码清单 4 中给出了创建 Mono 序列的示例。
     */
    public static void simpleMono() {
        Mono.fromSupplier(()->"Hello Mono").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Hello Mono2")).subscribe(System.out::println);
        Mono.create(MonoSink-> MonoSink.success("Hello")).subscribe(System.out::println);
    }

    /**
     * 对流中包含的元素进行过滤，只留下满足 Predicate 指定条件的元素。代码清单 6 中的语句输出的是 1 到 10 中的所有偶数。
     */
    public static void filterFlux() {
        Flux.range(1,10).filter(i-> i %2 == 0).subscribe(System.out::println);
    }

    /**
     * buffer 和 bufferTimeout
     * 这两个操作符的作用是把当前流中的元素收集到集合中，并把集合对象作为流中的新元素。在进行收集时可以指定不同的条件：所包含的元素的最大数量或收集的时间间隔。方法 buffer()仅使用一个条件，而 bufferTimeout
     * ()可以同时指定两个条件。指定时间间隔时可以使用 Duration 对象或毫秒数，即使用 bufferMillis()或 bufferTimeoutMillis()两个方法。
     *
     * 除了元素数量和时间间隔之外，还可以通过 bufferUntil 和 bufferWhile 操作符来进行收集。这两个操作符的参数是表示每个集合中的元素所要满足的条件的 Predicate 对象。bufferUntil
     * 会一直收集直到 Predicate 返回为 true。使得 Predicate 返回 true 的那个元素可以选择添加到当前集合或下一个集合中；bufferWhile 则只有当 Predicate 返回 true
     * 时才会收集。一旦值为 false，会立即开始下一次收集。
     *
     * 代码清单 5 给出了 buffer 相关操作符的使用示例。第一行语句输出的是 5 个包含 20 个元素的数组；第二行语句输出的是 2 个包含了 10 个元素的数组；第三行语句输出的是 5 个包含 2
     * 个元素的数组。每当遇到一个偶数就会结束当前的收集；第四行语句输出的是 5 个包含 1 个元素的数组，数组里面包含的只有偶数。
     *
     * 需要注意的是，在代码清单 5 中，首先通过 toStream()方法把 Flux 序列转换成 Java 8 中的 Stream 对象，再通过 forEach()方法来进行输出。这是因为序列的生成是异步的，而转换成
     * Stream 对象可以保证主线程在序列生成完成之前不会退出，从而可以正确地输出序列中的所有元素。
     */
    public static void bufferFlux() {
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        Flux.interval(Duration.ofMillis(100)).buffer(Duration.ofMillis(1001)).take(2).toStream().forEach(System.out::println);
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);

    }

    /**
     * window 操作符的作用类似于 buffer，所不同的是 window 操作符是把当前流中的元素收集到另外的 Flux 序列中，因此返回值类型是 Flux<Flux<T>>。在代码清单 7 中，两行语句的输出结果分别是 5 个和 2 个 UnicastProcessor 字符。这是因为 window 操作符所产生的流中包含的是 UnicastProcessor 类的对象，而 UnicastProcessor 类的 toString 方法输出的就是 UnicastProcessor 字符。
     */
    public static void windowFlux() {
        Flux.range(1, 100).window(20).subscribe(System.out::println);
        Flux.interval(Duration.ofMillis(100)).window(Duration.ofMillis(1001)).take(2).toStream().forEach(System.out::println);

    }

    /**
     * zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并。在合并时可以不做任何处理，由此得到的是一个元素类型为 Tuple2 的流；也可以通过一个 BiFunction
     * 函数对合并的元素进行处理，所得到的流的元素类型为该函数的返回值。
     *
     * 在代码清单 8 中，两个流中包含的元素分别是 a，b 和 c，d。第一个 zipWith 操作符没有使用合并函数，因此结果流中的元素类型为 Tuple2；第二个 zipWith 操作通过合并函数把元素类型变为 String。
     */
    public static void zipFlux() {
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2))
                .subscribe(System.out::println);
    }

    /**
     * take 系列操作符用来从当前流中提取元素。提取的方式可以有很多种。
     *
     * take(long n)，take(Duration timespan)和 takeMillis(long timespan)：按照指定的数量或时间间隔来提取。
     * takeLast(long n)：提取流中的最后 N 个元素。
     * takeUntil(Predicate<? super T> predicate)：提取元素直到 Predicate 返回 true。
     * takeWhile(Predicate<? super T> continuePredicate)： 当 Predicate 返回 true 时才进行提取。
     * takeUntilOther(Publisher<?> other)：提取元素直到另外一个流开始产生元素。
     * 在代码清单 9 中，第一行语句输出的是数字 1 到 10；第二行语句输出的是数字 991 到 1000；第三行语句输出的是数字 1 到 9；第四行语句输出的是数字 1 到 10，使得 Predicate 返回 true
     * 的元素也是包含在内的。
     */
    public static void take() {
        Flux.range(1, 1000).take(10).subscribe(System.out::println);
        Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
        Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
        Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);
    }

    /**
     * reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。累积操作是通过一个 BiFunction
     * 来表示的。在操作时可以指定一个初始值。如果没有初始值，则序列的第一个元素作为初始值。
     *
     * 在代码清单 10 中，第一行语句对流中的元素进行相加操作，结果为 5050；第二行语句同样也是进行相加操作，不过通过一个 Supplier 给出了初始值为 100，所以结果为 5150。
     */
    public static void reduce() {
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
    }

    /**
     * merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。不同之处在于 merge 按照所有流中元素的实际产生顺序来合并，而 mergeSequential
     * 则按照所有流被订阅的顺序，以流为单位进行合并。
     *
     * 代码清单 11 中分别使用了 merge 和 mergeSequential 操作符。进行合并的流都是每隔 100 毫秒产生一个元素，不过第二个流中的每个元素的产生都比第一个流要延迟 50 毫秒。在使用 merge
     * 的结果流中，来自两个流的元素是按照时间顺序交织在一起；而使用 mergeSequential 的结果流则是首先产生第一个流中的全部元素，再产生第二个流中的全部元素。
     */
    public static void merge() {
        Flux.merge(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100)).take(5),
                    Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);
        Flux.mergeSequential(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100)).take(5),
                Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * flatMap 和 flatMapSequential 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并。flatMapSequential 和 flatMap 之间的区别与 mergeSequential
     * 和 merge 之间的区别是一样的。
     *
     * 在代码清单 12 中，流中的元素被转换成每隔 100 毫秒产生的数量不同的流，再进行合并。由于第一个流中包含的元素数量较少，所以在结果流中一开始是两个流的元素交织在一起，然后就只有第二个流中的元素。
     */
    public static void flat() {
        Flux.just(5, 10)
                .flatMap(x -> Flux.interval(Duration.ofMillis(x*10),Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并。与 flatMap 不同的是，concatMap 会根据原始流中的元素顺序依次把转换之后的流进行合并；与
     * flatMapSequential 不同的是，concatMap 对转换之后的流的订阅是动态进行的，而 flatMapSequential 在合并之前就已经订阅了所有的流。
     *
     * 代码清单 13 与代码清单 12 类似，只不过把 flatMap 换成了 concatMap，结果流中依次包含了第一个流和第二个流中的全部元素。
     */
    public static void concat(){
        Flux.just(5, 10)
                .concatMap(x -> Flux.interval(Duration.ofMillis(x*10),Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * combineLatest 操作符把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。只要其中任何一个流中产生了新的元素，合并操作就会被执行一次，结果流中就会产生新的元素。在 代码清单 14 中，流中最新产生的元素会被收集到一个数组中，通过 Arrays.toString 方法来把数组转换成 String。
     */
    public static void combine() {
        Flux.combineLatest(
                Arrays::toString,
                Flux.interval(Duration.ofMillis(100)).take(5),
                Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5)
        ).toStream().forEach(System.out::println);
    }

    /**
     * 当需要处理 Flux 或 Mono 中的消息时，如之前的代码清单所示，可以通过 subscribe 方法来添加相应的订阅逻辑。在调用 subscribe 方法时可以指定需要处理的消息类型。可以只处理其中包含的正常消息，也可以同时处理错误消息和完成消息。代码清单 15 中通过 subscribe()方法同时处理了正常消息和错误消息。
     * 正常的消息处理相对简单。当出现错误时，有多种不同的处理策略。第一种策略是通过 onErrorReturn()方法返回一个默认值。在代码清单 16 中，当出现错误时，流会产生默认值 0.
     * 第二种策略是通过 switchOnError()方法来使用另外的流来产生元素。在代码清单 17 中，当出现错误时，将产生 Mono.just(0)对应的流，也就是数字 0。
     * 第三种策略是通过 onErrorResumeWith()方法来根据不同的异常类型来选择要使用的产生元素的流。在代码清单 18 中，根据异常类型来返回不同的流作为出现错误时的数据来源。因为异常的类型为 IllegalArgumentException，所产生的元素为-1。
     * 当出现错误时，还可以通过 retry 操作符来进行重试。重试的动作是通过重新订阅序列来实现的。在使用 retry 操作符时可以指定重试的次数。代码清单 19 中指定了重试次数为 1，所输出的结果是 1，2，1，2 和错误信息。
     */
    public static void subscribe() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .switchIfEmpty(Mono.just(0))
                .subscribe(System.out::println);
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println);
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .retry(1)
                .subscribe(System.out::println);
    }

    /**
     * 前面介绍了反应式流和在其上可以进行的各种操作，通过调度器（Scheduler）可以指定这些操作执行的方式和所在的线程。有下面几种不同的调度器实现。
     *
     * 当前线程，通过 Schedulers.immediate()方法来创建。
     * 单一的可复用的线程，通过 Schedulers.single()方法来创建。
     * 使用弹性的线程池，通过 Schedulers.elastic()方法来创建。线程池中的线程是可以复用的。当所需要时，新的线程会被创建。如果一个线程闲置太长时间，则会被销毁。该调度器适用于 I/O 操作相关的流的处理。
     * 使用对并行操作优化的线程池，通过 Schedulers.parallel()方法来创建。其中的线程数量取决于 CPU 的核的数量。该调度器适用于计算密集型的流的处理。
     * 使用支持任务调度的调度器，通过 Schedulers.timer()方法来创建。
     * 从已有的 ExecutorService 对象中创建调度器，通过 Schedulers.fromExecutorService()方法来创建。
     * 某些操作符默认就已经使用了特定类型的调度器。比如 intervalMillis()方法创建的流就使用了由 Schedulers.timer()创建的调度器。通过 publishOn()和 subscribeOn()
     * 方法可以切换执行操作的调度器。其中 publishOn()方法切换的是操作符的执行方式，而 subscribeOn()方法切换的是产生流中元素时的执行方式。
     *
     * 在代码清单 20 中，使用 create()方法创建一个新的 Flux 对象，其中包含唯一的元素是当前线程的名称。接着是两对 publishOn()和 map()
     * 方法，其作用是先切换执行时的调度器，再把当前的线程名称作为前缀添加。最后通过 subscribeOn()方法来改变流产生时的执行方式。运行之后的结果是[elastic-2] [single-1]
     * parallel-1。最内层的线程名字 parallel-1 来自产生流中元素时使用的 Schedulers.parallel()调度器，中间的线程名称 single-1 来自第一个 map 操作之前的
     * Schedulers.single()调度器，最外层的线程名字 elastic-2 来自第二个 map 操作之前的 Schedulers.elastic()调度器。
     */
    public static void scheduler() {
        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        })
                .publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel())
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * 进行测试时的一个典型的场景是对于一个序列，验证其中所包含的元素是否符合预期。StepVerifier 的作用是可以对序列中包含的元素进行逐一验证。在代码清单 21 中，需要验证的流中包含 a 和 b 两个元素。通过 StepVerifier.create()方法对一个流进行包装之后再进行验证。expectNext()方法用来声明测试时所期待的流中的下一个元素的值，而 verifyComplete()方法则验证流是否正常结束。类似的方法还有 verifyError()来验证流由于错误而终止。
     *
     *有些序列的生成是有时间要求的，比如每隔 1 分钟才产生一个新的元素。在进行测试中，不可能花费实际的时间来等待每个元素的生成。此时需要用到 StepVerifier 提供的虚拟时间功能。通过 StepVerifier
     * .withVirtualTime()方法可以创建出使用虚拟时钟的 StepVerifier。通过 thenAwait(Duration)方法可以让虚拟时钟前进。
     *
     * 在代码清单 22 中，需要验证的流中包含两个产生间隔为一天的元素，并且第一个元素的产生延迟是 4 个小时。在通过 StepVerifier.withVirtualTime()方法包装流之后，expectNoEvent()
     * 方法用来验证在 4 个小时之内没有任何消息产生，然后验证第一个元素 0 产生；接着 thenAwait()方法来让虚拟时钟前进一天，然后验证第二个元素 1 产生；最后验证流正常结束。
     *
     * TestPublisher 的作用在于可以控制流中元素的产生，甚至是违反反应流规范的情况。在代码清单 23 中，通过 create()方法创建一个新的 TestPublisher 对象，然后使用 next()方法来产生元素，使用 complete()方法来结束流。TestPublisher 主要用来测试开发人员自己创建的操作符。
     *
     *
     */
    public static void test() {

        StepVerifier.create(Flux.just("a", "b"))
                .expectNext("a")
                .expectNext("b")
                .verifyComplete();

        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofHours(4), Duration.ofDays(1)).take(2))
                .expectSubscription()
                .expectNoEvent(Duration.ofHours(4))
                .expectNext(0L)
                .thenAwait(Duration.ofDays(1))
                .expectNext(1L)
                .verifyComplete();


        final TestPublisher<String> testPublisher = TestPublisher.create();
        testPublisher.next("a");
        testPublisher.next("b");
        testPublisher.complete();

        StepVerifier.create(testPublisher)
                .expectNext("a")
                .expectNext("b")
                .expectComplete();
    }


    /**
     *
     * 由于反应式编程范式与传统编程范式的差异性，使用 Reactor 编写的代码在出现问题时比较难进行调试。为了更好的帮助开发人员进行调试，Reactor 提供了相应的辅助功能。
     * 当需要获取更多与流相关的执行信息时，可以在程序开始的地方添加代码清单 24 中的代码来启用调试模式。在调试模式启用之后，所有的操作符在执行时都会保存额外的与执行链相关的信息。当出现错误时，这些信息会被作为异常堆栈信息的一部分输出。通过这些信息可以分析出具体是在哪个操作符的执行中出现了问题。
     *不过当调试模式启用之后，记录这些额外的信息是有代价的。一般只有在出现了错误之后，再考虑启用调试模式。但是当为了找到问题而启用了调试模式之后，之前的错误不一定能很容易重现出来。为了减少可能的开销，可以限制只对特定类型的操作符启用调试模式。
     *
     *另外一种做法是通过 checkpoint 操作符来对特定的流处理链来启用调试模式。代码清单 25 中，在 map 操作符之后添加了一个名为 test 的检查点。当出现错误时，检查点名称会出现在异常堆栈信息中。对于程序中重要或者复杂的流处理链，可以在关键的位置上启用检查点来帮助定位可能存在的问题。
     *
     * 在开发和调试中的另外一项实用功能是把流相关的事件记录在日志中。这可以通过添加 log 操作符来实现。在代码清单 26 中，添加了 log 操作符并指定了日志分类的名称。
     */
    public static void debug() {
        //Hooks.onOperator(providedHook -> providedHook.operatorStacktrace());

        Flux.just(1, 0).map(x -> 1 / x).checkpoint("test").subscribe(System.out::println);
        Flux.range(1, 2).log("Range").subscribe(System.out::println);

    }

    /**
     * “冷”与“热”序列
     * 之前的代码清单中所创建的都是冷序列。冷序列的含义是不论订阅者在何时订阅该序列，总是能收到序列中产生的全部消息。而与之对应的热序列，则是在持续不断地产生消息，订阅者只能获取到在其订阅之后产生的消息。
     *
     * 在代码清单 28 中，原始的序列中包含 10 个间隔为 1 秒的元素。通过 publish()方法把一个 Flux 对象转换成 ConnectableFlux 对象。方法 autoConnect()的作用是当
     * ConnectableFlux 对象有一个订阅者时就开始产生消息。代码 source.subscribe()的作用是订阅该 ConnectableFlux 对象，让其开始产生数据。接着当前线程睡眠 5
     * 秒钟，第二个订阅者此时只能获得到该序列中的后 5 个元素，因此所输出的是数字 5 到 9。
     */
    public static void hotSubscribe() throws InterruptedException {
        final Flux<Long> source = Flux.interval(Duration.ofMillis(1000))
                .take(10)
                .publish()
                .autoConnect();
        source.subscribe();
        Thread.sleep(5000);
        source
                .toStream()
                .forEach(System.out::println);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("simple & Static Flux");
        simpleStaticFlux();
        System.out.println("generate flux");
        generateFlux();
        System.out.println("create flux");
        createFlux();
        System.out.println("buffer flux");
        bufferFlux();
        System.out.println("filter flux");
        filterFlux();
        System.out.println("window flux");
        windowFlux();
        System.out.println("simple mono");
        simpleMono();
        System.out.println("zip flux");
        zipFlux();
        System.out.println("reduce");
        reduce();
        System.out.println("take");
        take();
        System.out.println("merge");
        merge();
        System.out.println("flat");
        flat();
        System.out.println("concat");
        concat();
        System.out.println("combine");
        combine();

        System.out.println("subscribe");
        subscribe();
        System.out.println("scheduler");
        scheduler();

        System.out.println("test");
        test();

        System.out.println("debug");
        debug();

        System.out.println("hot sequence");
        hotSubscribe();
    }
}
