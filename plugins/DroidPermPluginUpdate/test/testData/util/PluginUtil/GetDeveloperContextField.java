class A {
    private int a = 1;
    private static Context aContext;
    private int b = 2;
    private static Context bContext;
    private int c = 3;
    private static Context cContext;

    void foo(){
        sens<caret>itive();
    }
}