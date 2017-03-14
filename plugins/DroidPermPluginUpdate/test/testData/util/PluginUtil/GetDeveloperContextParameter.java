class A {
    void foo(Context aContext) {
		<selection>sensitive();</selection>
    }

    void foofoo(int a, Context bContext) {
        a = 5;
        sensitive();
    }
}