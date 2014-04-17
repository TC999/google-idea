// "Replace with lambda" "true"
class HelloLambda {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  private final Runnable r = () -> {
    System.out.println(x);
  };
=======
  private final Runnable r = () -> System.out.println(x);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  private static int x = 0;
}
