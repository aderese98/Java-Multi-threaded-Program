package registrar;

// A public class that defines a Student object. A Student contains 2 fields
// studentFirst and studentLast holding their first and last names. 

// It has a constructor which accepts both fields.

// There are 3 overriden methods equals(), hashCode() and toString().
public class Student {
  // Student attributes to hold a student's first and last name individually.
  private String studentFirst, studentLast;

  // Student constructor accepts 2 @param fName and lName
  Student(String fName, String lName) {
    studentFirst = fName;
    studentLast = lName;
  }

  // The public method hasLastName accepts 1 @param and checks if it is
  // equal to the current student object's studentLast field.
  public boolean hasLastName(String lastName) {
    return studentLast.equals(lastName);
  }

  // Checks if student object is completely or partially equal to another
  // Uses a Student object's studentFirst and studentLast parameters to
  // check if they are equal.
  public boolean equals(Object s) {
    boolean result = false;
    if (s == this)
      result = true;

    if (!(s instanceof Student))
      result = false;

    Student otherStudent = (Student) s;

    if ((studentFirst == null || studentLast == null) || 
        (otherStudent.studentFirst == null || 
        otherStudent.studentLast == null))
      result = false;

    if (studentFirst.equals(otherStudent.studentFirst) && 
        studentLast.equals(otherStudent.studentLast))
      result = true;

    return result;
  }

  // Provides a unique hashCode based on the default hashCode value of
  // the current Student object's studentFirst and studentLast fields.
  @Override
  public int hashCode() {
    return (int) studentFirst.hashCode() + studentLast.hashCode();
  }

  @Override
  public String toString() {
    return studentFirst + " " + studentLast;
  }
}
