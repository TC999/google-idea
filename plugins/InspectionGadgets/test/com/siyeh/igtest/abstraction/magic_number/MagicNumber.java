package com.siyeh.igtest.abstraction.magic_number;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import java.util.List;
import java.util.ArrayList;
=======
import java.util.*;
import java.io.*;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

@Size(max = 15)
public class MagicNumber
{
    private static final int s_foo = 400;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    private int m_foo = -400;
    private static int s_foo2 = 400;
=======
    private int m_foo = <warning descr="Magic number '-400'">-400</warning>;
    private static int s_foo2 = <warning descr="Magic number '400'">400</warning>;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    private final int m_foo2 = -(-(400));
    private static final List s_set = new ArrayList(400);

    public static void main(String[] args)
    {
        final List set = new ArrayList(400);
        set.toString();
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MagicNumber magicNumber = (MagicNumber) o;

        if (m_foo != magicNumber.m_foo) return false;
        if (m_foo2 != magicNumber.m_foo2) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = m_foo;
        result = 29 * result + m_foo2;
        return result;
    }

    void foo() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
      final int value = 101 * 55;
=======
      final int value = <warning descr="Magic number '101'">101</warning> * <warning descr="Magic number '55'">55</warning>;
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream(756);
    public static final long ONE_HOUR_OF_MILLISECONDS = 1000L * 60L * 60L;
    void waitOneHour() throws Exception {
      Thread.sleep(<warning descr="Magic number '1000L'">1000L</warning> * <warning descr="Magic number '60L'">60L</warning> * <warning descr="Magic number '60L'">60L</warning>);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }
}
@interface Size {
  int max();
}
