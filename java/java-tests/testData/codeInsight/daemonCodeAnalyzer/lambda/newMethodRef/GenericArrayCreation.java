import java.util.List;

class Test {

    interface I {
        Test m(List<Integer> l1, List<Integer> l2);
    }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    static Test meth(List<Integer>... <warning descr="Parameter 'lli' is never used">lli</warning>) {
        return null;
    }

    Test(List<Integer>... <warning descr="Parameter 'lli' is never used">lli</warning>) {}
=======
    static Test meth(List<Integer>... lli) {
        return null;
    }

    Test(List<Integer>... lli) {}
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

    {
        I <warning descr="Variable 'i1' is never used">i1</warning> = <warning descr="Unchecked generics array creation for varargs parameter">Test::meth</warning>;
        I <warning descr="Variable 'i2' is never used">i2</warning> = <warning descr="Unchecked generics array creation for varargs parameter">Test::new</warning>;
    }
}
