/**
 * Actor.java
 * Maddie Kyhl and Rie Kurita, Carleton College, 2018-01-10
 *
 * CS201 ASSIGNMENT [Sorted People]
 */

    public class Actor {
      private String Name;
      private int yearOfBirth;
      private int yearOfDeath;
      private String[] actedMovies;

      public Actor(String name, int birth, int death) {
         this.Name = name;
         this.yearOfBirth = birth;
         this.yearOfDeath = death;
      }

      public String getName() {
        String fullName = "";
        if (givenName.length() == 0){
          fullName = (familyName + " ");
        } else {
        fullName = (givenName + " " + familyName + " ");
       }
         return fullName;
      }

      public String getFamilyName() {
          return familyName;
      }

      public String getGivenName() {
          return givenName;
      }
  }
