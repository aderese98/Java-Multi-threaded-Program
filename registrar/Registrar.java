/*Name: Abraham Derese
 * UID: 114725752
 * Directory ID: aderese
 * Section: 0102
 * Honor Pledge:I pledge on my honor that I have not given or received any 
 * unauthorized assistance on this assignment.
 * - Abraham Derese
 */
package registrar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

  // The Registrar class holds information on a Registrar object, which will 
  // have 3 fields HashMap studentMap, HashMap courseMap and static 
  // int MAX_COURSES. There are also 2 shared variables filesMap and counter
  // that work with threads in registering courses and students from files
  
  // Each object will have a public constructor that takes 1 @param that stores 
  // an integer value into MAX_COURSES and inititalizes the student and 
  // course maps
  
  // A registrarobject will have 11 public methods capable of adding, removing 
  // and displaying information on the current Registrar object. A Registrar
  // object holds information on a Registrar with available courses and seats
  // as well as students registered for those courses.
public class Registrar implements Runnable {

  // A studentMap holds a HashMap of Student Object keys and a HashSet<Course>
  // values for each student.
  private Map<Student, Set<Course>> studentMap;

  // A courseMap holds a HashMap of Course Object keys with Integer values that
  // contain the current seat size of the key Course.
  private Map<Course, Integer> courseMap;

  // MAX_COURSES holds the maximum number of courses a student can register for.
  private static int MAX_COURSES;

  // Shared variables filesMap and counter handle reading a file when threads
  // are called in doRegistration()
  private Map<Integer, String> filesMap = new HashMap<Integer, String>();
  private int counter;

  // The Registrar constructor takes 1 @param for the maximum number of courses
  // a student can register for and initializes the student and course maps
  // upon creation.
  public Registrar(int maxCoursesPerStudent) {
    studentMap = new HashMap<Student, Set<Course>>();
    courseMap = new HashMap<Course, Integer>();
    MAX_COURSES = maxCoursesPerStudent;
    if (maxCoursesPerStudent < 1)
      MAX_COURSES = 1;
  }

  // The addNewCourse method accepts 3 @param and will @return a reference to
  // the current Registrar object.

  // It throws an IllegalArgumentException if @param department is null.

  // If @param number and numSeats are greater than 0 then the course will be
  // added to the courseMap of courses and the current object Registar
  // will @return.

  // This method is used with multi-threading and is therefore
  // synchronized to prevent data races.
  public synchronized Registrar addNewCourse(String department, 
      int number, int numSeats) throws IllegalArgumentException {

    // Throw exception if department is null.
    if (department == null)
      throw new IllegalArgumentException();

    // Check if @param number and numSeats are greater than 0. Add course if so.
    if (number > 0 && numSeats > 0)
      courseMap.put(new Course(department, number), numSeats);

    return this;
  }

  // The cancelCourse method accepts 2 @param and will @return a boolean whether
  // or not the @param course has been removed.

  // It throws an IllegalArgumentException if department is null.

  // Checks if course with @param fields exists in current object's courseMap
  // and removes if true. It should also remove the course from any students
  // registered for that course.
  public boolean cancelCourse(String department, int number) 
      throws IllegalArgumentException {
    // Throw exception if department is null.
    if (department == null)
      throw new IllegalArgumentException();

    // Create Course object with @param fields and boolean to return
    Course courseToRemove = new Course(department, number);
    boolean removed = false;

    // Check if course exists and remove if it does
    if (courseMap.containsKey(courseToRemove)) {
      courseMap.remove(courseToRemove);
      removed = true;
    }

    return removed;
  }

  // The numCourses method accepts no @param and returns the size of the current
  // object's courseMap as the number of courses currently in the Registrar
  public int numCourses() {
    return courseMap.size();
  }

   // The addToCourse method accepts 4 @param and will add a student to the 
   // desired course. It will @return a boolean if the student 
   // was added to the course. 
  
   // It throws an IllegalArgumentException if either department, firstName or
   // lastName @params are null. 
  
   // A student will not be added to a course if they are: 
   // Already in the course, Course does not exist, Student has registered
   // for MAX_COURSES number of courses, Course has no more seats 
  
   // This method is used with multi-threading and is therefore synchronized 
   // to prevent data races.
  public synchronized boolean addToCourse(String department, int number, 
      String firstName, String lastName) throws IllegalArgumentException {
    
    // Throw exception if department or firstName or lastName is null
    if (department == null || firstName == null || lastName == null)
      throw new IllegalArgumentException();

    // Create added boolean to keep track of student being added
    boolean added = false;
    
    // Create temp Course and student object with @param data
    Student tempStudent = new Student(firstName, lastName);
    Course newCourse = new Course(department, number);

    if (courseMap.containsKey(newCourse))
      // Check if student exists in current registrar studentMap
      if (studentMap.containsKey(tempStudent)) {
        // If they exist check if they can have a course added and add if so
        if (addToCourseCheck(department, number, firstName, lastName)) {
          studentMap.get(tempStudent).add(newCourse);
          courseMap.put(newCourse, courseMap.get(newCourse) - 1);
          added = true;
        }
      }
      // If student does not exist in current registrar studentMap then added
      // the new student and create a new Set for their courses
      else if (!studentMap.containsKey(tempStudent) && 
                courseMap.get(newCourse) > 0) {
        // Create a newSet for student courses and newCourse object to hold the
        // existing course
        Set<Course> newSet = new HashSet<Course>();

        // Add course to set then add the student @param data and course set
        // to the studentMap
        newSet.add(newCourse);
        studentMap.put(tempStudent, newSet);
        courseMap.put(newCourse, courseMap.get(newCourse) - 1);
        added = true;
      }
    return added;
  }

   // The numStudentsInCourse method accepts 2 @param and will @return how many
   // students are in the @param course. 
  
   // It throws an IllegalArgumentException if department @param is null.
  
   // Otherwise, it will return the number of students in the studentMap have 
   // the course @param in their Set of courses registered   
  public int numStudentsInCourse(String department, int number) 
      throws IllegalArgumentException {
    
    // Throw exception if department is null
    if (department == null)
      throw new IllegalArgumentException();

    // Create fields for courseCount and tempCourse to hold @param fields
    int courseCount = 0;
    Course tempCourse = new Course(department, number);

    // Check if course exists in current object's Registrar.
    // For each student in the studentMap check if their set of courses
    // registered is equal to @param course values and increment course if so.
    if (courseMap.containsKey(tempCourse))
      for (Student s : studentMap.keySet()) {
        if (studentMap.get(s).contains(tempCourse))
          courseCount++;
      }
    else
      courseCount = -1;

    return courseCount;
  }

  // The numStudentsInCourseWithLastName method accepts 3 @param and will
  // @return the number of students in a course with the String @param lastName. 
  
  // It throws an IllegalArgumentException if department or lastName are null. 
  
  // It will search the studentMap for students with @param courses 
  // and if they have the @param lastName then increment studentCount if so.
  public int numStudentsInCourseWithLastName(String department, 
      int number, String lastName) throws IllegalArgumentException {
    
    // Throw exception if department or lastName is null
    if (department == null || lastName == null)
      throw new IllegalArgumentException();

    // Create studentCount field to hold how many students in course
    int studentCount = 0;
    
    // Create temp course object with @param fields
    Course tempCourse = new Course(department, number);

    // If course exists in registrar object, iterate through studentMap and look
    // for student with lastName @param and if their course HashSet contains
    // @param course
    if (courseMap.containsKey(tempCourse))
      for (Student s : studentMap.keySet()) {
        if (s.hasLastName(lastName) && studentMap.get(s).contains(tempCourse))
          studentCount++;
      }

    return studentCount;
  }


  // The isInCourse method accepts 4 @param fields and will @return a boolean 
  // if a the @param student is in @param course. 
  
  // It throws an IllegalArgumentException if department, 
  // firstName or lastName @param are null. 
  
  // It will first find the @param student in studentMap and check if that 
  // student's HashSet Course value contains @param course.
  public boolean isInCourse(String department, int number, 
      String firstName, String lastName) throws IllegalArgumentException {
    
    // Throw exception if department, firstName or lastName is null
    if (department == null || firstName == null || lastName == null)
      throw new IllegalArgumentException();

    // inCourse field holds if student is in course
    boolean inCourse = false;
    
    // Temp course object to hold @param course data
    Course tempCourse = new Course(department, number);
    
    // Temp student object to hold @param student data
    Student tempStudent = new Student(firstName, lastName);

    // If course and student exists, check if @param student has @param course
    if (courseMap.containsKey(tempCourse) && 
        studentMap.containsKey(tempStudent))
      for (Student s : studentMap.keySet()) {
        if (s.equals(tempStudent))
          if (studentMap.get(s).contains(tempCourse))
            inCourse = true;
      }

    return inCourse;
  }

  // The howManyCoursesTaking method takes 2 @param and will @return the 
  // number of courses they are taking. If the student exists in studentMap, 
  // it will get the size of their current HashSet course. 
  
  // It throws an IllegalArgumentException if firstName or lastName are null. 
  public int howManyCoursesTaking(String firstName, String lastName) 
      throws IllegalArgumentException {
    
    // Throw exception if firstName or lastName is null
    if (firstName == null || lastName == null)
      throw new IllegalArgumentException();

    // courseCount field holds count of courses
    int courseCount = 0;
    
    // Temp student object to hold @param student data
    Student tempStudent = new Student(firstName, lastName);

    // If student exists, set courseCount to their HashSet Course size.
    if (studentMap.containsKey(tempStudent))
      courseCount = studentMap.get(tempStudent).size();

    return courseCount;
  }

  // The dropCourse method accepts 4 @param and will return a boolean if @param
  // student is able to drop @param course. 
  
  // It throws IllegalArgumentException if department, firstName or lastName 
  // @params are null. 
  
  // It will remove the @param course from the studentMap HashSet Course 
  // value that corresponds.
  public boolean dropCourse(String department, int number, 
      String firstName, String lastName) throws IllegalArgumentException{
    
    // Throw exception if department, firstName or lastName is null
    if (department == null || firstName == null || lastName == null)
      throw new IllegalArgumentException();

    // dropped boolean stores whether a course was dropped
    boolean dropped = false;
    
    // Create temp course and student objects to hold @param data
    Course tempCourse = new Course(department, number);
    Student tempStudent = new Student(firstName, lastName);

    // If @param course and student exists in current object,
    // find student and remove course from their HashSet Course value.
    if (courseMap.containsKey(tempCourse) && 
        studentMap.containsKey(tempStudent)) {
      for (Student s : studentMap.keySet()) {
        if (s.equals(tempStudent) && studentMap.get(s).contains(tempCourse)) {
          studentMap.get(s).remove(tempCourse);
          
          // Update seat count of course student is dropped from
          courseMap.put(tempCourse, courseMap.get(tempCourse) + 1);
          dropped = true;
        }
      }
    }
    return dropped;
  }

  // The cancelRegistration method accepts 2 @param and will@return a boolean if 
  // the student was dropped from all their courses. 
  
  // It will throw an IllegalArgumentException if firstName or lastName are null 
  
  // It will check if @param student exists in studentMap and will remove that 
  // student from all their courses by removing them from the Map.
  public boolean cancelRegistration(String firstName, String lastName) 
      throws IllegalArgumentException {
    
    // Throw exception if firstName or lastName is null
    if (firstName == null || lastName == null)
      throw new IllegalArgumentException();

    // Removed tracks if student's registration was cancelled
    boolean removed = false;
    // Creates temp student object with @param student data.
    Student tempStudent = new Student(firstName, lastName);

    // Removes student from studentMap if they exist.
    if (studentMap.containsKey(tempStudent)) {
      // Update seat count for each course that student is removed from
      for (Course c : studentMap.get(tempStudent)) {
        courseMap.put(c, courseMap.get(c) + 1);
      }
      studentMap.remove(tempStudent);
      removed = true;
    }

    return removed;
  }

  // The doRegistrations method accepts one @param and handles adding courses
  // and student registrations based on the files passed into @param filenames. 
  
  // It throws an IllegalArgumentException if @param is null. 
  
  // This method will concurrently run an array of threads of size @param.size()
  // adding each line from the file. Each thread created will get its own file.
  public void doRegistrations(Collection<String> filenames) 
      throws IllegalArgumentException {
    // Check if filenames is null
    if (filenames == null)
      throw new IllegalArgumentException();
    
    // Create an array of threads of size filenames
    Thread[] threadsAr = new Thread[filenames.size()];

    // Create a counter k that assigns a key integer for each file in filenames
    // as it is stored as a value in filesMap.
    
    // This will provide the names of the files to be passed into each thread.
    int k = 0;
    for (String file : filenames) {
      filesMap.put(k, file);
      threadsAr[k] = new Thread(this);
      threadsAr[k].start();
      k++;
    }

    // Because all threads have been started concurrently, this will make sure
    // all threads are done before doRegistrations ends so other methods
    // in the Registrar object can be called safely. If not, other access
    // methods could be called before all lines are read and Registrations are
    // finished.
    try {
      for (Thread th : threadsAr)
        th.join();
      // Reset filesMap counter once threads finish for future doRegistration()
      // calls
      counter = 0;
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
    }
  }

  // The overrided run() method accepts no @param and will perform the function
  // of reading each line in one file stored in the shared filesMap. 
  
  // While multiple threads are being started, the method will make sure a
  // thread can only get access to one file to prevent data races and duplicate
  // registrations (although duplications will not happen with the way the
  // object is storing data).
  public void run() {
    // Create bufferedReader and lines field to read lines in a file.
    // name string will hold a filename from filesMap
    BufferedReader bufferedReader;
    String line = null, name = null;

    // filesMap will be the main shared field so it will be used to synchronize
    // the threads as they run concurrently
    synchronized (filesMap) {
      // The counter field will track which filename in the filesMap the
      // current thread will have access to. Once a thread gets a filename
      // stored into the name field, counter will increment making sure
      // no other thread will have access to that file
      if (counter <= filesMap.size()) {
        name = filesMap.get(counter);
        counter++;
      }

      // The thread will try to read the lines in the file using bufferedReader
      try {
        bufferedReader = new BufferedReader(new FileReader(name));

        // Each line will store the readLine() from bufferedReader and loop
        // if line is not null
        while ((line = bufferedReader.readLine()) != null) {
          // Each line will be parsed in its substring and processed accordingly
          // If the first 9 characters are "addcourse" then the synchronized
          // method addNewCourse will access the current Registrar object's
          // courseMap and add a course to the Registrar.
          if (line.substring(0, 9).equals("addcourse"))
            addNewCourse(line.substring(10, 14), 
                Integer.parseInt(line.substring(15, 18)),
                Integer.parseInt(line.substring(19, line.length())));

          // Otherwise, if the first 15 characters read "addregistration" then
          // the synchronized addToCourse method will access the current
          // Registrar object's studentMap field and add a student to the
          // desired course if it exists, reading the current line
          else if (line.substring(0, 15).equals("addregistration")) {
            String[] studentName = line.substring(25, line.length()).split(" ");
            addToCourse(line.substring(16, 20), 
                Integer.parseInt(line.substring(21, 24)), studentName[0],
                studentName[1]);
          }
        }

      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  // Private boolean method addToCourseCheck accepts 4 @param and will @return
  // true if a student is able to add a new class to their registration Set.
  // It throws an IllegalArgumentException if any @param is null
  private boolean addToCourseCheck(String department, int number, 
      String firstName, String lastName) throws IllegalArgumentException {
    
    // Check if department, firstName or lastName are null
    if (department == null || firstName == null || lastName == null)
      throw new IllegalArgumentException();

    // Return the check for all aspects of a student that need to be checked
    // prior to registering them for a course.
    return !isInCourse(department, number, firstName, lastName) && 
        courseMap.containsKey(new Course(department, number)) &&
        studentMap.get(new Student(firstName, lastName)).size() < MAX_COURSES &&
        courseMap.get(new Course(department, number)) > 0;
  }
}
