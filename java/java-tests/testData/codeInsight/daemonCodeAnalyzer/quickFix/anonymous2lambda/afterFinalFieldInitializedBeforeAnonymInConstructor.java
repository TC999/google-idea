// "Replace with lambda" "true"
class HelloLambda {
  final int x;

  HelloLambda() {
    x = 1;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    Runnable r = () -> {
      System.out.println(x);

    };
=======
    Runnable r = () -> System.out.println(x);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  }


}
