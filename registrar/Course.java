/*Name: Abraham Derese
 * UID: 114725752
 * Directory ID: aderese
 * Section: 0102
 * Honor Pledge:I pledge on my honor that I have not given or received any 
 * unauthorized assistance on this assignment.
 * - Abraham Derese
 */
package registrar;

// A public class that defines a Course object. A Course contains 3 fields
// courseDept, courseNum, and courseSeats. 

// It has a constructor which accepts all 3 fields. 

// It has 3 overriden methods for equals(), hashCode() and toString()
public class Course {
  // Private attributes to store a course's department and number
  private String courseDept;
  private int courseNum;

  // Constructor which accepts @param department and number
  Course(String department, int number) {
    courseDept = department;
    courseNum = number;
  }

  // Checks if current course object is completely or partially equal to another

  // Uses a course's department and number to check if they are equal.
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if (o == this)
      result = true;

    if (!(o instanceof Course))
      result = false;

    Course otherCourse = (Course) o;

    if (courseDept == null || otherCourse.courseDept == null)
      result = false;

    if (courseDept.equals(otherCourse.courseDept) && 
        courseNum == otherCourse.courseNum)
      result = true;

    return result;
  }

  // Uses current Course object's courseDept hashCode * courseNum
  // to derive a unique hashCode that only other objects of the same fields
  // will also hash to.
  @Override
  public int hashCode() {
    return (int) courseDept.hashCode() * courseNum;
  }

  public String toString() {
    return courseDept + courseNum;
  }

}
