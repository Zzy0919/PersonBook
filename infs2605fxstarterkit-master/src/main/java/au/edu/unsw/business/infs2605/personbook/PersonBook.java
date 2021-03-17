package au.edu.unsw.business.infs2605.personbook;

public class PersonBook {

    private int id;

    private String fullName;

    private String birthDay;

    private String birthYear;

    private String isImportantPersonal;

    private String isImportantBusiness;

    PersonBook(int id, String fullName, String birthDay, String birthYear, String isImportantPersonal,
               String isImportantBusiness){
        this.id = id;
        this.fullName = fullName;
        this.birthDay = birthDay;
        this.birthYear = birthYear;
        this.isImportantBusiness = isImportantBusiness;
        this.isImportantPersonal = isImportantPersonal;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public void setBirthDay(String birthDay){
        this.birthDay = birthDay;
    }

    public void setBirthYear(String birthYear){
        this.birthYear = birthYear;
    }

    public void setIsImportantPersonal(String isImportantPersonal){
        this.isImportantPersonal = isImportantPersonal;
    }

    public void setIsImportantBusiness(String isImportantBusiness){
        this.isImportantBusiness = isImportantBusiness;
    }

    public int getId(){
        return this.id;
    }

    public String getFullName(){
        return this.fullName;
    }

    public String getBirthDay(){
        return this.birthDay;
    }

    public String getBirthYear(){
        return this.birthYear;
    }

    public String getIsImportantPersonal(){
        return this.isImportantPersonal;
    }

    public String getIsImportantBusiness(){
        return this.isImportantBusiness;
    }


}
