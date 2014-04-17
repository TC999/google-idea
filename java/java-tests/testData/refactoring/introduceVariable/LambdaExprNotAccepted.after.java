interface SAM<X> {
        X m(int i, int j);
    }

class Foo {  
    void test() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        SAM<X> c = (i, j) -> "" + i + j;
=======
        SAM<Integer> c = (i, j) -> "" + i + j;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
        SAM<Integer> s3 = m(c);
    }
    <X> SAM<X> m(SAM<X> s) { return null; }
}
