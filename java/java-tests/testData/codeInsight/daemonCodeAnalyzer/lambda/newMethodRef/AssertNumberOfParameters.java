class Test {
    interface I {
        void m(Integer x1, Integer x2, Integer x3);
    }

    static class Foo {
       static void foo() {}
    }

    <T> void bar(I i) {}

    void test() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        bar<error descr="'bar(Test.I)' in 'Test' cannot be applied to '(<method reference>)'">(Foo::foo)</error>;
=======
        bar(Foo::<error descr="Cannot resolve method 'foo'">foo</error>);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }
}
