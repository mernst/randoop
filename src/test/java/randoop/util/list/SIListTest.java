package randoop.util;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;

/** Test (mainly) to make sure iterators over simple lists work. */
public class SIListTest {

  @Test
  public void testArrayList() {
    ArrayList<String> al = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      al.add("str" + i);
    }

    SIList<String> sl = SIList.fromList(al);

    for (int i = 0; i < sl.size(); i++) {
      assertTrue(al.contains(sl.get(i)));
    }
  }

  @Test
  public void oneMoreElement() {
    ArrayList<String> al = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      al.add("str" + i);
    }

    SIList<String> sl = SIList.fromList(al).add("str" + 100);

    al.add("str" + 100);

    for (int i = 0; i < sl.size(); i++) {
      assertTrue(al.contains(sl.get(i)));
    }
  }

  @Test
  public void listOfList() {
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> sub = new ArrayList<>();
    List<SIList<String>> lists = new ArrayList<>();

    Set<Integer> partitions = new TreeSet<>();
    int sum = 0;
    while (sum < 100) {
      sum += (int) (Math.random() * 47);
      partitions.add(sum);
    }

    for (int i = 0; i < 100; i++) {
      if (partitions.contains(i)) {
        lists.add(SIList.fromList(sub));
        sub = new ArrayList<>();
      }
      String str = "str" + i;
      al.add(str);
      sub.add(str);
    }

    if (!sub.isEmpty()) {
      lists.add(SIList.fromList(sub));
    }

    SIList<String> sl = SIList.concat(lists);

    for (int i = 0; i < sl.size(); i++) {
      assertTrue(al.contains(sl.get(i)));
    }
  }

  @Test
  public void listOfMixed() {

    List<SIList<String>> lists = new ArrayList<>();
    ArrayList<String> al = new ArrayList<>();

    SIList<String> base = SIList.fromList(new ArrayList<String>());

    int i;
    for (i = 0; i < 50; i++) {
      String v = "str" + i;
      base = base.add(v);
      al.add(v);
    }
    lists.add(base);
    lists.add(SIList.fromList(new ArrayList<String>()));
    base = SIList.concat(lists);
    for (i = 55; i < 70; i++) {
      String v = "str" + i;
      base = base.add(v);
      al.add(v);
    }

    for (int j = 0; i < base.size(); j++) {
      assertTrue(al.contains(base.get(j)));
    }
  }

  @Test
  public void emptyLOL() {
    List<SIList<String>> lists =
        Collections.singletonList(SIList.fromList(new ArrayList<String>()));
    SIList<String> sl = SIList.concat(lists);

    assertTrue(sl.isEmpty());
  }
}
