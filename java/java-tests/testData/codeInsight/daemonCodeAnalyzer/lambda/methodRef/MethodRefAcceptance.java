class Test {
    interface IFactory {
        Object m();
    }

    @interface Anno {}

    enum E {}

    interface I {}

    static class Foo<X> { }

    static abstract class ABar {
      protected ABar() {
      }
    }

    static abstract class ABaz {
    }

    void foo(IFactory cf) { }

    void testAssign() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        <error descr="Incompatible types. Found: '<method reference>', required: 'Test.IFactory'">IFactory c1 = Anno::new;</error>
        <error descr="Incompatible types. Found: '<method reference>', required: 'Test.IFactory'">IFactory c2 = E::new;</error>
        <error descr="Incompatible types. Found: '<method reference>', required: 'Test.IFactory'">IFactory c3 = I::new;</error>
=======
        IFactory c1 = <error descr="'Anno' is abstract; cannot be instantiated">Anno::new</error>;
        IFactory c2 = <error descr="Enum types cannot be instantiated">E::new</error>;
        IFactory c3 = <error descr="'I' is abstract; cannot be instantiated">I::new</error>;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
        IFactory c4 = <error descr="Unexpected wildcard">Foo<?></error>::new;
        IFactory c5 = <error descr="Cannot find class 1">1</error>::new;
        IFactory c6 = <error descr="'ABar' is abstract; cannot be instantiated">ABar::new</error>;
        IFactory c7 = <error descr="'ABaz' is abstract; cannot be instantiated">ABaz::new</error>;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        foo<error descr="'foo(Test.IFactory)' in 'Test' cannot be applied to '(<method reference>)'">(Anno::new)</error>;
        foo<error descr="'foo(Test.IFactory)' in 'Test' cannot be applied to '(<method reference>)'">(E::new)</error>;
        foo<error descr="'foo(Test.IFactory)' in 'Test' cannot be applied to '(<method reference>)'">(I::new)</error>;
=======
        foo(<error descr="'Anno' is abstract; cannot be instantiated">Anno::new</error>);
        foo(<error descr="Enum types cannot be instantiated">E::new</error>);
        foo(<error descr="'I' is abstract; cannot be instantiated">I::new</error>);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
        foo(<error descr="Unexpected wildcard">Foo<?></error>::new);
        foo(<error descr="Cannot find class 1">1</error>::new);
        foo(<error descr="'ABar' is abstract; cannot be instantiated">ABar::new</error>);
        foo(<error descr="'ABaz' is abstract; cannot be instantiated">ABaz::new</error>);
    }
}
