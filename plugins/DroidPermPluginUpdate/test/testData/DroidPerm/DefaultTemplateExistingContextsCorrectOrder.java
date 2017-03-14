class A {
    private Context myFieldContext;
    void foo(Context myParameterContext) {
        Context myLocalContext = getContext();
		<selection>sensitive();</selection>
    }
}