/*Name: Abraham Derese
 * UID: 114725752
 * Directory ID: aderese
 * Section: 0102
 * Honor Pledge:I pledge on my honor that I have not given or received any 
 * unauthorized assistance on this assignment.
 * - Abraham Derese
 */
package tests;

import org.junit.*;
import registrar.Registrar;

import static org.junit.Assert.*;

import java.util.Arrays;

public class StudentTests {

  // Tests adding a student when Registrar contructor parameter is 0.
  // Should make max courses 1
  @Test
  public void studentTest1() {
    Registrar registrar = new Registrar(0);

    registrar.addNewCourse("CMSC", 131, 10);
    registrar.addNewCourse("CMSC", 132, 10);

    assertEquals(2, registrar.numCourses());
    assertTrue(registrar.addToCourse("CMSC", 131, "John", "Doe"));
    assertFalse(registrar.addToCourse("CMSC", 132, "John", "Doe"));
  }

  // Test exception calls on null @param
  @Test
  public void studentTest2() {
    Registrar r = new Registrar(1);

    try {
      r.addToCourse(null, 0, null, null);
      fail();
    } catch (IllegalArgumentException iae) {
    }
    try {
      r.cancelCourse(null, 0);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    try {
      r.addToCourse(null, 0, null, null);
      fail();
    } catch (IllegalArgumentException iae) {
    }
    try {
      r.numStudentsInCourse(null, 0);
      fail();
    } catch (IllegalArgumentException iae) {
    }
    try {
      r.numStudentsInCourseWithLastName(null, 0, null);
      fail();
    } catch (IllegalArgumentException iae) {
    }
    try {
      r.isInCourse(null, 0, null, null);
      fail();
    } catch (IllegalArgumentException iae) {
    }
    try {
      r.howManyCoursesTaking(null, null);
      fail();
    } catch (IllegalArgumentException iae) {
    }
    try {
      r.dropCourse(null, 0, null, null);
      fail();
    } catch (IllegalArgumentException iae) {
    }
    try {
      r.cancelRegistration(null, null);
      fail();
    } catch (IllegalArgumentException iae) {
    }
    try {
      r.doRegistrations(null);
      ;
      fail();
    } catch (IllegalArgumentException iae) {
    }
  }

  // Tests adding a student when Registrar contructor parameter is 0. Should
  // make max courses 1
  @Test
  public void studentTest3() {
    Registrar registrar = new Registrar(0);

    registrar.addNewCourse("CMSC", 131, 10);
    registrar.addNewCourse("CMSC", 132, 10);

    assertEquals(2, registrar.numCourses());
    assertTrue(registrar.addToCourse("CMSC", 131, "John", "Doe"));
    assertFalse(registrar.addToCourse("CMSC", 132, "John", "Doe"));
  }

  // Tests capitalization in comparing students
  @Test
  public void studentTest4() {
    Registrar registrar = new Registrar(5);

    registrar.addNewCourse("CMSC", 131, 10);

    assertEquals(1, registrar.numCourses());
    assertTrue(registrar.addToCourse("CMSC", 131, "John", "Doe"));
    assertTrue(registrar.addToCourse("CMSC", 131, "JOHN", "Doe"));
    assertTrue(registrar.addToCourse("CMSC", 131, "JOHN", "DOE"));
    assertTrue(registrar.addToCourse("CMSC", 131, "John", "DOE"));
    assertTrue(registrar.addToCourse("CMSC", 131, "john", "doe"));

    assertEquals(5, registrar.numStudentsInCourse("CMSC", 131));

  }

  // Tests calling methods before doRegistrations(). Should fail
  @Test
  public void studentTest5() {
    Registrar registrar = new Registrar(5);

    assertFalse(registrar.isInCourse("CMSC", 131, "Ellie", "Elephant"));
    registrar.doRegistrations(Arrays.asList("registrationdata-public07a"));
    assertTrue(registrar.isInCourse("CMSC", 132, "Ellie", "Elephant"));
  }

  // Tests calling doRegistrations() on multiple calls with the same
  // registrar and calling methods in between
  @Test
  public void studentTest6() {
    Registrar registrar = new Registrar(5);

    registrar.doRegistrations(Arrays.asList("registrationdata-public07a"));
    assertTrue(registrar.isInCourse("CMSC", 132, "Ellie", "Elephant"));
    assertTrue(registrar.isInCourse("PSYC", 100, "Manny", "Manatee"));
    assertFalse(registrar.isInCourse("STAT", 400, "Sally", "Salamander"));
    assertTrue(registrar.cancelCourse("CMSC", 132));

    registrar.doRegistrations(Arrays.asList("registrationdata-public07b"));
    assertTrue(registrar.isInCourse("STAT", 400, "Sally", "Salamander"));
    assertTrue(registrar.isInCourse("ITAL", 207, "Wally", "Walrus"));
  }

  // Tests calling doRegistrations() on multiple calls with the same
  // registrar and that courses are incremented accordingly
  @Test
  public void studentTest7() {
    Registrar registrar = new Registrar(5);

    registrar.doRegistrations(Arrays.asList("registrationdata-public07a"));
    assertEquals(5, registrar.numCourses());

    registrar.doRegistrations(Arrays.asList("registrationdata-public07b"));
    assertEquals(10, registrar.numCourses());

    registrar.doRegistrations(Arrays.asList("registrationdata-public09a"));
    assertEquals(30, registrar.numCourses());
  }

  // Tests calling doRegistrations() on a very large list of files.
  @Test
  public void studentTest8() {
    Registrar registrar = new Registrar(5);

    registrar.doRegistrations(Arrays.asList("registrationdata-public10a", 
        "registrationdata-public10b",
        "registrationdata-public10c", "registrationdata-public10d", 
        "registrationdata-public10e",
        "registrationdata-public10f", "registrationdata-public10g", 
        "registrationdata-public10h",
        "registrationdata-public10i", "registrationdata-public10j"));
    assertEquals(40, registrar.numCourses());

  }

}
