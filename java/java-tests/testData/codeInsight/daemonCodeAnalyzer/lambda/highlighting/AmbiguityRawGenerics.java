interface I {
    void m();
}

interface I1 {
    int m();
}

interface I2 {
    String m();
}

interface I3<A> {
    A m();
}

class AmbiguityRawGenerics {

    void foo(I s) { }
    void foo(I1 s) { }
    void foo(I2 s) { }
    <Z> void foo(I3<Z> s) { }

    void bar() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        foo<error descr="Ambiguous method call: both 'AmbiguityRawGenerics.foo(I1)' and 'AmbiguityRawGenerics.foo(I2)' match">(()-> { throw new RuntimeException(); })</error>;
=======
        foo<error descr="Ambiguous method call: both 'AmbiguityRawGenerics.foo(I)' and 'AmbiguityRawGenerics.foo(I1)' match">(()-> { throw new RuntimeException(); })</error>;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }
}
